/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.twitter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import jakarta.json.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Parses JSON from twitter search REST API using object model API.
 * JSON would like :
 *
 * {
 "   statuses": [
 *     { ..., "user" : { "name" : "xxx", ...}, "text: "yyy", ... },
 *     { ..., "user" : { "name" : "ppp", ...}, "text: "qqq", ... },
 *     ...
 *   ],
 *   ...
 * }
 *
 * This codes writes the tweets to output as follows:
 * xxx: yyy
 * --------
 * ppp: qqq
 * --------
 *
 * @author Jitendra Kotamraju
 */
public class TwitterObjectSearch {

    public static void main(String... args) throws Exception {
        try (InputStream is = getSearchStream();
             JsonReader rdr = Json.createReader(is)) {

            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("statuses");
            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                System.out.print(result.getJsonObject("user").getString("name", "anonymous"));
                System.out.print(": ");
                System.out.println(result.get("text"));
                System.out.println("-----------");
            }

//            All the tweets are collected into Stream<String> and printed
//            obj.getJsonArray("statuses").getValuesAs(JsonObject.class)
//                    .stream()
//                    .map(v -> v.getString("text"))
//                    .forEach(s -> { System.out.println(s); } );
        }
    }

    static InputStream getSearchStream() throws Exception {
        final String searchStr = "#javaone";
        String searchUrl = "https://api.twitter.com/1.1/search/tweets.json";

        Properties config = new Properties();
        config.load(TwitterObjectSearch.class.getResourceAsStream(
                "/twitterconfig.properties"));

        final String consumerKey = (String)config.get("consumer-key");
        final String consumerSecret = (String)config.get("consumer-secret");
        final String accessToken = (String)config.get("access-token");
        final String accessTokenSecret = (String)config.get("access-token-secret");
        final int timestamp = (int)(System.currentTimeMillis()/1000);

        Map<String, String> map = new TreeMap<String, String>() {{
            put("count", "100");
            put("oauth_consumer_key", consumerKey);
            put("oauth_nonce", "4b25256957d75b6370f33a4501dc5e7e"); // TODO
            put("oauth_signature_method", "HMAC-SHA1");
            put("oauth_timestamp", ""+timestamp);
            put("oauth_token", accessToken);
            put("oauth_version", "1.0");
            put("q", searchStr);
        }};

        // Builds param string
        StringBuilder paramsBuilder = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> e : map.entrySet()) {
            if (!first) {
                paramsBuilder.append('&');
            }
            first = false;
            paramsBuilder.append(e.getKey());
            paramsBuilder.append("=");
            paramsBuilder.append(URLEncoder.encode(e.getValue(), "UTF-8"));
        }
        String paramsString = paramsBuilder.toString();

        // builds signature string
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append("GET");
        signatureBuilder.append('&');
        signatureBuilder.append(URLEncoder.encode(searchUrl, "UTF-8"));
        signatureBuilder.append('&');
        signatureBuilder.append(URLEncoder.encode(paramsString, "UTF-8"));
        String signatureBasedString = signatureBuilder.toString();

        // Create authorization signature
        Mac m = Mac.getInstance("HmacSHA1");
        m.init(new SecretKeySpec((consumerSecret+"&"+accessTokenSecret).getBytes(), "HmacSHA1"));
        m.update(signatureBasedString.getBytes());
        byte[] res = m.doFinal();
        final String oauthSig = URLEncoder.encode(DatatypeConverter.printBase64Binary(res), "UTF-8");
        map.put("oauth_signature", oauthSig);
        map.remove("count");
        map.remove("q");

        // Build Authorization header
        StringBuilder authorizationBuilder = new StringBuilder();
        authorizationBuilder.append("OAuth ");
        first = true;
        for(Map.Entry<String, String> e : map.entrySet()) {
            if (!first) {
                authorizationBuilder.append(',');
                authorizationBuilder.append(' ');
            }
            first = false;
            authorizationBuilder.append(e.getKey());
            authorizationBuilder.append('=');
            authorizationBuilder.append('"');
            authorizationBuilder.append(e.getValue());
            authorizationBuilder.append('"');
        }

        // Gets the search stream
        URL url = new URL(searchUrl+"?q="+URLEncoder.encode(searchStr, "UTF-8")+
                "&count=100");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.addRequestProperty("Authorization", authorizationBuilder.toString());
        return con.getInputStream();
    }

}

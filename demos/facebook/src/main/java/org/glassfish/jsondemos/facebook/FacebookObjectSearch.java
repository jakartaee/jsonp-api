/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.facebook;

import javax.json.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

/**
 * Parses JSON from facebook graph API using object model API.
 * JSON would like :
 *
 * {
 *   data: [
 *     { "from" : { "name" : "xxx", ... }, "message: "yyy", ... },
 *     { "from" : { "name" : "ppp", ... }, "message: "qqq", ... },
 *      ...
 *   ],
 *   ...
 * }
 *
 * This codes writes the facebook posts to output as follows:
 * xxx: yyy
 * --------
 * ppp: qqq
 * --------
 *
 * @author Jitendra Kotamraju
 */
public class FacebookObjectSearch {

    public static void main(String... args) throws Exception {
        try (InputStream is = getSearchStream();
             JsonReader rdr = Json.createReader(is)) {

            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");
            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                JsonValue value = result.get("from");
                if (value != null && value instanceof JsonObject) {
                    System.out.print(((JsonObject)value).getString("name", "anon"));
                }
                System.out.print(": ");
                System.out.println(result.getString("message", ""));
                System.out.println("-----------");
            }
        }
    }

    static InputStream getSearchStream() throws Exception {
        Properties config = new Properties();
        config.load(FacebookObjectSearch.class.getResourceAsStream(
                "/facebookconfig.properties"));
        final String accessToken = (String)config.get("access_token");

        // Gets the search stream
        String searchUrl = "https://graph.facebook.com/search?q=tamil&type=post&access_token=";
        URL url = new URL(searchUrl+accessToken);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        return con.getInputStream();
    }

}

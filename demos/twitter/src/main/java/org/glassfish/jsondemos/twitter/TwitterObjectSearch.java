/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package org.glassfish.jsondemos.twitter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.json.*;
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
 * TODO update
 * ... { ... "from_user" : "xxx", ..., "text: "yyy", ... } ...
 *
 * This codes writes the tweets to output as follows:
 * xxx: yyy
 * --------
 *
 * @author Jitendra Kotamraju
 */
public class TwitterObjectSearch {
    private static final String searchHash = "java";

    public static void main(String... args) throws Exception {

        URL url = new URL("https://api.twitter.com/1.1/search/tweets.json?" +
                "q=%23"+searchHash+"&count=100");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.addRequestProperty("Authorization", getAuthorization());

        try (InputStream is = con.getInputStream();
             JsonReader rdr = Json.createReader(is)) {

            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("statuses");
            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
//                System.out.print(result.get("from_user"));
//                System.out.print(": ");
                System.out.println(result.get("text"));
                System.out.println("-----------");
            }
        }
    }

    private static String getAuthorization() throws Exception {
        Properties config = new Properties();
        config.load(TwitterObjectSearch.class.getResourceAsStream(
                "/twitterconfig.properties"));

        final String consumerKey = (String)config.get("consumer-key");
        final String consumerSecret = (String)config.get("consumer-secret");
        final String accessToken = (String)config.get("access-token");
        final String accessTokenSecret = (String)config.get("access-token-secret");

        final int timestamp = (int)(System.currentTimeMillis()/1000);

        Map<String, String> map = new TreeMap<String, String>() {{
            put("oauth_consumer_key", consumerKey);
            put("oauth_nonce", "4b25256957d75b6370f33a4501dc5e7e"); // TODO
            put("oauth_signature_method", "HMAC-SHA1");
            put("oauth_timestamp", ""+timestamp);
            put("oauth_token", accessToken);
            put("oauth_version", "1.0");
        }};
        StringBuilder sb1 = new StringBuilder();
        sb1.append("GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fsearch%2Ftweets.json&count%3D100");
        for(Map.Entry<String, String> e : map.entrySet()) {
            sb1.append("%26");
            sb1.append(e.getKey());
            sb1.append("%3D");
            sb1.append(e.getValue());
        }
        sb1.append("%26q%3D%2523"+searchHash);
        String signatureBasedString = sb1.toString();

        Mac m = Mac.getInstance("HmacSHA1");
        m.init(new SecretKeySpec((consumerSecret+"&"+accessTokenSecret).getBytes(), "HmacSHA1"));
        m.update(signatureBasedString.getBytes());
        byte[] res = m.doFinal();
        final String oauthSig = URLEncoder.encode(DatatypeConverter.printBase64Binary(res), "UTF-8");
        map.put("oauth_signature", oauthSig);

        StringBuilder sb = new StringBuilder();
        sb.append("OAuth ");
        boolean first = true;
        for(Map.Entry<String, String> e : map.entrySet()) {
            if (!first) {
                sb.append(',');
                sb.append(' ');
            }
            first = false;
            sb.append(e.getKey());
            sb.append('=');
            sb.append('"');
            sb.append(e.getValue());
            sb.append('"');
        }

        return sb.toString();
    }

}

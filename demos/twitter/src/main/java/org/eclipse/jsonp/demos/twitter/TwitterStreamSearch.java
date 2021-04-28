/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsonp.demos.twitter;

import jakarta.json.*;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import java.io.*;

/**
 * Parses JSON from twitter search REST API using streaming API.
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
 * TODO need to do better, also the last tweet is repeated !
 *
 * @author Jitendra Kotamraju
 */
public class TwitterStreamSearch {

    public static void main(String... args) throws Exception {
        try (InputStream is = TwitterObjectSearch.getSearchStream();
             JsonParser parser = Json.createParser(is)) {
            int depth = 0;
            String name = null;
            String text = null;
            while (parser.hasNext()) {
                Event e = parser.next();
                if (e == Event.KEY_NAME) {
                    switch (parser.getString()) {
                        case "name":
                            if (depth == 3) {
                            parser.next();
                            name = parser.getString();
                            }
                            break;
                        case "text":
                            if (depth == 2) {
                            parser.next();
                            text = parser.getString();
                            }
                            break;
                    }
                } else if (e == Event.START_OBJECT) {
                    ++depth;
                } else if (e == Event.END_OBJECT) {
                    --depth;
                    if (depth == 1) {
                        System.out.println(name+": "+text);
                        System.out.println("-----------");

                    }
                }
            }
        }
    }

}

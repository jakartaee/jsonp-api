/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.facebook;

import jakarta.json.*;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import java.io.*;

/**
 * Parses JSON from facebook graph API using streaming API.
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
public class FacebookStreamSearch {

    public static void main(String... args) throws Exception {
        try (InputStream is = FacebookObjectSearch.getSearchStream();
             JsonParser parser = Json.createParser(is)) {
            while (parser.hasNext()) {
                Event e = parser.next();
                if (e == Event.KEY_NAME) {
                    switch (parser.getString()) {
                        case "name":
                            parser.next();
                            System.out.print(parser.getString());
                            System.out.print(": ");
                            break;
                        case "message":
                            parser.next();
                            System.out.println(parser.getString());
                            System.out.println("---------");
                            break;
                    }
                }
            }
        }
    }

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
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

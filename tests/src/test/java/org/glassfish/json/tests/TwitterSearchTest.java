/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.json.tests;

import junit.framework.TestCase;

import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.io.*;
import java.net.URL;

/**
 * JsonParser Tests using twitter search API
 *
 * @author Jitendra Kotamraju
 */
public class TwitterSearchTest extends TestCase {

    public void test() {
        // dummy test so that junit doesn't complain
    }

    public void xtestStreamTwitter() throws Exception {
        URL url = new URL("http://search.twitter.com/search.json?q=%23java&rpp=100");
        InputStream is = url.openStream();
        JsonParser parser = Json.createParser(is);

        while(parser.hasNext()) {
            Event e = parser.next();
            if (e == Event.KEY_NAME) {
                if (parser.getString().equals("from_user")) {
                    parser.next();
                    System.out.print(parser.getString());
                    System.out.print(": ");
                } else if (parser.getString().equals("text")) {
                    parser.next();
                    System.out.println(parser.getString());
                    System.out.println("---------");
                }
            }
        }
        parser.close();
	}

    public void xtestObjectTwitter() throws Exception {
        URL url = new URL("http://search.twitter.com/search.json?q=%23java&rpp=100");
        InputStream is = url.openStream();
        JsonReader rdr = Json.createReader(is);
        JsonObject obj = rdr.readObject();
        JsonArray results = obj.getJsonArray("results");
        for(JsonObject result : results.getValuesAs(JsonObject.class)) {
            System.out.print(result.get("from_user"));
            System.out.print(": ");
            System.out.println(result.get("text"));
            System.out.println("-----------");
        }
        rdr.close();
    }

}

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
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

package org.glassfish.jsondemos.jsonpointer;

import javax.json.*;
import java.io.*;

/**
 * JsonPointer (http://tools.ietf.org/html/rfc6901) demo with object model API
 *
 * @author Jitendra Kotamraju
 */
public class JsonpointerDemo {

    public static void main(String... args) throws Exception {
        testWiki();
        testPointer();
    }

    private static void testWiki() throws IOException {
        try (InputStream is = JsonpointerDemo.class.getResourceAsStream("/wiki.json");
             JsonReader rdr = Json.createReader(is)) {
            JsonObject person = rdr.readObject();

            assertEquals("NY", getString(person, "/address/state"));
            assertEquals("212 555-1234", getString(person, "/phoneNumber/0/number"));
        }
    }

    private static void testPointer() throws IOException {
        try (InputStream is = JsonpointerDemo.class.getResourceAsStream("/jsonpointer.json");
             JsonReader rdr = Json.createReader(is)) {
            JsonObject root = rdr.readObject();

            assertEquals(root, get(root, ""));
            assertEquals(root.get("foo"), get(root, "/foo"));
            assertEquals(root.getJsonArray("foo").get(0), get(root, "/foo/0"));
            assertEquals(root.get(""), get(root, "/"));
            assertEquals(root.get("a/b"), get(root, "/a~1b"));
            assertEquals(root.get("c%d"), get(root, "/c%d"));
            assertEquals(root.get("e^f"), get(root, "/e^f"));
            assertEquals(root.get("k\"l"), get(root, "/k\"l"));
            assertEquals(root.get("i\\j"), get(root, "/i\\j"));
            assertEquals(root.get(" "), get(root, "/ "));
            assertEquals(root.get("m~n"), get(root, "/m~0n"));

            // Adding a parent to current root and try with it
            JsonObject doc = Json.createObjectBuilder().add("doc", root).build();
            root = doc.getJsonObject("doc");
            assertEquals(doc, get(doc, ""));
            assertEquals(root.get("foo"), get(doc, "/doc/foo"));
            assertEquals(root.getJsonArray("foo").get(0), get(doc, "/doc/foo/0"));
            assertEquals(root.get(""), get(doc, "/doc/"));
            assertEquals(root.get("a/b"), get(doc, "/doc/a~1b"));
            assertEquals(root.get("c%d"), get(doc, "/doc/c%d"));
            assertEquals(root.get("e^f"), get(doc, "/doc/e^f"));
            assertEquals(root.get("k\"l"), get(doc, "/doc/k\"l"));
            assertEquals(root.get("i\\j"), get(doc, "/doc/i\\j"));
            assertEquals(root.get(" "), get(doc, "/doc/ "));
            assertEquals(root.get("m~n"), get(doc, "/doc/m~0n"));
        }
    }

    private static String getString(JsonValue root, String pointer) {
        return ((JsonString)get(root, pointer)).getString();
    }

    private static JsonValue get(JsonValue root, String pointer) {
        if (pointer.isEmpty()) {
            return root;
        }
        if (pointer.charAt(0) != '/') {
            throw new IllegalArgumentException(
                    "JsonPointer "+pointer+" doesn't start with /");
        }

        StringBuilder referenceToken = new StringBuilder();
        for(int i=1; i < pointer.length(); i++) {   // 1 to skip first /
            char ch = pointer.charAt(i);
            if (ch == '/') {
                return get(newRoot(root, referenceToken.toString()), pointer.substring(i));
            } else if (ch == '~') {
                // handle escaping ~0, ~1
                if (i+1 == pointer.length()) {
                    throw new IllegalArgumentException("Illegal escaping: expected ~0 or ~1, but got only ~ in pointer="+pointer);
                }
                ch = pointer.charAt(++i);
                if (ch == '0') {
                    referenceToken.append('~');
                } else if (ch == '1') {
                    referenceToken.append('/');
                } else {
                    throw new IllegalArgumentException("Illegal escaping: expected ~0 or ~1, but got ~"+ch+" in pointer="+pointer);
                }
            } else {
                referenceToken.append(ch);
            }
        }
        return newRoot(root, referenceToken.toString());
    }

    private static JsonValue newRoot(JsonValue root, String referenceToken) {
        if (root instanceof JsonObject) {
            return ((JsonObject)root).get(referenceToken);
        } else if (root instanceof JsonArray) {
            return ((JsonArray)root).get(Integer.parseInt(referenceToken));
        }
        throw new IllegalArgumentException("Illegal reference token="+referenceToken+" for value="+root);
    }

    private static void assertEquals(JsonValue exp, JsonValue got) {
        if (exp != got) {
            throw new RuntimeException("Expected = "+exp+" but got = "+got);
        }
    }

    private static void assertEquals(String exp, String got) {
        if (!exp.equals(got)) {
            throw new RuntimeException("Expected = "+exp+" but got = "+got);
        }
    }

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json.tests;

import java.io.StringReader;
import javax.json.Json;
import javax.json.stream.JsonParser;
import junit.framework.TestCase;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 *
 * @author lukas
 */
public class JsonParserSkipTest extends TestCase {

    public void testSkipArrayReader() {
        try (JsonParser parser = Json.createParser(new StringReader("[[],[[]]]"))) {
            testSkipArray(parser);
        }
    }

    public void testSkipArrayStructure() {
        try (JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder()
                        .add(Json.createArrayBuilder())
                        .add(Json.createArrayBuilder()
                                .add(Json.createArrayBuilder()))
                        .build())) {
            testSkipArray(parser);
        }
    }

    private static void testSkipArray(JsonParser parser) {
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        parser.skipArray();
        assertEquals(false, parser.hasNext());
    }

    public void testSkipArrayInObjectReader() {
        try (JsonParser parser = Json.createParser(new StringReader("{\"array\":[[],[[]]],\"object\":\"value2\"}"))) {
            testSkipArrayInObject(parser);
        }
    }

    public void testSkipArrayInObjectStructure() {
        try (JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder().add("array", Json.createArrayBuilder()
                        .add(Json.createArrayBuilder())
                        .add(Json.createArrayBuilder()
                                .add(Json.createArrayBuilder()))
                ).add("object", "value2")
                        .build())) {
            testSkipArrayInObject(parser);
        }
    }

    private static void testSkipArrayInObject(JsonParser parser) {
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        parser.skipArray();
        assertTrue(parser.hasNext());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_STRING, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertFalse(parser.hasNext());
    }

    public void testSkipObjectReader() {
        try (JsonParser parser = Json.createParser(new StringReader("{\"array\":[],\"objectToSkip\":{\"huge key\":\"huge value\"},\"simple\":2}"))) {
            testSkipObject(parser);
        }
    }

    public void testSkipObjectStructure() {
        try (JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder()
                        .add("array", Json.createArrayBuilder().build())
                        .add("objectToSkip", Json.createObjectBuilder().add("huge key", "huge value"))
                        .add("simple", 2)
                        .build())) {
            testSkipObject(parser);
        }
    }

    private static void testSkipObject(JsonParser parser) {
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_ARRAY, parser.next());
        assertEquals(JsonParser.Event.END_ARRAY, parser.next());
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.START_OBJECT, parser.next());
        parser.skipObject();
        assertEquals(JsonParser.Event.KEY_NAME, parser.next());
        assertEquals(JsonParser.Event.VALUE_NUMBER, parser.next());
        assertEquals(JsonParser.Event.END_OBJECT, parser.next());
        assertEquals(false, parser.hasNext());
    }
}

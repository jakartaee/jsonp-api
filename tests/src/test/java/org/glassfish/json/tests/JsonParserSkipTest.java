/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

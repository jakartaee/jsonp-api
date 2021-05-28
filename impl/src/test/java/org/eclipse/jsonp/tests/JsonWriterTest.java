/*
 * Copyright (c) 2012, 2021 Oracle and/or its affiliates. All rights reserved.
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

package org.eclipse.jsonp.tests;

import jakarta.json.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * @author Jitendra Kotamraju
 */
public class JsonWriterTest extends TestCase {
    public JsonWriterTest(String testName) {
        super(testName);
    }

    public void testObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(Json.createObjectBuilder().build());
        jsonWriter.close();
        writer.close();

        assertEquals("{}", writer.toString());
    }

    public void testEmptyObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(JsonValue.EMPTY_JSON_OBJECT);
        jsonWriter.close();
        writer.close();

        assertEquals("{}", writer.toString());
    }

    public void testArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().build());
        jsonWriter.close();
        writer.close();

        assertEquals("[]", writer.toString());
    }

    public void testEmptyArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(JsonValue.EMPTY_JSON_ARRAY);
        jsonWriter.close();
        writer.close();

        assertEquals("[]", writer.toString());
    }

    public void testNumber() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add(10).build());
        jsonWriter.close();
        writer.close();

        assertEquals("[10]", writer.toString());
    }

    public void testDoubleNumber() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add(10.5).build());
        jsonWriter.close();
        writer.close();

        assertEquals("[10.5]", writer.toString());
    }

    public void testArrayString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add("string").build());
        jsonWriter.close();
        writer.close();

        assertEquals("[\"string\"]", writer.toString());
    }

    public void testObjectAsValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write((JsonValue) (Json.createObjectBuilder().build()));
        jsonWriter.close();
        writer.close();

        assertEquals("{}", writer.toString());
    }

    public void testNullValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(JsonValue.NULL);
        jsonWriter.close();
        writer.close();

        assertEquals("null", writer.toString());
    }

    public void testTrueValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(JsonValue.TRUE);
        jsonWriter.close();
        writer.close();

        assertEquals("true", writer.toString());
    }

    public void testFalseValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.write(JsonValue.FALSE);
        jsonWriter.close();
        writer.close();

        assertEquals("false", writer.toString());
    }

    public void testIllegalStateExcepton() throws Exception {
        JsonObject obj = Json.createObjectBuilder().build();
        JsonArray array = Json.createArrayBuilder().build();

        JsonWriter writer = Json.createWriter(new StringWriter());
        writer.writeObject(obj);
        try {
            writer.writeObject(obj);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();

        writer = Json.createWriter(new StringWriter());
        writer.writeArray(array);
        try {
            writer.writeArray(array);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();

        writer = Json.createWriter(new StringWriter());
        writer.write(array);
        try {
            writer.writeArray(array);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();
    }

    public void testNoCloseWriteObjectToStream() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(baos);
        writer.write(Json.createObjectBuilder().build());
        // not calling writer.close() intentionally
        assertEquals("{}", baos.toString("UTF-8"));
    }

    public void testNoCloseWriteObjectToWriter() throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.write(Json.createObjectBuilder().build());
        // not calling writer.close() intentionally
        assertEquals("{}", sw.toString());
    }

    public void testNoCloseWriteArrayToStream() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(baos);
        writer.write(Json.createArrayBuilder().build());
        // not calling writer.close() intentionally
        assertEquals("[]", baos.toString("UTF-8"));
    }

    public void testNoCloseWriteArrayToWriter() throws Exception {
        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.write(Json.createArrayBuilder().build());
        // not calling writer.close() intentionally
        assertEquals("[]", sw.toString());
    }

    public void testClose() throws Exception {
        MyByteStream baos = new MyByteStream();
        JsonWriter writer = Json.createWriter(baos);
        writer.write(Json.createObjectBuilder().build());
        writer.close();
        assertEquals("{}", baos.toString("UTF-8"));
        assertTrue(baos.isClosed());
    }

    private static final class MyByteStream extends ByteArrayOutputStream {
        boolean closed;

        boolean isClosed() {
            return closed;
        }

        public void close() throws IOException {
            super.close();
            closed = true;
        }
    }
}

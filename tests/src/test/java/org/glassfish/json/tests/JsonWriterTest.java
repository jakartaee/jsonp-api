/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.*;
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

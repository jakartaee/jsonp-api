/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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
import org.glassfish.json.api.BufferPool;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * {@link JsonGenerator} tests
 *
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorTest extends TestCase {
    public JsonGeneratorTest(String testName) {
        super(testName);
    }

    public void testObjectWriter() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        testObject(generator);
        generator.close();
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(writer.toString()));
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
    }

    public void testObjectStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = Json.createGenerator(out);
        testObject(generator);
        generator.close();
        out.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        JsonReader reader = Json.createReader(in);
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
        reader.close();
        in.close();
    }

    static void testObject(JsonGenerator generator) throws Exception {
        generator
                .writeStartObject()
                .write("firstName", "John")
                .write("lastName", "Smith")
                .write("age", 25)
                .writeStartObject("address")
                .write("streetAddress", "21 2nd Street")
                .write("city", "New York")
                .write("state", "NY")
                .write("postalCode", "10021")
                .writeEnd()
                .writeStartArray("phoneNumber")
                .writeStartObject()
                .write("type", "home")
                .write("number", "212 555-1234")
                .writeEnd()
                .writeStartObject()
                .write("type", "fax")
                .write("number", "646 555-4567")
                .writeEnd()
                .writeEnd()
                .writeEnd();
    }

    public void testArray() throws Exception {
        Writer sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);
        generator
                .writeStartArray()
                .writeStartObject()
                .write("type", "home")
                .write("number", "212 555-1234")
                .writeEnd()
                .writeStartObject()
                .write("type", "fax")
                .write("number", "646 555-4567")
                .writeEnd()
                .writeEnd();
        generator.close();
    }

    // tests JsonGenerator when JsonValue is used for generation
    public void testJsonValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator
                .writeStartObject()
                .write("firstName", "John")
                .write("lastName", "Smith")
                .write("age", 25)
                .write("address", JsonBuilderTest.buildAddress())
                .write("phoneNumber", JsonBuilderTest.buildPhone())
                .writeEnd();
        generator.close();
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(writer.toString()));
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
    }

    public void testArrayString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartArray().write("string").writeEnd();
        generator.close();
        writer.close();

        assertEquals("[\"string\"]", writer.toString());
    }

    public void testEscapedString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartArray().write("\u0000").writeEnd();
        generator.close();
        writer.close();

        assertEquals("[\"\\u0000\"]", writer.toString());
    }

    public void testEscapedString1() throws Exception {
        String expected = "\u0000\u00ff";
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);
        generator.writeStartArray().write("\u0000\u00ff").writeEnd();
        generator.close();
        sw.close();

        JsonReader jr = Json.createReader(new StringReader(sw.toString()));
        JsonArray array = jr.readArray();
        String got = array.getString(0);
        jr.close();

        assertEquals(expected, got);
    }

    public void testGeneratorEquals() throws Exception {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);
        generator.writeStartArray()
                .write(JsonValue.TRUE)
                .write(JsonValue.FALSE)
                .write(JsonValue.NULL)
                .write(Integer.MAX_VALUE)
                .write(Long.MAX_VALUE)
                .write(Double.MAX_VALUE)
                .write(Integer.MIN_VALUE)
                .write(Long.MIN_VALUE)
                .write(Double.MIN_VALUE)
                .writeEnd();
        generator.close();

        JsonReader reader = Json.createReader(new StringReader(sw.toString()));
        JsonArray expected = reader.readArray();
        reader.close();

        JsonArray actual = Json.createArrayBuilder()
                .add(JsonValue.TRUE)
                .add(JsonValue.FALSE)
                .add(JsonValue.NULL)
                .add(Integer.MAX_VALUE)
                .add(Long.MAX_VALUE)
                .add(Double.MAX_VALUE)
                .add(Integer.MIN_VALUE)
                .add(Long.MIN_VALUE)
                .add(Double.MIN_VALUE)
                .build();

        assertEquals(expected, actual);
    }

    public void testPrettyObjectWriter() throws Exception {
        StringWriter writer = new StringWriter();
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGenerator generator = Json.createGeneratorFactory(config)
                .createGenerator(writer);
        testObject(generator);
        generator.close();
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(writer.toString()));
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
    }

    public void testPrettyObjectStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGenerator generator = Json.createGeneratorFactory(config)
                .createGenerator(out);
        testObject(generator);
        generator.close();
        out.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        JsonReader reader = Json.createReader(in);
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
        reader.close();
        in.close();
    }

    public void testGenerationException1() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject();
        try {
            generator.writeStartObject();
            fail("Expected JsonGenerationException, writeStartObject() cannot be called more than once");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException2() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject();
        try {
            generator.writeStartArray();
            fail("Expected JsonGenerationException, writeStartArray() is valid in no context");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException3() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        try {
            generator.close();
            fail("Expected JsonGenerationException, no JSON is generated");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException4() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartArray();
        try {
            generator.close();
            fail("Expected JsonGenerationException, writeEnd() is not called");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException5() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject();
        try {
            generator.close();
            fail("Expected JsonGenerationException, writeEnd() is not called");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException6() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject().writeEnd();
        try {
            generator.writeStartObject();
            fail("Expected JsonGenerationException, cannot generate one more JSON text");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException7() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartArray().writeEnd();
        try {
            generator.writeStartArray();
            fail("Expected JsonGenerationException, cannot generate one more JSON text");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }


    public void testGenerationException8() throws Exception {
        StringWriter sWriter = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sWriter);
        generator.writeStartObject();
        try {
            generator.write(JsonValue.TRUE);
            fail("Expected JsonGenerationException, cannot generate one more JSON text");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGenerationException9() throws Exception {
        StringWriter sWriter = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sWriter);
        generator.writeStartObject();
        try {
            generator.write("name");
            fail("Expected JsonGenerationException, cannot generate one more JSON text");
        } catch (JsonGenerationException je) {
            // Expected exception
        }
    }

    public void testGeneratorArrayDouble() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartArray();
        try {
            generator.write(Double.NaN);
            fail("JsonGenerator.write(Double.NaN) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        try {
            generator.write(Double.POSITIVE_INFINITY);
            fail("JsonGenerator.write(Double.POSITIVE_INIFINITY) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        try {
            generator.write(Double.NEGATIVE_INFINITY);
            fail("JsonGenerator.write(Double.NEGATIVE_INIFINITY) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        generator.writeEnd();
        generator.close();
    }

    public void testGeneratorObjectDouble() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.writeStartObject();
        try {
            generator.write("foo", Double.NaN);
            fail("JsonGenerator.write(String, Double.NaN) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        try {
            generator.write("foo", Double.POSITIVE_INFINITY);
            fail("JsonGenerator.write(String, Double.POSITIVE_INIFINITY) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        try {
            generator.write("foo", Double.NEGATIVE_INFINITY);
            fail("JsonGenerator.write(String, Double.NEGATIVE_INIFINITY) should produce NumberFormatException");
        } catch (NumberFormatException ne) {
            // expected
        }
        generator.writeEnd();
        generator.close();
    }

    public void testIntGenerator() throws Exception {
        Random r = new Random(System.currentTimeMillis());
        JsonGeneratorFactory gf = Json.createGeneratorFactory(null);
        JsonReaderFactory rf = Json.createReaderFactory(null);
        JsonBuilderFactory bf = Json.createBuilderFactory(null);
        for(int i=0; i < 100000; i++) {
            int num = r.nextInt();
            StringWriter sw = new StringWriter();
            JsonGenerator generator = gf.createGenerator(sw);
            generator.writeStartArray().write(num).writeEnd().close();

            JsonReader reader = rf.createReader(new StringReader(sw.toString()));
            JsonArray got = reader.readArray();
            reader.close();

            JsonArray expected = bf.createArrayBuilder().add(num).build();

            assertEquals(expected, got);
        }
    }

    public void testGeneratorBuf() throws Exception {
        JsonGeneratorFactory gf = Json.createGeneratorFactory(null);
        JsonReaderFactory rf = Json.createReaderFactory(null);
        JsonBuilderFactory bf = Json.createBuilderFactory(null);
        StringBuilder sb = new StringBuilder();
        int value = 10;
        for(int i=0; i < 25000; i++) {
            sb.append('a');
            String name = sb.toString();
            StringWriter sw = new StringWriter();
            JsonGenerator generator = gf.createGenerator(sw);
            generator.writeStartObject().write(name, value).writeEnd().close();

            JsonReader reader = rf.createReader(new StringReader(sw.toString()));
            JsonObject got = reader.readObject();
            reader.close();

            JsonObject expected = bf.createObjectBuilder().add(name, value).build();

            assertEquals(expected, got);
        }
    }

    public void testBufferPoolFeature() {
        final JsonParserTest.MyBufferPool bufferPool = new JsonParserTest.MyBufferPool(1024);
        Map<String, Object> config = new HashMap<String, Object>() {{
            put(BufferPool.class.getName(), bufferPool);
        }};

        JsonGeneratorFactory factory = Json.createGeneratorFactory(config);
        JsonGenerator generator = factory.createGenerator(new StringWriter());
        generator.writeStartArray();
        generator.writeEnd();
        generator.close();
        assertTrue(bufferPool.isTakeCalled());
        assertTrue(bufferPool.isRecycleCalled());
    }

    public void testBufferSizes() {
        JsonReaderFactory rf = Json.createReaderFactory(null);
        JsonBuilderFactory bf = Json.createBuilderFactory(null);
        for(int size=10; size < 1000; size++) {
            final JsonParserTest.MyBufferPool bufferPool = new JsonParserTest.MyBufferPool(size);
            Map<String, Object> config = new HashMap<String, Object>() {{
                put(BufferPool.class.getName(), bufferPool);
            }};
            JsonGeneratorFactory gf = Json.createGeneratorFactory(config);

            StringBuilder sb = new StringBuilder();
            int value = 10;
            for(int i=0; i < 1500; i++) {
                sb.append('a');
                String name = sb.toString();
                StringWriter sw = new StringWriter();
                JsonGenerator generator = gf.createGenerator(sw);
                generator.writeStartObject().write(name, value).writeEnd().close();

                JsonReader reader = rf.createReader(new StringReader(sw.toString()));
                JsonObject got = reader.readObject();
                reader.close();

                JsonObject expected = bf.createObjectBuilder().add(name, value).build();

                assertEquals(expected, got);
            }

        }
    }

    public void testString() throws Exception {
        escapedString("");
        escapedString("abc");
        escapedString("abc\f");
        escapedString("abc\na");
        escapedString("abc\tabc");
        escapedString("abc\n\tabc");
        escapedString("abc\n\tabc\r");
        escapedString("\n\tabc\r");
        escapedString("\bab\tb\rc\\\"\ftesting1234");
        escapedString("\f\babcdef\tb\rc\\\"\ftesting1234");
    }

    void escapedString(String expected) throws Exception {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);
        generator.writeStartArray().write(expected).writeEnd();
        generator.close();
        sw.close();

        JsonReader jr = Json.createReader(new StringReader(sw.toString()));
        JsonArray array = jr.readArray();
        String got = array.getString(0);
        jr.close();

        assertEquals(expected, got);
    }

    public void testFlush() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonGenerator gen = Json.createGenerator(baos);
        gen.writeStartObject().writeEnd();
        gen.flush();

        assertEquals("{}", baos.toString("UTF-8"));
    }

}

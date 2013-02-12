/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

import junit.framework.TestCase;

import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * JsonParser Tests
 *
 * @author Jitendra Kotamraju
 */
public class JsonParserTest extends TestCase {
    static final Charset UTF_8 = Charset.forName("UTF-8");
    static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    static final Charset UTF_16 = Charset.forName("UTF-16");
    static final Charset UTF_32LE = Charset.forName("UTF-32LE");
    static final Charset UTF_32BE = Charset.forName("UTF-32BE");

    public JsonParserTest(String testName) {
        super(testName);
    }

    public void testReader() {
        JsonParser reader = Json.createParser(
                new StringReader("{ \"a\" : \"b\", \"c\" : null, \"d\" : [null, \"abc\"] }"));
        reader.close();
    }


    public void testEmptyArrayReader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStream() {
        JsonParser parser = Json.createParser(
                new ByteArrayInputStream(new byte[]{'[', ']'}));
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF8() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_8));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF16LE() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_16LE));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF16BE() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_16BE));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF32LE() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_32LE));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF32BE() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_32BE));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamUTF16() {
        ByteArrayInputStream bin = new ByteArrayInputStream("[]".getBytes(UTF_16));
        JsonParser parser = Json.createParser(bin);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamWithConfig() {
        Map<String, ?> config = new HashMap<String, Object>();
        JsonParser parser = Json.createParserFactory(config).createParser(
                new ByteArrayInputStream(new byte[]{'[', ']'}));
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStructure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder().build());
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStructureWithConfig() {
        Map<String, ?> config = new HashMap<String, Object>();
        JsonParser parser = Json.createParserFactory(config).createParser(
                Json.createArrayBuilder().build());
        testEmptyArray(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testEmptyArray(JsonParser parser) {
        while (parser.hasNext()) {
            parser.next();
        }
    }


    public void testEmptyArrayReaderIterator() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator(parser);
        parser.close();
    }

    public void testEmptyArrayStructureIterator() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder().build());
        testEmptyArrayIterator(parser);
        parser.close();
    }

    static void testEmptyArrayIterator(JsonParser parser) {
        assertEquals(true, parser.hasNext());
        assertEquals(true, parser.hasNext());
        assertEquals(Event.START_ARRAY, parser.next());

        assertEquals(true, parser.hasNext());
        assertEquals(true, parser.hasNext());
        assertEquals(Event.END_ARRAY, parser.next());

        assertEquals(false, parser.hasNext());
        assertEquals(false, parser.hasNext());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
        }
    }


    public void testEmptyArrayIterator2Reader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator2(parser);
        parser.close();
    }

    public void testEmptyArrayIterator2Structure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder().build());
        testEmptyArrayIterator2(parser);
        parser.close();
    }

    static void testEmptyArrayIterator2(JsonParser parser) {
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
        }
    }

    public void testEmptyArrayIterator3Reader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator3(parser);
        parser.close();
    }

    public void testEmptyArrayIterator3Structure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder().build());
        testEmptyArrayIterator3(parser);
        parser.close();
    }

    static void testEmptyArrayIterator3(JsonParser parser) {
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        assertEquals(false, parser.hasNext());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
        }
    }


    // Tests empty object
    public void testEmptyObjectReader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStream() {
        JsonParser parser = Json.createParser(
                new ByteArrayInputStream(new byte[]{'{', '}'}));
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStructure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder().build());
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStructureWithConfig() {
        Map<String, ?> config = new HashMap<String, Object>();
        JsonParser parser = Json.createParserFactory(config).createParser(
                Json.createObjectBuilder().build());
        testEmptyObject(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testEmptyObject(JsonParser parser) {
        while (parser.hasNext()) {
            parser.next();
        }
    }


    public void testEmptyObjectIteratorReader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator(parser);
        parser.close();
    }

    public void testEmptyObjectIteratorStructure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder().build());
        testEmptyObjectIterator(parser);
        parser.close();
    }

    static void testEmptyObjectIterator(JsonParser parser) {
        assertEquals(true, parser.hasNext());
        assertEquals(true, parser.hasNext());
        assertEquals(Event.START_OBJECT, parser.next());

        assertEquals(true, parser.hasNext());
        assertEquals(true, parser.hasNext());
        assertEquals(Event.END_OBJECT, parser.next());

        assertEquals(false, parser.hasNext());
        assertEquals(false, parser.hasNext());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
        }
    }


    public void testEmptyObjectIterator2Reader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator2(parser);
        parser.close();
    }

    public void testEmptyObjectIterator2Structure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder().build());
        testEmptyObjectIterator2(parser);
        parser.close();
    }

    static void testEmptyObjectIterator2(JsonParser parser) {
        assertEquals(Event.START_OBJECT, parser.next());
        assertEquals(Event.END_OBJECT, parser.next());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
        }
    }


    public void testEmptyObjectIterator3Reader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator3(parser);
        parser.close();
    }

    public void testEmptyObjectIterator3Structure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createObjectBuilder().build());
        testEmptyObjectIterator3(parser);
        parser.close();
    }

    static void testEmptyObjectIterator3(JsonParser parser) {
        assertEquals(Event.START_OBJECT, parser.next());
        assertEquals(Event.END_OBJECT, parser.next());
        assertEquals(false, parser.hasNext());
        try {
            parser.next();
            fail("Should have thrown a NoSuchElementException");
        } catch (NoSuchElementException ne) {
            // expected
        }
    }


    public void testWikiIteratorReader() throws Exception {
        JsonParser parser = Json.createParser(wikiReader());
        testWikiIterator(parser);
        parser.close();
    }

    public void testWikiIteratorStructure() throws Exception {
        JsonParser parser = Json.createParserFactory(null).createParser(
                JsonBuilderTest.buildPerson());
        testWikiIterator(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testWikiIterator(JsonParser parser) throws Exception {
        while (parser.hasNext()) {
            parser.next();
        }
    }

    public void testWikiInputStream() throws Exception {
        JsonParser parser = Json.createParser(wikiStream());
        testWiki(parser);
        parser.close();
    }

    public void testWikiInputStreamUTF16LE() throws Exception {
        ByteArrayInputStream bin = new ByteArrayInputStream(wikiString()
                .getBytes(UTF_16LE));
        JsonParser parser = Json.createParser(bin);
        testWiki(parser);
        parser.close();
    }

    public void testWikiReader() throws Exception {
        JsonParser parser = Json.createParser(wikiReader());
        testWiki(parser);
        parser.close();
    }

    public void testWikiStructure() throws Exception {
        JsonParser parser = Json.createParserFactory(null).createParser(
                JsonBuilderTest.buildPerson());
        testWiki(parser);
        parser.close();
    }

    static void testWiki(JsonParser parser) {

        Event event = parser.next();
        assertEquals(Event.START_OBJECT, event);

        testObjectStringValue(parser, "firstName", "John");
        testObjectStringValue(parser, "lastName", "Smith");

        event = parser.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("age", parser.getString());

        event = parser.next();
        assertEquals(Event.VALUE_NUMBER, event);
        assertEquals(25, parser.getInt());
        assertEquals(25, parser.getLong());
        assertEquals(25, parser.getBigDecimal().intValue());
        assertTrue( parser.isIntegralNumber());

        event = parser.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("address", parser.getString());

        event = parser.next();
        assertEquals(Event.START_OBJECT, event);


        testObjectStringValue(parser, "streetAddress", "21 2nd Street");
        testObjectStringValue(parser, "city", "New York");
        testObjectStringValue(parser, "state", "NY");
        testObjectStringValue(parser, "postalCode", "10021");

        event = parser.next();
        assertEquals(Event.END_OBJECT, event);

        event = parser.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("phoneNumber", parser.getString());

        event = parser.next();
        assertEquals(Event.START_ARRAY, event);
        event = parser.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(parser, "type", "home");
        testObjectStringValue(parser, "number", "212 555-1234");
        event = parser.next();
        assertEquals(Event.END_OBJECT, event);

        event = parser.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(parser, "type", "fax");
        testObjectStringValue(parser, "number", "646 555-4567");
        event = parser.next();
        assertEquals(Event.END_OBJECT, event);
        event = parser.next();
        assertEquals(Event.END_ARRAY, event);

        event = parser.next();
        assertEquals(Event.END_OBJECT, event);
    }

    static void testObjectStringValue(JsonParser parser, String name, String value) {
        Event event = parser.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals(name, parser.getString());

        event = parser.next();
        assertEquals(Event.VALUE_STRING, event);
        assertEquals(value, parser.getString());
    }

    public void testNestedArrayReader() {
        JsonParser parser = Json.createParser(new StringReader("[[],[[]]]"));
        testNestedArray(parser);
        parser.close();
    }

    public void testNestedArrayStructure() {
        JsonParser parser = Json.createParserFactory(null).createParser(
                Json.createArrayBuilder()
                        .add(Json.createArrayBuilder())
                        .add(Json.createArrayBuilder()
                                .add(Json.createArrayBuilder()))
                        .build());
        testNestedArray(parser);
        parser.close();
    }

    static void testNestedArray(JsonParser parser) {
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.START_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        assertEquals(Event.END_ARRAY, parser.next());
        assertEquals(false, parser.hasNext());
        assertEquals(false, parser.hasNext());
    }

    public void testExceptionsReader() throws Exception {
        JsonParser parser = Json.createParser(wikiReader());
        testExceptions(parser);
        parser.close();
    }

    public void testExceptionsStructure() throws Exception {
        JsonParser parser = Json.createParserFactory(null).createParser(
                JsonBuilderTest.buildPerson());
        testExceptions(parser);
        parser.close();
    }

    static void testExceptions(JsonParser parser) {

        Event event = parser.next();
        assertEquals(Event.START_OBJECT, event);

        try {
            parser.getString();
            fail("JsonParser#getString() should have thrown exception in START_OBJECT state");
        } catch (IllegalStateException expected) {
            // no-op
        }

        try {
            parser.isIntegralNumber();
            fail("JsonParser#getNumberType() should have thrown exception in START_OBJECT state");
        } catch (IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getInt();
            fail("JsonParser#getInt() should have thrown exception in START_OBJECT state");
        } catch (IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getLong();
            fail("JsonParser#getLong() should have thrown exception in START_OBJECT state");
        } catch (IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getBigDecimal();
            fail("JsonParser#getBigDecimal() should have thrown exception in START_OBJECT state");
        } catch (IllegalStateException expected) {
            // no-op
        }
    }

    static String wikiString() {
        java.util.Scanner scanner = new java.util.Scanner(wikiReader())
                .useDelimiter("\\A");
        String str = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return str;
    }

    static InputStream wikiStream() {
        return JsonParserTest.class.getResourceAsStream("/wiki.json");
    }

    static Reader wikiReader() {
        return new InputStreamReader(
                JsonParserTest.class.getResourceAsStream("/wiki.json"), UTF_8);
    }

}

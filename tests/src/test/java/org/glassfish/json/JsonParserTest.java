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

package org.glassfish.json;

import junit.framework.TestCase;

import javax.json.Json;
import javax.json.JsonBuilder;
import javax.json.JsonConfiguration;
import javax.json.JsonNumber;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Jitendra Kotamraju
 */
public class JsonParserTest extends TestCase {

    public JsonParserTest(String testName) {
        super(testName);
    }
    
    public void testReader() {
        JsonParser reader = Json.createParser(new StringReader("{ \"a\" : \"b\", \"c\" : null, \"d\" :[null, \"abc\"] }"));
        reader.close();
    }


    public void testEmptyArrayReader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStream() {
        JsonParser parser = Json.createParser(new ByteArrayInputStream(new byte[]{'[',']'}));
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStreamWithConfig() {
        JsonConfiguration config = new JsonConfiguration();
        JsonParser parser = Json.createParser(new ByteArrayInputStream(new byte[]{'[',']'}), config);
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStructure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginArray().endArray().build());
        testEmptyArray(parser);
        parser.close();
    }

    public void testEmptyArrayStructureWithConfig() {
        JsonConfiguration config = new JsonConfiguration();
        JsonParser parser = Json.createParser(new JsonBuilder().beginArray().endArray().build(), config);
        testEmptyArray(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testEmptyArray(JsonParser parser) {
        for(Event e : parser) {
        }
    }


    public void testEmptyArrayReaderIterator() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator(parser);
        parser.close();
    }

    public void testEmptyArrayStructureIterator() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginArray().endArray().build());
        testEmptyArrayIterator(parser);
        parser.close();
    }

    static void testEmptyArrayIterator(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(true, it.hasNext());
        assertEquals(true, it.hasNext());
        assertEquals(Event.START_ARRAY, it.next());

        assertEquals(true, it.hasNext());
        assertEquals(true, it.hasNext());
        assertEquals(Event.END_ARRAY, it.next());

        assertEquals(false, it.hasNext());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
    }



    public void testEmptyArrayIterator2Reader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator2(parser);
        parser.close();
    }

    public void testEmptyArrayIterator2Structure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginArray().endArray().build());
        testEmptyArrayIterator2(parser);
        parser.close();
    }

    static void testEmptyArrayIterator2(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
    }


    public void testEmptyArrayIterator3Reader() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        testEmptyArrayIterator3(parser);
        parser.close();
    }

    public void testEmptyArrayIterator3Structure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginArray().endArray().build());
        testEmptyArrayIterator3(parser);
        parser.close();
    }

    static void testEmptyArrayIterator3(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
    }


    // Tests empty object
    public void testEmptyObjectReader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStream() {
        JsonParser parser = Json.createParser(new ByteArrayInputStream(new byte[]{'{','}'}));
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStructure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginObject().endObject().build());
        testEmptyObject(parser);
        parser.close();
    }

    public void testEmptyObjectStructureWithConfig() {
        JsonConfiguration config = new JsonConfiguration();
        JsonParser parser = Json.createParser(new JsonBuilder().beginObject().endObject().build(), config);
        testEmptyObject(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testEmptyObject(JsonParser parser) {
        for(Event e : parser) {
        }
    }




    public void testEmptyObjectIteratorReader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator(parser);
        parser.close();
    }

    public void testEmptyObjectIteratorStructure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginObject().endObject().build());
        testEmptyObjectIterator(parser);
        parser.close();
    }

    static void testEmptyObjectIterator(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(true, it.hasNext());
        assertEquals(true, it.hasNext());
        assertEquals(Event.START_OBJECT, it.next());

        assertEquals(true, it.hasNext());
        assertEquals(true, it.hasNext());
        assertEquals(Event.END_OBJECT, it.next());

        assertEquals(false, it.hasNext());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
    }


    public void testEmptyObjectIterator2Reader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator2(parser);
        parser.close();
    }
    public void testEmptyObjectIterator2Structure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginObject().endObject().build());
        testEmptyObjectIterator2(parser);
        parser.close();
    }

    static void testEmptyObjectIterator2(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(Event.START_OBJECT, it.next());
        assertEquals(Event.END_OBJECT, it.next());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
    }


    public void testEmptyObjectIterator3Reader() {
        JsonParser parser = Json.createParser(new StringReader("{}"));
        testEmptyObjectIterator3(parser);
        parser.close();
    }

    public void testEmptyObjectIterator3Structure() {
        JsonParser parser = Json.createParser(new JsonBuilder().beginObject().endObject().build());
        testEmptyObjectIterator3(parser);
        parser.close();
    }

    static void testEmptyObjectIterator3(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(Event.START_OBJECT, it.next());
        assertEquals(Event.END_OBJECT, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
            // expected
        }
    }



    public void testWikiIteratorReader() throws Exception {
        Reader wikiReader = new InputStreamReader(getClass().getResourceAsStream("/wiki.json"));
        JsonParser parser = Json.createParser(wikiReader);
        testWikiIterator(parser);
        parser.close();
        wikiReader.close();
    }

    public void testWikiIteratorStructure() throws Exception {
        JsonParser parser = Json.createParser(JsonBuilderTest.buildPerson());
        testWikiIterator(parser);
        parser.close();
    }

    @SuppressWarnings("UnusedDeclaration")
    static void testWikiIterator(JsonParser parser) throws Exception {
        for(Event e : parser) {
        }
    }

    
    
    public void testWikiReader() throws Exception {
        Reader wikiReader = new InputStreamReader(getClass().getResourceAsStream("/wiki.json"));
        JsonParser parser = Json.createParser(wikiReader);
        testWiki(parser);
        parser.close();
        wikiReader.close();
    }

    public void testWikiStructure() throws Exception {
        JsonParser parser = Json.createParser(JsonBuilderTest.buildPerson());
        testWiki(parser);
        parser.close();
    }

    static void testWiki(JsonParser parser) {
        Iterator<Event> it = parser.iterator();

        Event event = it.next();
        assertEquals(Event.START_OBJECT, event);

        testObjectStringValue(parser, it, "firstName", "John");
        testObjectStringValue(parser, it, "lastName", "Smith");

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("age", parser.getString());

        event = it.next();
        assertEquals(Event.VALUE_NUMBER, event);
        assertEquals(25, parser.getIntValue());
        assertEquals(25, parser.getLongValue());
        assertEquals(25, parser.getBigDecimalValue().intValue());
        assertEquals(JsonNumber.JsonNumberType.INT, parser.getNumberType());

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("address", parser.getString());

        event = it.next();
        assertEquals(Event.START_OBJECT, event);


        testObjectStringValue(parser, it, "streetAddress", "21 2nd Street");
        testObjectStringValue(parser, it, "city", "New York");
        testObjectStringValue(parser, it, "state", "NY");
        testObjectStringValue(parser, it, "postalCode", "10021");

        event = it.next();
        assertEquals(Event.END_OBJECT, event);

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("phoneNumber", parser.getString());

        event = it.next();
        assertEquals(Event.START_ARRAY, event);
        event = it.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(parser, it, "type", "home");
        testObjectStringValue(parser, it, "number", "212 555-1234");
        event = it.next();
        assertEquals(Event.END_OBJECT, event);

        event = it.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(parser, it, "type", "fax");
        testObjectStringValue(parser, it, "number", "646 555-4567");
        event = it.next();
        assertEquals(Event.END_OBJECT, event);
        event = it.next();
        assertEquals(Event.END_ARRAY, event);

        event = it.next();
        assertEquals(Event.END_OBJECT, event);
    }

    static void testObjectStringValue(JsonParser reader, Iterator<Event> it, String name, String value) {
        Event event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals(name, reader.getString());

        event = it.next();
        assertEquals(Event.VALUE_STRING, event);
        assertEquals(value, reader.getString());
    }

    public void testNestedArrayReader() {
        JsonParser parser = Json.createParser(new StringReader("[[],[[]]]"));
        testNestedArray(parser);
        parser.close();
    }

    public void testNestedArrayStructure() {
        JsonParser parser = Json.createParser(new JsonBuilder()
                .beginArray()
                    .beginArray().endArray()
                    .beginArray()
                        .beginArray().endArray()
                    .endArray()
                .endArray()
                .build());
        testNestedArray(parser);
        parser.close();
    }

    static void testNestedArray(JsonParser parser) {
        Iterator<Event> it = parser.iterator();
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(false, it.hasNext());
        assertEquals(false, it.hasNext());
    }

    public void testExceptionsReader() throws Exception {
        Reader wikiReader = new InputStreamReader(getClass().getResourceAsStream("/wiki.json"));
        JsonParser parser = Json.createParser(wikiReader);
        testExceptions(parser);
        parser.close();
        wikiReader.close();
    }

    public void testExceptionsStructure() throws Exception {
        JsonParser parser = Json.createParser(JsonBuilderTest.buildPerson());
        testExceptions(parser);
        parser.close();
    }

    static void testExceptions(JsonParser parser) {
        Iterator<Event> it = parser.iterator();

        Event event = it.next();
        assertEquals(Event.START_OBJECT, event);

        try {
            parser.getString();
            fail("JsonParser#getString() should have thrown exception in START_OBJECT state");
        } catch(IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getNumberType();
            fail("JsonParser#getNumberType() should have thrown exception in START_OBJECT state");
        } catch(IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getIntValue();
            fail("JsonParser#getIntValue() should have thrown exception in START_OBJECT state");
        } catch(IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getLongValue();
            fail("JsonParser#getLongValue() should have thrown exception in START_OBJECT state");
        } catch(IllegalStateException expected) {
            // no-op
        }

        try {
            parser.getBigDecimalValue();
            fail("JsonParser#getBigDecimalValue() should have thrown exception in START_OBJECT state");
        } catch(IllegalStateException expected) {
            // no-op
        }
    }

}

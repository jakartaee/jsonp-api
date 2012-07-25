/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
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

//    TODO need to fix impl for this to work
//
//    public void testParser() {
//        JsonParser parser = new JsonParser(new StringReader(""));
//        assertFalse(parser.iterator().hasNext());
//        parser.close();
//    }
    
    public void testReader() {
        JsonParser reader = Json.createParser(new StringReader("{ \"a\" : \"b\", \"c\" : null, \"d\" :[null, \"abc\"] }"));
        reader.close();
    }

    @SuppressWarnings("unused")
    public void testEmptyArray() {
        JsonParser reader = Json.createParser(new StringReader("[]"));
        for(Event e : reader) {
        }
        reader.close();
    }

    public void testEmptyArray1() {
        JsonParser reader = Json.createParser(new StringReader("[]"));
        Iterator<Event> it = reader.iterator();
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
        reader.close();
    }

    public void testEmptyArray2() {
        JsonParser reader = Json.createParser(new StringReader("[]"));
        Iterator<Event> it = reader.iterator();
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
        reader.close();
    }

    public void testEmptyArray3() {
        JsonParser reader = Json.createParser(new StringReader("[]"));
        Iterator<Event> it = reader.iterator();
        assertEquals(Event.START_ARRAY, it.next());
        assertEquals(Event.END_ARRAY, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
        reader.close();
    }

    @SuppressWarnings("unused")
    public void testEmptyObject() {
        JsonParser reader = Json.createParser(new StringReader("{}"));
        for(Event e : reader) {
        }
        reader.close();
    }

    public void testEmptyObject1() {
        JsonParser reader = Json.createParser(new StringReader("{}"));
        Iterator<Event> it = reader.iterator();
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
        reader.close();
    }

    public void testEmptyObject2() {
        JsonParser reader = Json.createParser(new StringReader("{}"));
        Iterator<Event> it = reader.iterator();
        assertEquals(Event.START_OBJECT, it.next());
        assertEquals(Event.END_OBJECT, it.next());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
        reader.close();
    }

    public void testEmptyObject3() {
        JsonParser reader = Json.createParser(new StringReader("{}"));
        Iterator<Event> it = reader.iterator();
        assertEquals(Event.START_OBJECT, it.next());
        assertEquals(Event.END_OBJECT, it.next());
        assertEquals(false, it.hasNext());
        try {
            it.next();
            fail("Should have thrown a NoSuchElementException");
        } catch(NoSuchElementException ne) {
        }
        reader.close();
    }

    @SuppressWarnings("unused")
    public void testWikiReaderIterator() throws Exception {
        Reader wikiReader = new InputStreamReader(getClass().getResourceAsStream("/wiki.json"));
        JsonParser reader = Json.createParser(wikiReader);
        for(Event e : reader) {
        }
        reader.close();
        wikiReader.close();
    }
    
    
    public void testWikiReader() throws Exception {
        Reader wikiReader = new InputStreamReader(getClass().getResourceAsStream("/wiki.json"));
        JsonParser reader = Json.createParser(wikiReader);
        testWikiReader(reader);
        reader.close();
        wikiReader.close();
    }

    public void testWikiObjectParser() throws Exception {
        JsonParser reader = Json.createParser(JsonBuilderTest.buildPerson());
        testWikiReader(reader);
        reader.close();
    }

    static void testWikiReader(JsonParser reader) {
        Iterator<Event> it = reader.iterator();

        Event event = it.next();
        assertEquals(Event.START_OBJECT, event);

        testObjectStringValue(reader, it, "firstName", "John");
        testObjectStringValue(reader, it, "lastName", "Smith");

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("age", reader.getString());

        event = it.next();
        assertEquals(Event.VALUE_NUMBER, event);
        assertEquals(25, reader.getNumber().getIntValue());

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("address", reader.getString());

        event = it.next();
        assertEquals(Event.START_OBJECT, event);


        testObjectStringValue(reader, it, "streetAddress", "21 2nd Street");
        testObjectStringValue(reader, it, "city", "New York");
        testObjectStringValue(reader, it, "state", "NY");
        testObjectStringValue(reader, it, "postalCode", "10021");

        event = it.next();
        assertEquals(Event.END_OBJECT, event);

        event = it.next();
        assertEquals(Event.KEY_NAME, event);
        assertEquals("phoneNumber", reader.getString());

        event = it.next();
        assertEquals(Event.START_ARRAY, event);
        event = it.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(reader, it, "type", "home");
        testObjectStringValue(reader, it, "number", "212 555-1234");
        event = it.next();
        assertEquals(Event.END_OBJECT, event);

        event = it.next();
        assertEquals(Event.START_OBJECT, event);
        testObjectStringValue(reader, it, "type", "fax");
        testObjectStringValue(reader, it, "number", "646 555-4567");
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

}

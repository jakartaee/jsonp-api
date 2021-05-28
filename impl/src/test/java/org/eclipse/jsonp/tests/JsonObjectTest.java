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

import junit.framework.TestCase;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.json.*;

/**
 * @author Jitendra Kotamraju
 */
public class JsonObjectTest extends TestCase {
    public JsonObjectTest(String testName) {
        super(testName);
    }

    public void test() {
    }

    public void testEmptyObjectEquals() throws Exception {
        JsonObject empty1 = Json.createObjectBuilder()
                .build();

        JsonObject empty2 = Json.createObjectBuilder()
                .build();

        assertEquals(empty1, empty2);
    }

    public void testPersonObjectEquals() throws Exception {
        JsonObject person1 = JsonBuilderTest.buildPerson();
        JsonObject person2 = JsonReaderTest.readPerson();

        assertEquals(person1, person2);
    }

    public void testGetStringOrDefault() throws Exception {
        JsonObject object = Json.createObjectBuilder()
                .add("string", "value")
                .add("number", 25)
                .add("boolean", false)
                .build();
        assertEquals("value", object.getString("string", "default"));
        assertEquals("default", object.getString("missing", "default"));
        assertEquals("default", object.getString("number", "default"));
    }

    public void testGetIntOrDefault() throws Exception {
        JsonObject object = Json.createObjectBuilder()
                .add("string", "value")
                .add("number", 25)
                .add("boolean", false)
                .build();
        assertEquals(25, object.getInt("number", 10));
        assertEquals(10, object.getInt("missing", 10));
        assertEquals(10, object.getInt("string", 10));
    }

    public void testGetBooleanOrDefault() throws Exception {
        JsonObject object = Json.createObjectBuilder()
                .add("string", "value")
                .add("number", 25)
                .add("boolean", false)
                .build();
        assertFalse(object.getBoolean("boolean", true));
        assertTrue(object.getBoolean("missing", true));
        assertTrue(object.getBoolean("string", true));
    }

    static void testPerson(JsonObject person) {
        assertEquals(5, person.size());
        assertEquals("John", person.getString("firstName"));
        assertEquals("Smith", person.getString("lastName"));
        assertEquals(25, person.getJsonNumber("age").intValue());
        assertEquals(25, person.getInt("age"));

        JsonObject address = person.getJsonObject("address");
        assertEquals(4, address.size());
        assertEquals("21 2nd Street", address.getString("streetAddress"));
        assertEquals("New York", address.getString("city"));
        assertEquals("NY", address.getString("state"));
        assertEquals("10021", address.getString("postalCode"));

        JsonArray phoneNumber = person.getJsonArray("phoneNumber");
        assertEquals(2, phoneNumber.size());
        JsonObject home = phoneNumber.getJsonObject(0);
        assertEquals(2, home.size());
        assertEquals("home", home.getString("type"));
        assertEquals("212 555-1234", home.getString("number"));
        assertEquals("212 555-1234", home.getString("number"));

        JsonObject fax = phoneNumber.getJsonObject(1);
        assertEquals(2, fax.size());
        assertEquals("fax", fax.getString("type"));
        assertEquals("646 555-4567", fax.getString("number"));

        assertEquals("\"646 555-4567\"", fax.getJsonString("number").toString());
    }

    static void testEmpty(JsonObject empty) {
        assertTrue(empty.isEmpty());
    }

    public void testClassCastException() {
        JsonObject obj = Json.createObjectBuilder()
                .add("foo", JsonValue.FALSE).build();
        try {
            obj.getJsonNumber("foo");
            fail("Expected ClassCastException for casting JsonValue.FALSE to JsonNumber");
        } catch (ClassCastException ce) {
            // Expected
        }
    }

    public void testPut() {
        JsonObject obj = Json.createObjectBuilder().add("foo", 1).build();
        try {
            obj.put("bar", JsonValue.FALSE);
            fail("JsonObject#put() should throw UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // Expected
        }
    }

    public void testRemove() {
        JsonObject obj = Json.createObjectBuilder().add("foo", 1).build();
        try {
            obj.remove("foo");
            fail("JsonObject#remove() should throw UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // Expected
        }
    }

    public void testObjectBuilderWithVariousValues() {
        JsonObject expected = Json.createObjectBuilder()
                .add("a", JsonValue.TRUE)
                .add("b", JsonValue.FALSE)
                .add("c", JsonValue.NULL)
                .add("d", Integer.MAX_VALUE)
                .add("e", Long.MAX_VALUE)
                .add("f", Double.MAX_VALUE)
                .add("g", Integer.MIN_VALUE)
                .add("h", Long.MIN_VALUE)
                .add("i", Double.MIN_VALUE)
                .add("j", Json.createArrayBuilder().add("abc"))
                .add("k", Json.createObjectBuilder().add("one", 1))
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.writeObject(expected);
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(sw.toString()));
        JsonObject actual = reader.readObject();
        reader.close();

        assertEquals(expected, actual);
    }

    public void testObjectBuilderWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", JsonValue.TRUE);
        map.put("b", JsonValue.FALSE);
        map.put("c", JsonValue.NULL);
        map.put("d", Integer.MAX_VALUE);
        map.put("e", Long.MAX_VALUE);
        map.put("f", Double.MAX_VALUE);
        map.put("g", Integer.MIN_VALUE);
        map.put("h", Long.MIN_VALUE);
        map.put("i", Double.MIN_VALUE);
        map.put("j", Json.createArrayBuilder().add("abc"));
        map.put("k", Json.createObjectBuilder().add("one", 1));

        JsonObject expected = Json.createObjectBuilder(map).build();

        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.writeObject(expected);
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(sw.toString()));
        JsonObject actual = reader.readObject();
        reader.close();

        assertEquals(expected, actual);
    }

    public void testObjectBuilderNpe() {
        try {
            JsonObject obj = Json.createObjectBuilder().add(null, 1).build();
            fail("JsonObjectBuilder#add(null, 1) should throw NullPointerException");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    public void testHashCode() {
        JsonObject object1 = Json.createObjectBuilder().add("a", 1).add("b", 2).add("c", 3).build();
        assertTrue(object1.hashCode() == object1.hashCode()); //1st call compute hashCode, 2nd call returns cached value

        JsonObject object2 = Json.createObjectBuilder().add("a", 1).add("b", 2).add("c", 3).build();
        assertTrue(object1.hashCode() == object2.hashCode());

        JsonObject object3 = Json.createObjectBuilder().build(); //org.eclipse.jsonp.JsonArrayBuilderImpl.JsonArrayImpl
        JsonObject object4 = JsonValue.EMPTY_JSON_OBJECT; //jakarta.json.EmptyObject

        assertTrue(object3.equals(object4));
        assertTrue(object3.hashCode() == object4.hashCode()); //equal instances have same hashCode
    }

}

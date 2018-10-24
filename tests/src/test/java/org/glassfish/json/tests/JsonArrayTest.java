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

import javax.json.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

/**
 * @author Jitendra Kotamraju
 */
public class JsonArrayTest extends TestCase {
    public JsonArrayTest(String testName) {
        super(testName);
    }

    public void testArrayEquals() throws Exception {
        JsonArray expected = Json.createArrayBuilder()
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

        StringWriter sw = new StringWriter();
        JsonWriter writer = Json.createWriter(sw);
        writer.writeArray(expected);
        writer.close();

        JsonReader reader = Json.createReader(new StringReader(sw.toString()));
        JsonArray actual = reader.readArray();
        reader.close();

        assertEquals(expected, actual);
    }

    public void testStringValue() throws Exception {
        JsonArray array = Json.createArrayBuilder()
                .add("John")
                .build();
        assertEquals("John", array.getString(0));
    }

    public void testIntValue() throws Exception {
        JsonArray array = Json.createArrayBuilder()
                .add(20)
                .build();
        assertEquals(20, array.getInt(0));
    }

    public void testAdd() {
        JsonArray array = Json.createArrayBuilder().build();
        try {
            array.add(JsonValue.FALSE);
            fail("JsonArray#add() should throw UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // Expected
        }
    }

    public void testRemove() {
        JsonArray array = Json.createArrayBuilder().build();
        try {
            array.remove(0);
            fail("JsonArray#remove() should throw UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // Expected
        }
    }

    public void testNumberView() throws Exception {
        JsonArray array = Json.createArrayBuilder().add(20).add(10).build();

        List<JsonNumber> numberList = array.getValuesAs(JsonNumber.class);
        for(JsonNumber num : numberList) {
            num.intValue();
        }

        assertEquals(20, array.getInt(0));
        assertEquals(10, array.getInt(1));
    }

    public void testArrayBuilderNpe() {
        try {
            JsonArray array = Json.createArrayBuilder().add((JsonValue)null).build();
            fail("JsonArrayBuilder#add(null) should throw NullPointerException");
        } catch(NullPointerException e) {
            // Expected
        }
    }

    public void testHashCode() {
        JsonArray array1 = Json.createArrayBuilder().add(1).add(2).add(3).build();
        assertTrue(array1.hashCode() == array1.hashCode()); //1st call compute hashCode, 2nd call returns cached value

        JsonArray array2 = Json.createArrayBuilder().add(1).add(2).add(3).build();
        assertTrue(array1.hashCode() == array2.hashCode());

        JsonArray array3 = Json.createArrayBuilder().build(); //org.glassfish.json.JsonArrayBuilderImpl.JsonArrayImpl
        JsonArray array4 = JsonValue.EMPTY_JSON_ARRAY; //javax.json.EmptyArray

        assertTrue(array3.equals(array4));
        assertTrue(array3.hashCode() == array4.hashCode()); //equal instances have same hashCode
    }

}

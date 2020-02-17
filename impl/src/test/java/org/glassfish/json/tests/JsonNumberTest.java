/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.json.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Jitendra Kotamraju
 */
public class JsonNumberTest extends TestCase {
    public JsonNumberTest(String testName) {
        super(testName);
    }

    public void testFloating() throws Exception {
        JsonArray array1 = Json.createArrayBuilder().add(10.4).build();
        JsonReader reader = Json.createReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testBigDecimal() throws Exception {
        JsonArray array1 = Json.createArrayBuilder().add(new BigDecimal("10.4")).build();
        JsonReader reader = Json.createReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.get(0), array2.get(0));
        assertEquals(array1, array2);
    }

    public void testIntNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Integer.MIN_VALUE + 1)
                .add(Integer.MAX_VALUE - 1)
                .add(12)
                .add(12l)
                .add(new BigInteger("0"))
                .build();
        testNumberType(array1, true);

        StringReader sr = new StringReader("[" +
                "-2147483648, " +
                "2147483647, " +
                "-2147483647, " +
                "2147483646, " +
                "12, " +
                "12, " +
                "0 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, true);

        assertEquals(array1, array2);
    }

    private void testNumberType(JsonArray array, boolean integral) {
        for (JsonValue value : array) {
            assertEquals(integral, ((JsonNumber) value).isIntegral());
        }
    }

    public void testLongNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Long.MIN_VALUE + 1)
                .add(Long.MAX_VALUE - 1)
                .add((long) Integer.MIN_VALUE - 1)
                .add((long) Integer.MAX_VALUE + 1)
                .build();
        testNumberType(array1, true);

        StringReader sr = new StringReader("[" +
                "-9223372036854775808, " +
                "9223372036854775807, " +
                "-9223372036854775807, " +
                "9223372036854775806, " +
                "-2147483649, " +
                "2147483648 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, true);

        assertEquals(array1, array2);
    }


//    public void testBigIntegerNumberType() throws Exception {
//        JsonArray array1 = new JsonBuilder()
//            .startArray()
//                .add(new BigInteger("-9223372036854775809"))
//                .add(new BigInteger("9223372036854775808"))
//                .add(new BigInteger("012345678901234567890"))
//            .end()
//        .build();
//        testNumberType(array1, JsonNumber.NumberType.BIG_INTEGER);
//
//        StringReader sr = new StringReader("[" +
//            "-9223372036854775809, " +
//            "9223372036854775808, " +
//            "12345678901234567890 " +
//        "]");
//        JsonReader reader = new JsonReader(sr);
//        JsonArray array2 = reader.readArray();
//        reader.close();
//        testNumberType(array2, JsonNumber.NumberType.BIG_INTEGER);
//
//        assertEquals(array1, array2);
//    }

    public void testBigDecimalNumberType() throws Exception {
        JsonArray array1 = Json.createArrayBuilder()
                .add(12d)
                .add(12.0d)
                .add(12.1d)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
                .build();
        testNumberType(array1, false);

        StringReader sr = new StringReader("[" +
                "12.0, " +
                "12.0, " +
                "12.1, " +
                "4.9E-324, " +
                "1.7976931348623157E+308 " +
                "]");
        JsonReader reader = Json.createReader(sr);
        JsonArray array2 = reader.readArray();
        reader.close();
        testNumberType(array2, false);

        assertEquals(array1, array2);
    }

    public void testMinMax() throws Exception {
        JsonArray expected = Json.createArrayBuilder()
                .add(Integer.MIN_VALUE)
                .add(Integer.MAX_VALUE)
                .add(Long.MIN_VALUE)
                .add(Long.MAX_VALUE)
                .add(Double.MIN_VALUE)
                .add(Double.MAX_VALUE)
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

    public void testLeadingZeroes() {
        JsonArray array = Json.createArrayBuilder()
                .add(0012.1d)
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter jw = Json.createWriter(sw);
        jw.write(array);
        jw.close();

        assertEquals("[12.1]", sw.toString());
    }

    public void testBigIntegerExact() {
        try {
            JsonArray array = Json.createArrayBuilder().add(12345.12345).build();
            array.getJsonNumber(0).bigIntegerValueExact();
            fail("Expected Arithmetic exception");
        } catch (ArithmeticException expected) {
            // no-op
        }
    }

    public void testHashCode() {
        JsonNumber jsonNumber1 = Json.createValue(1);
        assertTrue(jsonNumber1.hashCode() == jsonNumber1.bigDecimalValue().hashCode());

        JsonNumber jsonNumber2 = Json.createValue(1);

        assertTrue(jsonNumber1.equals(jsonNumber2));
        assertTrue(jsonNumber1.hashCode() == jsonNumber2.hashCode());
    }

}

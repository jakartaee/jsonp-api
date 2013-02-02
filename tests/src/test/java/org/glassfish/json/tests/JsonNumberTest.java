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
        testNumberType(array1, JsonNumber.NumberType.INTEGER);

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
        testNumberType(array2, JsonNumber.NumberType.INTEGER);

        assertEquals(array1, array2);
    }

    private void testNumberType(JsonArray array, JsonNumber.NumberType numberType) {
        for (JsonValue value : array) {
            assertEquals(numberType, ((JsonNumber) value).getNumberType());
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
        testNumberType(array1, JsonNumber.NumberType.INTEGER);

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
        testNumberType(array2, JsonNumber.NumberType.INTEGER);

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
        testNumberType(array1, JsonNumber.NumberType.DECIMAL);

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
        testNumberType(array2, JsonNumber.NumberType.DECIMAL);

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
            array.getValue(0, JsonNumber.class).getBigIntegerValueExact();
            fail("Expected Arithmetic exception");
        } catch (ArithmeticException expected) {
            // no-op
        }
    }


}
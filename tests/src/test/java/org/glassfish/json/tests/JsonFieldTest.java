/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;

/**
 * Test for writing json field names without values.
 *
 * @author Roman Grigoriadi
 */
public class JsonFieldTest extends TestCase {

    public void testFieldAsOnlyMember() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("fName");
        generator.write("fValue");
        generator.writeEnd();

        generator.close();
        assertEquals("{\"fName\":\"fValue\"}", sw.toString());
    }

    public void testFieldAsFirstMember() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("f1Name");
        generator.write("f1Value");
        generator.write("f2Name", "f2Value");
        generator.writeEnd();

        generator.close();
        assertEquals("{\"f1Name\":\"f1Value\",\"f2Name\":\"f2Value\"}", sw.toString());
    }

    public void testFieldAsLastMember() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.write("f1Name", "f1Value");
        generator.writeKey("f2Name");
        generator.write("f2Value");
        generator.writeEnd();

        generator.close();
        assertEquals("{\"f1Name\":\"f1Value\",\"f2Name\":\"f2Value\"}", sw.toString());
    }


    public void testFieldObject() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("f1Name");
        generator.writeStartObject();
        generator.write("innerFieldName", "innerFieldValue");
        generator.writeEnd();
        generator.write("f2Name", "f2Value");
        generator.writeEnd();

        generator.close();
        assertEquals("{\"f1Name\":{\"innerFieldName\":\"innerFieldValue\"},\"f2Name\":\"f2Value\"}", sw.toString());
    }

    public void testFieldArray() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("f1Name");
        generator.writeStartArray();
        generator.write("arrayValue");
        generator.writeEnd();
        generator.write("f2Name", "f2Value");
        generator.writeEnd();

        generator.close();
        assertEquals("{\"f1Name\":[\"arrayValue\"],\"f2Name\":\"f2Value\"}", sw.toString());
    }

    public void testFailFieldInField() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("f1Name");

        try {
            generator.write("f2Name", "f2Value");
            fail("Field value, start object/array expected");
        } catch (JsonGenerationException exception) {
            //ok
        }
    }


    public void testFailFieldKeyInArray() {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartArray();

        try {
            generator.writeKey("f1Value");
            fail("Not allowed in array .");
        } catch (JsonGenerationException exception) {
            //ok
        }
    }

    public void  testWriteString() {
        assertEquals("{\"f1Name\":\"f1Value\"}", writeValue((gen)->gen.write("f1Value")));
    }

    public void  testWriteBigDec() {
        assertEquals("{\"f1Name\":10}", writeValue((gen)->gen.write(BigDecimal.TEN)));
    }

    public void  testWriteBigInt() {
        assertEquals("{\"f1Name\":10}", writeValue((gen)->gen.write(BigInteger.TEN)));
    }

    public void  testWriteBool() {
        assertEquals("{\"f1Name\":true}", writeValue((gen)->gen.write(true)));
    }

    public void  testWriteInt() {
        assertEquals("{\"f1Name\":10}", writeValue((gen)->gen.write(10)));
    }

    public void  testWriteLong() {
        assertEquals("{\"f1Name\":10}", writeValue((gen)->gen.write(10L)));
    }

    public void  testWriteDouble() {
        assertEquals("{\"f1Name\":10.0}", writeValue((gen)->gen.write(10d)));
    }

    public void  testWriteNull() {
        assertEquals("{\"f1Name\":null}", writeValue(JsonGenerator::writeNull));
    }

    public void  testWriteJsonValue() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("first", "value");
        final JsonObject build = builder.build();
        assertEquals("{\"f1Name\":\"value\"}", writeValue((gen)->gen.write(build.getValue("/first"))));
    }

    private String writeValue(WriteValueFunction writeValueCallback) {
        StringWriter sw = new StringWriter();
        JsonGenerator generator = Json.createGenerator(sw);

        generator.writeStartObject();
        generator.writeKey("f1Name");
        writeValueCallback.writeValue(generator);
        generator.writeEnd();
        generator.close();
        return sw.toString();
    }

    private interface WriteValueFunction {
        void writeValue(JsonGenerator generator);
    }
}

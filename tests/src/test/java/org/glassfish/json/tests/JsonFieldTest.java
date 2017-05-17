/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

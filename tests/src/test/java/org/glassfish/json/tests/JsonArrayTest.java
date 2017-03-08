/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
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

}

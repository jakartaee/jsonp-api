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

import javax.json.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * @author Jitendra Kotamraju
 */
public class JsonNumberTest extends TestCase {
    public JsonNumberTest(String testName) {
        super(testName);
    }

    public void testFloating() throws Exception {
        JsonArray array1 = new JsonBuilder().beginArray().add(10.4).endArray().build();
        JsonReader reader = new JsonReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.getValue(0), array2.getValue(0));
        assertEquals(array1, array2);
    }

    public void testBigDecimal() throws Exception {
        JsonArray array1 = new JsonBuilder().beginArray().add(new BigDecimal("10.4")).endArray().build();
        JsonReader reader = new JsonReader(new StringReader("[10.4]"));
        JsonArray array2 = reader.readArray();

        assertEquals(array1.getValue(0), array2.getValue(0));
        assertEquals(array1, array2);
    }

    public void testNumberType() throws Exception {
        JsonArray array = new JsonBuilder().beginArray().add(12.0).endArray().build();
        assertEquals(JsonNumber.JsonNumberType.INT, array.getValue(0, JsonNumber.class).getNumberType());
    }

    public void testMinMax() throws Exception {
        JsonArray expected = new JsonBuilder()
                .beginArray()
                    .add(Integer.MIN_VALUE)
                    .add(Integer.MAX_VALUE)
                    .add(Long.MIN_VALUE)
                    .add(Long.MAX_VALUE)
                    .add(Double.MIN_VALUE)
                    .add(Double.MAX_VALUE)
                .endArray()
                .build();

        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.writeArray(expected);
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(sw.toString()));
        JsonArray actual = reader.readArray();
        reader.close();

        assertEquals(expected, actual);
    }

}
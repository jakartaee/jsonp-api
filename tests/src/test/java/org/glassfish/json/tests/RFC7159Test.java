/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2017 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringWriter;
import java.io.StringReader;

/**
 * @author Kin-man Chung
 */
public class RFC7159Test {

    @Test
    public void testCreatValues() {
        JsonArrayBuilder builder = Json.createArrayBuilder();
        JsonArray array = builder.add(Json.createValue("someString"))
                                 .add(Json.createValue(100))
                                 .add(Json.createValue(12345.6789))
                                 .build();
        builder = Json.createArrayBuilder();
        JsonArray expected = builder.add("someString")
                                    .add(100)
                                    .add(12345.6789)
                                    .build();
        assertEquals(expected, array);
    }

    @Test
    public void testReadValues() {
        JsonReader reader = Json.createReader(new StringReader("\"someString\""));
        JsonArrayBuilder builder = Json.createArrayBuilder();
        builder.add(reader.readValue());
        reader = Json.createReader(new StringReader("100"));
        builder.add(reader.readValue());
        reader = Json.createReader(new StringReader("12345.6789"));
        builder.add(reader.readValue());
        JsonArray array = builder.build();
        builder = Json.createArrayBuilder();
        JsonArray expected = builder.add("someString")
                                    .add(100)
                                    .add(12345.6789)
                                    .build();
        assertEquals(expected, array);
    }

    @Test
    public void testWriteValues() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = Json.createWriter(stringWriter);
        writer.write(Json.createValue("someString"));
        assertEquals("\"someString\"", stringWriter.toString());

        stringWriter = new StringWriter();
        writer = Json.createWriter(stringWriter);
        writer.write(Json.createValue(100));
        assertEquals("100", stringWriter.toString());

        stringWriter = new StringWriter();
        writer = Json.createWriter(stringWriter);
        writer.write(Json.createValue(12345.6789));
        assertEquals("12345.6789", stringWriter.toString());
    }

    @Test
    public void testGeneratorValues() {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = Json.createGenerator(stringWriter);
        generator.write("someString").close();
        assertEquals("\"someString\"", stringWriter.toString());

        stringWriter = new StringWriter();
        generator = Json.createGenerator(stringWriter);
        generator.write(100).close();
        assertEquals("100", stringWriter.toString());

        stringWriter = new StringWriter();
        generator = Json.createGenerator(stringWriter);
        generator.write(12345.6789).close();
        assertEquals("12345.6789", stringWriter.toString());
    }
}

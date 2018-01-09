/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved.
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

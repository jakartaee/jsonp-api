/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * {@link JsonGenerator} tests
 *
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorTest extends TestCase {
    public JsonGeneratorTest(String testName) {
        super(testName);
    }

    public void testObjectWriter() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        testObject(generator);
        generator.close();
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(writer.toString()));
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
    }

    public void testObjectStream() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonGenerator generator = Json.createGenerator(out);
        testObject(generator);
        generator.close();
        out.close();

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        JsonReader reader = new JsonReader(in);
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
        reader.close();
        in.close();
    }

    private void testObject(JsonGenerator generator) throws Exception {
        generator
            .beginObject()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .beginObject("address")
                    .add("streetAddress", "21 2nd Street")
                    .add("city", "New York")
                    .add("state", "NY")
                    .add("postalCode", "10021")
                .endObject()
                .beginArray("phoneNumber")
                    .beginObject()
                        .add("type", "home")
                        .add("number", "212 555-1234")
                    .endObject()
                    .beginObject()
                        .add("type", "fax")
                        .add("number", "646 555-4567")
                    .endObject()
                .endArray()
            .endObject();
    }

    public void testArray() throws Exception {
        JsonGenerator generator = Json.createGenerator(new StringWriter());
        generator
            .beginArray()
                .beginObject()
                    .add("type", "home")
                    .add("number", "212 555-1234")
                .endObject()
                .beginObject()
                    .add("type", "fax")
                    .add("number", "646 555-4567")
                .endObject()
            .endArray();
        generator.close();
    }

    // tests JsonGenerator when JsonValue is used for generation
    public void testJsonValue() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator
            .beginObject()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", JsonBuilderTest.buildAddress())
                .add("phoneNumber", JsonBuilderTest.buildPhone())
            .endObject();
        generator.close();
        writer.close();

        JsonReader reader = new JsonReader(new StringReader(writer.toString()));
        JsonObject person = reader.readObject();
        JsonObjectTest.testPerson(person);
    }

    public void testArrayString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.beginArray().add("string").endArray();
        generator.close();
        writer.close();

        assertEquals("[\"string\"]", writer.toString());
    }

    public void testEscapedString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = Json.createGenerator(writer);
        generator.beginArray().add("\u0000").endArray();
        generator.close();
        writer.close();

        assertEquals("[\"\\u0000\"]", writer.toString());
    }

}
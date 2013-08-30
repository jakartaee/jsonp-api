/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.*;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * @author Jitendra Kotamraju
 */
public class JsonWriterTest extends TestCase {
    public JsonWriterTest(String testName) {
        super(testName);
    }

    public void testObject() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeObject(Json.createObjectBuilder().build());
        jsonWriter.close();
        writer.close();

        assertEquals("{}", writer.toString());
    }

    public void testArray() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().build());
        jsonWriter.close();
        writer.close();

        assertEquals("[]", writer.toString());
    }

    public void testNumber() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add(10).build());
        jsonWriter.close();
        writer.close();

        assertEquals("[10]", writer.toString());
    }

    public void testDoubleNumber() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add(10.5).build());
        jsonWriter.close();
        writer.close();

        assertEquals("[10.5]", writer.toString());
    }

    public void testArrayString() throws Exception {
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(writer);
        jsonWriter.writeArray(Json.createArrayBuilder().add("string").build());
        jsonWriter.close();
        writer.close();

        assertEquals("[\"string\"]", writer.toString());
    }

    public void testIllegalStateExcepton() throws Exception {
        JsonObject obj = Json.createObjectBuilder().build();
        JsonArray array = Json.createArrayBuilder().build();

        JsonWriter writer = Json.createWriter(new StringWriter());
        writer.writeObject(obj);
        try {
            writer.writeObject(obj);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();

        writer = Json.createWriter(new StringWriter());
        writer.writeArray(array);
        try {
            writer.writeArray(array);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();

        writer = Json.createWriter(new StringWriter());
        writer.write(array);
        try {
            writer.writeArray(array);
        } catch (IllegalStateException expected) {
            // no-op
        }
        writer.close();
    }

// Doesn't work. We expect JsonWriter#close() to be called
//
//    public void testFlushBuffer() throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        JsonWriter writer = Json.createWriter(baos);
//        writer.write(Json.createObjectBuilder().build());
//        // not calling writer.close() intentionally
//        assertEquals("{}", baos.toString("UTF-8"));
//    }
}

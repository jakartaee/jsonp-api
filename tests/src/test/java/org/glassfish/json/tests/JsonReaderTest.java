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

import junit.framework.TestCase;
import org.glassfish.json.api.BufferPool;

import javax.json.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
public class JsonReaderTest extends TestCase {
    public JsonReaderTest(String testName) {
        super(testName);
    }

    public void testObject() throws Exception {
        JsonObject person = readPerson();
        JsonObjectTest.testPerson(person);
    }

    public void testEscapedString() throws Exception {
        // u00ff is escaped once, not escaped once
        JsonReader reader = Json.createReader(new StringReader("[\"\\u0000\\u00ff\u00ff\"]"));
        JsonArray array = reader.readArray();
        reader.close();
        String str = array.getString(0);
        assertEquals("\u0000\u00ff\u00ff", str);
    }

    public void testUnknownFeature() throws Exception {
        Map<String, Object> config = new HashMap<>();
        config.put("foo", true);
        JsonReaderFactory factory = Json.createReaderFactory(config);
        factory.createReader(new StringReader("{}"));
        Map<String, ?> config1 = factory.getConfigInUse();
        if (config1.size() > 0) {
            fail("Shouldn't have any config in use");
        }
    }

    public void testIllegalStateExcepton() throws Exception {
        JsonReader reader = Json.createReader(new StringReader("{}"));
        reader.readObject();
        try {
            reader.readObject();
        } catch (IllegalStateException expected) {
            // no-op
        }
        reader.close();

        reader = Json.createReader(new StringReader("[]"));
        reader.readArray();
        try {
            reader.readArray();
        } catch (IllegalStateException expected) {
            // no-op
        }
        reader.close();

        reader = Json.createReader(new StringReader("{}"));
        reader.read();
        try {
            reader.read();
        } catch (IllegalStateException expected) {
            // no-op
        }
        reader.close();
    }

    static JsonObject readPerson() throws Exception {
        Reader wikiReader = new InputStreamReader(JsonReaderTest.class.getResourceAsStream("/wiki.json"));
        JsonReader reader = Json.createReader(wikiReader);
        JsonValue value = reader.readObject();
        reader.close();
        return (JsonObject) value;
    }

    // JSONP-23 cached empty string is not reset
    public void testEmptyStringUsingStandardBuffer() throws Throwable {
        JsonReaderFactory factory = Json.createReaderFactory(null);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i < 40000; i++) {
            sb.append('a');
            String name = sb.toString();
            String str = "[1, \"\", \""+name+"\", \"\", \""+name+"\", \"\", 100]";
            try {
                JsonReader reader = factory.createReader(new StringReader(str));
                JsonArray array = reader.readArray();
                assertEquals(1, array.getInt(0));
                assertEquals("", array.getString(1));
                assertEquals(name, array.getString(2));
                assertEquals("", array.getString(3));
                assertEquals(name, array.getString(4));
                assertEquals("", array.getString(5));
                assertEquals(100, array.getInt(6));
                reader.close();
            } catch (Throwable t) {
                throw new Throwable("Failed for name length="+i, t);
            }
        }
    }

    // JSONP-23 cached empty string is not reset
    public void testEmptyStringUsingBuffers() throws Throwable {
        for(int size=20; size < 500; size++) {
            final JsonParserTest.MyBufferPool bufferPool = new JsonParserTest.MyBufferPool(size);
            Map<String, Object> config = new HashMap<String, Object>() {{
                put(BufferPool.class.getName(), bufferPool);
            }};
            JsonReaderFactory factory = Json.createReaderFactory(config);

            StringBuilder sb = new StringBuilder();
            for(int i=0; i < 1000; i++) {
                sb.append('a');
                String name = sb.toString();
                String str = "[1, \"\", \""+name+"\", \"\", \""+name+"\", \"\", 100]";
                try {
                    JsonReader reader = factory.createReader(new StringReader(str));
                    JsonArray array = reader.readArray();
                    assertEquals(1, array.getInt(0));
                    assertEquals("", array.getString(1));
                    assertEquals(name, array.getString(2));
                    assertEquals("", array.getString(3));
                    assertEquals(name, array.getString(4));
                    assertEquals("", array.getString(5));
                    assertEquals(100, array.getInt(6));
                    reader.close();
                } catch (Throwable t) {
                    throw new Throwable("Failed for buffer size="+size+" name length="+i, t);
                }
            }
        }
    }

}

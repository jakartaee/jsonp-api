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

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * 
 * @author Alex Soto
 *
 */
@RunWith(Parameterized.class)
public class JsonPointerTest {

    private static JsonObject rfc6901Example;

    @Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> data() throws Exception {
        rfc6901Example = JsonPointerTest.readRfc6901Example();
        return Arrays.asList(new Object[][] { 
                 {Json.createPointer(""), rfc6901Example, null },
                 {Json.createPointer("/foo"), rfc6901Example.getJsonArray("foo"), null},
                 {Json.createPointer("/foo/0"), rfc6901Example.getJsonArray("foo").get(0), null},
                 {Json.createPointer("/foo/5"), null, JsonException.class},
                 {Json.createPointer("/p/1"), null, JsonException.class},
                 {Json.createPointer("/"), rfc6901Example.getJsonNumber(""), null},
                 {Json.createPointer("/a~1b"), rfc6901Example.getJsonNumber("a/b"), null},
                 {Json.createPointer("/m~0n"), rfc6901Example.getJsonNumber("m~n"), null},
                 {Json.createPointer("/c%d"), rfc6901Example.getJsonNumber("c%d"), null},
                 {Json.createPointer("/e^f"), rfc6901Example.getJsonNumber("e^f"), null},
                 {Json.createPointer("/g|h"), rfc6901Example.getJsonNumber("g|h"), null},
                 {Json.createPointer("/i\\j"), rfc6901Example.getJsonNumber("i\\j"), null},
                 {Json.createPointer("/k\"l"), rfc6901Example.getJsonNumber("k\"l"), null},
                 {Json.createPointer("/ "), rfc6901Example.getJsonNumber(" "), null},
                 {Json.createPointer("/notexists"), null, JsonException.class},
                 {Json.createPointer("/s/t"), null, JsonException.class},
                 {Json.createPointer("/o"), JsonObject.NULL, null}
           });
    }

    private JsonPointer pointer;
    private JsonValue expected;
    private Class<? extends Exception> expectedException;

    public JsonPointerTest(JsonPointer pointer, JsonValue expected, Class<? extends Exception> expectedException) {
        super();
        this.pointer = pointer;
        this.expected = expected;
        this.expectedException = expectedException;
    }

    @Test
    public void shouldEvaluateJsonPointerExpressions() {
        try {
            JsonValue result = pointer.getValue(rfc6901Example);
            assertThat(result, is(expected));
            assertThat(expectedException, nullValue());
        } catch(Exception e) {
            if(expectedException == null) {
                fail(e.getMessage());
            } else {
                assertThat(e, instanceOf(expectedException));
            }
        }
    }

    static JsonObject readRfc6901Example() throws Exception {
        Reader rfc6901Reader = new InputStreamReader(JsonReaderTest.class.getResourceAsStream("/rfc6901.json"));
        JsonReader reader = Json.createReader(rfc6901Reader);
        JsonValue value = reader.readObject();
        reader.close();
        return (JsonObject) value;
    }
}

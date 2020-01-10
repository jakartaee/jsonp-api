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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;

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
public class JsonMergePatchTest {

        @Parameters(name = "{index}: ({0})={1}")
        public static Iterable<Object[]> data() throws Exception {
            List<Object[]> examples = new ArrayList<Object[]>();
            JsonArray data = loadData();
            for (JsonValue jsonValue : data) {
                JsonObject test = (JsonObject) jsonValue;
                Object[] testData = new Object[4];
                testData[0] = test.get("patch");
                testData[1] = test.get("target");
                testData[2] = test.get("expected");
                testData[3] = createExceptionClass((JsonString)test.get("exception"));

                examples.add(testData);
            }

            return examples;
        }

        private static Class<? extends Exception> createExceptionClass(
                JsonString exceptionClassName) throws ClassNotFoundException {
            if (exceptionClassName != null) {
                return (Class<? extends Exception>) Class
                        .forName(exceptionClassName.getString());
            }
            return null;
        }

        private static JsonArray loadData() {
            InputStream testData = JsonPatchTest.class
                    .getResourceAsStream("/jsonmergepatch.json");
            JsonReader reader = Json.createReader(testData);
            JsonArray data = (JsonArray) reader.read();
            return data;
        }

        private JsonValue patch;
        private JsonValue target;
        private JsonValue expected;
        private Class<? extends Exception> expectedException;

        public JsonMergePatchTest(JsonValue patch, JsonValue target,
                JsonValue expected, Class<? extends Exception> expectedException) {
            super();
            this.patch = patch;
            this.target = target;
            this.expected = expected;
            this.expectedException = expectedException;
        }

        @Test
        public void shouldExecuteJsonMergePatchDiffOperationsToJsonDocument() {
            try {
                JsonValue output = Json.createMergePatch(patch).apply(target);
                assertThat(output, is(expected));
                assertThat(expectedException, nullValue());
            } catch (Exception e) {
                if (expectedException == null) {
                    fail(e.getMessage());
                } else {
                    assertThat(e, instanceOf(expectedException));
                }
            }
        }

}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
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
public class JsonMergePatchDiffTest {

    @Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> data() throws Exception {
        List<Object[]> examples = new ArrayList<Object[]>();
        JsonArray data = loadData();
        for (JsonValue jsonValue : data) {
            JsonObject test = (JsonObject) jsonValue;
            Object[] testData = new Object[4];
            testData[0] = test.get("original");
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
                .getResourceAsStream("/jsonmergepatchdiff.json");
        JsonReader reader = Json.createReader(testData);
        JsonArray data = (JsonArray) reader.read();
        return data;
    }

    private JsonValue original;
    private JsonValue target;
    private JsonValue expected;
    private Class<? extends Exception> expectedException;

    public JsonMergePatchDiffTest(JsonValue original, JsonValue target,
            JsonValue expected, Class<? extends Exception> expectedException) {
        super();
        this.original = original;
        this.target = target;
        this.expected = expected;
        this.expectedException = expectedException;
    }
    @Test
    public void shouldExecuteJsonMergePatchOperationsToJsonDocument() {
        try {
            JsonValue output = JsonMergePatch.diff(original, target);
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

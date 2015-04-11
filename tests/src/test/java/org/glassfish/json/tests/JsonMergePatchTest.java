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
        public void shouldExecuteJsonMergePatchOperationsToJsonDocument() {
            try {
                JsonValue output = JsonMergePatch.mergePatch(target, patch);
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

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

@RunWith(Parameterized.class)
public class JsonPointerTest {

    private static JsonObject rfc6901Example;

    @Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> data() throws Exception {
        rfc6901Example = JsonPointerTest.readRfc6901Example();
        return Arrays.asList(new Object[][] { 
                 {new JsonPointer(""), rfc6901Example, null },
                 {new JsonPointer("/foo"), rfc6901Example.getJsonArray("foo"), null},
                 {new JsonPointer("/foo/0"), rfc6901Example.getJsonArray("foo").get(0), null},
                 {new JsonPointer("/foo/5"), null, JsonException.class},
                 {new JsonPointer("/p/1"), null, JsonException.class},
                 {new JsonPointer("/"), rfc6901Example.getJsonNumber(""), null},
                 {new JsonPointer("/a~1b"), rfc6901Example.getJsonNumber("a/b"), null},
                 {new JsonPointer("/m~0n"), rfc6901Example.getJsonNumber("m~n"), null},
                 {new JsonPointer("/c%d"), rfc6901Example.getJsonNumber("c%d"), null},
                 {new JsonPointer("/e^f"), rfc6901Example.getJsonNumber("e^f"), null},
                 {new JsonPointer("/g|h"), rfc6901Example.getJsonNumber("g|h"), null},
                 {new JsonPointer("/i\\j"), rfc6901Example.getJsonNumber("i\\j"), null},
                 {new JsonPointer("/k\"l"), rfc6901Example.getJsonNumber("k\"l"), null},
                 {new JsonPointer("/ "), rfc6901Example.getJsonNumber(" "), null},
                 {new JsonPointer("/notexists"), null, JsonException.class},
                 {new JsonPointer("/s/t"), null, NumberFormatException.class},
                 {new JsonPointer("/o"), JsonObject.NULL, null}
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

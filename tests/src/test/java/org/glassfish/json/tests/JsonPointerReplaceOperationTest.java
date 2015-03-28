package org.glassfish.json.tests;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JsonPointerReplaceOperationTest {

    @Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> data() throws Exception {
        return Arrays.asList(new Object[][] { 
                 {buildSimpleReplacePatch(), buildAddress(), buildExpectedAddress(), null},
                 {buildComplexReplacePatch(), buildPerson(), buildExpectedPerson(), null},
                 {buildArrayReplacePatchInPosition(), buildPerson(), buildExpectedPersonConcreteArrayPosition(), null},
                 {buildArrayAddPatchInLastPosition(), buildPerson(), null, JsonException.class},
                 {buildNoneExistingReplacePatch(), buildAddress(), null, JsonException.class}
           });
    }

    private JsonObject pathOperation;
    private JsonStructure target;
    private JsonValue expectedResult;
    private Class<? extends Exception> expectedException;

    public JsonPointerReplaceOperationTest(JsonObject pathOperation,
            JsonStructure target, JsonValue expectedResult, Class<? extends Exception> expectedException) {
        super();
        this.pathOperation = pathOperation;
        this.target = target;
        this.expectedResult = expectedResult;
        this.expectedException = expectedException;
    }

    @Test
    public void shouldReplaceElementsToExistingJsonDocument() {
        try {
        JsonPointer pointer = new JsonPointer(pathOperation.getString("path"));
        JsonObject modified = (JsonObject) pointer.replace(target, pathOperation.get("value"));
        assertThat(modified, is(expectedResult));
        assertThat(expectedException, nullValue());
        } catch(Exception e) {
            if(expectedException == null) {
                fail(e.getMessage());
            } else {
                assertThat(e, instanceOf(expectedException));
            }
        }
    }

    static JsonObject buildAddress() {
        return Json.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021")
                .build();
    }
    static JsonObject buildComplexReplacePatch() {
        return Json.createObjectBuilder()
                .add("op", "add")
                .add("path", "/address/streetAddress")
                .add("value", "myaddress")
                .build();
    }
    static JsonObject buildSimpleReplacePatch() {
        return Json.createObjectBuilder()
                .add("op", "replace")
                .add("path", "/streetAddress")
                .add("value", "myaddress")
                .build();
    }
    static JsonObject buildNoneExistingReplacePatch() {
        return Json.createObjectBuilder()
                .add("op", "replace")
                .add("path", "/notexists")
                .add("value", "myaddress")
                .build();
    }
    static JsonObject buildArrayReplacePatchInPosition() {
        return Json.createObjectBuilder()
                .add("op", "replace")
                .add("path", "/phoneNumber/0")
                .add("value", Json.createObjectBuilder()
                        .add("type", "home")
                        .add("number", "200 555-1234"))
                .build();
    }
    static JsonObject buildArrayAddPatchInLastPosition() {
        return Json.createObjectBuilder()
                .add("op", "add")
                .add("path", "/phoneNumber/-")
                .add("value", Json.createObjectBuilder()
                        .add("type", "home")
                        .add("number", "200 555-1234"))
                .build();
    }
    static JsonObject buildExpectedAddress() {
        return Json.createObjectBuilder()
                .add("streetAddress", "myaddress")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021")
                .build();
    }
    static JsonObject buildPerson() {
        return Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", Json.createObjectBuilder()
                        .add("streetAddress", "21 2nd Street")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
                .add("phoneNumber", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "212 555-1234"))
                        .add(Json.createObjectBuilder()
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();
    }
    static JsonObject buildExpectedPersonConcreteArrayPosition() {
        return Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", Json.createObjectBuilder()
                        .add("streetAddress", "21 2nd Street")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
                .add("phoneNumber", Json.createArrayBuilder()
                        .add((Json.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "200 555-1234")))
                        .add(Json.createObjectBuilder()
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();
    }
    static JsonObject buildExpectedPerson() {
        return Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", Json.createObjectBuilder()
                        .add("streetAddress", "myaddress")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
                .add("phoneNumber", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("type", "home")
                                .add("number", "212 555-1234"))
                        .add(Json.createObjectBuilder()
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();
    }
    
}

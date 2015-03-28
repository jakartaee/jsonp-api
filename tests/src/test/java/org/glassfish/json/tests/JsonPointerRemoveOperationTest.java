package org.glassfish.json.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonStructure;
import javax.json.JsonValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JsonPointerRemoveOperationTest {

    @Parameters(name = "{index}: ({0})={1}")
    public static Iterable<Object[]> data() throws Exception {
        return Arrays.asList(new Object[][] { 
                 {buildSimpleRemovePatch(), buildAddress(), buildExpectedRemovedAddress() },
                 {buildComplexRemovePatch(), buildPerson(), buildExpectedPersonWithoutStreetAddress()},
                 {buildArrayRemovePatchInPosition(), buildPerson(), buildPersonWithoutFirstPhone()}
           });
    }

    private JsonObject pathOperation;
    private JsonStructure target;
    private JsonValue expectedResult;

    public JsonPointerRemoveOperationTest(JsonObject pathOperation,
            JsonObject target, JsonValue expectedResult) {
        super();
        this.pathOperation = pathOperation;
        this.target = target;
        this.expectedResult = expectedResult;
    }

    @Test
    public void shouldRemoveElementsToExistingJsonDocument() {
        JsonPointer pointer = new JsonPointer(pathOperation.getString("path"));
        JsonObject modified = (JsonObject) pointer.remove(target);
        assertThat(modified, is(expectedResult));
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
    static JsonObject buildAddress() {
        return Json.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021")
                .build();
    }
    static JsonObject buildComplexRemovePatch() {
        return Json.createObjectBuilder()
                .add("op", "remove")
                .add("path", "/address/streetAddress")
                .build();
    }
    static JsonObject buildSimpleRemovePatch() {
        return Json.createObjectBuilder()
                .add("op", "remove")
                .add("path", "/streetAddress")
                .build();
    }
    static JsonObject buildArrayRemovePatchInPosition() {
        return Json.createObjectBuilder()
                .add("op", "remove")
                .add("path", "/phoneNumber/0")
                .build();
    }
    static JsonObject buildExpectedRemovedAddress() {
        return Json.createObjectBuilder()
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021")
                .build();
    }
    static JsonObject buildPersonWithoutFirstPhone() {
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
                                .add("type", "fax")
                                .add("number", "646 555-4567")))
                .build();
    }
    static JsonObject buildExpectedPersonWithoutStreetAddress() {
        return Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .add("address", Json.createObjectBuilder()
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

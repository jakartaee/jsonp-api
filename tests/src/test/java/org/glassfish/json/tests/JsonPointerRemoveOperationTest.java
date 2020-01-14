/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonStructure;
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
        JsonPointer pointer = Json.createPointer(pathOperation.getString("path"));
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

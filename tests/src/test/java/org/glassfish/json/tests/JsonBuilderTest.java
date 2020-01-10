/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

import junit.framework.TestCase;

import jakarta.json.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jitendra Kotamraju
 */
public class JsonBuilderTest extends TestCase {
    public JsonBuilderTest(String testName) {
        super(testName);
    }

    public void testEmptyObject() throws Exception {
        JsonObject empty = Json.createObjectBuilder()
                .build();

        JsonObjectTest.testEmpty(empty);
    }

    public void testEmptyArray() throws Exception {
        JsonArray empty = Json.createArrayBuilder()
                .build();

        assertTrue(empty.isEmpty());
    }

    public void testObject() throws Exception {
        JsonObject person = buildPerson();
        JsonObjectTest.testPerson(person);
    }

    public void testNumber() throws Exception {
        JsonObject person = buildPerson();
        JsonNumber number = person.getJsonNumber("age");
        assertEquals(25, number.intValueExact());
        assertEquals(25, number.intValue());
        assertTrue(number.isIntegral());
        JsonObjectTest.testPerson(person);
    }

    public void testJsonObjectCopy() {
        JsonObject person = buildPerson();
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder(person);
        final JsonObject copyPerson = objectBuilder.build();

        JsonNumber number = copyPerson.getJsonNumber("age");
        assertEquals(25, number.intValueExact());
        assertEquals(25, number.intValue());
        assertTrue(number.isIntegral());
        JsonObjectTest.testPerson(copyPerson);

    }

    public void testJsonObjectMap() {
        Map<String, Object> person = buildPersonAsMap();
        final JsonObjectBuilder objectBuilder = Json.createObjectBuilder(person);
        final JsonObject copyPerson = objectBuilder.build();

        JsonNumber number = copyPerson.getJsonNumber("age");
        assertEquals(25, number.intValueExact());
        assertEquals(25, number.intValue());
        assertTrue(number.isIntegral());
        JsonObjectTest.testPerson(copyPerson);
    }

    static Map<String, Object> buildPersonAsMap() {
        Map<String, Object> person = new HashMap<>();
        person.put("firstName", "John");
        person.put("lastName", "Smith");
        person.put("age", 25);

        Map<String, Object> address = Optional.of(new HashMap<String, Object>()).get();
        address.put("streetAddress", "21 2nd Street");
        address.put("city", "New York");
        address.put("state", "NY");
        address.put("postalCode", "10021");

        person.put("address", address);
        person.put("mailingAddress", Optional.empty());

        Collection<Map<String, Object>> phones = new ArrayList<>();

        Map<String, Object> phone1 = new HashMap<>();
        phone1.put("type", "home");
        phone1.put("number", "212 555-1234");
        phones.add(phone1);

        Map<String, Object> phone2 = new HashMap<>();
        phone2.put("type", "fax");
        phone2.put("number", "646 555-4567");
        phones.add(phone2);

        person.put("phoneNumber", phones);

        return person;
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

    static JsonArray buildPhone() {
        return Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("type", "home")
                        .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder()
                        .add("type", "fax")
                        .add("number", "646 555-4567"))
                .build();
    }

}

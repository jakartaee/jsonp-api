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

import javax.json.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, Object> address = new HashMap<>();
        address.put("streetAddress", "21 2nd Street");
        address.put("city", "New York");
        address.put("state", "NY");
        address.put("postalCode", "10021");

        person.put("address", address);

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

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPatchBuilder;

import org.junit.Test;

/**
 * 
 * @author Alex Soto
 *
 */
public class JsonPatchBuilderTest {

    @Test
    public void shouldBuildJsonPatchExpressionUsingJsonPatchBuilder() {
        JsonPatchBuilder patchBuilder = Json.createPatchBuilder();
        JsonObject result = patchBuilder.add("/email", "john@example.com")
                    .replace("/age", 30)
                    .remove("/phoneNumber")
                    .test("/firstName", "John")
                    .copy("/address/lastName", "/lastName")
                    .build()
                    .apply(buildPerson());
        assertThat(result, is(expectedBuildPerson()));
        
    }

    static JsonObject expectedBuildPerson() {
        return Json.createObjectBuilder()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("email", "john@example.com")
                .add("age", 30)
                .add("address", Json.createObjectBuilder()
                        .add("lastName", "Smith")
                        .add("streetAddress", "21 2nd Street")
                        .add("city", "New York")
                        .add("state", "NY")
                        .add("postalCode", "10021"))
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
}

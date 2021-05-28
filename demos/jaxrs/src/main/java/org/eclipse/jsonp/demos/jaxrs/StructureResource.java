/*
 * Copyright (c) 2012, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsonp.demos.jaxrs;

import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * JsonStructure as parameter and return type for a
 * Jakarta RESTful Web Services resource
 *
 * @author Jitendra Kotamraju
 */
@Path("/structure")
public class StructureResource {
    private static final JsonBuilderFactory bf = Json.createBuilderFactory(null);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonStructure doGet() {
        return bf.createObjectBuilder()
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            .add("address", bf.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021"))
            .add("phoneNumber", bf.createArrayBuilder()
                .add(bf.createObjectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(bf.createObjectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567")))
            .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void doPost(JsonStructure structure) {
        System.out.println(structure);
    }

}

/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.jaxrs;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import java.io.OutputStream;

/**
 * Writes wiki's JSON example in a streaming fashion using JsonGenerator
 *
 * @author Jitendra Kotamraju
 */
@Path("/generator")
public class GeneratorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public StreamingOutput doGet() {
        return new StreamingOutput() {
            public void write(OutputStream os) {
                writeWikiExample(os);
            }
        };
    }

    // Writes wiki example JSON in a streaming fashion
    private void writeWikiExample(OutputStream os) {
        try(JsonGenerator gene = Json.createGenerator(os)) {
            gene.writeStartObject()
                .write("firstName", "John")
                .write("lastName", "Smith")
                .write("age", 25)
                .writeStartObject("address")
                    .write("streetAddress", "21 2nd Street")
                    .write("city", "New York")
                    .write("state", "NY")
                    .write("postalCode", "10021")
                .writeEnd()
                .writeStartArray("phoneNumber")
                    .writeStartObject()
                        .write("type", "home")
                        .write("number", "212 555-1234")
                    .writeEnd()
                    .writeStartObject()
                        .write("type", "fax")
                        .write("number", "646 555-4567")
                    .writeEnd()
                .writeEnd()
            .writeEnd();
        }
    }

}

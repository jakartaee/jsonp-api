/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
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

package org.eclipse.jsonp.tests;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonPatch;
import jakarta.json.JsonReader;
import jakarta.json.JsonStructure;

import java.io.StringReader;

import org.junit.Test;

/**
 *
 * @author lukas
 */
public class JsonPatchBugsTest {

    // https://github.com/javaee/jsonp/issues/58
    @Test(expected = JsonException.class)
    public void applyThrowsJsonException() {
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("name", "Bob")
                        .build())
                .build();
        JsonPatch patch = Json.createPatchBuilder()
                .replace("/0/name", "Bobek")
                .replace("/1/name", "Vila Amalka")
                .build();
        JsonArray result = patch.apply(array);
    }

    // https://github.com/eclipse-ee4j/jsonp/issues/181
    @Test(expected = JsonException.class)
    public void applyThrowsJsonException2() {
        // JSON document to be patched
        String targetDocument
                = "{\n"
                + "  \"firstName\": \"John\",\n"
                + "  \"lastName\": \"Doe\"\n"
                + "}";

        // JSON Patch document
        // Instead of "op", we have "op_", which is invalid
        String patchDocument
                = "[\n"
                + "  { \"op_\": \"replace\", \"path\": \"/firstName\", \"value\": \"Jane\" }\n"
                + "]";

        try (JsonReader objectReader = Json.createReader(new StringReader(targetDocument));
                JsonReader arrayReader = Json.createReader(new StringReader(patchDocument))) {

            JsonStructure target = objectReader.read();
            JsonPatch patch = Json.createPatch(arrayReader.readArray());

            // Applies the patch
            // It will throw a NullPointerException with no message
            JsonStructure patched = patch.apply(target);
        }
    }
}

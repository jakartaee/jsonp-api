/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsondemos.osgi.jsonpointer;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class will be registered as a service inside an OSGI container and will be available for other bundles.
 */
public class JsonPointerServiceImpl implements JsonPointerService {
    @Override
    public JsonObject read(String pathToResource) {
        try (
                InputStream is = JsonpointerOsgiDemo.class.getResourceAsStream("/wiki.json");
                JsonReader rdr = Json.createReader(is)
        ) {
            return rdr.readObject();
        } catch (IOException e) {
            return null;
        }
    }
}

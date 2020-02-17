/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonPatch;
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
}

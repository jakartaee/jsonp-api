/*
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Collections;

import org.junit.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonParsingException;

public class JsonReaderDuplicateKeyTest {
    @Test
    public void testJsonReaderDuplicateKey1() {
        String json = "{\"a\":\"b\",\"a\":\"c\"}";
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = jsonReader.readObject();
        assertEquals(jsonObject.getString("a"), "c");
    }

    @Test
    public void testJsonReaderDuplicateKey2() {
        String json = "{\"a\":\"b\",\"a\":\"c\"}";
        JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(Collections.singletonMap(JsonReader.FORBID_DUPLICATE_KEYS, true));
        JsonReader jsonReader = jsonReaderFactory.createReader(new StringReader(json));
        try {
            jsonReader.readObject();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof JsonParsingException);
            assertEquals("Duplicate key 'a' is forbidden", e.getMessage());
        }
    }
}

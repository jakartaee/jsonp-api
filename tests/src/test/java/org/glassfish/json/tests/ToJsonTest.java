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

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonValue;

import org.glassfish.json.JsonUtil;

import static org.junit.Assert.assertEquals;
/**
 * @author Kin-man Chung
 */
public class ToJsonTest {

    @Test
    public void testToJson() {
        assertEquals(Json.createValue("someString"), JsonUtil.toJson("'someString'"));
        assertEquals(Json.createValue("some'thing"), JsonUtil.toJson("'some\\'thing'"));
        assertEquals(Json.createValue("some\"thing"), JsonUtil.toJson("'some\\\"thing'"));
        JsonArrayBuilder builder = Json.createArrayBuilder();
        JsonArray array = builder
            .add(Json.createObjectBuilder()
                .add("name", "John")
                .add("age", 35)
                .add("educations", Json.createArrayBuilder()
                    .add("Gunn High")
                    .add("UC Berkeley")))
            .add(Json.createObjectBuilder()
                .add("name", "Jane")
                .add("educations", Json.createArrayBuilder()
                    .add("Oxford")))
            .build();
         JsonValue expected = JsonUtil.toJson(
             "[ { 'name': 'John', " +
                 "'age': 35, " +
                 "'educations': ['Gunn High', 'UC Berkeley'] }, " +
              " { 'name': 'Jane', " +
                 "'educations': ['Oxford']}]");
          assertEquals(expected, array);
    }
}


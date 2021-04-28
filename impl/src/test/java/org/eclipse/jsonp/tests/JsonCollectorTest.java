/*
 * Copyright (c) 2015, 2021 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.BeforeClass;
import org.junit.Test;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;
import org.eclipse.jsonp.JsonUtil;

import static org.junit.Assert.assertEquals;

/**
 * Some JSON query tests/examples, using Java stream operations, with JSON collectors.
 * @author Kin-man Chung
 */
public class JsonCollectorTest {

    static JsonArray contacts;

    @BeforeClass
    public static void setUpClass() {
        // The JSON source
        contacts = (JsonArray) JsonUtil.toJson(
        "[                                 " +
        "  { 'name': 'Duke',               " +
        "    'age': 18,                    " +
        "    'gender': 'M',                " +
        "    'phones': {                   " +
        "       'home': '650-123-4567',    " +
        "       'mobile': '650-234-5678'}}," +
        "  { 'name': 'Jane',               " +
        "    'age': 23,                    " +
        "    'gender': 'F',                " +
        "    'phones': {                   " +
        "       'mobile': '707-999-5555'}}," +
        "  { 'name': 'Joanna',             " +
        "    'gender': 'F',                " +
        "    'phones': {                   " +
        "       'mobile': '505-333-4444'}} " +
        " ]");
    }

    @Test
    public void testToJsonArray() {
        /*
         * Query: retrieve the names of female contacts
         * Returns a JsonArray of names
         */
        JsonArray result = contacts.getValuesAs(JsonObject.class).stream()
                   .filter(x->"F".equals(x.getString("gender")))
                   .map(x-> x.get("name"))
                   .collect(JsonCollectors.toJsonArray());
        JsonValue expected = JsonUtil.toJson("['Jane','Joanna']");
        assertEquals(expected, result);
    }

    @Test
    public void testToJsonObject() {
        /*
         * Query: retrieve the names and mobile phones of female contacts
         * Returns a JsonObject of name phones pairs
         */
        JsonObject result = contacts.getValuesAs(JsonObject.class).stream()
                    .filter(x->"F".equals(x.getString("gender")))
                    .collect(JsonCollectors.toJsonObject(
                            x->x.asJsonObject().getString("name"),
                            x->x.asJsonObject().getJsonObject("phones").get("mobile")))
                    ;
        JsonValue expected = JsonUtil.toJson(
                "{'Jane': '707-999-5555', 'Joanna': '505-333-4444'}");
        assertEquals(expected, result);
    }

    @Test
    public void testGroupBy() {
        /*
         * Query: group the contacts according to gender
         * Returns a JsonObject, with gender/constacts value pairs
         */
        JsonObject result = contacts.getValuesAs(JsonObject.class).stream()
            .collect(JsonCollectors.groupingBy(x->((JsonObject)x).getString("gender")));
        JsonValue expected = JsonUtil.toJson(
        "{'F':                               " +
        "  [                                 " +
        "    { 'name': 'Jane',               " +
        "      'age': 23,                    " +
        "      'gender': 'F',                " +
        "      'phones': {                   " +
        "         'mobile': '707-999-5555'}}," +
        "    { 'name': 'Joanna',             " +
        "      'gender': 'F',                " +
        "      'phones': {                   " +
        "         'mobile': '505-333-4444'}} " +
        "  ],                                " +
        "'M':                                " +
        "  [                                 " +
        "    { 'name': 'Duke',               " +
        "      'age': 18,                    " +
        "      'gender': 'M',                " +
        "      'phones': {                   " +
        "         'home': '650-123-4567',    " +
        "         'mobile': '650-234-5678'}} " +
        "  ]                                 " +
        "}");

        assertEquals(result,expected);
    }

    static int index; //for keeping track of the array index
    @Test
    public void testQueryAndPatch() {
        /*
         * Query and patch: Increment the ages of contacts with an age entry
         * PatchBuilder is used for building the necessary JsonPatch.
         */
        index = -1;
        JsonPatchBuilder builder = Json.createPatchBuilder();
        contacts.getValuesAs(JsonObject.class).stream()
            .peek(p->index++)
            .filter(p->p.containsKey("age"))
            .forEach(p-> builder.replace("/"+index+"/age", p.getInt("age")+1));
        JsonArray result = builder.build().apply(contacts);

        JsonValue expected = (JsonArray) JsonUtil.toJson(
        "[                                 " +
        "  { 'name': 'Duke',               " +
        "    'age': 19,                    " +
        "    'gender': 'M',                " +
        "    'phones': {                   " +
        "       'home': '650-123-4567',    " +
        "       'mobile': '650-234-5678'}}," +
        "  { 'name': 'Jane',               " +
        "    'age': 24,                    " +
        "    'gender': 'F',                " +
        "    'phones': {                   " +
        "       'mobile': '707-999-5555'}}," +
        "  { 'name': 'Joanna',             " +
        "    'gender': 'F',                " +
        "    'phones': {                   " +
        "       'mobile': '505-333-4444'}} " +
        " ]");
 
        assertEquals(expected, result);
    }
}

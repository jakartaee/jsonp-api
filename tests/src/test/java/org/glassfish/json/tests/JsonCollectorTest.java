/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/le se for the specific
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

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.json.*;
import javax.json.stream.JsonCollectors;
import java.util.List;

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
                   .map(x->((JsonObject)x).get("name"))
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
        JsonPatchBuilder builder = new JsonPatchBuilder();
        contacts.getValuesAs(JsonObject.class).stream()
            .peek(p->index++)
            .filter(p->p.containsKey("age"))
            .forEach(p-> builder.replace("/"+index+"/age", p.getInt("age")+1));
        JsonArray result = builder.apply(contacts);

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

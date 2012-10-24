/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
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

package org.glassfish.json;

import junit.framework.TestCase;

import javax.json.*;

/**
 * @author Jitendra Kotamraju
 */
public class JsonObjectTest extends TestCase {
    public JsonObjectTest(String testName) {
        super(testName);
    }
    
    public void test() {
    }

    public void testEmptyObjectEquals() throws Exception {
        JsonObject empty1 = new JsonBuilder()
                .beginObject()
                .endObject()
                .build();

        JsonObject empty2 = new JsonBuilder()
                .beginObject()
                .endObject()
                .build();

        assertEquals(empty1, empty2);
    }

    public void testPersonObjectEquals() throws Exception {
        JsonObject person1 = JsonBuilderTest.buildPerson();
        JsonObject person2 = JsonReaderTest.readPerson();

        assertEquals(person1, person2);
    }

    static void testPerson(JsonObject person) {
        assertEquals(5, person.getNames().size());
        assertEquals("John", person.getValue("firstName", JsonString.class).getValue());
        assertEquals("Smith", person.getValue("lastName", JsonString.class).getValue());
        assertEquals(25, person.getValue("age", JsonNumber.class).getIntValue());
        assertEquals(25, person.getIntValue("age"));

        JsonObject address = person.getValue("address", JsonObject.class);
        assertEquals(4, address.getNames().size());
        assertEquals("21 2nd Street", address.getValue("streetAddress", JsonString.class).getValue());
        assertEquals("New York", address.getValue("city", JsonString.class).getValue());
        assertEquals("NY", address.getValue("state", JsonString.class).getValue());
        assertEquals("10021", address.getValue("postalCode", JsonString.class).getValue());

        JsonArray phoneNumber = person.getValue("phoneNumber", JsonArray.class);
        assertEquals(2, phoneNumber.size());
        JsonObject home = phoneNumber.getValue(0, JsonObject.class);
        assertEquals(2, home.getNames().size());
        assertEquals("home", home.getValue("type", JsonString.class).getValue());
        assertEquals("212 555-1234", home.getValue("number", JsonString.class).getValue());
        assertEquals("212 555-1234", home.getStringValue("number"));

        JsonObject fax = phoneNumber.getValue(1, JsonObject.class);
        assertEquals(2, fax.getNames().size());
        assertEquals("fax", fax.getValue("type", JsonString.class).getValue());
        assertEquals("646 555-4567", fax.getValue("number", JsonString.class).getValue());
        assertEquals("646 555-4567", fax.getStringValue("number"));
    }

    static void testEmpty(JsonObject empty) {
        assertTrue(empty.getNames().isEmpty());
    }
}

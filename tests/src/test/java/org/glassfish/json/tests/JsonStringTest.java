/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
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

import junit.framework.TestCase;

import javax.json.*;
import java.io.StringReader;

/**
 * @author Jitendra Kotamraju
 */
public class JsonStringTest extends TestCase {
    public JsonStringTest(String testName) {
        super(testName);
    }

    // tests JsonString#toString()
    public void testToString() throws Exception {
        escapedString("");
        escapedString("abc");
        escapedString("abc\f");
        escapedString("abc\na");
        escapedString("abc\tabc");
        escapedString("abc\n\tabc");
        escapedString("abc\n\tabc\r");
        escapedString("\n\tabc\r");
        escapedString("\bab\tb\rc\\\"\ftesting1234");
        escapedString("\f\babcdef\tb\rc\\\"\ftesting1234");
        escapedString("\u0000\u00ff");
        escapedString("abc\"\\/abc");
    }

    void escapedString(String str) throws Exception {
        JsonArray exp = Json.createArrayBuilder().add(str).build();
        String parseStr = "["+exp.get(0).toString()+"]";
        JsonReader jr = Json.createReader(new StringReader(parseStr));
        JsonArray got = jr.readArray();
        assertEquals(exp, got);
        jr.close();
    }

}

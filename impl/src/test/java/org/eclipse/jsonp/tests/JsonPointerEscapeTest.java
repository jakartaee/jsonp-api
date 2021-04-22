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

import org.junit.Test;

import jakarta.json.Json;

import static org.junit.Assert.assertEquals;

/**
 * JSON pointer escape/unescape tests.
 *
 * @author Dmitry Kornilov
 */
public class JsonPointerEscapeTest {

    @Test
    public void escapeTest() {
        assertEquals("a~1b", Json.encodePointer("a/b"));
        assertEquals("a~0b~1c", Json.encodePointer("a~b/c"));
    }

    @Test
    public void unescapeTest() {
        assertEquals("/a/b", Json.decodePointer("/a~1b"));
        assertEquals("/~1", Json.decodePointer("/~01"));
    }
}

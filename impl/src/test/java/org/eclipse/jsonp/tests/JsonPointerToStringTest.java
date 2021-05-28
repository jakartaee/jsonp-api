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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;

import jakarta.json.Json;
import jakarta.json.JsonPointer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * JSON pointer toString tests.
 *
 * @author leadpony
 */
@RunWith(Parameterized.class)
public class JsonPointerToStringTest {

    @Parameters(name = "{index}: {0}")
    public static Iterable<Object> data() {
        return Arrays.asList("", "/", "/one/two/3", "/a~1b", "/m~0n");
    }

    private final String expected;

    public JsonPointerToStringTest(String expected) {
        this.expected = expected;
    }

    @Test
    public void shouldReturnOriginalEscapedString() {
        JsonPointer pointer = Json.createPointer(expected);
        assertThat(pointer.toString(), is(equalTo(expected)));
    }
}

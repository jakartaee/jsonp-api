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

package org.glassfish.json;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

/**
 * A utility class
 * 
 * @since 1.1
 */
public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * Reads the input JSON text and returns a JsonValue.
     * <p>For convenience, single quotes as well as double quotes
     * are allowed to delimit JSON strings. If single quotes are
     * used, any quotes, single or double, in the JSON string must be
     * escaped (prepend with a '\').
     *
     * @param jsonString the input JSON data
     * @return the object model for {@code jsonString}
     * @throws JsonParsingException if the input is not legal JSON text
     */
    public static JsonValue toJson(String jsonString) {
        StringBuilder builder = new StringBuilder();
        boolean single_context = false;
        for (int i = 0; i < jsonString.length(); i++) {
            char ch = jsonString.charAt(i);
            if (ch == '\\') {
                i = i + 1;
                if (i < jsonString.length()) {
                    ch = jsonString.charAt(i);
                    if (!(single_context && ch == '\'')) {
                        // unescape ' inside single quotes
                        builder.append('\\');
                    }
                }
            } else if (ch == '\'') {
                // Turn ' into ", for proper JSON string
                ch = '"';
                single_context = ! single_context;
            }
            builder.append(ch);
        }
                   
        JsonReader reader = Json.createReader(
                                new StringReader(builder.toString()));
        JsonValue value = reader.readValue();
        reader.close();
        return value;
    }
}


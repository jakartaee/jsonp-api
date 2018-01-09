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

package org.glassfish.json;

import javax.json.JsonString;

/**
 * JsonString impl
 *
 * @author Jitendra Kotamraju
 */
final class JsonStringImpl implements JsonString {

    private final String value;

    JsonStringImpl(String value) {
        this.value = value;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public CharSequence getChars() {
        return value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public int hashCode() {
        return getString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }
        if (!(obj instanceof JsonString)) {
            return false;
        }
        JsonString other = (JsonString)obj;
        return getString().equals(other.getString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('"');

        for(int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            // unescaped = %x20-21 | %x23-5B | %x5D-10FFFF
            if (c >= 0x20 && c <= 0x10ffff && c != 0x22 && c != 0x5c) {
                sb.append(c);
            } else {
                switch (c) {
                    case '"':
                    case '\\':
                        sb.append('\\'); sb.append(c);
                        break;
                    case '\b':
                        sb.append('\\'); sb.append('b');
                        break;
                    case '\f':
                        sb.append('\\'); sb.append('f');
                        break;
                    case '\n':
                        sb.append('\\'); sb.append('n');
                        break;
                    case '\r':
                        sb.append('\\'); sb.append('r');
                        break;
                    case '\t':
                        sb.append('\\'); sb.append('t');
                        break;
                    default:
                        String hex = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(hex.substring(hex.length() - 4));
                }
            }
        }

        sb.append('"');
        return sb.toString();
    }
}


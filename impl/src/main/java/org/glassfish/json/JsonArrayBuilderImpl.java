/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.*;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builds a {@link javax.json.JsonArray} from scratch. It uses builder pattern
 * to build the array model and the builder methods can be chained while
 * building the JSON array.
 *
 * <p>
 * <a id="JsonArrayBuilderExample1"/>
 * <b>For example</b>, for the following JSON array
 *
 * <pre>
 * <code>
 * [
 *     { "type": "home", "number": "212 555-1234" },
 *     { "type": "fax", "number": "646 555-4567" }
 * ]
 * </code>
 * </pre>
 *
 * a JsonArray instance can be built using:
 *
 * <p>
 * <pre>
 * <code>
 * JsonArray value = new JsonArrayBuilderImpl()
 *     .add(new JsonObjectBuilderImpl()
 *         .add("type", "home")
 *         .add("number", "212 555-1234"))
 *     .add(new JsonObjectBuilderImpl()
 *         .add("type", "fax")
 *         .add("number", "646 555-4567"))
 *     .build();
 * </code>
 * </pre>
 *
 * @see javax.json.JsonObjectBuilder
 */
class JsonArrayBuilderImpl implements JsonArrayBuilder {
    private final List<JsonValue> valueList;

    /**
     * Constructs a {@code JsonArrayBuilderImpl} that initializes an empty JSON
     * array that is being built.
     */
    public JsonArrayBuilderImpl() {
        this.valueList = new ArrayList<JsonValue>();
    }

    /**
     * Adds the specified value to the array that is being built.
     *
     * @param value a JSON value
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder add(JsonValue value) {
        valueList.add(value);
        return this;
    }

    /**
     * Adds the specified value as a JSON string value to the array
     * that is being built.
     *
     * @param value string
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder add(String value) {
        valueList.add(new JsonStringImpl(value));
        return this;
    }

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see javax.json.JsonNumber
     */
    public javax.json.JsonArrayBuilder add(BigDecimal value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see javax.json.JsonNumber
     */
    public javax.json.JsonArrayBuilder add(BigInteger value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see javax.json.JsonNumber
     */
    public javax.json.JsonArrayBuilder add(int value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     *
     * @see javax.json.JsonNumber
     */
    public javax.json.JsonArrayBuilder add(long value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see javax.json.JsonNumber
     */
    public javax.json.JsonArrayBuilder add(double value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a JSON true or false value to the array that is being built.
     *
     * @param value a boolean
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder add(boolean value) {
        valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    /**
     * Adds a JSON null value to the array that is being built.
     *
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder addNull() {
        valueList.add(JsonValue.NULL);
        return this;
    }

    /**
     * Adds a JsonObject from the specified builder to the array that
     * is being built.
     *
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder add(JsonObjectBuilder builder) {
        valueList.add(builder.build());
        return this;
    }

    /**
     * Adds a JsonArray from the specified builder to the array that
     * is being built.
     *
     * @return this array builder
     */
    public javax.json.JsonArrayBuilder add(javax.json.JsonArrayBuilder builder) {
        valueList.add(builder.build());
        return this;
    }

    /**
     * Returns the array that is being built
     *
     * @return JSON array that is being built
     */
    public JsonArray build() {
        ArrayList<JsonValue> snapshot = new ArrayList<JsonValue>(valueList);
        return new JsonArrayImpl(Collections.unmodifiableList(snapshot));
    }

    private static final class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray {
        private final List<JsonValue> valueList;    // Unmodifiable

        JsonArrayImpl(List<JsonValue> valueList) {
            this.valueList = valueList;
        }

        @Override
        public int size() {
            return valueList.size();
        }

        @Override
        public JsonObject getJsonObject(int index) {
            return (JsonObject)valueList.get(index);
        }

        @Override
        public JsonArray getJsonArray(int index) {
            return (JsonArray)valueList.get(index);
        }

        @Override
        public JsonNumber getJsonNumber(int index) {
            return (JsonNumber)valueList.get(index);
        }

        @Override
        public JsonString getJsonString(int index) {
            return (JsonString)valueList.get(index);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
            return (List<T>)valueList;
        }

        @Override
        public String getString(int index) {
            return getJsonString(index).getString();
        }

        @Override
        public String getString(int index, String defaultValue) {
            try {
                return getString(index);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        @Override
        public int getInt(int index) {
            return getJsonNumber(index).intValue();
        }

        @Override
        public int getInt(int index, int defaultValue) {
            try {
                return getInt(index);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        @Override
        public boolean getBoolean(int index) {
            JsonValue jsonValue = get(index);
            if (jsonValue == JsonValue.TRUE) {
                return true;
            } else if (jsonValue == JsonValue.FALSE) {
                return false;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public boolean getBoolean(int index, boolean defaultValue) {
            try {
                return getBoolean(index);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        @Override
        public boolean isNull(int index) {
            return valueList.get(index).equals(JsonValue.NULL);
        }

        @Override
        public ValueType getValueType() {
            return ValueType.ARRAY;
        }

        @Override
        public JsonValue get(int index) {
            return valueList.get(index);
        }

        @Override
        public String toString() {
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriterImpl(sw);
            jw.write(this);
            jw.close();
            return sw.toString();
        }
    }

}




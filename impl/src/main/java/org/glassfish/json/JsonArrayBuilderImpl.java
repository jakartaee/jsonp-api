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

class JsonArrayBuilderImpl implements JsonArrayBuilder {
    private final List<JsonValue> valueList;

    JsonArrayBuilderImpl() {
        this.valueList = new ArrayList<JsonValue>();
    }

    public JsonArrayBuilder add(JsonValue value) {
        validateValue(value);
        valueList.add(value);
        return this;
    }

    public JsonArrayBuilder add(String value) {
        validateValue(value);
        valueList.add(new JsonStringImpl(value));
        return this;
    }

    public JsonArrayBuilder add(BigDecimal value) {
        validateValue(value);
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    public JsonArrayBuilder add(BigInteger value) {
        validateValue(value);
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    public JsonArrayBuilder add(int value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    public JsonArrayBuilder add(long value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    public JsonArrayBuilder add(double value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    public JsonArrayBuilder add(boolean value) {
        valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    public JsonArrayBuilder addNull() {
        valueList.add(JsonValue.NULL);
        return this;
    }

    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(
                    "Object builder that is used to add a value to JSON array cannot be null");
        }
        valueList.add(builder.build());
        return this;
    }

    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(
                    "Array builder that is used to add a value to JSON array cannot be null");
        }
        valueList.add(builder.build());
        return this;
    }

    public JsonArray build() {
        ArrayList<JsonValue> snapshot = new ArrayList<JsonValue>(valueList);
        return new JsonArrayImpl(Collections.unmodifiableList(snapshot));
    }

    private void validateValue(Object value) {
        if (value == null) {
            throw new NullPointerException("JsonArray's value cannot be null");
        }
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




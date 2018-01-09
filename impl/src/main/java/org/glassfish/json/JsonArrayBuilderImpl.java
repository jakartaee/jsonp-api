/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

import org.glassfish.json.api.BufferPool;

import javax.json.*;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * JsonArrayBuilder implementation
 *
 * @author Jitendra Kotamraju
 * @author Kin-man Chung
 */

class JsonArrayBuilderImpl implements JsonArrayBuilder {
    private ArrayList<JsonValue> valueList;
    private final BufferPool bufferPool;

    JsonArrayBuilderImpl(BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    JsonArrayBuilderImpl(JsonArray array, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        valueList = new ArrayList<>();
        valueList.addAll(array);
    }

    JsonArrayBuilderImpl(Collection<?> collection, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        valueList = new ArrayList<>();
        populate(collection);
    }

    @Override
    public JsonArrayBuilder add(JsonValue value) {
        validateValue(value);
        addValueList(value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(String value) {
        validateValue(value);
        addValueList(new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigDecimal value) {
        validateValue(value);
        addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(BigInteger value) {
        validateValue(value);
        addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int value) {
        addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(long value) {
        addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(double value) {
        addValueList(JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(boolean value) {
        addValueList(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull() {
        addValueList(JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        addValueList(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_ARRAY_BUILDER_NULL());
        }
        addValueList(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder addAll(JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_ARRAY_BUILDER_NULL());
        }
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        valueList.addAll(builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonValue value) {
        validateValue(value);
        addValueList(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, String value) {
        validateValue(value);
        addValueList(index, new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigDecimal value) {
        validateValue(value);
        addValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, BigInteger value) {
        validateValue(value);
        addValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, int value) {
        addValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, long value) {
        addValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, double value) {
        addValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, boolean value) {
        addValueList(index, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder addNull(int index) {
        addValueList(index, JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonObjectBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        addValueList(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder add(int index, JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        addValueList(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonValue value) {
        validateValue(value);
        setValueList(index, value);
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, String value) {
        validateValue(value);
        setValueList(index, new JsonStringImpl(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigDecimal value) {
        validateValue(value);
        setValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, BigInteger value) {
        validateValue(value);
        setValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, int value) {
        setValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, long value) {
        setValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, double value) {
        setValueList(index, JsonNumberImpl.getJsonNumber(value));
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, boolean value) {
        setValueList(index, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    @Override
    public JsonArrayBuilder setNull(int index) {
        setValueList(index, JsonValue.NULL);
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonObjectBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        setValueList(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder set(int index, JsonArrayBuilder builder) {
        if (builder == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_OBJECT_BUILDER_NULL());
        }
        setValueList(index, builder.build());
        return this;
    }

    @Override
    public JsonArrayBuilder remove(int index) {
        if (valueList == null) {
            throw new IndexOutOfBoundsException(JsonMessages.ARRBUILDER_VALUELIST_NULL(index, 0));
        }
        valueList.remove(index);
        return this;
    }

    @Override
    public JsonArray build() {
        List<JsonValue> snapshot;
        if (valueList == null) {
            snapshot = Collections.emptyList();
        } else {
            // Should we trim to minimize storage ?
            // valueList.trimToSize();
            snapshot = Collections.unmodifiableList(valueList);
        }
        valueList = null;
        return new JsonArrayImpl(snapshot, bufferPool);
    }

    private void populate(Collection<?> collection) {
        for (Object value : collection) {
            if (value != null && value instanceof Optional) {
                ((Optional<?>) value).ifPresent(v ->
                        this.valueList.add(MapUtil.handle(v, bufferPool)));
            } else {
                this.valueList.add(MapUtil.handle(value, bufferPool));
            }
        }
    }

    private void addValueList(JsonValue value) {
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        valueList.add(value);
    }

    private void addValueList(int index, JsonValue value) {
        if (valueList == null) {
            valueList = new ArrayList<>();
        }
        valueList.add(index, value);
    }

    private void setValueList(int index, JsonValue value) {
        if (valueList == null) {
            throw new IndexOutOfBoundsException(JsonMessages.ARRBUILDER_VALUELIST_NULL(index, 0));
        }
        valueList.set(index, value);
    }

    private void validateValue(Object value) {
        if (value == null) {
            throw new NullPointerException(JsonMessages.ARRBUILDER_VALUE_NULL());
        }
    }

    private static final class JsonArrayImpl extends AbstractList<JsonValue> implements JsonArray {
        private final List<JsonValue> valueList;    // Unmodifiable
        private final BufferPool bufferPool;

        JsonArrayImpl(List<JsonValue> valueList, BufferPool bufferPool) {
            this.valueList = valueList;
            this.bufferPool = bufferPool;
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
            try (JsonWriter jw = new JsonWriterImpl(sw, bufferPool)) {
                jw.write(this);
            }
            return sw.toString();
        }

        @Override
        public JsonArray asJsonArray() {
            return this;
        }
    }
}


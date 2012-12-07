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

package javax.json;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Builds a JSON {@link JsonObject object} or {@link JsonArray array} from
 * scratch. It uses builder pattern to build these object models and the
 * builder methods can be chained.
 *
 * <p>
 * <b>For example</b>, for the following JSON
 *
 * <code>
 * <pre>
 * {
 *     "firstName": "John", "lastName": "Smith", "age": 25,
 *     "address" : {
 *         "streetAddress", "21 2nd Street",
 *         "city", "New York",
 *         "state", "NY",
 *         "postalCode", "10021"
 *     },
 *     "phoneNumber": [
 *         { "type": "home", "number": "212 555-1234" },
 *         { "type": "fax", "number": "646 555-4567" }
 *     ]
 * }
 * </pre>
 * </code>
 *
 * a JsonObject instance can be built using:
 *
 * <p>
 * <code>
 * <pre>
 * JsonObject value = new JsonBuilder()
 *     .startObject()
 *         .add("firstName", "John")
 *         .add("lastName", "Smith")
 *         .add("age", 25)
 *         .startObject("address")
 *             .add("streetAddress", "21 2nd Street")
 *             .add("city", "New York")
 *             .add("state", "NY")
 *             .add("postalCode", "10021")
 *         .end()
 *         .startArray("phoneNumber")
 *             .startObject()
 *                 .add("type", "home")
 *                 .add("number", "212 555-1234")
 *             .end()
 *             .startObject()
 *                 .add("type", "home")
 *                 .add("number", "646 555-4567")
 *             .end()
 *         .end()
 *     .end()
 * .build();
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public class JsonBuilder {

    /**
     * Build task that gives the result of the build process
     */
    public static interface JsonBuildable<T extends JsonStructure> {
        /**
         * Builds a JSON object or array
         *
         * @return built object or array
         */
        public T build();
    }

    /**
     * Start building a JSON object
     *
     * @return an object builder
     */
    public JsonObjectBuilder<JsonBuildable<JsonObject>> startObject() {
        final JsonObjectImpl objectImpl = new JsonObjectImpl();
        JsonBuilder.JsonBuildable<JsonObject> enclosing = new JsonBuilder.JsonBuildable<JsonObject>() {
            @Override
            public JsonObject build() {
                return objectImpl;
            }
        };
        return new JsonObjectBuilderImpl<JsonBuilder.JsonBuildable<JsonObject>>(enclosing, objectImpl.valueMap);
    }

    /**
     * Start building a JSON array
     *
     * @return an array builder
     */
    public JsonArrayBuilder<JsonBuildable<JsonArray>> startArray() {
        final JsonArrayImpl arrayImpl = new JsonArrayImpl();
        JsonBuilder.JsonBuildable<JsonArray> enclosing = new JsonBuilder.JsonBuildable<JsonArray>() {
            @Override
            public JsonArray build() {
                return arrayImpl;
            }
        };
        return new JsonArrayBuilderImpl<JsonBuilder.JsonBuildable<JsonArray>>(enclosing, arrayImpl.valueList);
    }

    private static final class JsonObjectBuilderImpl<T> implements JsonObjectBuilder<T> {
        private final T enclosing;
        private final Map<String, JsonValue> valueMap;
        private boolean done;

        JsonObjectBuilderImpl(T enclosing, Map<String, JsonValue> valueMap) {
            this.enclosing = enclosing;
            this.valueMap = valueMap;
        }

        @Override
        public T end() {
            if (done) {
                throw new IllegalStateException("end() is already invoked.");
            }
            done = true;
            return enclosing;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, JsonValue value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, value);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, String value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonStringImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigInteger value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;

        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigDecimal value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, int value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, long value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, double value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, boolean value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> addNull(String name) {
            if (done) {
                throw new IllegalStateException("addNull() cannot be called after end()");
            }
            valueMap.put(name, JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonObjectBuilder<T>> startObject(String name) {
            if (done) {
                throw new IllegalStateException("startObject() cannot be called after end()");
            }
            JsonObjectImpl child = new JsonObjectImpl();
            valueMap.put(name, child);
            return new JsonObjectBuilderImpl<JsonObjectBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonObjectBuilder<T>> startArray(String name) {
            if (done) {
                throw new IllegalStateException("startArray() cannot be called after end()");
            }
            JsonArrayImpl child = new JsonArrayImpl();
            valueMap.put(name, child);
            return new JsonArrayBuilderImpl<JsonObjectBuilder<T>>(this, child.valueList);
        }
    }

    private static final class JsonArrayBuilderImpl<T> implements JsonArrayBuilder<T> {
        private final T enclosing;
        private final List<JsonValue> valueList;
        private boolean done;

        JsonArrayBuilderImpl(T enclosing, List<JsonValue> valueList) {
            this.enclosing = enclosing;
            this.valueList = valueList;
        }

        @Override
        public T end() {
            if (done) {
                throw new IllegalStateException("end() is already invoked.");
            }
            done = true;
            return enclosing;
        }

        @Override
        public JsonArrayBuilder<T> add(JsonValue value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(value);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(String value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonStringImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigDecimal value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigInteger value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(int value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(long value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(double value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(boolean value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> addNull() {
            if (done) {
                throw new IllegalStateException("addNull() cannot be called after end()");
            }
            valueList.add(JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonArrayBuilder<T>> startObject() {
            if (done) {
                throw new IllegalStateException("startObject() cannot be called after end()");
            }
            JsonObjectImpl child = new JsonObjectImpl();
            valueList.add(child);
            return new JsonObjectBuilderImpl<JsonArrayBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonArrayBuilder<T>> startArray() {
            if (done) {
                throw new IllegalStateException("startArray() cannot be called after end()");
            }
            JsonArrayImpl child = new JsonArrayImpl();
            valueList.add(child);
            return new JsonArrayBuilderImpl<JsonArrayBuilder<T>>(this, child.valueList);
        }
    }

    private static final class JsonStringImpl implements JsonString {

        private final String value;

        public JsonStringImpl(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
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
            return getValue().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JsonString)) {
                return false;
            }
            JsonString other = (JsonString)obj;
            return getValue().equals(other.getValue());
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private static final class JsonNumberImpl implements JsonNumber {
        private final BigDecimal bigDecimal;
        private static final BigDecimal INT_MIN_VALUE = new BigDecimal(Integer.MIN_VALUE);
        private static final BigDecimal INT_MAX_VALUE = new BigDecimal(Integer.MAX_VALUE);
        private static final BigDecimal LONG_MIN_VALUE = new BigDecimal(Long.MIN_VALUE);
        private static final BigDecimal LONG_MAX_VALUE = new BigDecimal(Long.MAX_VALUE);

        public JsonNumberImpl(int value) {
            bigDecimal = new BigDecimal(value);
        }

        public JsonNumberImpl(long value) {
            bigDecimal = new BigDecimal(value);
        }

        public JsonNumberImpl(BigInteger value) {
            bigDecimal = new BigDecimal(value);
        }

        public JsonNumberImpl(double value) {
            //bigDecimal = new BigDecimal(value);
            // This is the preferred way to convert double to BigDecimal
            bigDecimal = BigDecimal.valueOf(value);
        }

        public JsonNumberImpl(BigDecimal value) {
            this.bigDecimal = value;
        }

        @Override
        public NumberType getNumberType() {
            if (bigDecimal.scale() != 0)  {
                return NumberType.BIG_DECIMAL;
            } else {
                if (bigDecimal.compareTo(INT_MIN_VALUE) >= 0 && bigDecimal.compareTo(INT_MAX_VALUE) <= 0) {
                    return NumberType.INT;
                } else if (bigDecimal.compareTo(LONG_MIN_VALUE) >= 0 && bigDecimal.compareTo(LONG_MAX_VALUE) <= 0) {
                    return NumberType.LONG;
                } else {
                    return NumberType.BIG_DECIMAL;
                }
            }
        }

        @Override
        public int getIntValue() {
            return bigDecimal.intValue();
        }

        @Override
        public int getIntValueExact() {
            return bigDecimal.intValueExact();
        }

        @Override
        public long getLongValue() {
            return bigDecimal.longValue();
        }

        @Override
        public long getLongValueExact() {
            return bigDecimal.longValueExact();
        }

        @Override
        public BigInteger getBigIntegerValue() {
            return bigDecimal.toBigInteger();
        }

        @Override
        public BigInteger getBigIntegerValueExact() {
            return bigDecimal.toBigIntegerExact();
        }

        @Override
        public double getDoubleValue() {
            return bigDecimal.doubleValue();
        }

        @Override
        public BigDecimal getBigDecimalValue() {
            return bigDecimal;
        }

        @Override
        public ValueType getValueType() {
            return ValueType.NUMBER;
        }

        @Override
        public int hashCode() {
            return getBigDecimalValue().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JsonNumber)) {
                return false;
            }
            JsonNumber other = (JsonNumber)obj;
            return getBigDecimalValue().equals(other.getBigDecimalValue());
        }

        @Override
        public String toString() {
            return bigDecimal.toString();
        }

    }

    private static final class JsonArrayImpl implements JsonArray {
        final List<JsonValue> valueList = new ArrayList<JsonValue>();
        private final List<JsonValue> unmodifiableValueList = Collections.unmodifiableList(valueList);

        @Override
        public List<JsonValue> getValues() {
            return unmodifiableValueList;
        }

        @Override
        public int size() {
            return valueList.size();
        }

        @Override
        public JsonValue getValue(int index) {
            return valueList.get(index);
        }

        @Override
        public <T extends JsonValue> T getValue(int index, Class<T> clazz) {
            return clazz.cast(valueList.get(index));
        }

        @Override
        public String getStringValue(int index) {
            return getValue(index, JsonString.class).getValue();
        }

        @Override
        public int getIntValue(int index) {
            return getValue(index, JsonNumber.class).getIntValue();
        }

        @Override
        public ValueType getValueType() {
            return ValueType.ARRAY;
        }

        @Override
        public int hashCode() {
            return getValues().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JsonArray)) {
                return false;
            }
            JsonArray other = (JsonArray)obj;
            return getValues().equals(other.getValues());
        }

        @Override
        public String toString() {
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriter(sw);
            jw.write(this);
            jw.close();
            return sw.toString();
        }
    }

    private static final class JsonObjectImpl implements JsonObject {
        final Map<String, JsonValue> valueMap = new LinkedHashMap<String, JsonValue>();
        private final Map<String, JsonValue> unmodifiableValueMap = Collections.unmodifiableMap(valueMap);

        @Override
        public JsonValue getValue(String name) {
            return valueMap.get(name);
        }

        @Override
        public <T extends JsonValue> T getValue(String name, Class<T> clazz) {
            return clazz.cast(valueMap.get(name));
        }

        @Override
        public Set<String> getNames() {
            return valueMap.keySet();
        }

        @Override
        public Map<String, JsonValue> getValues() {
            return unmodifiableValueMap;
        }

        @Override
        public String getStringValue(String name) {
            return getValue(name, JsonString.class).getValue();
        }

        @Override
        public int getIntValue(String name) {
            return getValue(name, JsonNumber.class).getIntValue();
        }

        @Override
        public ValueType getValueType() {
            return ValueType.OBJECT;
        }

        @Override
        public int hashCode() {
            return getValues().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JsonObject)) {
                return false;
            }
            JsonObject other = (JsonObject)obj;
            return getValues().equals(other.getValues());
        }

        @Override
        public String toString() {
            StringWriter sw = new StringWriter();
            JsonWriter jw = new JsonWriter(sw);
            jw.write(this);
            jw.close();
            return sw.toString();
        }
    }

}

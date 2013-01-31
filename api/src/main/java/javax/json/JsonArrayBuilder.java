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
 * A builder class for creating {@link JsonArray} models from scratch. This
 * class initializes an empty JSON array model and provides methods to add
 * values to the array model and to return the resulting array. The methods
 * in this class can be chained to add multiple values to the array.
 *
 * <p>
 * <a id="JsonArrayBuilderExample1"/>
 * The example code below shows how to build a {@code JsonArray} object
 * that represents the following JSON array:
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
 * <p>The following code creates the JSON array above:
 *
 * <pre>
 * <code>
 * JsonArray value = new JsonArrayBuilder()
 *     .add(new JsonObjectBuilder()
 *         .add("type", "home")
 *         .add("number", "212 555-1234"))
 *     .add(new JsonObjectBuilder()
 *         .add("type", "fax")
 *         .add("number", "646 555-4567"))
 *     .build();
 * </code>
 * </pre>
 *
 * @see JsonObjectBuilder
 */
public class JsonArrayBuilder {
    private final List<JsonValue> valueList;

    /**
     * Creates a {@code JsonArrayBuilder} instance that initializes an empty
     * JSON array.
     */
    public JsonArrayBuilder() {
        this.valueList = new ArrayList<JsonValue>();
    }

    /**
     * Adds a value to the array.
     *
     * @param value the JSON value
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonValue value) {
        valueList.add(value);
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonString}.
     *
     * @param value the string value
     * @return this array builder
     */
    public JsonArrayBuilder add(String value) {
        valueList.add(new JsonStringImpl(value));
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonNumber}.
     *
     * @param value the number value
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(BigDecimal value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonNumber}.
     *
     * @param value the number value
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(BigInteger value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonNumber}.
     *
     * @param value the number value
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(int value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonNumber}.
     *
     * @param value the number value
     * @return this array builder
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(long value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a value to the array as a {@link JsonNumber}.
     *
     * @param value the number value
     * @return this array builder
     * @throws NumberFormatException if the value is Not-a-Number(NaN) or 
     *      infinity
     *
     * @see JsonNumber
     */
    public JsonArrayBuilder add(double value) {
        valueList.add(new JsonNumberImpl(value));
        return this;
    }

    /**
     * Adds a {@link JsonValue#TRUE}  or {@link JsonValue#FALSE} value to the
     * array.
     *
     * @param value the boolean value
     * @return this array builder
     */
    public JsonArrayBuilder add(boolean value) {
        valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    /**
     * Adds a {@link JsonValue#NULL} value to the array.
     *
     * @return this array builder
     */
    public JsonArrayBuilder addNull() {
        valueList.add(JsonValue.NULL);
        return this;
    }

    /**
     * Adds a {@link JsonObject} from an object builder to the array.
     *
     * @param builder the object builder
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
        valueList.add(builder.build());
        return this;
    }

    /**
     * Adds a {@link JsonArray} from an array builder to the array.
     *
     * @param builder the array builder
     * @return this array builder
     */
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
        valueList.add(builder.build());
        return this;
    }

    /**
     * Returns the current array.
     *
     * @return the current JSON array
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
        public <T extends JsonValue> T getValue(int index, Class<T> clazz) {
            return clazz.cast(valueList.get(index));
        }

        @Override
        public String getStringValue(int index) {
            return getValue(index, JsonString.class).getValue();
        }

        @Override
        public String getStringValue(int index, String defaultValue) {
            try {
                return getStringValue(index);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        @Override
        public int getIntValue(int index) {
            return getValue(index, JsonNumber.class).getIntValue();
        }

        @Override
        public int getIntValue(int index, int defaultValue) {
            try {
                return getIntValue(index);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        @Override
        public boolean getBooleanValue(int index) {
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
        public boolean getBooleanValue(int index, boolean defaultValue) {
            try {
                return getBooleanValue(index);
            } catch (Exception e) {
                return defaultValue;
            }
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
            JsonWriter jw = new JsonWriter(sw);
            jw.write(this);
            jw.close();
            return sw.toString();
        }
    }
}

final class JsonStringImpl implements JsonString {

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

final class JsonNumberImpl implements JsonNumber {
    private final BigDecimal bigDecimal;

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
        return bigDecimal.scale() == 0 ? NumberType.INTEGER : NumberType.DECIMAL;
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


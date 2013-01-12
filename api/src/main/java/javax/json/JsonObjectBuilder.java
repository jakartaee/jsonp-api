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
 * Builds a {@link JsonObject} from scratch. It uses builder pattern to build
 * the object model and the builder methods can be chained while building the
 * JSON Object.
 *
 * <p>
 * <a id="JsonObjectBuilderExample1"/>
 * <b>For example</b>, for the following JSON
 *
 * <pre>
 * <code>
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
 * </code>
 * </pre>
 *
 * a JsonObject instance can be built using:
 *
 * <p>
 * <pre>
 * <code>
 * JsonObject value = new JsonObjectBuilder()
 *     .add("firstName", "John")
 *     .add("lastName", "Smith")
 *     .add("age", 25)
 *     .add("address", new JsonObjectBuilder()
 *         .add("streetAddress", "21 2nd Street")
 *         .add("city", "New York")
 *         .add("state", "NY")
 *         .add("postalCode", "10021"))
 *     .add("phoneNumber", new JsonArrayBuilder()
 *         .add(new JsonObjectBuilder()
 *             .add("type", "home")
 *             .add("number", "212 555-1234"))
 *         .add(new JsonObjectBuilder()
 *             .add("type", "fax")
 *             .add("number", "646 555-4567")))
 *     .build();
 * </code>
 * </pre>
 *
 * @see JsonArrayBuilder
 */
public class JsonObjectBuilder {
    private final Map<String, JsonValue> valueMap;

    /**
     * Constructs a {@code JsonObjectBuilder} that initializes an empty JSON
     * object that is being built.
     */
    public JsonObjectBuilder() {
        this.valueMap = new LinkedHashMap<String, JsonValue>();
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     */
    public JsonObjectBuilder add(String name, JsonValue value) {
        valueMap.put(name, value);
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     */
    public JsonObjectBuilder add(String name, String value) {
        valueMap.put(name, new JsonStringImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder add(String name, BigInteger value) {
        valueMap.put(name, new JsonNumberImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder add(String name, BigDecimal value) {
        valueMap.put(name, new JsonNumberImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder add(String name, int value) {
        valueMap.put(name, new JsonNumberImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder add(String name, long value) {
        valueMap.put(name, new JsonNumberImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder add(String name, double value) {
        valueMap.put(name, new JsonNumberImpl(value));
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @param value value to be associated with the specified name
     * @return this object builder
     */
    public JsonObjectBuilder add(String name, boolean value) {
        valueMap.put(name, value ? JsonValue.TRUE : JsonValue.FALSE);
        return this;
    }

    /**
     * Associates the specified value with the specified name in the
     * JSON object that is being built. If the JSON object that is being
     * built previously contained a mapping for the name, the old value
     * is replaced by the specified value.
     *
     * @param name name with which the specified value is to be associated
     * @return this object builder
     */
    public JsonObjectBuilder addNull(String name) {
        valueMap.put(name, JsonValue.NULL);
        return this;
    }

    /**
     * Associates the JsonObject from the specified builder with the
     * specified name in the JSON object that is being built. If the JSON
     * object that is being built previously contained a mapping for the name,
     * the old value is replaced by the JsonObject from the specified builder.
     *
     * @param name name with which the specified value is to be associated
     * @return this object builder
     */
    public JsonObjectBuilder add(String name, JsonObjectBuilder builder) {
        valueMap.put(name, builder.build());
        return this;
    }

    /**
     * Associates the JSON array from the specified builder with the
     * specified name in the JSON object that is being built. If the JSON
     * object that is being built previously contained a mapping for the name,
     * the old value is replaced by the JsonArray from the specified builder.
     *
     * @param name name with which the specified value is to be associated
     * @return this object builder
     */
    public JsonObjectBuilder add(String name, JsonArrayBuilder builder) {
        valueMap.put(name, builder.build());
        return this;
    }

    /**
     * Returns the JSON object that is being built. The returned JsonObject's
     * iteration ordering is based on the order in which name/value pairs are
     * added in this builder.
     *
     * @return JSON object that is being built
     */
    public JsonObject build() {
        Map<String, JsonValue> snapshot = new LinkedHashMap<String, JsonValue>(valueMap);
        return new JsonObjectImpl(Collections.unmodifiableMap(snapshot));
    }

    private static final class JsonObjectImpl extends AbstractMap<String, JsonValue> implements JsonObject {
        private final Map<String, JsonValue> valueMap;      // unmodifiable

        JsonObjectImpl(Map<String, JsonValue> valueMap) {
            this.valueMap = valueMap;
        }

        @Override
        public <T extends JsonValue> T getValue(String name, Class<T> clazz) {
            return clazz.cast(valueMap.get(name));
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
        public Set<Entry<String, JsonValue>> entrySet() {
            return valueMap.entrySet();
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
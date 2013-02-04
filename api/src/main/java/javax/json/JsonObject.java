/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2013 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Map;

/**
 * {@code JsonObject} class represents an immutable JSON object value
 * (an unordered collection of zero or more name/value pairs).
 * It also provides unmodifiable map view to the JSON object
 * name/value mappings.
 *
 * <p>A JsonObject instance can be created from an input source using
 * {@link JsonReader#readObject()}. For example:
 * <pre><code>
 * JsonReader jsonReader = Json.createReader(...);
 * JsonObject object = jsonReader.readObject();
 * jsonReader.close();
 * </code></pre>
 *
 * It can also be built from scratch using a {@link JsonObjectBuilder}.
 *
 * <p>For example 1: An empty JSON object can be built as follows:
 * <pre><code>
 * JsonObject object = Json.createObjectBuilder().build();
 * </code></pre>
 *
 * For example 2: The following JSON
 * <pre><code>
 * {
 *     "firstName": "John", "lastName": "Smith", "age": 25,
 *     "address" : {
 *         "streetAddress": "21 2nd Street",
 *         "city": "New York",
 *         "state": "NY",
 *         "postalCode": "10021"
 *     },
 *     "phoneNumber": [
 *         { "type": "home", "number": "212 555-1234" },
 *         { "type": "fax", "number": "646 555-4567" }
 *     ]
 * }
 * </code></pre>
 * can be built using :
 * <pre><code>
 * JsonObject value = Json.createObjectBuilder()
 *     .add("firstName", "John")
 *     .add("lastName", "Smith")
 *     .add("age", 25)
 *     .add("address", Json.createObjectBuilder()
 *         .add("streetAddress", "21 2nd Street")
 *         .add("city", "New York")
 *         .add("state", "NY")
 *         .add("postalCode", "10021"))
 *     .add("phoneNumber", Json.createArrayBuilder()
 *         .add(Json.createObjectBuilder()
 *             .add("type", "home")
 *             .add("number", "212 555-1234"))
 *         .add(Json.createObjectBuilder()
 *             .add("type", "fax")
 *             .add("number", "646 555-4567")))
 *     .build();
 * </code></pre>
 *
 * {@code JsonObject} can be written to JSON as follows:
 * <pre><code>
 * JsonWriter writer = ...
 * JsonObject obj = ...;
 * writer.writeObject(obj);
 * </code></pre>
 *
 * {@code JsonObject} values can be {@link JsonObject}, {@link JsonArray},
 * {@link JsonString}, {@link JsonNumber}, {@link JsonValue#TRUE},
 * {@link JsonValue#FALSE}, {@link JsonValue#NULL}. These values can be
 * accessed using various accessor methods.
 *
 * <p>In the above example 2, "John" can be got using
 * <pre><code>
 * String firstName = object.getStringValue("firstName");
 * </code></pre>
 *
 * This map object provides read-only access to the JSON object data,
 * and attempts to modify the map, whether direct or via its collection
 * views, result in an {@code UnsupportedOperationException}.
 *
 * <p>The map object's iteration ordering is based on the order in which
 * name/value pairs are added to the corresponding builder or the order
 * in which name/value pairs appear in the corresponding stream.
 *
 * @author Jitendra Kotamraju
 */
public interface JsonObject extends JsonStructure, Map<String, JsonValue> {

    /**
     * Returns the value to which the specified name is mapped.
     * This is just a convenience method for {@code (T) get(name)} to get
     * the value.
     *
     * @param name the name(key) whose associated value is to be returned
     * @param clazz value class
     * @return the value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name(key)
     * @throws ClassCastException if the value for specified name/key mapping
     * is not assignable to the type T
     */
    <T extends JsonValue> T getValue(String name, Class<T> clazz);

    /**
     * A convenience method for
     * {@code getValue(name, JsonString.class).getStringValue()}
     *
     * @param name whose associated value is to be returned as String
     * @return the String value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to JsonString
     */
    String getStringValue(String name);

    /**
     * Returns the string value of the associated {@code JsonString} mapping
     * for the specified name. If {@code JsonString} is found, then its
     * {@link javax.json.JsonString#getStringValue()} is returned. Otherwise,
     * the specified default value is returned.
     *
     * @param name whose associated value is to be returned as String
     * @param defaultValue a default value to be returned
     * @return the string value of the associated mapping for the name,
     * or the default value
     */
    String getStringValue(String name, String defaultValue);

    /**
     * A convenience method for
     * {@code getValue(name, JsonNumber.class).getIntValue()}
     *
     * @param name whose associated value is to be returned as int
     * @return the int value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to JsonNumber
     */
    int getIntValue(String name);

    /**
     * Returns the int value of the associated {@code JsonNumber} mapping
     * for the specified name. If {@code JsonNumber} is found, then its
     * {@link javax.json.JsonNumber#getIntValue()} is returned. Otherwise,
     * the specified default value is returned.
     *
     * @param name whose associated value is to be returned as int
     * @param defaultValue a default value to be returned
     * @return the int value of the associated mapping for the name,
     * or the default value
     */
    int getIntValue(String name, int defaultValue);

    /**
     * Returns the boolean value of the associated mapping for the specified
     * name. If the associated mapping is JsonValue.TRUE, then returns true.
     * If the associated mapping is JsonValue.FALSE, then returns false.
     *
     * @param name whose associated value is to be returned as boolean
     * @return the boolean value to which the specified name is mapped
     * @throws NullPointerException if the specified name doesn't have any
     * mapping
     * @throws ClassCastException if the value for specified name mapping
     * is not assignable to JsonValue.TRUE or JsonValue.FALSE
     */
    boolean getBooleanValue(String name);

    /**
     * Returns the boolean value of the associated mapping for the specified
     * name. If the associated mapping is JsonValue.TRUE, then returns true.
     * If the associated mapping is JsonValue.FALSE, then returns false.
     * Otherwise, the specified default value is returned.
     *
     * @param name whose associated value is to be returned as int
     * @param defaultValue a default value to be returned
     * @return the boolean value of the associated mapping for the name,
     * or the default value
     */
    boolean getBooleanValue(String name, boolean defaultValue);

}

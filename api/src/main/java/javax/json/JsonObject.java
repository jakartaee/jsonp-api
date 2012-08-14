/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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
import java.util.Set;

/**
 * {@code JsonObject} class represents an immutable JSON object value.
 *
 * <p>
 * A full JsonObject instance can be created from an input source using
 * {@link JsonReader#readObject()}. For example:
 *
 * <code>
 * <pre>
 * JsonReader jsonReader = new JsonReader(...));
 * JsonObject object = jsonReader.readObject();
 * jsonReader.close();
 * </pre>
 * </code>
 *
 * It can also be built from scratch using {@link JsonBuilder#beginObject()}.
 *
 * <p>
 * For example 1:
 * <code>
 * <pre>
 * An empty JSON object can be built as follows:
 *
 * JsonArray array = new JsonBuilder()
 *     .beginObject()
 *     .endObject()
 * .build();
 * </pre>
 * </code>
 *
 * <p>
 * For example 2:
 * <code>
 * <pre>
 * The following JSON
 *
 * {
 *     "firstName": "John", "lastName": "Smith", "age": 25,
 *     "phoneNumber": [
 *         {"type": "home", "number": "212 555-1234"},
 *         {"type": "fax", "number": "646 555-4567"}
 *      ]
 * }
 *
 * can be built using :
 *
 * JsonObject object = new JsonBuilder()
 *     .beginObject()
 *         .add("firstName", "John")
 *         .add("lastName", "Smith")
 *         .add("age", 25)
 *         .beginObject("address")
 *             .add("streetAddress", "21 2nd Street")
 *             .add("city", "New York")
 *             .add("state", "NY")
 *             .add("postalCode", "10021")
 *         .endObject()
 *         .beginArray("phoneNumber")
 *             .beginObject()
 *                 .add("type", "home")
 *                 .add("number", "212 555-1234")
 *             .endObject()
 *             .beginObject()
 *                 .add("type", "home")
 *                 .add("number", "646 555-4567")
 *             .endObject()
 *         .endArray()
 *     .endObject()
 * .build();
 * </pre>
 * </code>
 *
 * {@code JsonObject} can be written to JSON as follows:
 * <code>
 * <pre>
 * JsonWriter writer = ...
 * JsonObject obj = ...;
 * writer.writeobject(obj);
 * </pre>
 * </code>
 *
 * <p>
 * {@code JsonObject} values can be {@link JsonObject}, {@link JsonArray},
 * {@link JsonString}, {@link JsonNumber}, {@link JsonValue#TRUE},
 * {@link JsonValue#FALSE}, {@link JsonValue#NULL}. These values can be
 * accessed using various accessor methods. For example:
 *
 * <code>
 * <pre>
 * In the above example 2, "John" can be got using
 *
 * String firstName = object.getValue("firstName", JsonString.class).getValue();
 * </pre>
 * </code>
 *
 * <p>
 * TODO 2. define equals() semantics
 *
 * <p>
 * TODO 4. Define an predictable iterating order ??
 *
 * @author Jitendra Kotamraju
 */
public interface JsonObject extends JsonStructure {

    /**
     * Returns the value to which the specified name/key is mapped,
     * or {@code null} if this object contains no mapping for the name/key.
     *
     * @param name the name/key whose associated value is to be returned
     * @return the value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name/key
     */
    public JsonValue getValue(String name);

    /**
     * Returns the value to which the specified name/key is mapped,
     * or {@code null} if this object contains no mapping for the name/key.
     *
     * @param name the name/key whose associated value is to be returned
     * @param clazz value class
     * @return the value to which the specified name is mapped, or
     *         {@code null} if this object contains no mapping for the name/key
     */
    public <T extends JsonValue> T getValue(String name, Class<T> clazz);

    /**
     * Returns an unmodifiable {@link Set} of the name/keys contained in this
     * JSON object.
     *
     * @return a set of the name/keys contained in this JSON object
     */
    public Set<String> getNames();

    /**
     * Returns an unmodifiable {@link Map} of the name(key)/value pairs
     * contained in this JSON object.
     *
     * @return a set of the name/keys contained in this JSON object
     */
    public Map<String, JsonValue> getValues();

    // TODO String getValue(String name) ??
    // TODO int getValue(String name) ??

}
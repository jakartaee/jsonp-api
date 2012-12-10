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

import java.util.List;

/**
 * {@code JsonArray} class represents an immutable JSON array value.
 *
 * <p>A full JsonArray instance can be created from a input source using
 * {@link JsonReader#readArray()}. For example:
 *
 * <code>
 * <pre>
 * JsonReader jsonReader = new JsonReader(...);
 * JsonArray array = jsonReader.readArray();
 * jsonReader.close();
 * </pre>
 * </code>
 *
 * It can also be built from scratch using {@link JsonArrayBuilder}.
 * <p>
 * For example 1:
 * <code>
 * <pre>
 * An empty JSON array can be built as follows:
 *
 * JsonArray array = new JsonArrayBuilder().build();
 * </pre>
 * </code>
 *
 * <p>
 * For example 2:
 * <code>
 * <pre>
 * The following JSON
 *
 * [
 *     { "type": "home", "number": "212 555-1234" },
 *     { "type": "fax", "number": "646 555-4567" }
 * ]
 *
 * can be built using :
 *
 * JsonArray value = new JsonArrayBuilder()
 *     .add(new JsonObjectBuilder()
 *         .add("type", "home")
 *         .add("number", "212 555-1234"))
 *     .add(new JsonObjectBuilder()
 *         .add("type", "fax")
 *         .add("number", "646 555-4567"))
 *     .build();
 * </pre>
 * </code>
 *
 * <p>
 * {@code JsonArray} can be written to JSON as follows:
 *
 * <code>
 * <pre>
 * JsonArray arr = ...;
 * JsonWriter writer = new JsonWriter(...)
 * writer.writeArray(arr);
 * writer.close();
 * </pre>
 * </code>
 *
 * <p>
 * {@code JsonArray} values can be {@link JsonObject}, {@link JsonArray},
 * {@link JsonString}, {@link JsonNumber}, {@link JsonValue#TRUE},
 * {@link JsonValue#FALSE}, {@link JsonValue#NULL}. These values can be
 * accessed using various accessor methods.
 *
 * <p>
 * In the above example 2, home number "212 555-1234" can be got using:
 * <code>
 * <pre>
 * JsonObject home = array.getValue(0, JsonObject.class);
 * String number = home.getStringValue("number");
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public interface JsonArray extends JsonStructure {

    /**
     * Returns an unmodifiable list of this JSON array values
     *
     * @return a list of array values
     */
    List<JsonValue> getValues();

    /**
     * Returns the number of values in this JSON array.  If this array contains
     * more than <tt>Integer.MAX_VALUE</tt> values, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of values in this JSON array
     */
    int size();

    /**
     * Returns the value at the specified position in this JSON array values.
     *
     * @param index index of the value to return
     * @return the value at the specified position in this array values
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    JsonValue getValue(int index);

    /**
     * Returns the value at the specified position in this JSON array values.
     *
     * @param index index of the value to return
     * @param clazz value class
     * @return the value at the specified position in this array values
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to the type T
     */
    <T extends JsonValue> T getValue(int index, Class<T> clazz);

    /**
     * A convenience method for
     * {@code getValue(int, JsonString.class).getValue()}
     *
     * @param index index of the JsonString value
     * @return the String value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to JsonString
     */
    String getStringValue(int index);

    /**
     * A Convenience method for
     * {@code getValue(int, JsonNumber.class).getIntValue()}
     *
     * @param index index of the JsonNumber value
     * @return the int value at the specified position in this array
     * @throws IndexOutOfBoundsException if the index is out of range
     * @throws ClassCastException if the value at the specified position is not
     * assignable to JsonNumber
     */
    int getIntValue(int index);

    /**
     * Compares the specified object with this JsonArray object for equality.
     * Returns {@code true} if and only if the specified object is also a
     * JsonArray object, and their {@link #getValues()} objects are <i>equal</i>
     *
     * @param obj the object to be compared for equality with this JsonArray
     *            object
     * @return {@code true} if the specified object is equal to this JsonArray
     * object
     */
    @Override
    boolean equals(Object obj);

    /**
     * Returns the hash code value for this JsonArray object.  The hash code of
     * a JsonArray object is defined to be its {@link #getValues()} object's
     * hash code.
     *
     * @return the hash code value for this JsonArray object
     */
    @Override
    int hashCode();

}
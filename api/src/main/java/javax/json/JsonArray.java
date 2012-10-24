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
 * It can also be built from scratch using {@link JsonBuilder#beginArray()}.
 * <p>
 * For example 1:
 * <code>
 * <pre>
 * An empty JSON array can be built as follows:
 *
 * JsonArray array = new JsonBuilder()
 *     .beginArray()
 *     .endArray()
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
 * [
 *     { "type": "home", "number": "212 555-1234" },
 *     { "type": "fax", "number": "646 555-4567" }
 * ]
 *
 * can be built using :
 *
 * JsonArray array = new JsonBuilder()
 *     .beginArray()
 *         .beginObject()
 *             .add("type", "home")
 *             .add("number", "212 555-1234")
 *         .endObject()
 *         .beginObject()
 *             .add("type", "home")
 *             .add("number", "646 555-4567")
 *         .endObject()
 *     .endArray()
 * .build();
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
 * accessed using various accessor methods. For example:
 *
 * <code>
 * <pre>
 * In the above example 2, home number "212 555-1234" can be got using:
 *
 * JsonObject home = array.getValue(0, JsonObject.class);
 * String number = home.getValue("number", JsonString.class).getValue();
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
    public List<JsonValue> getValues();

    /**
     * Returns the number of values in this JSON array.  If this array contains
     * more than <tt>Integer.MAX_VALUE</tt> values, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of values in this JSON array
     */
    public int size();

    /**
     * Returns the value at the specified position in this JSON array values.
     *
     * @param index index of the value to return
     * @return the value at the specified position in this array values
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public JsonValue getValue(int index);

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
    public <T extends JsonValue> T getValue(int index, Class<T> clazz);

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();


}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015 Oracle and/or its affiliates. All rights reserved.
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

/**
 * <p>This class is an immutable representation of a JSON Pointer as specified in
 * <a href="http://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 * </p>
 * <p> A {@code JsonPointer} is only meanful when it is applied to a target, which
 * can be any {@link JsonValue}.
 * <p> A {@code JsonPointer} with an empty JSON Pointer string
 * defines a reference to the target.</p>
 * <p> A {@code JsonPointer} with a non-empty JSON Pointer string, which must be a sequence
 * of '/' prefixed tokens, defines a reference to a value in a {@link JsonArray} or
 * {@link JsonArray}. In this case, the target must be a {@link JsonObject} or {@link JsonArray}.
 * </p>
 * <p> The method {@link JsonPointer#getValue getValue()} returns the referenced value.
 * The methods {@link JsonPointer#add add()}, {@link JsonPointer#replace replace()},
 * and {@link JsonPointer#remove remove()} executes the operations specified in 
 * <a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>. </p>
 * 
 * @since 1.1
 */

public class JsonPointer {

    private final String[] tokens;

    /**
     * Construct and initialize a JsonPointer
     * @param jsonPointer the raw JSON Pointer string
     * @throws NullPointerException if jsonPointer is null
     */
    public JsonPointer(String jsonPointer) {
        tokens = jsonPointer.split("/", -1);  // keep the trailing blanks
        if (! "".equals(tokens[0])) {
            throw new JsonException("A non-empty JSON pointer must begin with a '/'");
        }
        for (int i = 1; i < tokens.length; i++) {
            tokens[i] = tokens[i].replaceAll("~1", "/").replaceAll("~0","~");
        }
    }

    /**
     * Compares this {@code JsonPointer} with another object.
     * @param object The object to compare this {@code JsonPointer} against
     * @return true if the given object is a {@code JsonPointer} with the same
     * reference tokens as this one,
     *   false otherwise.
     */
    public boolean equals(Object object) {
        return false;
    }

    /**
     * Return the value at the referenced location
     * in the specified {@code JsonValue}
     *
     * @param target the {@code JsonValue} referenced by this {@code JsonPointer}
     *
     * @return the {@code JsonValue} referenced by this {@code JsonPointer}
     */
    public JsonValue getValue(JsonValue target) {
        return null;
    }

    /**
     * Add or replace a value at the referenced location
     * in the specified {@code JsonValue}.
     * <ol>
     * <li>If the reference is the target,
     * the value, which must be the same type as {@code target},
     * is returned.</li>
     * <li>If the reference is an index to an array, the value is inserted
     * into the array at the index.  If the index is specified with a "-",
     * or if the index is equal to the size of the array,
     * the value is appended to the array.</li>
     * <li>If the reference is a name of a {@code JsonObject}, and the
     * referenced value exists, the value is replaced by the specified value.
     * If it does not exists, a new name/value pair is added to the object.
     * </li>
     * </ol>
     *
     * @param target the target {@code JsonValue}
     * @param value the value to be added
     * @return the resultant JsonValue
     * @throws IndexOutOfBoundsException if the index to the array is out of range
     */
    public JsonValue add(JsonValue target, JsonValue value) {
        return null;
    }

    /**
     * Replace the value at the referenced location 
     * in the specified {@code JsonStructure} with the specified value.
     *
     * @param target the target {@code JsonStructure}
     * @param value the value to be stored at the referenced location
     * @return the resultant JsonStructure
     * @throws JsonException if the referenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonStructure replace(JsonStructure target, JsonValue value) {
        return null;
    }

    /**
     * Remove the value at the reference location 
     * in the specified {@code JsonStructure}
     *
     * @param target the target {@code JsonStructure}
     * @return the resultant JsonStructure
     * @throws JsonException if the referenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonStructure remove(JsonStructure target) {
        return null;
    }

    /**
     * Add or replace a value at the referenced location
     * in the specified {@code JsonObject}.
     * If the reference is the target {@code JsonObject},
     * the value, which must be a {@code JsonObject}, is returned.
     * If the referenced value exists in the target object, it is replaced
     * by the specified value. If it does not exists, a new name/value pair
     * is added to the object.
     *
     * @param target the target {@code JsonObject}
     * @param value the value to be added
     * @return the resultant {@code JsonObject}
     */
    public JsonObject add(JsonObject target, JsonValue value) {
        return null;
    }

    /**
     * Insert a value at the referenced location
     * in the specified {@code JsonArray}.
     * If the reference is the target {@code JsonArray},
     * the value, which must be a {@code JsonArray}, is returned.
     * If the reference is an index of an array, the value is inserted
     * into the array at the index.  If the index is specified with a "-",
     * or if the index is equal to the size of the array,
     * the value is appended to the array.
     *
     * @param target the target {@code JsonArray}
     * @param value the value to be added
     * @return the resultant {@code JsonArray}
     * @throws IndexOutOfBoundsException if the index to the array is out of range
     */
    public JsonArray add(JsonArray target, JsonValue value) {
        return null;
    }

    /**
     * Replace the value at the referenced location
     * in the specified {@code JsonObject} with the specified value.
     *
     * @param target the target {@code JsonObject}
     * @param value the value be stored at the referenced location
     * @return the resultant JsonObject
     * @throws JsonException if the regerenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonObject replace(JsonObject target, JsonValue value) {
        return null;
    }

    /**
     * Replace the value at the referenced location
     * in the specified {@code JsonArray} with the specified value.
     *
     * @param target the target {@code JsonArray}
     * @param value the value to be stored at the referenced location
     * @return the resultant JsonArray
     * @throws JsonException if the referenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonArray replace(JsonArray target, JsonValue value) {
        return null;
    }

    /**
     * Remove the value at the referenced location
     * in the specified {@code JsonObject}
     *
     * @param target the target {@code JsonObject}
     * @return the resultant JsonObject
     * @throws JsonException if the referenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonObject remove(JsonObject target) {
        return null;
    }

    /**
     * Remove the value at the referenced location
     * in the specified {@code JsonArray}
     *
     * @param target the target {@code JsonJsonArray}
     * @return the resultant JsonArray
     * @throws JsonException if the referenced location does not exists,
     *    or if the reference is the target.
     */
    public JsonArray remove(JsonArray target) {
        return null;
    }
}

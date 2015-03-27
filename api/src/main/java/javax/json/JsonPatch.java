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
 * This class is a immutable epresentation of a JSON Pointer as specified in
 * <a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>.
 */

public class JsonPatch {

    private final JsonArray patch;

    /**
     * Construct a JsonPatch
     * @param patch the JSON Patch
     */
    public JsonPatch(JsonArray patch) {
        this.patch = patch;
    }

    /**
     * Compares this {@code JsonPatch} with another object.
     * @param obj the object to compare this {@code JsonPatch} against
     * @return true if the given object is a {@code JsonPatch} with the same
     * reference tokens as this one, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != JsonPatch.class)
            return false;
        return patch.equals(((JsonPatch)obj).patch);
    }

    /**
     * Returns the hash code value for this {@code JsonPatch}.
     *
     * @return the hash code value for this {@code JsonPointer} object
     */
    @Override
    public int hashCode() {
        // Can be better
        return patch.hashCode();
    }

    /**
     * Apply the operations specified in the JSON patch to the specified
     * {@code JsonStructure}.  The target is not modified by the patch.
     *
     * @param target the {@code JsonStructure} to apply the patch operations
     * @return the {@code JsonStructure} as the result of applying the patch
     *    operations on the target.
     * @throws JsonException if the supplied JSON Patch is malformed or if
     *    it contains references to non-existing members
     */
    public JsonStructure apply(JsonStructure target) {

        JsonStructure result = target;

        for (JsonValue operation: patch) {
            if (operation.getValueType() != JsonValue.ValueType.OBJECT) {
                throw new JsonException("A JSON patch must be an array of JSON objects.");
            }
            result = apply(result, (JsonObject) operation);
        }
        return result;
    }

    /**
     * Apply the operations specified in the JSON patch to the specified
     * {@code JsonObject}.  The target is not modified by the patch.
     *
     * @param target the {@code JsonObject} to apply the patch operations
     * @return the {@code JsonObject} as the result of applying the patch
     *    operations on the target.
     * @throws JsonException if the supplied JSON Patch is malformed or if
     *    it contains references to non-existing members
     */
    public JsonObject apply(JsonObject target) {
        return (JsonObject) apply((JsonStructure)target);
    }

    /**
     * Apply the operations specified in the JSON patch to the specified
     * {@code JsonArray}.  The target is not modified by the patch.
     *
     * @param target the {@code JsonArray} to apply the patch operations
     * @return the {@code JsonArray} as the result of applying the patch
     *    operations on the target.
     * @throws JsonException if the supplied JSON Patch is malformed or if 
     *    it contains references to non-existing members
     */
    public JsonArray apply(JsonArray target) {
        return (JsonArray) apply((JsonStructure)target);
    }


    /**
     * Generate a JSON Patch from the source and target JsonStructure.
     * The generated JSON Patch need not be unique.
     * @param source the source
     * @param target the target, must be the same type as the source
     * @return a JSON Patch which when applied to the source, yields the target
     */
    public static JsonArray diff(JsonStructure source, JsonStructure target) {
        // TODO
        return null;
    }

    private JsonStructure apply(JsonStructure target, JsonObject operation) {

        JsonPointer pointer = getPointer(operation, "path");
        JsonPointer from;
        switch (operation.getString("op")) {
            case "add":
                return pointer.add(target, getValue(operation));
            case "replace":
                return pointer.replace(target, getValue(operation));
            case "remove":
                return pointer.remove(target);
            case "copy":
                checkOverlap(operation);
                from = getPointer(operation, "from");
                return pointer.add(target, from.getValue(target));
            case "move":
                checkOverlap(operation);
                from = getPointer(operation, "from");
                return pointer.add(from.remove(target), from.getValue(target));
            case "test":
                if (! getValue(operation).equals(pointer.getValue(target))) {
                    throw new JsonException("The JSON patch operation 'test' failed.");
                }
                return target;
            default:
                throw new JsonException("Illegal value for the op member of the JSON patch operation: " + operation.getString("op"));
        }
    }

    private JsonPointer getPointer(JsonObject operation, String member) {
        JsonString pointerString = operation.getJsonString(member);
        if (pointerString == null) {
            missingMember(operation.getString("op"), member);
        }
        return new JsonPointer(pointerString.getString());
    }

    private JsonValue getValue(JsonObject operation) {
        JsonValue value = operation.get("value");
        if (value == null) {
            missingMember(operation.getString("op"), "value");
        }
        return value;
    }

    private void missingMember(String op, String  member) {
        throw new JsonException(String.format("The JSON Patch operation %s must contain a %s member", op, member));
    }

    private void checkOverlap(JsonObject operation) {
         if (operation.getString("path").startsWith(operation.getString("from"))) {
             throw new JsonException("The 'from' path of the patch operation " 
                         + operation.getString("op") + " contains the 'path' path");
         }
    }

}


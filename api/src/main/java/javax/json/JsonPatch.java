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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.json.JsonValue.ValueType;

/**
 * This class is an immutable representation of a JSON Patch as specified in <a
 * href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>.
 * <p>
 * A {@code JsonPatch} can be instantiated with {@link #JsonPatch(JsonArray)} by
 * specifying the patch operations in a JSON Patch. Alternately, it can also be
 * constructed with a {@link JsonPatchBuilder}.
 * </p>
 * The following illustrates both approaches.
 * <p>
 * 1. Construct a JsonPatch with a JSON Patch.
 * 
 * <pre>
 * {@code
 *   JsonArray contacts = ... // The target to be patched
 *   JsonArray patch = ...  ; // JSON Patch
 *   JsonPatch jsonpatch = new JsonPatch(patch);
 *   JsonArray result = jsonpatch.apply(contacts);
 * }
 * </pre>
 * 
 * 2. Construct a JsonPatch with JsonPatchBuilder.
 * 
 * <pre>
 * {
 *     &#064;code
 *     JsonPatchBuilder builder = new JsonPatchBuilder();
 *     JsonArray result = builder.add(&quot;/John/phones/office&quot;, &quot;1234-567&quot;)
 *             .remove(&quot;/Amy/age&quot;).apply(contacts);
 * }
 * </pre>
 */

public class JsonPatch {

    private final JsonArray patch;

    /**
     * Constructs a JsonPatch
     * 
     * @param patch
     *            the JSON Patch
     */
    public JsonPatch(JsonArray patch) {
        this.patch = patch;
    }

    /**
     * Compares this {@code JsonPatch} with another object.
     * 
     * @param obj
     *            the object to compare this {@code JsonPatch} against
     * @return true if the given object is a {@code JsonPatch} with the same
     *         reference tokens as this one, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != JsonPatch.class)
            return false;
        return patch.equals(((JsonPatch) obj).patch);
    }

    /**
     * Returns the hash code value for this {@code JsonPatch}.
     *
     * @return the hash code value for this {@code JsonPatch} object
     */
    @Override
    public int hashCode() {
        return patch.hashCode();
    }

    /**
     * Returns the JSON Patch text
     * 
     * @return the JSON Patch text
     */
    @Override
    public String toString() {
        return patch.toString();
    }

    /**
     * Applies the patch operations to the specified {@code target}. The target
     * is not modified by the patch.
     *
     * @param target
     *            the target to apply the patch operations
     * @return the transformed target after the patch
     * @throws JsonException
     *             if the supplied JSON Patch is malformed or if it contains
     *             references to non-existing members
     */
    public JsonStructure apply(JsonStructure target) {

        JsonStructure result = target;

        for (JsonValue operation : patch) {
            if (operation.getValueType() != JsonValue.ValueType.OBJECT) {
                throw new JsonException(
                        "A JSON patch must be an array of JSON objects.");
            }
            result = apply(result, (JsonObject) operation);
        }
        return result;
    }

    /**
     * Applies the patch operations to the specified {@code target}.
     *
     * @param target
     *            the target to apply the patch operations
     * @return the transformed target after the patch
     * @throws JsonException
     *             if the supplied JSON Patch is malformed or if it contains
     *             references to non-existing members
     */
    public JsonObject apply(JsonObject target) {
        return (JsonObject) apply((JsonStructure) target);
    }

    /**
     * Applies the patch operations to the specified {@code target}.
     *
     * @param target
     *            the target to apply the patch operations
     * @return the transformed target after the patch
     * @throws JsonException
     *             if the supplied JSON Patch is malformed or if it contains
     *             references to non-existing members
     */
    public JsonArray apply(JsonArray target) {
        return (JsonArray) apply((JsonStructure) target);
    }

    /**
     * Generates a JSON Patch from the source and target {@code JsonStructure}.
     * The generated JSON Patch need not be unique.
     * 
     * @param source
     *            the source
     * @param target
     *            the target, must be the same type as the source
     * @return a JSON Patch which when applied to the source, yields the target
     */
    public static JsonPatch diff(JsonStructure source, JsonStructure target) {
        Map<String, JsonValue> unchangedValues = getUnchangedValues(source, target);
        JsonPatchBuilder builder = generateDiffs(unchangedValues, new JsonPatchBuilder(), "", source, target);
        return builder.build();
    }

    /**
     * Generates a JSON Patch from the source and target {@code JsonStructure}.
     * The generated JSON Patch need not be unique.
     * 
     * @param source
     *            the source
     * @param target
     *            the target, must be the same type as the source
     * @return a JSON Patch which when applied to the source, yields the target
     */
    public static JsonArray diffAsArray(JsonStructure source, JsonStructure target) {
        Map<String, JsonValue> unchangedValues = getUnchangedValues(source, target);
        JsonPatchBuilder builder = generateDiffs(unchangedValues, new JsonPatchBuilder(), "", source, target);
        //squash to move
        return builder.buildAsArray();
    }

    private static JsonPatchBuilder generateDiffs(final Map<String, JsonValue> unchangedValues, final JsonPatchBuilder builder,
            final String pointer, final JsonValue source,
            final JsonValue target) {

        if (source.equals(target)) {
            return builder;
        }

        ValueType firstValueType = source.getValueType();
        ValueType secondValueType = target.getValueType();

        if (firstValueType != secondValueType) {
            // TODO can we add JsonPointer in JsonPatchBuilder?
            builder.replace(pointer, target);
            return builder;
        }

        // if it is a value and not an array or an json object
        if (firstValueType != ValueType.OBJECT
                && firstValueType != ValueType.ARRAY) {
            builder.replace(pointer, target);
            return builder;
        }

        if (firstValueType == ValueType.OBJECT) {
            return generateObjectDiffs(unchangedValues, builder, pointer, (JsonObject) source,
                    (JsonObject) target);
        } else {
            // array
            return generateArrayDiffs(unchangedValues, builder, pointer, (JsonArray) source,
                    (JsonArray) target);
        }
    }

    private static JsonPatchBuilder generateObjectDiffs(final Map<String, JsonValue> unchangedValues, JsonPatchBuilder builder,
            final String pointer, final JsonObject source,
            final JsonObject target) {
        final Set<String> firstFields = source.keySet();
        final Set<String> secondFields = target.keySet();

        for (final String field : difference(firstFields, secondFields)) {
            builder.remove(pointer + "/" + field);
        }

        for (final String field : difference(secondFields, firstFields)) {
            //unchanged
            builder.add(pointer + "/" + field, target.get(field));
        }

        for (final String field : intersection(firstFields, secondFields)) {
            builder = generateDiffs(unchangedValues, builder, pointer + "/" + field, source.get(field),
                    target.get(field));
        }

        return builder;
    }

    private static JsonPatchBuilder generateArrayDiffs(final Map<String, JsonValue> unchangedValues, JsonPatchBuilder builder,
            final String pointer, final JsonArray source,
            final JsonArray target) {
        final int firstSize = source.size();
        final int secondSize = target.size();
        final int size = Math.min(firstSize, secondSize);

        for (int index = size; index < firstSize; index++) {
            builder.remove(pointer+ "/" + size);
        }

        for (int index = 0; index < size; index++) {
            builder = generateDiffs(unchangedValues, builder, pointer + "/" + index, source.get(index),
                    target.get(index));
        }

        for (int index = size; index < secondSize; index++) {
            //unchanged
            builder.add(pointer + "/" + "-", target.get(index));
        }
        return builder;
    }

    private static Set<String> difference(Set<String> setA, Set<String> setB) {
        Set<String> tmp = new TreeSet<String>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    private static Set<String> intersection(Set<String> setA, Set<String> setB) {
        Set<String> tmp = new TreeSet<String>(setA);
        tmp.retainAll(setB);
        return tmp;
    }

    private String findUnchangedValue(Map<String, JsonValue> unchangedValues, final JsonValue currentValue) {
        Optional<Entry<String, JsonValue>> findUnchanged = unchangedValues.entrySet().stream().filter(entry -> entry.getValue().equals(currentValue)).findFirst();
        return findUnchanged.isPresent() ? findUnchanged.get().getKey() : null;
    }

    private static Map<String, JsonValue> getUnchangedValues(
            final JsonStructure source, final JsonStructure target) {
        final Map<String, JsonValue> unchanged = new HashMap<>();
        findUnchanged(unchanged, "", source, target);
        return unchanged;
    }

    private static void findUnchanged(
            final Map<String, JsonValue> unchanged,
            final String pointer, final JsonValue first,
            final JsonValue second) {

        if (first.equals(second)) {
            unchanged.put(pointer, second);
        }

        ValueType firstValueType = first.getValueType();
        ValueType secondValueType = second.getValueType();

        if (firstValueType != secondValueType) {
            return;
        }

        switch (firstValueType) {
        case OBJECT:
            findUnchangedObject(unchanged, pointer, (JsonObject) first,
                    (JsonObject) second);
            break;
        case ARRAY:
            findUnchangedArray(unchanged, pointer, (JsonArray) first,
                    (JsonArray) second);
            break;
        default:
        }
    }

    private static void findUnchangedObject(
            final Map<String, JsonValue> unchanged,
            final String pointer, final JsonObject source,
            final JsonObject target) {
        Set<Entry<String, JsonValue>> entries = source.entrySet();

        for (Entry<String, JsonValue> entry : entries) {
            String name = entry.getKey();
            if (target.containsKey(name)) {
                // TODO should we add append method to JsonPointer?
                findUnchanged(unchanged, pointer + "/" + name,
                        source.get(name), target.get(name));
            }
        }
    }

    private static void findUnchangedArray(
            final Map<String, JsonValue> unchanged,
            final String pointer, final JsonArray source,
            final JsonArray target) {
        final int size = Math.min(source.size(), target.size());

        for (int i = 0; i < size; i++)
            findUnchanged(unchanged, pointer + "/" + i,
                    source.get(i), target.get(i));
    }

    /**
     * Applies a JSON Patch operation to the target.
     * 
     * @param target
     *            the target to apply the operation
     * @param operation
     *            the JSON Patch operation
     * @return the target after the patch
     */
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
            from = getPointer(operation, "from");
            return pointer.add(target, from.getValue(target));
        case "move":
            from = getPointer(operation, "from");
            if (pointer.equals(from)) {
                // nop
                return target;
            }
            // Check if from is a proper prefix of path
            if (operation.getString("path").startsWith(
                    operation.getString("from"))) {
                throw new JsonException(
                        "The 'from' path of the patch operation "
                                + "'move' is a proper prefix of the 'path' path");
            }
            return pointer.add(from.remove(target), from.getValue(target));
        case "test":
            if (!getValue(operation).equals(pointer.getValue(target))) {
                throw new JsonException(
                        "The JSON patch operation 'test' failed.");
            }
            return target;
        default:
            throw new JsonException(
                    "Illegal value for the op member of the JSON patch operation: "
                            + operation.getString("op"));
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

    private void missingMember(String op, String member) {
        throw new JsonException(String.format(
                "The JSON Patch operation %s must contain a %s member", op,
                member));
    }
}

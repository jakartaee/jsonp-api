/*
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.json;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 * This class is an implementation of a JSON Merge Patch as specified in
 * <a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>.
 *
 * @since 1.1
 */

public final class JsonMergePatchImpl implements JsonMergePatch {

    private JsonValue patch;

    public JsonMergePatchImpl(JsonValue patch) {
        this.patch = patch;
    }

    @Override
    public JsonValue apply(JsonValue target) {
        return mergePatch(target, patch);
    }

    @Override
    public JsonValue toJsonValue() {
        return patch;
    }
    /**
     * Applies the specified patch to the specified target.
     * The target is not modified by the patch.
     *
     * @param target the {@code JsonValue} to apply the patch operations
     * @param patch the patch
     * @return the {@code JsonValue} as the result of applying the patch
     *    operations on the target.
     */
    private static JsonValue mergePatch(JsonValue target, JsonValue patch) {

        if (patch.getValueType() != JsonValue.ValueType.OBJECT) {
            return patch;
        }
        if (target.getValueType() != JsonValue.ValueType.OBJECT) {
            target = JsonValue.EMPTY_JSON_OBJECT;
        }
        JsonObject targetJsonObject = target.asJsonObject();
        JsonObjectBuilder builder =
            Json.createObjectBuilder(targetJsonObject);
        patch.asJsonObject().forEach((key, value) -> {
            if (value == JsonValue.NULL) {
                if (targetJsonObject.containsKey(key)) {
                    builder.remove(key);
                }
            } else if (targetJsonObject.containsKey(key)) {
                builder.add(key, mergePatch(targetJsonObject.get(key), value));
            } else {
                builder.add(key, mergePatch(JsonValue.EMPTY_JSON_OBJECT, value));
            }
        });
        return builder.build();
    }

    /**
     * Generate a JSON Merge Patch from the source and target {@code JsonValue}.
     * @param source the source
     * @param target the target
     * @return a JSON Patch which when applied to the source, yields the target
     */
    static JsonValue diff(JsonValue source, JsonValue target) {
        if (source.getValueType() != JsonValue.ValueType.OBJECT ||
                target.getValueType() != JsonValue.ValueType.OBJECT) {
            return target;
        }
        JsonObject s = (JsonObject) source;
        JsonObject t = (JsonObject) target;
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // First find members to be replaced or removed
        s.forEach((key, value) -> {
            if (t.containsKey(key)) {
                // key present in both.
                if (! value.equals(t.get(key))) {
                    // If the values are equal, nop, else get diff for the values
                    builder.add(key, diff(value, t.get(key)));
                }
            } else {
                builder.addNull(key);
            }
        });
        // Then find members to be added
        t.forEach((key, value) -> {
            if (! s.containsKey(key))
                builder.add(key, value);
        });
        return builder.build();
    }

}


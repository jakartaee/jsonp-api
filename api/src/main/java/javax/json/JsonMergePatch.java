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

import java.util.Map;

/**
 * This class is an implementation of a JSON Merge Patch as specified in
 * <a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>.
 */

public class JsonMergePatch {

    /**
     * Applies the specified patch the specified target.
     * The target is not modified by the patch.
     *
     * @param target the {@code JsonValue} to apply the patch operations
     * @param patch the patch
     * @return the {@code JsonValue} as the result of applying the patch
     *    operations on the target.
     */
    public static JsonValue mergePatch(JsonValue target, JsonValue patch) {

        if (patch.getValueType() != JsonValue.ValueType.OBJECT ||
                target.getValueType() != JsonValue.ValueType.OBJECT) {
            return patch;
        }
        JsonObject targetObj = (JsonObject) target;
        JsonObjectBuilder builder = Json.createObjectBuilder(targetObj);
        for (Map.Entry<String, JsonValue> entry: ((JsonObject)patch).entrySet()) {
            String key = entry.getKey();
            JsonValue value = entry.getValue();
            if (value == JsonValue.NULL) {
                if (targetObj.containsKey(key)) {
                    builder.remove(key);
                }
            } else {
                builder.add(key, mergePatch(targetObj.get(key), value));
            }
        }
        return builder.build();
    }

    /**
     * Generate a JSON Merge Patch from the source and target {@code JsonValue}.
     * @param source the source
     * @param target the target
     * @return a JSON Patch which when applied to the source, yields the target
     */
    public static JsonValue diff(JsonValue source, JsonValue target) {
        if (source.getValueType() != JsonValue.ValueType.OBJECT ||
                target.getValueType() != JsonValue.ValueType.OBJECT) {
            return target;
        }
        JsonObject s = (JsonObject) source;
        JsonObject t = (JsonObject) target;
        JsonObjectBuilder builder = Json.createObjectBuilder();
        // First find members to be replaced or removed
        for (Map.Entry<String, JsonValue> entry: s.entrySet()) {
            String key = entry.getKey();
            JsonValue value = entry.getValue();
            if (t.containsKey(key)) {
                // key present in both.
                if (! s.get(key).equals(t.get(key))) {
                    // If the values are equal, nop, else get diff for the values
                    builder.add(key, diff(s.get(key), t.get(key)));
                }
            } else {
                builder.addNull(key);
            }
        }
        // Then find members to be added
        for (Map.Entry<String, JsonValue> entry: t.entrySet()) {
            String key = entry.getKey();
            if (! s.containsKey(key)) {
                builder.add(key, entry.getValue());
            }
        }
        return builder.build();
    }
}


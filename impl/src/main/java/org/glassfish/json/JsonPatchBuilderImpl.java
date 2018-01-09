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
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonPatch;
import javax.json.JsonPatch.Operation;
import javax.json.JsonPatchBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;

/**
 * A builder for constructing a JSON Patch by adding
 * JSON Patch operations incrementally.
 * <p>
 * The following illustrates the approach.
 * <pre>
 *   JsonPatchBuilder builder = Json.createPatchBuilder();
 *   JsonPatch patch = builder.add("/John/phones/office", "1234-567")
 *                            .remove("/Amy/age")
 *                            .build();
 * </pre>
 * The result is equivalent to the following JSON Patch.
 * <pre>
 * [
 *    {"op" = "add", "path" = "/John/phones/office", "value" = "1234-567"},
 *    {"op" = "remove", "path" = "/Amy/age"}
 * ] </pre>
 *
 * @since 1.1
 */
public final class JsonPatchBuilderImpl implements JsonPatchBuilder {

    private final JsonArrayBuilder builder;

    /**
     * Creates a JsonPatchBuilderImpl, starting with the specified
     * JSON Patch
     * @param patch the JSON Patch
     */
    public JsonPatchBuilderImpl(JsonArray patch) {
        builder = Json.createArrayBuilder(patch);
    }

    /**
     * Creates JsonPatchBuilderImpl with empty JSON Patch
     */
    public JsonPatchBuilderImpl() {
        builder = Json.createArrayBuilder();
    }

    /**
     * A convenience method for {@code new JsonPatchImpl(build()).apply(target)}.
     * The target is not modified by the patch.
     *
     * @param <T> the target type, must be a subtype of {@link JsonStructure}
     * @param target the target to apply the patch operations
     * @return the transformed target after the patch
     * @throws JsonException if the supplied JSON Patch is malformed or if
     *    it contains references to non-existing members
     */
    public <T extends JsonStructure> T apply(T target) {
        return build().apply(target);
    }

    /**
     * Adds an "add" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder add(String path, JsonValue value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.ADD.operationName())
                           .add("path", path)
                           .add("value", value)
                   );
        return this;
    }

    /**
     * Adds an "add" JSON Patch operation
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder add(String path, String value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.ADD.operationName())
                           .add("path", path)
                           .add("value", value)
                   );
        return this;
    }

    /**
     * Adds an "add" JSON Patch operation
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder add(String path, int value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.ADD.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds an "add" JSON Patch operation
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder add(String path, boolean value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.ADD.operationName())
                           .add("path", path)
                           .add("value", value)
                   );
        return this;
    }

    /**
     * Adds a "remove" JSON Patch operation.
     * @param path the "path" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder remove(String path) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.REMOVE.operationName())
                           .add("path", path)
                    );
        return this;
    }

    /**
     * Adds a "replace" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder replace(String path, JsonValue value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.REPLACE.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "replace" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder replace(String path, String value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.REPLACE.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "replace" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder replace(String path, int value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.REPLACE.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "replace" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder replace(String path, boolean value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.REPLACE.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "move" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param from the "from" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder move(String path, String from) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.MOVE.operationName())
                           .add("path", path)
                           .add("from", from)
                  );
        return this;
    }

    /**
     * Adds a "copy" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param from the "from" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder copy(String path, String from) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.COPY.operationName())
                           .add("path", path)
                           .add("from", from)
                  );
        return this;
    }

    /**
     * Adds a "test" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder test(String path, JsonValue value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.TEST.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "test" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder test(String path, String value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.TEST.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "test" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder test(String path, int value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.TEST.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Adds a "test" JSON Patch operation.
     * @param path the "path" member of the operation
     * @param value the "value" member of the operation
     * @return this JsonPatchBuilder
     */
    @Override
    public JsonPatchBuilder test(String path, boolean value) {
        builder.add(Json.createObjectBuilder()
                           .add("op", Operation.TEST.operationName())
                           .add("path", path)
                           .add("value", value)
                  );
        return this;
    }

    /**
     * Returns the patch operations in a JsonArray
     * @return the patch operations in a JsonArray
     */
    public JsonArray buildAsJsonArray() {
        return builder.build();
    }

    /**
     * Returns the patch operation in a JsonPatch
     * @return the patch operation in a JsonPatch
     */
    @Override
    public JsonPatch build() {
        return new JsonPatchImpl(buildAsJsonArray());
    }
}


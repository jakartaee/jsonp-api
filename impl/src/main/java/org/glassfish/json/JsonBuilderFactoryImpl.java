/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Collection;
import org.glassfish.json.api.BufferPool;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import java.util.Collections;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonBuilderFactoryImpl implements JsonBuilderFactory {
    private final Map<String, ?> config;
    private final BufferPool bufferPool;

    JsonBuilderFactoryImpl(BufferPool bufferPool) {
        this.config = Collections.emptyMap();
        this.bufferPool = bufferPool;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return new JsonObjectBuilderImpl(bufferPool);
    }
 
    @Override
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        return new JsonObjectBuilderImpl(object, bufferPool);
    }

    @Override
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> object) {
        return new JsonObjectBuilderImpl(object, bufferPool);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return new JsonArrayBuilderImpl(bufferPool);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        return new JsonArrayBuilderImpl(array, bufferPool);
    }

    @Override
    public JsonArrayBuilder createArrayBuilder(Collection<?> collection) {
        return new JsonArrayBuilderImpl(collection, bufferPool);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }
}

/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

import org.glassfish.json.api.BufferPool;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonParserFactory;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonParserFactoryImpl implements JsonParserFactory {
    private final Map<String, ?> config = Collections.emptyMap();
    private final BufferPool bufferPool;

    JsonParserFactoryImpl(BufferPool bufferPool) {
        this.bufferPool = bufferPool;
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return new JsonParserImpl(reader, bufferPool);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return new JsonParserImpl(in, bufferPool);
    }

    @Override
    public JsonParser createParser(InputStream in, Charset charset) {
        return new JsonParserImpl(in, charset, bufferPool);
    }

    @Override
    public JsonParser createParser(JsonArray array) {
        return new JsonStructureParser(array);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }

    @Override
    public JsonParser createParser(JsonObject object) {
        return new JsonStructureParser(object);
    }
}

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

import javax.json.*;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import javax.json.spi.JsonProvider;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Jitendra Kotamraju
 * @author Kin-man Chung
 * @author Alex Soto
 */
public class JsonProviderImpl extends JsonProvider {

    private final BufferPool bufferPool = new BufferPoolImpl();

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new JsonGeneratorImpl(writer, bufferPool);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return new JsonGeneratorImpl(out, bufferPool);
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
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        BufferPool pool = null;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = bufferPool;
        }
        return new JsonParserFactoryImpl(pool);
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        Map<String, Object> providerConfig;
        boolean prettyPrinting;
        BufferPool pool;
        if (config == null) {
            providerConfig = Collections.emptyMap();
            prettyPrinting = false;
            pool = bufferPool;
        } else {
            providerConfig = new HashMap<>();
            if (prettyPrinting=JsonProviderImpl.isPrettyPrintingEnabled(config)) {
                providerConfig.put(JsonGenerator.PRETTY_PRINTING, true);
            }
            pool = (BufferPool)config.get(BufferPool.class.getName());
            if (pool != null) {
                providerConfig.put(BufferPool.class.getName(), pool);
            } else {
                pool = bufferPool;
            }
            providerConfig = Collections.unmodifiableMap(providerConfig);
        }

        return new JsonGeneratorFactoryImpl(providerConfig, prettyPrinting, pool);
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return new JsonReaderImpl(reader, bufferPool);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return new JsonReaderImpl(in, bufferPool);
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return new JsonWriterImpl(writer, bufferPool);
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return new JsonWriterImpl(out, bufferPool);
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        Map<String, Object> providerConfig;
        boolean prettyPrinting;
        BufferPool pool;
        if (config == null) {
            providerConfig = Collections.emptyMap();
            prettyPrinting = false;
            pool = bufferPool;
        } else {
            providerConfig = new HashMap<>();
            if (prettyPrinting=JsonProviderImpl.isPrettyPrintingEnabled(config)) {
                providerConfig.put(JsonGenerator.PRETTY_PRINTING, true);
            }
            pool = (BufferPool)config.get(BufferPool.class.getName());
            if (pool != null) {
                providerConfig.put(BufferPool.class.getName(), pool);
            } else {
                pool = bufferPool;
            }
            providerConfig = Collections.unmodifiableMap(providerConfig);
        }
        return new JsonWriterFactoryImpl(providerConfig, prettyPrinting, pool);
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        BufferPool pool = null;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = bufferPool;
        }
        return new JsonReaderFactoryImpl(pool);
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
    public JsonObjectBuilder createObjectBuilder(Map<String, Object> map) {
        return new JsonObjectBuilderImpl(map, bufferPool);
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
    public JsonPointer createPointer(String jsonPointer) {
        return new JsonPointerImpl(jsonPointer);
    }

    @Override
    public JsonPatchBuilder createPatchBuilder() {
        return new JsonPatchBuilderImpl();
    }

    @Override
    public JsonPatchBuilder createPatchBuilder(JsonArray array) {
        return new JsonPatchBuilderImpl(array);
    }

    @Override
    public JsonPatch createPatch(JsonArray array) {
        return new JsonPatchImpl(array);
    }

    @Override
    public JsonPatch createDiff(JsonStructure source, JsonStructure target) {
        return new JsonPatchImpl(JsonPatchImpl.diff(source, target));
    }

    @Override
    public JsonMergePatch createMergePatch(JsonValue patch) {
        return new JsonMergePatchImpl(patch);
    }

    @Override
    public JsonMergePatch createMergeDiff(JsonValue source, JsonValue target) {
        return new JsonMergePatchImpl(JsonMergePatchImpl.diff(source, target));
    }

    @Override
    public JsonString createValue(String value) {
        return new JsonStringImpl(value);
    }

    @Override
    public JsonNumber createValue(int value) {
        return JsonNumberImpl.getJsonNumber(value);
    }

    @Override
    public JsonNumber createValue(long value) {
        return JsonNumberImpl.getJsonNumber(value);
    }

    @Override
    public JsonNumber createValue(double value) {
        return JsonNumberImpl.getJsonNumber(value);
    }

    @Override
    public JsonNumber createValue(BigInteger value) {
        return JsonNumberImpl.getJsonNumber(value);
    }

    @Override
    public JsonNumber createValue(BigDecimal value) {
        return JsonNumberImpl.getJsonNumber(value);
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String,?> config) {
        BufferPool pool = null ;
        if (config != null && config.containsKey(BufferPool.class.getName())) {
            pool = (BufferPool)config.get(BufferPool.class.getName());
        }
        if (pool == null) {
            pool = bufferPool;
        }
        return new JsonBuilderFactoryImpl(pool);
    }

    static boolean isPrettyPrintingEnabled(Map<String, ?> config) {
        return config.containsKey(JsonGenerator.PRETTY_PRINTING);
    }
}

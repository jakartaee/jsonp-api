/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.json.customprovider;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

/**
 * @author Jitendra Kotamraju
 */
public class TestProvider extends JsonProvider {

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new TestGenerator(writer);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return new TestGenerator(new OutputStreamWriter(out));
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return null;
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return null;
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return null;
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return null;
    }

    @Override
    public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return null;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return null;
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
        return null;
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return null;
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return null;
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        return null;
    }


}

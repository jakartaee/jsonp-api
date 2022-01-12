/*
 * Copyright (c) 2021, 2022 Oracle and/or its affiliates. All rights reserved.
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

package jsonp.tck.api.provider;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests related to JsonProvider.
 *
 */
public class JsonProviderTest {

    private static final String JSONP_PROVIDER_FACTORY = "jakarta.json.provider";

    
    /**
     * Verifies it is possible to obtain the JsonProvider implementation from a System property.
     */
    @Test
    public void systemProperty() {
        System.setProperty(JSONP_PROVIDER_FACTORY, DummyJsonProvider.class.getName());
        JsonProvider provider = JsonProvider.provider();
        assertEquals(DummyJsonProvider.class, provider.getClass());
    }

    public static class DummyJsonProvider extends JsonProvider {

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

        @Override
        public JsonGenerator createGenerator(Writer writer) {
            return null;
        }

        @Override
        public JsonGenerator createGenerator(OutputStream out) {
            return null;
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
        
    }
}

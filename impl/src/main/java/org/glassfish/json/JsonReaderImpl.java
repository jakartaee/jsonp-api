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

import org.glassfish.json.api.BufferPool;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;

/**
 * JsonReader impl using parser and builders.
 *
 * @author Jitendra Kotamraju
 */
class JsonReaderImpl implements JsonReader {
    private final JsonParserImpl parser;
    private boolean readDone;
    private final BufferPool bufferPool;

    JsonReaderImpl(Reader reader, BufferPool bufferPool) {
        parser = new JsonParserImpl(reader, bufferPool);
        this.bufferPool = bufferPool;
    }

    JsonReaderImpl(InputStream in, BufferPool bufferPool) {
        parser = new JsonParserImpl(in, bufferPool);
        this.bufferPool = bufferPool;
    }

    JsonReaderImpl(InputStream in, Charset charset, BufferPool bufferPool) {
        parser = new JsonParserImpl(in, charset, bufferPool);
        this.bufferPool = bufferPool;
    }

    @Override
    public JsonStructure read() {
        if (readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        readDone = true;
        if (parser.hasNext()) {
            try {
                JsonParser.Event e = parser.next();
                if (e == JsonParser.Event.START_ARRAY) {
                    return parser.getArray();
                } else if (e == JsonParser.Event.START_OBJECT) {
                    return parser.getObject();
                }
            } catch (IllegalStateException ise) {
                throw new JsonParsingException(ise.getMessage(), ise, parser.getLastCharLocation());
            }
        }
        throw new JsonException(JsonMessages.INTERNAL_ERROR());
    }

    @Override
    public JsonObject readObject() {
        if (readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        readDone = true;
        if (parser.hasNext()) {
            try {
                parser.next();
                return parser.getObject();
            } catch (IllegalStateException ise) {
                throw new JsonParsingException(ise.getMessage(), ise, parser.getLastCharLocation());
            }
        }
        throw new JsonException(JsonMessages.INTERNAL_ERROR());
    }

    @Override
    public JsonArray readArray() {
        if (readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        readDone = true;
        if (parser.hasNext()) {
            try {
                parser.next();
                return parser.getArray();
            } catch (IllegalStateException ise) {
                throw new JsonParsingException(ise.getMessage(), ise, parser.getLastCharLocation());
            }
        }
        throw new JsonException(JsonMessages.INTERNAL_ERROR());
    }

    @Override
    public JsonValue readValue() {
        if (readDone) {
            throw new IllegalStateException(JsonMessages.READER_READ_ALREADY_CALLED());
        }
        readDone = true;
        if (parser.hasNext()) {
            try {
                parser.next();
                return parser.getValue();
            } catch (IllegalStateException ise) {
                throw new JsonParsingException(ise.getMessage(), ise, parser.getLastCharLocation());
            }
        }
        throw new JsonException(JsonMessages.INTERNAL_ERROR());
    }

    @Override
    public void close() {
        readDone = true;
        parser.close();
    }
}

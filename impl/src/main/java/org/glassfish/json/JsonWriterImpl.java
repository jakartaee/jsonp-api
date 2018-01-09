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

import javax.json.*;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * JsonWriter impl using generator.
 *
 * @author Jitendra Kotamraju
 */
class JsonWriterImpl implements JsonWriter {

    private final JsonGeneratorImpl generator;
    private boolean writeDone;
    private final NoFlushOutputStream os;

    JsonWriterImpl(Writer writer, BufferPool bufferPool) {
        this(writer, false, bufferPool);
    }

    JsonWriterImpl(Writer writer, boolean prettyPrinting, BufferPool bufferPool) {
        generator = prettyPrinting
                ? new JsonPrettyGeneratorImpl(writer, bufferPool)
                : new JsonGeneratorImpl(writer, bufferPool);
        os = null;
    }

    JsonWriterImpl(OutputStream out, BufferPool bufferPool) {
        this(out, StandardCharsets.UTF_8, false, bufferPool);
    }

    JsonWriterImpl(OutputStream out, boolean prettyPrinting, BufferPool bufferPool) {
        this(out, StandardCharsets.UTF_8, prettyPrinting, bufferPool);
    }

    JsonWriterImpl(OutputStream out, Charset charset,
                   boolean prettyPrinting, BufferPool bufferPool) {
        // Decorating the given stream, so that buffered contents can be
        // written without actually flushing the stream.
        this.os = new NoFlushOutputStream(out);
        generator = prettyPrinting
                ? new JsonPrettyGeneratorImpl(os, charset, bufferPool)
                : new JsonGeneratorImpl(os, charset, bufferPool);
    }

    @Override
    public void writeArray(JsonArray array) {
        if (writeDone) {
            throw new IllegalStateException(JsonMessages.WRITER_WRITE_ALREADY_CALLED());
        }
        writeDone = true;
        generator.writeStartArray();
        for(JsonValue value : array) {
            generator.write(value);
        }
        generator.writeEnd();
        // Flush the generator's buffered contents. This won't work for byte
        // streams as intermediary OutputStreamWriter buffers.
        generator.flushBuffer();
        // Flush buffered contents but not the byte stream. generator.flush()
        // does OutputStreamWriter#flushBuffer (package private) and underlying
        // byte stream#flush(). Here underlying stream's flush() is no-op.
        if (os != null) {
            generator.flush();
        }
    }

    @Override
    public void writeObject(JsonObject object) {
        if (writeDone) {
            throw new IllegalStateException(JsonMessages.WRITER_WRITE_ALREADY_CALLED());
        }
        writeDone = true;
        generator.writeStartObject();
        for(Map.Entry<String, JsonValue> e : object.entrySet()) {
            generator.write(e.getKey(), e.getValue());
        }
        generator.writeEnd();
        // Flush the generator's buffered contents. This won't work for byte
        // streams as intermediary OutputStreamWriter buffers.
        generator.flushBuffer();
        // Flush buffered contents but not the byte stream. generator.flush()
        // does OutputStreamWriter#flushBuffer (package private) and underlying
        // byte stream#flush(). Here underlying stream's flush() is no-op.
        if (os != null) {
            generator.flush();
        }
    }

    @Override
    public void write(JsonStructure value) {
        if (value instanceof JsonArray) {
            writeArray((JsonArray)value);
        } else {
            writeObject((JsonObject)value);
        }
    }

    @Override
    public void write(JsonValue value) {
        switch (value.getValueType()) {
            case OBJECT:
                writeObject((JsonObject) value);
                return;
            case ARRAY:
                writeArray((JsonArray) value);
                return;
            default:
                if (writeDone) {
                    throw new IllegalStateException(JsonMessages.WRITER_WRITE_ALREADY_CALLED());
                }
                writeDone = true;
                generator.write(value);
                generator.flushBuffer();
                if (os != null) {
                    generator.flush();
                }
        }
    }

    @Override
    public void close() {
        writeDone = true;
        generator.close();
    }

    private static final class NoFlushOutputStream extends FilterOutputStream {
        public NoFlushOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            out.write(b, off ,len);
        }

        @Override
        public void flush() {
            // no-op
        }
    }

}

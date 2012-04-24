/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json;

import javax.json.*;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorImpl implements Closeable {
    private final Writer writer;
    
    public JsonGeneratorImpl(Writer writer) {
        this.writer = writer;
    }

    public JsonObjectBuilder<Closeable> beginObject() {
        write("{");
        return new JsonObjectBuilderImpl<Closeable>(this, writer);
    }

    public JsonArrayBuilder<Closeable> beginArray() {
        write("[");
        return new JsonArrayBuilderImpl<Closeable>(this, writer);
    }

    public void close() {
    }

    private void write(String str) {
        try {
            writer.write(str);
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    private static class JsonObjectBuilderImpl<T> implements JsonObjectBuilder<T> {
        private final T enclosing;
        private final Writer writer;
        private boolean first = true;

        JsonObjectBuilderImpl(T enclosing, Writer writer) {
            this.enclosing = enclosing;
            this.writer = writer;
        }

        @Override
        public T endObject() {
            try {
                writer.write("}");
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
            return enclosing;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, JsonValue value) {
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, String value) {
            writeValue(name, value);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigInteger value) {
            writeValue(name, value.toString());
            return this;

        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigDecimal value) {
            writeValue(name, value.toString());
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, int value) {
            writeValue(name, String.valueOf(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, long value) {
            writeValue(name, String.valueOf(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, double value) {
            writeValue(name, String.valueOf(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, boolean value) {
            writeValue(name, value ? "true" : "false");
            return this;
        }

        @Override
        public JsonObjectBuilder<T> addArray(String name, Iterable<JsonValue> values) {
            return null;
        }

        @Override
        public JsonObjectBuilder<T> addNull(String name) {
            writeValue(name, "null");
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonObjectBuilder<T>> beginObject(String name) {
            writeValue(name, "{");
            return new JsonObjectBuilderImpl<JsonObjectBuilder<T>>(this, writer);
        }

        @Override
        public JsonArrayBuilder<JsonObjectBuilder<T>> beginArray(String name) {
            writeValue(name, "[");
            return new JsonArrayBuilderImpl<JsonObjectBuilder<T>>(this, writer);
        }

        private void writeValue(String name, String value) {
            try {
                writeComma();
                writer.write('"');
                writer.write(name);
                writer.write('"');
                writer.write(':');
                writer.write(value);
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }

        private void writeComma() {
            try {
                if (!first) {
                    writer.write(',');
                }
                first = false;
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }
    }

    private static class JsonArrayBuilderImpl<T> implements JsonArrayBuilder<T> {
        private final T enclosing;
        private final Writer writer;
        private boolean first = true;

        JsonArrayBuilderImpl(T enclosing, Writer writer) {
            this.enclosing = enclosing;
            this.writer = writer;
        }

        @Override
        public T endArray() {
            try {
                writer.write(']');
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
            return enclosing;
        }

        @Override
        public JsonArrayBuilder<T> add(JsonValue value) {
            throw new JsonException("TODO");
        }

        @Override
        public JsonArrayBuilder<T> add(String value) {
            writeValue(value);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigDecimal value) {
            writeValue(value.toString());
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigInteger value) {
            writeValue(value.toString());
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(int value) {
            writeValue(String.valueOf(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(long value) {
            writeValue(String.valueOf(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(double value) {
            writeValue(String.valueOf(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(boolean value) {
            writeValue(value ? "true" : "false");
            return this;
        }

        @Override
        public JsonArrayBuilder<T> addNull() {
            writeValue("null");
            return this;
        }

        @Override
        public JsonArrayBuilder<T> addArray(Iterable<JsonValue> values) {
            return null;
        }

        @Override
        public JsonObjectBuilder<JsonArrayBuilder<T>> beginObject() {
            writeValue("{");
            return new JsonObjectBuilderImpl<JsonArrayBuilder<T>>(this, writer);
        }

        @Override
        public JsonArrayBuilder<JsonArrayBuilder<T>> beginArray() {
            writeValue("[");
            return new JsonArrayBuilderImpl<JsonArrayBuilder<T>>(this, writer);
        }

        private void writeComma() {
            try {
                if (!first) {
                    writer.write(',');
                }
                first = false;
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }

        private void writeValue(String value) {
            try {
                writeComma();
                writer.write(value);
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }
    }
}

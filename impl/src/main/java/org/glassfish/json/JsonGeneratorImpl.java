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
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorImpl implements JsonGenerator {
    private final Writer writer;
    private final JsonConfiguration config;
    
    public JsonGeneratorImpl(Writer writer) {
        this.writer = writer;
        this.config = null;
    }

    public JsonGeneratorImpl(Writer writer, JsonConfiguration config) {
        this.writer = writer;
        this.config = config;
    }

    public JsonGeneratorImpl(OutputStream out) {
        try {
            this.writer = new OutputStreamWriter(out, "UTF-8");
            this.config = null;
        } catch (UnsupportedEncodingException e) {
            throw new JsonException(e);
        }
    }

    public JsonGeneratorImpl(OutputStream out, JsonConfiguration config) {
        try {
            this.writer = new OutputStreamWriter(out, "UTF-8");
            this.config = config;
        } catch (UnsupportedEncodingException e) {
            throw new JsonException(e);
        }
    }

    public JsonGeneratorImpl(OutputStream out, String encoding) {
        try {
            this.writer = new OutputStreamWriter(out, encoding);
            this.config = null;
        } catch (UnsupportedEncodingException e) {
            throw new JsonException(e);
        }
    }

    public JsonGeneratorImpl(OutputStream out, String encoding, JsonConfiguration config) {
        try {
            this.writer = new OutputStreamWriter(out, encoding);
            this.config = config;
        } catch (UnsupportedEncodingException e) {
            throw new JsonException(e);
        }
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

        try {
            writer.flush();
        } catch (IOException e) {
            throw new JsonException(e);
        }
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
            switch (value.getValueType()) {
                case ARRAY:
                    JsonArray array = (JsonArray)value;
                    JsonArrayBuilder<JsonObjectBuilder<T>> arrayBuilder = beginArray(name);
                    for(JsonValue child: array.getValues()) {
                        arrayBuilder.add(child);
                    }
                    arrayBuilder.endArray();
                    break;
                case OBJECT:
                    JsonObject object = (JsonObject)value;
                    JsonObjectBuilder<JsonObjectBuilder<T>> objectBuilder = beginObject(name);
                    for(Map.Entry<String, JsonValue> member: object.getValues().entrySet()) {
                        objectBuilder.add(member.getKey(), member.getValue());
                    }
                    objectBuilder.endObject();
                    break;
                case STRING:
                    JsonString str = (JsonString)value;
                    add(name, str.getValue());
                    break;
                case NUMBER:
                    JsonNumber number = (JsonNumber)value;
//                    writeValue(name, number.toString());
                    switch (number.getNumberType()) {
                        case INT:
                            add(name, number.getIntValue());
                            break;
                        case LONG:
                            add(name, number.getLongValue());
                            break;
                        case BIG_INTEGER:
                            add(name, number.getBigIntegerValue());
                            break;
                        case DOUBLE:
                            add(name, number.getDoubleValue());
                            break;
                        case BIG_DECIMAL:
                            add(name, number.getBigDecimalValue());
                            break;
                    }
                    break;
                case TRUE:
                    add(name, true);
                    break;
                case FALSE:
                    add(name, false);
                    break;
                case NULL:
                    addNull(name);
                    break;
            }
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, String value) {
            writeString(name, value);
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
                writeEscapedString(writer, name);
                writer.write(':');
                writer.write(value);
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }

        private void writeString(String name, String value) {
            try {
                writeComma();
                writeEscapedString(writer, name);
                writer.write(':');
                writeEscapedString(writer, value);
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
            switch (value.getValueType()) {
                case ARRAY:
                    JsonArray array = (JsonArray)value;
                    JsonArrayBuilder<JsonArrayBuilder<T>> arrayBuilder = beginArray();
                    for(JsonValue child: array.getValues()) {
                        arrayBuilder.add(child);
                    }
                    arrayBuilder.endArray();
                    break;
                case OBJECT:
                    JsonObject object = (JsonObject)value;
                    JsonObjectBuilder<JsonArrayBuilder<T>> objectBuilder = beginObject();
                    for(Map.Entry<String, JsonValue> member: object.getValues().entrySet()) {
                        objectBuilder.add(member.getKey(), member.getValue());
                    }
                    objectBuilder.endObject();
                    break;
                case STRING:
                    JsonString str = (JsonString)value;
                    add(str.getValue());
                    break;
                case NUMBER:
                    JsonNumber number = (JsonNumber)value;
//                    writeValue(number.toString());
                    switch (number.getNumberType()) {
                        case INT:
                            add(number.getIntValue());
                            break;
                        case LONG:
                            add(number.getLongValue());
                            break;
                        case BIG_INTEGER:
                            add(number.getBigIntegerValue());
                            break;
                        case DOUBLE:
                            add(number.getDoubleValue());
                            break;
                        case BIG_DECIMAL:
                            add(number.getBigDecimalValue());
                            break;
                    }
                    break;
                case TRUE:
                    add(true);
                    break;
                case FALSE:
                    add(false);
                    break;
                case NULL:
                    addNull();
                    break;
            }
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(String value) {
            writeString(value);
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

        private void writeString(String value) {
            try {
                writeComma();
                writeEscapedString(writer, value);
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
        }

    }

    static void writeEscapedString(Writer w, String string) throws IOException {
        w.write('"');
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            switch (c) {
                case '"':
                case '\\':
                    w.write('\\');
                    w.write(c);
                    break;
                case '\b':
                    w.write("\\b");
                    break;
                case '\f':
                    w.write("\\f");
                    break;
                case '\n':
                    w.write("\\n");
                    break;
                case '\r':
                    w.write("\\r");
                    break;
                case '\t':
                    w.write("\\t");
                    break;
                default:
                    if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
                        String hex = "000" + Integer.toHexString(c);
                        w.write("\\u" + hex.substring(hex.length() - 4));
                    } else {
                        w.write(c);
                    }
            }
        }
        w.write('"');
    }
}
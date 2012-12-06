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
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorImpl implements JsonGenerator {
    protected final Writer writer;
    protected Context currentContext = new Context(Scope.IN_NONE);
    private final Deque<Context> stack = new ArrayDeque<Context>();

    public JsonGeneratorImpl(Writer writer) {
        this.writer = writer;
    }

    public JsonGeneratorImpl(OutputStream out) {
        try {
            this.writer = new OutputStreamWriter(out, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new JsonException(e);
        }
    }

    public JsonGeneratorImpl(OutputStream out, Charset encoding) {
        this.writer = new OutputStreamWriter(out, encoding);
    }

    @Override
    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    private static enum Scope {
        IN_NONE,
        IN_OBJECT,
        IN_ARRAY
    }

    @Override
    public JsonGenerator writeStartObject() {
        if (currentContext.scope == Scope.IN_OBJECT) {
            throw new JsonGenerationException("writeStartObject() cannot be called in object context");
        }
        if (currentContext.scope == Scope.IN_NONE && !currentContext.first) {
            throw new JsonGenerationException("writeStartObject() cannot be called in no context more than once");
        }
        try {
            writeComma();
            writer.write("{");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        stack.push(currentContext);
        currentContext = new Context(Scope.IN_OBJECT);
        return this;
    }

    @Override
    public JsonGenerator writeStartObject(String name) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("writeStartObject(String) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write("{");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        stack.push(currentContext);
        currentContext = new Context(Scope.IN_OBJECT);
        return this;
    }

    private JsonGenerator writeName(String name) {
        try {
            writeComma();
            writeEscapedString(writer, name);
            writer.write(":");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, String fieldValue) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, String) can only be called in object context");
        }
        try {
            writeName(name);
            writeEscapedString(writer, fieldValue);
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, int value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, int) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write(String.valueOf(value));
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, long value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, long) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write(String.valueOf(value));
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, double value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, double) can only be called in object context");
        }
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new NumberFormatException("write(String, double) value cannot be Infinite or NaN");
        }
        try {
            writeName(name);
            writer.write(String.valueOf(value));
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigInteger value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, BigInteger) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write(String.valueOf(value));
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, BigDecimal value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, BigDecimal) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write(String.valueOf(value));
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(String name, boolean value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, boolean) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write(value? "true" : "false");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator writeNull(String name) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("writeNull(String) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write("null");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator write(JsonValue value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(JsonValue) can only be called in array context");
        }
        switch (value.getValueType()) {
            case ARRAY:
                JsonArray array = (JsonArray)value;
                writeStartArray();
                for(JsonValue child: array.getValues()) {
                    write(child);
                }
                writeEnd();
                break;
            case OBJECT:
                JsonObject object = (JsonObject)value;
                writeStartObject();
                for(Map.Entry<String, JsonValue> member: object.getValues().entrySet()) {
                    write(member.getKey(), member.getValue());
                }
                writeEnd();
                break;
            case STRING:
                JsonString str = (JsonString)value;
                write(str.getValue());
                break;
            case NUMBER:
                JsonNumber number = (JsonNumber)value;
                writeValue(number.toString());
                break;
            case TRUE:
                write(true);
                break;
            case FALSE:
                write(false);
                break;
            case NULL:
                writeNull();
                break;
        }

        return this;
    }

    @Override
    public JsonGenerator writeStartArray() {
        if (currentContext.scope == Scope.IN_OBJECT) {
            throw new JsonGenerationException("writeStartArray() cannot be called in object context");
        }
        if (currentContext.scope == Scope.IN_NONE && !currentContext.first) {
            throw new JsonGenerationException("writeStartArray() cannot be called in no context more than once");
        }
        try {
            writeComma();
            writer.write("[");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        stack.push(currentContext);
        currentContext = new Context(Scope.IN_ARRAY);
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("writeStartArray(String) can only be called in object context");
        }
        try {
            writeName(name);
            writer.write("[");
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
        stack.push(currentContext);
        currentContext = new Context(Scope.IN_ARRAY);
        return this;
    }

    @Override
    public JsonGenerator write(String name, JsonValue value) {
        if (currentContext.scope != Scope.IN_OBJECT) {
            throw new JsonGenerationException("write(String, JsonValue) can only be called in object context");
        }
        switch (value.getValueType()) {
            case ARRAY:
                JsonArray array = (JsonArray)value;
                writeStartArray(name);
                for(JsonValue child: array.getValues()) {
                    write(child);
                }
                writeEnd();
                break;
            case OBJECT:
                JsonObject object = (JsonObject)value;
                writeStartObject(name);
                for(Map.Entry<String, JsonValue> member: object.getValues().entrySet()) {
                    write(member.getKey(), member.getValue());
                }
                writeEnd();
                break;
            case STRING:
                JsonString str = (JsonString)value;
                write(name, str.getValue());
                break;
            case NUMBER:
                JsonNumber number = (JsonNumber)value;
                writeValue(name, number.toString());
                break;
            case TRUE:
                write(name, true);
                break;
            case FALSE:
                write(name, false);
                break;
            case NULL:
                writeNull(name);
                break;
        }
        return this;
    }

    public JsonGenerator write(String value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(String) can only be called in array context");
        }
        try {
            writeComma();
            writeEscapedString(writer, value);
        } catch (IOException e) {
            throw new JsonException(e);
        }
        return this;
    }


    public JsonGenerator write(int value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(int) can only be called in array context");
        }
        try {
            writeComma();
            writer.write(String.valueOf(value));
        } catch (IOException e) {
            throw new JsonException(e);
        }
        return this;
    }

    @Override
    public JsonGenerator write(long value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(long) can only be called in array context");
        }
        writeValue(String.valueOf(value));
        return this;
    }

    @Override
    public JsonGenerator write(double value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(double) can only be called in array context");
        }
        if (Double.isInfinite(value) || Double.isNaN(value)) {
            throw new NumberFormatException("write(double) value cannot be Infinite or NaN");
        }
        writeValue(String.valueOf(value));
        return this;
    }

    @Override
    public JsonGenerator write(BigInteger value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(BigInteger) can only be called in array context");
        }
        writeValue(value.toString());
        return this;
    }

    @Override
    public JsonGenerator write(BigDecimal value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(BigDecimal) can only be called in array context");
        }
        writeValue(value.toString());
        return this;
    }

    public JsonGenerator write(boolean value) {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("write(boolean) can only be called in array context");
        }
        try {
            writeComma();
            writer.write(value ? "true" : "false");
        } catch (IOException e) {
            throw new JsonException(e);
        }
        return this;
    }

    public JsonGenerator writeNull() {
        if (currentContext.scope != Scope.IN_ARRAY) {
            throw new JsonGenerationException("writeNull() can only be called in array context");
        }
        try {
            writeComma();
            writer.write("null");
        } catch (IOException e) {
            throw new JsonException(e);
        }
        return this;
    }

    private void writeValue(String value) {
        try {
            writeComma();
            writer.write(value);
        } catch(IOException ioe) {
            throw new JsonException(ioe);
        }
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

    @Override
    public JsonGenerator writeEnd() {
        if (currentContext.scope == Scope.IN_NONE) {
            throw new JsonGenerationException("writeEnd() cannot be called in no context");
        }
        try {
            writer.write(currentContext.scope == Scope.IN_ARRAY ? ']' : '}');
        } catch (IOException e) {
            throw new JsonException(e);
        }
        currentContext = stack.pop();
        return this;
    }

    protected void writeComma() throws IOException {
        if (!currentContext.first) {
            writer.write(",");
        }
        currentContext.first = false;
    }

    private static class Context {
        boolean first = true;
        final Scope scope;

        Context(Scope scope) {
            this.scope = scope;
        }

    }

    public void close() {
        if (currentContext.scope != Scope.IN_NONE || currentContext.first) {
            throw new JsonGenerationException("Generating incomplete JSON");
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            throw new JsonException(ioe);
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
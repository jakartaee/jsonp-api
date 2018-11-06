/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;

import org.glassfish.json.JsonTokenizer.JsonToken;
import org.glassfish.json.api.BufferPool;

/**
 * JSON parser implementation. NoneContext, ArrayContext, ObjectContext is used
 * to go to next parser state.
 *
 * @author Jitendra Kotamraju
 * @author Kin-man Chung
 */
public class JsonParserImpl implements JsonParser {

    private final BufferPool bufferPool;
    private Context currentContext = new NoneContext();
    private Event currentEvent;

    private final Stack stack = new Stack();
    private final JsonTokenizer tokenizer;

    public JsonParserImpl(Reader reader, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        tokenizer = new JsonTokenizer(reader, bufferPool);
    }

    public JsonParserImpl(InputStream in, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        UnicodeDetectingInputStream uin = new UnicodeDetectingInputStream(in);
        tokenizer = new JsonTokenizer(new InputStreamReader(uin, uin.getCharset()), bufferPool);
    }

    public JsonParserImpl(InputStream in, Charset encoding, BufferPool bufferPool) {
        this.bufferPool = bufferPool;
        tokenizer = new JsonTokenizer(new InputStreamReader(in, encoding), bufferPool);
    }

    @Override
    public String getString() {
        if (currentEvent == Event.KEY_NAME || currentEvent == Event.VALUE_STRING
                || currentEvent == Event.VALUE_NUMBER) {
            return tokenizer.getValue();
        }
        throw new IllegalStateException(
                JsonMessages.PARSER_GETSTRING_ERR(currentEvent));
    }

    @Override
    public boolean isIntegralNumber() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(
                    JsonMessages.PARSER_ISINTEGRALNUMBER_ERR(currentEvent));
        }
        return tokenizer.isIntegral();
    }

    @Override
    public int getInt() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(
                    JsonMessages.PARSER_GETINT_ERR(currentEvent));
        }
        return tokenizer.getInt();
    }

    boolean isDefinitelyInt() {
        return tokenizer.isDefinitelyInt();
    }

    boolean isDefinitelyLong() {
    	return tokenizer.isDefinitelyLong();
    }

    @Override
    public long getLong() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(
                    JsonMessages.PARSER_GETLONG_ERR(currentEvent));
        }
        return tokenizer.getLong();
    }

    @Override
    public BigDecimal getBigDecimal() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException(
                    JsonMessages.PARSER_GETBIGDECIMAL_ERR(currentEvent));
        }
        return tokenizer.getBigDecimal();
    }

    @Override
    public JsonArray getArray() {
        if (currentEvent != Event.START_ARRAY) {
            throw new IllegalStateException(
                JsonMessages.PARSER_GETARRAY_ERR(currentEvent));
        }
        return getArray(new JsonArrayBuilderImpl(bufferPool));
    }

    @Override
    public JsonObject getObject() {
        if (currentEvent != Event.START_OBJECT) {
            throw new IllegalStateException(
                JsonMessages.PARSER_GETOBJECT_ERR(currentEvent));
        }
        return getObject(new JsonObjectBuilderImpl(bufferPool));
    }

    @Override
    public JsonValue getValue() {
        switch (currentEvent) {
            case START_ARRAY:
                return getArray(new JsonArrayBuilderImpl(bufferPool));
            case START_OBJECT:
                return getObject(new JsonObjectBuilderImpl(bufferPool));
            case KEY_NAME:
            case VALUE_STRING:
                return new JsonStringImpl(getString());
            case VALUE_NUMBER:
                if (isDefinitelyInt()) {
                    return JsonNumberImpl.getJsonNumber(getInt());
                } else if (isDefinitelyLong()) {
                    return JsonNumberImpl.getJsonNumber(getLong());
                }
                return JsonNumberImpl.getJsonNumber(getBigDecimal());
            case VALUE_TRUE:
                return JsonValue.TRUE;
            case VALUE_FALSE:
                return JsonValue.FALSE;
            case VALUE_NULL:
                return JsonValue.NULL;
            case END_ARRAY:
            case END_OBJECT:
            default:
            	throw new IllegalStateException(JsonMessages.PARSER_GETVALUE_ERR(currentEvent));
        }
    }

    @Override
    public Stream<JsonValue> getArrayStream() {
        if (currentEvent != Event.START_ARRAY) {
            throw new IllegalStateException(
                JsonMessages.PARSER_GETARRAY_ERR(currentEvent));
        }
        Spliterator<JsonValue> spliterator =
                new Spliterators.AbstractSpliterator<JsonValue>(Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public Spliterator<JsonValue> trySplit() {
                return null;
            }
            @Override
            public boolean tryAdvance(Consumer<? super JsonValue> action) {
                if (action == null) {
                    throw new NullPointerException();
                }
                if (! hasNext()) {
                    return false;
                }
                if (next() == JsonParser.Event.END_ARRAY) {
                    return false;
                }
                action.accept(getValue());
                return true;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public Stream<Map.Entry<String, JsonValue>> getObjectStream() {
        if (currentEvent != Event.START_OBJECT) {
            throw new IllegalStateException(
                JsonMessages.PARSER_GETOBJECT_ERR(currentEvent));
        }
        Spliterator<Map.Entry<String, JsonValue>> spliterator =
                new Spliterators.AbstractSpliterator<Map.Entry<String, JsonValue>>(Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public Spliterator<Map.Entry<String,JsonValue>> trySplit() {
                return null;
            }
            @Override
            public boolean tryAdvance(Consumer<? super Map.Entry<String, JsonValue>> action) {
                if (action == null) {
                    throw new NullPointerException();
                }
                if (! hasNext()) {
                    return false;
                }
                JsonParser.Event e = next();
                if (e == JsonParser.Event.END_OBJECT) {
                    return false;
                }
                if (e != JsonParser.Event.KEY_NAME) {
                    throw new JsonException(JsonMessages.INTERNAL_ERROR());
                }
                String key = getString();
                if (! hasNext()) {
                    throw new JsonException(JsonMessages.INTERNAL_ERROR());
                }
                next();
                JsonValue value = getValue();
                action.accept(new AbstractMap.SimpleImmutableEntry<>(key, value));
                return true;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public Stream<JsonValue> getValueStream() {
        if (! (currentContext instanceof NoneContext)) {
            throw new IllegalStateException(
                JsonMessages.PARSER_GETVALUESTREAM_ERR());
        }
        Spliterator<JsonValue> spliterator =
                new Spliterators.AbstractSpliterator<JsonValue>(Long.MAX_VALUE, Spliterator.ORDERED) {
            @Override
            public Spliterator<JsonValue> trySplit() {
                return null;
            }
            @Override
            public boolean tryAdvance(Consumer<? super JsonValue> action) {
                if (action == null) {
                    throw new NullPointerException();
                }
                if (! hasNext()) {
                    return false;
                }
                next();
                action.accept(getValue());
                return true;
            }
        };
        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public void skipArray() {
        if (currentEvent == Event.START_ARRAY) {
            currentContext.skip();
            currentContext = stack.pop();
            currentEvent = Event.END_ARRAY;
        }
    }

    @Override
    public void skipObject() {
        if (currentEvent == Event.START_OBJECT) {
            currentContext.skip();
            currentContext = stack.pop();
            currentEvent = Event.END_OBJECT;
        }
    }

    private JsonArray getArray(JsonArrayBuilder builder) {
        while(hasNext()) {
            JsonParser.Event e = next();
            if (e == JsonParser.Event.END_ARRAY) {
                return builder.build();
            }
            builder.add(getValue());
        }
        throw parsingException(JsonToken.EOF, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL, SQUARECLOSE]");
    }

    private JsonObject getObject(JsonObjectBuilder builder) {
        while(hasNext()) {
            JsonParser.Event e = next();
            if (e == JsonParser.Event.END_OBJECT) {
                return builder.build();
            }
            String key = getString();
            next();
            builder.add(key, getValue());
        }
        throw parsingException(JsonToken.EOF, "[STRING, CURLYCLOSE]");
    }

    @Override
    public JsonLocation getLocation() {
        return tokenizer.getLocation();
    }

    public JsonLocation getLastCharLocation() {
        return tokenizer.getLastCharLocation();
    }

    @Override
    public boolean hasNext() {
        if (stack.isEmpty() && (currentEvent != null && currentEvent.compareTo(Event.KEY_NAME) > 0)) {
            JsonToken token = tokenizer.nextToken();
            if (token != JsonToken.EOF) {
                throw new JsonParsingException(JsonMessages.PARSER_EXPECTED_EOF(token),
                        getLastCharLocation());
            }
            return false;
        } else if (!stack.isEmpty() && !tokenizer.hasNextToken()) {
            currentEvent = currentContext.getNextEvent();
            return false;
        }
        return true;
    }

    @Override
    public Event next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentEvent = currentContext.getNextEvent();
    }

    @Override
    public void close() {
        try {
            tokenizer.close();
        } catch (IOException e) {
            throw new JsonException(JsonMessages.PARSER_TOKENIZER_CLOSE_IO(), e);
        }
    }

    // Using the optimized stack impl as we don't require other things
    // like iterator etc.
    private static final class Stack {
        private Context head;

        private void push(Context context) {
            context.next = head;
            head = context;
        }

        private Context pop() {
            if (head == null) {
                throw new NoSuchElementException();
            }
            Context temp = head;
            head = head.next;
            return temp;
        }

        private Context peek() {
            return head;
        }

        private boolean isEmpty() {
            return head == null;
        }
    }

    private abstract class Context {
        Context next;
        abstract Event getNextEvent();
        abstract void skip();
    }

    private final class NoneContext extends Context {
        @Override
        public Event getNextEvent() {
            // Handle 1. {   2. [   3. value
            JsonToken token = tokenizer.nextToken();
            if (token == JsonToken.CURLYOPEN) {
                stack.push(currentContext);
                currentContext = new ObjectContext();
                return Event.START_OBJECT;
            } else if (token == JsonToken.SQUAREOPEN) {
                stack.push(currentContext);
                currentContext = new ArrayContext();
                return Event.START_ARRAY;
            } else if (token.isValue()) {
                return token.getEvent();
            }
            throw parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
        }

        @Override
        void skip() {
            // no-op
        }
    }

    private JsonParsingException parsingException(JsonToken token, String expectedTokens) {
        JsonLocation location = getLastCharLocation();
        return new JsonParsingException(
                JsonMessages.PARSER_INVALID_TOKEN(token, location, expectedTokens), location);
    }

    private final class ObjectContext extends Context {
        private boolean firstValue = true;

        /*
         * Some more things could be optimized. For example, instead
         * tokenizer.nextToken(), one could use tokenizer.matchColonToken() to
         * match ':'. That might optimize a bit, but will fragment nextToken().
         * I think the current one is more readable.
         *
         */
        @Override
        public Event getNextEvent() {
            // Handle 1. }   2. name:value   3. ,name:value
            JsonToken token = tokenizer.nextToken();
            if (token == JsonToken.EOF) {
                switch (currentEvent) {
                    case START_OBJECT:
                        throw parsingException(token, "[STRING, CURLYCLOSE]");
                    case KEY_NAME:
                        throw parsingException(token, "[COLON]");
                    default:
                        throw parsingException(token, "[COMMA, CURLYCLOSE]");
                }
            } else if (currentEvent == Event.KEY_NAME) {
                // Handle 1. :value
                if (token != JsonToken.COLON) {
                    throw parsingException(token, "[COLON]");
                }
                token = tokenizer.nextToken();
                if (token.isValue()) {
                    return token.getEvent();
                } else if (token == JsonToken.CURLYOPEN) {
                    stack.push(currentContext);
                    currentContext = new ObjectContext();
                    return Event.START_OBJECT;
                } else if (token == JsonToken.SQUAREOPEN) {
                    stack.push(currentContext);
                    currentContext = new ArrayContext();
                    return Event.START_ARRAY;
                }
                throw parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
            } else {
                // Handle 1. }   2. name   3. ,name
                if (token == JsonToken.CURLYCLOSE) {
                    currentContext = stack.pop();
                    return Event.END_OBJECT;
                }
                if (firstValue) {
                    firstValue = false;
                } else {
                    if (token != JsonToken.COMMA) {
                        throw parsingException(token, "[COMMA]");
                    }
                    token = tokenizer.nextToken();
                }
                if (token == JsonToken.STRING) {
                    return Event.KEY_NAME;
                }
                throw parsingException(token, "[STRING]");
            }
        }

        @Override
        void skip() {
            JsonToken token;
            int depth = 1;
            do {
                token = tokenizer.nextToken();
                switch (token) {
                    case CURLYCLOSE:
                        depth--;
                        break;
                    case CURLYOPEN:
                        depth++;
                        break;
                }
            } while (!(token == JsonToken.CURLYCLOSE && depth == 0));
        }

    }

    private final class ArrayContext extends Context {
        private boolean firstValue = true;

        // Handle 1. ]   2. value   3. ,value
        @Override
        public Event getNextEvent() {
            JsonToken token = tokenizer.nextToken();
            if (token == JsonToken.EOF) {
                switch (currentEvent) {
                    case START_ARRAY:
                        throw parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
                    default:
                        throw parsingException(token, "[COMMA, CURLYCLOSE]");
                }
            }
            if (token == JsonToken.SQUARECLOSE) {
                currentContext = stack.pop();
                return Event.END_ARRAY;
            }
            if (firstValue) {
                firstValue = false;
            } else {
                if (token != JsonToken.COMMA) {
                    throw parsingException(token, "[COMMA]");
                }
                token = tokenizer.nextToken();
            }
            if (token.isValue()) {
                return token.getEvent();
            } else if (token == JsonToken.CURLYOPEN) {
                stack.push(currentContext);
                currentContext = new ObjectContext();
                return Event.START_OBJECT;
            } else if (token == JsonToken.SQUAREOPEN) {
                stack.push(currentContext);
                currentContext = new ArrayContext();
                return Event.START_ARRAY;
            }
            throw parsingException(token, "[CURLYOPEN, SQUAREOPEN, STRING, NUMBER, TRUE, FALSE, NULL]");
        }

        @Override
        void skip() {
            JsonToken token;
            int depth = 1;
            do {
                token = tokenizer.nextToken();
                switch (token) {
                    case SQUARECLOSE:
                        depth--;
                        break;
                    case SQUAREOPEN:
                        depth++;
                        break;
                }
            } while (!(token == JsonToken.SQUARECLOSE && depth == 0));
        }
    }

}

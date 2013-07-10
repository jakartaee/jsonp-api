/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

import org.glassfish.json.JsonTokenizer.JsonToken;
import org.glassfish.json.api.BufferPool;

/**
 * JSON parser using a state machine.
 *
 * @author Jitendra Kotamraju
 */
public class JsonParserImpl implements JsonParser {

    private State currentState = State.START_DOCUMENT;
    private Context currentContext = new NoneContext(this);

    private Event currentEvent;

    private final Deque<Context> stack = new ArrayDeque<Context>();
    private final StateIterator stateIterator;
    private final JsonTokenizer tokenizer;

    public JsonParserImpl(Reader reader, BufferPool bufferPool) {
        tokenizer = new JsonTokenizer(reader, bufferPool);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, BufferPool bufferPool) {
        UnicodeDetectingInputStream uin = new UnicodeDetectingInputStream(in);
        tokenizer = new JsonTokenizer(new InputStreamReader(uin, uin.getCharset()), bufferPool);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, Charset encoding, BufferPool bufferPool) {
        tokenizer = new JsonTokenizer(new InputStreamReader(in, encoding), bufferPool);
        stateIterator = new StateIterator();
    }

    public String getString() {
        if (currentEvent == Event.KEY_NAME || currentEvent == Event.VALUE_STRING
                || currentEvent == Event.VALUE_NUMBER) {
            return tokenizer.getValue();
        }
        throw new IllegalStateException("JsonParser#getString() is valid only "+
                "KEY_NAME, VALUE_STRING, VALUE_NUMBER parser states. "+
                "But current parser state is "+currentEvent);
    }

    @Override
    public boolean isIntegralNumber() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#isIntegralNumber() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return tokenizer.isIntegral();
    }

    @Override
    public int getInt() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return tokenizer.getInt();
    }

    @Override
    public long getLong() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return tokenizer.getBigDecimal().longValue();
    }

    @Override
    public BigDecimal getBigDecimal() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return tokenizer.getBigDecimal();
    }

    @Override
    public JsonLocation getLocation() {
        return tokenizer.getLocation();
    }

    public JsonLocation getLastCharLocation() {
        return tokenizer.getLastCharLocation();
    }

    public boolean hasNext() {
        return stateIterator.hasNext();
    }

    public Event next() {
        return stateIterator.next();
    }

    private class StateIterator implements  Iterator<JsonParser.Event> {

        private JsonToken nextToken() {
            try {
                return tokenizer.nextToken();
            } catch(IOException ioe) {
                throw new JsonException("I/O error while moving parser to next state", ioe);
            }
        }

        @Override
        public boolean hasNext() {
            if (stack.isEmpty() && (currentState == State.END_ARRAY || currentState == State.END_OBJECT)) {
                JsonToken token = nextToken();
                if (token != JsonToken.EOF) {
                    throw new JsonParsingException("Expected EOF, but got="+token,
                            getLastCharLocation());
                }
                return false;
            }
            return true;
        }

        @Override
        public JsonParser.Event next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (true) {
                JsonToken token = nextToken();
                currentState = currentContext.getTransition(currentState, token);

                switch (currentState) {
                    case START_DOCUMENT:
                        continue;
                    case START_OBJECT:
                        stack.push(currentContext);
                        currentContext = new ObjectContext(JsonParserImpl.this);
                        return currentEvent=JsonParser.Event.START_OBJECT;
                    case KEY:
                        return currentEvent=JsonParser.Event.KEY_NAME;
                    case COLON:
                        continue;
                    case STRING:
                        return currentEvent=JsonParser.Event.VALUE_STRING;
                    case NUMBER:
                        return currentEvent=JsonParser.Event.VALUE_NUMBER;
                    case TRUE:
                        return currentEvent=JsonParser.Event.VALUE_TRUE;
                    case FALSE:
                        return currentEvent=JsonParser.Event.VALUE_FALSE;
                    case NULL:
                        return currentEvent=JsonParser.Event.VALUE_NULL;
                    case COMMA:
                        continue;
                    case END_OBJECT:
                        currentContext = stack.pop();
                        return currentEvent=JsonParser.Event.END_OBJECT;
                    case START_ARRAY:
                        stack.push(currentContext);
                        currentContext = new ArrayContext(JsonParserImpl.this);
                        return currentEvent=JsonParser.Event.START_ARRAY;
                    case END_ARRAY:
                        currentContext = stack.pop();
                        return currentEvent=JsonParser.Event.END_ARRAY;
                    case END_DOCUMENT:
                        break;
                }
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void close() {
        try {
            tokenizer.close();
        } catch (IOException e) {
            throw new JsonException("I/O error while closing JSON tokenizer", e);
        }
    }


    private static abstract class Context {
        final JsonParserImpl parser;

        Context(JsonParserImpl parser) {
            this.parser = parser;
        }

        abstract State[][] getTransitions();

        abstract EnumMap<State, Set<JsonToken>> getExpectedTokens();

        State getTransition(State current, JsonToken token) {
            State state = getTransitions()[current.ordinal()][token.ordinal()];
            if (state == State.INVALID) {
                throw new JsonParsingException(
                        "Invalid token "+token+" at "+parser.getLastCharLocation()+", "+
                        "Expected Tokens "+ getExpectedTokens().get(current), parser.getLastCharLocation());
            }
            return state;
        }

        // Using 2-dimensional array (may be better than
        // EnumMap<State, EnumMap<JsonToken, State>> )
        static void transition(State[][] TRANSITIONS, EnumMap<State,
                Set<JsonToken>> EXPECTED_TOKENS,
                State current, JsonToken token, State next) {
            TRANSITIONS[current.ordinal()][token.ordinal()] = next;
            Set<JsonToken> allowed =  EXPECTED_TOKENS.get(current);
            if (allowed == null) {
                allowed = EnumSet.noneOf(JsonToken.class);
                EXPECTED_TOKENS.put(current, allowed);
            }
            allowed.add(token);
        }
    }

    private static class NoneContext extends Context {
        private static final State[][] TRANSITIONS =
                new State[State.values().length][JsonToken.values().length];
        private static final EnumMap<State, Set<JsonToken>> EXPECTED_TOKENS =
                new EnumMap<State, Set<JsonToken>>(State.class);

        static {
            for(int i=0; i < TRANSITIONS.length; i++) {
                for(int j=0; j < TRANSITIONS[i].length; j++) {
                    TRANSITIONS[i][j] = State.INVALID;
                }
            }
            transition(State.START_DOCUMENT, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.START_DOCUMENT, JsonToken.SQUAREOPEN, State.START_ARRAY);
        }

        NoneContext(JsonParserImpl parser) {
            super(parser);
        }

        @Override
        State[][] getTransitions() {
            return TRANSITIONS;
        }

        @Override
        EnumMap<State, Set<JsonToken>> getExpectedTokens() {
            return EXPECTED_TOKENS;
        }

        static void transition(State current, JsonToken token, State next) {
            transition(TRANSITIONS, EXPECTED_TOKENS, current, token, next);
        }
    }

    private static class ObjectContext extends Context {
        private static final State[][] TRANSITIONS =
                new State[State.values().length][JsonToken.values().length];
        private static final EnumMap<State, Set<JsonToken>> EXPECTED_TOKENS =
                new EnumMap<State, Set<JsonToken>>(State.class);

        static {
            for(int i=0; i < TRANSITIONS.length; i++) {
                for(int j=0; j < TRANSITIONS[i].length; j++) {
                    TRANSITIONS[i][j] = State.INVALID;
                }
            }
            transition(State.START_OBJECT, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.START_OBJECT, JsonToken.STRING, State.KEY);

            transition(State.KEY, JsonToken.COLON, State.COLON);

            transition(State.COLON, JsonToken.STRING, State.STRING);
            transition(State.COLON, JsonToken.NUMBER, State.NUMBER);
            transition(State.COLON, JsonToken.TRUE, State.TRUE);
            transition(State.COLON, JsonToken.FALSE, State.FALSE);
            transition(State.COLON, JsonToken.NULL, State.NULL);
            transition(State.COLON, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.COLON, JsonToken.SQUAREOPEN, State.START_ARRAY);

            transition(State.STRING, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.STRING, JsonToken.COMMA, State.COMMA);

            transition(State.NUMBER, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.NUMBER, JsonToken.COMMA, State.COMMA);

            transition(State.TRUE, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.TRUE, JsonToken.COMMA, State.COMMA);

            transition(State.FALSE, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.FALSE, JsonToken.COMMA, State.COMMA);

            transition(State.NULL, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.NULL, JsonToken.COMMA, State.COMMA);

            transition(State.COMMA, JsonToken.STRING, State.KEY);

            transition(State.END_OBJECT, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.END_OBJECT, JsonToken.COMMA, State.COMMA);
            transition(State.END_OBJECT, JsonToken.EOF, State.END_DOCUMENT);

            transition(State.END_ARRAY, JsonToken.COMMA, State.COMMA);
            transition(State.END_ARRAY, JsonToken.CURLYCLOSE, State.END_OBJECT);
        }

        ObjectContext(JsonParserImpl parser) {
            super(parser);
        }

        static void transition(State current, JsonToken token, State next) {
            transition(TRANSITIONS, EXPECTED_TOKENS, current, token, next);
        }

        @Override
        State[][] getTransitions() {
            return TRANSITIONS;
        }

        @Override
        EnumMap<State, Set<JsonToken>> getExpectedTokens() {
            return EXPECTED_TOKENS;
        }
    }

    private static class ArrayContext extends Context {
        private static final State[][] TRANSITIONS =
                new State[State.values().length][JsonToken.values().length];
        private static final EnumMap<State, Set<JsonToken>> EXPECTED_TOKENS =
                new EnumMap<State, Set<JsonToken>>(State.class);

        static {
            for(int i=0; i < TRANSITIONS.length; i++) {
                for(int j=0; j < TRANSITIONS[i].length; j++) {
                    TRANSITIONS[i][j] = State.INVALID;
                }
            }
            transition(State.START_ARRAY, JsonToken.STRING, State.STRING);
            transition(State.START_ARRAY, JsonToken.NUMBER, State.NUMBER);
            transition(State.START_ARRAY, JsonToken.TRUE, State.TRUE);
            transition(State.START_ARRAY, JsonToken.FALSE, State.FALSE);
            transition(State.START_ARRAY, JsonToken.NULL, State.NULL);
            transition(State.START_ARRAY, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.START_ARRAY, JsonToken.SQUAREOPEN, State.START_ARRAY);
            transition(State.START_ARRAY, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.STRING, JsonToken.COMMA, State.COMMA);
            transition(State.STRING, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.NUMBER, JsonToken.COMMA, State.COMMA);
            transition(State.NUMBER, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.TRUE, JsonToken.COMMA, State.COMMA);
            transition(State.TRUE, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.FALSE, JsonToken.COMMA, State.COMMA);
            transition(State.FALSE, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.NULL, JsonToken.COMMA, State.COMMA);
            transition(State.NULL, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.COMMA, JsonToken.STRING, State.STRING);
            transition(State.COMMA, JsonToken.NUMBER, State.NUMBER);
            transition(State.COMMA, JsonToken.TRUE, State.TRUE);
            transition(State.COMMA, JsonToken.FALSE, State.FALSE);
            transition(State.COMMA, JsonToken.NULL, State.NULL);
            transition(State.COMMA, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.COMMA, JsonToken.SQUAREOPEN, State.START_ARRAY);

            transition(State.END_ARRAY, JsonToken.SQUARECLOSE, State.END_ARRAY);
            transition(State.END_ARRAY, JsonToken.COMMA, State.COMMA);
            transition(State.END_ARRAY, JsonToken.EOF, State.END_DOCUMENT);

            transition(State.END_OBJECT, JsonToken.COMMA, State.COMMA);
            transition(State.END_OBJECT, JsonToken.SQUARECLOSE, State.END_ARRAY);
        }

        ArrayContext(JsonParserImpl parser) {
            super(parser);
        }

        static void transition(State current, JsonToken token, State next) {
            transition(TRANSITIONS, EXPECTED_TOKENS, current, token, next);
        }

        @Override
        State[][] getTransitions() {
            return TRANSITIONS;
        }

        @Override
        EnumMap<State, Set<JsonToken>> getExpectedTokens() {
            return EXPECTED_TOKENS;
        }
    }


    private enum State {
        INVALID,

        START_DOCUMENT,

        START_OBJECT,
        START_ARRAY,
        KEY,
        COLON,
        STRING,
        NUMBER,
        TRUE,
        FALSE,
        NULL,
        COMMA,
        END_ARRAY,
        END_OBJECT,

        END_DOCUMENT
    }
    
}

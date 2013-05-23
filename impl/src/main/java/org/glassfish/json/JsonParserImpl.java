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

    public JsonParserImpl(Reader reader) {
        tokenizer = new JsonTokenizer(reader);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in) {
        UnicodeDetectingInputStream uin = new UnicodeDetectingInputStream(in);
        tokenizer = new JsonTokenizer(new InputStreamReader(uin, uin.getCharset()));
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, Charset encoding) {
        tokenizer = new JsonTokenizer(new InputStreamReader(in, encoding));
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
                    case OBJECT_STRING:
                        return currentEvent=JsonParser.Event.VALUE_STRING;
                    case OBJECT_NUMBER:
                        return currentEvent=JsonParser.Event.VALUE_NUMBER;
                    case OBJECT_TRUE:
                        return currentEvent=JsonParser.Event.VALUE_TRUE;
                    case OBJECT_FALSE:
                        return currentEvent=JsonParser.Event.VALUE_FALSE;
                    case OBJECT_NULL:
                        return currentEvent=JsonParser.Event.VALUE_NULL;
                    case OBJECT_COMMA:
                        continue;
                    case END_OBJECT:
                        currentContext = stack.pop();
                        return currentEvent=JsonParser.Event.END_OBJECT;
                    case START_ARRAY:
                        stack.push(currentContext);
                        currentContext = new ArrayContext(JsonParserImpl.this);
                        return currentEvent=JsonParser.Event.START_ARRAY;
                    case ARRAY_STRING:
                        return currentEvent=JsonParser.Event.VALUE_STRING;
                    case ARRAY_NUMBER:
                        return currentEvent=JsonParser.Event.VALUE_NUMBER;
                    case ARRAY_TRUE:
                        return currentEvent=JsonParser.Event.VALUE_TRUE;
                    case ARRAY_FALSE:
                        return currentEvent=JsonParser.Event.VALUE_FALSE;
                    case ARRAY_NULL:
                        return currentEvent=JsonParser.Event.VALUE_NULL;
                    case ARRAY_COMMA:
                        continue;
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

            transition(State.COLON, JsonToken.STRING, State.OBJECT_STRING);
            transition(State.COLON, JsonToken.NUMBER, State.OBJECT_NUMBER);
            transition(State.COLON, JsonToken.TRUE, State.OBJECT_TRUE);
            transition(State.COLON, JsonToken.FALSE, State.OBJECT_FALSE);
            transition(State.COLON, JsonToken.NULL, State.OBJECT_NULL);
            transition(State.COLON, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.COLON, JsonToken.SQUAREOPEN, State.START_ARRAY);

            transition(State.OBJECT_STRING, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.OBJECT_STRING, JsonToken.COMMA, State.OBJECT_COMMA);

            transition(State.OBJECT_NUMBER, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.OBJECT_NUMBER, JsonToken.COMMA, State.OBJECT_COMMA);

            transition(State.OBJECT_TRUE, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.OBJECT_TRUE, JsonToken.COMMA, State.OBJECT_COMMA);

            transition(State.OBJECT_FALSE, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.OBJECT_FALSE, JsonToken.COMMA, State.OBJECT_COMMA);

            transition(State.OBJECT_NULL, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.OBJECT_NULL, JsonToken.COMMA, State.OBJECT_COMMA);

            transition(State.OBJECT_COMMA, JsonToken.STRING, State.KEY);

            transition(State.END_OBJECT, JsonToken.CURLYCLOSE, State.END_OBJECT);
            transition(State.END_OBJECT, JsonToken.COMMA, State.OBJECT_COMMA);
            transition(State.END_OBJECT, JsonToken.EOF, State.END_DOCUMENT);

            transition(State.END_ARRAY, JsonToken.COMMA, State.OBJECT_COMMA);
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
            transition(State.START_ARRAY, JsonToken.STRING, State.ARRAY_STRING);
            transition(State.START_ARRAY, JsonToken.NUMBER, State.ARRAY_NUMBER);
            transition(State.START_ARRAY, JsonToken.TRUE, State.ARRAY_TRUE);
            transition(State.START_ARRAY, JsonToken.FALSE, State.ARRAY_FALSE);
            transition(State.START_ARRAY, JsonToken.NULL, State.ARRAY_NULL);
            transition(State.START_ARRAY, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.START_ARRAY, JsonToken.SQUAREOPEN, State.START_ARRAY);
            transition(State.START_ARRAY, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_STRING, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.ARRAY_STRING, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_NUMBER, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.ARRAY_NUMBER, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_TRUE, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.ARRAY_TRUE, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_FALSE, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.ARRAY_FALSE, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_NULL, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.ARRAY_NULL, JsonToken.SQUARECLOSE, State.END_ARRAY);

            transition(State.ARRAY_COMMA, JsonToken.STRING, State.ARRAY_STRING);
            transition(State.ARRAY_COMMA, JsonToken.NUMBER, State.ARRAY_NUMBER);
            transition(State.ARRAY_COMMA, JsonToken.TRUE, State.ARRAY_TRUE);
            transition(State.ARRAY_COMMA, JsonToken.FALSE, State.ARRAY_FALSE);
            transition(State.ARRAY_COMMA, JsonToken.NULL, State.ARRAY_NULL);
            transition(State.ARRAY_COMMA, JsonToken.CURLYOPEN, State.START_OBJECT);
            transition(State.ARRAY_COMMA, JsonToken.SQUAREOPEN, State.START_ARRAY);

            transition(State.END_ARRAY, JsonToken.SQUARECLOSE, State.END_ARRAY);
            transition(State.END_ARRAY, JsonToken.COMMA, State.ARRAY_COMMA);
            transition(State.END_ARRAY, JsonToken.EOF, State.END_DOCUMENT);

            transition(State.END_OBJECT, JsonToken.COMMA, State.ARRAY_COMMA);
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
        KEY,
        COLON,
        OBJECT_STRING,
        OBJECT_NUMBER,
        OBJECT_TRUE,
        OBJECT_FALSE,
        OBJECT_NULL,
        OBJECT_COMMA,
        END_OBJECT,

        START_ARRAY,
        ARRAY_STRING,
        ARRAY_NUMBER,
        ARRAY_TRUE,
        ARRAY_FALSE,
        ARRAY_NULL,
        ARRAY_COMMA,
        END_ARRAY,

        END_DOCUMENT
    }
    
}
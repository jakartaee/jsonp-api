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
import javax.json.stream.JsonParser;
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
    private static final BigDecimal INT_MIN_VALUE = new BigDecimal(Integer.MIN_VALUE);
    private static final BigDecimal INT_MAX_VALUE = new BigDecimal(Integer.MAX_VALUE);
    private static final BigDecimal LONG_MIN_VALUE = new BigDecimal(Long.MIN_VALUE);
    private static final BigDecimal LONG_MAX_VALUE = new BigDecimal(Long.MAX_VALUE);

    private State currentState = State.START_DOCUMENT;
    private State enclosingState = State.START_DOCUMENT;

    private Event currentEvent;

    private final Deque<State> stack = new ArrayDeque<State>();
    private final StateIterator stateIterator;
    private final JsonTokenizer tokenizer;
    private int depth = 0;

    public JsonParserImpl(Reader reader) {
        tokenizer = new JsonTokenizer(reader);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(Reader reader, JsonConfiguration config) {
        tokenizer = new JsonTokenizer(reader);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in) {
        tokenizer = new JsonTokenizer(in);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, Charset encoding) {
        tokenizer = new JsonTokenizer(new InputStreamReader(in, encoding));
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, JsonConfiguration config) {
        tokenizer = new JsonTokenizer(in);
        stateIterator = new StateIterator();
    }

    public JsonParserImpl(InputStream in, Charset encoding, JsonConfiguration config) {
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

    public JsonNumber.JsonNumberType getNumberType() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                "VALUE_NUMBER parser state. "+
                "But current parser state is "+currentEvent);
        }
        BigDecimal bigDecimal = new BigDecimal(tokenizer.getValue());
        if (bigDecimal.scale() != 0)  {
            return JsonNumber.JsonNumberType.BIG_DECIMAL;
        } else {
            if (bigDecimal.compareTo(INT_MIN_VALUE) >= 0 && bigDecimal.compareTo(INT_MAX_VALUE) <= 0) {
                return JsonNumber.JsonNumberType.INT;
            } else if (bigDecimal.compareTo(LONG_MIN_VALUE) >= 0 && bigDecimal.compareTo(LONG_MAX_VALUE) <= 0) {
                return JsonNumber.JsonNumberType.LONG;
            } else {
                return JsonNumber.JsonNumberType.BIG_DECIMAL;
            }
        }
    }

    @Override
    public int getIntValue() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return new BigDecimal(tokenizer.getValue()).intValue();
    }

    @Override
    public long getLongValue() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return new BigDecimal(tokenizer.getValue()).longValue();
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        if (currentEvent != Event.VALUE_NUMBER) {
            throw new IllegalStateException("JsonParser#getNumberType() is valid only "+
                    "VALUE_NUMBER parser state. "+
                    "But current parser state is "+currentEvent);
        }
        return new BigDecimal(tokenizer.getValue());
    }

    public Iterator<JsonParser.Event> iterator() {
        return stateIterator;
    }

    private class StateIterator implements  Iterator<JsonParser.Event> {
        JsonToken token;

        @Override
        public boolean hasNext() {
            if (token == JsonToken.EOF) {
                return false;
            }
            return !(depth == 0 && (currentState == State.END_OBJECT || currentState == State.END_ARRAY));
        }

        @Override
        public JsonParser.Event next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (true) {
                try {
                    token = tokenizer.nextToken();
                    State nextState = currentState.getTransition(token, enclosingState);
                    if (nextState == null) {
                        throw new JsonException("Expecting Tokens="+currentState.transitions.keySet()+"Got ="+token);
                    }
                    if (nextState == State.START_OBJECT || nextState == State.START_ARRAY) {
                        stack.addFirst(currentState);
                    }
                    currentState = nextState;
                } catch(IOException ioe) {
                    throw new JsonException(ioe);
                }

                switch (currentState) {
                    case START_DOCUMENT:
                        continue;
                    case START_OBJECT:
                        depth++;
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
                        depth--;
                        enclosingState = stack.removeFirst();
                        return currentEvent=JsonParser.Event.END_OBJECT;
                    case START_ARRAY:
                        depth++;
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
                        depth--;
                        enclosingState = stack.removeFirst();
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
            throw new JsonException(e);
        }
    }

    private enum State {
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
        END_OBJECT {
            @Override
            State getTransition(JsonToken token, State enclosingState) {
                if (token == JsonToken.COMMA) {
                    return enclosingState == State.COLON ? State.OBJECT_COMMA : State.ARRAY_COMMA;
                } else {
                    return transitions.get(token);
                }
            }
        },

        START_ARRAY,
        ARRAY_STRING,
        ARRAY_NUMBER,
        ARRAY_TRUE,
        ARRAY_FALSE,
        ARRAY_NULL,
        ARRAY_COMMA,
        END_ARRAY {
            @Override
            State getTransition(JsonToken token, State enclosingState) {
                if (token == JsonToken.COMMA) {
                    return enclosingState == State.COLON ? State.OBJECT_COMMA : State.ARRAY_COMMA;
                } else {
                    return transitions.get(token);
                }
            }            
        },

        END_DOCUMENT;
        
        static {
            START_DOCUMENT.transition(JsonToken.CURLYOPEN, START_OBJECT);
            START_DOCUMENT.transition(JsonToken.SQUAREOPEN, START_ARRAY);
            
            START_OBJECT.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            START_OBJECT.transition(JsonToken.STRING, KEY);
            
            KEY.transition(JsonToken.COLON, COLON);
            
            COLON.transition(JsonToken.STRING, OBJECT_STRING);
            COLON.transition(JsonToken.NUMBER, OBJECT_NUMBER);
            COLON.transition(JsonToken.TRUE, OBJECT_TRUE);
            COLON.transition(JsonToken.FALSE, OBJECT_FALSE);
            COLON.transition(JsonToken.NULL, OBJECT_NULL);
            COLON.transition(JsonToken.CURLYOPEN, START_OBJECT);
            COLON.transition(JsonToken.SQUAREOPEN, START_ARRAY);
            
            OBJECT_STRING.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            OBJECT_STRING.transition(JsonToken.COMMA, OBJECT_COMMA);

            OBJECT_NUMBER.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            OBJECT_NUMBER.transition(JsonToken.COMMA, OBJECT_COMMA);

            OBJECT_TRUE.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            OBJECT_TRUE.transition(JsonToken.COMMA, OBJECT_COMMA);

            OBJECT_FALSE.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            OBJECT_FALSE.transition(JsonToken.COMMA, OBJECT_COMMA);

            OBJECT_NULL.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            OBJECT_NULL.transition(JsonToken.COMMA, OBJECT_COMMA);
            
            OBJECT_COMMA.transition(JsonToken.STRING, KEY);
            
            END_OBJECT.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            END_OBJECT.transition(JsonToken.SQUARECLOSE, END_ARRAY);
            // Need an enclosing scope to determine the next state
            // END_OBJECT.transition(JsonToken.COMMA, OBJECT_COMMA or ARRAY_COMMA );
            END_OBJECT.transition(JsonToken.EOF, END_DOCUMENT);
            
            START_ARRAY.transition(JsonToken.STRING, ARRAY_STRING);
            START_ARRAY.transition(JsonToken.NUMBER, ARRAY_NUMBER);
            START_ARRAY.transition(JsonToken.TRUE, ARRAY_TRUE);
            START_ARRAY.transition(JsonToken.FALSE, ARRAY_FALSE);
            START_ARRAY.transition(JsonToken.NULL, ARRAY_NULL);
            START_ARRAY.transition(JsonToken.CURLYOPEN, START_OBJECT);
            START_ARRAY.transition(JsonToken.SQUAREOPEN, START_ARRAY);
            START_ARRAY.transition(JsonToken.SQUARECLOSE, END_ARRAY);
            
            ARRAY_STRING.transition(JsonToken.COMMA, ARRAY_COMMA);
            ARRAY_STRING.transition(JsonToken.SQUARECLOSE, END_ARRAY);

            ARRAY_NUMBER.transition(JsonToken.COMMA, ARRAY_COMMA);
            ARRAY_NUMBER.transition(JsonToken.SQUARECLOSE, END_ARRAY);

            ARRAY_TRUE.transition(JsonToken.COMMA, ARRAY_COMMA);
            ARRAY_TRUE.transition(JsonToken.SQUARECLOSE, END_ARRAY);

            ARRAY_FALSE.transition(JsonToken.COMMA, ARRAY_COMMA);
            ARRAY_FALSE.transition(JsonToken.SQUARECLOSE, END_ARRAY);

            ARRAY_NULL.transition(JsonToken.COMMA, ARRAY_COMMA);
            ARRAY_NULL.transition(JsonToken.SQUARECLOSE, END_ARRAY);

            ARRAY_COMMA.transition(JsonToken.STRING, ARRAY_STRING);
            ARRAY_COMMA.transition(JsonToken.NUMBER, ARRAY_NUMBER);
            ARRAY_COMMA.transition(JsonToken.TRUE, ARRAY_TRUE);
            ARRAY_COMMA.transition(JsonToken.FALSE, ARRAY_FALSE);
            ARRAY_COMMA.transition(JsonToken.NULL, ARRAY_NULL);
            ARRAY_COMMA.transition(JsonToken.CURLYOPEN, START_OBJECT);
            ARRAY_COMMA.transition(JsonToken.SQUAREOPEN, START_ARRAY);

            END_ARRAY.transition(JsonToken.CURLYCLOSE, END_OBJECT);
            END_ARRAY.transition(JsonToken.SQUARECLOSE, END_ARRAY);
            // Need an enclosing scope to determine the next state
            //END_ARRAY.transition(JsonToken.COMMA, OBJECT_COMMA or ARRAY_COMMA);
            END_ARRAY.transition(JsonToken.EOF, END_DOCUMENT);
        }
        
        final EnumMap<JsonToken, State> transitions
                = new EnumMap<JsonToken, State>(JsonToken.class);
        
        private void transition(JsonToken token, State state) {
            transitions.put(token, state);
        }

        State getTransition(JsonToken token, State enclosingState) {
            return transitions.get(token);
        }
    } 
    
    void parse() {
        JsonToken token;

        while(true) {
            try {
                token = tokenizer.nextToken();
                System.out.println("Token="+token);
                if (token == JsonToken.STRING) {
                    System.out.println("value=" + tokenizer.getValue());
                }
                if (token == JsonToken.EOF) {
                    break;
                }
            } catch(IOException ioe) {
                throw new JsonException(ioe);
            }
            
            State enclosingState = State.START_DOCUMENT;
            if (currentState == State.END_OBJECT || currentState == State.END_ARRAY) {
                enclosingState = stack.removeFirst();
            }
            
            State nextState = currentState.getTransition(token, enclosingState);
            
            if (nextState == State.START_OBJECT || nextState == State.START_ARRAY) {
                stack.addFirst(currentState);
            }
            currentState = nextState;

            System.out.println("Current State="+currentState);

        }
    } 
    
}
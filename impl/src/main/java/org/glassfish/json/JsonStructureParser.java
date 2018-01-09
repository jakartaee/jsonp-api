/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.*;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * {@link JsonParser} implementation on top of JsonArray/JsonObject
 *
 * @author Jitendra Kotamraju
 */
class JsonStructureParser implements JsonParser {

    private Scope current;
    private Event state;
    private final Deque<Scope> scopeStack = new ArrayDeque<>();

    JsonStructureParser(JsonArray array) {
        current = new ArrayScope(array);
    }

    JsonStructureParser(JsonObject object) {
        current = new ObjectScope(object);
    }

    @Override
    public String getString() {
        switch (state) {
            case KEY_NAME:
                return ((ObjectScope)current).key;
            case VALUE_STRING:
                return ((JsonString)current.getJsonValue()).getString();
            case VALUE_NUMBER:
                return ((JsonNumber)current.getJsonValue()).toString();
            default:
                throw new IllegalStateException(JsonMessages.PARSER_GETSTRING_ERR(state));
        }
    }

    @Override
    public boolean isIntegralNumber() {
        if (state == Event.VALUE_NUMBER) {
            return ((JsonNumber)current.getJsonValue()).isIntegral();
        }
        throw new IllegalStateException(JsonMessages.PARSER_ISINTEGRALNUMBER_ERR(state));
    }

    @Override
    public int getInt() {
        if (state == Event.VALUE_NUMBER) {
            return ((JsonNumber)current.getJsonValue()).intValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETINT_ERR(state));
    }

    @Override
    public long getLong() {
        if (state == Event.VALUE_NUMBER) {
            return ((JsonNumber)current.getJsonValue()).longValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETLONG_ERR(state));
    }

    @Override
    public BigDecimal getBigDecimal() {
        if (state == Event.VALUE_NUMBER) {
            return ((JsonNumber)current.getJsonValue()).bigDecimalValue();
        }
        throw new IllegalStateException(JsonMessages.PARSER_GETBIGDECIMAL_ERR(state));
    }

    @Override
    public JsonLocation getLocation() {
        return JsonLocationImpl.UNKNOWN;
    }

    @Override
    public boolean hasNext() {
        return !((state == Event.END_OBJECT || state == Event.END_ARRAY) && scopeStack.isEmpty());
    }

    @Override
    public Event next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        transition();
        return state;
    }

    private void transition() {
        if (state == null) {
            state = current instanceof ArrayScope ? Event.START_ARRAY : Event.START_OBJECT;
        } else {
            if (state == Event.END_OBJECT || state == Event.END_ARRAY) {
                current = scopeStack.pop();
            }
            if (current instanceof ArrayScope) {
                if (current.hasNext()) {
                    current.next();
                    state = getState(current.getJsonValue());
                    if (state == Event.START_ARRAY || state == Event.START_OBJECT) {
                        scopeStack.push(current);
                        current = Scope.createScope(current.getJsonValue());
                    }
                } else {
                    state = Event.END_ARRAY;
                }
            } else {
                // ObjectScope
                if (state == Event.KEY_NAME) {
                    state = getState(current.getJsonValue());
                    if (state == Event.START_ARRAY || state == Event.START_OBJECT) {
                        scopeStack.push(current);
                        current = Scope.createScope(current.getJsonValue());
                    }
                } else {
                    if (current.hasNext()) {
                        current.next();
                        state = Event.KEY_NAME;
                    } else {
                        state = Event.END_OBJECT;
                    }
                }
            }
        }
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public void skipObject() {
        if (current instanceof ObjectScope) {
            int depth = 1;
            do {
                if (state == Event.KEY_NAME) {
                    state = getState(current.getJsonValue());
                    switch (state) {
                        case START_OBJECT:
                            depth++;
                            break;
                        case END_OBJECT:
                            depth--;
                            break;
                        default:
                            //no-op
                    }
                } else {
                    if (current.hasNext()) {
                        current.next();
                        state = Event.KEY_NAME;
                    } else {
                        state = Event.END_OBJECT;
                        depth--;
                    }
                }
            } while (state != Event.END_OBJECT && depth > 0);
        }
    }

    @Override
    public void skipArray() {
        if (current instanceof ArrayScope) {
            int depth = 1;
            do {
                if (current.hasNext()) {
                    current.next();
                    state = getState(current.getJsonValue());
                    switch (state) {
                        case START_ARRAY:
                            depth++;
                            break;
                        case END_ARRAY:
                            depth--;
                            break;
                        default:
                            //no-op
                    }
                } else {
                    state = Event.END_ARRAY;
                    depth--;
                }
            } while (!(state == Event.END_ARRAY && depth == 0));
        }
    }

    private static Event getState(JsonValue value) {
        switch (value.getValueType()) {
            case ARRAY:
                return Event.START_ARRAY;
            case OBJECT:
                return Event.START_OBJECT;
            case STRING:
                return Event.VALUE_STRING;
            case NUMBER:
                return Event.VALUE_NUMBER;
            case TRUE:
                return Event.VALUE_TRUE;
            case FALSE:
                return Event.VALUE_FALSE;
            case NULL:
                return Event.VALUE_NULL;
            default:
                throw new JsonException(JsonMessages.PARSER_STATE_ERR(value.getValueType()));
        }
    }

    private static abstract class Scope implements Iterator {
        abstract JsonValue getJsonValue();

        static Scope createScope(JsonValue value) {
            if (value instanceof JsonArray) {
                return new ArrayScope((JsonArray)value);
            } else if (value instanceof JsonObject) {
                return new ObjectScope((JsonObject)value);
            }
            throw new JsonException(JsonMessages.PARSER_SCOPE_ERR(value));
        }
    }

    private static class ArrayScope extends Scope {
        private final Iterator<JsonValue> it;
        private JsonValue value;

        ArrayScope(JsonArray array) {
            this.it = array.iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public JsonValue next() {
            value = it.next();
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        JsonValue getJsonValue() {
            return value;
        }

    }

    private static class ObjectScope extends Scope {
        private final Iterator<Map.Entry<String, JsonValue>> it;
        private JsonValue value;
        private String key;

        ObjectScope(JsonObject object) {
            this.it = object.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Map.Entry<String, JsonValue> next() {
            Map.Entry<String, JsonValue> next = it.next();
            this.key = next.getKey();
            this.value = next.getValue();
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        JsonValue getJsonValue() {
            return value;
        }

    }

}

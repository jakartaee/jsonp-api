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
import java.util.*;

/**
 * JSON Tokenizer
 *
 * @author Jitendra Kotamraju
 */
class JsonStructureParser implements JsonParser {

    private Scope current;
    private Event state;
    private final Deque<Scope> scopeStack = new ArrayDeque<Scope>();

    JsonStructureParser(JsonArray array) {
        current = new ArrayScope(array);
    }

    JsonStructureParser(JsonObject object) {
        current = new ObjectScope(object);
    }

    @Override
    public String getString() {
        if (current instanceof ArrayScope) {
            return ((JsonString)(((ArrayScope)current).value)).getValue();
        } else {
            if (state == Event.KEY_NAME)
                return ((ObjectScope)current).key;
            else
                return ((JsonString)(((ObjectScope)current).value)).getValue();
        }
    }

    @Override
    public JsonNumber.JsonNumberType getNumberType() {
        return getNumber().getNumberType();
    }

    @Override
    public JsonNumber getNumber() {
        JsonValue value = (current instanceof ArrayScope)
            ? ((ArrayScope)current).value
            : ((ObjectScope)current).value;
        return (JsonNumber)value;
    }

    @Override
    public <T extends JsonValue> T getJsonValue(Class<T> clazz) {
        return null;
    }

    @Override
    public Iterator<Event> iterator() {
        return new Iterator<Event>() {

            @Override
            public boolean hasNext() {
                if ((state == Event.END_OBJECT || state == Event.END_ARRAY) && scopeStack.isEmpty()) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public Event next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (state == null) {
                    state = current instanceof ArrayScope ? Event.START_ARRAY : Event.START_OBJECT;
                } else {
                    transition();
                }
                return state;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private void transition() {
                if (state == Event.END_OBJECT || state == Event.END_ARRAY) {
                    current = scopeStack.pop();
                }

                if (current instanceof ArrayScope) {
                    switch (state) {
                        case START_ARRAY:
                        case VALUE_STRING:
                        case VALUE_NUMBER:
                        case VALUE_TRUE:
                        case VALUE_FALSE:
                        case VALUE_NULL:
                        case END_ARRAY:
                        case END_OBJECT:
                            if (((ArrayScope)current).hasNext()) {
                                ((ArrayScope)current).next();
                                state = getState(((ArrayScope)current).value);
                                if (state == Event.START_ARRAY) {
                                    scopeStack.push(current);
                                    current = new ArrayScope((JsonArray)((ArrayScope)current).value);
                                } else if (state == Event.START_OBJECT) {
                                    scopeStack.push(current);
                                    current = new ObjectScope((JsonObject)((ArrayScope)current).value);
                                }
                            } else {
                                state = Event.END_ARRAY;
                            }
                            break;
                    }
                } else {
                    switch (state) {
                        case END_OBJECT:
                        case END_ARRAY:
                        case START_OBJECT:
                            if (((ObjectScope)current).hasNext()) {
                                ((ObjectScope)current).next();
                                state = Event.KEY_NAME;
                            } else {
                                state = Event.END_OBJECT;
                            }
                            break;
                        case KEY_NAME:
                            state = getState(((ObjectScope)current).value);
                            if (state == Event.START_ARRAY) {
                                scopeStack.push(current);
                                current = new ArrayScope((JsonArray)((ObjectScope)current).value);
                            } else if (state == Event.START_OBJECT) {
                                scopeStack.push(current);
                                current = new ObjectScope((JsonObject)((ObjectScope)current).value);
                            }
                            break;
                        case VALUE_STRING:
                        case VALUE_NUMBER:
                        case VALUE_TRUE:
                        case VALUE_FALSE:
                        case VALUE_NULL:
                            if (((ObjectScope)current).hasNext()) {
                                ((ObjectScope)current).next();
                                state = Event.KEY_NAME;
                            } else {
                                state = Event.END_OBJECT;
                            }
                            break;
                    }
                }
            }



            private Event getState(JsonValue value) {
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
                        throw new JsonException("Unknown value type="+value.getValueType());
                }
            }

        };
    }

    @Override
    public void close() {
        // no-op
    }

    static class Scope {

    }

    static class ArrayScope extends Scope implements Iterator<JsonValue> {
        private final Iterator<JsonValue> it;
        JsonValue value;

        ArrayScope(JsonArray array) {
            this.it = array.getValues().iterator();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public JsonValue next() {
            return value=it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static class ObjectScope extends Scope implements Iterator<Map.Entry<String, JsonValue>> {
        private final Iterator<Map.Entry<String, JsonValue>> it;
        JsonValue value;
        String key;

        ObjectScope(JsonObject object) {
            this.it = object.getValues().entrySet().iterator();
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
    }

}
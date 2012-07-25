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
 * JSONParser implementation on top of JsonArray/JsonObject
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
            return ((JsonString)current.getJsonValue()).getValue();
        } else {
            if (state == Event.KEY_NAME)
                return ((ObjectScope)current).key;
            else
                return ((JsonString)current.getJsonValue()).getValue();
        }
    }

    @Override
    public JsonNumber.JsonNumberType getNumberType() {
        return getNumber().getNumberType();
    }

    @Override
    public JsonNumber getNumber() {
        return (JsonNumber)current.getJsonValue();
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
                return !((state == Event.END_OBJECT || state == Event.END_ARRAY) && scopeStack.isEmpty());
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
                    if (current.hasNext()) {
                        current.next();
                        state = getState(current.getJsonValue());
                        if (state == Event.START_ARRAY) {
                            scopeStack.push(current);
                            current = new ArrayScope((JsonArray)current.getJsonValue());
                        } else if (state == Event.START_OBJECT) {
                            scopeStack.push(current);
                            current = new ObjectScope((JsonObject)current.getJsonValue());
                        }
                    } else {
                        state = Event.END_ARRAY;
                    }
                } else {
                    // ObjectScope
                    if (state == Event.KEY_NAME) {
                        state = getState(current.getJsonValue());
                        if (state == Event.START_ARRAY) {
                            scopeStack.push(current);
                            current = new ArrayScope((JsonArray)current.getJsonValue());
                        } else if (state == Event.START_OBJECT) {
                            scopeStack.push(current);
                            current = new ObjectScope((JsonObject)current.getJsonValue());
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

        };
    }

    @Override
    public void close() {
        // no-op
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
                throw new JsonException("Unknown value type="+value.getValueType());
        }
    }

    private static interface Scope extends Iterator {
        JsonValue getJsonValue();
    }

    private static class ArrayScope implements Scope {
        private final Iterator<JsonValue> it;
        private JsonValue value;

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

        @Override
        public JsonValue getJsonValue() {
            return value;
        }
    }

    private static class ObjectScope implements Scope {
        private final Iterator<Map.Entry<String, JsonValue>> it;
        private JsonValue value;
        private String key;

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

        @Override
        public JsonValue getJsonValue() {
            return value;
        }
    }

}
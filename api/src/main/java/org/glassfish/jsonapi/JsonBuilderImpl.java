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

package org.glassfish.jsonapi;

import javax.json.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
public class JsonBuilderImpl {
    
    public JsonObjectBuilder<JsonBuilder.JsonBuildable<JsonObject>> beginObject() {
        final JsonObjectImpl objectImpl = new JsonObjectImpl();
        JsonBuilder.JsonBuildable<JsonObject> enclosing = new JsonBuilder.JsonBuildable<JsonObject>() {
            @Override
            public JsonObject build() {
                return objectImpl;
            }
        };
        return new JsonObjectBuilderImpl<JsonBuilder.JsonBuildable<JsonObject>>(enclosing, objectImpl.valueMap);
    }

    public JsonArrayBuilder<JsonBuilder.JsonBuildable<JsonArray>> beginArray() {
        final JsonArrayImpl arrayImpl = new JsonArrayImpl();                
        JsonBuilder.JsonBuildable<JsonArray> enclosing = new JsonBuilder.JsonBuildable<JsonArray>() {
            @Override
            public JsonArray build() {
                return arrayImpl;
            }
        };
        return new JsonArrayBuilderImpl<JsonBuilder.JsonBuildable<JsonArray>>(enclosing, arrayImpl.valueList);
    }

    private static class JsonObjectBuilderImpl<T> implements JsonObjectBuilder<T> {
        private final T enclosing;
        private final Map<String, JsonValue> valueMap;
        private boolean done;
        
        JsonObjectBuilderImpl(T enclosing, Map<String, JsonValue> valueMap) {
            this.enclosing = enclosing;
            this.valueMap = valueMap;
        }

        @Override
        public T end() {
            if (done) {
                throw new IllegalStateException("end() is already invoked.");
            }
            done = true;
            return enclosing;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, JsonValue value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, value);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, String value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonStringImpl(value));
            return this;  
        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigInteger value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;

        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigDecimal value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, int value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, long value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, double value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, boolean value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueMap.put(name, value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> addNull(String name) {
            if (done) {
                throw new IllegalStateException("addNull() cannot be called after end()");
            }
            valueMap.put(name, JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonObjectBuilder<T>> startObject(String name) {
            if (done) {
                throw new IllegalStateException("startObject() cannot be called after end()");
            }
            JsonObjectImpl child = new JsonObjectImpl();
            valueMap.put(name, child);
            return new JsonObjectBuilderImpl<JsonObjectBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonObjectBuilder<T>> startArray(String name) {
            if (done) {
                throw new IllegalStateException("startArray() cannot be called after end()");
            }
            JsonArrayImpl child = new JsonArrayImpl();
            valueMap.put(name, child);
            return new JsonArrayBuilderImpl<JsonObjectBuilder<T>>(this, child.valueList);
        }
    }

    static class JsonArrayBuilderImpl<T> implements JsonArrayBuilder<T> {
        private final T enclosing;
        private final List<JsonValue> valueList;
        private boolean done;
        
        JsonArrayBuilderImpl(T enclosing, List<JsonValue> valueList) {
            this.enclosing = enclosing;
            this.valueList = valueList;
        }

        @Override
        public T end() {
            if (done) {
                throw new IllegalStateException("end() is already invoked.");
            }
            done = true;
            return enclosing;
        }

        @Override
        public JsonArrayBuilder<T> add(JsonValue value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(value);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(String value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonStringImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigDecimal value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigInteger value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(int value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(long value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(double value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(boolean value) {
            if (done) {
                throw new IllegalStateException("add() cannot be called after end()");
            }
            valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> addNull() {
            if (done) {
                throw new IllegalStateException("addNull() cannot be called after end()");
            }
            valueList.add(JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonArrayBuilder<T>> startObject() {
            if (done) {
                throw new IllegalStateException("startObject() cannot be called after end()");
            }
            JsonObjectImpl child = new JsonObjectImpl();
            valueList.add(child);
            return new JsonObjectBuilderImpl<JsonArrayBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonArrayBuilder<T>> startArray() {
            if (done) {
                throw new IllegalStateException("startArray() cannot be called after end()");
            }
            JsonArrayImpl child = new JsonArrayImpl();
            valueList.add(child);
            return new JsonArrayBuilderImpl<JsonArrayBuilder<T>>(this, child.valueList);
        }
    }
}
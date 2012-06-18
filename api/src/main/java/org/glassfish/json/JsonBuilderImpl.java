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
        private Map<String, JsonValue> valueMap;
        
        JsonObjectBuilderImpl(T enclosing, Map<String, JsonValue> valueMap) {
            this.enclosing = enclosing;
            this.valueMap = valueMap;
        }

        @Override
        public T endObject() {
            return enclosing;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, JsonValue value) {
            valueMap.put(name, value);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, String value) {
            valueMap.put(name, new JsonStringImpl(value));
            return this;  
        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigInteger value) {
            valueMap.put(name, new JsonNumberImpl(value));
            return this;

        }

        @Override
        public JsonObjectBuilder<T> add(String name, BigDecimal value) {
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, int value) {
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, long value) {
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, double value) {
            valueMap.put(name, new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonObjectBuilder<T> add(String name, boolean value) {
            valueMap.put(name, value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonObjectBuilder<T> addNull(String name) {
            valueMap.put(name, JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonObjectBuilder<T>> beginObject(String name) {
            JsonObjectImpl child = new JsonObjectImpl();
            valueMap.put(name, child);
            return new JsonObjectBuilderImpl<JsonObjectBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonObjectBuilder<T>> beginArray(String name) {
            JsonArrayImpl child = new JsonArrayImpl();
            valueMap.put(name, child);
            return new JsonArrayBuilderImpl<JsonObjectBuilder<T>>(this, child.valueList);
        }
    }

    static class JsonArrayBuilderImpl<T> implements JsonArrayBuilder<T> {
        private final T enclosing;
        private final List<JsonValue> valueList;
        
        JsonArrayBuilderImpl(T enclosing, List<JsonValue> valueList) {
            this.enclosing = enclosing;
            this.valueList = valueList;
        }

        @Override
        public T endArray() {
            return enclosing;
        }

        @Override
        public JsonArrayBuilder<T> add(JsonValue value) {
            valueList.add(value);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(String value) {
            valueList.add(new JsonStringImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigDecimal value) {
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(BigInteger value) {
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(int value) {
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(long value) {
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(double value) {
            valueList.add(new JsonNumberImpl(value));
            return this;
        }

        @Override
        public JsonArrayBuilder<T> add(boolean value) {
            valueList.add(value ? JsonValue.TRUE : JsonValue.FALSE);
            return this;
        }

        @Override
        public JsonArrayBuilder<T> addNull() {
            valueList.add(JsonValue.NULL);
            return this;
        }

        @Override
        public JsonObjectBuilder<JsonArrayBuilder<T>> beginObject() {
            JsonObjectImpl child = new JsonObjectImpl();
            valueList.add(child);
            return new JsonObjectBuilderImpl<JsonArrayBuilder<T>>(this, child.valueMap);
        }

        @Override
        public JsonArrayBuilder<JsonArrayBuilder<T>> beginArray() {
            JsonArrayImpl child = new JsonArrayImpl();
            valueList.add(child);
            return new JsonArrayBuilderImpl<JsonArrayBuilder<T>>(this, child.valueList);
        }
    }
}
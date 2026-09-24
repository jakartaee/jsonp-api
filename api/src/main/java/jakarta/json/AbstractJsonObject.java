/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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
package jakarta.json;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Abstract JsonObject which delegates calls to an encapsulated
 * JsonObject delegate.<br><br>
 *
 * Extend this class when you want to implement your own JsonObject,
 * based on another JsonObject, without having to reimplement
 * all the methods yourself.<br><br>
 *
 * Example usage: Polymorphism.
 *
 * <pre>
 *     //Container which is also a JsonObject.
 *     public interface Container extends JsonObject {
 *         //...other methods of Container.
 *     }
 *
 *     public final class JsonContainer implements Container extends AbstractJsonObject {
 *
 *         public ContainerImpl(final JsonObject representation) {
 *             super(representation);
 *         }
 *     }
 * </pre>
 */
public abstract class AbstractJsonObject implements JsonObject {

    /**
     * JsonObject delegate. You may also think of it as the
     * "backbone" of your JsonObject implementation.
     */
    private final JsonObject delegate;

    /**
     * Constructor.
     * @param delegate Delegate JsonObject. The "backbone" of your
     *  implementation.
     */
    public AbstractJsonObject(final JsonObject delegate) {
        this.delegate = delegate;
    }

    @Override
    public JsonArray getJsonArray(String name) {
        return this.delegate.getJsonArray(name);
    }

    @Override
    public JsonObject getJsonObject(String name) {
        return this.delegate.getJsonObject(name);
    }

    @Override
    public JsonNumber getJsonNumber(String name) {
        return this.delegate.getJsonNumber(name);
    }

    @Override
    public JsonString getJsonString(String name) {
        return this.delegate.getJsonString(name);
    }

    @Override
    public String getString(String name) {
        return this.delegate.getString(name);
    }

    @Override
    public String getString(String name, String defaultValue) {
        return this.delegate.getString(name, defaultValue);
    }

    @Override
    public int getInt(String name) {
        return this.delegate.getInt(name);
    }

    @Override
    public int getInt(String name, int defaultValue) {
        return this.delegate.getInt(name, defaultValue);
    }

    @Override
    public boolean getBoolean(String name) {
        return this.delegate.getBoolean(name);
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        return this.delegate.getBoolean(name, defaultValue);
    }

    @Override
    public boolean isNull(String name) {
        return this.delegate.isNull(name);
    }

    @Override
    public JsonValue getValue(String jsonPointer) {
        return this.delegate.getValue(jsonPointer);
    }

    @Override
    public ValueType getValueType() {
        return this.delegate.getValueType();
    }

    @Override
    public JsonObject asJsonObject() {
        return this.delegate.asJsonObject();
    }

    @Override
    public JsonArray asJsonArray() {
        return this.delegate.asJsonArray();
    }

    @Override
    public String toString() {
        return this.delegate.toString();
    }

    @Override
    public int size() {
        return this.delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.delegate.containsValue(value);
    }

    @Override
    public JsonValue get(Object key) {
        return this.delegate.get(key);
    }

    @Override
    public JsonValue put(String key, JsonValue value) {
        return this.delegate.put(key, value);
    }

    @Override
    public JsonValue remove(Object key) {
        return this.delegate.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends JsonValue> m) {
        this.delegate.putAll(m);
    }

    @Override
    public void clear() {
        this.delegate.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.delegate.keySet();
    }

    @Override
    public Collection<JsonValue> values() {
        return this.delegate.values();
    }

    @Override
    public Set<Entry<String, JsonValue>> entrySet() {
        return this.delegate.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return this.delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return this.delegate.hashCode();
    }

    @Override
    public JsonValue getOrDefault(Object key, JsonValue defaultValue) {
        return this.delegate.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super JsonValue> action) {
        this.delegate.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super JsonValue, ? extends JsonValue> function) {
        this.delegate.replaceAll(function);
    }

    @Override
    public JsonValue putIfAbsent(String key, JsonValue value) {
        return this.delegate.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return this.delegate.remove(key, value);
    }

    @Override
    public boolean replace(String key, JsonValue oldValue, JsonValue newValue) {
        return this.delegate.replace(key, oldValue, newValue);
    }

    @Override
    public JsonValue replace(String key, JsonValue value) {
        return this.delegate.replace(key, value);
    }

    @Override
    public JsonValue computeIfAbsent(String key, Function<? super String, ? extends JsonValue> mappingFunction) {
        return this.delegate.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public JsonValue computeIfPresent(String key, BiFunction<? super String, ? super JsonValue, ? extends JsonValue> remappingFunction) {
        return this.delegate.computeIfPresent(key, remappingFunction);
    }

    @Override
    public JsonValue compute(String key, BiFunction<? super String, ? super JsonValue, ? extends JsonValue> remappingFunction) {
        return this.delegate.compute(key, remappingFunction);
    }

    @Override
    public JsonValue merge(String key, JsonValue value, BiFunction<? super JsonValue, ? super JsonValue, ? extends JsonValue> remappingFunction) {
        return this.delegate.merge(key, value, remappingFunction);
    }
}

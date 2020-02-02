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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Abstract JsonArray which delegates calls to an encapsulated
 * JsonArray delegate.<br><br>
 *
 * Extend this class when you want to implement your own JsonArray,
 * based on another JsonArray, without having to reimplement
 * all the methods yourself.<br><br>
 *
 * Example usage: Encapsulation of utility method.
 * <pre>
 *     //Instead of this utility method which pollutes the code and
 *     //will probably never be tested...
 *
 *     public static JsonArray buildArray(JsonValue... values) {
 *         List&lt;JsonValue&gt; values = Arrays.asList(values);
 *         JsonArray array = values.stream().collect(
 *             JsonCollectors.toJsonArray()
 *         );
 *     }
 *
 *     //There should be the following class. Logic becomes
 *     //decoupled, encapsulated and testable.
 *
 *     public final class CollectedJsonArray extends AbstractJsonArray {
 *
 *         public CollectedJsonArry(final JsonValue... values) {
 *             this(Arrays.asList(values));
 *         }
 *
 *         public CollectedJsonArray(final List&lt;JsonValue&gt; values) {
 *             super(
 *                 values.stream().collect(
 *                     JsonCollectors.toJsonArray()
 *                 )
 *             );
 *         }
 *     }
 * </pre>
 */
public abstract class AbstractJsonArray implements JsonArray {

    /**
     * JsonArray this.delegate. You may also think of it as the
     * "backbone" of your polymorphic JsonArray implementation.
     */
    private final JsonArray delegate;

    /**
     * Constructor.
     * @param delegate Delegate JsonArray. The "backbone" of your
     *  implementation.
     */
    public AbstractJsonArray(final JsonArray delegate) {
        this.delegate = delegate;
    }

    @Override
    public JsonObject getJsonObject(int index) {
        return this.delegate.getJsonObject(index);
    }

    @Override
    public JsonArray getJsonArray(int index) {
        return this.delegate.getJsonArray(index);
    }

    @Override
    public JsonNumber getJsonNumber(int index) {
        return this.delegate.getJsonNumber(index);
    }

    @Override
    public JsonString getJsonString(int index) {
        return this.delegate.getJsonString(index);
    }

    @Override
    public <T extends JsonValue> List<T> getValuesAs(Class<T> clazz) {
        return this.delegate.getValuesAs(clazz);
    }

    @Override
    public <T, K extends JsonValue> List<T> getValuesAs(Function<K, T> func) {
        return this.delegate.getValuesAs(func);
    }

    @Override
    public String getString(int index) {
        return this.delegate.getString(index);
    }

    @Override
    public String getString(int index, String defaultValue) {
        return this.delegate.getString(index, defaultValue);
    }

    @Override
    public int getInt(int index) {
        return this.delegate.getInt(index);
    }

    @Override
    public int getInt(int index, int defaultValue) {
        return this.delegate.getInt(index, defaultValue);
    }

    @Override
    public boolean getBoolean(int index) {
        return this.delegate.getBoolean(index);
    }

    @Override
    public boolean getBoolean(int index, boolean defaultValue) {
        return this.delegate.getBoolean(index, defaultValue);
    }

    @Override
    public boolean isNull(int index) {
        return this.delegate.isNull(index);
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
    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @Override
    public Iterator<JsonValue> iterator() {
        return this.delegate.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.delegate.toArray(a);
    }

    @Override
    public boolean add(JsonValue jsonValue) {
        return this.delegate.add(jsonValue);
    }

    @Override
    public boolean remove(Object o) {
        return this.delegate.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.delegate.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends JsonValue> c) {
        return this.delegate.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends JsonValue> c) {
        return this.delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.delegate.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<JsonValue> operator) {
        this.delegate.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super JsonValue> c) {
        this.delegate.sort(c);
    }

    @Override
    public void clear() {
        this.delegate.clear();
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
    public JsonValue get(int index) {
        return this.delegate.get(index);
    }

    @Override
    public JsonValue set(int index, JsonValue element) {
        return this.delegate.set(index, element);
    }

    @Override
    public void add(int index, JsonValue element) {
        this.delegate.add(index, element);
    }

    @Override
    public JsonValue remove(int index) {
        return this.delegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator<JsonValue> listIterator() {
        return this.delegate.listIterator();
    }

    @Override
    public ListIterator<JsonValue> listIterator(int index) {
        return this.delegate.listIterator(index);
    }

    @Override
    public List<JsonValue> subList(int fromIndex, int toIndex) {
        return this.delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<JsonValue> spliterator() {
        return this.delegate.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super JsonValue> filter) {
        return this.delegate.removeIf(filter);
    }

    @Override
    public Stream<JsonValue> stream() {
        return this.delegate.stream();
    }

    @Override
    public Stream<JsonValue> parallelStream() {
        return this.delegate.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super JsonValue> action) {
        this.delegate.forEach(action);
    }
}

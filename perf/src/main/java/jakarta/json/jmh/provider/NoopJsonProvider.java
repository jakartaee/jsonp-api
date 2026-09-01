/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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
package jakarta.json.jmh.provider;

import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * Minimal {@link JsonProvider} implementation used only for JMH benchmarks.
 *
 * <p>Methods required by the benchmarks in {@link FactoryTest} are fully
 * implemented with lightweight no-op objects so the benchmark measures
 * provider-lookup and dispatch overhead rather than real JSON construction.
 * Every other abstract method throws {@link UnsupportedOperationException}.
 *
 * <p>Registered via {@code META-INF/services/jakarta.json.spi.JsonProvider}
 * so that {@link jakarta.json.spi.JsonProvider#provider()} resolves to this
 * class through the ServiceLoader without requiring a real implementation on
 * the classpath.
 */
public class NoopJsonProvider extends JsonProvider {

    // -----------------------------------------------------------------------
    // Builders
    // -----------------------------------------------------------------------

    @Override
    public JsonObjectBuilder createObjectBuilder() {
        return NoopObjectBuilder.INSTANCE;
    }

    @Override
    public JsonArrayBuilder createArrayBuilder() {
        return NoopArrayBuilder.INSTANCE;
    }

    @Override
    public JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
        return NoopBuilderFactory.INSTANCE;
    }

    // -----------------------------------------------------------------------
    // createValue
    // -----------------------------------------------------------------------

    @Override
    public JsonString createValue(String value) {
        return NoopJsonString.INSTANCE;
    }

    @Override
    public JsonNumber createValue(int value) {
        return NoopJsonNumber.INSTANCE;
    }

    // -----------------------------------------------------------------------
    // Remaining abstract methods — not exercised by the benchmarks
    // -----------------------------------------------------------------------

    @Override
    public JsonParser createParser(Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonParser createParser(InputStream in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonParserFactory createParserFactory(Map<String, ?> config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonReader createReader(Reader reader) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonReader createReader(InputStream in) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonWriter createWriter(Writer writer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonWriter createWriter(OutputStream out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonWriterFactory createWriterFactory(Map<String, ?> config) {
        throw new UnsupportedOperationException();
    }

    @Override
    public jakarta.json.JsonReaderFactory createReaderFactory(Map<String, ?> config) {
        throw new UnsupportedOperationException();
    }

    // -----------------------------------------------------------------------
    // No-op inner types
    // -----------------------------------------------------------------------

    /** Singleton no-op {@link JsonObjectBuilder}. */
    private static final class NoopObjectBuilder implements JsonObjectBuilder {
        static final NoopObjectBuilder INSTANCE = new NoopObjectBuilder();

        @Override public JsonObjectBuilder add(String name, JsonValue value)   { return this; }
        @Override public JsonObjectBuilder add(String name, String value)      { return this; }
        @Override public JsonObjectBuilder add(String name, BigInteger value)  { return this; }
        @Override public JsonObjectBuilder add(String name, BigDecimal value)  { return this; }
        @Override public JsonObjectBuilder add(String name, int value)         { return this; }
        @Override public JsonObjectBuilder add(String name, long value)        { return this; }
        @Override public JsonObjectBuilder add(String name, double value)      { return this; }
        @Override public JsonObjectBuilder add(String name, boolean value)     { return this; }
        @Override public JsonObjectBuilder addNull(String name)                { return this; }
        @Override public JsonObjectBuilder add(String name, JsonObjectBuilder builder) { return this; }
        @Override public JsonObjectBuilder add(String name, JsonArrayBuilder builder)  { return this; }
        @Override public JsonObject build() { return JsonValue.EMPTY_JSON_OBJECT; }
    }

    /** Singleton no-op {@link JsonArrayBuilder}. */
    private static final class NoopArrayBuilder implements JsonArrayBuilder {
        static final NoopArrayBuilder INSTANCE = new NoopArrayBuilder();

        @Override public JsonArrayBuilder add(JsonValue value)        { return this; }
        @Override public JsonArrayBuilder add(String value)           { return this; }
        @Override public JsonArrayBuilder add(BigInteger value)       { return this; }
        @Override public JsonArrayBuilder add(BigDecimal value)       { return this; }
        @Override public JsonArrayBuilder add(int value)              { return this; }
        @Override public JsonArrayBuilder add(long value)             { return this; }
        @Override public JsonArrayBuilder add(double value)           { return this; }
        @Override public JsonArrayBuilder add(boolean value)          { return this; }
        @Override public JsonArrayBuilder addNull()                   { return this; }
        @Override public JsonArrayBuilder add(JsonObjectBuilder builder) { return this; }
        @Override public JsonArrayBuilder add(JsonArrayBuilder builder)  { return this; }
        @Override public JsonArray build() { return JsonValue.EMPTY_JSON_ARRAY; }
    }

    /** Singleton no-op {@link JsonBuilderFactory}. */
    private static final class NoopBuilderFactory implements JsonBuilderFactory {
        static final NoopBuilderFactory INSTANCE = new NoopBuilderFactory();

        @Override public JsonObjectBuilder createObjectBuilder() { return NoopObjectBuilder.INSTANCE; }
        @Override public JsonArrayBuilder  createArrayBuilder()  { return NoopArrayBuilder.INSTANCE; }
        @Override public Map<String, ?>    getConfigInUse()      { return Map.of(); }
    }

    /** Singleton no-op {@link JsonString}. */
    private static final class NoopJsonString implements JsonString {
        static final NoopJsonString INSTANCE = new NoopJsonString();

        @Override public String      getString()      { return ""; }
        @Override public CharSequence getChars()      { return ""; }
        @Override public ValueType   getValueType()   { return ValueType.STRING; }
    }

    /** Singleton no-op {@link JsonNumber}. */
    private static final class NoopJsonNumber implements JsonNumber {
        static final NoopJsonNumber INSTANCE = new NoopJsonNumber();

        @Override public boolean    isIntegral()           { return true; }
        @Override public int        intValue()             { return 0; }
        @Override public int        intValueExact()        { return 0; }
        @Override public long       longValue()            { return 0L; }
        @Override public long       longValueExact()       { return 0L; }
        @Override public BigInteger bigIntegerValue()      { return BigInteger.ZERO; }
        @Override public BigInteger bigIntegerValueExact() { return BigInteger.ZERO; }
        @Override public double     doubleValue()          { return 0.0; }
        @Override public BigDecimal bigDecimalValue()      { return BigDecimal.ZERO; }
        @Override public ValueType  getValueType()         { return ValueType.NUMBER; }
        @Override public String     toString()             { return "0"; }
    }
}

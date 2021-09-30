/*
 * Copyright (c) 2011, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.spi;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Service provider for JSON processing objects.
 *
 * <p>All the methods in this class are safe for use by multiple concurrent
 * threads.
 *
 * @see ServiceLoader
 */
public abstract class JsonProvider {

    /**
     * A constant representing the name of the default
     * {@code JsonProvider} implementation class.
     */
    private static final String DEFAULT_PROVIDER
            = "org.eclipse.jsonp.JsonProviderImpl";

    /**
     * Default constructor.
     */
    protected JsonProvider() {
    }

    /**
     * Creates a JSON provider object. The provider is loaded using the
     * {@link ServiceLoader#load(Class)} method. If there are no available
     * service providers, this method returns the default service provider.
     * Users are recommended to cache the result of this method.
     *
     * @see ServiceLoader
     * @return a JSON provider
     */
    public static JsonProvider provider() {
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        if (it.hasNext()) {
            return it.next();
        }
        try {
            Class<?> clazz = Class.forName(DEFAULT_PROVIDER);
            return (JsonProvider) clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException x) {
            throw new JsonException(
                    "Provider " + DEFAULT_PROVIDER + " not found", x);
        } catch (Exception x) {
            throw new JsonException(
                    "Provider " + DEFAULT_PROVIDER + " could not be instantiated: " + x,
                    x);
        }
    }

    /**
     * Creates a JSON parser from a character stream.
     *
     * @param reader i/o reader from which JSON is to be read
     * @return a JSON parser
     */
    public abstract JsonParser createParser(Reader reader);

    /**
     * Creates a JSON parser from the specified byte stream.
     * The character encoding of the stream is determined
     * as defined in <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159
     * </a>.
     *
     * @param in i/o stream from which JSON is to be read
     * @throws JsonException if encoding cannot be determined
     *         or i/o error (IOException would be cause of JsonException)
     * @return a JSON parser
     */
    public abstract JsonParser createParser(InputStream in);

    /**
     * Creates a parser factory for creating {@link JsonParser} instances.
     *
     * @return a JSON parser factory
     *
    public abstract JsonParserFactory createParserFactory();
     */

    /**
     * Creates a parser factory for creating {@link JsonParser} instances.
     * The factory is configured with the specified map of
     * provider specific configuration properties. Provider implementations
     * should ignore any unsupported configuration properties specified in
     * the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON parsers. The map may be empty or null
     * @return a JSON parser factory
     */
    public abstract JsonParserFactory createParserFactory(Map<String, ?> config);

    /**
     * Creates a JSON generator for writing JSON text to a character stream.
     *
     * @param writer a i/o writer to which JSON is written
     * @return a JSON generator
     */
    public abstract JsonGenerator createGenerator(Writer writer);

    /**
     * Creates a JSON generator for writing JSON text to a byte stream.
     *
     * @param out i/o stream to which JSON is written
     * @return a JSON generator
     */
    public abstract JsonGenerator createGenerator(OutputStream out);

    /**
     * Creates a generator factory for creating {@link JsonGenerator} instances.
     *
     * @return a JSON generator factory
     *
    public abstract JsonGeneratorFactory createGeneratorFactory();
     */

    /**
     * Creates a generator factory for creating {@link JsonGenerator} instances.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should
     * ignore any unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON generators. The map may be empty or null
     * @return a JSON generator factory
     */
    public abstract JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config);

    /**
     * Creates a JSON reader from a character stream.
     *
     * @param reader a reader from which JSON is to be read
     * @return a JSON reader
     */
    public abstract JsonReader createReader(Reader reader);

    /**
     * Creates a JSON reader from a byte stream. The character encoding of
     * the stream is determined as described in
     * <a href="http://tools.ietf.org/rfc/rfc7159.txt">RFC 7159</a>.
     *
     * @param in a byte stream from which JSON is to be read
     * @return a JSON reader
     */
    public abstract JsonReader createReader(InputStream in);

    /**
     * Creates a JSON writer to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified character stream.
     *
     * @param writer to which JSON object or array is written
     * @return a JSON writer
     */
    public abstract JsonWriter createWriter(Writer writer);

    /**
     * Creates a JSON writer to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     *
     * @param out to which JSON object or array is written
     * @return a JSON writer
     */
    public abstract JsonWriter createWriter(OutputStream out);

    /**
     * Creates a writer factory for creating {@link JsonWriter} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON writers. The map may be empty or null
     * @return a JSON writer factory
     */
    public abstract JsonWriterFactory createWriterFactory(Map<String,?> config);

    /**
     * Creates a reader factory for creating {@link JsonReader} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON readers. The map may be empty or null
     * @return a JSON reader factory
     */
    public abstract JsonReaderFactory createReaderFactory(Map<String,?> config);

    /**
     * Creates a JSON object builder.
     *
     * @return a JSON object builder
     */
    public abstract JsonObjectBuilder createObjectBuilder();

    /**
     * Creates a JSON object builder, initialized with the specified object.
     *
     * @param object the initial JSON object in the builder
     * @return a JSON object builder
     *
     * @since 1.1
     */
    public JsonObjectBuilder createObjectBuilder(JsonObject object) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON object builder, initialized with the data from specified {@code map}.
     * If the @{code map} contains {@link Optional}s then resulting JSON object builder
     * contains the key from the {@code map} only if the {@link Optional} is not empty.
     *
     * @param map the initial object in the builder
     * @return a JSON object builder
     * @exception IllegalArgumentException if the value from the {@code map} cannot be converted
     *            to the corresponding {@link JsonValue}
     *
     * @since 1.1
     */
    public JsonObjectBuilder createObjectBuilder(Map<String, ?> map) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON array builder.
     *
     * @return a JSON array builder
     */
    public abstract JsonArrayBuilder createArrayBuilder();

    /**
     * Creates a JSON array builder, initialized with the specified array.
     *
     * @param array the initial JSON array in the builder
     * @return a JSON array builder
     *
     * @since 1.1
     */
    public JsonArrayBuilder createArrayBuilder(JsonArray array) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates JSON Pointer (<a href="http://tools.ietf.org/html/rfc6901">RFC 6901</a>)
     * from given {@code jsonPointer} string.
     * <ul>
     *     <li>An empty {@code jsonPointer} string defines a reference to the target itself.</li>
     *     <li>If the {@code jsonPointer} string is non-empty, it must be a sequence of '{@code /}' prefixed tokens.</li>
     * </ul>
     *
     * @param jsonPointer the JSON Pointer string
     * @throws NullPointerException if {@code jsonPointer} is {@code null}
     * @throws JsonException if {@code jsonPointer} is not a valid JSON Pointer
     * @return a JSON Pointer
     *
     * @since 1.1
     */
    public JsonPointer createPointer(String jsonPointer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON Patch builder (<a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>).
     *
     * @return a JSON Patch builder
     *
     * @since 1.1
     */
    public JsonPatchBuilder createPatchBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON Patch builder
     * (<a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>),
     * initialized with the specified operations.
     *
     * @param array the initial patch operations
     * @return a JSON Patch builder
     *
     * @since 1.1
     */
    public JsonPatchBuilder createPatchBuilder(JsonArray array) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON Patch (<a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>)
     * from the specified operations.
     *
     * @param array patch operations
     * @return a JSON Patch
     *
     * @since 1.1
     */
    public JsonPatch createPatch(JsonArray array) {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates a JSON Patch (<a href="http://tools.ietf.org/html/rfc6902">RFC 6902</a>)
     * from the source and target {@code JsonStructure}.
     * The generated JSON Patch need not be unique.
     *
     * @param source the source
     * @param target the target, must be the same type as the source
     * @return a JSON Patch which when applied to the source, yields the target
     *
     * @since 1.1
     */
    public JsonPatch createDiff(JsonStructure source, JsonStructure target) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates JSON Merge Patch (<a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>)
     * from specified {@code JsonValue}.
     *
     * @param patch the patch
     * @return a JSON Merge Patch
     *
     * @since 1.1
     */
    public JsonMergePatch createMergePatch(JsonValue patch) {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates a JSON Merge Patch (<a href="http://tools.ietf.org/html/rfc7396">RFC 7396</a>)
     * from the source and target {@code JsonValue}s
     * which when applied to the {@code source}, yields the {@code target}.
     *
     * @param source the source
     * @param target the target
     * @return a JSON Merge Patch
     *
     * @since 1.1
     */
    public JsonMergePatch createMergeDiff(JsonValue source, JsonValue target) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JSON array builder, initialized with the content of specified {@code collection}.
     * If the @{code collection} contains {@link Optional}s then resulting JSON array builder
     * contains the value from the {@code collection} only if the {@link Optional} is not empty.
     *
     * @param collection the initial data for the builder
     * @return a JSON array builder
     * @exception IllegalArgumentException if the value from the {@code collection} cannot be converted
     *            to the corresponding {@link JsonValue}
     *
     * @since 1.1
     */
    public JsonArrayBuilder createArrayBuilder(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }


    /**
     * Creates a builder factory for creating {@link JsonArrayBuilder}
     * and {@link JsonObjectBuilder} objects.
     * The factory is configured with the specified map of provider specific
     * configuration properties. Provider implementations should ignore any
     * unsupported configuration properties specified in the map.
     *
     * @param config a map of provider specific properties to configure the
     *               JSON builders. The map may be empty or null
     * @return a JSON builder factory
     */
    public abstract JsonBuilderFactory createBuilderFactory(Map<String,?> config);

    /**
     * Creates a JsonString.
     *
     * @param value a JSON string
     * @return the JsonString for the string
     *
     * @since 1.1
     */
    public JsonString createValue(String value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * @param value a JSON number
     * @return the JsonNumber for the number
     *
     * @since 1.1
     */
    public JsonNumber createValue(int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * @param value a JSON number
     * @return the JsonNumber for the number
     *
     * @since 1.1
     */
    public JsonNumber createValue(long value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * @param value a JSON number
     * @return the JsonNumber for the number
     *
     * @since 1.1
     */
    public JsonNumber createValue(double value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * @param value a JSON number
     * @return the JsonNumber for the number
     *
     * @since 1.1
     */
    public JsonNumber createValue(BigDecimal value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * @param value a JSON number
     * @return the JsonNumber for the number
     *
     * @since 1.1
     */
    public JsonNumber createValue(BigInteger value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a JsonNumber.
     *
     * When it is not implemented it checks the type and delegates
     * to an existing method that already handles that type. It throws
     * UnsupportedOperationException in case the type is not known.
     *
     * @param number a JSON number
     * @return the JsonNumber for the number
     *
     * @since 2.1
     */
    public JsonNumber createValue(Number number) {
        if (number instanceof Integer) {
            return createValue(number.intValue());
        } else if (number instanceof Long) {
            return createValue(number.longValue());
        } else if (number instanceof Double) {
            return createValue(number.doubleValue());
        } else if (number instanceof BigInteger) {
            return createValue((BigInteger) number);
        } else if (number instanceof BigDecimal) {
            return createValue((BigDecimal) number);
        } else {
            throw new UnsupportedOperationException(number + " type is not known");
        }
    }
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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

package javax.json;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.*;

/**
 * JSON factory to create {@code JsonParser}, {@code JsonGenerator}
 * {@code JsonParserFactory} and {@code JsonGeneratorFactory} instances.
 *
 * <p> All of the methods in this class are safe for use by multiple concurrent
 * threads.</p>
 *
 * @author Jitendra Kotamraju
 */
public class Json {

    /**
     * Creates a JSON parser from the specified character stream
     *
     * @param reader i/o reader from which JSON is to be read
     */
    public static JsonParser createParser(Reader reader) {
        return JsonProvider.provider().createParser(reader);
    }

    /**
     * Creates a JSON parser from the specified character stream. The created
     * parser is configured with the specified configuration.
     *
     * @param reader i/o reader from which JSON is to be read
     * @param config configuration of the parser
     */
    public static JsonParser createParser(Reader reader, JsonConfiguration config) {
        return JsonProvider.provider().createParser(reader, config);
    }

    /**
     * Creates a JSON parser from the specified byte stream
     *
     * @param in i/o stream from which JSON is to be read
     */
    public static JsonParser createParser(InputStream in) {
        return JsonProvider.provider().createParser(in);
    }

    /**
     * Creates a JSON parser from the specified byte stream.
     *
     * @param in i/o stream from which JSON is to be read
     * @param encoding the character encoding of the stream
     */
    public static JsonParser createParser(InputStream in, String encoding) {
        return JsonProvider.provider().createParser(in, encoding);
    }

    /**
     * Creates a JSON parser from the specified byte stream. The created
     * parser is configured with the specified configuration.
     *
     * @param in i/o stream from which JSON is to be read
     * @param config configuration of the parser
     */
    public static JsonParser createParser(InputStream in, JsonConfiguration config) {
        return JsonProvider.provider().createParser(in, config);
    }

    /**
     * Creates a JSON parser from the specified byte stream. The created
     * parser is configured with the specified configuration.
     *
     * @param in i/o stream from which JSON is to be read
     * @param encoding the character encoding of the stream
     * @param config configuration of the parser
     */
    public static JsonParser createParser(InputStream in, String encoding, JsonConfiguration config) {
        return JsonProvider.provider().createParser(in, encoding, config);
    }

    /**
     * Creates a JSON parser from the specified JSON object.
     *
     * @param obj JSON object
     */
    public static JsonParser createParser(JsonObject obj) {
        return JsonProvider.provider().createParser(obj);
    }

    /**
     * Creates a JSON parser from the specified JSON object. The created
     * parser is configured with the specified configuration.
     *
     * @param obj JSON object
     * @param config configuration of the parser
     */
    public static JsonParser createParser(JsonObject obj, JsonConfiguration config) {
        return JsonProvider.provider().createParser(obj, config);
    }

    /**
     * Creates a JSON parser from the specified JSON array.
     *
     * @param array JSON array
     */
    public static JsonParser createParser(JsonArray array) {
        return JsonProvider.provider().createParser(array);
    }

    /**
     * Creates a JSON parser from the specified JSON array. The created
     * parser is configured with the specified configuration.
     *
     * @param array JSON array
     * @param config configuration of the parser
     */
    public static JsonParser createParser(JsonArray array, JsonConfiguration config) {
        return JsonProvider.provider().createParser(array, config);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified character stream.
     *
     * @param writer a i/o writer to which JSON is written
     */
    public static JsonGenerator createGenerator(Writer writer) {
        return JsonProvider.provider().createGenerator(writer);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified character stream. The created generator is configured
     * with the specified configuration.
     *
     * @param writer i/o writer to which JSON is written
     * @param config configuration of the generator
     */
    public static JsonGenerator createGenerator(Writer writer, JsonConfiguration config) {
        return JsonProvider.provider().createGenerator(writer, config);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified byte stream.
     *
     * @param out i/o stream to which JSON is written
     */
    public static JsonGenerator createGenerator(OutputStream out) {
        return JsonProvider.provider().createGenerator(out);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified byte stream. The created generator is configured
     * with the specified configuration.
     *
     * @param out i/o stream to which JSON is written
     * @param config configuration of the generator
     */
    public static JsonGenerator createGenerator(OutputStream out, JsonConfiguration config) {
        return JsonProvider.provider().createGenerator(out, config);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified byte stream.
     *
     * @param out i/o stream to which JSON is written
     * @param encoding the character encoding of the stream
     */
    public static JsonGenerator createGenerator(OutputStream out, String encoding) {
        return JsonProvider.provider().createGenerator(out, encoding);
    }

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified byte stream. The created generator is configured
     * with the specified configuration.
     *
     * @param out i/o stream to which JSON is written
     * @param encoding the character encoding of the stream
     * @param config configuration of the generator
     */
    public static JsonGenerator createGenerator(OutputStream out, String encoding, JsonConfiguration config) {
        return JsonProvider.provider().createGenerator(out, encoding, config);
    }

    public static JsonParserFactory createParserFactory() {
        return JsonProvider.provider().createParserFactory();
    }

    public static JsonParserFactory createParserFactory(JsonConfiguration config) {
        return JsonProvider.provider().createParserFactory(config);
    }

    public static JsonGeneratorFactory createGeneratorFactory() {
        return JsonProvider.provider().createGeneratorFactory();
    }

    public static JsonGeneratorFactory createGeneratorFactory(JsonConfiguration config) {
        return JsonProvider.provider().createGeneratorFactory(config);
    }

    private void testParser() {
        JsonParser parser = Json.createParser(new StringReader("[]"));
        parser.close();
    }

    private void testParserWithConfig() {
        JsonConfiguration config = new JsonConfiguration();
        JsonParser parser = Json.createParser(new StringReader("[]"), config);
        parser.close();
    }

    private void testGenerator() {
        JsonGenerator generator = Json.createGenerator(new StringWriter());
        generator.beginArray().endArray();
        generator.close();
    }

    private void testGeneratorWithConfig() {
        JsonConfiguration config = new JsonConfiguration().with(JsonFeature.PRETTY_PRINTING);
        JsonGenerator generator = Json.createGenerator(new StringWriter(), config);
        generator.beginArray().endArray();
        generator.close();
    }

    private void testParserFactory() {
        JsonParserFactory parserFactory = Json.createParserFactory();
        JsonParser parser1 = parserFactory.createParser(new StringReader("[]"));
        parser1.close();
        JsonParser parser2 = parserFactory.createParser(new StringReader("[]"));
        parser2.close();
    }

    private void testParserFactoryWithConfig() {
        JsonConfiguration config = new JsonConfiguration();
        JsonParserFactory parserFactory = Json.createParserFactory(config);
        JsonParser parser1 = parserFactory.createParser(new StringReader("[]"));
        parser1.close();
        JsonParser parser2 = parserFactory.createParser(new StringReader("[]"));
        parser2.close();
    }

    private void testGeneratorFactory() {
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory();

        JsonGenerator generator1 = generatorFactory.createGenerator(new StringWriter());
        generator1.beginArray().endArray();
        generator1.close();

        JsonGenerator generator2 = generatorFactory.createGenerator(new StringWriter());
        generator2.beginArray().endArray();
        generator2.close();
    }

    private void testGeneratorFactoryWithConfig() {
        JsonConfiguration config = new JsonConfiguration().with(JsonFeature.PRETTY_PRINTING);
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(config);

        JsonGenerator generator1 = generatorFactory.createGenerator(new StringWriter());
        generator1.beginArray().endArray();
        generator1.close();

        JsonGenerator generator2 = generatorFactory.createGenerator(new StringWriter());
        generator2.beginArray().endArray();
        generator2.close();
    }

}

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

package javax.json;

import javax.json.stream.JsonParser;
import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * A JSON reader that reads a JSON {@link JsonObject object} or
 * {@link JsonArray array} from an input source.
 *
 * <p><b>For example</b>, an empty JSON array can be created as follows:
 * <code>
 * <pre>
 * JsonReader jsonReader = new JsonReader(new StringReader("[]"));
 * JsonArray array = jsonReader.readArray();
 * jsonReader.close();
 * </pre>
 * </code>
 *
 * It uses {@link javax.json.stream.JsonParser} for parsing. The parser
 * is created using one of the {@link Json}'s {@code createParser} methods.
 *
 * @author Jitendra Kotamraju
 */
public class JsonReader implements /*Auto*/Closeable {

    private final JsonParser parser;
    private boolean readDone;

    /**
     * Creates a JSON reader from a character stream
     *
     * @param reader a reader from which JSON is to be read
     */
    public JsonReader(Reader reader) {
        parser = Json.createParser(reader);
    }

    /**
     * Creates a JSON reader from a character stream
     *
     * @param reader a character stream from which JSON is to be read
     * @param config configuration of the reader
     * @throws IllegalArgumentException if a feature in the configuration
     * is not known
     */
    public JsonReader(Reader reader, JsonConfiguration config) {
        parser = Json.createParser(reader, config);
    }

    /**
     * Creates a JSON reader from a byte stream. The character encoding of
     * the stream is determined as per the
     * <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC</a>.
     *
     * @param in a byte stream from which JSON is to be read
     */
    public JsonReader(InputStream in) {
        parser = Json.createParser(in);
    }

    /**
     * Creates a JSON reader from a byte stream. The bytes of the stream
     * are decoded to characters using the specified charset.
     *
     * @param in a byte stream from which JSON is to be read
     * @param charset a charset
     */
    public JsonReader(InputStream in, Charset charset) {
        parser = Json.createParser(in, charset);
    }

    /**
     * Creates a JSON reader from a byte stream. The bytes of the stream
     * are decoded to characters using the specified charset. The created
     * reader is configured with the specified configuration.
     *
     * @param in a byte stream from which JSON is to be read
     * @param charset a charset
     * @param config configuration of the reader
     * @throws IllegalArgumentException if a feature in the configuration
     * is not known
     */
    public JsonReader(InputStream in, Charset charset, JsonConfiguration config) {
        parser = Json.createParser(in, charset, config);
    }

    /**
     * Returns a JSON array or object that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json object or array
     * @throws JsonException if a JSON object or array cannot
     *     be created due to i/o error or incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonStructure read() {
        if (readDone) {
            throw new IllegalStateException("read/readObject/readArray/close method is already called.");
        }
        readDone = true;
        Iterator<JsonParser.Event> it = parser.iterator();
        if (it.hasNext()) {
            JsonParser.Event e = it.next();
            if (e == JsonParser.Event.START_ARRAY || e == JsonParser.Event.START_OBJECT) {
                return read(it, e);
            } else {
                throw new JsonException("Cannot read JSON, parsing error. Parsing Event="+e);
            }
        }
        throw new JsonException("Cannot read JSON, possibly empty stream");
    }

    /**
     * Returns a JSON object that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json object
     * @throws JsonException if a JSON object or array cannot
     *     be created due to i/o error or incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonObject readObject() {
        if (readDone) {
            throw new IllegalStateException("read/readObject/readArray/close method is already called.");
        }
        readDone = true;
        Iterator<JsonParser.Event> it = parser.iterator();
        if (it.hasNext()) {
            JsonParser.Event e = it.next();
            if (e == JsonParser.Event.START_OBJECT) {
                return (JsonObject)read(it, e);
            } else if (e == JsonParser.Event.START_ARRAY) {
                throw new JsonException("Cannot read JSON object, found JSON array");
            } else {
                throw new JsonException("Cannot read JSON object, parsing error. Parsing Event="+e);
            }
        }
        throw new JsonException("Cannot read JSON object, possibly empty stream");
    }

    /**
     * Returns a JSON array that is represented in
     * the input source. This method needs to be called
     * only once for a reader instance.
     *
     * @return a Json array
     * @throws JsonException if a JSON object or array cannot
     *     be created due to i/o error or incorrect representation
     * @throws IllegalStateException if this method, readObject, readArray or
     *     close method is already called
     */
    public JsonArray readArray() {
        if (readDone) {
            throw new IllegalStateException("read/readObject/readArray/close method is already called.");
        }
        readDone = true;
        Iterator<JsonParser.Event> it = parser.iterator();
        if (it.hasNext()) {
            JsonParser.Event e = it.next();
            if (e == JsonParser.Event.START_ARRAY) {
                return (JsonArray)read(it, e);
            } else if (e == JsonParser.Event.START_OBJECT) {
                throw new JsonException("Cannot read JSON array, found JSON object");
            } else {
                throw new JsonException("Cannot read JSON array, parsing error. Parsing Event="+e);
            }
        }
        throw new JsonException("Cannot read JSON array, possibly empty stream");
    }

    /**
     * Closes this reader and frees any resources associated with the
     * reader. This closes the underlying input source.
     */
    @Override
    public void close() {
        readDone = true;
        parser.close();
    }

    private JsonStructure read(Iterator<JsonParser.Event> it, JsonParser.Event firstEvent) {
        Object builder = new JsonBuilder();
        String key = null;
        JsonParser.Event e = firstEvent;
        do {
            switch (e) {
                case START_ARRAY:
                    if (builder instanceof JsonBuilder) {
                        builder =  ((JsonBuilder)builder).beginArray();
                    } else if (builder instanceof JsonArrayBuilder) {
                        builder = ((JsonArrayBuilder)builder).beginArray();
                    } else {
                        builder = ((JsonObjectBuilder)builder).beginArray(key);
                    }
                    break;
                case START_OBJECT:
                    if (builder instanceof JsonBuilder) {
                        builder =  ((JsonBuilder)builder).beginObject();
                    } else if (builder instanceof JsonArrayBuilder) {
                        builder = ((JsonArrayBuilder)builder).beginObject();
                    } else {
                        builder = ((JsonObjectBuilder)builder).beginObject(key);
                    }
                    break;
                case KEY_NAME:
                    key = parser.getString();
                    break;
                case VALUE_STRING:
                    String  string = parser.getString();
                    if (builder instanceof JsonArrayBuilder) {
                        ((JsonArrayBuilder)builder).add(string);
                    } else {
                        ((JsonObjectBuilder)builder).add(key, string);
                    }
                    break;
                case VALUE_NUMBER:
                    BigDecimal bd = new BigDecimal(parser.getString());
                    if (builder instanceof JsonArrayBuilder) {
                        ((JsonArrayBuilder)builder).add(bd);
                    } else {
                        ((JsonObjectBuilder)builder).add(key, bd);
                    }
                    break;
                case VALUE_TRUE:
                    if (builder instanceof JsonArrayBuilder) {
                        ((JsonArrayBuilder)builder).add(true);
                    } else {
                        ((JsonObjectBuilder)builder).add(key, true);
                    }
                    break;
                case VALUE_FALSE:
                    if (builder instanceof JsonArrayBuilder) {
                        ((JsonArrayBuilder)builder).add(false);
                    } else {
                        ((JsonObjectBuilder)builder).add(key, false);
                    }
                    break;
                case VALUE_NULL:
                    if (builder instanceof JsonArrayBuilder) {
                        ((JsonArrayBuilder)builder).addNull();
                    } else {
                        ((JsonObjectBuilder)builder).addNull(key);
                    }
                    break;
                case END_OBJECT:
                    builder = ((JsonObjectBuilder)builder).endObject();
                    break;
                case END_ARRAY:
                    builder = ((JsonArrayBuilder)builder).endArray();
                    break;
            }
            if (it.hasNext()) {
                e = it .next();
            } else {
                break;
            }
        } while(true);

        return ((JsonBuilder.JsonBuildable)builder).build();
    }

}
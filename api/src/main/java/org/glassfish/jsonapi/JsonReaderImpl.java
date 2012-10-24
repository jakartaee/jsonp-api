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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Iterator;
import javax.json.*;
import javax.json.stream.JsonParser;

/**
 * @author Jitendra Kotamraju
 */
public class JsonReaderImpl {
    private final JsonParser parser;
    private boolean readDone;

    public JsonReaderImpl(Reader reader) {
        parser = Json.createParser(reader);
    }

    public JsonReaderImpl(Reader reader, JsonConfiguration config) {
        parser = Json.createParser(reader, config);
    }

    public JsonReaderImpl(InputStream in) {
        parser = Json.createParser(in);
    }

    public JsonReaderImpl(InputStream in, Charset encoding) {
        parser = Json.createParser(in, encoding);
    }

    public JsonReaderImpl(InputStream in, Charset encoding, JsonConfiguration config) {
        parser = Json.createParser(in, encoding, config);
    }

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

    public void close() {
        readDone = true;
        parser.close();
    }
}
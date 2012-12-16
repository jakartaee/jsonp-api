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

import javax.json.stream.JsonGenerator;
import java.io.Closeable;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * A JSON writer that writes a JSON {@link JsonObject object} or
 * {@link JsonArray array} structure to an output source.
 *
 * <p>
 * <b>For example</b>, an empty JSON object can be written as follows:
 * <pre>
 * <code>
 * JsonWriter jsonWriter = new JsonWriter(...);
 * jsonWriter.writeObject(new JsonObjectBuilder().build());
 * jsonWriter.close();
 * </code>
 * </pre>
 *
 * It uses {@link javax.json.stream.JsonGenerator} internally for writing.
 * The generator is created using one of the {@link Json}'s
 * {@code createGenerator} methods
 *
 * @author Jitendra Kotamraju
 */
public class JsonWriter implements /*Auto*/Closeable {

    private final JsonGenerator generator;
    private boolean writeDone;

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified character stream.
     *
     * @param writer to which JSON object or array is written
     */
    public JsonWriter(Writer writer) {
        generator = Json.createGenerator(writer);
    }

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified character stream.  The created
     * writer is configured with the specified configuration.
     *
     * @param writer to which JSON object or array is written
     * @param config configuration of the writer
     * @throws IllegalArgumentException if a feature in the configuration
     * is not known
     */
    public JsonWriter(Writer writer, JsonConfiguration config) {
        generator = Json.createGeneratorFactory(config).createGenerator(writer);
    }

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     *
     * @param out to which JSON object or array is written
     */
    public JsonWriter(OutputStream out) {
        generator = Json.createGenerator(out);
    }

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using UTF-8 encoding.
     * The created writer is configured with the specified configuration.
     *
     * @param out to which JSON object or array is written
     * @param config configuration of the writer
     * @throws IllegalArgumentException if a feature in the configuration
     * is not known
     */
    public JsonWriter(OutputStream out, JsonConfiguration config) {
        generator = Json.createGeneratorFactory(config).createGenerator(out);
    }

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using the specified charset.
     *
     * @param out to which JSON object or array is written
     * @param charset a charset
     */
    public JsonWriter(OutputStream out, Charset charset) {
        generator = Json.createGeneratorFactory().createGenerator(out, charset);
    }

    /**
     * Creates a JSON writer which can be used to write a
     * JSON {@link JsonObject object} or {@link JsonArray array}
     * structure to the specified byte stream. Characters written to
     * the stream are encoded into bytes using the specified charset.
     * The created writer is configured with the specified configuration.
     *
     * @param out to which JSON object or array is written
     * @param charset a charset
     * @param config configuration of the writer
     * @throws IllegalArgumentException if a feature in the configuration
     * is not known
     */
    public JsonWriter(OutputStream out, Charset charset, JsonConfiguration config) {
        generator = Json.createGeneratorFactory(config).createGenerator(out, charset);
    }

    /**
     * Writes the specified JSON {@link JsonArray array} to the output
     * source. This method needs to be called only once for a writer instance.
     *
     * @param array JSON array that is to be written to the output source
     * @throws JsonException if the specified JSON object cannot be
     *     written due to i/o error (IOException would be cause of
     *     JsonException)
     * @throws IllegalStateException if this method, writeObject, write or close
     *     method is already called
     */
    public void writeArray(JsonArray array) {
        if (writeDone) {
            throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");
        }
        writeDone = true;
        generator.writeStartArray();
        for(JsonValue value : array) {
            generator.write(value);
        }
        generator.writeEnd().close();
    }

    /**
     * Writes the specified JSON {@link JsonObject object} to the output
     * source. This method needs to be called only once for a writer instance.
     *
     * @param object JSON object that is to be written to the output source
     * @throws JsonException if the specified JSON object cannot be
     *     written due to i/o error (IOException would be cause of JsonException)
     * @throws IllegalStateException if this method, writeArray, write or close
     *     method is already called
     */
    public void writeObject(JsonObject object) {
        if (writeDone) {
            throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");
        }
        writeDone = true;
        generator.writeStartObject();
        for(Map.Entry<String, JsonValue> e : object.entrySet()) {
            generator.write(e.getKey(), e.getValue());
        }
        generator.writeEnd().close();
    }

    /**
     * Writes the specified JSON {@link JsonObject object} or
     * {@link JsonArray array} to the output source. This method needs
     * to be called only once for a writer instance.
     *
     * @param value JSON array or object that is to be written to the output
     *              source
     * @throws JsonException if the specified JSON object cannot be
     *     written due to i/o error (IOException would be cause of
     *     JsonException)
     * @throws IllegalStateException if this method, writeObject, writeArray
     *     or close method is already called
     */
    public void write(JsonStructure value) {
        if (value instanceof JsonArray) {
            writeArray((JsonArray)value);
        } else {
            writeObject((JsonObject)value);
        }
    }

    /**
     * Closes this JSON writer and frees any resources associated with the
     * writer. This closes the underlying output source.
     *
     * @throws JsonException if an i/o error occurs (IOException would be
     * cause of JsonException)
     */
    @Override
    public void close() {
        writeDone = true;
        generator.close();
    }

}
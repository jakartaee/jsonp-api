/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
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

import org.glassfish.json.JsonWriterImpl;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A JSON writer.
 *
 * <p>
 * This writer writes a JSON object or array to the stream. For example:
 * <code>
 * <pre>
 * An empty JSON object can be written as follows:
 *
 * JsonWriter jsonWriter = new JsonWriter(...);
 * jsonWriter.writeObject(new JsonBuilder().beginObject().endObject().build());
 * jsonWriter.close();
 * </pre>
 * </code>
 * @author Jitendra Kotamraju
 */
public class JsonWriter implements /*Auto*/Closeable {

    private final JsonWriterImpl impl;

    /**
     * Creates a JSON writer which can be used to write a JSON
     * object or array to the specified i/o writer.
     *
     * @param writer to which JSON object or array is written
     */
    public JsonWriter(Writer writer) {
        impl = new JsonWriterImpl(writer);
    }

    public JsonWriter(Writer writer, JsonConfiguration config) {
        impl = new JsonWriterImpl(writer, config);
    }

    public JsonWriter(OutputStream out) {
        impl = new JsonWriterImpl(out);
    }

    public JsonWriter(OutputStream out, JsonConfiguration config) {
        impl = new JsonWriterImpl(out, config);
    }

    public JsonWriter(OutputStream out, String encoding) {
        impl = new JsonWriterImpl(out, encoding);
    }

    public JsonWriter(OutputStream out, String encoding, JsonConfiguration config) {
        impl = new JsonWriterImpl(out, encoding, config);
    }

    /**
     * Writes the specified {@link JsonArray}'s representation to the character
     * stream. This method needs to be called only once for a writer instance.
     *
     * @throws JsonException if the specified JSON object cannot be
     *     written due to i/o error
     * @throws IllegalStateException if this method, or writeObject or close
     *     method is already called
     */
    public void writeArray(JsonArray value) {
        impl.writeArray(value);
    }

    /**
     * Writes the specified {@link JsonObject}'s representation to the character
     * stream. This method needs to be called only once for a writer instance.
     *
     * @throws JsonException if the specified JSON object cannot be
     *     written due to i/o error
     * @throws IllegalStateException if this method, or writeArray or close
     *     method is already called
     */
    public void writeObject(JsonObject value) {
        impl.writeObject(value);
    }

    /**
     * Closes this JSON writer and frees any resources associated with the
     * writer. This doesn't close the underlying output source.
     */
    @Override
    public void close() {
        impl.close();
    }

    private void test() throws Exception {
        Writer writer = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(writer);
        jsonWriter.writeObject(new JsonBuilder().beginObject().endObject().build());
        jsonWriter.close();
        writer.close();

        writer = new StringWriter();
        jsonWriter = new JsonWriter(writer);
        jsonWriter.writeArray(new JsonBuilder().beginArray().endArray().build());
        jsonWriter.close();
        writer.close();
    }

}
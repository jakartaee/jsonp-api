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

package org.glassfish.json;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * A {@code JsonWriter} implementation using {@code JsonGenerator}
 *
 * @author Jitendra Kotamraju
 */
public class JsonWriterImpl {
    private final JsonGenerator generator;

    public JsonWriterImpl(Writer writer) {
        generator = Json.createGenerator(writer);
    }

    public JsonWriterImpl(Writer writer, JsonConfiguration config) {
        generator = Json.createGenerator(writer, config);
    }

    public JsonWriterImpl(OutputStream out) {
        generator = Json.createGenerator(out);
    }

    public JsonWriterImpl(OutputStream out, JsonConfiguration config) {
        generator = Json.createGenerator(out, config);
    }

    public JsonWriterImpl(OutputStream out, String encoding) {
        generator = Json.createGenerator(out, encoding);
    }

    public JsonWriterImpl(OutputStream out, String encoding, JsonConfiguration config) {
        generator = Json.createGenerator(out, encoding, config);
    }

    public void writeArray(JsonArray array) {
        try {
            JsonArrayBuilder<Closeable> builder = generator.beginArray();
            for(JsonValue value : array.getValues()) {
                builder.add(value);
            }
            builder.endArray().close();
        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    public void writeObject(JsonObject object) {
        try {
            JsonObjectBuilder<Closeable> builder = generator.beginObject();
            for(Map.Entry<String, JsonValue> e : object.getValues().entrySet()) {
                builder.add(e.getKey(), e.getValue());
            }
            builder.endObject().close();
        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    public void write(JsonStructure value) {
        if (value instanceof JsonArray) {
            writeArray((JsonArray)value);
        } else {
            writeObject((JsonObject)value);
        }
    }

    public void close() {
        generator.close();
    }

}
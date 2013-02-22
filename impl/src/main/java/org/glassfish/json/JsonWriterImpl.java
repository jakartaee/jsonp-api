/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
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
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

class JsonWriterImpl implements JsonWriter {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final JsonGenerator generator;
    private boolean writeDone;

    JsonWriterImpl(Writer writer) {
        this(writer, Collections.<String, Object>emptyMap());
    }

    JsonWriterImpl(Writer writer, Map<String, ?> config) {
        boolean prettyPrinting = JsonProviderImpl.isPrettyPrintingEnabled(config);
        generator = prettyPrinting
                ? new JsonPrettyGeneratorImpl(writer)
                : new JsonGeneratorImpl(writer);
    }

    JsonWriterImpl(OutputStream out) {
        this(out, UTF_8, Collections.<String, Object>emptyMap());
    }

    JsonWriterImpl(OutputStream out, Map<String, ?> config) {
        this(out, UTF_8, config);
    }

    JsonWriterImpl(OutputStream out, Charset charset, Map<String, ?> config) {
        boolean prettyPrinting = JsonProviderImpl.isPrettyPrintingEnabled(config);
        generator = prettyPrinting
                ? new JsonPrettyGeneratorImpl(out, charset)
                : new JsonGeneratorImpl(out, charset);
    }

    @Override
    public void writeArray(JsonArray array) {
        if (writeDone) {
            throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");
        }
        writeDone = true;
        generator.writeStartArray();
        for(JsonValue value : array) {
            generator.write(value);
        }
        generator.writeEnd();
    }

    @Override
    public void writeObject(JsonObject object) {
        if (writeDone) {
            throw new IllegalStateException("write/writeObject/writeArray/close method is already called.");
        }
        writeDone = true;
        generator.writeStartObject();
        for(Map.Entry<String, JsonValue> e : object.entrySet()) {
            generator.write(e.getKey(), e.getValue());
        }
        generator.writeEnd();
    }

    @Override
    public void write(JsonStructure value) {
        if (value instanceof JsonArray) {
            writeArray((JsonArray)value);
        } else {
            writeObject((JsonObject)value);
        }
    }

    @Override
    public void close() {
        writeDone = true;
        generator.close();
    }

}

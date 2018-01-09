/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json;

import org.glassfish.json.api.BufferPool;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonGeneratorFactoryImpl implements JsonGeneratorFactory {

    private final boolean prettyPrinting;
    private final Map<String, ?> config;    // unmodifiable map
    private final BufferPool bufferPool;

    JsonGeneratorFactoryImpl(Map<String, ?> config, boolean prettyPrinting,
            BufferPool bufferPool) {
        this.config = config;
        this.prettyPrinting = prettyPrinting;
        this.bufferPool = bufferPool;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return prettyPrinting
                ? new JsonPrettyGeneratorImpl(writer, bufferPool)
                : new JsonGeneratorImpl(writer, bufferPool);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return prettyPrinting
                ? new JsonPrettyGeneratorImpl(out, bufferPool)
                : new JsonGeneratorImpl(out, bufferPool);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        return prettyPrinting
                ? new JsonPrettyGeneratorImpl(out, charset, bufferPool)
                : new JsonGeneratorImpl(out, charset, bufferPool);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }

}

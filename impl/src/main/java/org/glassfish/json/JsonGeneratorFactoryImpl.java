/*
 * Copyright (c) 2012, 2019 Oracle and/or its affiliates. All rights reserved.
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

    private JsonConfig config;


    JsonGeneratorFactoryImpl(JsonConfig config) {
        this.config = config;
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return config.isPrettyPrinting()
                ? new JsonPrettyGeneratorImpl(writer, config)
                : new JsonGeneratorImpl(writer, config);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return config.isPrettyPrinting()
                ? new JsonPrettyGeneratorImpl(out, config)
                : new JsonGeneratorImpl(out, config);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, Charset charset) {
        return config.isPrettyPrinting()
                ? new JsonPrettyGeneratorImpl(out, charset, config)
                : new JsonGeneratorImpl(out, charset, config);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config.toConfigInUse();
    }

}

/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
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

package org.eclipse.jsonp;

import org.eclipse.jsonp.api.BufferPool;

import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonReaderFactoryImpl implements JsonReaderFactory {
    private final Map<String, ?> config;
    private final BufferPool bufferPool;
    private final boolean rejectDuplicateKeys;

    JsonReaderFactoryImpl(Map<String, ?> config, BufferPool bufferPool, boolean rejectDuplicateKeys) {
        this.config = config;
        this.bufferPool = bufferPool;
        this.rejectDuplicateKeys = rejectDuplicateKeys;
    }

    @Override
    public JsonReader createReader(Reader reader) {
        return new JsonReaderImpl(reader, bufferPool, rejectDuplicateKeys);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return new JsonReaderImpl(in, bufferPool, rejectDuplicateKeys);
    }

    @Override
    public JsonReader createReader(InputStream in, Charset charset) {
        return new JsonReaderImpl(in, charset, bufferPool, rejectDuplicateKeys);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }
}

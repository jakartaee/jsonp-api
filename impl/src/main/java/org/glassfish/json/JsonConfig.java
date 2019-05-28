/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
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

import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and stores configuration supported by org.glassfish.json JSONP implementation.
 */
public class JsonConfig {

    private final BufferPool bufferPool;
    private final boolean prettyPrinting;
    private final String bigNumberStrategy;

    /**
     * Create configuration from a String, Object property map.
     *
     * @param config properties to parse
     */
    public JsonConfig(Map<String, ?> config) {
        if (config == null) {
            prettyPrinting = false;
            bufferPool = new BufferPoolImpl();
            bigNumberStrategy = NumberStrategy.JSON_NUMBER;
        } else {
            prettyPrinting = JsonProviderImpl.isPrettyPrintingEnabled(config);
            Object bufferPool = config.get(BufferPool.class.getName());
            if (bufferPool == null) {
                this.bufferPool = new BufferPoolImpl();
            } else {
                if (!(bufferPool instanceof BufferPool)) {
                    throw new JsonGenerationException(BufferPool.class.getName()
                            + " must be an instance of a " + BufferPool.class.getName());
                }
                this.bufferPool = (BufferPool) bufferPool;
            }

            Object bigNumberStrategy = config.get(NumberStrategy.class.getName());
            if (bigNumberStrategy == null) {
                this.bigNumberStrategy = NumberStrategy.JSON_NUMBER;
            } else {
                if (!(bigNumberStrategy instanceof String)) {
                    throw new JsonGenerationException(NumberStrategy.class.getName()
                            + " must be an instance of a " + String.class.getName());
                }
                this.bigNumberStrategy = (String) bigNumberStrategy;
            }
        }
    }

    /**
     * Creates configuration with default values:
     *
     * Pretty printing: false.
     * BufferPool: org.glassfish.json.BufferPoolImpl.
     * Big number strategy: JSON_NUMBER.
     */
    public JsonConfig() {
        this.prettyPrinting = false;
        this.bufferPool = new BufferPoolImpl();
        this.bigNumberStrategy = NumberStrategy.JSON_NUMBER;
    }

    /**
     * Buffer pool to use.
     *
     * @return buffer pool.
     */
    public BufferPool getBufferPool() {
        return bufferPool;
    }

    /**
     * Pretty printing enabled for json generation.
     *
     * @return true if enabled
     */
    public boolean isPrettyPrinting() {
        return prettyPrinting;
    }

    /**
     * One of: {@link NumberStrategy} JSON_NUMBER, JSON_STRING.
     *
     * @return Number strategy.
     */
    public String getBigNumberStrategy() {
        return bigNumberStrategy;
    }

    /**
     * All configuration properties currently used.
     * @return used configuration.
     */
    public Map<String, ?> toConfigInUse() {
        Map<String, Object> configInUse = new HashMap<>();
        configInUse.put(JsonGenerator.PRETTY_PRINTING, prettyPrinting);
        configInUse.put(BufferPool.class.getName(), bufferPool);
        configInUse.put(NumberStrategy.class.getName(), bigNumberStrategy);
        return Collections.unmodifiableMap(configInUse);
    }
}

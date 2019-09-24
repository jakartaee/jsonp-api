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

package org.glassfish.json.tests;

import junit.framework.TestCase;

import jakarta.json.*;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorFactory;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests JsonGeneratorFactory
 *
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorFactoryTest extends TestCase {

    public JsonGeneratorFactoryTest(String testName) {
        super(testName);
    }

    public void testGeneratorFactory() {
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(null);

        JsonGenerator generator1 = generatorFactory.createGenerator(new StringWriter());
        generator1.writeStartArray().writeEnd();
        generator1.close();

        JsonGenerator generator2 = generatorFactory.createGenerator(new StringWriter());
        generator2.writeStartArray().writeEnd();
        generator2.close();
    }

    public void testGeneratorFactoryWithConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(config);
        Map<String, ?> config1 = generatorFactory.getConfigInUse();
        if (config1.size() != 1) {
            throw new JsonException("Expecting no of properties=1, got="+config1.size());
        }
        assertTrue(config1.containsKey(JsonGenerator.PRETTY_PRINTING));

        JsonGenerator generator1 = generatorFactory.createGenerator(new StringWriter());
        generator1.writeStartArray().writeEnd();
        generator1.close();

        JsonGenerator generator2 = generatorFactory.createGenerator(new StringWriter());
        generator2.writeStartArray().writeEnd();
        generator2.close();
    }

}

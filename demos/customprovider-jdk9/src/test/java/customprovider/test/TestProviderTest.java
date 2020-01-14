/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package customprovider.test;

import jakarta.json.Json;
import jakarta.json.stream.JsonGenerator;
import org.glassfish.json.customprovider.TestGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lukas
 */
public class TestProviderTest {

    @Test
    public void hello() {
        try (JsonGenerator generator = Json.createGenerator(System.out)) {
            Assert.assertTrue("TestGenerator is not picked up", generator instanceof TestGenerator);
            generator.writeStartArray().writeEnd();
        }
        System.out.println();
        System.out.println("Hurray!!!");
    }
}

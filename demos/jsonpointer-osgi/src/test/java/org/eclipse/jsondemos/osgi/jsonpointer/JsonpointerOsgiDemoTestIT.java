/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsondemos.osgi.jsonpointer;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(PaxExam.class)
public class JsonpointerOsgiDemoTestIT extends AbstractJsonpOsgiTest {

    @Test
    public void testJsonPointerOsgiBundle() {
        int expectedBundleStatus = 32; //Bundle.ACTIVE
        String expectedJsonFieldValue = "John";
        List<Bundle> bundles = Arrays.asList(bundleContext.getBundles());

        ServiceReference<?> reference = bundleContext.getServiceReference(JsonPointerService.class.getName());
        JsonPointerService jsonPointerService = (JsonPointerService) bundleContext.getService(reference);
        JsonObject jsonObject = jsonPointerService.read("/wiki.json");

        assertEquals(expectedBundleStatus, getBundleState(bundles, "jakarta.json-api"));
        assertEquals(expectedBundleStatus, getBundleState(bundles, "org.eclipse.jsonp"));
        assertEquals(expectedBundleStatus, getBundleState(bundles, "jsondemos-jsonpointer-osgi-api"));
        assertEquals(expectedJsonFieldValue, ((JsonString)jsonObject.get("firstName")).getString());
    }

    private int getBundleState(List<Bundle> bundles, String bundleSymbolicName) {
        return bundles
                .stream()
                .filter(bundle -> bundle.getSymbolicName().equals(bundleSymbolicName))
                .findFirst().get()
                .getState();
    }
}

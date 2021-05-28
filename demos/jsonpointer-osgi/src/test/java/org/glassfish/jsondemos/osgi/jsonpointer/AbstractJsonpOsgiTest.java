/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.osgi.jsonpointer;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

public class AbstractJsonpOsgiTest {

    @Inject
    protected BundleContext bundleContext;

    @Configuration
    public Option[] configuration() {
        final Option[] base = options(
                cleanCaches(true),
                junitBundles(),
                mavenBundle()
                        .groupId("org.apache.aries.spifly")
                        .artifactId("org.apache.aries.spifly.dynamic.bundle")
                        .versionAsInProject(),
                mavenBundle().groupId("org.ow2.asm").artifactId("asm-all").versionAsInProject(),
                mavenBundle().groupId("org.apache.aries").artifactId("org.apache.aries.util").versionAsInProject(),
                mavenBundle().groupId("jakarta.json").artifactId("jakarta.json-api").versionAsInProject(),
                mavenBundle()
                        .groupId("org.glassfish")
                        .artifactId("jakarta.json")
                        .versionAsInProject()
                        .classifier("module"),
                mavenBundle()
                        .groupId("org.glassfish.jsonp")
                        .artifactId("jsondemos-jsonpointer-osgi")
                        .versionAsInProject()
        );
        return base;
    }
}

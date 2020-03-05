/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.jaxrs;

import jakarta.json.stream.JsonGenerator;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A Jakarta RESTful Web Services Demo Application
 *
 * @author Jitendra Kotamraju
 */
@ApplicationPath("/")
public class DemoApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(ParserResource.class);
        set.add(GeneratorResource.class);
        set.add(ObjectResource.class);
        set.add(ArrayResource.class);
        set.add(StructureResource.class);

        return set;
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put(JsonGenerator.PRETTY_PRINTING, true);
        }};
    }
}

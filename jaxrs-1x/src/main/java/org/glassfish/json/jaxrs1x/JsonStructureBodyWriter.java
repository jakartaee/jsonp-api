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

package org.glassfish.json.jaxrs1x;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS MessageBodyWriter for JsonStructure. This allows
 * JsonStructure, JsonArray and JsonObject to be return type of a
 * resource method.
 *
 * @author Jitendra Kotamraju
 * @author Blaise Doughan
 * @author Michal Gajdos
 */
@Provider
@Produces({"application/json", "text/json", "*/*"})
public class JsonStructureBodyWriter implements MessageBodyWriter<JsonStructure> {
    private static final String JSON = "json";
    private static final String PLUS_JSON = "+json";

    private JsonWriterFactory wf = Json.createWriterFactory(null);

    @PostConstruct
    private void init() {
        wf = Json.createWriterFactory(new HashMap<>());
    }

    @Override
    public boolean isWriteable(Class<?> aClass,
                               Type type, Annotation[] annotations, MediaType mediaType) {
        return JsonStructure.class.isAssignableFrom(aClass) && supportsMediaType(mediaType);
    }

    /**
     * @return true for all media types of the pattern *&#47;json and
     * *&#47;*+json.
     */
    private static boolean supportsMediaType(final MediaType mediaType) {
        return mediaType.getSubtype().equals(JSON) || mediaType.getSubtype().endsWith(PLUS_JSON);
    }

    @Override
    public long getSize(JsonStructure jsonStructure, Class<?> aClass,
                        Type type, Annotation[] annotations, MediaType mediaType) {

        return -1;
    }

    @Override
    public void writeTo(JsonStructure jsonStructure, Class<?> aClass, Type type,
                        Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> stringObjectMultivaluedMap,
                        OutputStream outputStream) throws IOException, WebApplicationException {
        try (JsonWriter writer = wf.createWriter(outputStream)) {
            writer.write(jsonStructure);
        }
    }
}

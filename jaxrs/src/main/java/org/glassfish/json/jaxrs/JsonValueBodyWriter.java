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

package org.glassfish.json.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

/**
 * Jakarta RESTful Web Services MessageBodyWriter for JsonValue.
 * This allows JsonValue to be return type of a resource method.
 *
 * @author Jitendra Kotamraju
 * @author Blaise Doughan
 * @author Michal Gajdos
 */
@Provider
@Produces({"application/json", "text/json", "*/*"})
public class JsonValueBodyWriter implements MessageBodyWriter<JsonValue> {
    private static final String JSON = "json";
    private static final String PLUS_JSON = "+json";

    private JsonWriterFactory wf = Json.createWriterFactory(null);

    @Context
    private Configuration config;

    @PostConstruct
    private void init() {
        Map<String, Object> props = new HashMap<>();
        if (config != null && config.getProperties().containsKey(JsonGenerator.PRETTY_PRINTING)) {
            props.put(JsonGenerator.PRETTY_PRINTING, true);
        }
        wf = Json.createWriterFactory(props);
    }

    @Override
    public boolean isWriteable(Class<?> aClass,
            Type type, Annotation[] annotations, MediaType mediaType) {
        return JsonValue.class.isAssignableFrom(aClass) && supportsMediaType(mediaType);
    }

    /**
     * @return true for all media types of the pattern *&#47;json and
     * *&#47;*+json.
     */
    private static boolean supportsMediaType(final MediaType mediaType) {
        return mediaType.getSubtype().equals(JSON) || mediaType.getSubtype().endsWith(PLUS_JSON);
    }

    @Override
    public long getSize(JsonValue jsonValue, Class<?> aClass,
            Type type, Annotation[] annotations, MediaType mediaType) {

        return -1;
    }

    @Override
    public void writeTo(JsonValue jsonValue, Class<?> aClass, Type type,
            Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> stringObjectMultivaluedMap,
            OutputStream outputStream) throws IOException, WebApplicationException {
        try (JsonWriter writer = wf.createWriter(outputStream)) {
            writer.write(jsonValue);
        }
    }
}

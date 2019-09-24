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
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import jakarta.json.Json;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

/**
 * Jakarta RESTful Web Services MessageBodyReader for JsonValue.
 * This allows JsonValue to be a parameter of a resource method.
 *
 * @author Jitendra Kotamraju
 * @author Blaise Doughan
 * @author Michal Gajdos
 */
@Provider
@Consumes({"application/json", "text/json", "*/*"})
public class JsonValueBodyReader implements MessageBodyReader<JsonValue> {
    private final JsonReaderFactory rf = Json.createReaderFactory(null);

    private static final String JSON = "json";
    private static final String PLUS_JSON = "+json";

    @Override
    public boolean isReadable(Class<?> aClass, Type type,
            Annotation[] annotations, MediaType mediaType) {
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
    public JsonValue readFrom(Class<JsonValue> jsonValueClass,
            Type type, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> stringStringMultivaluedMap,
            InputStream inputStream) throws IOException, WebApplicationException {
        try (JsonReader reader = rf.createReader(inputStream)) {
            return reader.readValue();
        }
    }
}

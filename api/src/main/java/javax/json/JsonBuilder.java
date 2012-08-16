/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.json;

import org.glassfish.jsonapi.JsonBuilderImpl;

/**
 * Builds a JSON {@link JsonObject object} or {@link JsonArray array} from
 * scratch. It uses builder pattern to build these object models and the
 * builder methods can be chained.
 *
 * <p>
 * <b>For example</b>, for the following JSON
 *
 * <code>
 * <pre>
 * {
 *     "firstName": "John", "lastName": "Smith", "age": 25,
 *     "address" : {
 *         "streetAddress", "21 2nd Street",
 *         "city", "New York",
 *         "state", "NY",
 *         "postalCode", "10021"
 *     },
 *     "phoneNumber": [
 *         { "type": "home", "number": "212 555-1234" },
 *         { "type": "fax", "number": "646 555-4567" }
 *     ]
 * }
 * </pre>
 * </code>
 *
 * a JsonObject instance can be built using:
 *
 * <p>
 * <code>
 * <pre>
 * JsonObject value = new JsonBuilder()
 *     .beginObject()
 *         .add("firstName", "John")
 *         .add("lastName", "Smith")
 *         .add("age", 25)
 *         .beginObject("address")
 *             .add("streetAddress", "21 2nd Street")
 *             .add("city", "New York")
 *             .add("state", "NY")
 *             .add("postalCode", "10021")
 *         .endObject()
 *         .beginArray("phoneNumber")
 *             .beginObject()
 *                 .add("type", "home")
 *                 .add("number", "212 555-1234")
 *             .endObject()
 *             .beginObject()
 *                 .add("type", "home")
 *                 .add("number", "646 555-4567")
 *             .endObject()
 *         .endArray()
 *     .endObject()
 * .build();
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public class JsonBuilder {

    private final JsonBuilderImpl impl;

    /**
     * Build task that gives the result of the build process
     */
    public static interface JsonBuildable<T extends JsonStructure> {
        /**
         * Builds a JSON object or array
         *
         * @return built object or array
         */
        public T build();
    }

    public JsonBuilder() {
        impl = new JsonBuilderImpl();
    }

    /**
     * Start building a JSON object
     *
     * @return an object builder
     */
    public JsonObjectBuilder<JsonBuildable<JsonObject>> beginObject() {
        return impl.beginObject();
    }

    /**
     * Start building a JSON array
     *
     * @return an array builder
     */
    public JsonArrayBuilder<JsonBuildable<JsonArray>> beginArray() {
        return impl.beginArray();
    }

}
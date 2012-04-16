/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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

package javax.json.stream;

import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.Closeable;
import java.io.StringWriter;
import java.io.Writer;

/**
 * A streaming JSON generator.
 *
 * <p>
 * For example:
 *
 * <code>
 * <pre>
 * JsonGenerator generator = JsonGenerator.create(...);
 * generator
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
 *                 .add("type", "fax")
 *                 .add("number", "646 555-4567")
 *             .endObject()
 *         .endArray()
 *     .endObject();
 * generator.close();
 *
 * would generate a JSON equivalent to the following:
 * {
 *   "firstName": "John", "lastName": "Smith", "age": 25,
 *   "address" : {
 *       "streetAddress", "21 2nd Street",
 *       "city", "New York",
 *       "state", "NY",
 *       "postalCode", "10021"
 *   },
 *   "phoneNumber": [
 *       {"type": "home", "number": "212 555-1234"},
 *       {"type": "fax", "number": "646 555-4567"}
 *    ]
 * }
 *
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public class JsonGenerator implements /*Auto*/Closeable {

    /**
     * Creates a JSON generator which can be used to write JSON text to the
     * specified i/o writer.
     *
     * @param writer to which JSON is written
     */
    public JsonGenerator(Writer writer) {
    }

    /**
     * Starts writing of a JSON object in a streaming fashion.
     *
     * @return an object builder
     */
    public JsonObjectBuilder<Closeable> beginObject() {
        return null;
    }

    /**
     * Starts writing of a JSON array in a streaming fashion.
     *
     * @return an array builder
     */
    public JsonArrayBuilder<Closeable> beginArray() {
        return null;
    }

    /**
     * Closes this generator and frees any resources associated with the
     * generator. This doesn't close the underlying output source.
     */
    @Override
    public void close() {
    }

    private void test() {
        JsonGenerator generator = new JsonGenerator(new StringWriter());
        generator
            .beginObject()
                .add("firstName", "John")
                .add("lastName", "Smith")
                .add("age", 25)
                .beginObject("address")
                    .add("streetAddress", "21 2nd Street")
                    .add("city", "New York")
                    .add("state", "NY")
                    .add("postalCode", "10021")
                .endObject()
                .beginArray("phoneNumber")
                    .beginObject()
                        .add("type", "home")
                        .add("number", "212 555-1234")
                    .endObject()
                    .beginObject()
                        .add("type", "fax")
                        .add("number", "646 555-4567")
                    .endObject()
                .endArray()
            .endObject();
        generator.close();

        generator = new JsonGenerator(new StringWriter());
        generator
            .beginArray()
                .beginObject()
                    .add("type", "home")
                    .add("number", "212 555-1234")
                .endObject()
                .beginObject()
                    .add("type", "fax")
                    .add("number", "646 555-4567")
                .endObject()
            .endArray();
        generator.close();
    }

}
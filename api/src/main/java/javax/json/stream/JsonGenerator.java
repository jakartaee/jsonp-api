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

import javax.json.JsonValue;
import java.io.Closeable;
import java.io.Flushable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A JSON generator that writes JSON in a streaming way. The generator
 * can be created from many output sources like {@link java.io.Writer}
 * and {@link java.io.OutputStream}.
 *
 * <p>
 * For example, a generator can be created as follows:
 * <code>
 * <pre>
 * JsonGenerator generator = Json.createGenerator(...);
 * </pre>
 * </code>
 *
 * A generator can also be created using {@link JsonGeneratorFactory}. If
 * multiple generator instances are created, then creating them using
 * a generator factory is preferred.
 * <p>
 * <code>
 * <pre>
 * JsonGeneratorFactory factory = Json.createGeneratorFactory();
 * JsonGenerator generator1 = factory.createGenerator(...);
 * JsonGenerator generator2 = factory.createGenerator(...);
 * </pre>
 * </code>
 *
 * <p>
 * The generator is used to generate JSON object in a streaming way by calling
 * its {@link #writeStartObject()} method and writing name/value pairs.
 * <p>
 * <b>For example 1:</b>
 * <p>Empty JSON object can be generated as follows:
 * <code>
 * <pre>
 * JsonGenerator generator = ...;
 * generator.writeStartObject().end().close();
 * </pre>
 * </code>
 *
 * The generator is used to generate JSON array in a streaming way by calling
 * its {@link #writeStartArray()} method and adding values.
 * <p>
 * <b>For example 2:</b>
 * <p>Empty JSON array can be generated as follows:
 * <code>
 * <pre>
 * JsonGenerator generator = ...;
 * generator.writeStartArray().end().close();
 * </pre>
 * </code>
 *
 * The generator methods can be chained. Similarly, the following generator
 * <p>
 * <code>
 * <pre>
 * generator
 *     .writeStartObject()
 *         .write("firstName", "John")
 *         .write("lastName", "Smith")
 *         .write("age", 25)
 *         .writeStartObject("address")
 *             .write("streetAddress", "21 2nd Street")
 *             .write("city", "New York")
 *             .write("state", "NY")
 *             .write("postalCode", "10021")
 *         .end()
 *         .writeStartArray("phoneNumber")
 *             .writeStartObject()
 *                 .write("type", "home")
 *                 .write("number", "212 555-1234")
 *             .end()
 *             .writeStartObject()
 *                 .write("type", "fax")
 *                 .write("number", "646 555-4567")
 *             .end()
 *         .end()
 *     .end();
 * generator.close();
 * </pre>
 * </code>
 *
 * would generate a JSON equivalent to the following:
 * <p>
 * <code>
 * <pre>
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
 * </pre>
 * </code>
 *
 * The generated JSON text must strictly conform to the grammar defined in the
 * <a href="http://www.ietf.org/rfc/rfc4627.txt">RFC</a>.
 *
 * @see javax.json.Json
 * @see JsonGeneratorFactory
 * @author Jitendra Kotamraju
 */
public interface JsonGenerator extends Flushable, /*Auto*/Closeable {

    /**
     * Starts writing a JSON Object
     *
     * @return this generator
     */
    public JsonGenerator writeStartObject();

    public JsonGenerator writeStartObject(String name);

    /**
     * Starts writing a JSON array
     *
     * @return this generator
     */
    public JsonGenerator writeStartArray();
    public JsonGenerator writeStartArray(String name);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws javax.json.JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonGenerator write(String name, JsonValue value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     */
    public JsonGenerator write(String name, String value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws javax.json.JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(String name, BigInteger value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(String name, BigDecimal value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     *
     */
    public JsonGenerator write(String name, int value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     */
    public JsonGenerator write(String name, long value);

    /**
     * Writes a name/value pair with in an object context.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(String name, double value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * <p>TODO not needed since add(JsonValue.TRUE|FALSE) can be used ??
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     */
    public JsonGenerator write(String name, boolean value);


    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * <p>TODO not needed since add(JsonValue.NULL) can be used ??
     *
     * @param name name/key with which the specified value is to be associated
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     */
    public JsonGenerator writeNull(String name);

    /**
     * Indicates the end of the JSON array that is being built.
     *
     * @return this generator
     * @throws javax.json.JsonException if there is an i/o error
     * @throws JsonGenerationException
     */
    public JsonGenerator end();

    /**
     * Adds the specified value to the array that is being built.
     *
     * @param value
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     */
    public JsonGenerator write(JsonValue value);

    /**
     * Adds the specified value as a JSON string value to the array
     * that is being built.
     *
     * @param value string
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     */
    public JsonGenerator write(String value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(BigDecimal value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(BigInteger value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(int value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(long value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see javax.json.JsonNumber
     */
    public JsonGenerator write(double value);

    /**
     * Adds a JSON true or false value to the array that is being built.
     *
     * <p>TODO not needed since add(JsonValue.TRUE|FALSE) can be used ??
     *
     * @param value a boolean
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     */
    public JsonGenerator write(boolean value);

    /**
     * Adds the JSON null value to the array that is being built.
     *
     * <p>TODO not needed since add(JsonValue.NULL) can be used ??
     *
     * @return this array builder
     * @throws IllegalStateException when invoked after end method is
     * called.
     */
    public JsonGenerator writeNull();

    /**
     * Closes this generator and frees any resources associated with the
     * generator. This closes the underlying output source.
     */
    @Override
    public void close();

    @Override
    public void flush();

}
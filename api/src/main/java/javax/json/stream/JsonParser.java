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

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.Closeable;
import java.io.Reader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;

import static javax.json.JsonNumber.JsonNumberType;

/**
 * A JSON parser that allows forward, read-only access to JSON in a
 * a streaming way. This is designed to be the most efficient
 * way to read JSON text. The parser can be created from many input sources
 * like {@link Reader}, {@link InputStream}, {@link JsonArray} and
 * {@link JsonObject}.
 *
 * <p>
 * For example, a parser for empty JSON array can be created as follows:
 * <code>
 * <pre>
 * JsonParser parser = Json.createParser(new StringReader("[]"));
 * </pre>
 * </code>
 *
 * A parser can also be created using {@link JsonParserFactory}. If
 * multiple parser instances are created, then creating them using
 * a factory is preferred.
 * <p>
 * <code>
 * <pre>
 * JsonParserFactory factory = Json.createParserFactory();
 * JsonParser parser1 = factory.createParser(...);
 * JsonParser parser2 = factory.createParser(...);
 * </pre>
 * </code>
 * 
 * <p>
 * The parser is used to parse JSON in a pull manner by calling its iterator
 * methods. The iterator's {@code next()} method causes the parser to advance
 * to the next parse state.
 * <p>
 * <b>For example 1</b>:
 * <p>For empty JSON object { },
 * the iterator would give {<B>START_OBJECT</B> }<B>END_OBJECT</B> parse
 * events at the specified locations. Those events can be accessed using the
 * following code.
 *
 * <code>
 * <pre>
 * Iterator&lt;Event> it = parser.iterator();
 * Event event = it.next(); // START_OBJECT
 * event = it.next();       // END_OBJECT
 * </pre>
 * </code>
 *
 * <p>
 * <b>For example 2</b>:
 * <p>
 * For the following JSON
 * <pre>
 * {
 *   "firstName": "John", "lastName": "Smith", "age": 25,
 *   "phoneNumber": [
 *       { "type": "home", "number": "212 555-1234" },
 *       { "type": "fax", "number": "646 555-4567" }
 *    ]
 * }
 * </pre>
 *
 * the iterator would give
 *
 * <p>
 * <pre>
 * {<B>START_OBJECT</B>
 *   "firstName"<B>KEY_NAME</B>: "John"<B>VALUE_STRING</B>, "lastName"<B>KEY_NAME</B>: "Smith"<B>VALUE_STRING</B>, "age"<B>KEY_NAME</B>: 25<B>VALUE_NUMBER</B>,
 *   "phoneNumber"<B>KEY_NAME</B> : [<B>START_ARRAY</B>
 *       {<B>START_OBJECT</B> "type"<B>KEY_NAME</B>: "home"<B>VALUE_STRING</B>, "number"<B>KEY_NAME</B>: "212 555-1234"<B>VALUE_STRING</B> }<B>END_OBJECT</B>,
 *       {<B>START_OBJECT</B> "type"<B>KEY_NAME</B>: "fax"<B>VALUE_STRING</B>, "number"<B>KEY_NAME</B>: "646 555-4567"<B>VALUE_STRING</B> }<B>END_OBJECT</B>
 *    ]<B>END_ARRAY</B>
 * }<B>END_OBJECT</B>
 * </pre>
 * parse events at the specified locations.
 *
 * <p>
 * Here, "John" value is accessed as follows:
 *
 * <p>
 * <code>
 * <pre>
 * Iterator&lt;Event> it = parser.iterator();
 * Event event = it.next(); // START_OBJECT
 * event = it.next();       // KEY_NAME
 * event = it.next();       // VALUE_STRING
 * parser.getString();      // "John"
 * </pre>
 * </code>
 *
 * @see javax.json.Json
 * @see JsonParserFactory
 * @author Jitendra Kotamraju
 */
public interface JsonParser extends /*Auto*/Closeable {

    /**
     * Event for parser state while parsing the JSON
     */
    enum Event {
        /**
         * Event for start of a JSON array. This event indicates '[' is parsed.
         */
        START_ARRAY,
        /**
         * Event for start of a JSON object. This event indicates '{' is parsed.
         */
        START_OBJECT,
        /**
         * Event for a name in name(key)/value pair of a JSON object. This event
         * indicates that the key name is parsed. The name/key value itself
         * can be accessed using {@link #getString}
         */
        KEY_NAME,
        /**
         * Event for JSON string value. This event indicates a string value in
         * an array or object is parsed. The string value itself can be
         * accessed using {@link #getString}
         */
        VALUE_STRING,
        /**
         * Event for a number value. This event indicates a number value in
         * an array or object is parsed. The number value itself can be
         * accessed using {@link javax.json.JsonNumber} methods
         */
        VALUE_NUMBER,
        /**
         * Event for a true value. This event indicates a true value in an
         * array or object is parsed.
         */
        VALUE_TRUE,
        /**
         * Event for a false value. This event indicates a false value in an
         * array or object is parsed.
         */
        VALUE_FALSE,
        /**
         * Event for a null value. This event indicates a null value in an
         * array or object is parsed.
         */
        VALUE_NULL,
        /**
         * Event for end of an object. This event indicates '}' is parsed.
         */
        END_OBJECT,
        /**
         * Event for end of an array. This event indicates ']' is parsed.
         */
        END_ARRAY
    }

    boolean hasNext();

    Event next();

    /**
     * Returns a String for name(key), string value and number value. This
     * method is only called when the parser state is one of
     * {@link Event#KEY_NAME}, {@link Event#VALUE_STRING},
     * {@link Event#VALUE_NUMBER}.
     *
     * @return name when the parser state is {@link Event#KEY_NAME}.
     *         string value when the parser state is {@link Event#VALUE_STRING}.
     *         number value when the parser state is {@link Event#VALUE_NUMBER}.
     * @throws IllegalStateException when the parser is not in one of
     *      KEY_NAME, VALUE_STRING, VALUE_NUMBER states
     */
    String getString();

    /**
     * Returns a number type that can hold JSON number. A {@link BigDecimal}
     * may be used to store the numeric value of the number. If the scale of
     * a value is non-zero, its number type is
     * {@link JsonNumberType#BIG_DECIMAL BIG_DECIMAL}.
     * If the scale is zero, and the value is numerically an integer.
     * If the value can be exactly represented as an int, its type is
     * {@link JsonNumberType#INT INT}; if the value can be exactly represented
     * as a long, its type is {@link JsonNumberType#LONG LONG}; otherwise,
     * its type is {@link JsonNumberType#BIG_DECIMAL BIG_DECIMAL}.
     *
     * <p>
     * This method can be used to get the correct number type for a number.
     * For example:
     * <code>
     * <pre>
     * switch(parser.getNumberType()) {
     *     case INT :
     *         int i = parser.getIntValue(); break;
     *     case LONG :
     *         long l = parser.getLongValue(); break;
     *     case BIG_DECIMAL :
     *         BigDecimal bd = parser.getBigDecimalValue(); break;
     * }
     * </pre>
     * </code>
     * This method can only be called when the parser
     * state is {@link Event#VALUE_NUMBER}
     *
     * @return a number type
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     */
    JsonNumberType getNumberType();

    /**
     * Returns JSON number as an integer. The returned value is equal
     * to {@code new BigDecimal(getString()).intValue()}. Note that
     * this conversion can lose information about the overall magnitude
     * and precision of the number value as well as return a result with
     * the opposite sign. This method is only called when the parser is in
     * {@link Event#VALUE_NUMBER} state.
     *
     * @return an integer for JSON number.
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     * @see java.math.BigDecimal#intValue()
     */
    int getIntValue();

    /**
     * Returns JSON number as a long. The returned value is equal
     * to {@code new BigDecimal(getString()).longValue()}. Note that this
     * conversion can lose information about the overall magnitude and
     * precision of the number value as well as return a result with
     * the opposite sign. This method is only called when the parser is in
     * {@link Event#VALUE_NUMBER} state.
     *
     * @return a long for JSON number.
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     * @see java.math.BigDecimal#longValue()
     */
    long getLongValue();

    /**
     * Returns JSON number as a {@code BigDecimal}. The BigDecimal
     * is created using {@code new BigDecimal(getString())}. This
     * method is only called when the parser is in
     * {@link Event#VALUE_NUMBER} state.
     *
     * @return a BigDecimal for JSON number
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     */
    BigDecimal getBigDecimalValue();

    /**
     * getJsonValue(JsonObject.class) is valid in START_OBJECT state,
     * moves cursor to END_OBJECT
     *
     * getJsonValue(JsonArray.class) is valid in START_ARRAY state
     * moves cursor to END_ARRAY
     *
     * getJsonValue(JsonString.class) is valid in VALUE_STRING state
     *
     * getJsonValue(JsonNumber.class) is valid in VALUE_NUMBER state
     *
     * @param clazz
     * @return
     *
    public <T extends JsonValue> T getJsonValue(Class<T> clazz);
     */

    /**
     * Closes this parser and frees any resources associated with the
     * parser. This closes the underlying input source.
     */
    @Override
    void close();

}
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
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.Closeable;
import java.io.Reader;
import java.util.Iterator;

/**
 * A JSON parser that allows forward, read-only access to JSON in a
 * a streaming way. This is designed to be the most efficient
 * way to read JSON data. The parser can be created from many input sources
 * like {@link Reader}, {@link JsonArray}, and {@link JsonObject}
 * 
 * <p>
 * The JsonParser is used to parse JSON in a pull manner by calling its iterator
 * methods. The iterator's {@code next()} method causes the parser to advance
 * to the next parse state.
 * <p>
 * For example 1:
 * <p>For empty JSON object { },
 * the iterator would give {<B>START_OBJECT</B> }<B>END_OBJECT</B> parse
 * events at the specified locations. Those events can be accessed using the
 * following code.
 *
 * <code>
 * <pre>
 * Iterator&lt;Event> it = reader.iterator();
 * Event event = it.next(); // START_OBJECT
 * event = it.next();       // END_OBJECT
 * </pre>
 * </code>
 *
 * <p>
 * For example 2:
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
 *
 * the iterator would give
 *
 * {<B>START_OBJECT</B>
 *   "firstName"<B>KEY_NAME</B>: "John"<B>VALUE_STRING</B>, "lastName"<B>KEY_NAME</B>: "Smith"<B>VALUE_STRING</B>, "age"<B>KEY_NAME</B>: 25<B>VALUE_NUMBER</B>,
 *   "phoneNumber"<B>KEY_NAME</B> : [<B>START_ARRAY</B>
 *       {<B>START_OBJECT</B> "type"<B>KEY_NAME</B>: "home"<B>VALUE_STRING</B>, "number"<B>KEY_NAME</B>: "212 555-1234"<B>VALUE_STRING</B> }<B>END_OBJECT</B>,
 *       {<B>START_OBJECT</B> "type"<B>KEY_NAME</B>: "fax"<B>VALUE_STRING</B>, "number"<B>KEY_NAME</B>: "646 555-4567"<B>VALUE_STRING</B> }<B>END_OBJECT</B>
 *    ]<B>END_ARRAY</B>
 * }<B>END_OBJECT</B> parse events at the specified locations.
 * </pre>
 * 
 * Here, "John" value is accessed as follows:
 * <code>
 * <pre>
 * Iterator&lt;Event> it = reader.iterator();
 * Event event = it.next(); // START_OBJECT
 * event = it.next();       // KEY_NAME
 * event = it.next();       // VALUE_STRING
 * reader.getString();      // "John"
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public interface JsonParser extends Iterable<JsonParser.Event>, /*Auto*/Closeable {

    /**
     * Event for parser state while parsing the JSON
     */
    public enum Event {
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


    /**
     * Returns name when the parser state is {@link Event#KEY_NAME} or
     * returns string value when the parser state is {@link Event#VALUE_STRING}
     * 
     * @return a string
     * @throws IllegalStateException when the parser state is not
     *      KEY_NAME or VALUE_STRING
     */
    public String getString();

    /**
     * Returns number type and this method can only be called when the parser
     * state is {@link Event#VALUE_NUMBER}
     *
     * @return a number type
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     */
    public JsonNumber.JsonNumberType getNumberType();

    /**
     * Returns a number and this method can only be called when the parser
     * state is {@link Event#VALUE_NUMBER}
     *
     * <p>TODO Any performance implications of creating JsonNumber,
     * Otherwise, need to expose methods from JsonNumber
     *
     * @return a number
     * @throws IllegalStateException when the parser state is not
     *      VALUE_NUMBER
     */
    public JsonNumber getNumber();

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
     * @param <T>
     * @return
     */
    public <T extends JsonValue> T getJsonValue(Class<T> clazz);

    @Override
    public Iterator<Event> iterator();

    /**
     * Closes this parser and frees any resources associated with the
     * parser. This doesn't close the underlying input source.
     */
    @Override
    public void close();

}
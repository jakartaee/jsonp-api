/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Helps in building a JSON array. This is an intermediary class and the
 * actual build process is started from {@link JsonBuilder} or
 * {@link javax.json.stream.JsonGenerator}
 *
 * @author Jitendra Kotamraju
 * @see JsonBuilder
 * @see javax.json.stream.JsonGenerator
 */
public interface JsonArrayBuilder<T> {

    /**
     * Indicates the end of the JSON array that is being built.
     *
     * @return the enclosing object of type T
     */
    public T endArray();

    /**
     * Adds the specified value to the array that is being built.
     *
     * @param value
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(JsonValue value);

    /**
     * Adds the specified value as a JSON string value to the array
     * that is being built.
     *
     * @param value string
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(String value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(BigDecimal value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(BigInteger value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(int value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(long value);

    /**
     * Adds the specified value as a JSON number value to the array
     * that is being built.
     *
     * @param value a number
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     */
    public JsonArrayBuilder<T> add(double value);

    /**
     * Adds a JSON true or false value to the array that is being built.
     *
     * <p>TODO not needed since add(JsonValue.TRUE|FALSE) can be used ??
     *
     * @param value a boolean
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> add(boolean value);

    /**
     * Adds the JSON null value to the array that is being built.
     *
     * <p>TODO not needed since add(JsonValue.NULL) can be used ??
     *
     * @return this array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<T> addNull();

    /**
     * Returns a JSON array builder to build a new object value
     *
     * @return an array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonObjectBuilder<JsonArrayBuilder<T>> beginObject();

    /**
     * Returns a JSON array builder to build a new array value
     *
     * @return an array builder
     * @throws IllegalStateException when invoked after endArray method is
     * called.
     */
    public JsonArrayBuilder<JsonArrayBuilder<T>> beginArray();

}
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
 * Helps in building a JSON object. This is an intermediary class and the
 * actual build process is started from {@link JsonBuilder} or
 * {@link javax.json.stream.JsonGenerator}
 *
 * @author Jitendra Kotamraju
 * @see JsonBuilder
 * @see javax.json.stream.JsonGenerator
 */
public interface JsonObjectBuilder<T> {

    /**
     * Indicates the end of the JSON object that is being built.
     *
     * @return the enclosing object of type T
     * @throws IllegalStateException when end method
     * is already called
     */
    public T end();

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonObjectBuilder<T> add(String name, JsonValue value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonObjectBuilder<T> add(String name, String value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder<T> add(String name, BigInteger value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder<T> add(String name, BigDecimal value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder<T> add(String name, int value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder<T> add(String name, long value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     * @throws NumberFormatException if value is Not-a-Number(NaN) or infinity
     *
     * @see JsonNumber
     */
    public JsonObjectBuilder<T> add(String name, double value);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * <p>TODO not needed since add(JsonValue.TRUE|FALSE) can be used ??
     *
     * @param name name/key with which the specified value is to be associated
     * @param value value to be associated with the specified name/key
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonObjectBuilder<T> add(String name, boolean value);

    /**
     *
     * @return this object builder
     *
    public JsonObjectBuilder<T> addObject(String name, Map<String, JsonValue> values);
    */

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * <p>TODO not needed since add(JsonValue.NULL) can be used ??
     *
     * @param name name/key with which the specified value is to be associated
     * @return this object builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonObjectBuilder<T> addNull(String name);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @return a object member builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonObjectBuilder<JsonObjectBuilder<T>> startObject(String name);

    /**
     * Associates the specified value with the specified name/key in the
     * JSON object that is being built.
     *
     * @param name name/key with which the specified value is to be associated
     * @return a array member builder
     * @throws JsonException if there is a mapping for the specified name/key
     * in the JSON object
     * @throws IllegalStateException when invoked after the end method
     * is called
     */
    public JsonArrayBuilder<JsonObjectBuilder<T>> startArray(String name);

}
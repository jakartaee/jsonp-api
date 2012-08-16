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

package javax.json;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@code JsonNumber} represents a JSON number value. A {@link BigDecimal}
 * may be used to store the numeric value. Some of the method semantics
 * in this class are defined using the {@code BigDecimal} semantics.
 *
 * @author Jitendra Kotamraju
 */
public interface JsonNumber extends JsonValue {

    /**
     * JSON number type
     */
    public enum JsonNumberType {
        /**
         * int number type
         */
        INT,

        /**
         * long number type
         */
        LONG,

        /**
         * BigDecimal number type
         */
        BIG_DECIMAL
    }

    /**
     * Returns a JSON number type that can hold the number's numeric value.
     * A {@link BigDecimal} may be used to store the numeric value.
     * If the scale of a value is non-zero, its number type is
     * {@link JsonNumberType#BIG_DECIMAL BIG_DECIMAL}. If the scale is zero,
     * and the value is numerically an integer. If the value can be exactly
     * represented as an int, its type is {@link JsonNumberType#INT INT};
     * if the value can be exactly represented as a long, its type is
     * {@link JsonNumberType#LONG LONG}; otherwise, its type is
     * {@link JsonNumberType#BIG_DECIMAL BIG_DECIMAL}.
     *
     * @return a number type
     */
    public JsonNumberType getNumberType();

    /**
     * Converts JSON number to an integer number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return an {@code int} for JSON number.
     * @see java.math.BigDecimal#intValue()
     */
    public int getIntValue();

    /**
     * Converts JSON number to an integer number.
     *
     * @return an {@code int} for JSON number
     * @throws ArithmeticException cause if the number has a nonzero fractional
     *         part, or will not fit in an {@code int}
     * @see java.math.BigDecimal#intValueExact()
     */
    public int getIntValueExact();

    /**
     * Converts JSON number to a long number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return a {@code long} for JSON number.
     * @see java.math.BigDecimal#longValue()
     */
    public long getLongValue();

    /**
     * Converts JSON number to a long number.
     *
     * @return a {@code long} for JSON number
     * @throws ArithmeticException if the number has a nonzero fractional
     *         part, or will not fit in a {@code long}.
     * @see java.math.BigDecimal#longValueExact()
     */
    public long  getLongValueExact();

    /**
     * Converts JSON number to a {@code BigInteger} number. Note that this
     * conversion can lose information about the overall magnitude and
     * precision of the number value as well as return a result with the
     * opposite sign.
     *
     * @return a BigInteger for JSON number.
     * @see java.math.BigDecimal#toBigInteger()
     */
    public BigInteger getBigIntegerValue();

    /**
     * Converts JSON number to a {@code BigDecimal} number.
     *
     * @return a BigInteger for JSON number
     * @throws ArithmeticException if the number has a nonzero fractional part.
     * @see java.math.BigDecimal#toBigIntegerExact()
     */
    public BigInteger getBigIntegerValueExact();

    /**
     * Converts JSON number to a double number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return a {@code double} for JSON number
     * @see java.math.BigDecimal#doubleValue()
     */
    public double getDoubleValue();

    /**
     * Converts JSON number to a {@code BigDecimal}
     *
     * @return a BigDecimal for JSON number
     */
    public BigDecimal getBigDecimalValue();

    /**
     * Returns a JSON representation of the JSON number value.
     *
     * @return JSON representation of the number
     */
    @Override
    public String toString();

}
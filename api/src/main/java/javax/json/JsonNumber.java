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
 * <code>JsonNumber</code> represents a JSON number value.
 *
 * <p>TODO Should we just use Integer|Long|BigInteger|Double|BigDecimal
 * instead of this wrapper type for performance reasons
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
         * BigInteger number type
         */
        BIG_INTEGER,

        /**
         * double number type
         */
        DOUBLE,

        /**
         * BigDecimal number type
         */
        BIG_DECIMAL
    }

    /**
     * Returns a JSON number type
     *
     * @return a number type
     */
    public JsonNumberType getNumberType();

    /**
     * Converts JSON number to an integer number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return an integer for JSON number.
     */
    public int getIntValue();

    /**
     * Converts JSON number to an integer number.
     *
     * @return a number type
     * @throws ArithmeticException cause if the number has a nonzero fractional
     *         part, or will not fit in an {@code int}
     */
    public int getIntValueExact();

    /**
     * Converts JSON number to a long number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return a long for JSON number.
     */
    public long getLongValue();

    /**
     * Converts JSON number to a long number.
     *
     * @return a long for JSON number
     * @throws ArithmeticException if the number has a nonzero fractional
     *         part, or will not fit in a {@code long}.
     */
    public long  getLongValueExact();

    /**
     * Converts JSON number to a {@code BigInteger} number. Note that this
     * conversion can lose information about the overall magnitude and
     * precision of the number value as well as return a result with the
     * opposite sign.
     *
     * @return a long for JSON number.
     */
    public BigInteger getBigIntegerValue();

    /**
     * Converts JSON number to a {@code BigDecimal} number.
     *
     * @return a BigDecimal for JSON number
     * @throws ArithmeticException if the number has a nonzero fractional part.
     */
    public BigInteger getBigIntegerValueExact();

    /**
     * Converts JSON number to a double number. Note that this conversion
     * can lose information about the overall magnitude and precision of the
     * number value as well as return a result with the opposite sign.
     *
     * @return a long for JSON number
     */
    public double getDoubleValue();

    /**
     * Converts JSON number to a {@code BigDecimal}
     *
     * @return a BigDecimal for JSON number
     */
    public BigDecimal getBigDecimalValue();


}
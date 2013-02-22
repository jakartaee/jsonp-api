/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

final class JsonNumberImpl implements JsonNumber {
    private final BigDecimal bigDecimal;

    public JsonNumberImpl(int value) {
        bigDecimal = new BigDecimal(value);
    }

    public JsonNumberImpl(long value) {
        bigDecimal = new BigDecimal(value);
    }

    public JsonNumberImpl(BigInteger value) {
        bigDecimal = new BigDecimal(value);
    }

    public JsonNumberImpl(double value) {
        //bigDecimal = new BigDecimal(value);
        // This is the preferred way to convert double to BigDecimal
        bigDecimal = BigDecimal.valueOf(value);
    }

    public JsonNumberImpl(BigDecimal value) {
        this.bigDecimal = value;
    }

    @Override
    public boolean isIntegral() {
        return bigDecimal.scale() == 0;
    }

    @Override
    public int intValue() {
        return bigDecimal.intValue();
    }

    @Override
    public int intValueExact() {
        return bigDecimal.intValueExact();
    }

    @Override
    public long longValue() {
        return bigDecimal.longValue();
    }

    @Override
    public long longValueExact() {
        return bigDecimal.longValueExact();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return bigDecimal.toBigInteger();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return bigDecimal.toBigIntegerExact();
    }

    @Override
    public double doubleValue() {
        return bigDecimal.doubleValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return bigDecimal;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public int hashCode() {
        return bigDecimalValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JsonNumber)) {
            return false;
        }
        JsonNumber other = (JsonNumber)obj;
        return bigDecimalValue().equals(other.bigDecimalValue());
    }

    @Override
    public String toString() {
        return bigDecimal.toString();
    }

}


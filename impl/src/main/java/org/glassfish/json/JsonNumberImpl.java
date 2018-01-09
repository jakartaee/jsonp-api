/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.json;

import javax.json.JsonNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * JsonNumber impl. Subclasses provide optimized implementations
 * when backed by int, long, BigDecimal
 *
 * @author Jitendra Kotamraju
 */
abstract class JsonNumberImpl implements JsonNumber {

    static JsonNumber getJsonNumber(int num) {
        return new JsonIntNumber(num);
    }

    static JsonNumber getJsonNumber(long num) {
        return new JsonLongNumber(num);
    }

    static JsonNumber getJsonNumber(BigInteger value) {
        return new JsonBigDecimalNumber(new BigDecimal(value));
    }

    static JsonNumber getJsonNumber(double value) {
        //bigDecimal = new BigDecimal(value);
        // This is the preferred way to convert double to BigDecimal
        return new JsonBigDecimalNumber(BigDecimal.valueOf(value));
    }

    static JsonNumber getJsonNumber(BigDecimal value) {
        return new JsonBigDecimalNumber(value);
    }

    // Optimized JsonNumber impl for int numbers.
    private static final class JsonIntNumber extends JsonNumberImpl {
        private final int num;
        private BigDecimal bigDecimal;  // assigning it lazily on demand

        JsonIntNumber(int num) {
            this.num = num;
        }

        @Override
        public boolean isIntegral() {
            return true;
        }

        @Override
        public int intValue() {
            return num;
        }

        @Override
        public int intValueExact() {
            return num;
        }

        @Override
        public long longValue() {
            return num;
        }

        @Override
        public long longValueExact() {
            return num;
        }

        @Override
        public double doubleValue() {
            return num;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            // reference assignments are atomic. At the most some more temp
            // BigDecimal objects are created
            BigDecimal bd = bigDecimal;
            if (bd == null) {
                bigDecimal = bd = new BigDecimal(num);
            }
            return bd;
        }

        @Override
        public Number numberValue() {
            return num;
        }

        @Override
        public String toString() {
            return Integer.toString(num);
        }
    }

    // Optimized JsonNumber impl for long numbers.
    private static final class JsonLongNumber extends JsonNumberImpl {
        private final long num;
        private BigDecimal bigDecimal;  // assigning it lazily on demand

        JsonLongNumber(long num) {
            this.num = num;
        }

        @Override
        public boolean isIntegral() {
            return true;
        }

        @Override
        public int intValue() {
            return (int) num;
        }

        @Override
        public int intValueExact() {
            return Math.toIntExact(num);
        }

        @Override
        public long longValue() {
            return num;
        }

        @Override
        public long longValueExact() {
            return num;
        }

        @Override
        public double doubleValue() {
            return num;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            // reference assignments are atomic. At the most some more temp
            // BigDecimal objects are created
            BigDecimal bd = bigDecimal;
            if (bd == null) {
                bigDecimal = bd = new BigDecimal(num);
            }
            return bd;
        }

        @Override
        public Number numberValue() {
            return num;
        }

        @Override
        public String toString() {
            return Long.toString(num);
        }

    }

    // JsonNumber impl using BigDecimal numbers.
    private static final class JsonBigDecimalNumber extends JsonNumberImpl {
        private final BigDecimal bigDecimal;

        JsonBigDecimalNumber(BigDecimal value) {
            this.bigDecimal = value;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return bigDecimal;
        }

        @Override
        public Number numberValue() {
            return bigDecimalValue();
        }

    }

    @Override
    public boolean isIntegral() {
        return bigDecimalValue().scale() == 0;
    }

    @Override
    public int intValue() {
        return bigDecimalValue().intValue();
    }

    @Override
    public int intValueExact() {
        return bigDecimalValue().intValueExact();
    }

    @Override
    public long longValue() {
        return bigDecimalValue().longValue();
    }

    @Override
    public long longValueExact() {
        return bigDecimalValue().longValueExact();
    }

    @Override
    public double doubleValue() {
        return bigDecimalValue().doubleValue();
    }

    @Override
    public BigInteger bigIntegerValue() {
        return bigDecimalValue().toBigInteger();
    }

    @Override
    public BigInteger bigIntegerValueExact() {
        return bigDecimalValue().toBigIntegerExact();
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
        if (this == obj){
            return true;
        }
        if (!(obj instanceof JsonNumber)) {
            return false;
        }
        JsonNumber other = (JsonNumber)obj;
        return bigDecimalValue().equals(other.bigDecimalValue());
    }

    @Override
    public String toString() {
        return bigDecimalValue().toString();
    }

}


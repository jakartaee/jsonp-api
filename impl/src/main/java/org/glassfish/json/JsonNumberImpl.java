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
    public NumberType getNumberType() {
        return bigDecimal.scale() == 0 ? NumberType.INTEGER : NumberType.DECIMAL;
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


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
    public int getIntValue() {
        return bigDecimal.intValue();
    }

    @Override
    public int getIntValueExact() {
        return bigDecimal.intValueExact();
    }

    @Override
    public long getLongValue() {
        return bigDecimal.longValue();
    }

    @Override
    public long getLongValueExact() {
        return bigDecimal.longValueExact();
    }

    @Override
    public BigInteger getBigIntegerValue() {
        return bigDecimal.toBigInteger();
    }

    @Override
    public BigInteger getBigIntegerValueExact() {
        return bigDecimal.toBigIntegerExact();
    }

    @Override
    public double getDoubleValue() {
        return bigDecimal.doubleValue();
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        return bigDecimal;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.NUMBER;
    }

    @Override
    public int hashCode() {
        return getBigDecimalValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JsonNumber)) {
            return false;
        }
        JsonNumber other = (JsonNumber)obj;
        return getBigDecimalValue().equals(other.getBigDecimalValue());
    }

    @Override
    public String toString() {
        return bigDecimal.toString();
    }

}


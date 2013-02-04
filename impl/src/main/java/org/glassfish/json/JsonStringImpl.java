package org.glassfish.json;

import javax.json.JsonString;

final class JsonStringImpl implements JsonString {

    private final String value;

    public JsonStringImpl(String value) {
        this.value = value;
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public CharSequence getChars() {
        return value;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.STRING;
    }

    @Override
    public int hashCode() {
        return getStringValue().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JsonString)) {
            return false;
        }
        JsonString other = (JsonString)obj;
        return getStringValue().equals(other.getStringValue());
    }

    @Override
    public String toString() {
        return value;
    }
}


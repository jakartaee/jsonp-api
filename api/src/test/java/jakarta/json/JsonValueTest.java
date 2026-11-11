/*
 * Copyright (c) 2026 Eclipse Foundation and/or its affiliates. All rights reserved.
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

package jakarta.json;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import jakarta.json.JsonValue.ValueType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JsonValue} constants.
 */
class JsonValueTest {

    /**
     * Tests that {@link JsonValue#NULL}, {@link JsonValue#TRUE}, {@link JsonValue#FALSE},
     * {@link JsonValue#EMPTY_JSON_OBJECT} and {@link JsonValue#EMPTY_JSON_ARRAY} keep their
     * identity after Java serialization and deserialization.
     *
     * @throws Exception if serialization or deserialization fails
     */
    @Test
    void jsonValueConstantsSerializationTest() throws Exception {
        assertSame(JsonValue.NULL, serializeAndDeserialize(JsonValue.NULL));
        assertSame(JsonValue.TRUE, serializeAndDeserialize(JsonValue.TRUE));
        assertSame(JsonValue.FALSE, serializeAndDeserialize(JsonValue.FALSE));
        assertSame(JsonValue.EMPTY_JSON_OBJECT, serializeAndDeserialize(JsonValue.EMPTY_JSON_OBJECT));
        assertSame(JsonValue.EMPTY_JSON_ARRAY, serializeAndDeserialize(JsonValue.EMPTY_JSON_ARRAY));
    }

    /**
     * Fails when a new {@link ValueType} is added without deciding how
     * {@code JsonValueImpl.readResolve()} must handle it.
     * <p>
     * {@code NULL}, {@code TRUE} and {@code FALSE} are backed by {@code JsonValueImpl} singletons and
     * must resolve to the matching constants. The other types are never backed by {@code JsonValueImpl},
     * so a {@code JsonValueImpl} stream carrying one of them must be rejected. Any other type must be
     * added to this test and possibly to {@code JsonValueImpl.readResolve()}.
     *
     * @throws Exception if serialization or deserialization fails unexpectedly
     */
    @Test
    void jsonValueImplReadResolveCoversAllValueTypesTest() throws Exception {
        Map<ValueType, JsonValue> singletons = new EnumMap<>(ValueType.class);
        singletons.put(ValueType.NULL, JsonValue.NULL);
        singletons.put(ValueType.TRUE, JsonValue.TRUE);
        singletons.put(ValueType.FALSE, JsonValue.FALSE);
        Set<ValueType> notJsonValueImpl = EnumSet.of(ValueType.OBJECT, ValueType.ARRAY, ValueType.STRING, ValueType.NUMBER);

        for (ValueType type : ValueType.values()) {
            JsonValueImpl impl = new JsonValueImpl(type);
            if (singletons.containsKey(type)) {
                assertSame(singletons.get(type), serializeAndDeserialize(impl), "JsonValueImpl of type " + type);
            } else if (notJsonValueImpl.contains(type)) {
                assertThrows(InvalidObjectException.class, () -> serializeAndDeserialize(impl),
                        "JsonValueImpl of type " + type);
            } else {
                fail("New ValueType " + type + ". Add it to this test, and iff this type is implemented by JsonValueImpl, " +
                     "add to JsonValueImpl.readResolve() otherwise add to notJsonValueImpl.");
            }
        }
    }

    /**
     * Serializes and then deserializes the given value.
     *
     * @param value value to round-trip
     * @return the deserialized value
     * @throws Exception if serialization or deserialization fails
     */
    private static Object serializeAndDeserialize(Object value) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(value);
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
            return in.readObject();
        }
    }
}

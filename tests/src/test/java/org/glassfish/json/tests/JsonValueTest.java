/*
 * Copyright (c) 2016, 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Lukas Jungmann
 */
public class JsonValueTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetJsonObjectIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getJsonObject(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetJsonArrayIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getJsonArray(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetJsonNumberIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getJsonNumber(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetJsonStringIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getJsonString(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetStringIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getString(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetIntIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getInt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayGetBooleanIdx() {
        JsonValue.EMPTY_JSON_ARRAY.getBoolean(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void arrayIsNull() {
        JsonValue.EMPTY_JSON_ARRAY.isNull(0);
    }

    @Test
    public void arrayMethods() {
        Assert.assertEquals(JsonValue.ValueType.ARRAY, JsonValue.EMPTY_JSON_ARRAY.getValueType());
        Assert.assertEquals(Collections.<JsonObject>emptyList(), JsonValue.EMPTY_JSON_ARRAY.getValuesAs(JsonObject.class));
        Assert.assertEquals(Collections.<String>emptyList(), JsonValue.EMPTY_JSON_ARRAY.getValuesAs(JsonString::getString));
        Assert.assertEquals(true, JsonValue.EMPTY_JSON_ARRAY.getBoolean(0, true));
        Assert.assertEquals(42, JsonValue.EMPTY_JSON_ARRAY.getInt(0, 42));
        Assert.assertEquals("Sasek", JsonValue.EMPTY_JSON_ARRAY.getString(0, "Sasek"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void arrayIsImmutable() {
        JsonValue.EMPTY_JSON_ARRAY.add(JsonValue.EMPTY_JSON_OBJECT);
    }

    @Test(expected = NullPointerException.class)
    public void objectGetString() {
        JsonValue.EMPTY_JSON_OBJECT.getString("normalni string");
    }

    @Test(expected = NullPointerException.class)
    public void objectGetInt() {
        JsonValue.EMPTY_JSON_OBJECT.getInt("hledej cislo");
    }

    @Test(expected = NullPointerException.class)
    public void objectGetBoolean() {
        JsonValue.EMPTY_JSON_OBJECT.getBoolean("booo");
    }

    @Test(expected = NullPointerException.class)
    public void objectIsNull() {
        JsonValue.EMPTY_JSON_OBJECT.isNull("???");
    }

    @Test
    public void objectMethods() {
        Assert.assertNull(JsonValue.EMPTY_JSON_OBJECT.getJsonArray("pole"));
        Assert.assertNull(JsonValue.EMPTY_JSON_OBJECT.getJsonObject("objekt"));
        Assert.assertNull(JsonValue.EMPTY_JSON_OBJECT.getJsonNumber("cislo"));
        Assert.assertNull(JsonValue.EMPTY_JSON_OBJECT.getJsonString("divnej string"));
        
        Assert.assertEquals("ja jo", JsonValue.EMPTY_JSON_OBJECT.getString("nejsem tu", "ja jo"));
        Assert.assertEquals(false, JsonValue.EMPTY_JSON_OBJECT.getBoolean("najdes mne", false));
        Assert.assertEquals(98, JsonValue.EMPTY_JSON_OBJECT.getInt("spatnej dotaz", 98));
    }
    
    
    @Test(expected = UnsupportedOperationException.class)
    public void objectImmutable() {
        JsonValue.EMPTY_JSON_OBJECT.put("klauni", JsonValue.EMPTY_JSON_ARRAY);
    }

    @Test
    public void serialization() {
        byte[] data = serialize(JsonValue.TRUE);
        JsonValue value = deserialize(JsonValue.class, data);
        Assert.assertEquals(JsonValue.TRUE, value);

        data = serialize(JsonValue.FALSE);
        value = deserialize(JsonValue.class, data);
        Assert.assertEquals(JsonValue.FALSE, value);

        data = serialize(JsonValue.NULL);
        value = deserialize(JsonValue.class, data);
        Assert.assertEquals(JsonValue.NULL, value);

        data = serialize(JsonValue.EMPTY_JSON_ARRAY);
        value = deserialize(JsonValue.class, data);
        Assert.assertEquals(JsonValue.EMPTY_JSON_ARRAY, value);

        data = serialize(JsonValue.EMPTY_JSON_OBJECT);
        value = deserialize(JsonValue.class, data);
        Assert.assertEquals(JsonValue.EMPTY_JSON_OBJECT, value);
    }

    private byte[] serialize(Object o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return baos.toByteArray();
    }

    private <T> T deserialize(Class<T> type, byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

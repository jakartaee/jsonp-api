/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2016-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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
package org.glassfish.json.tests;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
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

/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
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

import org.glassfish.json.NumberStrategy;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberStrategyTest {


    @Test
    public void testJsonGeneratorString() {
        //Expect only types of Long and bigger to be serialized as JSON string
        String expectedJson = "{\"writeByteKey\":-128,\"writeByteKeyAndValue\":127,\"writeShortKey\":-32768,\"writeShortKeyAndValue\":32767,\"writeIntegerKey\":-2147483648,\"writeIntegerKeyAndValue\":2147483647,\"writeLongInRangeKey\":\"10\",\"writeLongInRangeKeyAndValue\":,\"10\",\"writeLongOutOfRangeKey\":\"9223372036854775807\",\"writeLongOutOfRangeKeyAndValue\":,\"9223372036854775807\",\"writeBigDecimalInRangeKey\":\"10\",\"writeBigDecimalInRangeKeyAndValue\":,\"10\",\"writeBigDecimalOutOfRangeKey\":\"9223372036854775807\",\"writeBigDecimalOutOfRangeKeyAndValue\":,\"9223372036854775807\",\"writeBigIntegerInRangeKey\":\"10\",\"writeBigIntegerInRangeKeyAndValue\":,\"10\",\"writeBigIntegerOutOfRangeKey\":\"9223372036854775807\",\"writeBigIntegerOutOfRangeKeyAndValue\":,\"9223372036854775807\"}";
        testJsonGenerator(expectedJson, NumberStrategy.JSON_STRING);
    }

    @Test
    public void testJsonGeneratorNumber() {
        //Expect all values to be serialized as JSON number
        String expectedJson = "{\"writeByteKey\":-128,\"writeByteKeyAndValue\":127,\"writeShortKey\":-32768,\"writeShortKeyAndValue\":32767,\"writeIntegerKey\":-2147483648,\"writeIntegerKeyAndValue\":2147483647,\"writeLongInRangeKey\":10,\"writeLongInRangeKeyAndValue\":,10,\"writeLongOutOfRangeKey\":9223372036854775807,\"writeLongOutOfRangeKeyAndValue\":,9223372036854775807,\"writeBigDecimalInRangeKey\":10,\"writeBigDecimalInRangeKeyAndValue\":,10,\"writeBigDecimalOutOfRangeKey\":9223372036854775807,\"writeBigDecimalOutOfRangeKeyAndValue\":,9223372036854775807,\"writeBigIntegerInRangeKey\":10,\"writeBigIntegerInRangeKeyAndValue\":,10,\"writeBigIntegerOutOfRangeKey\":9223372036854775807,\"writeBigIntegerOutOfRangeKeyAndValue\":,9223372036854775807}";
        testJsonGenerator(expectedJson, NumberStrategy.JSON_NUMBER);
    }

    private void testJsonGenerator(String expectedJson, String numberStrategy) {
        Map<String, Object> config = new HashMap<>();
        config.put(NumberStrategy.class.getName(), numberStrategy);
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(config);
        StringWriter writer = new StringWriter();
        JsonGenerator generator = generatorFactory.createGenerator(writer);

        generator.writeStartObject();
        generator.writeKey("writeByteKey");
        generator.write(Byte.MIN_VALUE);
        generator.write("writeByteKeyAndValue", Byte.MAX_VALUE);

        generator.writeKey("writeShortKey");
        generator.write(Short.MIN_VALUE);
        generator.write("writeShortKeyAndValue", Short.MAX_VALUE);

        generator.writeKey("writeIntegerKey");
        generator.write(Integer.MIN_VALUE);
        generator.write("writeIntegerKeyAndValue", Integer.MAX_VALUE);

        generator.writeKey("writeLongInRangeKey");
        generator.write(10L);
        generator.write("writeLongInRangeKeyAndValue", 10L);

        generator.writeKey("writeLongOutOfRangeKey");
        generator.write(Long.MAX_VALUE);
        generator.write("writeLongOutOfRangeKeyAndValue", Long.MAX_VALUE);

        generator.writeKey("writeBigDecimalInRangeKey");
        generator.write(new BigDecimal(String.valueOf(10L)));
        generator.write("writeBigDecimalInRangeKeyAndValue", new BigDecimal(String.valueOf(10L)));

        generator.writeKey("writeBigDecimalOutOfRangeKey");
        generator.write(new BigDecimal(String.valueOf(Long.MAX_VALUE)));
        generator.write("writeBigDecimalOutOfRangeKeyAndValue", new BigDecimal(String.valueOf(Long.MAX_VALUE)));

        generator.writeKey("writeBigIntegerInRangeKey");
        generator.write(new BigInteger(String.valueOf(10L)));
        generator.write("writeBigIntegerInRangeKeyAndValue", new BigInteger(String.valueOf(10L)));

        generator.writeKey("writeBigIntegerOutOfRangeKey");
        generator.write(new BigInteger(String.valueOf(Long.MAX_VALUE)));
        generator.write("writeBigIntegerOutOfRangeKeyAndValue", new BigInteger(String.valueOf(Long.MAX_VALUE)));

        generator.writeEnd();

        generator.flush();
        generator.close();

        assertEquals(expectedJson, writer.toString());

    }

    @Test
    public void testJsonParser() {
        //parses Long and BigDecimal numbers both quoted and unquoted no matter of strategy.
        //Unfortunately opposed to writeBigInteger there is no getBigInteger in the API
        String json = "{\"longUnquoted\":10,\"longQuoted\":\"9223372036854775807\",\"bigDecimalUnquoted\":10,\"bigDecimalQuoted\":\"9223372036854775807\"}";
        Map<String, Object> config = new HashMap<>();
        JsonParserFactory parserFactory = Json.createParserFactory(config);
        JsonParser parser = parserFactory.createParser(new StringReader(json));

        parser.next(); //start object
        parser.next(); //longUnquoted
        parser.next();
        assertEquals(10L, parser.getLong());
        parser.next(); //longQuoted
        parser.next();
        assertEquals(9223372036854775807L, parser.getLong());
        parser.next(); //bigDecimalUnquoted
        parser.next();
        assertEquals(new BigDecimal("10"), parser.getBigDecimal());
        parser.next(); //bigDecimalQuoted
        parser.next();
        assertEquals(new BigDecimal("9223372036854775807"), parser.getBigDecimal());
        parser.close();
    }

    @Test
    public void testJsonReader() {
        //Json reader actually doesn't have any clue of reading number stored in json string
        String json = "{\"longUnquoted\":10,\"longQuoted\":\"9223372036854775807\"}";

        Map<String, Object> config = new HashMap<>();
        JsonReaderFactory parserFactory = Json.createReaderFactory(config);
        JsonReader reader = parserFactory.createReader(new StringReader(json));

        JsonObject obj = reader.readObject();
        assertEquals(10L, obj.getJsonNumber("longUnquoted").longValue());
        assertEquals("9223372036854775807", obj.getJsonString("longQuoted").getString());
    }

    @Test
    public void jsonWriterJsonStringTest() {
        //Expect only types of Long and bigger to be serialized as JSON string
        String expectedJson = "{\"writeByteKeyAndValue\":127,\"writeShortKeyAndValue\":32767,\"writeIntegerKeyAndValue\":2147483647,\"writeLongInRangeKeyAndValue\":10,\"writeLongOutOfRangeKeyAndValue\":9223372036854775807,\"writeBigDecimalInRangeKeyAndValue\":10,\"writeBigDecimalOutOfRangeKeyAndValue\":9223372036854775807,\"writeBigIntegerInRangeKeyAndValue\":10,\"writeBigIntegerOutOfRangeKeyAndValue\":9223372036854775807}";
        testJsonWriter(NumberStrategy.JSON_STRING, expectedJson);
    }

    @Test
    public void jsonWriterJsonNumberTest() {
        //Expect all values to be serialized as JSON number
        String expectedJson = "{\"writeByteKeyAndValue\":127,\"writeShortKeyAndValue\":32767,\"writeIntegerKeyAndValue\":2147483647,\"writeLongInRangeKeyAndValue\":10,\"writeLongOutOfRangeKeyAndValue\":9223372036854775807,\"writeBigDecimalInRangeKeyAndValue\":10,\"writeBigDecimalOutOfRangeKeyAndValue\":9223372036854775807,\"writeBigIntegerInRangeKeyAndValue\":10,\"writeBigIntegerOutOfRangeKeyAndValue\":9223372036854775807}";
        testJsonWriter(NumberStrategy.JSON_NUMBER, expectedJson);
    }

    private void testJsonWriter(String numberStrategy, String expectedJson) {
        Map<String, Object> config = new HashMap<>();
        config.put(NumberStrategy.class.getName(), numberStrategy);
        JsonWriterFactory writerFactory = Json.createWriterFactory(config);
        StringWriter writer = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(writer);

        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        objectBuilder.add("writeByteKeyAndValue", Byte.MAX_VALUE);
        objectBuilder.add("writeShortKeyAndValue", Short.MAX_VALUE);
        objectBuilder.add("writeIntegerKeyAndValue", Integer.MAX_VALUE);
        objectBuilder.add("writeLongInRangeKeyAndValue", 10L);
        objectBuilder.add("writeLongOutOfRangeKeyAndValue", Long.MAX_VALUE);
        objectBuilder.add("writeBigDecimalInRangeKeyAndValue", new BigDecimal(String.valueOf(10L)));
        objectBuilder.add("writeBigDecimalOutOfRangeKeyAndValue", new BigDecimal(String.valueOf(Long.MAX_VALUE)));
        objectBuilder.add("writeBigIntegerInRangeKeyAndValue", new BigInteger(String.valueOf(10L)));
        objectBuilder.add("writeBigIntegerOutOfRangeKeyAndValue", new BigInteger(String.valueOf(Long.MAX_VALUE)));

        JsonObject jsonObject = objectBuilder.build();
        jsonWriter.write(jsonObject);
        jsonWriter.close();

        assertEquals(expectedJson, writer.toString());
    }
}

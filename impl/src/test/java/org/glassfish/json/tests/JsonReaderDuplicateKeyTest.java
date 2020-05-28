package org.glassfish.json.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Collections;

import org.junit.Test;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonReaderFactory;
import jakarta.json.stream.JsonParsingException;

public class JsonReaderDuplicateKeyTest {
	@Test
	public void testJsonReaderDuplicateKey1() {
		String json = "{\"a\":\"b\",\"a\":\"c\"}";
		JsonReader jsonReader = Json.createReader(new StringReader(json));
		JsonObject jsonObject = jsonReader.readObject();
		assertEquals(jsonObject.getString("a"), "c");
	}
	
	@Test
	public void testJsonReaderDuplicateKey2() {
		String json = "{\"a\":\"b\",\"a\":\"c\"}";
		JsonReaderFactory jsonReaderFactory = Json.createReaderFactory(Collections.singletonMap("jakarta.json.JsonReader.allowDuplicateKeys", false));
		JsonReader jsonReader = jsonReaderFactory.createReader(new StringReader(json));
		try {
			jsonReader.readObject();
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof JsonParsingException);
			assertEquals("Duplicate key 'a'", e.getMessage());
		}
	}
}

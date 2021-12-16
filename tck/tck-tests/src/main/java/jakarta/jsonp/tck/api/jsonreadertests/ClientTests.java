/*
 * Copyright (c) 2020, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.jsonp.tck.api.jsonreadertests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import jakarta.json.*;
import jakarta.json.stream.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// $Id$
public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* Utility Methods */

  /*
   * compareJsonObjectForUTFEncodedTests
   */
  private boolean compareJsonObjectForUTFEncodedTests(JsonObject jsonObject) {
    boolean pass = true;
    LOGGER.info("Comparing JsonObject values to expected results.");
    String expString = "stringValue";
    String actString = jsonObject.getJsonString("stringName").getString();
    if (!JSONP_Util.assertEquals(expString, actString))
      pass = false;
    JsonObject actObject = jsonObject.getJsonObject("objectName");
    expString = "bar";
    actString = actObject.getJsonString("foo").getString();
    if (!JSONP_Util.assertEquals(expString, actString))
      pass = false;
    JsonArray actArray = jsonObject.getJsonArray("arrayName");
    if (!JSONP_Util.assertEquals(1, actArray.getJsonNumber(0).intValue())
        || !JSONP_Util.assertEquals(2, actArray.getJsonNumber(1).intValue())
        || !JSONP_Util.assertEquals(3, actArray.getJsonNumber(2).intValue()))
      pass = false;
    return pass;
  }

  /* Tests */

  /*
   * @testName: readEmptyArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an empty array "[]" from stream. Use
   * JsonReader.readArray() API call.
   *
   */
  @Test
  public void readEmptyArrayTest() {
    JsonReader reader = null;
    try {
      String expJsonText = "[]";
      LOGGER.info("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonArray array = reader.readArray();
      assertTrue(JSONP_Util.assertEqualsEmptyArrayList(array), "readEmptyArrayTest Failed");
    } catch (Exception e) {
      fail("readEmptyArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readEscapeCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array from a resource file with special
   * chars in data. Use JsonReader.readArray() API call. Test scenario: Read
   * string of JSON text containing a JSON array from resource file with
   * following data: [ "popeye\"\\\/\b\f\n\r\tolive" ]
   *
   * These characters are backslash escape'd as follows: \" \\ \/ \b \f \n \r \t
   *
   * Create a JsonWriter to write above JsonArray to a string of JSON text.
   * Re-read JsonWriter text back into a JsonArray Compare expected JSON array
   * with actual JSON array for equality.
   *
   */
  @Test
  public void readEscapeCharsInArrayTest() {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithEscapeCharsData.json";
    String expString = "popeye" + JSONP_Data.escapeCharsAsString + "olive";
    try {

      LOGGER.info("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      LOGGER.info("readerContents=" + readerContents);

      LOGGER.info("Testing read of resource contents: " + readerContents);
      reader = Json.createReader(new StringReader(readerContents));
      JsonArray expJsonArray = reader.readArray();

      LOGGER.info("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      LOGGER.info("Comparing JsonArray values with expected results.");
      String actString = expJsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;

      LOGGER.info("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Create actJsonArray from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      LOGGER.info("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      LOGGER.info("Compare expJsonArray and actJsonArray for equality");
      if (!JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray))
        pass = false;
    } catch (Exception e) {
      fail("readEscapeCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    assertTrue(pass, "readEscapeCharsInArrayTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array with unicode chars escaped and not
   * escaped. Use JsonReader.readArray() API call. Test scenario: Read string of
   * JSON text containing a JSON array with the following data: [
   * "\\u0000\u00ff\\uff00\uffff" ]
   *
   * Notice unicode \u0000 and \uff00 is escaped but \u00ff and \uffff is not.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  @Test
  public void readEscapeUnicodeCharsInArrayTest() {
    JsonReader reader = null;
    String unicodeTextString = "[\"\\u0000\u00ff\\uff00\uffff\"]";
    String expResult = "\u0000\u00ff\uff00\uffff";
    try {
      LOGGER.info("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonArray array = reader.readArray();
      String actResult = array.getJsonString(0).getString();
      assertTrue(JSONP_Util.assertEquals(expResult, actResult), "readEscapeUnicodeCharsInArrayTest Failed");
    } catch (Exception e) {
      fail("readEscapeUnicodeCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readEscapeUnicodeControlCharsInArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array with unicode control chars escaped.
   * Use JsonReader.readArray() API call. Test scenario: Read string of JSON
   * text containing unicode control chars escaped as a Json Array.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  @Test
  public void readEscapeUnicodeControlCharsInArrayTest() {
    JsonReader reader = null;
    String unicodeTextString = "[\"" + JSONP_Data.unicodeControlCharsEscaped
        + "\"]";
    String expResult = JSONP_Data.unicodeControlCharsNonEscaped;
    try {
      LOGGER.info("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonArray array = reader.readArray();
      String actResult = array.getJsonString(0).getString();
      assertTrue(JSONP_Util.assertEquals(expResult, actResult), "readEscapeUnicodeControlCharsInArrayTest Failed");
    } catch (Exception e) {
      fail("readEscapeUnicodeControlCharsInArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readEmptyObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an empty object "{}" from stream. Use
   * JsonReader.readObject() API call.
   *
   */
  @Test
  public void readEmptyObjectTest() {
    JsonReader reader = null;
    try {
      String expJsonText = "{}";
      LOGGER.info("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonObject object = reader.readObject();
      assertTrue(JSONP_Util.assertEqualsEmptyObjectMap(object), "readEmptyObjectTest Failed");
    } catch (Exception e) {
      fail("readEmptyObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readEscapeCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object from a resource file with special
   * chars in data. Use JsonReader.readObject() API call. Test scenario: Read
   * string of JSON text containing a JSON object from resource file with
   * following data: { "specialChars" : "popeye\"\\\/\b\f\n\r\tolive" }
   *
   * These characters are backslash escape'd as follows: \" \\ \/ \b \f \n \r \t
   *
   * Create a JsonWriter to write above JsonObject to a string of JSON text.
   * Re-read JsonWriter text back into a JsonObject Compare expected JSON object
   * with actual JSON object for equality.
   *
   */
  @Test
  public void readEscapeCharsInObjectTest() {
    boolean pass = true;
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithEscapeCharsData.json";
    String expString = "popeye" + JSONP_Data.escapeCharsAsString + "olive";
    try {

      LOGGER.info("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      LOGGER.info("readerContents=" + readerContents);

      LOGGER.info("Testing read of resource contents: " + readerContents);
      reader = Json.createReader(new StringReader(readerContents));
      JsonObject expJsonObject = reader.readObject();

      LOGGER.info("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      LOGGER.info("Comparing JsonArray values with expected results.");
      String actString = expJsonObject.getJsonString("escapeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;

      LOGGER.info("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Create actJsonObject from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonObject actJsonObject = reader.readObject();

      LOGGER.info("Dump of actJsonObject");
      JSONP_Util.dumpJsonObject(actJsonObject);

      LOGGER.info("Compare expJsonObject and actJsonObject for equality");
      if (!JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject))
        pass = false;
    } catch (Exception e) {
      fail("readEscapeCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    assertTrue(pass, "readEscapeCharsInObjectTest Failed");
  }

  /*
   * @testName: readEscapeUnicodeCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object with unicode chars escaped and not
   * escaped. Use JsonReader.readObject() API call. Test scenario: Read string
   * of JSON text containing a JSON object with the following data: {
   * "unicodechars":"\\u0000\u00ff\\uff00\uffff" ]
   *
   * Notice unicode \u0000 and \uff00 is escaped but \u00ff and \uffff is not.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  @Test
  public void readEscapeUnicodeCharsInObjectTest() {
    JsonReader reader = null;
    String unicodeTextString = "{\"unicodechars\":\"\\u0000\u00ff\\uff00\uffff\"}";
    String expResult = "\u0000\u00ff\uff00\uffff";
    try {
      LOGGER.info("Reading object of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonObject object = reader.readObject();
      String actResult = object.getJsonString("unicodechars").getString();
      assertTrue(JSONP_Util.assertEquals(expResult, actResult), "readEscapeUnicodeCharsInObjectTest Failed");
    } catch (Exception e) {
      fail("readEscapeUnicodeCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readEscapeUnicodeControlCharsInObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an array with unicode control chars escaped.
   * Use JsonReader.readObject() API call. Test scenario: Read string of JSON
   * text containing unicode control chars escaped as a Json Object.
   *
   * Compare expected JSON String with actual JSON String for equality.
   *
   */
  @Test
  public void readEscapeUnicodeControlCharsInObjectTest() {
    JsonReader reader = null;
    String unicodeTextString = "{\"unicodechars\":\""
        + JSONP_Data.unicodeControlCharsEscaped + "\"}";
    String expResult = JSONP_Data.unicodeControlCharsNonEscaped;
    try {
      LOGGER.info("Reading array of escaped and non escaped unicode chars.");
      reader = Json.createReader(new StringReader(unicodeTextString));
      JsonObject object = reader.readObject();
      String actResult = object.getJsonString("unicodechars").getString();
      assertTrue(JSONP_Util.assertEquals(expResult, actResult), "readEscapeUnicodeControlCharsInObjectTest Failed");
    } catch (Exception e) {
      fail("readEscapeUnicodeControlCharsInObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807, 1.7976931348623157E308,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1,"double":10.4}
   * ] Test scenario: Read string of JSON text above consisting of a JSON array
   * into a JsonArray object. Create an expected List of JsonArray values for
   * use in test comparison. Compare expected list of JsonArray values with
   * actual list for equality.
   *
   */
  @Test
  public void readArrayTest() {
    JsonReader reader = null;
    try {
      String jsonText = "[true,false,null,\"booyah\",2147483647,9223372036854775807,1.7976931348623157E308,"
          + "[true,false,null,\"bingo\",-2147483648,-9223372036854775808,4.9E-324],"
          + "{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1,"
          + "\"double\":10.4}]";

      LOGGER.info("Create the expected list of JsonArray values");
      List<JsonValue> expList = new ArrayList<>();
      expList.add(JsonValue.TRUE);
      expList.add(JsonValue.FALSE);
      expList.add(JsonValue.NULL);
      expList.add(JSONP_Util.createJsonString("booyah"));
      expList.add(JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      JsonArray array = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
          .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE)
          .build();
      JsonObject object = Json.createObjectBuilder().add("true", JsonValue.TRUE)
          .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
          .add("bonga", "boo").add("int", 1).add("double", 10.4).build();
      expList.add(array);
      expList.add(object);

      LOGGER.info("Testing read of " + jsonText);
      reader = Json.createReader(new StringReader(jsonText));
      JsonArray myJsonArray = reader.readArray();

      List<JsonValue> actList = myJsonArray;
      LOGGER.info(
          "Compare actual list of JsonArray values with expected list of JsonArray values");
      assertTrue(JSONP_Util.assertEqualsList(expList, actList), "readArrayTest Failed");
    } catch (Exception e) {
      fail("readArrayTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:178;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807, 1.7976931348623157E308,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1,"double":10.4}
   * ] Test Scenario: Create an expected JsonArray of the above JSON array for
   * use in test comparison. Create a JsonWriter to write the above JsonArray to
   * a string of JSON text. Next call JsonReader to read the JSON text from the
   * JsonWriter to a JsonArray object. Compare expected JsonArray object with
   * actual JsonArray object for equality.
   *
   */
  @Test
  public void readArrayTest2() {
    JsonReader reader = null;
    try {
      LOGGER.info("Create the expected list of JsonArray values");
      JsonArray expJsonArray = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("booyah")
          .add(Integer.MAX_VALUE).add(Long.MAX_VALUE).add(Double.MAX_VALUE)
          .add(Json.createArrayBuilder().add(JsonValue.TRUE)
              .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
              .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE))
          .add(Json.createObjectBuilder().add("true", JsonValue.TRUE)
              .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
              .add("bonga", "boo").add("int", 1).add("double", 10.4))
          .build();

      LOGGER.info("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String jsonText = sWriter.toString();

      LOGGER.info("Dump contents of JsonWriter as a String");
      LOGGER.info("JsonWriterContents=" + jsonText);

      LOGGER.info("Testing read of " + jsonText);
      reader = Json.createReader(JSONP_Util.getInputStreamFromString(jsonText));
      JsonArray actJsonArray = reader.readArray();

      LOGGER.info("Compare expJsonArray and actJsonArray for equality");
      assertTrue(JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray), "readArrayTest2 Failed");
    } catch (Exception e) {
      fail("readArrayTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:449; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:419;
   * 
   * @test_Strategy: Test read of an array containing various types from stream.
   * Use JsonReader.readArray() API call. [true, false, null, "booyah",
   * 2147483647, 9223372036854775807,
   * [true,false,null,"bingo",-2147483648,-9223372036854775808],
   * {"true":true,"false":false,"null":null,"bonga":"boo","int":1} ] Test
   * scenario: Read string of JSON text above consisting of a JSON array into a
   * JsonArray object with an empty configuration. Create a JsonWriter to write
   * the above JsonArray to a string of JSON text. Compare expected JSON text
   * with actual JSON text for equality.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(Reader) JsonArray
   * array = JsonReader.readArray()
   *
   */
  @Test
  public void readArrayTest3() {
    JsonReader reader = null;
    try {
      String expJsonText = "[true,false,null,\"booyah\",2147483647,9223372036854775807,"
          + "[true,false,null,\"bingo\",-2147483648,-9223372036854775808],"
          + "{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1}]";

      LOGGER.info("Testing read of " + expJsonText);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(new StringReader(expJsonText));
      JsonArray myJsonArray = reader.readArray();

      LOGGER.info("Write the JsonArray 'myJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(myJsonArray);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String actJsonText = sWriter.toString();

      LOGGER.info("Compare actual JSON text with expected JSON text");
      assertTrue(JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText), "readArrayTest3 Failed");
    } catch (Exception e) {
      fail("readArrayTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an array from a resource file with various
   * amounts of data. Use JsonReader.readArray() API call. Test scenario: Read
   * InputStream of JSON text containing a JSON array from resource file with
   * various amounts of data use UTF-8 encoding. Create a JsonWriter to write
   * above JsonArray to a string of JSON text. Re-read JsonWriter text back into
   * a JsonArray Compare expected JSON array with actual JSON array for
   * equality.
   *
   */
  @Test
  public void readArrayTest4() {
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithAllTypesOfData.json";
    try {
      LOGGER.info(
          "Read contents of InputStream from resource file: " + resourceFile);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(
          JSONP_Util.getInputStreamFromResource(resourceFile),
          JSONP_Util.UTF_8);
      JsonArray expJsonArray = reader.readArray();

      LOGGER.info("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      LOGGER.info("Write the JsonArray 'expJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(expJsonArray);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Create actJsonArray from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      LOGGER.info("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      LOGGER.info("Compare expJsonArray and actJsonArray for equality");
      assertTrue(JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray), "readArrayTest4 Failed");
    } catch (Exception e) {
      fail("readArrayTest4 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:181;
   * JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an array from a resource file with lots of
   * nesting. Use JsonReader.read() API call. Test scenario: Read InputStream of
   * JSON text containing a JSON array from resource file with lots of nesting
   * use UTF-8 encoding with empty configuration. Create a JsonWriter to write
   * above JsonArray to a string of JSON text. Compare expected JSON text with
   * actual JSON text for equality. Filter all text output to remove whitespace
   * before comparison.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   * JsonArray array = (JsonArray)JsonReader.read()
   *
   *
   */
  @Test
  public void readArrayTest5() {
    JsonReader reader = null;
    String resourceFile = "jsonArrayWithLotsOfNestedObjectsData.json";
    try {
      LOGGER.info("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      LOGGER.info("readerContents=" + readerContents);

      // Create expected JSON text from resource contents filtered of whitespace
      // for comparison
      LOGGER.info("Filter readerContents of whitespace for comparison");
      String expJsonText = JSONP_Util.removeWhitespace(readerContents);

      LOGGER.info(
          "Read contents of InputStream from resource file: " + resourceFile);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(JSONP_Util.getInputStreamFromResource(resourceFile),
              JSONP_Util.UTF_8);
      JsonArray myJsonArray = (JsonArray) reader.read();

      LOGGER.info("Write the JsonArray 'myJsonArray' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeArray(myJsonArray);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Dump contents of the JsonWriter as a String");
      LOGGER.info("writerContents=" + writerContents);

      LOGGER.info("Filter writerContents of whitespace for comparison");
      String actJsonText = JSONP_Util.removeWhitespace(writerContents);

      LOGGER.info("Compare actual JSON text with expected JSON text");
      assertTrue(JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText), "readArrayTest5 Failed");
    } catch (Exception e) {
      fail("readArrayTest5 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readArrayEncodingTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test read of a JsonArray from a resource file using both
   * encodings of UTF-8 and UTF-16BE.
   * 
   * Test scenario: For each encoding read the appropriate resource file
   * containing a string value. Call JsonArray.getJsonString() to get the value
   * of the JsonString. Compare expected string with actual string for equality.
   */
  @Test
  public void readArrayEncodingTest() {
    boolean pass = true;
    JsonReader reader = null;
    String expString = "a\u65e8\u452c\u8b9e\u6589\u5c57\u5217z";
    String resourceFileUTF8 = "jsonArrayUTF8.json";
    String resourceFileUTF16BE = "jsonArrayUTF16BE.json";
    Map<String, ?> config = JSONP_Util.getEmptyConfig();
    try {
      LOGGER.info("Reading contents of resource file using UTF-8 encoding "
          + resourceFileUTF8);
      InputStream is = JSONP_Util.getInputStreamFromResource(resourceFileUTF8);
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonArray jsonArray = reader.readArray();
      LOGGER.info("Comparing JsonArray values with expected results.");
      String actString = jsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      fail("readArrayEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    try {
      LOGGER.info("Reading contents of resource file using UTF-16BE encoding "
          + resourceFileUTF16BE);
      InputStream is = JSONP_Util
          .getInputStreamFromResource(resourceFileUTF16BE);
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16BE);
      JsonArray jsonArray = reader.readArray();
      LOGGER.info("Comparing JsonArray values with expected results.");
      String actString = jsonArray.getJsonString(0).getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      fail("readArrayEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    assertTrue(pass, "readArrayEncodingTest Failed");
  }

  /*
   * @testName: readObjectTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807, "double":1.7976931348623157E308,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324]
   * , "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1,
   * "double":10.4} } Test scenario: Read string of JSON text above consisting
   * of a JSON object into a JsonObject object. Create an expected map of
   * JsonObject values for use in test comparison. Compare expected map of
   * JsonObject values with actual map for equality.
   *
   */
  @Test
  public void readObjectTest() {
    JsonReader reader = null;
    try {
      String expJsonText = "{\"true\":true,\"false\":false,\"null\":null,\"booyah\":\"booyah\",\"int\":2147483647,"
          + "\"long\":9223372036854775807,\"double\":1.7976931348623157E308,"
          + "\"array\":[true,false,null,\"bingo\",-2147483648,-9223372036854775808,4.9E-324],"
          + "\"object\":{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1,"
          + "\"double\":10.4}}";

      LOGGER.info("Create the expected map of JsonObject values");
      Map<String, JsonValue> expMap = new HashMap<>();
      expMap.put("true", JsonValue.TRUE);
      expMap.put("false", JsonValue.FALSE);
      expMap.put("null", JsonValue.NULL);
      expMap.put("booyah", JSONP_Util.createJsonString("booyah"));
      expMap.put("int", JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expMap.put("long", JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expMap.put("double", JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      JsonArray array = Json.createArrayBuilder().add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL).add("bingo")
          .add(Integer.MIN_VALUE).add(Long.MIN_VALUE).add(Double.MIN_VALUE)
          .build();
      JsonObject object = Json.createObjectBuilder().add("true", JsonValue.TRUE)
          .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
          .add("bonga", "boo").add("int", 1).add("double", 10.4).build();
      expMap.put("array", array);
      expMap.put("object", object);

      LOGGER.info("Testing read of " + expJsonText);
      reader = Json.createReader(new StringReader(expJsonText));
      JsonObject myJsonObject = reader.readObject();

      Map<String, JsonValue> actMap = myJsonObject;
      LOGGER.info(
          "Compare actual map of JsonObject values with expected map of JsonObject values");
      assertTrue(JSONP_Util.assertEqualsMap(expMap, actMap), "readObjectTest Failed");
    } catch (Exception e) {
      fail("readObjectTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readObjectTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:178; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807, "double":1.7976931348623157E308,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808,4.9E-324]
   * , "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1,
   * "double":10.4} } Test Scenario: Create an expected JsonObject of the above
   * JSON object for use in test comparison. Create a JsonWriter to write the
   * above JsonObject to a string of JSON text. Next call JsonReader to read the
   * JSON text from the JsonWriter to a JsonObject object. Compare expected
   * JsonObject object with actual JsonObject object for equality.
   *
   */
  @Test
  public void readObjectTest2() {
    JsonReader reader = null;
    try {
      LOGGER.info("Create the expected list of JsonObject values");
      JsonObject expJsonObject = Json.createObjectBuilder()
          .add("true", JsonValue.TRUE).add("false", JsonValue.FALSE)
          .add("null", JsonValue.NULL).add("booyah", "booyah")
          .add("int", Integer.MAX_VALUE).add("long", Long.MAX_VALUE)
          .add("double", Double.MAX_VALUE)
          .add("array",
              Json.createArrayBuilder().add(JsonValue.TRUE).add(JsonValue.FALSE)
                  .add(JsonValue.NULL).add("bingo").add(Integer.MIN_VALUE)
                  .add(Long.MIN_VALUE).add(Double.MIN_VALUE))
          .add("object",
              Json.createObjectBuilder().add("true", JsonValue.TRUE)
                  .add("false", JsonValue.FALSE).add("null", JsonValue.NULL)
                  .add("bonga", "boo").add("int", 1).add("double", 10.4))
          .build();

      LOGGER.info("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String jsonText = sWriter.toString();

      LOGGER.info("Dump contents of JsonWriter as a String");
      LOGGER.info("JsonWriterContents=" + jsonText);

      LOGGER.info("Testing read of " + jsonText);
      reader = Json.createReader(JSONP_Util.getInputStreamFromString(jsonText));
      JsonObject actJsonObject = reader.readObject();

      LOGGER.info("Compare expJsonObject and actJsonObject for equality");
      assertTrue(JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject), "readObjectTest2 Failed");
    } catch (Exception e) {
      fail("readObjectTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readObjectTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:185; JSONP:JAVADOC:419;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object containing various types from
   * stream. Use JsonReader.readObject() API call. {"true":true, "false":false,
   * "null":null, "booyah":"booyah", "int":2147483647,
   * "long":9223372036854775807,
   * "array":[true,false,null,"bingo",-2147483648,-9223372036854775808],
   * "object":{"true":true,"false":false,"null":null,"bonga":"boo","int":1} }
   * Test scenario: Read string of JSON text above consisting of a JSON object
   * into a JsonObject object with an empty configuration. Create a JsonWriter
   * to write the above JsonObject to a string of JSON text. Compare expected
   * JSON text with actual JSON text for equality.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(Reader) JsonObject
   * object = JsonReader.readObject()
   *
   *
   */
  @Test
  public void readObjectTest3() {
    JsonReader reader = null;
    try {
      String expJsonText = "{\"true\":true,\"false\":false,\"null\":null,\"booyah\":\"booyah\",\"int\":2147483647,\"long\":9223372036854775807,"
          + "\"array\":[true,false,null,\"bingo\",-2147483648,-9223372036854775808],"
          + "\"object\":{\"true\":true,\"false\":false,\"null\":null,\"bonga\":\"boo\",\"int\":1}}";

      LOGGER.info("Testing read of " + expJsonText);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(new StringReader(expJsonText));
      JsonObject myJsonObject = reader.readObject();

      LOGGER.info("Write the JsonObject 'myJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(myJsonObject);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String actJsonText = sWriter.toString();

      LOGGER.info("Compare actual JSON text with expected JSON text");
      assertTrue(JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText), "readObjectTest3 Failed");
    } catch (Exception e) {
      fail("readObjectTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readObjectTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:420;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object from a resource file with various
   * amounts of data. Use JsonReader.readObject() API call. Test scenario: Read
   * InputStream of JSON text containing a JSON object from resource file with
   * various amounts of data use UTF-8 encoding. Create a JsonWriter to write
   * above JsonObject to a string of JSON text. Re-read JsonWriter text back
   * into a JsonObject Compare expected JSON object with actual JSON object for
   * equality.
   *
   */
  @Test
  public void readObjectTest4() {
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithAllTypesOfData.json";
    try {
      LOGGER.info(
          "Read contents of InputStream from resource file: " + resourceFile);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(
          JSONP_Util.getInputStreamFromResource(resourceFile),
          JSONP_Util.UTF_8);
      JsonObject expJsonObject = reader.readObject();

      LOGGER.info("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      LOGGER.info("Write the JsonObject 'expJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(expJsonObject);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Create actJsonObject from read of writer contents: "
          + writerContents);
      reader = Json.createReader(new StringReader(writerContents));
      JsonObject actJsonObject = reader.readObject();

      LOGGER.info("Dump of actJsonObject");
      JSONP_Util.dumpJsonObject(actJsonObject);

      LOGGER.info("Compare expJsonObject and actJsonObject for equality");
      assertTrue(JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject), "readObjectTest4 Failed");
    } catch (Exception e) {
      fail("readObjectTest4 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readObjectTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:181;
   * JSONP:JAVADOC:420; JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of an object from a resource file with lots of
   * nesting. Use JsonReader.read() API call. Test scenario: Read InputStream of
   * JSON text containing a JSON object from resource file with lots of nesting
   * use UTF-8 encoding with empty configuration. Create a JsonWriter to write
   * above JsonObject to a string of JSON text. Compare expected JSON text with
   * actual JSON text for equality. Filter all text output to remove whitespace
   * before comparison.
   *
   * Tests the following API's: JsonReader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   * JsonReader.read() JsonObject object = (JsonObject)JsonReader.read()
   *
   *
   */
  @Test
  public void readObjectTest5() {
    JsonReader reader = null;
    String resourceFile = "jsonObjectWithLotsOfNestedObjectsData.json";
    try {
      LOGGER.info("Reading contents of resource file " + resourceFile);
      String readerContents = JSONP_Util
          .getContentsOfResourceAsString(resourceFile);
      LOGGER.info("readerContents=" + readerContents);

      // Create expected JSON text from resource contents filtered of whitespace
      // for comparison
      LOGGER.info("Filter readerContents of whitespace for comparison");
      String expJsonText = JSONP_Util.removeWhitespace(readerContents);

      LOGGER.info(
          "Read contents of InputStream from resource file: " + resourceFile);
      reader = Json.createReaderFactory(JSONP_Util.getEmptyConfig())
          .createReader(JSONP_Util.getInputStreamFromResource(resourceFile),
              JSONP_Util.UTF_8);
      JsonObject myJsonObject = (JsonObject) reader.read();

      LOGGER.info("Write the JsonObject 'myJsonObject' out to a JsonWriter");
      StringWriter sWriter = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sWriter)) {
        writer.writeObject(myJsonObject);
        LOGGER.info("Close JsonWriter");
      }

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();

      LOGGER.info("Dump contents of the JsonWriter as a String");
      LOGGER.info("writerContents=" + writerContents);

      LOGGER.info("Filter writerContents of whitespace for comparison");
      String actJsonText = JSONP_Util.removeWhitespace(writerContents);

      LOGGER.info("Compare actual JSON text with expected JSON text");
      assertTrue(JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText), "readObjectTest5 Failed");
    } catch (Exception e) {
      fail("readObjectTest5 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
  }

  /*
   * @testName: readObjectEncodingTest
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Test read of a JsonObject from a resource file using both
   * encodings of UTF-8 and UTF-16LE.
   * 
   * Test scenario: For each encoding read the appropriate resource file
   * containing a string value. Call JsonObject.getJsonString() to get the value
   * of the JsonString. Compare expected string with actual string for equality.
   */
  @Test
  public void readObjectEncodingTest() {
    boolean pass = true;
    JsonReader reader = null;
    String expString = "a\u65e8\u452c\u8b9e\u6589\u5c57\u5217z";
    String resourceFileUTF8 = "jsonObjectUTF8.json";
    String resourceFileUTF16LE = "jsonObjectUTF16LE.json";
    try {
      LOGGER.info("Reading contents of resource file using UTF-8 encoding "
          + resourceFileUTF8);
      InputStream is = JSONP_Util.getInputStreamFromResource(resourceFileUTF8);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonObject jsonObject = reader.readObject();
      LOGGER.info("Comparing JsonObject values with expected results.");
      String actString = jsonObject.getJsonString("unicodeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      fail("readObjectEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    try {
      LOGGER.info("Reading contents of resource file using UTF-16LE encoding "
          + resourceFileUTF16LE);
      InputStream is = JSONP_Util
          .getInputStreamFromResource(resourceFileUTF16LE);
      Map<String, ?> config = JSONP_Util.getEmptyConfig();
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16LE);
      JsonObject jsonObject = reader.readObject();
      LOGGER.info("Comparing JsonObject values with expected results.");
      String actString = jsonObject.getJsonString("unicodeChars").getString();
      if (!JSONP_Util.assertEquals(expString, actString))
        pass = false;
    } catch (Exception e) {
      fail("readObjectEncodingTest Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    assertTrue(pass, "readObjectEncodingTest Failed");
  }

  /*
   * @testName: readUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:420; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:449;
   * 
   * @test_Strategy: Tests the JsonReader reader. Verifies READING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16.json
   * jsonObjectEncodingUTF16LE.json jsonObjectEncodingUTF16BE.json
   * jsonObjectEncodingUTF32LE.json jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonReader via the API:
   *
   * JsonReader reader =
   * Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset)
   *
   * For each supported encoding supported by JSON RFC read the JsonObject and
   * verify we get the expected results. The Charset encoding is passed in as
   * argument for each encoding type read.
   */
  @Test
  public void readUTFEncodedTests() {
    boolean pass = true;
    JsonReader reader = null;
    Map<String, ?> config = JSONP_Util.getEmptyConfig();
    try {
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-8]");
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-8");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_8);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-8 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16]");
      LOGGER.info(
          "------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-16");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-16 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-16LE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16LE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-16LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-16BE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-16BE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_16BE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-16BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-32LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-32LE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_32LE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-32LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createReaderFactory(Map<String,?>).createReader(InputStream, Charset) as UTF-32BE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream with character encoding UTF-32BE");
      reader = Json.createReaderFactory(config).createReader(is,
          JSONP_Util.UTF_32BE);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-32BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "readUTFEncodedTests Failed");
  }

  /*
   * @testName: readUTFEncodedTests2
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:178; JSONP:JAVADOC:185;
   * 
   * @test_Strategy: Tests the JsonReader reader. Verifies READING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16LE.json
   * jsonObjectEncodingUTF16BE.json jsonObjectEncodingUTF32LE.json
   * jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonReader via the API:
   *
   * JsonReader reader = Json.createReader(InputStream istream)
   *
   * For each supported encoding supported by JSON RFC read the JsonObject and
   * verify we get the expected results. The character encoding of the stream is
   * auto-detected and determined as per the RFC.
   */
  @Test
  public void readUTFEncodedTests2() {
    boolean pass = true;
    JsonReader reader = null;
    try {
      LOGGER.info("---------------------------------------------------");
      LOGGER.info("TEST CASE [Json.createReader(InputStream) as UTF-8]");
      LOGGER.info("---------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      LOGGER.info(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-8");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-8 encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info("------------------------------------------------------");
      LOGGER.info("TEST CASE [Json.createReader(InputStream) as UTF-16LE]");
      LOGGER.info("------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-16LE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-16LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info("------------------------------------------------------");
      LOGGER.info("TEST CASE [Json.createReader(InputStream) as UTF-16BE]");
      LOGGER.info("------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-16BE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-16BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info("------------------------------------------------------");
      LOGGER.info("TEST CASE [Json.createReader(InputStream) as UTF-32LE]");
      LOGGER.info("------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-32LE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-32LE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info("------------------------------------------------------");
      LOGGER.info("TEST CASE [Json.createReader(InputStream) as UTF-32BE]");
      LOGGER.info("------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      LOGGER.info(
          "Create JsonReader from the InputStream and auto-detect character encoding UTF-32BE");
      reader = Json.createReader(is);
      JsonObject jsonObject = reader.readObject();
      if (!compareJsonObjectForUTFEncodedTests(jsonObject))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing reading of UTF-32BE encoding: " + e);
    } finally {
      try {
        if (reader != null)
          reader.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "readUTFEncodedTests2 Failed");
  }

  /*
   * @testName: negativeObjectTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:413;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a JsonObject.
   * The tests trip various JsonParsingException/JsonException conditions when
   * reading an object.
   *
   */
  @Test
  public void negativeObjectTests() {
    boolean pass = true;
    JsonReader reader = null;

    // Not an object []

    try {
      LOGGER.info("Testing for not an object '[]'");
      reader = Json.createReader(new StringReader("[]"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonException");
    } catch (JsonException e) {
      LOGGER.info("Got expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Trip JsonParsingException for JsonReader.readObject() if incorrect
    // representation for object
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for object.");
      LOGGER.info("Reading " + "{\"name\":\"value\",1,2,3}");
      reader = Json
          .createReader(new StringReader("{\"name\":\"value\",1,2,3}"));
      JsonObject jsonObject = reader.readObject();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      LOGGER.info("Testing for missing '['");
      reader = Json.createReader(new StringReader("{1,2]}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      LOGGER.info("Testing for missing ']'");
      reader = Json.createReader(new StringReader("{[1,2}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      LOGGER.info("Testing for missing '{'");
      reader = Json.createReader(new StringReader("}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      LOGGER.info("Testing for missing '}'");
      reader = Json.createReader(new StringReader("{"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("{[5\"foo\"]}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("{[5{}]}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("{\"foo\":\"bar\"5}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("{\"one\":1[]}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      LOGGER.info("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("{:\"bar\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      LOGGER.info("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("{\"foo\":}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      LOGGER.info("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("{name\" : \"value\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      LOGGER.info("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("{\"name\" : value\"}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      LOGGER.info("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("{\"number\" : -foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      LOGGER.info("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("{\"number\" : +foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      LOGGER.info("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("{\"number\" : -784foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      LOGGER.info("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("{\"number\" : +784foo}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      LOGGER.info("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("{\"number\" : 0.1E5E5}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      LOGGER.info("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("{\"number\" : 0.F10}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      LOGGER.info("Incorrect digit value string");
      reader = Json.createReader(new StringReader("{\"number\" : string}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 8 (hex numbers invalid per JSON RFC)

    try {
      LOGGER.info("Incorrect digit value hex numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("{\"number\" : 0x137a}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 9 (octal numbers invalid per JSON RFC)

    try {
      LOGGER.info("Incorrect digit value octal numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("{\"number\" : 0137}"));
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    assertTrue(pass, "negativeObjectTests Failed");
  }

  /*
   * @testName: negativeArrayTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:412;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a JsonArray.
   * The tests trip various JsonParsingException/JsonException conditions when
   * reading an array.
   *
   */
  @Test
  public void negativeArrayTests() {
    boolean pass = true;
    JsonReader reader = null;

    // Not an array {}

    try {
      LOGGER.info("Testing for not an array '{}'");
      reader = Json.createReader(new StringReader("{}"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonException");
    } catch (JsonException e) {
      LOGGER.info("Got expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Trip JsonParsingException for JsonReader.readArray() if incorrect
    // representation for array
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.readArray() if incorrect representation for array.");
      LOGGER.info("Reading " + "[foo,10,\"name\":\"value\"]");
      reader = Json
          .createReader(new StringReader("[foo,10,\"name\":\"value\"]"));
      JsonArray jsonArray = reader.readArray();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      LOGGER.info("Testing for missing '['");
      reader = Json.createReader(new StringReader("]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      LOGGER.info("Testing for missing ']'");
      reader = Json.createReader(new StringReader("["));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      LOGGER.info("Testing for missing '{'");
      reader = Json.createReader(new StringReader("[1,\"name\":\"value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      LOGGER.info("Testing for missing '}'");
      reader = Json.createReader(new StringReader("[1,{\"name\":\"value\"]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("[5\"foo\"]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("[5{}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("[{\"foo\":\"bar\"5}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("[{\"one\":1[]}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      LOGGER.info("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("[{:\"bar\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      LOGGER.info("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("[{\"foo\":}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      LOGGER.info("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("[{name\" : \"value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      LOGGER.info("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("[{\"name\" : value\"}]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      LOGGER.info("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("[-foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      LOGGER.info("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("[+foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      LOGGER.info("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("[-784foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      LOGGER.info("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("[+784foo]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      LOGGER.info("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("[0.1E5E5]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      LOGGER.info("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("[0.F10]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      LOGGER.info("Incorrect digit value string");
      reader = Json.createReader(new StringReader("[string]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 8 (hex numbers invalid per JSON RFC)

    try {
      LOGGER.info("Incorrect digit value hex numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("[0x137a]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 9 (octal numbers invalid per JSON RFC)

    try {
      LOGGER.info("Incorrect digit value octal numbers invalid per JSON RFC");
      reader = Json.createReader(new StringReader("[0137]"));
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    assertTrue(pass, "negativeArrayTests Failed");
  }

  /*
   * @testName: illegalStateExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:218;
   * JSONP:JAVADOC:220; JSONP:JAVADOC:183;
   * 
   * @test_Strategy: Test IllegalStateException test conditions.
   *
   */
  @Test
  public void illegalStateExceptionTests() {
    boolean pass = true;
    JsonReader reader = null;

    // IllegalStateException if reader.close() called before reader.read()
    try {
      reader = Json.createReader(new StringReader("{}"));
      reader.close();
      LOGGER.info(
          "Calling reader.read() after reader.close() is called is illegal.");
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.read() called after reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject value = reader.readObject();
      LOGGER.info(
          "Calling reader.readObject() after reader.readObject() was called is illegal.");
      value = (JsonObject) reader.read();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.read() called after reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray value = reader.readArray();
      LOGGER.info(
          "Calling reader.read() after reader.readArray() was called is illegal.");
      value = (JsonArray) reader.read();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.close() called before reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      reader.close();
      LOGGER.info(
          "Calling reader.readObject() after reader.close() is called is illegal.");
      JsonObject value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.readObject() called after
    // reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject value = reader.readObject();
      LOGGER.info(
          "Calling reader.readObject() after reader.readObject() was called is illegal.");
      value = reader.readObject();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.readArray() called after
    // reader.readObject()
    try {
      reader = Json.createReader(new StringReader("{}"));
      JsonObject obj = reader.readObject();
      LOGGER.info(
          "Calling reader.readArray() after reader.readObject() was called is illegal.");
      JsonArray arr = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.close() called before reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      reader.close();
      LOGGER.info(
          "Calling reader.readArray() after reader.close() is called is illegal.");
      JsonArray value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // IllegalStateException if reader.readArray() called after
    // reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray value = reader.readArray();
      LOGGER.info(
          "Calling reader.readArray() after reader.readArray() was called is illegal.");
      value = reader.readArray();
      pass = false;
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // IllegalStateException if reader.readObject() called after
    // reader.readArray()
    try {
      reader = Json.createReader(new StringReader("[]"));
      JsonArray arr = reader.readArray();
      LOGGER.info(
          "Calling reader.readObject() after reader.readArray() was called is illegal.");
      JsonObject obj = reader.readObject();
      pass = false;
      LOGGER.info("obj=" + obj);
      LOGGER.warning("Failed to throw IllegalStateException");
    } catch (IllegalStateException e) {
      LOGGER.info("Got expected IllegalStateException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    assertTrue(pass, "illegalStateExceptionTests Failed");
  }

  /*
   * @testName: negativeJsonStructureTests
   * 
   * @assertion_ids: JSONP:JAVADOC:96; JSONP:JAVADOC:97; JSONP:JAVADOC:411;
   * 
   * @test_Strategy: Test various Json Syntax Errors when reading a
   * JsonStructure. The tests trip various JsonParsingException conditions when
   * doing a read.
   *
   */
  @Test
  public void negativeJsonStructureTests() {
    boolean pass = true;
    JsonReader reader = null;

    // Trip JsonParsingException for JsonReader.read() if incorrect
    // representation for array
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for array.");
      LOGGER.info("Reading " + "[foo,10,\"name\":\"value\"]");
      reader = Json
          .createReader(new StringReader("[foo,10,\"name\":\"value\"]"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if incorrect
    // representation for object
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if incorrect representation for object.");
      LOGGER.info("Reading " + "{\"name\":\"value\",1,2,3}");
      reader = Json
          .createReader(new StringReader("{\"name\":\"value\",1,2,3}"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // incorrect representation {]
    try {
      LOGGER.info("Testing for incorrect representation '{]'");
      reader = Json.createReader(new StringReader("{]"));
      LOGGER.info(
          "Calling reader.read() with incorrect representation should throw JsonParsingException");
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Missing [

    try {
      LOGGER.info("Testing for missing '['");
      reader = Json.createReader(new StringReader("{1,2]}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing ]

    try {
      LOGGER.info("Testing for missing ']'");
      reader = Json.createReader(new StringReader("{[1,2}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing {

    try {
      LOGGER.info("Testing for missing '{'");
      reader = Json.createReader(new StringReader("}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing }

    try {
      LOGGER.info("Testing for missing '}'");
      reader = Json.createReader(new StringReader("{"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 1");
      reader = Json.createReader(new StringReader("{[5\"foo\"]}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between array elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between array elements test case 2");
      reader = Json.createReader(new StringReader("{[5{}]}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 1

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 1");
      reader = Json.createReader(new StringReader("{\"foo\":\"bar\"5}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing , between object elements test case 2

    try {
      LOGGER.info("Testing for missing ',' between object elements test case 2");
      reader = Json.createReader(new StringReader("{\"one\":1[]}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing key name in object element

    try {
      LOGGER.info("Testing for missing key name in object element");
      reader = Json.createReader(new StringReader("{:\"bar\"}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing value name in object element

    try {
      LOGGER.info("Testing for missing value name in object element");
      reader = Json.createReader(new StringReader("{\"foo\":}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a name

    try {
      LOGGER.info("Test for missing double quote on a name");
      reader = Json.createReader(new StringReader("{name\" : \"value\"}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Missing double quote on a value

    try {
      LOGGER.info("Test for missing double quote on a value");
      reader = Json.createReader(new StringReader("{\"name\" : value\"}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 1

    try {
      LOGGER.info("Incorrect digit value -foo");
      reader = Json.createReader(new StringReader("{\"number\" : -foo}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 2

    try {
      LOGGER.info("Incorrect digit value +foo");
      reader = Json.createReader(new StringReader("{\"number\" : +foo}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 3

    try {
      LOGGER.info("Incorrect digit value -784foo");
      reader = Json.createReader(new StringReader("{\"number\" : -784foo}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 4

    try {
      LOGGER.info("Incorrect digit value +784foo");
      reader = Json.createReader(new StringReader("{\"number\" : +784foo}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 5

    try {
      LOGGER.info("Incorrect digit value 0.1E5E5");
      reader = Json.createReader(new StringReader("{\"number\" : 0.1E5E5}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 6

    try {
      LOGGER.info("Incorrect digit value  0.F10");
      reader = Json.createReader(new StringReader("{\"number\" : 0.F10}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    // Incorrect digit value test case 7

    try {
      LOGGER.info("Incorrect digit value string");
      reader = Json.createReader(new StringReader("{\"number\" : string}"));
      JsonStructure value = reader.read();
      pass = false;
      LOGGER.warning("Failed to throw JsonParsingException");
    } catch (JsonParsingException e) {
      LOGGER.info("Got expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    } finally {
      if (reader != null)
        reader.close();
    }

    assertTrue(pass, "negativeJsonStructureTests Failed");
  }

  /*
   * @testName: jsonReaderIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:182; JSONP:JAVADOC:217; JSONP:JAVADOC:219;
   * JSONP:JAVADOC:410;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  @Test
  public void jsonReaderIOErrorTests() {
    boolean pass = true;

    String jsonArrayText = "[\"name1\",\"value1\"]";
    String jsonObjectText = "{\"name1\":\"value1\"}";

    // Trip JsonException if there is an i/o error on JsonReader.close()
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on JsonReader.close().");
      LOGGER.info("Reading object " + jsonObjectText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonObjectText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is);
      try (JsonReader reader = Json.createReader(mbi)) {
        JsonObject jsonObject = reader.readObject();
        LOGGER.info("jsonObject=" + jsonObject);
        mbi.setThrowIOException(true);
        LOGGER.info("Calling JsonReader.close()");
        mbi.setThrowIOException(true);
      }
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.read() if i/o error
    try {
      LOGGER.info("Trip JsonException for JsonReader.read() if i/o error.");
      LOGGER.info("Reading array " + jsonArrayText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonArrayText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      LOGGER.info("Calling JsonReader.read()");
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.readArray() if i/o error
    try {
      LOGGER.info("Trip JsonException for JsonReader.readArray() if i/o error.");
      LOGGER.info("Reading array " + jsonArrayText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonArrayText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      LOGGER.info("Calling JsonReader.readArray()");
      JsonArray jsonArray = reader.readArray();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException for JsonReader.readObject() if i/o error
    try {
      LOGGER.info("Trip JsonException for JsonReader.read() if i/o error.");
      LOGGER.info("Reading object " + jsonObjectText);
      MyBufferedReader mbr = new MyBufferedReader(
          new StringReader(jsonObjectText));
      JsonReader reader = Json.createReader(mbr);
      mbr.setThrowIOException(true);
      LOGGER.info("Calling JsonReader.readObject()");
      JsonObject jsonObject = reader.readObject();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonReaderIOErrorTests Failed");
  }

  /*
   * @testName: invalidLiteralNamesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:97; JSONP:JAVADOC:411;
   * 
   * @test_Strategy: This test trips various JsonParsingException conditions
   * when reading an uppercase literal name that must be lowercase per JSON RFC
   * for the literal values (true, false or null).
   *
   */
  @Test
  public void invalidLiteralNamesTest() {
    boolean pass = true;
    JsonReader reader;

    // Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE
    // instead of true
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE instead of true.");
      LOGGER.info("Reading " + "[TRUE]");
      reader = Json.createReader(new StringReader("[TRUE]"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE
    // instead of false
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE instead of false.");
      LOGGER.info("Reading " + "[FALSE]");
      reader = Json.createReader(new StringReader("[FALSE]"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal NULL
    // instead of null
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal NULL instead of null.");
      LOGGER.info("Reading " + "[NULL]");
      reader = Json.createReader(new StringReader("[NULL]"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE
    // instead of true
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal TRUE instead of true.");
      LOGGER.info("Reading " + "{\"true\":TRUE}");
      reader = Json.createReader(new StringReader("{\"true\":TRUE}"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE
    // instead of false
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal FALSE instead of false.");
      LOGGER.info("Reading " + "{\"false\":FALSE}");
      reader = Json.createReader(new StringReader("{\"false\":FALSE}"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonReader.read() if invalid liternal NULL
    // instead of null
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonReader.read() if invalid liternal NULL instead of null.");
      LOGGER.info("Reading " + "{\"null\":NULL}");
      reader = Json.createReader(new StringReader("{\"null\":NULL}"));
      JsonStructure jsonStructure = reader.read();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "invalidLiteralNamesTest Failed");
  }

  /*
   * @testName: jsonReader11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:646; JSONP:JAVADOC:583; JSONP:JAVADOC:584;
   * JSONP:JAVADOC:585; JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588;
   * JSONP:JAVADOC:662; JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665;
   * JSONP:JAVADOC:666; JSONP:JAVADOC:667;
   * 
   * @test_Strategy: Tests JsonReader API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonReader11Test() {
    Reader readerTest = new Reader();
    final TestResult result = readerTest.test();
    result.eval();
  }

  /*
   * @testName: testDuplicateKeysDefault
   *
   * @assertion_ids: JSONP:JAVADOC:681;
   *
   * @test_Strategy: Tests key strategy added in JSON-P 2.1.
   */
  @Test
  public void testDuplicateKeysDefault() {
      Map<String, Object> config = new HashMap<>();
      JsonReaderFactory factory = Json.createReaderFactory(config);
      String json = "{\"val1\":\"A\",\"val1\":\"B\"}";
      JsonReader reader = factory.createReader(new StringReader(json));
      JsonObject object = reader.readObject();
      reader.close();
      assertEquals("B", object.getString("val1"));
  }

  /*
   * @testName: testDuplicateKeysNone
   *
   * @assertion_ids: JSONP:JAVADOC:681;
   *
   * @test_Strategy: Tests key strategy added in JSON-P 2.1.
   */
  @Test
  public void testDuplicateKeysNone() {
      Map<String, Object> config = new HashMap<>();
      config.put(JsonConfig.KEY_STRATEGY, JsonConfig.KeyStrategy.NONE);
      JsonReaderFactory factory = Json.createReaderFactory(config);
      String json = "{\"val1\":\"A\",\"val1\":\"B\"}";
      JsonReader reader = factory.createReader(new StringReader(json));
      try {
          reader.readObject();
          fail("It is expected a JsonException");
      } catch (JsonException e) {}
  }

  /*
   * @testName: testDuplicateKeysFirst
   *
   * @assertion_ids: JSONP:JAVADOC:681;
   *
   * @test_Strategy: Tests key strategy added in JSON-P 2.1.
   */
  @Test
  public void testDuplicateKeysFirst() {
      Map<String, Object> config = new HashMap<>();
      config.put(JsonConfig.KEY_STRATEGY, JsonConfig.KeyStrategy.FIRST);
      JsonReaderFactory factory = Json.createReaderFactory(config);
      String json = "{\"val1\":\"A\",\"val1\":\"B\"}";
      JsonReader reader = factory.createReader(new StringReader(json));
      JsonObject object = reader.readObject();
      reader.close();
      assertEquals("A", object.getString("val1"));
  }

  /*
   * @testName: testDuplicateKeysLast
   *
   * @assertion_ids: JSONP:JAVADOC:681;
   *
   * @test_Strategy: Tests key strategy added in JSON-P 2.1.
   */
  @Test
  public void testDuplicateKeysLast() {
      Map<String, Object> config = new HashMap<>();
      config.put(JsonConfig.KEY_STRATEGY, JsonConfig.KeyStrategy.LAST);
      JsonReaderFactory factory = Json.createReaderFactory(config);
      String json = "{\"val1\":\"A\",\"val1\":\"B\"}";
      JsonReader reader = factory.createReader(new StringReader(json));
      JsonObject object = reader.readObject();
      reader.close();
      assertEquals("B", object.getString("val1"));
  }
}

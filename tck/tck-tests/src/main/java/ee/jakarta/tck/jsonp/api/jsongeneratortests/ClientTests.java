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

/*
 * $Id$
 */
package ee.jakarta.tck.jsonp.api.jsongeneratortests;

import ee.jakarta.tck.jsonp.api.common.TestResult;
import ee.jakarta.tck.jsonp.common.JSONP_Util;
import ee.jakarta.tck.jsonp.common.MyBufferedWriter;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;

import jakarta.json.*;
import jakarta.json.stream.*;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* private Utility methods */

  /*********************************************************************************
   * generateSimpleJsonObject
   *********************************************************************************/
  private void generateSimpleJsonObject(JsonGenerator generator) {
    try {
      generator.writeStartObject().writeStartObject("object")
          .write("string", "string").write("number", 1)
          .write("true", JsonValue.TRUE).write("false", JsonValue.FALSE)
          .write("null", JsonValue.NULL).writeEnd().writeStartArray("array")
          .write("string").write(1).write(JsonValue.TRUE).write(JsonValue.FALSE)
          .write(JsonValue.NULL).writeEnd().writeEnd();
      generator.close();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * generateSimpleJsonArray
   *********************************************************************************/
  private void generateSimpleJsonArray(JsonGenerator generator) {
    try {
      generator.writeStartArray().writeStartObject().write("string", "string")
          .write("number", 1).write("true", JsonValue.TRUE)
          .write("false", JsonValue.FALSE).write("null", JsonValue.NULL)
          .writeEnd().writeStartArray().write("string").write(1)
          .write(JsonValue.TRUE).write(JsonValue.FALSE).write(JsonValue.NULL)
          .writeEnd().writeEnd();
      generator.close();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * generateJsonObject
   *********************************************************************************/
  private String generateJsonObject() {
    try {
      LOGGER.info("Generate a JsonObject");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("emptyString", "")
          .writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("intMin", Integer.MIN_VALUE).write("intMax", Integer.MAX_VALUE)
          .write("longMin", Long.MIN_VALUE).write("longMax", Long.MAX_VALUE)
          .write("doubleMin", Double.MIN_VALUE)
          .write("doubleMax", Double.MAX_VALUE)
          .write("bigInteger",
              new BigInteger(Integer.toString(Integer.MAX_VALUE)))
          .write("bigDecimal", BigDecimal.valueOf(Integer.MIN_VALUE))
          .write("true", JsonValue.TRUE).write("false", JsonValue.FALSE)
          .write("null", JsonValue.NULL).writeStartObject("object")
          .write("emptyString", "").writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("number", 100).write("true", true).write("false", false)
          .writeNull("null").writeStartObject("object").write("name", "value")
          .write("objectFooBar", JSONP_Util.buildJsonObjectFooBar())
          .write("arrayFooBar", JSONP_Util.buildJsonArrayFooBar()).writeEnd()
          .writeStartArray("array").write("one").write("two")
          .write(JSONP_Util.buildJsonObjectFooBar())
          .write(JSONP_Util.buildJsonArrayFooBar()).writeEnd().writeEnd()
          .writeStartArray("array").write("string").write(Integer.MAX_VALUE)
          .write(Long.MAX_VALUE).write(Double.MAX_VALUE)
          .write(new BigInteger(Integer.toString(Integer.MAX_VALUE)))
          .write(JsonValue.TRUE).write(JsonValue.FALSE).write(JsonValue.NULL)
          .writeStartObject().write("name", "value").writeEnd()
          .writeStartArray().write("one").write("two").writeEnd().writeEnd()
          .write("asciiChars",
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .writeEnd();
      generator.close();
      return sWriter.toString();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  /*********************************************************************************
   * buildJsonObject
   *********************************************************************************/
  private JsonObject buildJsonObject() {
    try {
      LOGGER.info("Build a JsonObject");
      JsonObject jsonObject = Json.createObjectBuilder().add("emptyString", "")
          .add("emptyArray", Json.createArrayBuilder())
          .add("emptyObject", Json.createObjectBuilder())
          .add("string", "string").add("intMin", Integer.MIN_VALUE)
          .add("intMax", Integer.MAX_VALUE).add("longMin", Long.MIN_VALUE)
          .add("longMax", Long.MAX_VALUE).add("doubleMin", Double.MIN_VALUE)
          .add("doubleMax", Double.MAX_VALUE)
          .add("bigInteger",
              new BigInteger(Integer.toString(Integer.MAX_VALUE)))
          .add("bigDecimal", BigDecimal.valueOf(Integer.MIN_VALUE))
          .add("true", JsonValue.TRUE).add("false", JsonValue.FALSE)
          .add("null", JsonValue.NULL)
          .add("object",
              Json.createObjectBuilder().add("emptyString", "")
                  .add("emptyArray", Json.createArrayBuilder())
                  .add("emptyObject", Json.createObjectBuilder())
                  .add("string", "string").add("number", 100)
                  .add("true", JsonValue.TRUE).add("false", JsonValue.FALSE)
                  .add("null", JsonValue.NULL)
                  .add("object", Json.createObjectBuilder().add("name", "value")
                      .add("objectFooBar", JSONP_Util.buildJsonObjectFooBar())
                      .add("arrayFooBar", JSONP_Util.buildJsonArrayFooBar()))
                  .add("array",
                      Json.createArrayBuilder().add("one").add("two")
                          .add(JSONP_Util.buildJsonObjectFooBar())
                          .add(JSONP_Util.buildJsonArrayFooBar())))
          .add("array",
              Json.createArrayBuilder().add("string").add(Integer.MAX_VALUE)
                  .add(Long.MAX_VALUE).add(Double.MAX_VALUE)
                  .add(new BigInteger(Integer.toString(Integer.MAX_VALUE)))
                  .add(JsonValue.TRUE).add(JsonValue.FALSE).add(JsonValue.NULL)
                  .add(Json.createObjectBuilder().add("name", "value"))
                  .add(Json.createArrayBuilder().add("one").add("two")))
          .add("asciiChars",
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .build();
      return jsonObject;
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  /*********************************************************************************
   * generateJsonArray
   *********************************************************************************/
  private String generateJsonArray() {
    try {
      LOGGER.info("Generate a JsonArray");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("").writeStartArray().writeEnd()
          .writeStartObject().writeEnd().write("string")
          .write(Integer.MIN_VALUE).write(Integer.MAX_VALUE)
          .write(Long.MIN_VALUE).write(Long.MAX_VALUE).write(Double.MIN_VALUE)
          .write(Double.MAX_VALUE)
          .write(new BigInteger(new Integer(Integer.MAX_VALUE).toString()))
          .write(BigDecimal.valueOf(Integer.MIN_VALUE)).write(JsonValue.TRUE)
          .write(JsonValue.FALSE).write(JsonValue.NULL).writeStartObject()
          .write("emptyString", "").writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("number", 100).write("true", JsonValue.TRUE)
          .write("false", JsonValue.FALSE).write("null", JsonValue.NULL)
          .writeStartObject("object").write("name", "value")
          .write("objectFooBar", JSONP_Util.buildJsonObjectFooBar())
          .write("arrayFooBar", JSONP_Util.buildJsonArrayFooBar()).writeEnd()
          .writeStartArray("array").write("one").write("two")
          .write(JSONP_Util.buildJsonObjectFooBar())
          .write(JSONP_Util.buildJsonArrayFooBar()).writeEnd().writeEnd()
          .writeStartArray().write("string").write(Integer.MAX_VALUE)
          .write(Long.MAX_VALUE).write(Double.MAX_VALUE)
          .write(new BigInteger(new Integer(Integer.MAX_VALUE).toString()))
          .write(true).write(false).writeNull().writeStartObject()
          .write("name", "value").writeEnd().writeStartArray().write("one")
          .write("two").writeEnd().writeEnd()
          .write(
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .writeEnd();
      generator.close();
      return sWriter.toString();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  /*********************************************************************************
   * buildJsonArray
   *********************************************************************************/
  private JsonArray buildJsonArray() {
    try {
      LOGGER.info("Build a JsonArray");
      JsonArray jsonArray = Json.createArrayBuilder().add("")
          .add(Json.createArrayBuilder()).add(Json.createObjectBuilder())
          .add("string").add(Integer.MIN_VALUE).add(Integer.MAX_VALUE)
          .add(Long.MIN_VALUE).add(Long.MAX_VALUE).add(Double.MIN_VALUE)
          .add(Double.MAX_VALUE)
          .add(new BigInteger(new Integer(Integer.MAX_VALUE).toString()))
          .add(BigDecimal.valueOf(Integer.MIN_VALUE)).add(JsonValue.TRUE)
          .add(JsonValue.FALSE).add(JsonValue.NULL)
          .add(Json.createObjectBuilder().add("emptyString", "")
              .add("emptyArray", Json.createArrayBuilder())
              .add("emptyObject", Json.createObjectBuilder())
              .add("string", "string").add("number", 100)
              .add("true", JsonValue.TRUE).add("false", JsonValue.FALSE)
              .add("null", JsonValue.NULL)
              .add("object",
                  Json.createObjectBuilder().add("name", "value")
                      .add("objectFooBar", JSONP_Util.buildJsonObjectFooBar())
                      .add("arrayFooBar", JSONP_Util.buildJsonArrayFooBar()))
              .add("array",
                  Json.createArrayBuilder().add("one").add("two")
                      .add(JSONP_Util.buildJsonObjectFooBar())
                      .add(JSONP_Util.buildJsonArrayFooBar())))
          .add(Json.createArrayBuilder().add("string").add(Integer.MAX_VALUE)
              .add(Long.MAX_VALUE).add(Double.MAX_VALUE)
              .add(new BigInteger(new Integer(Integer.MAX_VALUE).toString()))
              .add(JsonValue.TRUE).add(JsonValue.FALSE).add(JsonValue.NULL)
              .add(Json.createObjectBuilder().add("name", "value"))
              .add(Json.createArrayBuilder().add("one").add("two")))
          .add(
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .build();
      return jsonArray;
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  /* Tests */

  /*
   * @testName: jsonGeneratorObjectTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:131;
   * JSONP:JAVADOC:289;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * The output is written to a writer, read back as a string and filtered to
   * remove whitespace and is compared against an expected string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] }
   */
  @Test
  public void jsonGeneratorObjectTest1() {
    try {
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generateSimpleJsonObject(generator);

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(sWriter.toString());
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectTest1 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:168;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:307; JSONP:JAVADOC:327; JSONP:JAVADOC:339;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * The output is written to a writer, read back as a string and filtered to
   * remove whitespace and is compared against an expected string.
   *
   * { "emptyString":"", "emptyArray":[], "emptyObject":{}, "string":"string","+
   * "intMin":Integer.MIN_VALUE, "intMax":Integer.MAX_VALUE,
   * "longMin":Long.MIN_VALUE, "longMax":Long.MAX_VALUE, "true":true,
   * "false":false, "null":null, "object": { "emptyString":"", "emptyArray":[],
   * "emptyObject":{}, "string":"string", "number":100, "true":true,
   * "false":false, "null":null, "object":{"name":"value"} "array":["one","two"]
   * }, "array": [ "string", Integer.MAX_VALUE, Long.MAX_VALUE, true, false,
   * null, {"name":"value"}, ["one","two"] ],
   * "asciiChars":"\"\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
   * }
   */
  @Test
  public void jsonGeneratorObjectTest2() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json.createGenerator(baos);
      generator.writeStartObject().write("emptyString", "")
          .writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("intMin", Integer.MIN_VALUE).write("intMax", Integer.MAX_VALUE)
          .write("longMin", Long.MIN_VALUE).write("longMax", Long.MAX_VALUE)
          .write("true", JsonValue.TRUE).write("false", JsonValue.FALSE)
          .write("null", JsonValue.NULL).writeStartObject("object")
          .write("emptyString", "").writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("number", 100).write("true", JsonValue.TRUE)
          .write("false", JsonValue.FALSE).write("null", JsonValue.NULL)
          .writeStartObject("object").write("name", "value").writeEnd()
          .writeStartArray("array").write("one").write("two").writeEnd()
          .writeEnd().writeStartArray("array").write("string")
          .write(Integer.MAX_VALUE).write(Long.MAX_VALUE).write(JsonValue.TRUE)
          .write(JsonValue.FALSE).write(JsonValue.NULL).writeStartObject()
          .write("name", "value").writeEnd().writeStartArray().write("one")
          .write("two").writeEnd().writeEnd()
          .write("asciiChars",
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .writeEnd();
      generator.close();

      LOGGER.info("Dump of string: " + baos.toString("UTF-8"));

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"emptyString\":\"\",\"emptyArray\":[],\"emptyObject\":{},\"string\":\"string\","
          + "\"intMin\":" + Integer.MIN_VALUE + "," + "\"intMax\":"
          + Integer.MAX_VALUE + "," + "\"longMin\":" + Long.MIN_VALUE + ","
          + "\"longMax\":" + Long.MAX_VALUE + ","
          + "\"true\":true,\"false\":false,\"null\":null,\"object\":{\"emptyString\":\"\",\"emptyArray\":[],"
          + "\"emptyObject\":{},\"string\":\"string\",\"number\":100,\"true\":true,\"false\":false,"
          + "\"null\":null,\"object\":{\"name\":\"value\"},"
          + "\"array\":[\"one\",\"two\"]},\"array\":[\"string\","
          + Integer.MAX_VALUE + "," + Long.MAX_VALUE + ",true,false,null,"
          + "{\"name\":\"value\"},[\"one\",\"two\"]],\"asciiChars\":"
          + "\"\\\\\\\"\\\\\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM\""
          + "}";

      LOGGER.info("Read the JSON text back from OutputStream removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectTest2 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectTest2 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:131;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:307; JSONP:JAVADOC:327; JSONP:JAVADOC:339;
   * JSONP:JAVADOC:301; JSONP:JAVADOC:310; JSONP:JAVADOC:329; JSONP:JAVADOC:323;
   * JSONP:JAVADOC:298; JSONP:JAVADOC:314; JSONP:JAVADOC:334;
   * 
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * The output is written to a writer, read back as a JsonObject and compared
   * against an expected JsonObject.
   *
   * { "emptyString":"", "emptyArray":[], "emptyObject":{}, "string":"string","+
   * "intMin":Integer.MIN_VALUE, "intMax":Integer.MAX_VALUE,
   * "longMin":Long.MIN_VALUE, "longMax":Long.MAX_VALUE,
   * "doubleMin":Double.MIN_VALUE, "doubleMax":Double.MAX_VALUE,
   * "bigInteger":Integer.MAX_VALUE, "bigDecimal":Integer.MIN_VALUE,
   * "true":true, "false":false, "null":null, "object": { "emptyString":"",
   * "emptyArray":[], "emptyObject":{}, "string":"string", "number":100,
   * "true":true, "false":false, "null":null,
   * "object":{"name":"value",{"foo":"bar"}},
   * "array":["one","two",["foo","bar"]] }, "array": [ "string",
   * Integer.MAX_VALUE, Long.MAX_VALUE, Double.MAX_VALUE, Integer.MAX_VALUE
   * true, false, null, {"name":"value"}, ["one","two"] ],
   * "asciiChars":"\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
   * }
   */
  @Test
  public void jsonGeneratorObjectTest3() {
    try {
      JsonObject expJsonObject = buildJsonObject();
      String jsonText = generateJsonObject();

      JsonReader reader = Json.createReader(new StringReader(jsonText));
      JsonObject actJsonObject = (JsonObject) reader.read();

      // Do comparison
      LOGGER.info("Compare expJsonObject and actJsonObject for equality");
      assertTrue(
          JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject),
          "jsonGeneratorObjectTest3 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectTest3 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:416;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:163;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * Encoding is done in UTF-16BE. The output is written to an OutputStream as
   * UTF-16BE encoding, read back as a string using UTF-16BE encoding and
   * filtered to remove whitespace and is compared against an expected string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] } Tests the following API
   * call:
   *
   * JsonGenerator generator =
   * Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream,
   * Charset);
   */
  @Test
  public void jsonGeneratorObjectTest4() {
    try {
      LOGGER.info("Create generator output in UTF-16BE encoding.");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_16BE);
      generateSimpleJsonObject(generator);

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info(
          "Read the JSON text back encoding from OutputStream using UTF-16BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16BE"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectTest4 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectTest4 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:131; JSONP:JAVADOC:341; JSONP:JAVADOC:295;
   * JSONP:JAVADOC:289;
   * 
   * @test_Strategy: Test generation of object data with multiple unicode chars
   * in data. The output is written to a writer, read back using a reader as a
   * JsonObject and extracts the JsonString value out and compares it against
   * the expected JsonString value.
   *
   * Generate the following object of unicode char(s):
   *
   * {"unicodechars":"\u0000\u000f\u001f\u00ff\uff00\uffff"}
   */
  @Test
  public void jsonGeneratorObjectTest5() {
    JsonReader reader = null;
    String expUnicodeChars = "\u0000\u000f\u001f\u00ff\uff00\uffff";
    try {

      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject()
          .write("unicodechars", "\u0000\u000f\u001f\u00ff\uff00\uffff")
          .writeEnd();
      generator.close();
      sWriter.close();

      LOGGER.info("Testing read of " + sWriter.toString());
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonObject jsonObject = reader.readObject();
      String actUnicodeChars = jsonObject.getJsonString("unicodechars")
          .getString();
      reader.close();
      LOGGER.info("actUnicodeChars=" + actUnicodeChars);

      assertTrue(JSONP_Util.assertEquals(expUnicodeChars, actUnicodeChars), "jsonGeneratorObjectTest5 Failed");
    } catch (Exception e) {
      fail("Exception occurred: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorArrayTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:339; JSONP:JAVADOC:341; JSONP:JAVADOC:295;
   * JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317; JSONP:JAVADOC:325;
   * JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:131; JSONP:JAVADOC:289;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonArray.
   * The output is written to a writer, read back as a string and filtered to
   * remove whitespace and is compared against an expected string.
   *
   * [ {"string":"string","number":1,"true":true,"false":false,"null":null},
   * ["string", 1, true, false, null] ]
   *
   */
  @Test
  public void jsonGeneratorArrayTest1() {
    try {
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generateSimpleJsonArray(generator);

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "[{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},[\"string\",1,true,false,null]]";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(sWriter.toString());
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorArrayTest1 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorArrayTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorArrayTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:168;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:327; JSONP:JAVADOC:339;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonArray.
   * The output is written to a writer, read back as a string and filtered to
   * remove whitespace and is compared against an expected string.
   *
   * [ "", [], {}, "string", Integer.MIN_VALUE, Integer.MAX_VALUE,
   * Long.MIN_VALUE, Long.MAX_VALUE, true, false, null, { "emptyString":"",
   * "emptyArray":[], "emptyObject":{}, "string":"string", "number":100,
   * "true":true, "false":false, "null":null, "object":{"name":"value"},
   * "array":["one","two"] }, [ "string", Integer.MAX_VALUE, Long.MAX_VALUE,
   * true, false, null, {"name":"value"}, ["one","two"] ],
   * "\"\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
   * ]
   */
  @Test
  public void jsonGeneratorArrayTest2() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json.createGenerator(baos);
      generator.writeStartArray().write("").writeStartArray().writeEnd()
          .writeStartObject().writeEnd().write("string")
          .write(Integer.MIN_VALUE).write(Integer.MAX_VALUE)
          .write(Long.MIN_VALUE).write(Long.MAX_VALUE).write(JsonValue.TRUE)
          .write(JsonValue.FALSE).write(JsonValue.NULL).writeStartObject()
          .write("emptyString", "").writeStartArray("emptyArray").writeEnd()
          .writeStartObject("emptyObject").writeEnd().write("string", "string")
          .write("number", 100).write("true", JsonValue.TRUE)
          .write("false", JsonValue.FALSE).write("null", JsonValue.NULL)
          .writeStartObject("object").write("name", "value").writeEnd()
          .writeStartArray("array").write("one").write("two").writeEnd()
          .writeEnd().writeStartArray().write("string").write(Integer.MAX_VALUE)
          .write(Long.MAX_VALUE).write(JsonValue.TRUE).write(JsonValue.FALSE)
          .write(JsonValue.NULL).writeStartObject().write("name", "value")
          .writeEnd().writeStartArray().write("one").write("two").writeEnd()
          .writeEnd()
          .write(
              "\\\"\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")
          .writeEnd();
      generator.close();

      LOGGER.info("Dump of string: " + baos.toString("UTF-8"));

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "[\"\",[],{},\"string\"," + Integer.MIN_VALUE + ","
          + Integer.MAX_VALUE + "," + Long.MIN_VALUE + "," + Long.MAX_VALUE
          + "," + "true,false,null,{\"emptyString\":\"\",\"emptyArray\":[],"
          + "\"emptyObject\":{},\"string\":\"string\",\"number\":100,\"true\":true,\"false\":false,"
          + "\"null\":null,\"object\":{\"name\":\"value\"},"
          + "\"array\":[\"one\",\"two\"]},[\"string\"," + Integer.MAX_VALUE
          + "," + Long.MAX_VALUE + ",true,false,null,"
          + "{\"name\":\"value\"},[\"one\",\"two\"]],"
          + "\"\\\\\\\"\\\\\\\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM\""
          + "]";

      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorArrayTest2 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorArrayTest2 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorArrayTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:131;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:327; JSONP:JAVADOC:339; JSONP:JAVADOC:329;
   * JSONP:JAVADOC:321; JSONP:JAVADOC:323; JSONP:JAVADOC:332; JSONP:JAVADOC:337;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonArray.
   * The output is written to a writer, read back as a JsonArray and compared
   * against an expected JsonArray.
   *
   * [ "", [], {}, "string", Integer.MIN_VALUE, Integer.MAX_VALUE,
   * Long.MIN_VALUE, Long.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE,
   * Integer.MAX_VALUE, Integer.MIN_VALUE, true, false, null, {
   * "emptyString":"", "emptyArray":[], "emptyObject":{}, "string":"string",
   * "number":100, "true":true, "false":false, "null":null,
   * "object":{"name":"value",{"foo":"bar"}},
   * "array":["one","two",["foo","bar"]] }, [ "string", Integer.MAX_VALUE,
   * Long.MAX_VALUE, Double.MAX_VALUE, Integer.MAX_VALUE true, false, null,
   * {"name":"value"}, ["one","two"] ],
   * "\"\\!@#$%^&*()_+|~1234567890-=`[]{}:;',./<>? qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM"
   * ]
   */
  @Test
  public void jsonGeneratorArrayTest3() {
    try {
      JsonArray expJsonArray = buildJsonArray();
      String jsonText = generateJsonArray();
      LOGGER.info("generator json text: " + jsonText);

      JsonReader reader = Json.createReader(new StringReader(jsonText));
      JsonArray actJsonArray = (JsonArray) reader.read();

      // Do comparison
      LOGGER.info("Compare expJsonArray and actJsonArray for equality");
      assertTrue(JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray), "jsonGeneratorArrayTest3 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorArrayTest3 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorArrayTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:339; JSONP:JAVADOC:341; JSONP:JAVADOC:115;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:163; JSONP:JAVADOC:289;
   * JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonArray.
   * Encoding is done in UTF-16BE. The output is written to an OutputStream as
   * UTF-16BE encoding, read back as a string using UTF-16BE encoding and
   * filtered to remove whitespace and is compared against an expected string.
   *
   * [ {"string":"string","number":1,"true":true,"false":false,"null":null},
   * ["string", 1, true, false, null] ]
   *
   */
  @Test
  public void jsonGeneratorArrayTest4() {
    try {
      LOGGER.info("Create generator output in UTF-16BE encoding.");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_16BE);
      generateSimpleJsonArray(generator);

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "[{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},[\"string\",1,true,false,null]]";
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-16BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16BE"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorArrayTest4 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorArrayTest4 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorArrayTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:131; JSONP:JAVADOC:339; JSONP:JAVADOC:319;
   * JSONP:JAVADOC:289;
   * 
   * @test_Strategy: Test generation of array data with multiple unicode chars
   * in data. The output is written to a writer, read back using a reader as a
   * JsonArray and extracts the JsonString value out and compares it against the
   * expected JsonString value.
   *
   * Generate the following array of unicode char(s):
   *
   * ["\u0000\u000f\u001f\u00ff\uff00\uffff"]
   */
  @Test
  public void jsonGeneratorArrayTest5() {
    JsonReader reader = null;
    String expUnicodeChars = "\u0000\u000f\u001f\u00ff\uff00\uffff";
    try {

      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("\u0000\u000f\u001f\u00ff\uff00\uffff")
          .writeEnd();
      generator.close();
      sWriter.close();

      LOGGER.info("Testing read of " + sWriter.toString());
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonArray jsonArray = reader.readArray();
      String actUnicodeChars = jsonArray.getJsonString(0).getString();
      reader.close();
      LOGGER.info("actUnicodeChars=" + actUnicodeChars);

      assertTrue(JSONP_Util.assertEquals(expUnicodeChars, actUnicodeChars), "jsonGeneratorArrayTest5 Failed");
    } catch (Exception e) {
      fail("JsonGeneratorArrayTest5 Failed. Exception occurred:", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectConfigTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:289;
   * JSONP:JAVADOC:162; JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * This test uses the configuration feature to PRETTY PRINT. The output is
   * written to a Writer, read back as a string and filtered to remove
   * whitespace and is compared against an expected string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] } Tests following API call:
   *
   * JsonGenerator generator = Json.createGeneratorFactory(Map<String,
   * ?>).createGenerator(Writer)
   */
  @Test
  public void jsonGeneratorObjectConfigTest1() {
    try {
      LOGGER.info("Create JsonGenerator using configuration with PRETTY_PRINTING");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig())
          .createGenerator(sWriter);
      generateSimpleJsonObject(generator);

      // Dump JsonText output with PRETTY_PRINTING feature
      LOGGER.info("PRETTY_PRINTING feature\n" + sWriter.toString());

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(sWriter.toString());
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectConfigTest1 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectConfigTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectConfigTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:289;
   * JSONP:JAVADOC:200; JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * This test uses the configuration feature to PRETTY PRINT. The output is
   * written to a OutputStream, read back as a string and filtered to remove
   * whitespace and is compared against an expected string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] } Tests following API call:
   *
   * JsonGenerator generator = Json.createGeneratorFactory(Map<String,
   * ?>).createGenerator(OutputStream)
   */
  @Test
  public void jsonGeneratorObjectConfigTest2() {
    try {
      LOGGER.info("Create JsonGenerator using configuration with PRETTY_PRINTING");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig())
          .createGenerator(baos);
      generateSimpleJsonObject(generator);

      // Dump JsonText output with PRETTY_PRINTING feature
      LOGGER.info("PRETTY_PRINTING feature\n" + baos.toString("UTF-8"));

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectConfigTest2 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectConfigTest2 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectEncodingTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:163;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * The output is written to an OutputStream using UTF-8 encoding, read back as
   * a string and filtered to remove whitespace and is compared against an
   * expected string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] }
   */
  @Test
  public void jsonGeneratorObjectEncodingTest1() {
    try {
      LOGGER.info("Create JsonGenerator using UTF-8 encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_8);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generator Output=" + baos.toString("UTF-8"));

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-8 encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectEncodingTest1 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectEncodingTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorObjectEncodingTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:289;
   * JSONP:JAVADOC:163; JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   * This test uses the configuration feature to PRETTY PRINT. The output is
   * written to an OutputStream using UTF-16BE encoding, read back as a string
   * and filtered to remove whitespace and is compared against an expected
   * string.
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] } Tests the following API
   * call:
   *
   * JsonGenerator generator = Json.createGeneratorFactory(Map<String,
   * ?>).createGenerator(OutputStream, Charset);
   */
  @Test
  public void jsonGeneratorObjectEncodingTest2() {
    try {
      LOGGER.info(
          "Create JsonGenerator using configuration with PRETTY_PRINTING using UTF-16BE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig())
          .createGenerator(baos, JSONP_Util.UTF_16BE);
      generateSimpleJsonObject(generator);

      // Dump JsonText output with PRETTY_PRINTING feature
      LOGGER.info("PRETTY_PRINTING feature\n" + baos.toString("UTF-16BE"));

      // Do comparison
      LOGGER.info("Create expected JSON text with no whitespace");
      String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-16BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16BE"));
      assertTrue(JSONP_Util.assertEqualsJsonText(expJson, actJson), "jsonGeneratorObjectEncodingTest2 Failed");

    } catch (Exception e) {
      fail("jsonGeneratorObjectEncodingTest2 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGeneratorUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * JSONP:JAVADOC:295; JSONP:JAVADOC:304; JSONP:JAVADOC:292; JSONP:JAVADOC:317;
   * JSONP:JAVADOC:325; JSONP:JAVADOC:319; JSONP:JAVADOC:115; JSONP:JAVADOC:163;
   * JSONP:JAVADOC:289; JSONP:JAVADOC:416;
   * 
   * @test_Strategy: Tests various JsonGenerator API's to create a JsonObject.
   *
   * The output is written to an OutputStream using all supported UTF encodings
   * and read back as a string and filtered to remove whitespace and is compared
   * against an expected string. The following UTF encodings are tested:
   *
   * UTF8 UTF16 UTF16LE UTF16BE UTF32LE UTF32BE
   *
   * { "object":{"string":"string","number":1,"true":true,"false":false,"null":
   * null}, "array":["string", 1, true, false, null] }
   */
  @Test
  public void jsonGeneratorUTFEncodedTests() {
    boolean pass = true;
    LOGGER.info(
        "Create expected JSON text with no whitespace for use with comparison");
    String expJson = "{\"object\":{\"string\":\"string\",\"number\":1,\"true\":true,\"false\":false,\"null\":null},\"array\":[\"string\",1,true,false,null]}";
    try {
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-8]");
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-8 encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_8);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-8"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-8 encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing generation to UTF-8 encoding: " + e);
    }
    try {
      LOGGER.info(
          "------------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-16]");
      LOGGER.info(
          "------------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-16 encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_16);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-16"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-16 encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing generation to UTF-16 encoding: " + e);
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-16LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-16LE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_16LE);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-16LE"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-16LE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16LE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning(
          "Exception occurred testing generation to UTF-16LE encoding: " + e);
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-16BE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-16BE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_16BE);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-16BE"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-16BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-16BE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning(
          "Exception occurred testing generation to UTF-16BE encoding: " + e);
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-32LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-32LE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_32LE);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-32LE"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-32LE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-32LE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning(
          "Exception occurred testing generation to UTF-32LE encoding: " + e);
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createGeneratorFactory(Map<String,?>).createGenerator(OutputStream, Charset) as UTF-32BE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------------");
      LOGGER.info("Create JsonGenerator using UTF-32BE encoding");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig())
          .createGenerator(baos, JSONP_Util.UTF_32BE);
      generateSimpleJsonObject(generator);

      // Dump JsonText output
      LOGGER.info("Generated Output=" + baos.toString("UTF-32BE"));

      // Do comparison
      LOGGER.info(
          "Read the JSON text back from OutputStream using UTF-32BE encoding removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(baos.toString("UTF-32BE"));
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

    } catch (Exception e) {
      pass = false;
      LOGGER.warning(
          "Exception occurred testing generation to UTF-32BE encoding: " + e);
    }
    assertTrue(pass, "jsonGeneratorUTFEncodedTests Failed");
  }

  /*
   * @testName: jsonGeneratorExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:339; JSONP:JAVADOC:341; JSONP:JAVADOC:115;
   * JSONP:JAVADOC:293; JSONP:JAVADOC:296; JSONP:JAVADOC:299; JSONP:JAVADOC:302;
   * JSONP:JAVADOC:305; JSONP:JAVADOC:308; JSONP:JAVADOC:311; JSONP:JAVADOC:315;
   * JSONP:JAVADOC:297; JSONP:JAVADOC:303; JSONP:JAVADOC:306; JSONP:JAVADOC:309;
   * JSONP:JAVADOC:312; JSONP:JAVADOC:316; JSONP:JAVADOC:336; JSONP:JAVADOC:335;
   * JSONP:JAVADOC:290; JSONP:JAVADOC:331; JSONP:JAVADOC:381; JSONP:JAVADOC:382;
   * JSONP:JAVADOC:350; JSONP:JAVADOC:352; JSONP:JAVADOC:354; JSONP:JAVADOC:356;
   * JSONP:JAVADOC:358; JSONP:JAVADOC:360; JSONP:JAVADOC:362; JSONP:JAVADOC:364;
   * JSONP:JAVADOC:366; JSONP:JAVADOC:347; JSONP:JAVADOC:348; JSONP:JAVADOC:368;
   * JSONP:JAVADOC:370; JSONP:JAVADOC:372; JSONP:JAVADOC:374;
   * 
   * @test_Strategy: Tests various exception test cases.
   *
   * NumberFormatException JsonGenerationException
   *
   */
  @Test
  public void jsonGeneratorExceptionTests() {
    boolean pass = true;

    // Test NumberFormatException for write(double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(Double.NaN);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test NumberFormatException for write(double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(Double.NEGATIVE_INFINITY);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test NumberFormatException for write(double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(Double.POSITIVE_INFINITY);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test NumberFormatException for write(String,double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(String,double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("badnumber", Double.NaN);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test NumberFormatException for write(String,double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(String,double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("badnumber", Double.NEGATIVE_INFINITY);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test NumberFormatException for write(String,double) if value is
    // Not-a-Number(NaN) or infinity
    try {
      LOGGER.info(
          "Trip NumberFormatException for write(String,double) if value is Not-a-Number(NaN) or infinity");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("badnumber", Double.POSITIVE_INFINITY);
      LOGGER.warning("Did not get expected NumberFormatException");
      pass = false;
    } catch (NumberFormatException e) {
      LOGGER.info("Caught expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonGenerationExceptipn if an incomplete JSON is generated.
    try {
      LOGGER.info(
          "Trip JsonGenerationExceptipn if an incomplete JSON is generated.");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("name", "value");
      generator.close();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonGenerationExceptipn if an incomplete JSON is generated.
    try {
      LOGGER.info(
          "Trip JsonGenerationExceptipn if an incomplete JSON is generated.");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string");
      generator.close();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(JsonValue) if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(JsonValue) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(JsonValue.TRUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String) if not called within array
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("name");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(BigInteger) if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(BigInteger) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject()
          .write(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(BigDecimal) if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(BigDecimal) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(BigDecimal.valueOf(Integer.MIN_VALUE));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(int) if not called within array
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(int) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(Integer.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(long) if not called within array
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(long) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(Long.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(double) if not called within array
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(double) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(Double.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(boolean) if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(boolean) if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write(true);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeNull() if not called within array
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeNull() if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeNull();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeStartArray() if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeStartArray() if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeStartArray();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeStartObject() if not called within
    // array context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeStartObject() if not called within array context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeStartObject();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,JsonValue) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,JsonValue) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", JsonValue.TRUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,String) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,String) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", "name");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,BigInteger) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,BigInteger) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string",
          new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,BigDecimal) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,BigDecimal) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string",
          BigDecimal.valueOf(Integer.MIN_VALUE));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,int) if not called within
    // object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,int) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", Integer.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,long) if not called within
    // object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,long) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", Long.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,double) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,double) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", Double.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,boolean) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,boolean) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write("string", true);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeNull(String) if not called within
    // object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeNull(String) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeNull("string");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeStartArray(String) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeStartArray(String) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeStartArray("string");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeStartObject(String) if not called
    // within object context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeStartObject(String) if not called within object context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeStartObject("string");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,JsonValue) when invoked
    // after the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,JsonValue) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name", "value");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeEnd() when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeEnd() when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().writeEnd();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,BigInteger) when invoked
    // after the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,BigInteger) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name",
          new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,BigDecimal) when invoked
    // after the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,BigDecimal) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name",
          BigDecimal.valueOf(Integer.MIN_VALUE));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,int) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,int) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name", Integer.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,long) when invoked after
    // the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,long) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name", Long.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,double) when invoked after
    // the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,double) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name", Double.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String,boolean) when invoked after
    // the writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String,boolean) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().write("name", false);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for writeNull(String) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for writeNull(String) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeEnd().writeNull("name");
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(JsonValue) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(JsonValue) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(JsonValue.TRUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(String) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(String) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(JsonValue.TRUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(BigDecimal) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(BigDecimal) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd()
          .write(BigDecimal.valueOf(Integer.MIN_VALUE));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(BigInteger) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(BigInteger) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd()
          .write(new BigInteger(new Integer(Integer.MAX_VALUE).toString()));
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(int) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(int) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(Integer.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(long) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(long) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(Long.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(double) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(double) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(Double.MAX_VALUE);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for write(boolean) when invoked after the
    // writeEnd method is called
    try {
      LOGGER.info(
          "Trip JsonGenerationException for write(boolean) when invoked after the writeEnd method is called");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().write(true);
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Test JsonGenerationException for for writeNull() when invoked with no
    // context
    try {
      LOGGER.info(
          "Trip JsonGenerationException for for writeNull() when invoked with no context");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().writeEnd().writeNull();
      LOGGER.warning("Did not get expected JsonGenerationException");
      pass = false;
    } catch (JsonGenerationException e) {
      LOGGER.info("Caught expected JsonGenerationException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonGeneratorExceptionTests Failed");
  }

  /*
   * @testName: flushTest
   * 
   * @assertion_ids: JSONP:JAVADOC:131; JSONP:JAVADOC:289; JSONP:JAVADOC:291;
   * JSONP:JAVADOC:340; JSONP:JAVADOC:341; JSONP:JAVADOC:342;
   * 
   * @test_Strategy: Generate some partial Json, flush output and compare
   * output. Generate additional Json to complete, flush output again and
   * compare. Test passes if comparisons are correct.
   *
   * {"object":{},"array":[]}
   */
  @Test
  public void flushTest() {
    boolean pass = true;
    try {
      LOGGER.info("Generate some partial Json and flush output.");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().writeStartObject("object").writeEnd()
          .flush();

      // Do comparison 1
      LOGGER.info("Create expected partial JSON text with no whitespace");
      String expJson = "{\"object\":{}";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      String actJson = JSONP_Util.removeWhitespace(sWriter.toString());
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

      LOGGER.info("Generate additional Json to complete and flush output.");
      generator.writeStartArray("array").writeEnd().writeEnd().flush();

      // Do comparison 2
      expJson = "{\"object\":{},\"array\":[]}";
      LOGGER.info("Read the JSON text back from Writer removing whitespace");
      actJson = JSONP_Util.removeWhitespace(sWriter.toString());
      if (!JSONP_Util.assertEqualsJsonText(expJson, actJson))
        pass = false;

      generator.close();

    } catch (Exception e) {
      fail("flushTest Failed: ", e);
    }
    assertTrue(pass, "flushTest Failed");
  }

  /*
   * @testName: jsonGeneratorIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:346; JSONP:JAVADOC:551;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  @Test
  public void jsonGeneratorIOErrorTests() {
    boolean pass = true;

    // Trip JsonException if there is an i/o error on JsonGenerator.close()
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on JsonGenerator.close().");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      JsonGenerator generator = Json.createGenerator(mbw);
      generator.writeStartObject().writeEnd();
      mbw.setThrowIOException(true);
      LOGGER.info("Calling JsonGenerator.close()");
      generator.close();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on JsonGenerator.flush()
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on JsonGenerator.flush().");
      MyBufferedWriter mbw = new MyBufferedWriter(new StringWriter());
      JsonGenerator generator = Json.createGenerator(mbw);
      generator.writeStartObject().writeEnd();
      mbw.setThrowIOException(true);
      LOGGER.info("Calling JsonGenerator.flush()");
      generator.flush();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonGeneratorIOErrorTests Failed");
  }

  /*
   * @testName: jsonGeneratorDocumentRootTest
   * 
   * @assertion_ids: JSONP:JAVADOC:317; JSONP:JAVADOC:319; JSONP:JAVADOC:321;
   * JSONP:JAVADOC:323; JSONP:JAVADOC:325; JSONP:JAVADOC:327; JSONP:JAVADOC:329;
   * JSONP:JAVADOC:332; JSONP:JAVADOC:583; JSONP:JAVADOC:584; JSONP:JAVADOC:585;
   * JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588; JSONP:JAVADOC:662;
   * JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665; JSONP:JAVADOC:666;
   * JSONP:JAVADOC:667; JSONP:JAVADOC:289; JSONP:JAVADOC:341; JSONP:JAVADOC:672;
   * 
   * @test_Strategy: Tests RFC 7159 grammar changes:<br> {@code
   * "JSON-text = ws value ws"}<br> {@code
   * "value     = false / null / true / object / array / number / string"}
   */
  @Test
  public void jsonGeneratorDocumentRootTest() {
    Generator genTest = new Generator();
    final TestResult result = genTest.test();
    result.eval();
  }

  /*
   * @testName: jsonGeneratorStreamNotClosedTest
   */
  @Test
  public void jsonGeneratorStreamNotClosedTest() {
    ByteArrayOutputStreamCloseChecker stream = new ByteArrayOutputStreamCloseChecker();
    JsonGenerator gen = Json.createGenerator(stream);
    try {
          gen.writeStartObject();
          gen.write("foo", "bar");
          // no end object
          gen.close();
          throw new AssertionFailedError("It is expected a JsonGenerationException");
    } catch (JsonGenerationException e) {
          if (stream.closed) {
              // Stream should not be closed
              throw new AssertionFailedError("The underlying stream is closed but it shouldn't because JSON object was not completed");
          }
    } 
  }

  /*
   * @testName: jsonGeneratorStreamClosedTest
   */
  @Test
  public void jsonGeneratorStreamClosedTest() {
    ByteArrayOutputStreamCloseChecker stream = new ByteArrayOutputStreamCloseChecker();
    JsonGenerator gen = Json.createGenerator(stream);
    gen.writeStartObject();
    gen.write("foo", "bar");
    gen.writeEnd();
    gen.close();
    if (!stream.closed) {
        // Stream should be closed
        throw new AssertionFailedError("The underlying stream has to be closed because JSON object was completed");
    }
  }

  private static class ByteArrayOutputStreamCloseChecker extends ByteArrayOutputStream {
    private boolean closed = false;
    @Override
    public void close() throws IOException {
        closed = true;
    }
  }
}

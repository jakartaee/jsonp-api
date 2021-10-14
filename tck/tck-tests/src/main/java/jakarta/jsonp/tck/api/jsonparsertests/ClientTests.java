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
package jakarta.jsonp.tck.api.jsonparsertests;

import static jakarta.jsonp.tck.api.common.JsonAssert.valueToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import jakarta.json.stream.JsonParsingException;
import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.JSONP_Data;
import jakarta.jsonp.tck.common.JSONP_Util;
import jakarta.jsonp.tck.common.MyBufferedInputStream;
import jakarta.jsonp.tck.lib.harness.Fault;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  /*
   * Utitity method to parse various JsonObjectUTF encoded files
   */
  private boolean parseAndVerify_JsonObjectUTF(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "stringName", "stringValue");
      JSONP_Util.testKeyStartObjectValue(parser, "objectName");
      JSONP_Util.testKeyStringValue(parser, "foo", "bar");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "arrayName");
      JSONP_Util.testIntegerValue(parser, 1);
      JSONP_Util.testIntegerValue(parser, 2);
      JSONP_Util.testIntegerValue(parser, 3);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonObjectWithAllTypesOfData
   */
  private boolean parseAndVerify_JsonObjectWithAllTypesOfData(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyIntegerValue(parser, "intPositive", 100);
      JSONP_Util.testKeyIntegerValue(parser, "intNegative", -100);
      JSONP_Util.testKeyLongValue(parser, "longMax", 9223372036854775807L);
      JSONP_Util.testKeyLongValue(parser, "longMin", -9223372036854775808L);
      JSONP_Util.testKeyDoubleValue(parser, "fracPositive", (double) 0.5);
      JSONP_Util.testKeyDoubleValue(parser, "fracNegative", (double) -0.5);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive1", (double) 7e3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive2", (double) 7e+3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive3", (double) 9E3);
      JSONP_Util.testKeyDoubleValue(parser, "expPositive4", (double) 9E+3);
      JSONP_Util.testKeyDoubleValue(parser, "expNegative1", (double) 7e-3);
      JSONP_Util.testKeyDoubleValue(parser, "expNegative2", (double) 7E-3);
      JSONP_Util.testKeyStringValue(parser, "asciiChars",
          JSONP_Data.asciiCharacters);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonObjectWithLotsOfNestedObjectsData
   */
  private boolean parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      for (int i = 1; i < 31; i++) {
        JSONP_Util.testKeyStartObjectValue(parser, "nested" + i);
        JSONP_Util.testKeyStringValue(parser, "name" + i, "value" + i);
      }
      for (int i = 1; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithLotsOfNestedObjectsData
   */
  private boolean parseAndVerify_JsonArrayWithLotsOfNestedObjectsData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name1", "value1");
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testKeyStartObjectValue(parser, "nested" + i);
        JSONP_Util.testKeyStringValue(parser, "name" + i, "value" + i);
      }
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithLotsOfNestedArraysData
   */
  private boolean parseAndVerify_JsonArrayWithLotsOfNestedArraysData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "name1");
      JSONP_Util.testStringValue(parser, "value1");
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testStringValue(parser, "nested" + i);
        JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser, "name" + i);
        JSONP_Util.testStringValue(parser, "value" + i);
      }
      for (int i = 2; i < 31; i++) {
        JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      }
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithMultipleArraysData
   */
  private boolean parseAndVerify_JsonArrayWithMultipleArraysData(
      JsonParser parser) throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "object", "object");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testDoubleValue(parser, (double) 7e7);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "object2", "object2");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /*
   * Utitity method to parse JsonArrayWithAllTypesOfData
   */
  private boolean parseAndVerify_JsonArrayWithAllTypesOfData(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "");
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "emptyString", "");
      JSONP_Util.testKeyStartArrayValue(parser, "emptyArray");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testKeyStartObjectValue(parser, "emptyObject");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "string", "string");
      JSONP_Util.testKeyIntegerValue(parser, "number", 100);
      JSONP_Util.testKeyTrueValue(parser, "true");
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testKeyNullValue(parser, "null");
      JSONP_Util.testKeyStartObjectValue(parser, "object");
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testKeyStartArrayValue(parser, "array");
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "string");
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testTrueValue(parser, JsonParser.Event.VALUE_TRUE);
      JSONP_Util.testFalseValue(parser, JsonParser.Event.VALUE_FALSE);
      JSONP_Util.testNullValue(parser, JsonParser.Event.VALUE_NULL);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStringValue(parser, "name", "value");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.testEventType(parser, JsonParser.Event.START_ARRAY);
      JSONP_Util.testStringValue(parser, "one");
      JSONP_Util.testStringValue(parser, "two");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.testIntegerValue(parser, 100);
      JSONP_Util.testIntegerValue(parser, -100);
      JSONP_Util.testLongValue(parser, 9223372036854775807L);
      JSONP_Util.testLongValue(parser, -9223372036854775808L);
      JSONP_Util.testDoubleValue(parser, (double) 0.5);
      JSONP_Util.testDoubleValue(parser, (double) -0.5);
      JSONP_Util.testDoubleValue(parser, (double) 7e3);
      JSONP_Util.testDoubleValue(parser, (double) 7e+3);
      JSONP_Util.testDoubleValue(parser, (double) 9E3);
      JSONP_Util.testDoubleValue(parser, (double) 9E+3);
      JSONP_Util.testDoubleValue(parser, (double) 7e-3);
      JSONP_Util.testDoubleValue(parser, (double) 7E-3);
      JSONP_Util.testStringValue(parser, JSONP_Data.asciiCharacters);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  private boolean parseAndVerify_JsonHelloWorld(JsonParser parser)
      throws Exception {
    boolean pass = true;

    try {
      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyStartObjectValue(parser, "greetingObj");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testKeyStringValue(parser, "hello", "world");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testKeyStartArrayValue(parser, "greetingArr");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testStringValue(parser, "hello");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testStringValue(parser, "world");
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_ARRAY);
      JSONP_Util.dumpLocation(parser);
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      JSONP_Util.dumpLocation(parser);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }
    } catch (Exception e) {
      throw e;
    }
    return pass;
  }

  /* Tests */

  /*
   * @testName: jsonParserTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:133; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithAllTypesOfData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(Reader)
   */
  @Test
  public void jsonParserTest1() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("-------------------------------------");
      LOGGER.info("TEST CASE [Json.createParser(Reader)]");
      LOGGER.info("-------------------------------------");
      LOGGER.info("Create Reader from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithAllTypesOfData);
      LOGGER.info("Create JsonParser from the Reader");
      parser = Json.createParser(reader);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      assertTrue(parseAndVerify_JsonObjectWithAllTypesOfData(parser), "jsonParserTest1 Failed");
    } catch (Exception e) {
      fail("jsonParserTest1 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithAllTypesOfData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonObject)
   */
  @Test
  public void jsonParserTest2() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonObject)]");
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "Create JsonObject from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      JsonObject jsonObj = JSONP_Util
          .createJsonObjectFromString(JSONP_Data.jsonObjectWithAllTypesOfData);
      JSONP_Util.dumpJsonObject(jsonObj);
      LOGGER.info("Create JsonParser from the JsonObject");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonObj);
      LOGGER.info("parser=" + parser);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      assertTrue(parseAndVerify_JsonObjectWithAllTypesOfData(parser), "jsonParserTest2 Failed");
    } catch (Exception e) {
      fail("jsonParserTest2 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:133;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectWithLotsOfNestedObjectsData".
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(Reader)
   */
  @Test
  public void jsonParserTest3() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("-------------------------------------------");
      LOGGER.info("TEST CASE [Json.createParser(Reader) again]");
      LOGGER.info("-------------------------------------------");
      LOGGER.info(
          "Create Reader from (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithLotsOfNestedObjectsData);
      LOGGER.info("Create JsonParser from the Reader");
      parser = Json.createParser(reader);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      assertTrue(parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser), "jsonParserTest3 Failed");
    } catch (Exception e) {
      fail("jsonParserTest3 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in "JSONP_Data.jsonObjectithLotsOfNestedObjectsData".
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonObject)
   */
  @Test
  public void jsonParserTest4() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "-----------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonObject object) again]");
      LOGGER.info(
          "-----------------------------------------------------------------------------------------");
      LOGGER.info(
          "Create JsonObject from (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      JsonObject jsonObj = JSONP_Util.createJsonObjectFromString(
          JSONP_Data.jsonObjectWithLotsOfNestedObjectsData);
      JSONP_Util.dumpJsonObject(jsonObj);
      LOGGER.info("Create JsonParser from the JsonObject");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonObj);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithLotsOfNestedObjectsData)");
      assertTrue(parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser), "jsonParserTest4 Failed");
    } catch (Exception e) {
      fail("jsonParserTest4 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:167;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in "JSONP_Data.jsonArrayWithMultipleArraysData". Creates
   * the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(JsonArray)
   */
  @Test
  public void jsonParserTest5() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "---------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(JsonArray)]");
      LOGGER.info(
          "---------------------------------------------------------------------------");
      LOGGER.info(
          "Create JsonArray from (JSONP_Data.jsonArrayWithMultipleArraysData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithMultipleArraysData);
      JSONP_Util.dumpJsonArray(jsonArr);
      LOGGER.info("Create JsonParser from the JsonArray");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithMultipleArraysData)");
      assertTrue(parseAndVerify_JsonArrayWithMultipleArraysData(parser), "jsonParserTest5 Failed");
    } catch (Exception e) {
      fail("jsonParserTest5 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:172; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file "jsonArrayWithAllTypesOfData.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   */
  @Test
  public void jsonParserTest6() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("------------------------------------------");
      LOGGER.info("TEST CASE [Json.createParser(InputStream)]");
      LOGGER.info("------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonArrayWithAllTypesOfData.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonArrayWithAllTypesOfData.json");
      LOGGER.info("Create JsonParser from the InputStream");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithAllTypesOfData.json)");
      assertTrue(parseAndVerify_JsonArrayWithAllTypesOfData(parser), "jsonParserTest6 Failed");
    } catch (Exception e) {
      fail("jsonParserTest6 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest7
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:164; JSONP:JAVADOC:235; JSONP:JAVADOC:237;
   * JSONP:JAVADOC:239; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with a configuration. Verifies
   * PARSING of the JsonObject defined in
   * "JSONP_Data.jsonObjectWithAllTypesOfData". Creates the JsonParser via the
   * following API
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(Reader)
   */
  @Test
  public void jsonParserTest7() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "-------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(Reader)]");
      LOGGER.info(
          "-------------------------------------------------------------------------");
      LOGGER.info("Create a Reader from (JSONP_Data.jsonObjectWithAllTypesOfData)");
      StringReader reader = new StringReader(
          JSONP_Data.jsonObjectWithAllTypesOfData);
      LOGGER.info("Create JsonParser using Reader and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(reader);
      LOGGER.info("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonObjectWithAllTypesOfData)");
      assertTrue(parseAndVerify_JsonObjectWithAllTypesOfData(parser), "jsonParserTest7 Failed");
    } catch (Exception e) {
      fail("jsonParserTest7 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest8
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:167; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with a configuration. Verifies
   * PARSING of the JsonArray defined in
   * "JSONP_Data.jsonArrayWithLotsOfNestedObjectsData". Creates the JsonParser
   * via the following API
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(JsonArray)
   */
  @Test
  public void jsonParserTest8() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(JsonArray)]");
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "Create a JsonArray from (JSONP_Data.jsonArrayWithLotsOfNestedObjectsData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithLotsOfNestedObjectsData);
      JSONP_Util.dumpJsonArray(jsonArr);
      LOGGER.info("Create JsonParser using JsonArray and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      LOGGER.info("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithLotsOfNestedObjectsData)");
      assertTrue(parseAndVerify_JsonArrayWithLotsOfNestedObjectsData(parser), "jsonParserTest8 Failed");
    } catch (Exception e) {
      fail("jsonParserTest8 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest9
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:417; JSONP:JAVADOC:167; JSONP:JAVADOC:235; JSONP:JAVADOC:237;
   * JSONP:JAVADOC:239; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser with an empty configuration.
   * Verifies PARSING of the JsonArray defined in
   * "JSONP_Data.jsonArrayWithMultipleArraysData". Creates the JsonParser via
   * the following API
   * 
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(JsonArray)
   */
  @Test
  public void jsonParserTest9() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(JsonArray)]");
      LOGGER.info(
          "----------------------------------------------------------------------------");
      LOGGER.info(
          "Create JsonArray from (JSONP_Data.jsonArrayWithMultipleArraysData)");
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(
          JSONP_Data.jsonArrayWithMultipleArraysData);
      JSONP_Util.dumpJsonArray(jsonArr);
      LOGGER.info("Create JsonParser using JsonArray and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(jsonArr);
      LOGGER.info("Call JsonParser.toString() to print the JsonArray");
      parser.toString();
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (JSONP_Data.jsonArrayWithMultipleArraysData)");
      assertTrue(parseAndVerify_JsonArrayWithMultipleArraysData(parser), "jsonParserTest9 Failed");
    } catch (Exception e) {
      fail("jsonParserTest9 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest10
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:165; JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource file
   * "jsonObjectWithLotsOfNestedObjectsData.json". Creates the JsonParser via
   * the following API
   * 
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream)
   */
  @Test
  public void jsonParserTest10() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream)]");
      LOGGER.info(
          "------------------------------------------------------------------------------");
      LOGGER.info("Create JsonParser using InputStream and a configuration");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonObjectWithLotsOfNestedObjectsData.json");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream);
      LOGGER.info("Call JsonParser.toString() to print the JsonObject");
      parser.toString();
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectWithLotsOfNestedObjectsData.json)");
      assertTrue(parseAndVerify_JsonObjectWithLotsOfNestedObjectsData(parser), "jsonParserTest10 Failed");
    } catch (Exception e) {
      fail("jsonParserTest10 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest11
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239; JSONP:JAVADOC:375;
   * JSONP:JAVADOC:376; JSONP:JAVADOC:417; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file
   * "jsonArrayWithAllTypesOfDataUTF16BE.json". Use UTF-16BE encoding.
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)
   */
  @Test
  public void jsonParserTest11() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonArrayWithAllTypesOfDataUTF16BE.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonArrayWithAllTypesOfDataUTF16BE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-16BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16BE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithAllTypesOfDataUTF16BE.json)");
      assertTrue(parseAndVerify_JsonArrayWithAllTypesOfData(parser), "jsonParserTest11 Failed");
    } catch (Exception e) {
      fail("jsonParserTest11 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest12
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonArray defined in resource file
   * "jsonArrayWithLotsOfNestedArraysData.json". Use UTF-8 encoding.
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream, Charset)
   */
  @Test
  public void jsonParserTest12() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "---------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream, Charset)]");
      LOGGER.info(
          "---------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonArrayWithLotsOfNestedArraysData.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonArrayWithLotsOfNestedArraysData.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-8 and a configuration");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_8);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonArrayWithLotsOfNestedArraysData.json)");
      assertTrue(parseAndVerify_JsonArrayWithLotsOfNestedArraysData(parser), "jsonParserTest12 Failed");
    } catch (Exception e) {
      fail("jsonParserTest12 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest13
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:201; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   *
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in UTF-16LE encoding resource file
   * "jsonObjectWithAllTypesOfDataUTF16LE.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParserFactory(Map<String,
   * ?>).createParser(InputStream, Charset)
   */
  @Test
  public void jsonParserTest13() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "---------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String, ?>).createParser(InputStream, Charset)]");
      LOGGER.info(
          "---------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectWithAllTypesOfDataUTF16LE.json)");
      InputStream istream = JSONP_Util.getInputStreamFromResource(
          "jsonObjectWithAllTypesOfDataUTF16LE.json");
      LOGGER.info("Create JsonParser from the InputStream using UTF-16LE encoding");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16LE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectWithAllTypesOfDataUTF16LE.json)");
      assertTrue(parseAndVerify_JsonObjectWithAllTypesOfData(parser), "jsonParserTest13 Failed");
    } catch (Exception e) {
      fail("jsonParserTest13 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: jsonParserTest14
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:172;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:477;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource file "jsonHelloWorld.json.json".
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   */
  @Test
  public void jsonParserTest14() {
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("------------------------------------------");
      LOGGER.info("TEST CASE [Json.createParser(InputStream)]");
      LOGGER.info("------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonHelloWorld.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonHelloWorld.json");
      LOGGER.info("Create JsonParser from the InputStream");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonHelloWorld.json)");
      assertTrue(parseAndVerify_JsonHelloWorld(parser), "jsonParserTest14 Failed");
    } catch (Exception e) {
      fail("jsonParserTest14 Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: parseUTFEncodedTests
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:122; JSONP:JAVADOC:417;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource files:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16.json
   * jsonObjectEncodingUTF16LE.json jsonObjectEncodingUTF16BE.json
   * jsonObjectEncodingUTF32LE.json jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser =
   * Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset)
   *
   * For each supported encoding supported by JSON RFC parse the JsonObject and
   * verify we get the expected results. The Charset encoding is passed in as an
   * argument for each encoding type tested.
   */
  @Test
  public void parseUTFEncodedTests() {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-8]");
      LOGGER.info(
          "-----------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-8");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_8);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF8.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-8 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16]");
      LOGGER.info(
          "------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-16");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-16 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-16LE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16LE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-16LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-16BE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-16BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_16BE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-16BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-32LE]");
      LOGGER.info(
          "--------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-32LE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_32LE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-32LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "-------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParserFactory(Map<String,?>).createParser(InputStream, Charset) as UTF-32BE]");
      LOGGER.info(
          "-------------------------------------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream with character encoding UTF-32BE");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(istream, JSONP_Util.UTF_32BE);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-32BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "parseUTFEncodedTests Failed");
  }

  /*
   * @testName: parseUTFEncodedTests2
   * 
   * @assertion_ids: JSONP:JAVADOC:117; JSONP:JAVADOC:120; JSONP:JAVADOC:122;
   * JSONP:JAVADOC:172; JSONP:JAVADOC:235; JSONP:JAVADOC:237; JSONP:JAVADOC:239;
   * JSONP:JAVADOC:375; JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Tests the JsonParser parser. Verifies PARSING of the
   * JsonObject defined in resource files and auto-detecting the encoding:
   *
   * jsonObjectEncodingUTF8.json jsonObjectEncodingUTF16LE.json
   * jsonObjectEncodingUTF16BE.json jsonObjectEncodingUTF32LE.json
   * jsonObjectEncodingUTF32BE.json
   * 
   * Creates the JsonParser via the API:
   *
   * JsonParser parser = Json.createParser(InputStream)
   *
   * For each supported encoding supported by JSON RFC the above should
   * auto-detect the encoding and verify we get the expected results.
   */
  @Test
  public void parseUTFEncodedTests2() {
    boolean pass = true;
    JsonParser parser = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info(
          "-------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-8]");
      LOGGER.info(
          "-------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF8.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF8.json");
      LOGGER.info(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-8");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF8.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-8 encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-16LE]");
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16LE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-16LE");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-16LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-16BE]");
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF16BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF16BE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-16BE");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF16BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-16BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-32LE]");
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32LE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32LE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-32LE");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32LE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-32LE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }
    try {
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [Json.createParser(InputStream) and auto-detect as UTF-32BE]");
      LOGGER.info(
          "----------------------------------------------------------------------");
      LOGGER.info(
          "Get InputStream from data file as resource (jsonObjectEncodingUTF32BE.json)");
      InputStream istream = JSONP_Util
          .getInputStreamFromResource("jsonObjectEncodingUTF32BE.json");
      LOGGER.info(
          "Create JsonParser from the InputStream and auto-detect character encoding UTF-32BE");
      parser = Json.createParser(istream);
      LOGGER.info(
          "Verify that JSON Parser Events/Data matches (jsonObjectEncodingUTF32BE.json)");
      if (!parseAndVerify_JsonObjectUTF(parser))
        pass = false;
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Exception occurred testing parsing of UTF-32BE encoding: " + e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    assertTrue(pass, "parseUTFEncodedTests2 Failed");
  }

  /*
   * @testName: jsonParserIsIntegralNumberTest
   * 
   * @assertion_ids: JSONP:JAVADOC:120; JSONP:JAVADOC:133; JSONP:JAVADOC:375;
   * JSONP:JAVADOC:376;
   * 
   * @test_Strategy: Test JsonParser.isIntegralNumber() method.
   */
  @Test
  public void jsonParserIsIntegralNumberTest() {
    boolean pass = true;
    JsonParser parser = null;
    String jsonTestString = "[123, 12345.45]";
    try {
      LOGGER.info("Create JsonParser");
      parser = Json.createParser(new StringReader(jsonTestString));
      // INTEGRAL NUMBER TEST
      JsonParser.Event event = JSONP_Util.getNextSpecificParserEvent(parser,
          JsonParser.Event.VALUE_NUMBER); // e=JsonParser.Event.VALUE_NUMBER
      JSONP_Util.dumpEventType(event);
      if (!JSONP_Util.assertEqualsJsonNumberType(parser.isIntegralNumber(),
          JSONP_Util.INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(123, parser.getInt()))
          pass = false;
      }
      // NON_INTEGRAL NUMBER TEST
      event = JSONP_Util.getNextSpecificParserEvent(parser,
          JsonParser.Event.VALUE_NUMBER); // e=JsonParser.Event.VALUE_NUMBER
      JSONP_Util.dumpEventType(event);
      if (!JSONP_Util.assertEqualsJsonNumberType(parser.isIntegralNumber(),
          JSONP_Util.NON_INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(12345.45,
            parser.getBigDecimal().doubleValue()))
          pass = false;
      }

    } catch (Exception e) {
      fail("jsonParserIsIntegralNumberTest Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    assertTrue(pass, "jsonParserIsIntegralNumberTest Failed");
  }

  private boolean tripIllegalStateException(JsonParser parser,
      JsonParser.Event event) {
    boolean pass = true;

    // Check in case event is null
    if (event == null) {
      LOGGER.warning("event is null - unexpected.");
      return false;
    }
    LOGGER.info("Event=" + JSONP_Util.getEventTypeString(event));
    LOGGER.info("Testing call to JsonParser.getString()");
    if (event != JsonParser.Event.VALUE_STRING
        && event != JsonParser.Event.VALUE_NUMBER
        && event != JsonParser.Event.KEY_NAME) {
      try {
        LOGGER.info("Trip IllegalStateException by calling JsonParser.getString()");
        String string = parser.getString();
        pass = false;
        LOGGER.warning("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        LOGGER.warning("Caught unexpected exception: " + e);
      }
    } else {
      LOGGER.info("No testing for IllegalStateException for this scenario.");
    }

    LOGGER.info("Testing call to JsonParser.isIntegralNumber()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        LOGGER.info(
            "Trip IllegalStateException by calling JsonParser.isIntegralNumber()");
        boolean numberType = parser.isIntegralNumber();
        pass = false;
        LOGGER.warning("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        LOGGER.warning("Caught unexpected exception: " + e);
      }
    } else {
      LOGGER.info("No testing for IllegalStateException for this scenario.");
    }

    LOGGER.info("Testing call to JsonParser.getBigDecimal()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        LOGGER.info(
            "Trip IllegalStateException by calling JsonParser.getBigDecimal()");
        BigDecimal number = parser.getBigDecimal();
        pass = false;
        LOGGER.warning("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        LOGGER.warning("Caught unexpected exception: " + e);
      }
    } else {
      LOGGER.info("No testing for IllegalStateException for this scenario.");
    }

    LOGGER.info("Testing call to JsonParser.getInt()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        LOGGER.info("Trip IllegalStateException by calling JsonParser.getInt()");
        int number = parser.getInt();
        pass = false;
        LOGGER.warning("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        LOGGER.warning("Caught unexpected exception: " + e);
      }
    } else {
      LOGGER.info("No testing for IllegalStateException for this scenario.");
    }

    LOGGER.info("Testing call to JsonParser.getLong()");
    if (event != JsonParser.Event.VALUE_NUMBER) {
      try {
        LOGGER.info("Trip IllegalStateException by calling JsonParser.getLong()");
        long number = parser.getLong();
        pass = false;
        LOGGER.warning("Failed to throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("Got expected IllegalStateException");
      } catch (Exception e) {
        pass = false;
        LOGGER.warning("Caught unexpected exception: " + e);
      }
    } else {
      LOGGER.info("No testing for IllegalStateException for this scenario.");
    }
    return pass;
  }

  /*
   * @testName: jsonParserIllegalExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:121; JSONP:JAVADOC:123; JSONP:JAVADOC:236;
   * JSONP:JAVADOC:238; JSONP:JAVADOC:240;
   * 
   * @test_Strategy: Test JsonParser exception conditions. Trip the following
   * exceptions:
   *
   * java.lang.IllegalStateException
   */
  @Test
  public void jsonParserIllegalExceptionTests() {
    boolean pass = true;
    JsonParser parser = null;
    String jsonTestString = "[\"string\",100,false,null,true,{\"foo\":\"bar\"}]";
    try {
      LOGGER.info("Create JsonParser");
      parser = Json.createParserFactory(JSONP_Util.getEmptyConfig())
          .createParser(new StringReader(jsonTestString));
      JsonParser.Event event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.START_ARRAY */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_STRING */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_NUMBER */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_FALSE */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_NULL */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_TRUE */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.START_OBJECT */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.KEY_NAME */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.VALUE_STRING */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.END_OBJECT */
      if (!tripIllegalStateException(parser, event))
        pass = false;
      event = JSONP_Util
          .getNextParserEvent(parser); /* e=JsonParser.Event.END_ARRAY */
      if (!tripIllegalStateException(parser, event))
        pass = false;
    } catch (Exception e) {
      fail("jsonParserIllegalExceptionTests Failed: ", e);
    } finally {
      try {
        parser.close();
      } catch (Exception e) {
      }
    }

    assertTrue(pass, "jsonParserIllegalExceptionTests Failed");
  }

  /*
   * @testName: jsonParserIOErrorTests
   * 
   * @assertion_ids: JSONP:JAVADOC:207; JSONP:JAVADOC:389; JSONP:JAVADOC:415;
   * 
   * @test_Strategy: Tests for JsonException for testable i/o errors.
   *
   */
  @SuppressWarnings("ConvertToTryWithResources")
  @Test
  public void jsonParserIOErrorTests() {
    boolean pass = true;

    String jsonText = "{\"name1\":\"value1\",\"name2\":\"value2\"}";

    // Trip JsonException if there is an i/o error on
    // Json.createParser(InputStream)
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on Json.createParser(InputStream).");
      LOGGER.info("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is, true);
      LOGGER.info("Calling Json.createParser(InputStream)");
      JsonParser parser = Json.createParser(mbi);
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on JsonParser.next()
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on JsonParser.next().");
      LOGGER.info("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is, true);
      JsonParser parser = Json.createParser(mbi);
      LOGGER.info("Calling JsonParser.next()");
      parser.next();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonException if there is an i/o error on JsonParser.close()
    try {
      LOGGER.info(
          "Trip JsonException if there is an i/o error on JsonParser.close().");
      LOGGER.info("Parsing " + jsonText);
      InputStream is = JSONP_Util.getInputStreamFromString(jsonText);
      MyBufferedInputStream mbi = new MyBufferedInputStream(is);
      JsonParser parser = Json.createParser(mbi);
      mbi.setThrowIOException(true);
      LOGGER.info("Calling JsonParser.close()");
      parser.close();
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonParserIOErrorTests Failed");
  }

  /*
   * @testName: jsonParserExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:390; JSONP:JAVADOC:391;
   * 
   * @test_Strategy: Tests for the following exception test cases:
   *
   * JsonParsingException - if incorrect JSON is encountered while advancing
   * parser to next state NoSuchElementException - if there are no more parsing
   * states
   *
   */
  @Test
  public void jsonParserExceptionTests() {
    boolean pass = true;

    // Trip JsonParsingException for JsonParser.next() if incorrect JSON is
    // encountered
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonParser.next() if incorrect JSON is encountered");
      InputStream is = JSONP_Util.getInputStreamFromString("}{");
      JsonParser parser = Json.createParser(is);
      parser.next();
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip NoSuchElementException for JsonParser.next() if no more parsing
    // states
    try {
      LOGGER.info(
          "Trip NoSuchElementException for JsonParser.next() if no more parsing states");
      InputStream is = JSONP_Util.getInputStreamFromString("{}");
      JsonParser parser = Json.createParser(is);
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> END_OBJECT }
      parser.next(); // Event -> NoSuchElementException should be thrown
      LOGGER.warning("Did not get expected NoSuchElementException");
      pass = false;
    } catch (NoSuchElementException e) {
      LOGGER.info("Caught expected NoSuchElementException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonParserExceptionTests Failed");
  }

  /*
   * @testName: invalidLiteralNamesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:133; JSONP:JAVADOC:390;
   * 
   * @test_Strategy: This test trips various JsonParsingException conditions
   * when parsing an uppercase literal name that must be lowercase per JSON RFC
   * for the literal values (true, false or null).
   *
   */
  @Test
  public void invalidLiteralNamesTest() {
    boolean pass = true;

    // Trip JsonParsingException for JsonParser.next() if invalid liternal TRUE
    // instead of true
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal TRUE instead of true.");
      LOGGER.info("Reading " + "[TRUE]");
      JsonParser parser = Json.createParser(new StringReader("[TRUE]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal TRUE)
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonParser.next() if invalid liternal FALSE
    // instead of false
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal FALSE instead of false.");
      LOGGER.info("Reading " + "[FALSE]");
      JsonParser parser = Json.createParser(new StringReader("[FALSE]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal FALSE)
      LOGGER.warning("Did not get expected JsonParsingException");
      pass = false;
    } catch (JsonParsingException e) {
      LOGGER.info("Caught expected JsonParsingException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Trip JsonParsingException for JsonParser.next() if invalid liternal NULL
    // instead of null
    try {
      LOGGER.info(
          "Trip JsonParsingException for JsonParser.next() if invalid liternal NULL instead of null.");
      LOGGER.info("Reading " + "[NULL]");
      JsonParser parser = Json.createParser(new StringReader("[NULL]"));
      parser.next(); // Event -> START_OBJECT {
      parser.next(); // Event -> JsonParsingException (invalid literal NULL)
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
   * @testName: jsonParser11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:673; JSONP:JAVADOC:674; JSONP:JAVADOC:675;
   * JSONP:JAVADOC:676; JSONP:JAVADOC:677; JSONP:JAVADOC:678; JSONP:JAVADOC:679;
   * JSONP:JAVADOC:680; JSONP:JAVADOC:583; JSONP:JAVADOC:584; JSONP:JAVADOC:585;
   * JSONP:JAVADOC:586; JSONP:JAVADOC:587; JSONP:JAVADOC:588; JSONP:JAVADOC:662;
   * JSONP:JAVADOC:663; JSONP:JAVADOC:664; JSONP:JAVADOC:665; JSONP:JAVADOC:666;
   * JSONP:JAVADOC:667;
   * 
   * @test_Strategy: Tests JsonParser API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonParser11Test() {
    Parser parserTest = new Parser();
    final TestResult result = parserTest.test();
    result.eval();
  }

  /*
   * @testName: jsonParserCurrentEvent
   *
   * @test_Strategy: Tests JsonParser API methods added in JSON-P 2.1.
   */
  @Test
  public void jsonParserCurrentEvent() {
      try (JsonParser parser = Json.createParser(new StringReader("{\"a\":\"v\",\"b\":\"w\"}"))) {
          assertNull(parser.currentEvent());
          int events = 0;
          while (parser.hasNext()) {
              Event next = parser.next();
              assertNotNull(next);
              assertEquals(next, parser.currentEvent());
              assertEquals(parser.currentEvent(), parser.currentEvent());
              events++;
          }
          assertEquals(6, events);
      }
  }
}

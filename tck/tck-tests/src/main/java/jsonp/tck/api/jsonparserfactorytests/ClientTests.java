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
 * $Id: Client.java 66863 2012-07-23 11:26:40Z adf $
 */
package jsonp.tck.api.jsonparserfactorytests;

import jakarta.json.*;
import jakarta.json.stream.*;
import java.io.*;

import java.util.Map;
import java.util.logging.Logger;

import jsonp.tck.common.*;
import jsonp.tck.common.JSONP_Util;
import jsonp.tck.common.MyBufferedInputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  /* Tests */

  /*
   * @testName: jsonParserFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:164;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(Reader) JsonParser parser2
   * = parserFactory.createParser(Reader)
   */
  @Test
  public void jsonParserFactoryTest1() {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("--------------------------------------------------");
      LOGGER.info("TEST CASE [JsonParserFactory.createParser(Reader)]");
      LOGGER.info("--------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      LOGGER.info("Create 1st JsonParser from the Reader using JsonParserFactory");
      parser1 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser1 == null) {
        LOGGER.warning("ParserFactory failed to create parser1 from Reader");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      LOGGER.info("Create 2nd JsonParser from the Reader using JsonParserFactory");
      parser2 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser2 == null) {
        LOGGER.warning("ParserFactory failed to create parser2 from Reader");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      fail("jsonParserFactoryTest1 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "jsonParserFactoryTest1 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:166;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(JsonObject) JsonParser
   * parser2 = parserFactory.createParser(JsonObject)
   */
  @Test
  public void jsonParserFactoryTest2() {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("------------------------------------------------------");
      LOGGER.info("TEST CASE [JsonParserFactory.createParser(JsonObject)]");
      LOGGER.info("------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      JsonObject jsonObj = JSONP_Util
          .createJsonObjectFromString(jsonObjectString);
      LOGGER.info(
          "Create 1st JsonParser from the JsonObject using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonObj);
      if (parser1 == null) {
        LOGGER.warning("ParserFactory failed to create parser1 from JsonObject");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      LOGGER.info(
          "Create 2nd JsonParser from the JsonObject using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonObj);
      if (parser2 == null) {
        LOGGER.warning("ParserFactory failed to create parser2 from JsonObject");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      fail("jsonParserFactoryTest2 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "jsonParserFactoryTest2 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:167;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(JsonArray) JsonParser
   * parser2 = parserFactory.createParser(JsonArray)
   */
  @Test
  public void jsonParserFactoryTest3() {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-----------------------------------------------------");
      LOGGER.info("TEST CASE [JsonParserFactory.createParser(JsonArray)]");
      LOGGER.info("-----------------------------------------------------");
      String jsonArrayString = "[\"foo\",\"bar\"]";
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(jsonArrayString);
      LOGGER.info(
          "Create 1st JsonParser from the JsonArray using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonArr);
      if (parser1 == null) {
        LOGGER.warning("ParserFactory failed to create parser1 from JsonArray");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonArrayString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser1, "foo");
        JSONP_Util.testStringValue(parser1, "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      LOGGER.info(
          "Create 2nd JsonParser from the JsonArray using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonArr);
      if (parser2 == null) {
        LOGGER.warning("ParserFactory failed to create parser2 from JsonArray");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonArrayString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser2, "foo");
        JSONP_Util.testStringValue(parser2, "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }
    } catch (Exception e) {
      fail("jsonParserFactoryTest3 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "jsonParserFactoryTest3 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:165;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(InputStream) JsonParser
   * parser2 = parserFactory.createParser(InputStream)
   */
  @Test
  public void jsonParserFactoryTest4() {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-------------------------------------------------------");
      LOGGER.info("TEST CASE [JsonParserFactory.createParser(InputStream)]");
      LOGGER.info("-------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      LOGGER.info(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser1 == null) {
        LOGGER.warning("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      LOGGER.info(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser2 == null) {
        LOGGER.warning("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      fail("jsonParserFactoryTest4 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "jsonParserFactoryTest4 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:201;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * JsonParser parser1 = parserFactory.createParser(InputStream, Charset)
   * JsonParser parser2 = parserFactory.createParser(InputStream, Charset)
   */
  @Test
  public void jsonParserFactoryTest5() {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      LOGGER.info("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info(
          "----------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [JsonParserFactory.createParser(InputStream, Charset)]");
      LOGGER.info(
          "----------------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      LOGGER.info(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser1 == null) {
        LOGGER.warning("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      LOGGER.info(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser2 == null) {
        LOGGER.warning("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        LOGGER.info("Parsing " + jsonObjectString);
        LOGGER.info("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          LOGGER.warning("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      fail("jsonParserFactoryTest5 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    assertTrue(pass, "jsonParserFactoryTest5 Failed");
  }

  /*
   * @testName: jsonParserFactoryTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:417; JSONP:JAVADOC:164; JSONP:JAVADOC:428;
   * 
   * @test_Strategy: Tests the JsonParserFactory API.
   *
   * JsonParserFactory parserFactory = Json.createParserFactory(Map<String, ?>);
   * Map<String, ?> config = JsonParserFactory.getConfigInUse();
   *
   * Test for the following 2 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  @Test
  public void jsonParserFactoryTest6() {
    boolean pass = true;
    JsonParserFactory parserFactory;
    Map<String, ?> config;
    try {
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Test scenario1: no supported provider property");
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Create JsonParserFactory with Map<String, ?> with EMPTY config");
      parserFactory = Json.createParserFactory(JSONP_Util.getEmptyConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Test scenario2: non supported provider property");
      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Create JsonParserFactory with Map<String, ?> with FOO config");
      parserFactory = Json.createParserFactory(JSONP_Util.getFooConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      fail("jsonParserFactoryTest6 Failed: ", e);
    }
    assertTrue(pass, "jsonParserFactoryTest6 Failed");
  }

  /*
   * @testName: jsonParserFactoryExceptionTest
   * 
   * @assertion_ids: JSONP:JAVADOC:225;
   * 
   * @test_Strategy: Test JsonParserFactory exception conditions. Trip the
   * following exception due to unknown encoding or i/o error:
   *
   * jakarta.json.JsonException
   */
  @Test
  public void jsonParserFactoryExceptionTest() {
    boolean pass = true;

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // i/o error
    try {
      LOGGER.info(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if i/o error.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      MyBufferedInputStream mbi = new MyBufferedInputStream(
          JSONP_Util.getInputStreamFromString("{}"), true);
      JsonParser parser = parserFactory.createParser(mbi);
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // unknown encoding
    try {
      LOGGER.info(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if unknown encoding.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectUnknownEncoding.json");
      JsonParser parser = parserFactory.createParser(is);
      LOGGER.info("parser=" + parser);
      LOGGER.warning("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      LOGGER.warning("Caught unexpected exception: " + e);
    }

    assertTrue(pass, "jsonParserFactoryExceptionTest Failed");
  }
}

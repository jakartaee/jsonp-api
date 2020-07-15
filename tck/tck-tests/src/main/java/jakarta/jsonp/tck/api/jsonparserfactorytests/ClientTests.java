/*
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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
package jakarta.jsonp.tck.api.jsonparserfactorytests;

import jakarta.json.*;
import jakarta.json.stream.*;
import java.io.*;
import java.nio.charset.Charset;

import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import jakarta.json.stream.JsonParser.Event.*;
import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
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
  public void jsonParserFactoryTest1() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      System.out.println("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("--------------------------------------------------");
      System.out.println("TEST CASE [JsonParserFactory.createParser(Reader)]");
      System.out.println("--------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      System.out.println("Create 1st JsonParser from the Reader using JsonParserFactory");
      parser1 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser1 == null) {
        System.err.println("ParserFactory failed to create parser1 from Reader");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      System.out.println("Create 2nd JsonParser from the Reader using JsonParserFactory");
      parser2 = parserFactory.createParser(new StringReader(jsonObjectString));
      if (parser2 == null) {
        System.err.println("ParserFactory failed to create parser2 from Reader");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest1 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest1 Failed");
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
  public void jsonParserFactoryTest2() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      System.out.println("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("------------------------------------------------------");
      System.out.println("TEST CASE [JsonParserFactory.createParser(JsonObject)]");
      System.out.println("------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      JsonObject jsonObj = JSONP_Util
          .createJsonObjectFromString(jsonObjectString);
      System.out.println(
          "Create 1st JsonParser from the JsonObject using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonObj);
      if (parser1 == null) {
        System.err.println("ParserFactory failed to create parser1 from JsonObject");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      System.out.println(
          "Create 2nd JsonParser from the JsonObject using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonObj);
      if (parser2 == null) {
        System.err.println("ParserFactory failed to create parser2 from JsonObject");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest2 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest2 Failed");
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
  public void jsonParserFactoryTest3() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      System.out.println("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-----------------------------------------------------");
      System.out.println("TEST CASE [JsonParserFactory.createParser(JsonArray)]");
      System.out.println("-----------------------------------------------------");
      String jsonArrayString = "[\"foo\",\"bar\"]";
      JsonArray jsonArr = JSONP_Util.createJsonArrayFromString(jsonArrayString);
      System.out.println(
          "Create 1st JsonParser from the JsonArray using JsonParserFactory");
      parser1 = parserFactory.createParser(jsonArr);
      if (parser1 == null) {
        System.err.println("ParserFactory failed to create parser1 from JsonArray");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonArrayString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser1, "foo");
        JSONP_Util.testStringValue(parser1, "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      System.out.println(
          "Create 2nd JsonParser from the JsonArray using JsonParserFactory");
      parser2 = parserFactory.createParser(jsonArr);
      if (parser2 == null) {
        System.err.println("ParserFactory failed to create parser2 from JsonArray");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonArrayString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_ARRAY);
        JSONP_Util.testStringValue(parser2, "foo");
        JSONP_Util.testStringValue(parser2, "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_ARRAY);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest3 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest3 Failed");
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
  public void jsonParserFactoryTest4() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      System.out.println("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-------------------------------------------------------");
      System.out.println("TEST CASE [JsonParserFactory.createParser(InputStream)]");
      System.out.println("-------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      System.out.println(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser1 == null) {
        System.err.println("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      System.out.println(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(new ByteArrayInputStream(
          jsonObjectString.getBytes(JSONP_Util.UTF_8)));
      if (parser2 == null) {
        System.err.println("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest4 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest4 Failed");
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
  public void jsonParserFactoryTest5() throws Fault {
    boolean pass = true;
    JsonParser parser1 = null;
    JsonParser parser2 = null;
    JsonParser.Event event = null;
    try {
      System.out.println("Create JsonParserFactory with a configuration");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println(
          "----------------------------------------------------------------");
      System.out.println(
          "TEST CASE [JsonParserFactory.createParser(InputStream, Charset)]");
      System.out.println(
          "----------------------------------------------------------------");
      String jsonObjectString = "{\"foo\":\"bar\"}";
      System.out.println(
          "Create 1st JsonParser from the InputStream using JsonParserFactory");
      parser1 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser1 == null) {
        System.err.println("ParserFactory failed to create parser1 from InputStream");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser1, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser1, "foo", "bar");
        JSONP_Util.testEventType(parser1, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

      System.out.println(
          "Create 2nd JsonParser from the InputStream using JsonParserFactory");
      parser2 = parserFactory.createParser(
          new ByteArrayInputStream(jsonObjectString.getBytes(JSONP_Util.UTF_8)),
          JSONP_Util.UTF_8);
      if (parser2 == null) {
        System.err.println("ParserFactory failed to create parser2 from InputStream");
        pass = false;
      } else {
        System.out.println("Parsing " + jsonObjectString);
        System.out.println("Verify that JSON Parser Events/Data matches");
        JSONP_Util.resetParseErrs();
        JSONP_Util.testEventType(parser2, JsonParser.Event.START_OBJECT);
        JSONP_Util.testKeyStringValue(parser2, "foo", "bar");
        JSONP_Util.testEventType(parser2, JsonParser.Event.END_OBJECT);
        int parseErrs = JSONP_Util.getParseErrs();
        if (parseErrs != 0) {
          System.err.println("There were " + parseErrs + " parser errors that occurred.");
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest5 Failed: ", e);
    } finally {
      try {
        parser1.close();
        parser2.close();
      } catch (Exception e) {
      }
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest5 Failed");
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
  public void jsonParserFactoryTest6() throws Fault {
    boolean pass = true;
    JsonParserFactory parserFactory;
    Map<String, ?> config;
    try {
      System.out.println("----------------------------------------------");
      System.out.println("Test scenario1: no supported provider property");
      System.out.println("----------------------------------------------");
      System.out.println("Create JsonParserFactory with Map<String, ?> with EMPTY config");
      parserFactory = Json.createParserFactory(JSONP_Util.getEmptyConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-----------------------------------------------");
      System.out.println("Test scenario2: non supported provider property");
      System.out.println("-----------------------------------------------");
      System.out.println("Create JsonParserFactory with Map<String, ?> with FOO config");
      parserFactory = Json.createParserFactory(JSONP_Util.getFooConfig());
      config = parserFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParserFactoryTest6 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonParserFactoryTest6 Failed");
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
  public void jsonParserFactoryExceptionTest() throws Fault {
    boolean pass = true;

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // i/o error
    try {
      System.out.println(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if i/o error.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      MyBufferedInputStream mbi = new MyBufferedInputStream(
          JSONP_Util.getInputStreamFromString("{}"), true);
      JsonParser parser = parserFactory.createParser(mbi);
      System.err.println("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      System.out.println("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Tests JsonParserFactory.createParser(InputStream) for JsonException if
    // unknown encoding
    try {
      System.out.println(
          "Tests JsonParserFactory.createParser(InputStream) for JsonException if unknown encoding.");
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      InputStream is = JSONP_Util
          .getInputStreamFromResource("jsonObjectUnknownEncoding.json");
      JsonParser parser = parserFactory.createParser(is);
      System.out.println("parser=" + parser);
      System.err.println("Did not get expected JsonException");
      pass = false;
    } catch (JsonException e) {
      System.out.println("Caught expected JsonException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonParserFactoryExceptionTest Failed");
  }
}

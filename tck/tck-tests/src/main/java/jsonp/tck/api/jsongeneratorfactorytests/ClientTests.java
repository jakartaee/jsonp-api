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
package jsonp.tck.api.jsongeneratorfactorytests;

import jakarta.json.*;
import jakarta.json.stream.*;

import java.io.*;

import java.util.Map;
import java.util.logging.Logger;

import jsonp.tck.common.*;
import jsonp.tck.common.JSONP_Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* Tests */

  /*
   * @testName: jsonGeneratorFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:162; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(Writer) JsonGenerator generator2 =
   * generatorFactory.createGenerator(Writer)
   */
  @Test
  public void jsonGeneratorFactoryTest1() {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString;
    String actString;
    try {
      LOGGER.info(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
      LOGGER.info("--------------------------------------------------------");
      LOGGER.info("TEST CASE [JsonGeneratorFactory.createGenerator(Writer)]");
      LOGGER.info("--------------------------------------------------------");
      LOGGER.info("Create 1st JsonGenerator using JsonGeneratorFactory");
      StringWriter sWriter1 = new StringWriter();
      generator1 = generatorFactory.createGenerator(sWriter1);
      if (generator1 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      LOGGER.info("sWriter1=" + sWriter1.toString());
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(sWriter1.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      LOGGER.info("Create 2nd JsonGenerator using JsonGeneratorFactory");
      StringWriter sWriter2 = new StringWriter();
      generator2 = generatorFactory.createGenerator(sWriter2);
      if (generator2 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      LOGGER.info("sWriter2=" + sWriter2.toString());
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(sWriter2.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      fail("jsonGeneratorFactoryTest1 Failed: ", e);
    }
    assertTrue(pass, "jsonGeneratorFactoryTest1 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:163; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String,?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(OutputStream, Charset) JsonGenerator
   * generator2 = generatorFactory.createGenerator(OutputStream, Charset)
   *
   * Create generator with both UTF-8 and UTF-16BE.
   */
  @Test
  public void jsonGeneratorFactoryTest2() {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString, actString;
    try {
      LOGGER.info(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;

      LOGGER.info(
          "-----------------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [JsonGeneratorFactory.createGenerator(OutputStream, Charset)]");
      LOGGER.info(
          "-----------------------------------------------------------------------");
      LOGGER.info(
          "Create 1st JsonGenerator using JsonGeneratorFactory with UTF-8 encoding");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      generator1 = generatorFactory.createGenerator(baos1, JSONP_Util.UTF_8);
      if (generator1 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      LOGGER.info("baos1=" + baos1.toString("UTF-8"));
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      LOGGER.info(
          "Create 2nd JsonGenerator using JsonGeneratorFactory with UTF-16BE encoding");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      generator2 = generatorFactory.createGenerator(baos2, JSONP_Util.UTF_16BE);
      if (generator2 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      LOGGER.info("baos2=" + baos2.toString("UTF-16BE"));
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-16BE"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      fail("jsonGeneratorFactoryTest2 Failed: ", e);
    }
    assertTrue(pass, "jsonGeneratorFactoryTest2 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:200; JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); JsonGenerator generator1 =
   * generatorFactory.createGenerator(OutputStream) JsonGenerator generator2 =
   * generatorFactory.createGenerator(OutputStream)
   */
  @Test
  public void jsonGeneratorFactoryTest3() {
    boolean pass = true;
    JsonGenerator generator1 = null;
    JsonGenerator generator2 = null;
    String expString;
    String actString;
    try {
      LOGGER.info(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
      LOGGER.info(
          "-----------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [JsonGeneratorFactory.createGenerator(OutputStream os)]");
      LOGGER.info(
          "-----------------------------------------------------------------");
      LOGGER.info("Create 1st JsonGenerator using JsonGeneratorFactory");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      generator1 = generatorFactory.createGenerator(baos1);
      if (generator1 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator1");
        pass = false;
      } else {
        generator1.writeStartObject().writeEnd();
        generator1.close();
      }
      LOGGER.info("baos1=" + baos1.toString("UTF-8"));
      expString = "{}";
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      LOGGER.info("Create 2nd JsonGenerator using JsonGeneratorFactory");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      generator2 = generatorFactory.createGenerator(baos2);
      if (generator2 == null) {
        LOGGER.warning("GeneratorFactory failed to create generator2");
        pass = false;
      } else {
        generator2.writeStartArray().writeEnd();
        generator2.close();
      }
      LOGGER.info("baos2=" + baos2.toString("UTF-8"));
      expString = "[]";
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      fail("jsonGeneratorFactoryTest3 Failed: ", e);
    }
    assertTrue(pass, "jsonGeneratorFactoryTest3 Failed");
  }

  /*
   * @testName: jsonGeneratorFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:416; JSONP:JAVADOC:427;
   * 
   * @test_Strategy: Tests the JsonGeneratorFactory API.
   *
   * JsonGeneratorFactory generatorFactory =
   * Json.createGeneratorFactory(Map<String, ?>); Map<String, ?> config =
   * JsonGeneratorFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) supported provider property 3) supported and non
   * supported provider property
   */
  @Test
  public void jsonGeneratorFactoryTest4() {
    boolean pass = true;
    JsonGeneratorFactory generatorFactory;
    Map<String, ?> config;
    try {
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Test scenario1: no supported provider property");
      LOGGER.info("----------------------------------------------");
      LOGGER.info(
          "Create JsonGeneratorFactory with Map<String, ?> with EMPTY config");
      generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getEmptyConfig());
      config = generatorFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-------------------------------------------");
      LOGGER.info("Test scenario2: supported provider property");
      LOGGER.info("-------------------------------------------");
      LOGGER.info(
          "Create JsonGeneratorFactory with Map<String, ?> with PRETTY_PRINTING config");
      generatorFactory = Json
          .createGeneratorFactory(JSONP_Util.getPrettyPrintingConfig());
      config = generatorFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;

      LOGGER.info("-------------------------------------------------------------");
      LOGGER.info("Test scenario3: supported and non supported provider property");
      LOGGER.info("-------------------------------------------------------------");
      LOGGER.info("Create JsonGeneratorFactory with Map<String, ?> with all config");
      generatorFactory = Json.createGeneratorFactory(JSONP_Util.getAllConfig());
      config = generatorFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
    } catch (Exception e) {
      fail("jsonGeneratorFactoryTest4 Failed: ", e);
    }
    assertTrue(pass, "jsonGeneratorFactoryTest4 Failed");
  }
}

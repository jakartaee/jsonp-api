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
package jsonp.tck.api.jsonbuilderfactorytests;

import jsonp.tck.api.common.TestResult;
import jsonp.tck.common.*;

import java.util.Map;
import java.util.logging.Logger;

import jakarta.json.*;
import jsonp.tck.common.JSONP_Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* Tests */

  /*
   * @testName: jsonBuilderFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:445; JSONP:JAVADOC:453; JSONP:JAVADOC:454;
   * JSONP:JAVADOC:455;
   * 
   * @test_Strategy: Tests the JsonBuilderFactory API.
   *
   * JsonBuilderFactory builderFactory = Json.createBuilderFactory(Map<String,
   * ?>); JsonArray array = builderFactory.createArrayBuilder() JsonObject
   * object = builderFactory.createObjectBuilder()
   */
  @Test
  public void jsonBuilderFactoryTest1() {
    boolean pass = true;
    try {
      LOGGER.info("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      JsonBuilderFactory builderFactory = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("---------------------------------------------------");
      LOGGER.info("TEST CASE [JsonBuilderFactory.createArrayBuilder()]");
      LOGGER.info("---------------------------------------------------");
      LOGGER.info("Create JsonArrayBuilder using JsonBuilderFactory");
      JsonArray expJsonArray = JSONP_Util.createJsonArrayFromString("[0,2]");
      JsonArray actJsonArray = builderFactory.createArrayBuilder().add(0).add(2)
          .build();
      if (!JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray))
        pass = false;

      LOGGER.info("----------------------------------------------------");
      LOGGER.info("TEST CASE [JsonBuilderFactory.createObjectBuilder()]");
      LOGGER.info("----------------------------------------------------");
      LOGGER.info("Create JsonObjectBuilder using JsonBuilderFactory");
      JsonObject expJsonObject = JSONP_Util
          .createJsonObjectFromString("{\"foo\":\"bar\"}");
      JsonObject actJsonObject = builderFactory.createObjectBuilder()
          .add("foo", "bar").build();
      if (!JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject))
        pass = false;

    } catch (Exception e) {
      fail("jsonBuilderFactoryTest1 Failed: ", e);
    }
    assertTrue(pass, "jsonBuilderFactoryTest1 Failed");
  }

  /*
   * @testName: jsonBuilderFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:445; JSONP:JAVADOC:455;
   * 
   * @test_Strategy: Tests the JsonBuilderFactory API.
   *
   * JsonBuilderFactory builderFactory = Json.createBuilderFactory(Map<String,
   * ?>); Map<String, ?> config = JsonBuilderFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  @Test
  public void jsonBuilderFactoryTest2() {
    boolean pass = true;
    JsonBuilderFactory builderFactory;
    Map<String, ?> config;
    try {
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Test scenario1: no supported provider property");
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getEmptyConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Test scenario2: non supported provider property");
      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Create JsonBuilderFactory with Map<String, ?> with FOO config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getFooConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      fail("jsonBuilderFactoryTest2 Failed: ", e);
    }
    assertTrue(pass, "jsonBuilderFactoryTest2 Failed");
  }

  /*
   * @testName: jsonBuilderFactory11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:614; JSONP:JAVADOC:615;
   * 
   * @test_Strategy: Tests JsonBuilderFactory API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonBuilderFactory11Test() {
    BuilderFactory factoryTest = new BuilderFactory();
    final TestResult result = factoryTest.test();
    result.eval();
  }

}

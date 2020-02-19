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
 * $Id$
 */
package jakarta.jsonp.tck.api.jsonbuilderfactorytests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

import java.util.Map;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.*;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
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
  public void jsonBuilderFactoryTest1() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      JsonBuilderFactory builderFactory = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("---------------------------------------------------");
      System.out.println("TEST CASE [JsonBuilderFactory.createArrayBuilder()]");
      System.out.println("---------------------------------------------------");
      System.out.println("Create JsonArrayBuilder using JsonBuilderFactory");
      JsonArray expJsonArray = JSONP_Util.createJsonArrayFromString("[0,2]");
      JsonArray actJsonArray = builderFactory.createArrayBuilder().add(0).add(2)
          .build();
      if (!JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray))
        pass = false;

      System.out.println("----------------------------------------------------");
      System.out.println("TEST CASE [JsonBuilderFactory.createObjectBuilder()]");
      System.out.println("----------------------------------------------------");
      System.out.println("Create JsonObjectBuilder using JsonBuilderFactory");
      JsonObject expJsonObject = JSONP_Util
          .createJsonObjectFromString("{\"foo\":\"bar\"}");
      JsonObject actJsonObject = builderFactory.createObjectBuilder()
          .add("foo", "bar").build();
      if (!JSONP_Util.assertEqualsJsonObjects(expJsonObject, actJsonObject))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonBuilderFactoryTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonBuilderFactoryTest1 Failed");
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
  public void jsonBuilderFactoryTest2() throws Fault {
    boolean pass = true;
    JsonBuilderFactory builderFactory;
    Map<String, ?> config;
    try {
      System.out.println("----------------------------------------------");
      System.out.println("Test scenario1: no supported provider property");
      System.out.println("----------------------------------------------");
      System.out.println("Create JsonBuilderFactory with Map<String, ?> with EMPTY config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getEmptyConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-----------------------------------------------");
      System.out.println("Test scenario2: non supported provider property");
      System.out.println("-----------------------------------------------");
      System.out.println("Create JsonBuilderFactory with Map<String, ?> with FOO config");
      builderFactory = Json.createBuilderFactory(JSONP_Util.getFooConfig());
      config = builderFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonBuilderFactoryTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonBuilderFactoryTest2 Failed");
  }

  /*
   * @testName: jsonBuilderFactory11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:614; JSONP:JAVADOC:615;
   * 
   * @test_Strategy: Tests JsonBuilderFactory API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonBuilderFactory11Test() throws Fault {
    BuilderFactory factoryTest = new BuilderFactory();
    final TestResult result = factoryTest.test();
    result.eval();
  }

}

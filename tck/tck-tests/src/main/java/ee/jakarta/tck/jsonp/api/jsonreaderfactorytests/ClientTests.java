/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates. All rights reserved.
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
package ee.jakarta.tck.jsonp.api.jsonreaderfactorytests;


import ee.jakarta.tck.jsonp.common.JSONP_Util;
import jakarta.json.*;

import java.io.*;

import java.util.Map;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  /* Tests */

  /*
   * @testName: jsonReaderFactoryTest1
   *
   * @assertion_ids: JSONP:JAVADOC:419; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   *
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * JsonReader reader1 = readerFactory.createReader(Reader) JsonReader reader2
   * = readerFactory.createReader(Reader)
   */
  @Test
  public void jsonReaderFactoryTest1() {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      LOGGER.info("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
      LOGGER.info("--------------------------------------------------");
      LOGGER.info("TEST CASE [JsonReaderFactory.createReader(Reader)]");
      LOGGER.info("--------------------------------------------------");
      LOGGER.info("Create 1st JsonReader using JsonReaderFactory");
      reader1 = readerFactory.createReader(new StringReader(jsonObjectText));
      if (reader1 == null) {
        LOGGER.warning("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      LOGGER.info("Create 2nd JsonReader using JsonReaderFactory");
      reader2 = readerFactory.createReader(new StringReader(jsonObjectText));
      if (reader2 == null) {
        LOGGER.warning("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      fail("jsonReaderFactoryTest1 Failed: ", e);
    }
    assertTrue(pass, "jsonReaderFactoryTest1 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest2
   *
   * @assertion_ids: JSONP:JAVADOC:420; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   *
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String,?>);
   * JsonReader reader1 = readerFactory.createReader(InputStream, Charset)
   * JsonReader reader2 = readerFactory.createReader(InputStream, Charset)
   *
   * Create reader with both UTF-8 and UTF-16BE.
   */
  @Test
  public void jsonReaderFactoryTest2() {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      LOGGER.info("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info(
          "----------------------------------------------------------------");
      LOGGER.info(
          "TEST CASE [JsonReaderFactory.createReader(InputStream, Charset)]");
      LOGGER.info(
          "----------------------------------------------------------------");
      LOGGER.info(
          "Create 1st JsonReader using JsonReaderFactory with UTF-8 encoding");
      InputStream is1 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader1 = readerFactory.createReader(is1, JSONP_Util.UTF_8);
      if (reader1 == null) {
        LOGGER.warning("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      LOGGER.info(
          "Create 2nd JsonReader using JsonReaderFactory with UTF-8 encoding");
      InputStream is2 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader2 = readerFactory.createReader(is2, JSONP_Util.UTF_8);
      if (reader2 == null) {
        LOGGER.warning("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      fail("jsonReaderFactoryTest2 Failed: ", e);
    }
    assertTrue(pass, "jsonReaderFactoryTest2 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest3
   *
   * @assertion_ids: JSONP:JAVADOC:429; JSONP:JAVADOC:449; JSONP:JAVADOC:185;
   * JSONP:JAVADOC:459;
   *
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * JsonReader reader1 = readerFactory.createReader(InputStream) JsonReader
   * reader2 = readerFactory.createReader(InputStream)
   */
  @Test
  public void jsonReaderFactoryTest3() {
    boolean pass = true;
    JsonReader reader1 = null;
    JsonReader reader2 = null;
    JsonObject jsonObject = null;
    String jsonObjectText = "{\"foo\":\"bar\"}";
    try {
      LOGGER.info("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      JsonReaderFactory readerFactory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      LOGGER.info("Checking factory configuration properties");
      Map<String, ?> config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-------------------------------------------------------");
      LOGGER.info("TEST CASE [JsonReaderFactory.createReader(InputStream)]");
      LOGGER.info("-------------------------------------------------------");
      LOGGER.info("Create 1st JsonReader using JsonReaderFactory");
      InputStream is1 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader1 = readerFactory.createReader(is1);
      if (reader1 == null) {
        LOGGER.warning("ReaderFactory failed to create reader1");
        pass = false;
      } else {
        jsonObject = reader1.readObject();
        reader1.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

      LOGGER.info("Create 2nd JsonReader using JsonReaderFactory");
      InputStream is2 = JSONP_Util.getInputStreamFromString(jsonObjectText);
      reader2 = readerFactory.createReader(is2);
      if (reader2 == null) {
        LOGGER.warning("ReaderFactory failed to create reader2");
        pass = false;
      } else {
        jsonObject = reader2.readObject();
        reader2.close();

        if (!JSONP_Util.assertEquals(jsonObject.size(), 1)
            || !JSONP_Util.assertEquals(jsonObject.getString("foo"), "bar"))
          pass = false;
      }

    } catch (Exception e) {
      fail("jsonReaderFactoryTest3 Failed: ", e);
    }
    assertTrue(pass, "jsonReaderFactoryTest3 Failed");
  }

  /*
   * @testName: jsonReaderFactoryTest4
   *
   * @assertion_ids: JSONP:JAVADOC:449; JSONP:JAVADOC:459;
   *
   * @test_Strategy: Tests the JsonReaderFactory API.
   *
   * JsonReaderFactory readerFactory = Json.createReaderFactory(Map<String, ?>);
   * Map<String, ?> config = JsonReaderFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) non supported provider property
   */
  @Test
  public void jsonReaderFactoryTest4() {
    boolean pass = true;
    JsonReaderFactory readerFactory;
    Map<String, ?> config;
    try {
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Test scenario1: no supported provider property");
      LOGGER.info("----------------------------------------------");
      LOGGER.info("Create JsonReaderFactory with Map<String, ?> with EMPTY config");
      readerFactory = Json.createReaderFactory(JSONP_Util.getEmptyConfig());
      config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Test scenario2: non supported provider property");
      LOGGER.info("-----------------------------------------------");
      LOGGER.info("Create JsonReaderFactory with Map<String, ?> with FOO config");
      readerFactory = Json.createReaderFactory(JSONP_Util.getFooConfig());
      config = readerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
    } catch (Exception e) {
      fail("jsonReaderFactoryTest4 Failed: ", e);
    }
    assertTrue(pass, "jsonReaderFactoryTest4 Failed");
  }
}

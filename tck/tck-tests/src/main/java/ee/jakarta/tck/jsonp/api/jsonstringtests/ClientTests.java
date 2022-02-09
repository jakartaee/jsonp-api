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
package ee.jakarta.tck.jsonp.api.jsonstringtests;

import ee.jakarta.tck.jsonp.common.JSONP_Util;
import jakarta.json.*;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  /* Tests */

  /*
   * @testName: jsonStringEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:254;
   * 
   * @test_Strategy: Tests JsonString equals method. Create 2 equal JsonStrings
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonStrings and compare them for equality and expect false.
   */
  @Test
  public void jsonStringEqualsTest() {
    boolean pass = true;
    try {
      LOGGER.info("Create sample JsonString 1 for testing");
      JsonString string1 = (JsonString) JSONP_Util
          .createJsonString("Hello World");
      LOGGER.info("string1=" + JSONP_Util.toStringJsonString(string1));

      LOGGER.info("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      LOGGER.info("string2=" + JSONP_Util.toStringJsonString(string2));

      LOGGER.info(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect true");
      if (string1.equals(string2)) {
        LOGGER.info("JsonStrings are equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonStrings are not equal - unexpected.");
      }

      LOGGER.info("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      LOGGER.info("string1=" + JSONP_Util.toStringJsonString(string1));

      LOGGER.info("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      LOGGER.info("string2=" + JSONP_Util.toStringJsonString(string2));

      LOGGER.info(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect false");
      if (!string1.equals(string2)) {
        LOGGER.info("JsonStrings are not equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonStrings are equal - unexpected.");
      }
    } catch (Exception e) {
      fail("jsonStringEqualsTest Failed: ", e);
    }
    assertTrue(pass, "jsonStringEqualsTest Failed");
  }

  /*
   * @testName: jsonStringHashCodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:255;
   * 
   * @test_Strategy: Tests JsonString equals method. Create 2 equal JsonStrings
   * and compare them for hashcode and expect true. Create 2 non-equal
   * JsonStrings and compare them for hashcode and expect false.
   */
  @Test
  public void jsonStringHashCodeTest() {
    boolean pass = true;
    try {
      LOGGER.info("Create sample JsonString 1 for testing");
      JsonString string1 = JSONP_Util.createJsonString("Hello World");
      LOGGER.info("string1=" + JSONP_Util.toStringJsonString(string1));
      LOGGER.info("string1.hashCode()=" + string1.hashCode());

      LOGGER.info("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      LOGGER.info("string2=" + JSONP_Util.toStringJsonString(string2));
      LOGGER.info("string2.hashCode()=" + string2.hashCode());

      LOGGER.info(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect true");
      if (string1.hashCode() == string2.hashCode()) {
        LOGGER.info("JsonStrings hashCode are equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonStrings hashCode are not equal - unexpected.");
      }

      LOGGER.info("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      LOGGER.info("string1=" + JSONP_Util.toStringJsonString(string1));
      LOGGER.info("string1.hashCode()=" + string1.hashCode());

      LOGGER.info("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      LOGGER.info("string2=" + JSONP_Util.toStringJsonString(string2));
      LOGGER.info("string2.hashCode()=" + string2.hashCode());

      LOGGER.info(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect false");
      if (string1.hashCode() != string2.hashCode()) {
        LOGGER.info("JsonStrings hashCode are not equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonStrings hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      fail("jsonStringHashCodeTest Failed: ", e);
    }
    assertTrue(pass, "jsonStringHashCodeTest Failed");
  }

  /*
   * @testName: jsonStringGetCharsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:383;
   * 
   * @test_Strategy: Tests JsonString getChars method.
   */
  @Test
  public void jsonStringGetCharsTest() {
    boolean pass = true;
    String helloWorld = "Hello World";

    try {
      LOGGER.info("Create sample JsonString for testing");
      JsonString string = JSONP_Util.createJsonString(helloWorld);
      LOGGER.info("string=" + JSONP_Util.toStringJsonString(string));

      LOGGER.info(
          "Call JsonString.getChars() to return the char sequence for the JSON string");
      CharSequence cs = string.getChars();
      LOGGER.info("charSequence=" + cs.toString());

      LOGGER.info("Checking char sequence for equality to expected string contents");
      if (!JSONP_Util.assertEquals(helloWorld, cs.toString()))
        pass = false;

      LOGGER.info("Checking char sequence for expected equality to string length");
      if (!JSONP_Util.assertEquals(helloWorld.length(), cs.length()))
        pass = false;
    } catch (Exception e) {
      fail("jsonStringGetCharsTest Failed: ", e);
    }
    assertTrue(pass, "jsonStringGetCharsTest Failed");
  }
}

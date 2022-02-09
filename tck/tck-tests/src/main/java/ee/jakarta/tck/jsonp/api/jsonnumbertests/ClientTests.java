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
package ee.jakarta.tck.jsonp.api.jsonnumbertests;

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
   * @testName: jsonNumberEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:250;
   * 
   * @test_Strategy: Tests JsonNumber equals method. Create 2 equal JsonNumbers
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonNumbers and compare them for equality and expect false.
   */
  @Test
  public void jsonNumberEqualsTest() {
    boolean pass = true;
    try {
      LOGGER.info("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number1=" + JSONP_Util.toStringJsonNumber(number1));

      LOGGER.info("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number2=" + JSONP_Util.toStringJsonNumber(number2));

      LOGGER.info(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect true");
      if (number1.equals(number2)) {
        LOGGER.info("JsonNumbers are equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonNumbers are not equal - unexpected.");
      }

      LOGGER.info("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number1=" + JSONP_Util.toStringJsonNumber(number1));

      LOGGER.info("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      LOGGER.info("number2=" + JSONP_Util.toStringJsonNumber(number2));

      LOGGER.info(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect false");
      if (!number1.equals(number2)) {
        LOGGER.info("JsonNumbers are not equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonNumbers are equal - unexpected.");
      }
    } catch (Exception e) {
      fail("jsonNumberEqualsTest Failed: ", e);
    }
    assertTrue(pass, "jsonNumberEqualsTest Failed");
  }

  /*
   * @testName: jsonNumberHashCodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:251;
   * 
   * @test_Strategy: Tests JsonNumber equals method. Create 2 equal JsonNumbers
   * and compare them for hashcode and expect true. Create 2 non-equal
   * JsonNumbers and compare them for hashcode and expect false.
   */
  @Test
  public void jsonNumberHashCodeTest() {
    boolean pass = true;
    try {
      LOGGER.info("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number1=" + JSONP_Util.toStringJsonNumber(number1));
      LOGGER.info("number1.hashCode()=" + number1.hashCode());

      LOGGER.info("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number2=" + JSONP_Util.toStringJsonNumber(number2));
      LOGGER.info("number2.hashCode()=" + number2.hashCode());

      LOGGER.info(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect true");
      if (number1.hashCode() == number2.hashCode()) {
        LOGGER.info("JsonNumbers hashCode are equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonNumbers hashCode are not equal - unexpected.");
      }

      LOGGER.info("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      LOGGER.info("number1=" + JSONP_Util.toStringJsonNumber(number1));
      LOGGER.info("number1.hashCode()=" + number1.hashCode());

      LOGGER.info("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      LOGGER.info("number2=" + JSONP_Util.toStringJsonNumber(number2));
      LOGGER.info("number2.hashCode()=" + number2.hashCode());

      LOGGER.info(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect false");
      if (number1.hashCode() != number2.hashCode()) {
        LOGGER.info("JsonNumbers hashCode are not equal - expected.");
      } else {
        pass = false;
        LOGGER.warning("JsonNumbers hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      fail("jsonNumberHashCodeTest Failed: ", e);
    }
    assertTrue(pass, "jsonNumberHashCodeTest Failed");
  }

  /*
   * @testName: jsonNumberIsIntegralTest
   * 
   * @assertion_ids: JSONP:JAVADOC:51;
   * 
   * @test_Strategy: Test JsonNumber.isIntegral() method.
   */
  @Test
  public void jsonNumberIsIntegralTest() {
    boolean pass = true;
    JsonNumber jsonNumber = null;
    try {
      // INTEGRAL NUMBER TEST
      JsonNumber number1 = JSONP_Util.createJsonNumber(123);
      if (!JSONP_Util.assertEqualsJsonNumberType(number1.isIntegral(),
          JSONP_Util.INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(123, number1.intValue()))
          pass = false;
      }
      // NON_INTEGRAL NUMBER TEST
      JsonNumber number2 = JSONP_Util.createJsonNumber(12345.45);
      if (!JSONP_Util.assertEqualsJsonNumberType(number2.isIntegral(),
          JSONP_Util.NON_INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(12345.45, number2.doubleValue()))
          pass = false;
      }

    } catch (Exception e) {
      fail("jsonNumberIsIntegralTest Failed: ", e);
    }

    assertTrue(pass, "jsonNumberIsIntegralTest Failed");
  }
}

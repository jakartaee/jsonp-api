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
package jakarta.jsonp.tck.api.jsonnumbertests;

import jakarta.json.*;
import jakarta.json.stream.*;

import java.io.*;

import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.BigInteger;

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
   * @testName: jsonNumberEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:250;
   * 
   * @test_Strategy: Tests JsonNumber equals method. Create 2 equal JsonNumbers
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonNumbers and compare them for equality and expect false.
   */
  @Test
  public void jsonNumberEqualsTest() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      System.out.println("number1=" + JSONP_Util.toStringJsonNumber(number1));

      System.out.println("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      System.out.println("number2=" + JSONP_Util.toStringJsonNumber(number2));

      System.out.println(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect true");
      if (number1.equals(number2)) {
        System.out.println("JsonNumbers are equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonNumbers are not equal - unexpected.");
      }

      System.out.println("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      System.out.println("number1=" + JSONP_Util.toStringJsonNumber(number1));

      System.out.println("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      System.out.println("number2=" + JSONP_Util.toStringJsonNumber(number2));

      System.out.println(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect false");
      if (!number1.equals(number2)) {
        System.out.println("JsonNumbers are not equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonNumbers are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonNumberEqualsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonNumberEqualsTest Failed");
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
  public void jsonNumberHashCodeTest() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      System.out.println("number1=" + JSONP_Util.toStringJsonNumber(number1));
      System.out.println("number1.hashCode()=" + number1.hashCode());

      System.out.println("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      System.out.println("number2=" + JSONP_Util.toStringJsonNumber(number2));
      System.out.println("number2.hashCode()=" + number2.hashCode());

      System.out.println(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect true");
      if (number1.hashCode() == number2.hashCode()) {
        System.out.println("JsonNumbers hashCode are equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonNumbers hashCode are not equal - unexpected.");
      }

      System.out.println("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      System.out.println("number1=" + JSONP_Util.toStringJsonNumber(number1));
      System.out.println("number1.hashCode()=" + number1.hashCode());

      System.out.println("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      System.out.println("number2=" + JSONP_Util.toStringJsonNumber(number2));
      System.out.println("number2.hashCode()=" + number2.hashCode());

      System.out.println(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect false");
      if (number1.hashCode() != number2.hashCode()) {
        System.out.println("JsonNumbers hashCode are not equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonNumbers hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonNumberHashCodeTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonNumberHashCodeTest Failed");
  }

  /*
   * @testName: jsonNumberIsIntegralTest
   * 
   * @assertion_ids: JSONP:JAVADOC:51;
   * 
   * @test_Strategy: Test JsonNumber.isIntegral() method.
   */
  @Test
  public void jsonNumberIsIntegralTest() throws Fault {
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
      throw new Fault("jsonNumberIsIntegralTest Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonNumberIsIntegralTest Failed");
  }
}

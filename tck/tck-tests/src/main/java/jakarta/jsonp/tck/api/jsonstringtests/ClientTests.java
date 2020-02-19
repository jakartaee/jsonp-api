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
package jakarta.jsonp.tck.api.jsonstringtests;

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
   * @testName: jsonStringEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:254;
   * 
   * @test_Strategy: Tests JsonString equals method. Create 2 equal JsonStrings
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonStrings and compare them for equality and expect false.
   */
  @Test
  public void jsonStringEqualsTest() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonString 1 for testing");
      JsonString string1 = (JsonString) JSONP_Util
          .createJsonString("Hello World");
      System.out.println("string1=" + JSONP_Util.toStringJsonString(string1));

      System.out.println("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      System.out.println("string2=" + JSONP_Util.toStringJsonString(string2));

      System.out.println(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect true");
      if (string1.equals(string2)) {
        System.out.println("JsonStrings are equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonStrings are not equal - unexpected.");
      }

      System.out.println("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      System.out.println("string1=" + JSONP_Util.toStringJsonString(string1));

      System.out.println("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      System.out.println("string2=" + JSONP_Util.toStringJsonString(string2));

      System.out.println(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect false");
      if (!string1.equals(string2)) {
        System.out.println("JsonStrings are not equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonStrings are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonStringEqualsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringEqualsTest Failed");
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
  public void jsonStringHashCodeTest() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonString 1 for testing");
      JsonString string1 = JSONP_Util.createJsonString("Hello World");
      System.out.println("string1=" + JSONP_Util.toStringJsonString(string1));
      System.out.println("string1.hashCode()=" + string1.hashCode());

      System.out.println("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      System.out.println("string2=" + JSONP_Util.toStringJsonString(string2));
      System.out.println("string2.hashCode()=" + string2.hashCode());

      System.out.println(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect true");
      if (string1.hashCode() == string2.hashCode()) {
        System.out.println("JsonStrings hashCode are equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonStrings hashCode are not equal - unexpected.");
      }

      System.out.println("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      System.out.println("string1=" + JSONP_Util.toStringJsonString(string1));
      System.out.println("string1.hashCode()=" + string1.hashCode());

      System.out.println("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      System.out.println("string2=" + JSONP_Util.toStringJsonString(string2));
      System.out.println("string2.hashCode()=" + string2.hashCode());

      System.out.println(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect false");
      if (string1.hashCode() != string2.hashCode()) {
        System.out.println("JsonStrings hashCode are not equal - expected.");
      } else {
        pass = false;
        System.err.println("JsonStrings hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonStringHashCodeTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringHashCodeTest Failed");
  }

  /*
   * @testName: jsonStringGetCharsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:383;
   * 
   * @test_Strategy: Tests JsonString getChars method.
   */
  @Test
  public void jsonStringGetCharsTest() throws Fault {
    boolean pass = true;
    String helloWorld = "Hello World";

    try {
      System.out.println("Create sample JsonString for testing");
      JsonString string = JSONP_Util.createJsonString(helloWorld);
      System.out.println("string=" + JSONP_Util.toStringJsonString(string));

      System.out.println(
          "Call JsonString.getChars() to return the char sequence for the JSON string");
      CharSequence cs = string.getChars();
      System.out.println("charSequence=" + cs.toString());

      System.out.println("Checking char sequence for equality to expected string contents");
      if (!JSONP_Util.assertEquals(helloWorld, cs.toString()))
        pass = false;

      System.out.println("Checking char sequence for expected equality to string length");
      if (!JSONP_Util.assertEquals(helloWorld.length(), cs.length()))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonStringGetCharsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringGetCharsTest Failed");
  }
}

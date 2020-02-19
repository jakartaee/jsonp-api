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
package jakarta.jsonp.tck.api.jsonvaluetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

import java.util.*;

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
   * @testName: jsonValueTypesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:102;
   * 
   * @test_Strategy: Test JsonValue.getValueType() API method call with all
   * JsonValue types.
   *
   */
  @Test
  public void jsonValueTypesTest() throws Fault {
    boolean pass = true;
    try {

      JsonValue.ValueType valueType;

      // Testing JsonValue.FALSE case
      System.out.println("Testing getValueType for JsonValue.FALSE value");
      valueType = JsonValue.FALSE.getValueType();
      if (valueType != JsonValue.ValueType.FALSE) {
        System.err.println("Expected JSON FALSE value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON FALSE value");

      // Testing JsonValue.TRUE case
      System.out.println("Testing getValueType for JsonValue.TRUE value");
      valueType = JsonValue.TRUE.getValueType();
      if (valueType != JsonValue.ValueType.TRUE) {
        System.err.println("Expected JSON TRUE value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON TRUE value");

      // Testing JsonValue.NULL case
      System.out.println("Testing getValueType for JsonValue.NULL value");
      valueType = JsonValue.NULL.getValueType();
      if (valueType != JsonValue.ValueType.NULL) {
        System.err.println("Expected JSON NULL value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON NULL value");

      // Testing JsonValue.String case
      System.out.println("Testing getValueType for JsonValue.String value");
      valueType = JSONP_Util.createJsonString("string").getValueType();
      if (valueType != JsonValue.ValueType.STRING) {
        System.err.println("Expected JSON STRING value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON STRING value");

      // Testing JsonValue.Number case
      System.out.println("Testing getValueType for JsonValue.Number value");
      valueType = JSONP_Util.createJsonNumber(Integer.MAX_VALUE).getValueType();
      if (valueType != JsonValue.ValueType.NUMBER) {
        System.err.println("Expected JSON NUMBER value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON NUMBER value");

      // Testing JsonValue.Array case
      System.out.println("Testing getValueType for JsonValue.Array value");
      valueType = JSONP_Util.createJsonArrayFromString("[]").getValueType();
      if (valueType != JsonValue.ValueType.ARRAY) {
        System.err.println("Expected JSON ARRAY value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON ARRAY value");

      // Testing JsonValue.Object case
      System.out.println("Testing getValueType for JsonValue.Object value");
      valueType = JSONP_Util.createJsonObjectFromString("{}").getValueType();
      if (valueType != JsonValue.ValueType.OBJECT) {
        System.err.println("Expected JSON OBJECT value type but got instead " + valueType);
        pass = false;
      } else
        System.out.println("Got expected value type for JSON OBJECT value");

    } catch (Exception e) {
      throw new Fault("jsonValueTypesTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonValueTypesTest Failed");
  }

  /*
   * @testName: jsonValueOfTest
   * 
   * @assertion_ids: JSONP:JAVADOC:103;
   * 
   * @test_Strategy: Test JsonValue.ValueType.valueOf() API method call with all
   * JsonValue types.
   *
   */
  @Test
  public void jsonValueOfTest() throws Fault {
    boolean pass = true;

    String valueTypeStrings[] = { "ARRAY", "FALSE", "NULL", "NUMBER", "OBJECT",
        "STRING", "TRUE" };

    for (String valueTypeString : valueTypeStrings) {
      JsonValue.ValueType valueType;
      try {
        System.out.println(
            "Testing enum value for string constant name " + valueTypeString);
        valueType = JsonValue.ValueType.valueOf(valueTypeString);
        System.out.println("Got enum type " + valueType + " for enum string constant named "
            + valueTypeString);
      } catch (Exception e) {
        System.err.println("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    System.out.println("Testing negative test case for NullPointerException");
    try {
      JsonValue.ValueType.valueOf(null);
      System.err.println("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      System.err.println("Got unexpected exception " + e);
      pass = false;
    }

    System.out.println("Testing negative test case for IllegalArgumentException");
    try {
      JsonValue.ValueType.valueOf("INVALID");
      System.err.println("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      System.out.println("Got expected IllegalArgumentException");
    } catch (Exception e) {
      System.err.println("Got unexpected exception " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("jsonValueOfTest Failed");
  }

  /*
   * @testName: jsonValuesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:104;
   * 
   * @test_Strategy: Test JsonValue.ValueType.values() API method call and
   * verify enums returned.
   *
   */
  @Test
  public void jsonValuesTest() throws Fault {
    boolean pass = true;

    System.out.println(
        "Testing API method JsonValue.ValueType.values() to return array of enums.");
    JsonValue.ValueType[] values = JsonValue.ValueType.values();

    for (JsonValue.ValueType valueType : values) {
      String valueString = JSONP_Util.getValueTypeString(valueType);
      if (valueString == null) {
        System.err.println("Got no value for enum " + valueType);
        pass = false;
      } else
        System.out.println("Got " + valueString + " for enum " + valueType);
    }

    if (!pass)
      throw new Fault("jsonValuesTest Failed");
  }

  /*
   * @testName: jsonValueToStringTest
   * 
   * @assertion_ids: JSONP:JAVADOC:288;
   * 
   * @test_Strategy: Test JsonValue.toString() API method call with various
   * JsonValue types.
   *
   */
  @Test
  public void jsonValueToStringTest() throws Fault {
    boolean pass = true;
    try {
      String stringValue;
      JsonValue jsonValue;

      // Testing JsonValue.FALSE case
      System.out.println("Testing JsonValue.toString() for JsonValue.FALSE value");
      stringValue = JsonValue.FALSE.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("false")) {
        System.err.println("Expected false");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonValue.TRUE case
      System.out.println("Testing JsonValue.toString() for JsonValue.TRUE value");
      stringValue = JsonValue.TRUE.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("true")) {
        System.err.println("Expected true");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonValue.NULL case
      System.out.println("Testing JsonValue.toString() for JsonValue.NULL value");
      stringValue = JsonValue.NULL.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("null")) {
        System.err.println("Expected null");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonString case
      System.out.println("Testing JsonValue.toString() for JsonString value");
      jsonValue = JSONP_Util.createJsonString("string");
      stringValue = jsonValue.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("\"string\"")) {
        System.err.println("Expected \"string\"");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonNumber case
      System.out.println("Testing JsonValue.toString() for JsonNumber value");
      jsonValue = JSONP_Util.createJsonNumber(10);
      stringValue = jsonValue.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("10")) {
        System.err.println("Expected 10");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonArray case
      System.out.println("Testing JsonValue.toString() for JsonArray value");
      jsonValue = JSONP_Util.createJsonArrayFromString("[]");
      stringValue = jsonValue.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("[]")) {
        System.err.println("Expected []");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

      // Testing JsonObject case
      System.out.println("Testing JsonValue.toString() for JsonObject value");
      jsonValue = JSONP_Util.createJsonObjectFromString("{}");
      stringValue = jsonValue.toString();
      System.out.println("stringValue=" + stringValue);
      if (!stringValue.equals("{}")) {
        System.err.println("Expected {}");
        pass = false;
      } else {
        System.out.println("Got " + stringValue);
      }

    } catch (Exception e) {
      throw new Fault("jsonValueToStringTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonValueToStringTest Failed");
  }

  /*
   * @testName: jsonValue11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:648; JSONP:JAVADOC:649;
   * 
   * @test_Strategy: Tests JsonValue API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonValue11Test() throws Fault {
    Value valueTest = new Value();
    final TestResult result = valueTest.test();
    result.eval();
  }

  /*
   * @testName: jsonStructure11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:647;
   * 
   * @test_Strategy: Tests JsonStructure API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonStructure11Test() throws Fault {
    Structure structTest = new Structure();
    final TestResult result = structTest.test();
    result.eval();
  }

}

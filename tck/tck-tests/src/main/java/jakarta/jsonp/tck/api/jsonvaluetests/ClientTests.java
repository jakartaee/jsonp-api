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
package jakarta.jsonp.tck.api.jsonvaluetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;

import jakarta.json.*;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

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
  public void jsonValueTypesTest() {
    boolean pass = true;
    try {

      JsonValue.ValueType valueType;

      // Testing JsonValue.FALSE case
      LOGGER.info("Testing getValueType for JsonValue.FALSE value");
      valueType = JsonValue.FALSE.getValueType();
      if (valueType != JsonValue.ValueType.FALSE) {
        LOGGER.warning("Expected JSON FALSE value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON FALSE value");

      // Testing JsonValue.TRUE case
      LOGGER.info("Testing getValueType for JsonValue.TRUE value");
      valueType = JsonValue.TRUE.getValueType();
      if (valueType != JsonValue.ValueType.TRUE) {
        LOGGER.warning("Expected JSON TRUE value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON TRUE value");

      // Testing JsonValue.NULL case
      LOGGER.info("Testing getValueType for JsonValue.NULL value");
      valueType = JsonValue.NULL.getValueType();
      if (valueType != JsonValue.ValueType.NULL) {
        LOGGER.warning("Expected JSON NULL value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON NULL value");

      // Testing JsonValue.String case
      LOGGER.info("Testing getValueType for JsonValue.String value");
      valueType = JSONP_Util.createJsonString("string").getValueType();
      if (valueType != JsonValue.ValueType.STRING) {
        LOGGER.warning("Expected JSON STRING value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON STRING value");

      // Testing JsonValue.Number case
      LOGGER.info("Testing getValueType for JsonValue.Number value");
      valueType = JSONP_Util.createJsonNumber(Integer.MAX_VALUE).getValueType();
      if (valueType != JsonValue.ValueType.NUMBER) {
        LOGGER.warning("Expected JSON NUMBER value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON NUMBER value");

      // Testing JsonValue.Array case
      LOGGER.info("Testing getValueType for JsonValue.Array value");
      valueType = JSONP_Util.createJsonArrayFromString("[]").getValueType();
      if (valueType != JsonValue.ValueType.ARRAY) {
        LOGGER.warning("Expected JSON ARRAY value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON ARRAY value");

      // Testing JsonValue.Object case
      LOGGER.info("Testing getValueType for JsonValue.Object value");
      valueType = JSONP_Util.createJsonObjectFromString("{}").getValueType();
      if (valueType != JsonValue.ValueType.OBJECT) {
        LOGGER.warning("Expected JSON OBJECT value type but got instead " + valueType);
        pass = false;
      } else
        LOGGER.info("Got expected value type for JSON OBJECT value");

    } catch (Exception e) {
      fail("jsonValueTypesTest Failed: ", e);
    }
    assertTrue(pass, "jsonValueTypesTest Failed");
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
  public void jsonValueOfTest() {
    boolean pass = true;

    String valueTypeStrings[] = { "ARRAY", "FALSE", "NULL", "NUMBER", "OBJECT",
        "STRING", "TRUE" };

    for (String valueTypeString : valueTypeStrings) {
      JsonValue.ValueType valueType;
      try {
        LOGGER.info(
            "Testing enum value for string constant name " + valueTypeString);
        valueType = JsonValue.ValueType.valueOf(valueTypeString);
        LOGGER.info("Got enum type " + valueType + " for enum string constant named "
            + valueTypeString);
      } catch (Exception e) {
        LOGGER.warning("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    LOGGER.info("Testing negative test case for NullPointerException");
    try {
      JsonValue.ValueType.valueOf(null);
      LOGGER.warning("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      LOGGER.info("Got expected NullPointerException");
    } catch (Exception e) {
      LOGGER.warning("Got unexpected exception " + e);
      pass = false;
    }

    LOGGER.info("Testing negative test case for IllegalArgumentException");
    try {
      JsonValue.ValueType.valueOf("INVALID");
      LOGGER.warning("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      LOGGER.info("Got expected IllegalArgumentException");
    } catch (Exception e) {
      LOGGER.warning("Got unexpected exception " + e);
      pass = false;
    }

    assertTrue(pass, "jsonValueOfTest Failed");
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
  public void jsonValuesTest() {
    LOGGER.info(
        "Testing API method JsonValue.ValueType.values() to return array of enums.");
    JsonValue.ValueType[] values = JsonValue.ValueType.values();

    for (JsonValue.ValueType valueType : values) {
      String valueString = JSONP_Util.getValueTypeString(valueType);
      if (valueString == null) {
        fail("jsonValuesTest Failed. Got no value for enum " + valueType);
      } else
        LOGGER.info("Got " + valueString + " for enum " + valueType);
    }
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
  public void jsonValueToStringTest() {
    boolean pass = true;
    try {
      String stringValue;
      JsonValue jsonValue;

      // Testing JsonValue.FALSE case
      LOGGER.info("Testing JsonValue.toString() for JsonValue.FALSE value");
      stringValue = JsonValue.FALSE.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("false")) {
        LOGGER.warning("Expected false");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonValue.TRUE case
      LOGGER.info("Testing JsonValue.toString() for JsonValue.TRUE value");
      stringValue = JsonValue.TRUE.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("true")) {
        LOGGER.warning("Expected true");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonValue.NULL case
      LOGGER.info("Testing JsonValue.toString() for JsonValue.NULL value");
      stringValue = JsonValue.NULL.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("null")) {
        LOGGER.warning("Expected null");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonString case
      LOGGER.info("Testing JsonValue.toString() for JsonString value");
      jsonValue = JSONP_Util.createJsonString("string");
      stringValue = jsonValue.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("\"string\"")) {
        LOGGER.warning("Expected \"string\"");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonNumber case
      LOGGER.info("Testing JsonValue.toString() for JsonNumber value");
      jsonValue = JSONP_Util.createJsonNumber(10);
      stringValue = jsonValue.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("10")) {
        LOGGER.warning("Expected 10");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonArray case
      LOGGER.info("Testing JsonValue.toString() for JsonArray value");
      jsonValue = JSONP_Util.createJsonArrayFromString("[]");
      stringValue = jsonValue.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("[]")) {
        LOGGER.warning("Expected []");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

      // Testing JsonObject case
      LOGGER.info("Testing JsonValue.toString() for JsonObject value");
      jsonValue = JSONP_Util.createJsonObjectFromString("{}");
      stringValue = jsonValue.toString();
      LOGGER.info("stringValue=" + stringValue);
      if (!stringValue.equals("{}")) {
        LOGGER.warning("Expected {}");
        pass = false;
      } else {
        LOGGER.info("Got " + stringValue);
      }

    } catch (Exception e) {
      fail("jsonValueToStringTest Failed: ", e);
    }
    assertTrue(pass, "jsonValueToStringTest Failed");
  }

  /*
   * @testName: jsonValue11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:648; JSONP:JAVADOC:649;
   * 
   * @test_Strategy: Tests JsonValue API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonValue11Test() {
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
  public void jsonStructure11Test() {
    Structure structTest = new Structure();
    final TestResult result = structTest.test();
    result.eval();
  }

  /*
   * @testName: jsonNumber21Test
   *
   * @assertion_ids: JSONP:JAVADOC:682;
   *
   * @test_Strategy: Tests Json.createValue(Number) API method added in JSON-P 2.1.
   */
  @Test
  public void jsonNumber21Test() {
      assertEquals(Json.createValue(1), Json.createValue(Byte.valueOf((byte) 1)));
      assertEquals(Json.createValue(1).toString(), Json.createValue(Byte.valueOf((byte) 1)).toString());
      assertEquals(Json.createValue(1), Json.createValue(Short.valueOf((short) 1)));
      assertEquals(Json.createValue(1).toString(), Json.createValue(Short.valueOf((short) 1)).toString());
      assertEquals(Json.createValue(1), Json.createValue(Integer.valueOf(1)));
      assertEquals(Json.createValue(1).toString(), Json.createValue(Integer.valueOf(1)).toString());
      assertEquals(Json.createValue(1L), Json.createValue(Long.valueOf(1)));
      assertEquals(Json.createValue(1L).toString(), Json.createValue(Long.valueOf(1)).toString());
      assertEquals(Json.createValue(1D), Json.createValue(Float.valueOf(1)));
      assertEquals(Json.createValue(1D).toString(), Json.createValue(Float.valueOf(1)).toString());
      assertEquals(Json.createValue(1D), Json.createValue(Double.valueOf(1)));
      assertEquals(Json.createValue(1D).toString(), Json.createValue(Double.valueOf(1)).toString());
      assertEquals(Json.createValue(1), Json.createValue(new CustomNumber(1)));
      assertEquals(Json.createValue(1).toString(), Json.createValue(new CustomNumber(1)).toString());
  }

  private static class CustomNumber extends Number {

      private static final long serialVersionUID = 1L;
      private final int num;

      private CustomNumber(int num) {
          this.num = num;
      }

      @Override
      public int intValue() {
          return num;
      }

      @Override
      public long longValue() {
          return num;
      }

      @Override
      public float floatValue() {
          return num;
      }

      @Override
      public double doubleValue() {
          return num;
      }

      @Override
      public String toString() {
          return Integer.toString(num);
      }

  }
}

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

package jsonp.tck.api.jsonvaluetests;

import jsonp.tck.api.common.TestResult;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonValue}.
 */
public class Value {

  private static final Logger LOGGER = Logger.getLogger(Value.class.getName());

  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonValue}.
   */
  Value() {
    super();
  }

  /**
   * {@link JsonValue} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonValue API methods added in JSON-P 1.1.");
    LOGGER.info("JsonValue API methods added in JSON-P 1.1.");
    testAsJsonObject(result);
    testAsJsonObjectOnNonObject(result);
    testAsJsonArray(result);
    testAsJsonArrayOnNonArray(result);
    return result;
  }

  /**
   * Test {@code JsonObject asJsonObject()} method on {@code JsonObject}
   * instances.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAsJsonObject(final TestResult result) {
    LOGGER.info(" - asJsonObject() on JsonObject instances");
    final JsonObject[] values = { SimpleValues.createEmptyObject(), SimpleValues.createSimpleObjectStr(),
        SimpleValues.createSimpleObjectInt(), SimpleValues.createSimpleObjectBool(),
        SimpleValues.createSimpleObjectObject(), SimpleValues.createCompoundObject() };
    for (final JsonObject objValue : values) {
      final JsonValue value = objValue;
      final JsonObject out = objValue.asJsonObject();
      if (operationFailed(objValue, out)) {
        result.fail("asJsonObject()", "Output " + JsonAssert.valueToString(out)
            + " value shall be " + JsonAssert.valueToString(objValue));
      }
    }
  }

  /**
   * Test {@code JsonObject asJsonObject()} method on non {@code JsonObject}
   * instances.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAsJsonObjectOnNonObject(final TestResult result) {
    LOGGER.info(" - asJsonObject() on non JsonObject instances");
    final JsonValue[] values = { SimpleValues.createEmptyArrayWithStr(),
        SimpleValues.createEmptyArrayWithInt(), SimpleValues.createEmptyArrayWithBool(),
        SimpleValues.createEmptyArrayWithObject(), SimpleValues.toJsonValue(SimpleValues.STR_VALUE),
        SimpleValues.toJsonValue(SimpleValues.INT_VALUE), SimpleValues.toJsonValue(SimpleValues.LNG_VALUE), SimpleValues.toJsonValue(SimpleValues.DBL_VALUE),
        SimpleValues.toJsonValue(SimpleValues.BIN_VALUE), SimpleValues.toJsonValue(SimpleValues.BDC_VALUE), SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE),
        SimpleValues.toJsonValue(null) };
    for (final JsonValue value : values) {
      try {
        value.asJsonObject();
        result.fail("asJsonObject()",
            "Call of asJsonObject() on non JsonObject instance shall throw ClassCastException");
      } catch (ClassCastException ex) {
        LOGGER.info("    - Expected exception: " + ex.getMessage());
      } catch (Throwable t) {
        result.fail("asJsonObject()",
            "Call of asJsonObject() on non JsonObject instance shall throw ClassCastException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonObject asJsonArray()} method on {@code JsonArray}
   * instances.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAsJsonArray(final TestResult result) {
    LOGGER.info(" - asJsonArray() on JsonArray instances");
    final JsonArray[] values = { SimpleValues.createEmptyArray(), SimpleValues.createEmptyArrayWithStr(),
        SimpleValues.createEmptyArrayWithInt(), SimpleValues.createEmptyArrayWithBool(),
        SimpleValues.createEmptyArrayWithObject(), SimpleValues.createSimpleStringArray5(),
        SimpleValues.createSimpleIntArray5(), SimpleValues.createSimpleBoolArray5(),
        SimpleValues.createSimpleObjectArray5() };
    for (final JsonArray objValue : values) {
      final JsonValue value = objValue;
      final JsonArray out = objValue.asJsonArray();
      if (operationFailed(objValue, out)) {
        result.fail("asJsonArray()", "Output " + JsonAssert.valueToString(out)
            + " value shall be " + JsonAssert.valueToString(objValue));
      }
    }
  }

  /**
   * Test {@code JsonObject asJsonArray()} method on non {@code JsonArray}
   * instances.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAsJsonArrayOnNonArray(final TestResult result) {
    LOGGER.info(" - asJsonArray() on non JsonArray instances");
    final JsonValue[] values = { SimpleValues.createSimpleObjectStr(),
        SimpleValues.createSimpleObjectInt(), SimpleValues.createSimpleObjectBool(),
        SimpleValues.createSimpleObjectObject(), SimpleValues.createCompoundObject(),
        SimpleValues.toJsonValue(SimpleValues.STR_VALUE), SimpleValues.toJsonValue(SimpleValues.INT_VALUE), SimpleValues.toJsonValue(SimpleValues.LNG_VALUE),
        SimpleValues.toJsonValue(SimpleValues.DBL_VALUE), SimpleValues.toJsonValue(SimpleValues.BIN_VALUE), SimpleValues.toJsonValue(SimpleValues.BDC_VALUE),
        SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE), SimpleValues.toJsonValue(null) };
    for (final JsonValue value : values) {
      try {
        value.asJsonArray();
        result.fail("asJsonArray()",
            "Call of asJsonArray() on non JsonArray instance shall throw ClassCastException");
      } catch (ClassCastException ex) {
        LOGGER.info("    - Expected exception: " + ex.getMessage());
      } catch (Throwable t) {
        result.fail("asJsonArray()",
            "Call of asJsonArray() on non JsonArray instance shall throw ClassCastException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null || !JsonAssert.assertEquals(check, out);
  }

}

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

package jakarta.jsonp.tck.api.jsonvaluetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonValue}.
 */
public class Value {

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
    System.out.println("JsonValue API methods added in JSON-P 1.1.");
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
    System.out.println(" - asJsonObject() on JsonObject instances");
    final JsonObject[] values = { createEmptyObject(), createSimpleObjectStr(),
        createSimpleObjectInt(), createSimpleObjectBool(),
        createSimpleObjectObject(), createCompoundObject() };
    for (final JsonObject objValue : values) {
      final JsonValue value = objValue;
      final JsonObject out = objValue.asJsonObject();
      if (operationFailed(objValue, out)) {
        result.fail("asJsonObject()", "Output " + valueToString(out)
            + " value shall be " + valueToString(objValue));
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
    System.out.println(" - asJsonObject() on non JsonObject instances");
    final JsonValue[] values = { createEmptyArrayWithStr(),
        createEmptyArrayWithInt(), createEmptyArrayWithBool(),
        createEmptyArrayWithObject(), toJsonValue(STR_VALUE),
        toJsonValue(INT_VALUE), toJsonValue(LNG_VALUE), toJsonValue(DBL_VALUE),
        toJsonValue(BIN_VALUE), toJsonValue(BDC_VALUE), toJsonValue(BOOL_VALUE),
        toJsonValue(null) };
    for (final JsonValue value : values) {
      try {
        value.asJsonObject();
        result.fail("asJsonObject()",
            "Call of asJsonObject() on non JsonObject instance shall throw ClassCastException");
      } catch (ClassCastException ex) {
        System.out.println("    - Expected exception: " + ex.getMessage());
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
    System.out.println(" - asJsonArray() on JsonArray instances");
    final JsonArray[] values = { createEmptyArray(), createEmptyArrayWithStr(),
        createEmptyArrayWithInt(), createEmptyArrayWithBool(),
        createEmptyArrayWithObject(), createSimpleStringArray5(),
        createSimpleIntArray5(), createSimpleBoolArray5(),
        createSimpleObjectArray5() };
    for (final JsonArray objValue : values) {
      final JsonValue value = objValue;
      final JsonArray out = objValue.asJsonArray();
      if (operationFailed(objValue, out)) {
        result.fail("asJsonArray()", "Output " + valueToString(out)
            + " value shall be " + valueToString(objValue));
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
    System.out.println(" - asJsonArray() on non JsonArray instances");
    final JsonValue[] values = { createSimpleObjectStr(),
        createSimpleObjectInt(), createSimpleObjectBool(),
        createSimpleObjectObject(), createCompoundObject(),
        toJsonValue(STR_VALUE), toJsonValue(INT_VALUE), toJsonValue(LNG_VALUE),
        toJsonValue(DBL_VALUE), toJsonValue(BIN_VALUE), toJsonValue(BDC_VALUE),
        toJsonValue(BOOL_VALUE), toJsonValue(null) };
    for (final JsonValue value : values) {
      try {
        value.asJsonArray();
        result.fail("asJsonArray()",
            "Call of asJsonArray() on non JsonArray instance shall throw ClassCastException");
      } catch (ClassCastException ex) {
        System.out.println("    - Expected exception: " + ex.getMessage());
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
    return out == null || !assertEquals(check, out);
  }

}

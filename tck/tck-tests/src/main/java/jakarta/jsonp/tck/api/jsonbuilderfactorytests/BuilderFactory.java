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

package jakarta.jsonp.tck.api.jsonbuilderfactorytests;

import jakarta.jsonp.tck.api.common.TestResult;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for
 * {@link JsonBuilderFactory}.
 */
public class BuilderFactory {

  private static final Logger LOGGER = Logger.getLogger(BuilderFactory.class.getName());

  /**
   * {@link JsonBuilderFactory} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonBuilderFactory API methods added in JSON-P 1.1.");
    LOGGER.info("JsonBuilderFactory API methods added in JSON-P 1.1.");
    testCreateArrayBuilderString(result);
    testCreateArrayBuilderInt(result);
    testCreateArrayBuilderBool(result);
    testCreateArrayBuilderObject(result);
    testCreateArrayBuilderNull(result);
    testCreateObjectBuilderString(result);
    testCreateObjectBuilderInt(result);
    testCreateObjectBuilderBool(result);
    testCreateObjectBuilderObject(result);
    testCreateObjectBuilderNull(result);
    return result;
  }

  /**
   * Test {@code JsonArrayBuilder createArrayBuilder(JsonArray)} method on
   * {@code String} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateArrayBuilderString(final TestResult result) {
    LOGGER.info(" - createArrayBuilder(JsonArray) for String");
    final JsonArray in = createStringArray2();
    final JsonArray check = createStringArray2();
    verifyCreateArrayBuilder(result, check, in);
  }

  /**
   * Test {@code JsonArrayBuilder createArrayBuilder(JsonArray)} method on
   * {@code int} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateArrayBuilderInt(final TestResult result) {
    LOGGER.info(" - createArrayBuilder(JsonArray) for int");
    final JsonArray in = createIntArray2();
    final JsonArray check = createIntArray2();
    verifyCreateArrayBuilder(result, check, in);
  }

  /**
   * Test {@code JsonArrayBuilder createArrayBuilder(JsonArray)} method on
   * {@code boolean} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateArrayBuilderBool(final TestResult result) {
    LOGGER.info(" - createArrayBuilder(JsonArray) for boolean");
    final JsonArray in = createBoolArray2();
    final JsonArray check = createBoolArray2();
    verifyCreateArrayBuilder(result, check, in);
  }

  /**
   * Test {@code JsonArrayBuilder createArrayBuilder(JsonArray)} method on
   * {@code JsonObject} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateArrayBuilderObject(final TestResult result) {
    LOGGER.info(" - createArrayBuilder(JsonArray) for JsonObject");
    final JsonArray in = createObjectArray2();
    final JsonArray check = createObjectArray2();
    verifyCreateArrayBuilder(result, check, in);
  }

  /**
   * Test {@code JsonArrayBuilder createArrayBuilder(JsonArray)} method on
   * {@code null} value. Method shall throw NullPointerException.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateArrayBuilderNull(final TestResult result) {
    LOGGER.info(" - createArrayBuilder(JsonArray) for null");
    final JsonArray in = null;
    final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    try {
      factory.createArrayBuilder(in);
      result.fail("createArrayBuilder(JsonArray)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info(
          "    - Expected exception for null argument: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("createObjectBuilder(JsonObject)",
          "Calling method with with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonObjectBuilder createObjectBuilder(JsonObject)} method on
   * {@code String} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateObjectBuilderString(final TestResult result) {
    LOGGER.info(" - createObjectBuilder(JsonObject) for String");
    final JsonObject in = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectStr();
    verifyCreateObjectBuilder(result, check, in);
  }

  /**
   * Test {@code JsonObjectBuilder createObjectBuilder(JsonObject)} method on
   * {@code int} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateObjectBuilderInt(final TestResult result) {
    LOGGER.info(" - createObjectBuilder(JsonObject) for int");
    final JsonObject in = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectInt();
    verifyCreateObjectBuilder(result, check, in);
  }

  /**
   * Test {@code JsonObjectBuilder createObjectBuilder(JsonObject)} method on
   * {@code boolean} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateObjectBuilderBool(final TestResult result) {
    LOGGER.info(" - createObjectBuilder(JsonObject) for boolean");
    final JsonObject in = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectBool();
    verifyCreateObjectBuilder(result, check, in);
  }

  /**
   * Test {@code JsonObjectBuilder createObjectBuilder(JsonObject)} method on
   * {@code JsonObject} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateObjectBuilderObject(final TestResult result) {
    LOGGER.info(" - createObjectBuilder(JsonObject) for JsonObject");
    final JsonObject in = createSimpleObjectObject();
    final JsonObject check = createSimpleObjectObject();
    verifyCreateObjectBuilder(result, check, in);
  }

  /**
   * Test helper: Verify {@code JsonArrayBuilder createArrayBuilder(JsonArray)}
   * method on provided array.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param in
   *          JSON array to pass to the method.
   */
  private void verifyCreateArrayBuilder(final TestResult result,
      final JsonArray check, final JsonArray in) {
    final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    final JsonArrayBuilder builder = factory.createArrayBuilder(in);
    final JsonArray out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createArrayBuilder(JsonArray)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test helper: Verify
   * {@code JsonObjectBuilder createObjectBuilder(JsonObjecty)} method on
   * provided object.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param in
   *          JSON object to pass to the method.
   */
  private void verifyCreateObjectBuilder(final TestResult result,
      final JsonObject check, final JsonObject in) {
    LOGGER.info("    - IN: " + valueToString(in));
    final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    final JsonObjectBuilder builder = factory.createObjectBuilder(in);
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createObjectBuilder(JsonObject)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test {@code JsonObjectBuilder createObjectBuilder(JsonObject)} method on
   * {@code null} value. Method shall throw NullPointerException.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateObjectBuilderNull(final TestResult result) {
    LOGGER.info(" - createObjectBuilder(JsonObject) for null");
    final JsonObject in = null;
    final JsonBuilderFactory factory = Json.createBuilderFactory(null);
    try {
      factory.createObjectBuilder(in);
      result.fail("createObjectBuilder(JsonObject)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info(
          "    - Expected exception for null argument: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("createObjectBuilder(JsonObject)",
          "Calling method with with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
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

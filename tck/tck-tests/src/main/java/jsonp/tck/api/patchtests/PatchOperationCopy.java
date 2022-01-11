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

package jsonp.tck.api.patchtests;

import jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatchBuilder;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Implements
 * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.5">RFC 6902:
 * 4.5. copy</a>} tests.
 */
public class PatchOperationCopy extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationCopy.class.getName());

  /** Tested operation name. */
  private final String OPERATION = "COPY";

  /**
   * Creates an instance of RFC 6902 replace operation test.
   */
  PatchOperationCopy() {
    super();
  }

  /**
   * Test RFC 6902 COPY operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 copy operation");
    LOGGER.info("Testing RFC 6902 copy operation:");
    testCopyStringOnSimpleObject(result);
    testCopyStringOnSimpleArray(result);
    testCopyIntOnSimpleObject(result);
    testCopyIntOnSimpleArray(result);
    testCopyBoolOnSimpleObject(result);
    testCopyBoolOnSimpleArray(result);
    testCopyObjectOnSimpleObject(result);
    testCopyObjectOnSimpleArray(result);
    testCopyStringOnCompoundObject(result);
    testCopyOfNonExistingLocationInObject(result);
    testCopyOfNonExistingLocationInArray(result);

    return result;
  }

  /**
   * Test RFC 6902 COPY operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createSimpleObjectCopyStr();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, SimpleValues.DEF_PATH);
  }

  /**
   * Test RFC 6902 COPY operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createStringArray2();
    simpleOperation(result, in, SimpleValues.createStringArray2Copy1to0(), "/1", "/0");
    simpleOperation(result, in, SimpleValues.createStringArray2Copy0to2(), "/0", "/2");
    simpleOperation(result, in, SimpleValues.createStringArray2Copy0to1(), "/0", "/1");
  }

  /**
   * Test RFC 6902 COPY operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createSimpleObjectCopyInt();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, SimpleValues.DEF_PATH);
  }

  /**
   * Test RFC 6902 COPY operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createIntArray2();
    simpleOperation(result, in, SimpleValues.createIntArray2Copy1to0(), "/1", "/0");
    simpleOperation(result, in, SimpleValues.createIntArray2Copy0to2(), "/0", "/2");
    simpleOperation(result, in, SimpleValues.createIntArray2Copy0to1(), "/0", "/1");
  }

  /**
   * Test RFC 6902 COPY operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createSimpleObjectCopyBool();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, SimpleValues.DEF_PATH);
  }

  /**
   * Test RFC 6902 COPY operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createBoolArray2();
    simpleOperation(result, in, SimpleValues.createBoolArray2Copy1to0(), "/1", "/0");
    simpleOperation(result, in, SimpleValues.createBoolArray2Copy0to2(), "/0", "/2");
    simpleOperation(result, in, SimpleValues.createBoolArray2Copy0to1(), "/0", "/1");
  }

  /**
   * Test RFC 6902 COPY operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyObjectOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectObject();
    final JsonObject check = SimpleValues.createSimpleObjectCopyObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, SimpleValues.DEF_PATH);
  }

  /**
   * Test RFC 6902 COPY operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createObjectArray2();
    simpleOperation(result, in, SimpleValues.createObjectArray2Copy1to0(), "/1", "/0");
    simpleOperation(result, in, SimpleValues.createObjectArray2Copy0to2(), "/0", "/2");
    simpleOperation(result, in, SimpleValues.createObjectArray2Copy0to1(), "/0", "/1");
  }

  /**
   * Test RFC 6902 COPY operation for {@code String} on compound JSON object.
   * Copied value overwrites an existing value.
   * 
   * @param result
   *          Tests result record.
   */
  private void testCopyStringOnCompoundObject(final TestResult result) {
    LOGGER.info(" - for String on compound JSON object");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonObject check = SimpleValues.createCompoundObjectCopyValue();
    simpleOperation(result, in, check, SimpleValues.DEF_PATH, SimpleValues.DEF_OBJ_PATH + SimpleValues.DEF_PATH);
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test RFC 6902 COPY operation for non existing location in object.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.5. copy</a>} defines:<br>
   * The "from" location MUST exist for the operation to be successful.
   */
  private void testCopyOfNonExistingLocationInObject(final TestResult result) {
    LOGGER.info(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { SimpleValues.createEmptyObject(),
        SimpleValues.createSimpleObject(), SimpleValues.createCompoundObject() };
    final String[] paths = new String[] { SimpleValues.STR_PATH, SimpleValues.INT_PATH, SimpleValues.BOOL_PATH,
        SimpleValues.OBJ_PATH };
    final Object[] values = new Object[] { SimpleValues.OBJ_PATH, SimpleValues.BOOL_PATH, SimpleValues.INT_PATH,
        SimpleValues.STR_PATH };
    // Go trough all objects
    for (int i = 0; i < objsIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, objsIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Test RFC 6902 COPY operation for non existing location in array.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.5. copy</a>} defines:<br>
   * The "from" location MUST exist for the operation to be successful.
   */
  private void testCopyOfNonExistingLocationInArray(final TestResult result) {
    LOGGER.info(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { SimpleValues.createEmptyArray(),
        SimpleValues.createStringArray1(), SimpleValues.createIntArray2(), SimpleValues.createSimpleBoolArray5(),
        SimpleValues.createObjectArray2() };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        SimpleValues.STR_PATH + "/0" };
    final Object[] values = new Object[] { "/0", "/1", "/2", "/5", "/1" };
    // Go trough all arrays
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, arraysIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Tested operation name {@code "COPY"}.
   * 
   * @return Operation name to be used in logs.
   */
  @Override
  protected String operationName() {
    return OPERATION;
  }

  /**
   * Create and initialize patch builder to contain COPY operation to be
   * applied.
   * 
   * @param path
   *          Source JSON path of COPY operation.
   * @param value
   *          Target JSON path of COPY operation. Must be instance of
   *          {@link String}.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    if (value instanceof String) {
      // LOGGER.info(" COPY "+path+" -> "+(String)value);
      return Json.createPatchBuilder().copy((String) value, path);
    } else {
      throw new IllegalArgumentException(
          "Argument \"value\" is not an instance of String");
    }
  }

  /**
   * Update patch builder to contain next COPY operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          Source JSON path of COPY operation.
   * @param value
   *          Target JSON path of COPY operation. Must be instance of
   *          {@link String}.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    if (value instanceof String) {
      // LOGGER.info(" COPY "+path+" -> "+(String)value);
      return builder.copy((String) value, path);
    } else {
      throw new IllegalArgumentException(
          "Argument \"value\" is not an instance of String");
    }
  }

}

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

package ee.jakarta.tck.jsonp.api.patchtests;

import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatchBuilder;

import java.util.logging.Logger;

// $Id$
/*
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Implements {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902: 4.2. remove</a>}
 * tests.
 */
public class PatchOperationRemove extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationRemove.class.getName());
  
  /** Tested operation name. */
  private final String OPERATION = "REMOVE";

  /**
   * Creates an instance of RFC 6902 remove operation test.
   */
  PatchOperationRemove() {
    super();
  }

  /**
   * Test RFC 6902 remove operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 remove operation");
    LOGGER.info("Testing RFC 6902 remove operation:");
    testRemoveStringOnEmptyObject(result);
    testRemoveStringOnEmptyArray(result);
    testRemoveStringOnSimpleObject(result);
    testRemoveStringOnSimpleArray(result);
    testRemoveStringOnSimpleArray2(result);
    testRemoveIntOnEmptyObject(result);
    testRemoveIntOnEmptyArray(result);
    testRemoveIntOnSimpleObject(result);
    testRemoveIntOnSimpleArray(result);
    testRemoveIntOnSimpleArray2(result);
    testRemoveBoolOnEmptyObject(result);
    testRemoveBoolOnEmptyArray(result);
    testRemoveBoolOnSimpleObject(result);
    testRemoveBoolOnSimpleArray(result);
    testRemoveBoolOnSimpleArray2(result);
    testRemoveObjectOnEmptyObject(result);
    testRemoveObjectOnEmptyArray(result);
    testRemoveObjectOnSimpleObject(result);
    testRemoveObjectOnSimpleArray(result);
    testRemoveObjectOnSimpleArray2(result);
    testRemoveFromNonExistingLocationInObject(result);
    testRemoveFromNonExistingLocationInArray(result);
    return result;
  }

  /**
   * Test pointer remove operation for {@code String} to produce empty JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for String to produce empty JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createEmptyObject();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, null);
  }

  /**
   * Test pointer remove operation for {@code String} to produce empty JSON
   * array. Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for String to produce empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArrayWithStr();
    final JsonArray check = SimpleValues.createEmptyArray();
    simpleOperation(result, in, check, "/0", null);
  }

  /**
   * Test pointer remove operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithStr();
    final JsonObject check = SimpleValues.createSimpleObject();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, null);
  }

  /**
   * Test pointer remove operation for {@code String} on simple JSON array of
   * size 2. Using index {@code 0} to remove {@code String} before another
   * existing element and index {@code 1} to remove {@code String} after another
   * existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 2");
    final JsonArray inBefore = SimpleValues.createSimpleStringArrayWithStrBefore();
    final JsonArray inAfter = SimpleValues.createSimpleStringArrayWithStrAfter();
    final JsonArray check = SimpleValues.createStringArray1();
    simpleOperation(result, inBefore, check, "/0", null);
    simpleOperation(result, inAfter, check, "/1", null);
  }

  /**
   * Test pointer REMOVE for {@code String} on simple JSON array of size 5.
   * Starting with an array of size 2.
   * <ul>
   * <li>Removing {@code String} at the end, at the middle and at the beginning
   * of this array.
   * <li>Removing {@code String} at the beginning, in the middle and at the end
   * of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleStringArray5();
    final JsonArray check = SimpleValues.createStringArray2();
    complexOperation(result, in, check, new String[] { "/4", "/2", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/2" });
  }

  /**
   * Test pointer REMOVE operation for {@code int} to produce empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveIntOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for int to produce empty JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createEmptyObject();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code int} to produce empty JSON array.
   * Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveIntOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for int to produce empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArrayWithInt();
    final JsonArray check = SimpleValues.createEmptyArray();
    simpleOperation(result, in, check, "/0", null);
  }

  /**
   * Test pointer REMOVE operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithInt();
    final JsonObject check = SimpleValues.createSimpleObject();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code int} on simple JSON array of size
   * 2. Using index {@code 0} to remove {@code int} before another existing
   * element and index {@code 1} to remove {@code int} after another existing
   * element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 2");
    final JsonArray inBefore = SimpleValues.createSimpleIntArrayWithIntBefore();
    final JsonArray inAfter = SimpleValues.createSimpleIntArrayWithIntAfter();
    final JsonArray check = SimpleValues.createIntArray1();
    simpleOperation(result, inBefore, check, "/0", null);
    simpleOperation(result, inAfter, check, "/1", null);
  }

  /**
   * Test pointer REMOVE for {@code int} on simple JSON array of size 5.
   * Starting with an array of size 5.
   * <ul>
   * <li>Removing {@code int} at the end, at the middle and at the beginning of
   * this array.
   * <li>Removing {@code int} at the beginning, in the middle and at the end of
   * this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveIntOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleIntArray5();
    final JsonArray check = SimpleValues.createIntArray2();
    complexOperation(result, in, check, new String[] { "/4", "/2", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/2" });
  }

  /**
   * Test pointer REMOVE operation for {@code boolean} to produce empty JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveBoolOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for boolean to produce empty JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createEmptyObject();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code boolean} to produce empty JSON
   * array. Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveBoolOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for boolean to produce empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArrayWithBool();
    final JsonArray check = SimpleValues.createEmptyArray();
    simpleOperation(result, in, check, "/0", null);
  }

  /**
   * Test pointer REMOVE operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithBool();
    final JsonObject check = SimpleValues.createSimpleObject();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code boolean} on simple JSON array of
   * size 2. Using index {@code 0} to remove {@code boolean} before another
   * existing element and index {@code 1} to remove {@code boolean} after
   * another existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 2");
    final JsonArray inBefore = SimpleValues.createSimpleBoolArrayWithBoolBefore();
    final JsonArray inAfter = SimpleValues.createSimpleBoolArrayWithBoolAfter();
    final JsonArray check = SimpleValues.createBoolArray1();
    simpleOperation(result, inBefore, check, "/0", null);
    simpleOperation(result, inAfter, check, "/1", null);
  }

  /**
   * Test pointer REMOVE for {@code boolean} on simple JSON array of size 5.
   * Starting with an array of size 5.
   * <ul>
   * <li>Removing {@code boolean} at the end, at the middle and at the beginning
   * of this array.
   * <li>Removing {@code boolean} at the beginning, in the middle and at the end
   * of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveBoolOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleBoolArray5();
    final JsonArray check = SimpleValues.createBoolArray2();
    complexOperation(result, in, check, new String[] { "/4", "/2", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/2" });
  }

  /**
   * Test pointer REMOVE operation for {@code JsonObject} to produce empty JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveObjectOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for JsonObject to produce empty JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectObject();
    final JsonObject check = SimpleValues.createEmptyObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code JsonObject} to produce empty JSON
   * array. Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveObjectOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for JsonObject to produce empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArrayWithObject();
    final JsonArray check = SimpleValues.createEmptyArray();
    simpleOperation(result, in, check, "/0", null);
  }

  /**
   * Test pointer REMOVE operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveObjectOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createCompoundObjectWithObject();
    final JsonObject check = SimpleValues.createCompoundObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, null);
  }

  /**
   * Test pointer REMOVE operation for {@code JsonObject} on simple JSON array
   * of size 2. Using index {@code 0} to remove {@code JsonObject} before
   * another existing element and index {@code 1} to remove {@code JsonObject}
   * after another existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 2");
    final JsonArray inBefore = SimpleValues.createSimpleObjectArrayWithObjectBefore();
    final JsonArray inAfter = SimpleValues.createSimpleObjectArrayWithObjectAfter();
    final JsonArray check = SimpleValues.createObjectArray1();
    simpleOperation(result, inBefore, check, "/0", null);
    simpleOperation(result, inAfter, check, "/1", null);
  }

  /**
   * Test pointer REMOVE for {@code JsonObject} on simple JSON array of size 5.
   * Starting with an array of size 5.
   * <ul>
   * <li>Removing {@code JsonObject} at the end, at the middle and at the
   * beginning of this array.
   * <li>Removing {@code JsonObject} at the beginning, in the middle and at the
   * end of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveObjectOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleObjectArray5();
    final JsonArray check = SimpleValues.createObjectArray2();
    complexOperation(result, in, check, new String[] { "/4", "/2", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/2" });
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test pointer REMOVE for non existing location in object.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902:
   * 4.2. remove</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testRemoveFromNonExistingLocationInObject(
      final TestResult result) {
    LOGGER.info(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { SimpleValues.createEmptyObject(),
        SimpleValues.createSimpleObject(), SimpleValues.createCompoundObject() };
    final String[] paths = new String[] { SimpleValues.STR_PATH, SimpleValues.INT_PATH, SimpleValues.BOOL_PATH,
        SimpleValues.OBJ_PATH };
    // Go trough all objects
    for (int i = 0; i < objsIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, objsIn[i], paths[j], null);
      }
    }
  }

  /**
   * Test pointer REMOVE for non existing location in array.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902:
   * 4.2. remove</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testRemoveFromNonExistingLocationInArray(
      final TestResult result) {
    LOGGER.info(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { SimpleValues.createEmptyArray(),
        SimpleValues.createStringArray1(), SimpleValues.createIntArray2(), SimpleValues.createSimpleBoolArray5(),
        SimpleValues.createObjectArray2()

    };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        SimpleValues.STR_PATH + "/0" };
    // Go trough all arrays
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, arraysIn[i], paths[j], null);
      }
    }
  }

  /**
   * Tested operation name {@code "REMOVE"}.
   * 
   * @return Operation name to be used in logs.
   */
  @Override
  protected String operationName() {
    return OPERATION;
  }

  /**
   * Create and initialize patch builder to contain REMOVE operation to be
   * applied.
   * 
   * @param path
   *          JSON path of value to removed.
   * @param value
   *          Not used for REMOVE operation.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    return Json.createPatchBuilder().remove(path);
  }

  /**
   * Update patch builder to contain next REMOVE operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          JSON path of value to removed.
   * @param value
   *          Not used for REMOVE operation.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    return builder.remove(path);
  }

}

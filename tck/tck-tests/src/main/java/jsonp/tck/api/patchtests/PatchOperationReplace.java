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

package jsonp.tck.api.patchtests;

import jsonp.tck.api.common.JsonValueType;
import jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Implements
 * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
 * 4.3. replace</a>} tests.
 */
public class PatchOperationReplace extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationReplace.class.getName());

  /** Tested operation name. */
  private final String OPERATION = "REPLACE";

  /**
   * Creates an instance of RFC 6902 replace operation test.
   */
  PatchOperationReplace() {
    super();
  }

  /**
   * Test RFC 6902 replace operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 replace operation");
    LOGGER.info("Testing RFC 6902 replace operation:");
    testReplaceStringOnSimpleObject(result);
    testReplaceStringOnSimpleArray(result);
    testReplaceStringOnSimpleArray2(result);
    testReplaceIntOnSimpleObject(result);
    testReplaceIntOnSimpleArray(result);
    testReplaceIntOnSimpleArray2(result);
    testReplaceBoolOnSimpleObject(result);
    testReplaceBoolOnSimpleArray(result);
    testReplaceBoolOnSimpleArray2(result);
    testReplaceObjectOnCompoundObject(result);
    testReplaceObjectOnSimpleArray(result);
    testReplaceObjectOnSimpleArray2(result);
    testReplaceOfNonExistingLocationInObject(result);
    testReplaceOfNonExistingLocationInArray(result);
    return result;
  }

  /**
   * Test pointer replace operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceStr();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, SimpleValues.STR_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createStringArray1();
    final JsonArray check = SimpleValues.createSimpleStringArrayReplaceStr();
    simpleOperation(result, in, check, "/0", SimpleValues.STR_VALUE);
  }

  /**
   * Test pointer replace operation for {@code String} on simple JSON array of
   * size 5. Starting with an array of size 5.
   * <ul>
   * <li>Replacing {@code String} items from the end to the beginning of this
   * array.
   * <li>Replacing {@code String} from the beginning to the end of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleStringArray5();
    final JsonArray check = SimpleValues.createSimpleStringArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new String[] { SimpleValues.STR_VALUE_1, SimpleValues.STR_VALUE_2, SimpleValues.STR_VALUE_4, SimpleValues.STR_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new String[] { SimpleValues.STR_VALUE_5, SimpleValues.STR_VALUE_4, SimpleValues.STR_VALUE_2, SimpleValues.STR_VALUE_1 });
  }

  /**
   * Test pointer replace operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceInt();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, SimpleValues.INT_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createIntArray1();
    final JsonArray check = SimpleValues.createSimpleIntArrayReplaceInt();
    simpleOperation(result, in, check, "/0", SimpleValues.INT_VALUE);
  }

  /**
   * Test pointer replace operation for {@code int} on simple JSON array of size
   * 5. Starting with an array of size 5.
   * <ul>
   * <li>Replacing {@code int} items from the end to the beginning of this
   * array.
   * <li>Replacing {@code int} from the beginning to the end of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleIntArray5();
    final JsonArray check = SimpleValues.createSimpleIntArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new Integer[] { SimpleValues.INT_VALUE_1, SimpleValues.INT_VALUE_2, SimpleValues.INT_VALUE_4, SimpleValues.INT_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new Integer[] { SimpleValues.INT_VALUE_5, SimpleValues.INT_VALUE_4, SimpleValues.INT_VALUE_2, SimpleValues.INT_VALUE_1 });
  }

  /**
   * Test pointer replace operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceBool();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, SimpleValues.BOOL_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createBoolArray1();
    final JsonArray check = SimpleValues.createSimpleBoolArrayReplaceBool();
    simpleOperation(result, in, check, "/0", SimpleValues.BOOL_FALSE);
  }

  /**
   * Test pointer replace operation for {@code boolean} on simple JSON array of
   * size 5. Starting with an array of size 5.
   * <ul>
   * <li>Replacing {@code boolean} items from the end to the beginning of this
   * array.
   * <li>Replacing {@code boolean} from the beginning to the end of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleBoolArray5();
    final JsonArray check = SimpleValues.createSimpleBoolArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new Boolean[] { SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new Boolean[] { SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE });
  }

  /**
   * Test pointer replace operation for {@code JsonObject} on compound JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnCompoundObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createCompoundObjectWithObject();
    final JsonObject check = SimpleValues.createCompoundObjectReplaceObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, SimpleValues.OBJ_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createObjectArray1();
    final JsonArray check = SimpleValues.createSimpleObjectArrayReplaceObject();
    simpleOperation(result, in, check, "/0", SimpleValues.OBJ_VALUE);
  }

  /**
   * Test pointer replace operation for {@code JsonObject} on simple JSON array
   * of size 5. Starting with an array of size 5.
   * <ul>
   * <li>Replacing {@code JsonObject} items from the end to the beginning of
   * this array.
   * <li>Replacing {@code JsonObject} from the beginning to the end of this
   * array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleObjectArray5();
    final JsonArray check = SimpleValues.createSimpleObjectArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_1, SimpleValues.OBJ_VALUE_2, SimpleValues.OBJ_VALUE_4,
            SimpleValues.OBJ_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_5, SimpleValues.OBJ_VALUE_4, SimpleValues.OBJ_VALUE_2,
            SimpleValues.OBJ_VALUE_1 });
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test pointer replace for non existing location in object.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
   * 4.3. replace</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testReplaceOfNonExistingLocationInObject(
      final TestResult result) {
    LOGGER.info(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { SimpleValues.createEmptyObject(),
        SimpleValues.createSimpleObject(), SimpleValues.createCompoundObject() };
    final String[] paths = new String[] { SimpleValues.STR_PATH, SimpleValues.INT_PATH, SimpleValues.BOOL_PATH,
        SimpleValues.OBJ_PATH };
    final Object[] values = new Object[] { SimpleValues.STR_VALUE, SimpleValues.INT_VALUE, SimpleValues.BOOL_VALUE,
        SimpleValues.OBJ_VALUE };
    // Go trough all objects
    for (int i = 0; i < objsIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, objsIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Test pointer replace for non existing location in array.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
   * 4.3. replace</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testReplaceOfNonExistingLocationInArray(
      final TestResult result) {
    LOGGER.info(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { SimpleValues.createEmptyArray(),
        SimpleValues.createStringArray1(), SimpleValues.createIntArray2(), SimpleValues.createSimpleBoolArray5(),
        SimpleValues.createObjectArray2() };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        SimpleValues.STR_PATH + "/0" };
    final Object[] values = new Object[] { SimpleValues.STR_VALUE, SimpleValues.STR_VALUE, SimpleValues.INT_VALUE,
        SimpleValues.BOOL_VALUE, SimpleValues.OBJ_VALUE };
    // Go trough all arrays
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, arraysIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Tested operation name {@code "MOVE"}.
   * 
   * @return Operation name to be used in logs.
   */
  @Override
  protected String operationName() {
    return OPERATION;
  }

  /**
   * Create and initialize patch builder to contain REPLACE operation to be
   * applied.
   * 
   * @param path
   *          JSON path of value to be replaced.
   * @param value
   *          Value to replace previous one.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    return builderReplace(Json.createPatchBuilder(), path, value);
  }

  /**
   * Update patch builder to contain next REPLACE operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          JSON path of value to be replaced.
   * @param value
   *          Value to replace previous one.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    return builderReplace(builder, path, value);
  }

  /**
   * Add REPLACE {@code value} at {@code path} operation to provided JSON patch
   * builder.
   * 
   * @param builder
   *          Target JSON patch builder.
   * @param path
   *          JSON path of value to be replaced.
   * @param value
   *          Value to be replaced at given JSON path.
   * @return JSON patch builder containing new {@code value} at {@code path}
   *         replaced.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  private static JsonPatchBuilder builderReplace(final JsonPatchBuilder builder,
      final String path, final Object value) {
    switch (JsonValueType.getType(value.getClass())) {
    case String:
      return builder.replace(path, (String) value);
    case Integer:
      return builder.replace(path, ((Integer) value).intValue());
    case Boolean:
      return builder.replace(path, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.replace(path, (JsonValue) value);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

}

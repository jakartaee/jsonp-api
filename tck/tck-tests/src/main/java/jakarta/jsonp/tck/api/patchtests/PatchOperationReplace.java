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

package jakarta.jsonp.tck.api.patchtests;

import jakarta.jsonp.tck.api.common.JsonValueType;
import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

import static jakarta.jsonp.tck.api.common.SimpleValues.*;

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
    final JsonObject in = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectReplaceStr();
    simpleOperation(result, in, check, STR_PATH, STR_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 1");
    final JsonArray in = createStringArray1();
    final JsonArray check = createSimpleStringArrayReplaceStr();
    simpleOperation(result, in, check, "/0", STR_VALUE);
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
    final JsonArray in = createSimpleStringArray5();
    final JsonArray check = createSimpleStringArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new String[] { STR_VALUE_1, STR_VALUE_2, STR_VALUE_4, STR_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new String[] { STR_VALUE_5, STR_VALUE_4, STR_VALUE_2, STR_VALUE_1 });
  }

  /**
   * Test pointer replace operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectReplaceInt();
    simpleOperation(result, in, check, INT_PATH, INT_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 1");
    final JsonArray in = createIntArray1();
    final JsonArray check = createSimpleIntArrayReplaceInt();
    simpleOperation(result, in, check, "/0", INT_VALUE);
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
    final JsonArray in = createSimpleIntArray5();
    final JsonArray check = createSimpleIntArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new Integer[] { INT_VALUE_1, INT_VALUE_2, INT_VALUE_4, INT_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new Integer[] { INT_VALUE_5, INT_VALUE_4, INT_VALUE_2, INT_VALUE_1 });
  }

  /**
   * Test pointer replace operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectReplaceBool();
    simpleOperation(result, in, check, BOOL_PATH, BOOL_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 1");
    final JsonArray in = createBoolArray1();
    final JsonArray check = createSimpleBoolArrayReplaceBool();
    simpleOperation(result, in, check, "/0", BOOL_FALSE);
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
    final JsonArray in = createSimpleBoolArray5();
    final JsonArray check = createSimpleBoolArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new Boolean[] { BOOL_FALSE, BOOL_TRUE, BOOL_FALSE, BOOL_TRUE });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new Boolean[] { BOOL_TRUE, BOOL_FALSE, BOOL_TRUE, BOOL_FALSE });
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
    final JsonObject in = createCompoundObjectWithObject();
    final JsonObject check = createCompoundObjectReplaceObject();
    simpleOperation(result, in, check, OBJ_PATH, OBJ_VALUE2);
  }

  /**
   * Test pointer replace operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = createObjectArray1();
    final JsonArray check = createSimpleObjectArrayReplaceObject();
    simpleOperation(result, in, check, "/0", OBJ_VALUE);
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
    final JsonArray in = createSimpleObjectArray5();
    final JsonArray check = createSimpleObjectArray5R();
    complexOperation(result, in, check, new String[] { "/4", "/3", "/1", "/0" },
        new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_2, OBJ_VALUE_4,
            OBJ_VALUE_5 });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/3", "/4" },
        new JsonObject[] { OBJ_VALUE_5, OBJ_VALUE_4, OBJ_VALUE_2,
            OBJ_VALUE_1 });
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
    final JsonObject[] objsIn = new JsonObject[] { createEmptyObject(),
        createSimpleObject(), createCompoundObject() };
    final String[] paths = new String[] { STR_PATH, INT_PATH, BOOL_PATH,
        OBJ_PATH };
    final Object[] values = new Object[] { STR_VALUE, INT_VALUE, BOOL_VALUE,
        OBJ_VALUE };
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
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray1(), createIntArray2(), createSimpleBoolArray5(),
        createObjectArray2() };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        STR_PATH + "/0" };
    final Object[] values = new Object[] { STR_VALUE, STR_VALUE, INT_VALUE,
        BOOL_VALUE, OBJ_VALUE };
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

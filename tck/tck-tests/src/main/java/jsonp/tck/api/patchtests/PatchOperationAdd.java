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

import jsonp.tck.api.common.JsonValueType;
import jsonp.tck.api.common.TestResult;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jakarta.json.JsonStructure;
import jakarta.json.Json;
import jakarta.json.JsonPointer;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Implements
 * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
 * 4.1. add</a>} tests.
 */
class PatchOperationAdd extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationAdd.class.getName());
  
  /** Tested operation name. */
  private final String OPERATION = "ADD";

  /**
   * Creates an instance of RFC 6902 add operation test.
   */
  PatchOperationAdd() {
    super();
  }

  /**
   * Test RFC 6902 add operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 add operation");
    LOGGER.info("Testing RFC 6902 add operation:");
    testAddStringOnEmptyObject(result);
    testAddStringOnSimpleObject(result);
    testAddStringOnEmptyArray(result);
    testAddStringOnSimpleArray(result);
    testAddStringOnSimpleArray2(result);
    testAddIntOnEmptyObject(result);
    testAddIntOnSimpleObject(result);
    testAddIntOnEmptyArray(result);
    testAddIntOnSimpleArray(result);
    testAddIntOnSimpleArray2(result);
    testAddBooleanOnEmptyObject(result);
    testAddBooleanOnSimpleObject(result);
    testAddBooleanOnEmptyArray(result);
    testAddBooleanOnSimpleArray(result);
    testAddBooleanOnSimpleArray2(result);
    testAddObjectOnEmptyObject(result);
    testAddObjectOnSimpleObject(result);
    testAddObjectOnEmptyArray(result);
    testAddObjectOnSimpleArray(result);
    testAddObjectOnSimpleArray2(result);
    testAddArrayToReplaceObject(result);
    testAddArrayToReplaceDocument(result);
    testAddStringArrayToStringArray(result);
    testAddStringToNonExistingObject(result);
    return result;
  }

  /**
   * Test RFC 6902 add operation for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for String on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectStr();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, SimpleValues.STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for String on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithStr();
    simpleOperation(result, in, check, "/0", SimpleValues.STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithStr();
    simpleOperation(result, in, check, SimpleValues.STR_PATH, SimpleValues.STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String} on simple JSON array. Using
   * index {@code 0} to add {@code String} before already existing element and
   * index {@code 1} to add {@code String} after already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createStringArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleStringArrayWithStrBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleStringArrayWithStrAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", SimpleValues.STR_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", SimpleValues.STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String}s on simple JSON array.
   * Starting with an array of size 2.
   * <ul>
   * <li>Adding {@code String} at the end, in the middle and at the beginning of
   * this array.
   * <li>Adding {@code String} at the beginning, in the middle and at the end of
   * this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createStringArray2();
    final JsonArray check = SimpleValues.createSimpleStringArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new String[] { SimpleValues.STR_VALUE_5, SimpleValues.STR_VALUE_3, SimpleValues.STR_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new String[] { SimpleValues.STR_VALUE_1, SimpleValues.STR_VALUE_3, SimpleValues.STR_VALUE_5 });
  }

  /**
   * Test RFC 6902 add operation for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for int on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectInt();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, SimpleValues.INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for int on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithInt();
    simpleOperation(result, in, check, "/0", SimpleValues.INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithInt();
    simpleOperation(result, in, check, SimpleValues.INT_PATH, SimpleValues.INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int} on simple JSON array. Using
   * index {@code 0} to add {@code int} before already existing element and
   * index {@code 1} to add {@code int} after already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createIntArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleIntArrayWithIntBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleIntArrayWithIntAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", SimpleValues.INT_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", SimpleValues.INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int}s on simple JSON array. Starting
   * with an array of size 2.
   * <ul>
   * <li>Adding {@code int} at the end, in the middle and at the beginning of
   * this array.
   * <li>Adding {@code int} at the beginning, in the middle and at the end of
   * this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createIntArray2();
    final JsonArray check = SimpleValues.createSimpleIntArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new Integer[] { SimpleValues.INT_VALUE_5, SimpleValues.INT_VALUE_3, SimpleValues.INT_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new Integer[] { SimpleValues.INT_VALUE_1, SimpleValues.INT_VALUE_3, SimpleValues.INT_VALUE_5 });
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for boolean on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectBool();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, SimpleValues.BOOL_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for boolean on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithBool();
    simpleOperation(result, in, check, "/0", SimpleValues.BOOL_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithBool();
    simpleOperation(result, in, check, SimpleValues.BOOL_PATH, SimpleValues.BOOL_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on simple JSON array. Using
   * index {@code 0} to add {@code boolean} before already existing element and
   * index {@code 1} to add {@code boolean} after already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createBoolArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleBoolArrayWithBoolBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleBoolArrayWithBoolAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", SimpleValues.BOOL_FALSE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", SimpleValues.BOOL_FALSE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean}s on simple JSON array.
   * Starting with an array of size 2.
   * <ul>
   * <li>Adding {@code boolean} at the end, in the middle and at the beginning
   * of this array.
   * <li>Adding {@code boolean} at the beginning, in the middle and at the end
   * of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createBoolArray2();
    final JsonArray check = SimpleValues.createSimpleBoolArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new Boolean[] { SimpleValues.BOOL_TRUE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new Boolean[] { SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_TRUE });
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, SimpleValues.OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on empty JSON array.
   * Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithObject();
    simpleOperation(result, in, check, "/0", SimpleValues.OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonObject check = SimpleValues.createCompoundObjectWithObject();
    simpleOperation(result, in, check, SimpleValues.OBJ_PATH, SimpleValues.OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on simple JSON array.
   * Using index {@code 0} to add {@code JsonObject} before already existing
   * element and index {@code 1} to add {@code JsonObject} after already
   * existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createObjectArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleObjectArrayWithObjectBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleObjectArrayWithObjectAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", SimpleValues.OBJ_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", SimpleValues.OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject}s on simple JSON array.
   * Starting with an array of size 2.
   * <ul>
   * <li>Adding {@code JsonObject} at the end, in the middle and at the
   * beginning of this array.
   * <li>Adding {@code JsonObject} at the beginning, in the middle and at the
   * end of this array.
   * </ul>
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createObjectArray2();
    final JsonArray check = SimpleValues.createSimpleObjectArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_5, SimpleValues.OBJ_VALUE_3, SimpleValues.OBJ_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_1, SimpleValues.OBJ_VALUE_3, SimpleValues.OBJ_VALUE_5 });
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test that existing target object is replaced by specified array when ADD
   * operation is applied.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
   * 4.1. add</a>}:<br>
   * When the operation is applied, the target location MUST reference one of:
   * <ul>
   * <li>A member to add to an existing object - whereupon the supplied value is
   * added to that object at the indicated location. If the member already
   * exists, it is replaced by the specified value.</li>
   * <li>...</li>
   * </ul>
   */
  private void testAddArrayToReplaceObject(final TestResult result) {
    LOGGER.info(" - for JsonArray to replace JsonObject");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonObject check = SimpleValues.createCompoundObjectWithObjectReplaced();
    final JsonArray value = SimpleValues.createSimpleStringArray5();
    simpleOperation(result, in, check, SimpleValues.DEF_OBJ_PATH, value);
  }

  /**
   * Test that whole document is replaced by specified array when ADD operation
   * is applied with root pointer.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
   * 4.1. add</a>}:<br>
   * When the operation is applied, the target location MUST reference one of:
   * <ul>
   * <li>The root of the target document - whereupon the specified value becomes
   * the entire content of the target document.</li>
   * <li>...</li>
   * </ul>
   */
  private void testAddArrayToReplaceDocument(final TestResult result) {
    LOGGER.info(" - for JsonArray to replace whole document");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonArray check = SimpleValues.createSimpleStringArray5();
    final JsonArray value = SimpleValues.createSimpleStringArray5();
    // Instance being replaced is JsonObject, instance being added is JsonArray.
    // The only API method allowing
    // this is the one working with JsonStructure. New builder instance is used
    // for each of the cases.
    final JsonPatch patch1 = builderAdd(Json.createPatchBuilder(), "", value)
        .build();
    final JsonValue out1 = patch1.apply((JsonStructure) in);
    if (!JsonAssert.assertEquals(check, out1)) {
      final String className = value.getClass().getSimpleName();
      result.fail("ADD " + className + " to compound object",
          "ADD operation for " + className + " failed on compound value");
    }
  }

  /**
   * Test ADD operation of an array of {@code String}s into existing array of
   * {@code String}s. This scenario is inspired by
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
   * 4.1. add</a>} operation example {@code { "op": "add", "path": "/a/b/c",
   * "value": [ "foo", "bar" ] }} and following explanation of this operation on
   * an array:
   * <ul>
   * <li>An element to add to an existing array - whereupon the supplied value
   * is added to the array at the indicated location. Any elements at or above
   * the specified index are shifted one position to the right. The specified
   * index MUST NOT be greater than the number of elements in the array. If the
   * "-" character is used to index the end of the array (see [RFC6901]), this
   * has the effect of appending the value to the array.</li>
   * </ul>
   */
  private void testAddStringArrayToStringArray(final TestResult result) {
    LOGGER.info(" - for String array to be added to existing String array");
    final JsonArray in = SimpleValues.createStringArray2();
    final JsonArray check = SimpleValues.createStringArray2WithStringArrayInTheMiddle();
    final JsonArray value = SimpleValues.createStringInnerArray2();
    simpleOperation(result, in, check, "/1", value);
  }

  /**
   * Test ADD operation on non existing JsonObject. This scenario is described
   * in {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC
   * 6902: 4.1. add</a>} error handling samples. Test is trying to ADD value {
   * "address" : "In a galaxy far far away"} into object { "name" : "John Smith"
   * } using path "/child/address". Even "/child" path does not exist so this
   * operation must fail.
   * 
   */
  private void testAddStringToNonExistingObject(final TestResult result) {
    LOGGER.info(" - for String to be added to non existing JsonObject");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonValue value = Json.createValue(SimpleValues.STR_VALUE);
    final String path = SimpleValues.DEF_OBJ_PATH + SimpleValues.STR_PATH;
    final JsonPointer ptr = Json.createPointer(path);
    simpleOperationFail(result, in, path, value);
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
   * Create and initialize patch builder to contain ADD operation to be applied.
   * 
   * @param path
   *          JSON path of value to be added.
   * @param value
   *          JSON Value to be added.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    return builderAdd(Json.createPatchBuilder(), path, value);
  }

  /**
   * Update patch builder to contain next ADD operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          JSON path of value to be added.
   * @param value
   *          JSON Value to be added.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    return builderAdd(builder, path, value);
  }

  /**
   * Add {@code value} at {@code path} to provided JSON patch builder.
   * 
   * @param builder
   *          Target JSON patch builder.
   * @param path
   *          JSON path of value to be added.
   * @param value
   *          Value to be added at given JSON path.
   * @return JSON patch builder containing new {@code value} at {@code path}
   *         added.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  private static JsonPatchBuilder builderAdd(final JsonPatchBuilder builder,
      final String path, final Object value) {
    switch (JsonValueType.getType(value.getClass())) {
    case String:
      return builder.add(path, (String) value);
    case Integer:
      return builder.add(path, ((Integer) value).intValue());
    case Boolean:
      return builder.add(path, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.add(path, (JsonValue) value);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

}

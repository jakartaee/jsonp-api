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

package jakarta.jsonp.tck.api.patchtests;

import jakarta.jsonp.tck.api.common.JsonValueType;
import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jakarta.json.JsonStructure;
import jakarta.json.Json;
import jakarta.json.JsonPointer;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

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
    System.out.println("Testing RFC 6902 add operation:");
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
    System.out.println(" - for String on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectStr();
    simpleOperation(result, in, check, STR_PATH, STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnEmptyArray(final TestResult result) {
    System.out.println(" - for String on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithStr();
    simpleOperation(result, in, check, "/0", STR_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleObject(final TestResult result) {
    System.out.println(" - for String on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithStr();
    simpleOperation(result, in, check, STR_PATH, STR_VALUE);
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
    System.out.println(" - for String on simple JSON array of size 1");
    final JsonArray in = createStringArray1();
    final JsonArray checkBefore = createSimpleStringArrayWithStrBefore();
    final JsonArray checkAfter = createSimpleStringArrayWithStrAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", STR_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", STR_VALUE);
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
    System.out.println(" - for String on simple JSON array of size 2");
    final JsonArray in = createStringArray2();
    final JsonArray check = createSimpleStringArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new String[] { STR_VALUE_5, STR_VALUE_3, STR_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new String[] { STR_VALUE_1, STR_VALUE_3, STR_VALUE_5 });
  }

  /**
   * Test RFC 6902 add operation for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyObject(final TestResult result) {
    System.out.println(" - for int on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectInt();
    simpleOperation(result, in, check, INT_PATH, INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyArray(final TestResult result) {
    System.out.println(" - for int on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithInt();
    simpleOperation(result, in, check, "/0", INT_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleObject(final TestResult result) {
    System.out.println(" - for int on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithInt();
    simpleOperation(result, in, check, INT_PATH, INT_VALUE);
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
    System.out.println(" - for int on simple JSON array of size 1");
    final JsonArray in = createIntArray1();
    final JsonArray checkBefore = createSimpleIntArrayWithIntBefore();
    final JsonArray checkAfter = createSimpleIntArrayWithIntAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", INT_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", INT_VALUE);
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
    System.out.println(" - for int on simple JSON array of size 2");
    final JsonArray in = createIntArray2();
    final JsonArray check = createSimpleIntArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new Integer[] { INT_VALUE_5, INT_VALUE_3, INT_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new Integer[] { INT_VALUE_1, INT_VALUE_3, INT_VALUE_5 });
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnEmptyObject(final TestResult result) {
    System.out.println(" - for boolean on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectBool();
    simpleOperation(result, in, check, BOOL_PATH, BOOL_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on empty JSON array. Only
   * allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnEmptyArray(final TestResult result) {
    System.out.println(" - for boolean on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithBool();
    simpleOperation(result, in, check, "/0", BOOL_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBooleanOnSimpleObject(final TestResult result) {
    System.out.println(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithBool();
    simpleOperation(result, in, check, BOOL_PATH, BOOL_VALUE);
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
    System.out.println(" - for boolean on simple JSON array of size 1");
    final JsonArray in = createBoolArray1();
    final JsonArray checkBefore = createSimpleBoolArrayWithBoolBefore();
    final JsonArray checkAfter = createSimpleBoolArrayWithBoolAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", BOOL_FALSE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", BOOL_FALSE);
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
    System.out.println(" - for boolean on simple JSON array of size 2");
    final JsonArray in = createBoolArray2();
    final JsonArray check = createSimpleBoolArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new Boolean[] { BOOL_TRUE, BOOL_TRUE, BOOL_FALSE });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new Boolean[] { BOOL_FALSE, BOOL_TRUE, BOOL_TRUE });
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyObject(final TestResult result) {
    System.out.println(" - for JsonObject on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectObject();
    simpleOperation(result, in, check, OBJ_PATH, OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on empty JSON array.
   * Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyArray(final TestResult result) {
    System.out.println(" - for JsonObject on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithObject();
    simpleOperation(result, in, check, "/0", OBJ_VALUE);
  }

  /**
   * Test RFC 6902 add operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleObject(final TestResult result) {
    System.out.println(" - for JsonObject on simple JSON object");
    final JsonObject in = createCompoundObject();
    final JsonObject check = createCompoundObjectWithObject();
    simpleOperation(result, in, check, OBJ_PATH, OBJ_VALUE);
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
    System.out.println(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = createObjectArray1();
    final JsonArray checkBefore = createSimpleObjectArrayWithObjectBefore();
    final JsonArray checkAfter = createSimpleObjectArrayWithObjectAfter();
    // Add before.
    simpleOperation(result, in, checkBefore, "/0", OBJ_VALUE);
    // Add after.
    simpleOperation(result, in, checkAfter, "/1", OBJ_VALUE);
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
    System.out.println(" - for JsonObject on simple JSON array of size 2");
    final JsonArray in = createObjectArray2();
    final JsonArray check = createSimpleObjectArray5();
    complexOperation(result, in, check, new String[] { "/2", "/1", "/0" },
        new JsonObject[] { OBJ_VALUE_5, OBJ_VALUE_3, OBJ_VALUE_1 });
    complexOperation(result, in, check, new String[] { "/0", "/2", "/4" },
        new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_3, OBJ_VALUE_5 });
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
    System.out.println(" - for JsonArray to replace JsonObject");
    final JsonObject in = createCompoundObject();
    final JsonObject check = createCompoundObjectWithObjectReplaced();
    final JsonArray value = createSimpleStringArray5();
    simpleOperation(result, in, check, DEF_OBJ_PATH, value);
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
    System.out.println(" - for JsonArray to replace whole document");
    final JsonObject in = createCompoundObject();
    final JsonArray check = createSimpleStringArray5();
    final JsonArray value = createSimpleStringArray5();
    // Instance being replaced is JsonObject, instance being added is JsonArray.
    // The only API method allowing
    // this is the one working with JsonStructure. New builder instance is used
    // for each of the cases.
    final JsonPatch patch1 = builderAdd(Json.createPatchBuilder(), "", value)
        .build();
    final JsonValue out1 = patch1.apply((JsonStructure) in);
    if (!assertEquals(check, out1)) {
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
    System.out.println(" - for String array to be added to existing String array");
    final JsonArray in = createStringArray2();
    final JsonArray check = createStringArray2WithStringArrayInTheMiddle();
    final JsonArray value = createStringInnerArray2();
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
    System.out.println(" - for String to be added to non existing JsonObject");
    final JsonObject in = createSimpleObject();
    final JsonValue value = Json.createValue(STR_VALUE);
    final String path = DEF_OBJ_PATH + STR_PATH;
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

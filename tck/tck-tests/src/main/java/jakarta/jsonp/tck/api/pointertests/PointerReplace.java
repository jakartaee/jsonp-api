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

package jakarta.jsonp.tck.api.pointertests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: pointer
 * usage for {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}
 * replace operation tests.<br>
 */
public class PointerReplace {

  /**
   * Creates an instance of RFC 6901 pointer instance usage for RFC 6902 replace
   * operation tests.
   */
  PointerReplace() {
    super();
  }

  /**
   * Test RFC 6901 pointer instance usage for RFC 6902 replace operation. Suite
   * entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 6901 pointer usage for RFC 6902 replace operation");
    System.out.println(
        "Testing RFC 6901 pointer usage for RFC 6902 replace operation");
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
   * Test pointer REPLACE operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleObject(final TestResult result) {
    System.out.println(" - for String on simple JSON object");
    final JsonObject in = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectReplaceStr();
    final JsonPointer ptr = Json.createPointer(STR_PATH);
    final JsonObject out = ptr.replace(in, Json.createValue(STR_VALUE2));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + STR_PATH
          + "\" REPLACE \"" + STR_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleArray(final TestResult result) {
    System.out.println(" - for String on simple JSON array of size 1");
    final JsonArray in = createStringArray1();
    final JsonArray check = createSimpleStringArrayReplaceStr();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, Json.createValue(STR_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + STR_VALUE + "\" failed on simple JSON array");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code String} on simple JSON array of
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
    System.out.println(" - for String on simple JSON array of size 5");
    final JsonArray in = createSimpleStringArray5();
    final JsonArray check = createSimpleStringArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new String[] { STR_VALUE_1, STR_VALUE_2, STR_VALUE_4, STR_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new String[] { STR_VALUE_5, STR_VALUE_4, STR_VALUE_2, STR_VALUE_1 },
        "Pointer REPLACE operation",
        "Pointers \"/0\", \"/1\", \"/3\", \"/4\" REPLACE sequence failed on simple JSON array");
  }

  /**
   * Test pointer REPLACE operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleObject(final TestResult result) {
    System.out.println(" - for int on simple JSON object");
    final JsonObject in = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectReplaceInt();
    final JsonPointer ptr = Json.createPointer(INT_PATH);
    final JsonObject out = ptr.replace(in, Json.createValue(INT_VALUE2));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + INT_PATH
          + "\" REPLACE \"" + INT_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleArray(final TestResult result) {
    System.out.println(" - for int on simple JSON array of size 1");
    final JsonArray in = createIntArray1();
    final JsonArray check = createSimpleIntArrayReplaceInt();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, Json.createValue(INT_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + INT_VALUE + "\" failed on simple JSON array");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code int} on simple JSON array of size
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
    System.out.println(" - for int on simple JSON array of size 5");
    final JsonArray in = createSimpleIntArray5();
    final JsonArray check = createSimpleIntArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new Integer[] { INT_VALUE_1, INT_VALUE_2, INT_VALUE_4, INT_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new Integer[] { INT_VALUE_5, INT_VALUE_4, INT_VALUE_2, INT_VALUE_1 },
        "Pointer REPLACE operation",
        "Pointers \"/0\", \"/1\", \"/3\", \"/4\" REPLACE sequence failed on simple JSON array");
  }

  /**
   * Test pointer REPLACE operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleObject(final TestResult result) {
    System.out.println(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectReplaceBool();
    final JsonPointer ptr = Json.createPointer(BOOL_PATH);
    final JsonObject out = ptr.replace(in, toJsonValue(BOOL_VALUE2));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + BOOL_PATH
          + "\" REPLACE \"" + BOOL_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleArray(final TestResult result) {
    System.out.println(" - for boolean on simple JSON array of size 1");
    final JsonArray in = createBoolArray1();
    final JsonArray check = createSimpleBoolArrayReplaceBool();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, toJsonValue(BOOL_FALSE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + BOOL_FALSE + "\" failed on simple JSON array");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code boolean} on simple JSON array of
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
    System.out.println(" - for boolean on simple JSON array of size 5");
    final JsonArray in = createSimpleBoolArray5();
    final JsonArray check = createSimpleBoolArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new Boolean[] { BOOL_FALSE, BOOL_TRUE, BOOL_FALSE, BOOL_TRUE },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new Boolean[] { BOOL_TRUE, BOOL_FALSE, BOOL_TRUE, BOOL_FALSE },
        "Pointer REPLACE operation",
        "Pointers \"/0\", \"/1\", \"/3\", \"/4\" REPLACE sequence failed on simple JSON array");
  }

  /**
   * Test pointer REPLACE operation for {@code JsonObject} on compound JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnCompoundObject(final TestResult result) {
    System.out.println(" - for JsonObject on simple JSON object");
    final JsonObject in = createCompoundObjectWithObject();
    final JsonObject check = createCompoundObjectReplaceObject();
    final JsonPointer ptr = Json.createPointer(OBJ_PATH);
    final JsonObject out = ptr.replace(in, OBJ_VALUE2);
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + OBJ_PATH
          + "\" REPLACE \"" + OBJ_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnSimpleArray(final TestResult result) {
    System.out.println(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = createObjectArray1();
    final JsonArray check = createSimpleObjectArrayReplaceObject();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, OBJ_VALUE);
    if (!assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + OBJ_VALUE + "\" failed on simple JSON array");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code JsonObject} on simple JSON array
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
    System.out.println(" - for JsonObject on simple JSON array of size 5");
    final JsonArray in = createSimpleObjectArray5();
    final JsonArray check = createSimpleObjectArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_2, OBJ_VALUE_4, OBJ_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new JsonObject[] { OBJ_VALUE_5, OBJ_VALUE_4, OBJ_VALUE_2, OBJ_VALUE_1 },
        "Pointer REPLACE operation",
        "Pointers \"/0\", \"/1\", \"/3\", \"/4\" REPLACE sequence failed on simple JSON array");
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test pointer REPLACE for non existing location in object.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
   * 4.3. replace</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testReplaceOfNonExistingLocationInObject(
      final TestResult result) {
    System.out.println(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { createEmptyObject(),
        createSimpleObject(), createCompoundObject() };
    final String[] paths = new String[] { STR_PATH, INT_PATH, BOOL_PATH,
        OBJ_PATH };
    final JsonValue[] values = new JsonValue[] { Json.createValue(STR_VALUE),
        Json.createValue(INT_VALUE), toJsonValue(BOOL_VALUE), OBJ_VALUE };
    // Go trough all objects
    for (int i = 0; i < objsIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        final JsonPointer ptr = Json.createPointer(paths[j]);
        try {
          final JsonObject out = ptr.replace(objsIn[i], values[i]);
          result.fail("Pointer REPLACE operation", "Pointer \"" + paths[j]
              + "\" REPLACE succeeded on non existing location");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
      }
    }
  }

  /**
   * Test pointer REPLACE for non existing location in array.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902:
   * 4.2. remove</a>} defines:<br>
   * The target location MUST exist for the operation to be successful.
   */
  private void testReplaceOfNonExistingLocationInArray(
      final TestResult result) {
    System.out.println(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray1(), createIntArray2(), createSimpleBoolArray5(),
        createObjectArray2()

    };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        STR_PATH + "/0" };
    final JsonValue[] values = new JsonValue[] { Json.createValue(STR_VALUE),
        Json.createValue(STR_VALUE), Json.createValue(INT_VALUE),
        toJsonValue(BOOL_VALUE), OBJ_VALUE };
    // Go trough all arrays
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        final JsonPointer ptr = Json.createPointer(paths[j]);
        try {
          final JsonArray out = ptr.replace(arraysIn[i], values[i]);
          result.fail("Pointer REPLACE operation", "Pointer \"" + paths[j]
              + "\" REPLACE succeeded on non existing location");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
      }
    }
  }

  /**
   * Test helper: Verify set of REPLACE operations on provided JSON array and
   * verify result using provided expected JSON value. JSON pointer instance is
   * used to modify the array.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          JSON array to be modified.
   * @param check
   *          Expected modified JSON array (used for operation check).
   * @param paths
   *          JSON array paths of values to be added. Pairs of {@code paths[i]}
   *          and {@code values[i]} are used for add operations.
   * @param values
   *          JSON array values to be added on specified indexes.
   * @param testName
   *          Name of this test.
   * @param errorMessage
   *          Error message to be added on verification failure.
   */
  private void verifyReplaceValues(final TestResult result, final JsonArray in,
      final JsonArray check, final String[] paths, final Object[] values,
      final String testName, final String errorMessage) {
    if (paths.length != values.length) {
      throw new IllegalArgumentException(
          "Number of paths does not match number of indexes");
    }
    JsonArray out = in;
    for (int i = 0; i < paths.length; i++) {
      final JsonPointer ptr = Json.createPointer(paths[i]);
      out = ptr.replace(out, toJsonValue(values[i]));
    }
    if (!assertEquals(check, out)) {
      result.fail(testName, errorMessage);
    }
  }

}

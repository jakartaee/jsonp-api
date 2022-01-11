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

package jsonp.tck.api.pointertests;

import jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: pointer
 * usage for {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}
 * replace operation tests.<br>
 */
public class PointerReplace {

  private static final Logger LOGGER = Logger.getLogger(PointerReplace.class.getName());

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
    LOGGER.info(
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
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceStr();
    final JsonPointer ptr = Json.createPointer(SimpleValues.STR_PATH);
    final JsonObject out = ptr.replace(in, Json.createValue(SimpleValues.STR_VALUE2));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + SimpleValues.STR_PATH
          + "\" REPLACE \"" + SimpleValues.STR_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createStringArray1();
    final JsonArray check = SimpleValues.createSimpleStringArrayReplaceStr();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, Json.createValue(SimpleValues.STR_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + SimpleValues.STR_VALUE + "\" failed on simple JSON array");
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
    LOGGER.info(" - for String on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleStringArray5();
    final JsonArray check = SimpleValues.createSimpleStringArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new String[] { SimpleValues.STR_VALUE_1, SimpleValues.STR_VALUE_2, SimpleValues.STR_VALUE_4, SimpleValues.STR_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new String[] { SimpleValues.STR_VALUE_5, SimpleValues.STR_VALUE_4, SimpleValues.STR_VALUE_2, SimpleValues.STR_VALUE_1 },
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
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceInt();
    final JsonPointer ptr = Json.createPointer(SimpleValues.INT_PATH);
    final JsonObject out = ptr.replace(in, Json.createValue(SimpleValues.INT_VALUE2));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + SimpleValues.INT_PATH
          + "\" REPLACE \"" + SimpleValues.INT_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createIntArray1();
    final JsonArray check = SimpleValues.createSimpleIntArrayReplaceInt();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, Json.createValue(SimpleValues.INT_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + SimpleValues.INT_VALUE + "\" failed on simple JSON array");
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
    LOGGER.info(" - for int on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleIntArray5();
    final JsonArray check = SimpleValues.createSimpleIntArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new Integer[] { SimpleValues.INT_VALUE_1, SimpleValues.INT_VALUE_2, SimpleValues.INT_VALUE_4, SimpleValues.INT_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new Integer[] { SimpleValues.INT_VALUE_5, SimpleValues.INT_VALUE_4, SimpleValues.INT_VALUE_2, SimpleValues.INT_VALUE_1 },
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
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createSimpleObjectReplaceBool();
    final JsonPointer ptr = Json.createPointer(SimpleValues.BOOL_PATH);
    final JsonObject out = ptr.replace(in, SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE2));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + SimpleValues.BOOL_PATH
          + "\" REPLACE \"" + SimpleValues.BOOL_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createBoolArray1();
    final JsonArray check = SimpleValues.createSimpleBoolArrayReplaceBool();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, SimpleValues.toJsonValue(SimpleValues.BOOL_FALSE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + SimpleValues.BOOL_FALSE + "\" failed on simple JSON array");
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
    LOGGER.info(" - for boolean on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleBoolArray5();
    final JsonArray check = SimpleValues.createSimpleBoolArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new Boolean[] { SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new Boolean[] { SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE },
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
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createCompoundObjectWithObject();
    final JsonObject check = SimpleValues.createCompoundObjectReplaceObject();
    final JsonPointer ptr = Json.createPointer(SimpleValues.OBJ_PATH);
    final JsonObject out = ptr.replace(in, SimpleValues.OBJ_VALUE2);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"" + SimpleValues.OBJ_PATH
          + "\" REPLACE \"" + SimpleValues.OBJ_VALUE2 + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer REPLACE operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testReplaceObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createObjectArray1();
    final JsonArray check = SimpleValues.createSimpleObjectArrayReplaceObject();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.replace(in, SimpleValues.OBJ_VALUE);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REPLACE operation", "Pointer \"/0\" REPLACE \""
          + SimpleValues.OBJ_VALUE + "\" failed on simple JSON array");
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
    LOGGER.info(" - for JsonObject on simple JSON array of size 5");
    final JsonArray in = SimpleValues.createSimpleObjectArray5();
    final JsonArray check = SimpleValues.createSimpleObjectArray5R();
    verifyReplaceValues(result, in, check,
        new String[] { "/4", "/3", "/1", "/0" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_1, SimpleValues.OBJ_VALUE_2, SimpleValues.OBJ_VALUE_4, SimpleValues.OBJ_VALUE_5 },
        "Pointer REPLACE operation",
        "Pointers \"/4\", \"/3\", \"/1\", \"/0\" REPLACE sequence failed on simple JSON array");
    verifyReplaceValues(result, in, check,
        new String[] { "/0", "/1", "/3", "/4" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_5, SimpleValues.OBJ_VALUE_4, SimpleValues.OBJ_VALUE_2, SimpleValues.OBJ_VALUE_1 },
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
    LOGGER.info(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { SimpleValues.createEmptyObject(),
        SimpleValues.createSimpleObject(), SimpleValues.createCompoundObject() };
    final String[] paths = new String[] { SimpleValues.STR_PATH, SimpleValues.INT_PATH, SimpleValues.BOOL_PATH,
        SimpleValues.OBJ_PATH };
    final JsonValue[] values = new JsonValue[] { Json.createValue(SimpleValues.STR_VALUE),
        Json.createValue(SimpleValues.INT_VALUE), SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE), SimpleValues.OBJ_VALUE };
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
    LOGGER.info(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { SimpleValues.createEmptyArray(),
        SimpleValues.createStringArray1(), SimpleValues.createIntArray2(), SimpleValues.createSimpleBoolArray5(),
        SimpleValues.createObjectArray2()

    };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        SimpleValues.STR_PATH + "/0" };
    final JsonValue[] values = new JsonValue[] { Json.createValue(SimpleValues.STR_VALUE),
        Json.createValue(SimpleValues.STR_VALUE), Json.createValue(SimpleValues.INT_VALUE),
        SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE), SimpleValues.OBJ_VALUE };
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
      out = ptr.replace(out, SimpleValues.toJsonValue(values[i]));
    }
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail(testName, errorMessage);
    }
  }

}

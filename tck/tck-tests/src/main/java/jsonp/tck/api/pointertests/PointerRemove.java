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
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: pointer
 * usage for {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}
 * remove operation tests.<br>
 */
public class PointerRemove {

  private static final Logger LOGGER = Logger.getLogger(PointerRemove.class.getName());

  /**
   * Creates an instance of RFC 6901 pointer instance usage for RFC 6902 remove
   * operation tests.
   */
  PointerRemove() {
    super();
  }

  /**
   * Test RFC 6901 pointer instance usage for RFC 6902 remove operation. Suite
   * entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 6901 pointer usage for RFC 6902 remove operation");
    LOGGER.info("Testing RFC 6901 pointer usage for RFC 6902 remove operation");
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
   * Test pointer REMOVE operation for {@code String} to produce empty JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for String to produce empty JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createEmptyObject();
    final JsonPointer ptr = Json.createPointer(SimpleValues.STR_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation", "Pointer \"" + SimpleValues.STR_PATH
          + "\" REMOVE failed when producing empty JSON object");
    }
  }

  /**
   * Test pointer REMOVE operation for {@code String} to produce empty JSON
   * array. Only allowed index for empty array is {@code 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for String to produce empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArrayWithStr();
    final JsonArray check = SimpleValues.createEmptyArray();
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed when producing empty JSON array");
    }
  }

  /**
   * Test pointer REMOVE operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testRemoveStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithStr();
    final JsonObject check = SimpleValues.createSimpleObject();
    final JsonPointer ptr = Json.createPointer(SimpleValues.STR_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"" + SimpleValues.STR_PATH + "\" REMOVE failed on simple JSON object");
    }
  }

  /**
   * Test pointer REMOVE operation for {@code String} on simple JSON array of
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
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer ptrAfter = Json.createPointer("/1");
    final JsonArray outBefore = ptrBefore.remove(inBefore);
    final JsonArray outAfter = ptrAfter.remove(inAfter);
    if (!JsonAssert.assertEquals(check, outBefore)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed on simple JSON array");
    }
    if (!JsonAssert.assertEquals(check, outAfter)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/1\" REMOVE failed on simple JSON array");
    }
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
    verifyRemoveValues(result, in, check, new String[] { "/4", "/2", "/0" },
        "Pointer REMOVE operation",
        "Pointers \"/4\", \"/2\", \"/0\" REMOVE sequence failed on simple JSON array");
    verifyRemoveValues(result, in, check, new String[] { "/0", "/1", "/2" },
        "Pointer REMOVE operation",
        "Pointers \"/0\", \"/1\", \"/2\" REMOVE sequence failed on simple JSON array");
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.INT_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation", "Pointer \"" + SimpleValues.INT_PATH
          + "\" REMOVE failed when producing empty JSON object");
    }
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
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed when producing empty JSON array");
    }
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.INT_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"" + SimpleValues.INT_PATH + "\" REMOVE failed on simple JSON object");
    }
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
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer ptrAfter = Json.createPointer("/1");
    final JsonArray outBefore = ptrBefore.remove(inBefore);
    final JsonArray outAfter = ptrAfter.remove(inAfter);
    if (!JsonAssert.assertEquals(check, outBefore)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed on simple JSON array");
    }
    if (!JsonAssert.assertEquals(check, outAfter)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/1\" REMOVE failed on simple JSON array");
    }
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
    verifyRemoveValues(result, in, check, new String[] { "/4", "/2", "/0" },
        "Pointer REMOVE operation",
        "Pointers \"/4\", \"/2\", \"/0\" REMOVE sequence failed on simple JSON array");
    verifyRemoveValues(result, in, check, new String[] { "/0", "/1", "/2" },
        "Pointer REMOVE operation",
        "Pointers \"/0\", \"/1\", \"/2\" REMOVE sequence failed on simple JSON array");
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.BOOL_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation", "Pointer \"" + SimpleValues.BOOL_PATH
          + "\" REMOVE failed when producing empty JSON object");
    }
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
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed when producing empty JSON array");
    }
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.BOOL_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"" + SimpleValues.BOOL_PATH + "\" REMOVE failed on simple JSON object");
    }
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
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer ptrAfter = Json.createPointer("/1");
    final JsonArray outBefore = ptrBefore.remove(inBefore);
    final JsonArray outAfter = ptrAfter.remove(inAfter);
    if (!JsonAssert.assertEquals(check, outBefore)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed on simple JSON array");
    }
    if (!JsonAssert.assertEquals(check, outAfter)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/1\" REMOVE failed on simple JSON array");
    }
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
    verifyRemoveValues(result, in, check, new String[] { "/4", "/2", "/0" },
        "Pointer REMOVE operation",
        "Pointers \"/4\", \"/2\", \"/0\" REMOVE sequence failed on simple JSON array");
    verifyRemoveValues(result, in, check, new String[] { "/0", "/1", "/2" },
        "Pointer REMOVE operation",
        "Pointers \"/0\", \"/1\", \"/2\" REMOVE sequence failed on simple JSON array");
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.OBJ_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation", "Pointer \"" + SimpleValues.OBJ_PATH
          + "\" REMOVE failed when producing empty JSON object");
    }
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
    final JsonPointer ptr = Json.createPointer("/0");
    final JsonArray out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed when producing empty JSON array");
    }
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.OBJ_PATH);
    final JsonObject out = ptr.remove(in);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"" + SimpleValues.OBJ_PATH + "\" REMOVE failed on simple JSON object");
    }
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
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer ptrAfter = Json.createPointer("/1");
    final JsonArray outBefore = ptrBefore.remove(inBefore);
    final JsonArray outAfter = ptrAfter.remove(inAfter);
    if (!JsonAssert.assertEquals(check, outBefore)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/0\" REMOVE failed on simple JSON array");
    }
    if (!JsonAssert.assertEquals(check, outAfter)) {
      result.fail("Pointer REMOVE operation",
          "Pointer \"/1\" REMOVE failed on simple JSON array");
    }
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
    verifyRemoveValues(result, in, check, new String[] { "/4", "/2", "/0" },
        "Pointer REMOVE operation",
        "Pointers \"/4\", \"/2\", \"/0\" REMOVE sequence failed on simple JSON array");
    verifyRemoveValues(result, in, check, new String[] { "/0", "/1", "/2" },
        "Pointer REMOVE operation",
        "Pointers \"/0\", \"/1\", \"/2\" REMOVE sequence failed on simple JSON array");
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
        final JsonPointer ptr = Json.createPointer(paths[j]);
        try {
          final JsonObject out = ptr.remove(objsIn[i]);
          result.fail("Pointer REMOVE operation", "Pointer \"" + paths[j]
              + "\" REMOVE succeeded on non existing location");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
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
        final JsonPointer ptr = Json.createPointer(paths[j]);
        try {
          final JsonArray out = ptr.remove(arraysIn[i]);
          result.fail("Pointer REMOVE operation", "Pointer \"" + paths[j]
              + "\" REMOVE succeeded on non existing location");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
      }
    }
  }

  /**
   * Test helper: Verify set of REMOVE operations on provided JSON array and
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
   * @param testName
   *          Name of this test.
   * @param errorMessage
   *          Error message to be added on verification failure.
   */
  private void verifyRemoveValues(final TestResult result, final JsonArray in,
      final JsonArray check, final String[] paths, final String testName,
      final String errorMessage) {
    JsonArray out = in;
    for (int i = 0; i < paths.length; i++) {
      final JsonPointer ptr = Json.createPointer(paths[i]);
      out = ptr.remove(out);
    }
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail(testName, errorMessage);
    }
  }

}

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
import jakarta.json.JsonStructure;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: pointer
 * usage for {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}
 * add operation tests.<br>
 */
public class PointerAdd {

  private static final Logger LOGGER = Logger.getLogger(PointerAdd.class.getName());

  /**
   * Creates an instance of RFC 6901 pointer instance usage for RFC 6902 add
   * operation tests.
   */
  PointerAdd() {
    super();
  }

  /**
   * Test RFC 6901 pointer instance usage for RFC 6902 add operation. Suite
   * entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 6901 pointer usage for RFC 6902 add operation");
    LOGGER.info("Testing RFC 6901 pointer usage for RFC 6902 add operation");
    testAddStringOnEmptyObject(result);
    testAddStringOnEmptyArray(result);
    testAddStringOnSimpleObject(result);
    testAddStringOnSimpleArray(result);
    testAddStringOnSimpleArray2(result);
    testAddIntOnEmptyObject(result);
    testAddIntOnEmptyArray(result);
    testAddIntOnSimpleObject(result);
    testAddIntOnSimpleArray(result);
    testAddIntOnSimpleArray2(result);
    testAddBoolOnEmptyObject(result);
    testAddBoolOnEmptyArray(result);
    testAddBoolOnSimpleObject(result);
    testAddBoolOnSimpleArray(result);
    testAddBoolOnSimpleArray2(result);
    testAddObjectOnEmptyObject(result);
    testAddObjectOnEmptyArray(result);
    testAddObjectOnSimpleObject(result);
    testAddObjectOnSimpleArray(result);
    testAddObjectOnSimpleArray2(result);
    testAddArrayToReplaceObject(result);
    testAddArrayToReplaceDocument(result);
    testAddStringArrayToStringArray(result);
    testAddStringToNonExistingObject(result);
    return result;
  }

  /**
   * Test pointer ADD operation for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for String on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectStr();
    final JsonPointer ptr = Json.createPointer(SimpleValues.STR_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(SimpleValues.STR_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.STR_PATH + "\" ADD \""
          + SimpleValues.STR_VALUE + "\" failed on empty JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code String} on empty JSON array. Only
   * allowed index for empty array is {@code 0} and {@code -}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for String on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithStr();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, Json.createValue(SimpleValues.STR_VALUE));
      if (!JsonAssert.assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + SimpleValues.STR_VALUE + "\" failed on empty JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithStr();
    final JsonPointer ptr = Json.createPointer(SimpleValues.STR_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(SimpleValues.STR_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.STR_PATH + "\" ADD \""
          + SimpleValues.STR_VALUE + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code String} on simple JSON array of size
   * 1. Using index {@code 0} to add {@code String} before already existing
   * element and indexes {@code 1} and {@code -} to add {@code String} after
   * already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createStringArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleStringArrayWithStrBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleStringArrayWithStrAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, Json.createValue(SimpleValues.STR_VALUE));
    if (!JsonAssert.assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + SimpleValues.STR_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, Json.createValue(SimpleValues.STR_VALUE));
      if (!JsonAssert.assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + SimpleValues.STR_VALUE
            + "\" failed on simple JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code String} on simple JSON array of size
   * 2. Starting with an array of size 2.
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
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new String[] { SimpleValues.STR_VALUE_5, SimpleValues.STR_VALUE_3, SimpleValues.STR_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new String[] { SimpleValues.STR_VALUE_1, SimpleValues.STR_VALUE_3, SimpleValues.STR_VALUE_5 },
        "Pointer ADD operation",
        "Pointers \"/0\", \"/2\", \"/4\" ADD sequence failed on simple JSON array");
  }

  /**
   * Test pointer ADD operation for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for int on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectInt();
    final JsonPointer ptr = Json.createPointer(SimpleValues.INT_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(SimpleValues.INT_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.INT_PATH + "\" ADD \""
          + SimpleValues.INT_VALUE + "\" failed on empty JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code int} on empty JSON array. Only
   * allowed index for empty array is {@code 0} and {@code -}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for int on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithInt();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, Json.createValue(SimpleValues.INT_VALUE));
      if (!JsonAssert.assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + SimpleValues.INT_VALUE + "\" failed on empty JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithInt();
    final JsonPointer ptr = Json.createPointer(SimpleValues.INT_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(SimpleValues.INT_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.INT_PATH + "\" ADD \""
          + SimpleValues.INT_VALUE + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code int} on simple JSON array of size 1.
   * Using index {@code 0} to add {@code int} before already existing element
   * and index {@code 1} and {@code -} to add {@code int} after already existing
   * element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createIntArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleIntArrayWithIntBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleIntArrayWithIntAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, Json.createValue(SimpleValues.INT_VALUE));
    if (!JsonAssert.assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + SimpleValues.INT_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, Json.createValue(SimpleValues.INT_VALUE));
      if (!JsonAssert.assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + SimpleValues.INT_VALUE
            + "\" failed on simple JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code int} on simple JSON array of size 2.
   * Starting with an array of size 2.
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
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new Integer[] { SimpleValues.INT_VALUE_5, SimpleValues.INT_VALUE_3, SimpleValues.INT_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new Integer[] { SimpleValues.INT_VALUE_1, SimpleValues.INT_VALUE_3, SimpleValues.INT_VALUE_5 },
        "Pointer ADD operation",
        "Pointers \"/0\", \"/2\", \"/4\" ADD sequence failed on simple JSON array");
  }

  /**
   * Test pointer ADD operation for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBoolOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for boolean on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectBool();
    final JsonPointer ptr = Json.createPointer(SimpleValues.BOOL_PATH);
    final JsonObject out = ptr.add(in, SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.BOOL_PATH
          + "\" ADD \"" + SimpleValues.BOOL_VALUE + "\" failed on empty JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code boolean} on empty JSON array. Only
   * allowed index for empty array is {@code 0} and {@code -}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBoolOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for boolean on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithBool();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE));
      if (!JsonAssert.assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + SimpleValues.BOOL_VALUE + "\" failed on empty JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject check = SimpleValues.createSimpleObjectWithBool();
    final JsonPointer ptr = Json.createPointer(SimpleValues.BOOL_PATH);
    final JsonObject out = ptr.add(in, SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE));
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.BOOL_PATH
          + "\" ADD \"" + SimpleValues.BOOL_VALUE + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code boolean} on simple JSON array of size
   * 1. Using index {@code 0} to add {@code boolean} before already existing
   * element and index {@code 1} and {@code -} to add {@code boolean} after
   * already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createBoolArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleBoolArrayWithBoolBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleBoolArrayWithBoolAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, SimpleValues.toJsonValue(SimpleValues.BOOL_FALSE));
    if (!JsonAssert.assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + SimpleValues.BOOL_FALSE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, SimpleValues.toJsonValue(SimpleValues.BOOL_FALSE));
      if (!JsonAssert.assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \""
            + SimpleValues.BOOL_FALSE + "\" failed on simple JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code boolean} on simple JSON array of size
   * 2. Starting with an array of size 2.
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
  private void testAddBoolOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 2");
    final JsonArray in = SimpleValues.createBoolArray2();
    final JsonArray check = SimpleValues.createSimpleBoolArray5();
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new Boolean[] { SimpleValues.BOOL_TRUE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_FALSE },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new Boolean[] { SimpleValues.BOOL_FALSE, SimpleValues.BOOL_TRUE, SimpleValues.BOOL_TRUE },
        "Pointer ADD operation",
        "Pointers \"/0\", \"/2\", \"/4\" ADD sequence failed on simple JSON array");
  }

  /**
   * Test pointer ADD operation for {@code JsonObject} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject check = SimpleValues.createSimpleObjectObject();
    final JsonPointer ptr = Json.createPointer(SimpleValues.OBJ_PATH);
    final JsonObject out = ptr.add(in, SimpleValues.OBJ_VALUE);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.OBJ_PATH + "\" ADD \""
          + SimpleValues.OBJ_VALUE + "\" failed on empty JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code JsonObject} on empty JSON array. Only
   * allowed index for empty array is {@code 0} and {@code -}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnEmptyArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on empty JSON array");
    final JsonArray in = SimpleValues.createEmptyArray();
    final JsonArray check = SimpleValues.createEmptyArrayWithObject();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, SimpleValues.OBJ_VALUE);
      if (!JsonAssert.assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + SimpleValues.OBJ_VALUE + "\" failed on empty JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonObject check = SimpleValues.createCompoundObjectWithObject();
    final JsonPointer ptr = Json.createPointer(SimpleValues.OBJ_PATH);
    final JsonObject out = ptr.add(in, SimpleValues.OBJ_VALUE);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.OBJ_PATH + "\" ADD \""
          + SimpleValues.OBJ_VALUE + "\" failed on simple JSON object");
    }
  }

  /**
   * Test pointer ADD operation for {@code JsonObject} on simple JSON array of
   * size 1. Using index {@code 0} to add {@code JsonObject} before already
   * existing element and index {@code 1} and {@code -} to add
   * {@code JsonObject} after already existing element.
   * 
   * @param result
   *          Tests result record.
   */
  private void testAddObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = SimpleValues.createObjectArray1();
    final JsonArray checkBefore = SimpleValues.createSimpleObjectArrayWithObjectBefore();
    final JsonArray checkAfter = SimpleValues.createSimpleObjectArrayWithObjectAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, SimpleValues.OBJ_VALUE);
    if (!JsonAssert.assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + SimpleValues.OBJ_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, SimpleValues.OBJ_VALUE);
      if (!JsonAssert.assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + SimpleValues.OBJ_VALUE
            + "\" failed on simple JSON array");
      }
    }
  }

  /**
   * Test pointer ADD operation for {@code JsonObject} on simple JSON array of
   * size 2. Starting with an array of size 2.
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
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_5, SimpleValues.OBJ_VALUE_3, SimpleValues.OBJ_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new JsonObject[] { SimpleValues.OBJ_VALUE_1, SimpleValues.OBJ_VALUE_3, SimpleValues.OBJ_VALUE_5 },
        "Pointer ADD operation",
        "Pointers \"/0\", \"/2\", \"/4\" ADD sequence failed on simple JSON array");
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.DEF_OBJ_PATH);
    final JsonArray replace = SimpleValues.createSimpleStringArray5();
    final JsonObject out = ptr.add(in, replace);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.DEF_OBJ_PATH
          + "\" ADD array to replace existing object failed on compound JSON object");
    }
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
    final JsonPointer ptr = Json.createPointer("");
    final JsonArray replace = SimpleValues.createSimpleStringArray5();
    // Instance being replaced is JsonObject, instance being added is JsonArray
    final JsonStructure out = ptr.add((JsonStructure) in, replace);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + SimpleValues.DEF_OBJ_PATH
          + "\" ADD array to replace existing object failed on compound JSON object");
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
    final JsonArray arrayToAdd = SimpleValues.createStringInnerArray2();
    final JsonPointer ptr = Json.createPointer("/1");
    final JsonArray out = ptr.add(in, arrayToAdd);
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail("Pointer ADD operation",
          "Pointer \"/1\" ADD array failed on JSON array");
    }
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
    final JsonPointer ptr = Json.createPointer(SimpleValues.DEF_OBJ_PATH + SimpleValues.STR_PATH);
    boolean exception = false;
    try {
      ptr.add(in, Json.createValue(SimpleValues.STR_VALUE));
    } catch (JsonException e) {
      exception = true;
      LOGGER.info("    - Expected exception: " + e.getMessage());
    }
    if (!exception) {
      result.fail("Pointer ADD operation",
          "ADD operation on non existing JsonObject \"" + SimpleValues.DEF_OBJ_PATH
              + "\" passed");
    }
  }

  /**
   * Test helper: Verify set of ADD operations on provided JSON array and verify
   * result using provided expected JSON value. JSON pointer instance is used to
   * modify the array.
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
  private void verifyAddValues(final TestResult result, final JsonArray in,
      final JsonArray check, final String[] paths, final Object[] values,
      final String testName, final String errorMessage) {
    if (paths.length != values.length) {
      throw new IllegalArgumentException(
          "Number of paths does not match number of indexes");
    }
    JsonArray out = in;
    for (int i = 0; i < paths.length; i++) {
      final JsonPointer ptr = Json.createPointer(paths[i]);
      out = ptr.add(out, SimpleValues.toJsonValue(values[i]));
    }
    if (!JsonAssert.assertEquals(check, out)) {
      result.fail(testName, errorMessage);
    }
  }

}

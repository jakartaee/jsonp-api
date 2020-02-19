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
import jakarta.json.JsonStructure;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: pointer
 * usage for {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}
 * add operation tests.<br>
 */
public class PointerAdd {

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
    System.out.println("Testing RFC 6901 pointer usage for RFC 6902 add operation");
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
    System.out.println(" - for String on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectStr();
    final JsonPointer ptr = Json.createPointer(STR_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(STR_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + STR_PATH + "\" ADD \""
          + STR_VALUE + "\" failed on empty JSON object");
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
    System.out.println(" - for String on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithStr();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, Json.createValue(STR_VALUE));
      if (!assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + STR_VALUE + "\" failed on empty JSON array");
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
    System.out.println(" - for String on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithStr();
    final JsonPointer ptr = Json.createPointer(STR_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(STR_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + STR_PATH + "\" ADD \""
          + STR_VALUE + "\" failed on simple JSON object");
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
    System.out.println(" - for String on simple JSON array of size 1");
    final JsonArray in = createStringArray1();
    final JsonArray checkBefore = createSimpleStringArrayWithStrBefore();
    final JsonArray checkAfter = createSimpleStringArrayWithStrAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, Json.createValue(STR_VALUE));
    if (!assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + STR_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, Json.createValue(STR_VALUE));
      if (!assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + STR_VALUE
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
    System.out.println(" - for String on simple JSON array of size 2");
    final JsonArray in = createStringArray2();
    final JsonArray check = createSimpleStringArray5();
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new String[] { STR_VALUE_5, STR_VALUE_3, STR_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new String[] { STR_VALUE_1, STR_VALUE_3, STR_VALUE_5 },
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
    System.out.println(" - for int on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectInt();
    final JsonPointer ptr = Json.createPointer(INT_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(INT_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + INT_PATH + "\" ADD \""
          + INT_VALUE + "\" failed on empty JSON object");
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
    System.out.println(" - for int on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithInt();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, Json.createValue(INT_VALUE));
      if (!assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + INT_VALUE + "\" failed on empty JSON array");
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
    System.out.println(" - for int on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithInt();
    final JsonPointer ptr = Json.createPointer(INT_PATH);
    final JsonObject out = ptr.add(in, Json.createValue(INT_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + INT_PATH + "\" ADD \""
          + INT_VALUE + "\" failed on simple JSON object");
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
    System.out.println(" - for int on simple JSON array of size 1");
    final JsonArray in = createIntArray1();
    final JsonArray checkBefore = createSimpleIntArrayWithIntBefore();
    final JsonArray checkAfter = createSimpleIntArrayWithIntAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, Json.createValue(INT_VALUE));
    if (!assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + INT_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, Json.createValue(INT_VALUE));
      if (!assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + INT_VALUE
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
    System.out.println(" - for int on simple JSON array of size 2");
    final JsonArray in = createIntArray2();
    final JsonArray check = createSimpleIntArray5();
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new Integer[] { INT_VALUE_5, INT_VALUE_3, INT_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new Integer[] { INT_VALUE_1, INT_VALUE_3, INT_VALUE_5 },
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
    System.out.println(" - for boolean on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectBool();
    final JsonPointer ptr = Json.createPointer(BOOL_PATH);
    final JsonObject out = ptr.add(in, toJsonValue(BOOL_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + BOOL_PATH
          + "\" ADD \"" + BOOL_VALUE + "\" failed on empty JSON object");
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
    System.out.println(" - for boolean on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithBool();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, toJsonValue(BOOL_VALUE));
      if (!assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + BOOL_VALUE + "\" failed on empty JSON array");
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
    System.out.println(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject check = createSimpleObjectWithBool();
    final JsonPointer ptr = Json.createPointer(BOOL_PATH);
    final JsonObject out = ptr.add(in, toJsonValue(BOOL_VALUE));
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + BOOL_PATH
          + "\" ADD \"" + BOOL_VALUE + "\" failed on simple JSON object");
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
    System.out.println(" - for boolean on simple JSON array of size 1");
    final JsonArray in = createBoolArray1();
    final JsonArray checkBefore = createSimpleBoolArrayWithBoolBefore();
    final JsonArray checkAfter = createSimpleBoolArrayWithBoolAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, toJsonValue(BOOL_FALSE));
    if (!assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + BOOL_FALSE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, toJsonValue(BOOL_FALSE));
      if (!assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \""
            + BOOL_FALSE + "\" failed on simple JSON array");
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
    System.out.println(" - for boolean on simple JSON array of size 2");
    final JsonArray in = createBoolArray2();
    final JsonArray check = createSimpleBoolArray5();
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new Boolean[] { BOOL_TRUE, BOOL_TRUE, BOOL_FALSE },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new Boolean[] { BOOL_FALSE, BOOL_TRUE, BOOL_TRUE },
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
    System.out.println(" - for JsonObject on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject check = createSimpleObjectObject();
    final JsonPointer ptr = Json.createPointer(OBJ_PATH);
    final JsonObject out = ptr.add(in, OBJ_VALUE);
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + OBJ_PATH + "\" ADD \""
          + OBJ_VALUE + "\" failed on empty JSON object");
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
    System.out.println(" - for JsonObject on empty JSON array");
    final JsonArray in = createEmptyArray();
    final JsonArray check = createEmptyArrayWithObject();
    final JsonPointer[] ptrs = new JsonPointer[] { Json.createPointer("/0"),
        Json.createPointer("/-") };
    for (final JsonPointer ptr : ptrs) {
      final JsonArray out = ptr.add(in, OBJ_VALUE);
      if (!assertEquals(check, out)) {
        result.fail("Pointer ADD operation", "Pointer \"" + ptr + "\" ADD \""
            + OBJ_VALUE + "\" failed on empty JSON array");
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
    System.out.println(" - for JsonObject on simple JSON object");
    final JsonObject in = createCompoundObject();
    final JsonObject check = createCompoundObjectWithObject();
    final JsonPointer ptr = Json.createPointer(OBJ_PATH);
    final JsonObject out = ptr.add(in, OBJ_VALUE);
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + OBJ_PATH + "\" ADD \""
          + OBJ_VALUE + "\" failed on simple JSON object");
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
    System.out.println(" - for JsonObject on simple JSON array of size 1");
    final JsonArray in = createObjectArray1();
    final JsonArray checkBefore = createSimpleObjectArrayWithObjectBefore();
    final JsonArray checkAfter = createSimpleObjectArrayWithObjectAfter();
    final JsonPointer ptrBefore = Json.createPointer("/0");
    final JsonPointer[] ptrsAfter = new JsonPointer[] {
        Json.createPointer("/1"), Json.createPointer("/-") };
    final JsonArray outBefore = ptrBefore.add(in, OBJ_VALUE);
    if (!assertEquals(checkBefore, outBefore)) {
      result.fail("Pointer ADD operation", "Pointer \"/0\" ADD \"" + OBJ_VALUE
          + "\" failed on simple JSON array");
    }
    for (final JsonPointer ptrAfter : ptrsAfter) {
      final JsonArray outAfter = ptrAfter.add(in, OBJ_VALUE);
      if (!assertEquals(checkAfter, outAfter)) {
        result.fail("Pointer ADD operation", "Pointer \"/1\" ADD \"" + OBJ_VALUE
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
    System.out.println(" - for JsonObject on simple JSON array of size 2");
    final JsonArray in = createObjectArray2();
    final JsonArray check = createSimpleObjectArray5();
    verifyAddValues(result, in, check, new String[] { "/2", "/1", "/0" },
        new JsonObject[] { OBJ_VALUE_5, OBJ_VALUE_3, OBJ_VALUE_1 },
        "Pointer ADD operation",
        "Pointers \"/2\", \"/1\", \"/0\" ADD sequence failed on simple JSON array");
    verifyAddValues(result, in, check, new String[] { "/0", "/2", "/4" },
        new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_3, OBJ_VALUE_5 },
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
    System.out.println(" - for JsonArray to replace JsonObject");
    final JsonObject in = createCompoundObject();
    final JsonObject check = createCompoundObjectWithObjectReplaced();
    final JsonPointer ptr = Json.createPointer(DEF_OBJ_PATH);
    final JsonArray replace = createSimpleStringArray5();
    final JsonObject out = ptr.add(in, replace);
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + DEF_OBJ_PATH
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
    System.out.println(" - for JsonArray to replace whole document");
    final JsonObject in = createCompoundObject();
    final JsonArray check = createSimpleStringArray5();
    final JsonPointer ptr = Json.createPointer("");
    final JsonArray replace = createSimpleStringArray5();
    // Instance being replaced is JsonObject, instance being added is JsonArray
    final JsonStructure out = ptr.add((JsonStructure) in, replace);
    if (!assertEquals(check, out)) {
      result.fail("Pointer ADD operation", "Pointer \"" + DEF_OBJ_PATH
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
    System.out.println(" - for String array to be added to existing String array");
    final JsonArray in = createStringArray2();
    final JsonArray check = createStringArray2WithStringArrayInTheMiddle();
    final JsonArray arrayToAdd = createStringInnerArray2();
    final JsonPointer ptr = Json.createPointer("/1");
    final JsonArray out = ptr.add(in, arrayToAdd);
    if (!assertEquals(check, out)) {
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
    System.out.println(" - for String to be added to non existing JsonObject");
    final JsonObject in = createSimpleObject();
    final JsonPointer ptr = Json.createPointer(DEF_OBJ_PATH + STR_PATH);
    boolean exception = false;
    try {
      ptr.add(in, Json.createValue(STR_VALUE));
    } catch (JsonException e) {
      exception = true;
      System.out.println("    - Expected exception: " + e.getMessage());
    }
    if (!exception) {
      result.fail("Pointer ADD operation",
          "ADD operation on non existing JsonObject \"" + DEF_OBJ_PATH
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
      out = ptr.add(out, toJsonValue(values[i]));
    }
    if (!assertEquals(check, out)) {
      result.fail(testName, errorMessage);
    }
  }

}

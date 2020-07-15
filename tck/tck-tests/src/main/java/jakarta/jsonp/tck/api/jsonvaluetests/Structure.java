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

package jakarta.jsonp.tck.api.jsonvaluetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.PointerRFCObject.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for
 * {@link JsonStructure}. RFC 6901 JSON Pointer is being passed to
 * {@code JsonValue getValue(String)} method so whole JSON Pointer resolving
 * sample is being used to test this method.
 */
public class Structure {
  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonStructure}.
   */
  Structure() {
    super();
  }

  /**
   * {@link JsonStructure} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonStructure API methods added in JSON-P 1.1.");
    System.out.println("JsonStructure API methods added in JSON-P 1.1.");
    testResolveWholeDocument(result);
    testResolveEmptyName(result);
    testResolveSimpleArray(result);
    testResolveSimpleArrayItems(result);
    testResolvePathWithEncodedSlash(result);
    testResolvePathWithSlash(result);
    testResolvePathWithPercent(result);
    testResolvePathWithCaret(result);
    testResolvePathWithVerticalBar(result);
    testResolvePathWithBackSlash(result);
    testResolvePathWithDoubleQuotes(result);
    testResolvePathWithSpace(result);
    testResolvePathWithTilde(result);
    testResolvePathWithEncodedTilde(result);
    testResolvePathWithEncodedTildeOne(result);
    testResolveValidNumericIndexInArray(result);
    testResolveMemberAfterLastInArray(result);
    testResolveNonNumericIndexInArray(result);
    return result;
  }

  /**
   * Test RFC 6901 JSON Pointer resolving for the whole document path using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveWholeDocument(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = value;
    verifyGetValue(result, check, value, RFC_KEY_WHOLE);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "": 0} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveEmptyName(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL2);
    verifyGetValue(result, check, value, RFC_PTR2);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "foo": ["bar", "baz"]} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveSimpleArray(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = RFC_VAL1;
    verifyGetValue(result, check, value, RFC_PTR1);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "foo": ["bar", "baz"]} array
   * elements using {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveSimpleArrayItems(final TestResult result) {
    final String[] itemPtrs = new String[] { RFC_PTR1_ITEM1, RFC_PTR1_ITEM2 };
    final String[] itemVals = new String[] { RFC_VAL1_ITEM1, RFC_VAL1_ITEM2 };
    final JsonObject value = createRFC6901Object();
    for (int i = 0; i < itemPtrs.length; i++) {
      final JsonValue check = Json.createValue(itemVals[i]);
      verifyGetValue(result, check, value, itemPtrs[i]);
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "a/b": 1} using
   * {@code JsonValue getValue(String)}. Character {@code '/'} is encoded as
   * {@code "~1"} string.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedSlash(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL3);
    verifyGetValue(result, check, value, RFC_PTR3_ENC);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "a/b": 1} using
   * {@code JsonValue getValue(String)}. Character {@code '/'} is not encoded as
   * {@code "~1"} string. This results in invalid {@code "/a/b"} path and
   * resolving such path must throw an exception.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithSlash(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    verifyGetValueFail(result, value, RFC_PTR3);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "c%d": 2} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithPercent(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL4);
    verifyGetValue(result, check, value, RFC_PTR4);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "e^f": 3} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithCaret(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL5);
    verifyGetValue(result, check, value, RFC_PTR5);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "g|h": 4} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithVerticalBar(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL6);
    verifyGetValue(result, check, value, RFC_PTR6);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "i\\j": 5} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithBackSlash(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL7);
    verifyGetValue(result, check, value, RFC_PTR7);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "k\"l": 6} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithDoubleQuotes(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL8);
    verifyGetValue(result, check, value, RFC_PTR8);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code " ": 7} using
   * {@code JsonValue getValue(String)}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithSpace(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL9);
    verifyGetValue(result, check, value, RFC_PTR9);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "m~n": 8} without encoding
   * using {@code JsonValue getValue(String)}. Passing this test is not
   * mandatory.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-3">RFC 6901: 3.
   * Syntax</a>} defines JSON pointer grammar as:<br>
   * {@code json-pointer    = *( "/" reference-token )}<br>
   * {@code reference-token = *( unescaped / escaped )}<br>
   * {@code unescaped       = %x00-2E / %x30-7D / %x7F-10FFFF}<br>
   * {@code escaped         = "~" ( "0" / "1" )}<br>
   * Characters {@code '/'} and {@code '~'} are excluded from {@code unescaped}.
   * But having {@code '~'} outside escape sequence may be acceptable.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithTilde(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR10 + "\" pointer (optional)");
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL10);
    boolean noError = true;
    try {
      final JsonValue out = value.getValue(RFC_PTR10);
      if (operationFailed(check, out)) {
        noError = false;
        System.out.println("    - Pointer \"" + RFC_KEY10
            + "\" did not return expected value");
      }
    } catch (JsonException e) {
      noError = false;
      System.out.println("    - Expected exception: " + e.getMessage());
    }
    if (noError) {
      System.out.println(
          "    - Pointer resolving accepts '~' outside escape sequence");
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "m~n": 8} using
   * {@code JsonValue getValue(String)}. Character {@code '~'} is encoded as
   * {@code "~0"} string.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedTilde(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL10);
    verifyGetValue(result, check, value, RFC_KEY10_ENC);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "o~1p": 9} using
   * {@code JsonValue getValue(String)}. String {@code "~1"} is encoded as
   * {@code "~01"} String. Proper encoded sequences transformation is described
   * in chapter:
   * {@code "the string '~01' correctly becomes '~1' after transformation"}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedTildeOne(final TestResult result) {
    final JsonStructure value = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL11);
    verifyGetValue(result, check, value, RFC_PTR11_ENC);
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for existing numeric indexes of an
   * array. {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC
   * 6901: 4. Evaluation</a>} chapter:<br>
   * If the currently referenced value is a JSON array, the reference token MUST
   * contain either:
   * <ul>
   * <li>characters comprised of digits (see ABNF below; note that leading zeros
   * are not allowed) that represent an unsigned base-10 integer value, making
   * the new referenced value the array element with the zero-based index
   * identified by the token</li>
   * </ul>
   */
  private void testResolveValidNumericIndexInArray(final TestResult result) {
    System.out.println(
        " - getValue(String) resolving of pointer containing existing numeric array index");
    final JsonArray[] arraysIn = new JsonArray[] { createSimpleStringArray5(),
        createSimpleIntArray5(), createSimpleBoolArray5(),
        createSimpleObjectArray5() };
    final JsonValue[] strings = new JsonValue[] { toJsonValue(STR_VALUE_1),
        toJsonValue(STR_VALUE_2), toJsonValue(STR_VALUE_3),
        toJsonValue(STR_VALUE_4), toJsonValue(STR_VALUE_5) };
    final JsonValue[] ints = new JsonValue[] { toJsonValue(INT_VALUE_1),
        toJsonValue(INT_VALUE_2), toJsonValue(INT_VALUE_3),
        toJsonValue(INT_VALUE_4), toJsonValue(INT_VALUE_5) };
    final JsonValue[] bools = new JsonValue[] { toJsonValue(BOOL_FALSE),
        toJsonValue(BOOL_TRUE), toJsonValue(BOOL_TRUE), toJsonValue(BOOL_FALSE),
        toJsonValue(BOOL_TRUE) };
    final JsonValue[] objs = new JsonValue[] { OBJ_VALUE_1, OBJ_VALUE_2,
        OBJ_VALUE_3, OBJ_VALUE_4, OBJ_VALUE_5 };
    final JsonValue[][] checks = new JsonValue[][] { strings, ints, bools,
        objs };
    // Go trough all array types
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all valid indexes in arrays
      for (int j = 0; j < 5; j++) {
        final String path = "/" + Integer.toString(j);
        try {
          final JsonValue out = arraysIn[i].getValue(path);
          if (operationFailed(checks[i][j], out)) {
            result.fail("getValue(String)", "Failed for \"" + path + "\" path");
          }
        } catch (JsonException e) {
          result.fail("getValue(String)", "Exception: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for character {@code '-'} marking the
   * end of an array.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC 6901: 4.
   * Evaluation</a>} chapter:<br>
   * If the currently referenced value is a JSON array, the reference token MUST
   * contain either:
   * <ul>
   * <li>exactly the single character "-", making the new referenced value the
   * (nonexistent) member after the last array element</li>
   * </ul>
   * Note that the use of the "-" character to index an array will always result
   * in such an error condition because by definition it refers to a nonexistent
   * array element. Thus, applications of JSON Pointer need to specify how that
   * character is to be handled, if it is to be useful.
   */
  private void testResolveMemberAfterLastInArray(final TestResult result) {
    System.out.println(" - getValue(String) resolving of array \"/-\" pointer");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray(), createSimpleIntArray5(), createBoolArray2(),
        createSimpleObjectArray5() };
    for (int i = 0; i < arraysIn.length; i++) {
      try {
        arraysIn[i].getValue("/-");
        result.fail("getValue(String)", "Call of getValue(String) on \"" + "/-"
            + "\" shall throw JsonException");
      } catch (JsonException e) {
        System.out.println("    - Expected exception: " + e.getMessage());
      }
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for invalid index containing non
   * numeric characters on array.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC 6901: 4.
   * Evaluation</a>} chapter:<br>
   * {@code array-index = %x30 / ( %x31-39 *(%x30-39) )} grammar rule prohibits
   * indexes with anything else than sequence of digits. Index {@code '-'} is
   * being checked in another tests. The only exception is path for whole
   * document ({@code ""}) which must return the whole array.
   */
  private void testResolveNonNumericIndexInArray(final TestResult result) {
    System.out.println(
        " - getValue(String) resolving of pointer containing non numeric array index");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray(), createSimpleIntArray5(), createBoolArray2(),
        createSimpleObjectArray5() };
    final String[] typeNames = new String[] { "empty", "String", "int",
        "boolean", "JsonObject" };
    final String wholeDocument = "";
    final String[] paths = new String[] { "/", "/1a", "/b4", "/name" };
    // Go trough all array types
    for (int i = 0; i < arraysIn.length; i++) {
      try {
        final JsonValue wholeOut = arraysIn[i].getValue(wholeDocument);
        if (operationFailed(wholeOut, arraysIn[i])) {
          result.fail("getValue(String)", "Failed for \"" + wholeDocument
              + "\" path on " + typeNames[i] + " array");
        }
      } catch (JsonException e) {
        result.fail("getValue(String)", "Failed for \"" + wholeDocument
            + "\" path on " + typeNames[i] + " array: " + e.getMessage());
      }
      for (int j = 0; j < paths.length; j++) {
        try {
          final JsonValue out = arraysIn[i].getValue(paths[j]);
          result.fail("getValue(String)", "Succeeded for \"" + paths[j]
              + "\" path on " + typeNames[i] + " array");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
      }
    }
  }

  /**
   * Test helper: Verify {@code JsonValue getValue(String)} for given JSON path.
   * 
   * @param result
   *          Tests result record.
   */
  private void verifyGetValue(final TestResult result, final JsonValue check,
      final JsonStructure value, final String path) {
    System.out.println(" - getValue(String) resolving of \"" + path + "\" pointer");
    try {
      final JsonValue out = value.getValue(path);
      if (operationFailed(check, out)) {
        result.fail("getValue(String)", "Failed for \"" + path + "\" path");
      }
    } catch (JsonException e) {
      result.fail("getValue(String)", "Exception: " + e.getMessage());
    }
  }

  /**
   * Test helper: Verify {@code JsonValue getValue(String)} for given JSON path.
   * 
   * @param result
   *          Tests result record.
   */
  private void verifyGetValueFail(final TestResult result,
      final JsonStructure value, final String path) {
    System.out.println(
        " - getValue(String) resolving of invalid \"" + path + "\" pointer");
    try {
      value.getValue(path);
      result.fail("getValue(String)", "Call of getValue(String) on \"" + path
          + "\" shall throw JsonException");
    } catch (JsonException e) {
      System.out.println("    - Expected exception: " + e.getMessage());
    }
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null || !assertEquals(check, out);
  }

}

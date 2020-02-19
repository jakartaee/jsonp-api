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
import static jakarta.jsonp.tck.api.common.PointerRFCObject.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: JavaScript
 * Object Notation (JSON) Pointer resolving tests.<br>
 */
public class PointerResolve {

  /**
   * Creates an instance of RFC 6901 JSON Pointer resolver tests.
   */
  PointerResolve() {
    super();
  }

  /**
   * Test RFC 6901 JSON Pointer resolver tests. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6901 pointer resolving");
    System.out.println("Testing RFC 6901 pointer resolving");
    testResolveWholeDocument(result);
    testResolveEmptyName(result);
    testResolveSimpleArray(result);
    testResolveSimpleArrayItems(result);
    testResolvePathWithSlash(result);
    testResolvePathWithEncodedSlash(result);
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
    testResolveNumericIndexWithLeadingZeroInArray(result);
    testResolvenonNumericIndexInArray(result);
    return result;
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for the whole document path.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveWholeDocument(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_KEY_WHOLE + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = in;
    final JsonPointer ptr = Json.createPointer(RFC_KEY_WHOLE);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_KEY_WHOLE + "\"",
            "GET operation failed for \"" + RFC_KEY_WHOLE + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_KEY_WHOLE + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "": 0}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveEmptyName(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR2 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL2);
    final JsonPointer ptr = Json.createPointer(RFC_PTR2);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR2 + "\"",
            "GET operation failed for \"" + RFC_PTR2 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR2 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "foo": ["bar", "baz"]}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveSimpleArray(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR1 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = RFC_VAL1;
    final JsonPointer ptr = Json.createPointer(RFC_PTR1);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR1 + "\"",
            "GET operation failed for \"" + RFC_PTR1 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR1 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "foo": ["bar", "baz"]} array
   * elements.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolveSimpleArrayItems(final TestResult result) {
    final String[] itemPtrs = new String[] { RFC_PTR1_ITEM1, RFC_PTR1_ITEM2 };
    final String[] itemVals = new String[] { RFC_VAL1_ITEM1, RFC_VAL1_ITEM2 };
    final JsonObject in = createRFC6901Object();
    for (int i = 0; i < itemPtrs.length; i++) {
      System.out.println(" - resolving of \"" + itemPtrs[i] + "\" pointer");
      final JsonValue check = Json.createValue(itemVals[i]);
      final JsonPointer ptr = Json.createPointer(itemPtrs[i]);
      try {
        final JsonValue out = ptr.getValue(in);
        if (!assertEquals(out, check)) {
          result.fail("GET \"" + itemPtrs[i] + "\"",
              "GET operation failed for \"" + itemPtrs[i] + "\" path");
        }
      } catch (JsonException e) {
        result.fail("GET \"" + itemPtrs[i] + "\"",
            "GET operation exception: " + e.getMessage());
      }
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "a/b": 1}. Character
   * {@code '/'} is encoded as {@code "~1"} string.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedSlash(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR3_ENC + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL3);
    final JsonPointer ptr = Json.createPointer(RFC_PTR3_ENC);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR3_ENC + "\"",
            "GET operation failed for \"" + RFC_PTR3_ENC + "\" path");
      }
    } catch (JsonException e) {
      System.out.println("    ! Exception: " + e.getMessage());
      result.fail("GET \"" + RFC_PTR3_ENC + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "a/b": 1}. Character
   * {@code '/'} is not encoded as {@code "~1"} string. This results in invalid
   * {@code "/a/b"} path and resolving such path must throw an exception.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithSlash(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR3 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonPointer ptr = Json.createPointer(RFC_PTR3);
    try {
      final JsonValue out = ptr.getValue(in);
      result.fail("GET \"" + RFC_PTR3 + "\"",
          "GET operation succeeded for \"" + RFC_PTR3 + "\" path");
    } catch (JsonException e) {
      System.out.println("    - Expected exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "c%d": 2}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithPercent(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR4 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL4);
    final JsonPointer ptr = Json.createPointer(RFC_PTR4);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR4 + "\"",
            "GET operation failed for \"" + RFC_PTR4 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR4 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "e^f": 3}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithCaret(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR5 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL5);
    final JsonPointer ptr = Json.createPointer(RFC_PTR5);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR5 + "\"",
            "GET operation failed for \"" + RFC_PTR5 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR5 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "g|h": 4}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithVerticalBar(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR6 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL6);
    final JsonPointer ptr = Json.createPointer(RFC_PTR6);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR6 + "\"",
            "GET operation failed for \"" + RFC_PTR6 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR6 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "i\\j": 5}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithBackSlash(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR7 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL7);
    final JsonPointer ptr = Json.createPointer(RFC_PTR7);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR7 + "\"",
            "GET operation failed for \"" + RFC_PTR7 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR7 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "k\"l": 6}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithDoubleQuotes(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR8 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL8);
    final JsonPointer ptr = Json.createPointer(RFC_PTR8);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR8 + "\"",
            "GET operation failed for \"" + RFC_PTR8 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR8 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code " ": 7}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithSpace(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR9 + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL9);
    final JsonPointer ptr = Json.createPointer(RFC_PTR9);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR9 + "\"",
            "GET operation failed for \"" + RFC_PTR9 + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR9 + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "m~n": 8} without encoding.
   * Passing this test is not mandatory.
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
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL10);
    final JsonPointer ptr = Json.createPointer(RFC_PTR10);
    boolean noError = true;
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
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
   * Test RFC 6901 JSON Pointer resolver for {@code "m~n": 8}. Character
   * {@code '~'} is encoded as {@code "~0"} string.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedTilde(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_KEY10_ENC + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL10);
    final JsonPointer ptr = Json.createPointer(RFC_KEY10_ENC);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_KEY10_ENC + "\"",
            "GET operation failed for \"" + RFC_KEY10_ENC + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_KEY10_ENC + "\"",
          "GET operation exception: " + e.getMessage());
    }
  }

  /**
   * Test RFC 6901 JSON Pointer resolver for {@code "o~1p": 9}. String
   * {@code "~1"} is encoded as {@code "~01"} String. Proper encoded sequences
   * transformation is described in
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC 6901: 4.
   * Evaluation</a>} chapter:
   * {@code "the string '~01' correctly becomes '~1' after transformation"}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testResolvePathWithEncodedTildeOne(final TestResult result) {
    System.out.println(" - resolving of \"" + RFC_PTR11_ENC + "\" pointer");
    final JsonObject in = createRFC6901Object();
    final JsonValue check = Json.createValue(RFC_VAL11);
    final JsonPointer ptr = Json.createPointer(RFC_PTR11_ENC);
    try {
      final JsonValue out = ptr.getValue(in);
      if (!assertEquals(out, check)) {
        result.fail("GET \"" + RFC_PTR11_ENC + "\"",
            "GET operation failed for \"" + RFC_PTR11_ENC + "\" path");
      }
    } catch (JsonException e) {
      result.fail("GET \"" + RFC_PTR11_ENC + "\"",
          "GET operation exception: " + e.getMessage());
    }
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
        " - resolving of pointer containing existing numeric array index");
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
        final JsonPointer ptr = Json.createPointer(path);
        final JsonValue out = ptr.getValue(arraysIn[i]);
        if (!assertEquals(out, checks[i][j])) {
          JsonValue.ValueType type = checks[i][j].getValueType();
          String typeName = type == JsonValue.ValueType.TRUE
              || type == JsonValue.ValueType.FALSE ? "boolean"
                  : type.toString().toLowerCase();
          result.fail("GET \"" + path + "\"", "GET operation failed for \""
              + path + "\" path on " + typeName + " array");
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
    System.out.println(" - resolving of array \"/-\" pointer");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray(), createSimpleIntArray5(), createBoolArray2(),
        createSimpleObjectArray5() };
    final String[] typeNames = new String[] { "empty", "String", "int",
        "boolean", "JsonObject" };
    // Go trough all array types
    for (int i = 0; i < arraysIn.length; i++) {
      final JsonPointer ptr = Json.createPointer("/-");
      try {
        final JsonValue out = ptr.getValue(arraysIn[i]);
        result.fail("GET \"/-\"", "GET operation succeeded for \"/-\" key");
      } catch (JsonException e) {
        System.out.println("    - Expected exception for \"/-\" path in "
            + typeNames[i] + " array: " + e.getMessage());
      }
    }
  }

  // TODO: Consider whether passing this test is mandatory or optional.
  /**
   * Test RFC 6901 JSON Pointer resolver for existing index with leading
   * {@code '0'} on array.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC 6901: 4.
   * Evaluation</a>} chapter:<br>
   * {@code array-index = %x30 / ( %x31-39 *(%x30-39) )} grammar rule prohibits
   * indexes with leading {@code '0'} except the case when index is exactly
   * {@code "0"}. Exact case for {@code "0"} is being checked in other tests.
   * This test checks illegal values with leading {@code '0'} followed by valid
   * index numbers.
   */
  private void testResolveNumericIndexWithLeadingZeroInArray(
      final TestResult result) {
    System.out.println(
        " - resolving of pointer containing numeric array index with leading '0' (optional)");
    final JsonArray[] arraysIn = new JsonArray[] { createSimpleStringArray5(),
        createSimpleIntArray5(), createSimpleBoolArray5(),
        createSimpleObjectArray5() };
    final String[] typeNames = new String[] { "String", "int", "boolean",
        "JsonObject" };
    // Go trough all array types
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all valid indexes in arrays
      for (int j = 0; j < 5; j++) {
        final String path = "/0" + Integer.toString(j);
        final JsonPointer ptr = Json.createPointer(path);
        try {
          final JsonValue out = ptr.getValue(arraysIn[i]);
          System.out.println("    ! GET operation succeeded for \"" + path
              + "\" path on " + typeNames[i] + " array");
          // result.fail("GET \""+path+"\"",
          // "GET operation succeeded for \""+path+"\" key on "+typeNames[i]+"
          // array");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
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
  private void testResolvenonNumericIndexInArray(final TestResult result) {
    System.out.println(" - resolving of pointer containing non numeric array index");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray(), createSimpleIntArray5(), createBoolArray2(),
        createSimpleObjectArray5() };
    final String[] typeNames = new String[] { "empty", "String", "int",
        "boolean", "JsonObject" };
    final String wholeDocument = "";
    final String[] paths = new String[] { "/", "/1a", "/b4", "/name" };
    // Go trough all array types
    for (int i = 0; i < arraysIn.length; i++) {
      final JsonPointer wholeDocPtr = Json.createPointer(wholeDocument);
      try {
        final JsonValue wholeOut = wholeDocPtr.getValue(arraysIn[i]);
        if (!assertEquals(wholeOut, arraysIn[i])) {
          result.fail("GET \"" + wholeDocument + "\"",
              "GET operation failed for \"" + wholeDocument + "\" path on "
                  + typeNames[i] + " array");
        }
      } catch (JsonException e) {
        result.fail("GET \"" + wholeDocument + "\"",
            "GET operation failed for \"" + wholeDocument + "\" path on "
                + typeNames[i] + " array: " + e.getMessage());
      }
      for (int j = 0; j < paths.length; j++) {
        final JsonPointer ptr = Json.createPointer(paths[j]);
        try {
          final JsonValue out = ptr.getValue(arraysIn[i]);
          result.fail("GET \"" + paths[j] + "\"",
              "GET operation succeeded for \"" + paths[j] + "\" path on "
                  + typeNames[i] + " array");
        } catch (JsonException e) {
          // There are too many combinations to log them.
        }
      }
    }
  }

}

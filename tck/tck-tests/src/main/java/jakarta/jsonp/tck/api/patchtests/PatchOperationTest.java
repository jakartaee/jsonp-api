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
 * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.6">RFC 6902:
 * 4.6. test</a>} tests.
 */
public class PatchOperationTest extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationTest.class.getName());

  /** Tested operation name. */
  private final String OPERATION = "TEST";

  /**
   * Creates an instance of RFC 6902 replace operation test.
   */
  PatchOperationTest() {
    super();
  }

  /**
   * Test RFC 6902 MOVE operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 test operation");
    LOGGER.info("Testing RFC 6902 test operation:");
    testOnEmptyObject(result);
    testOnEmptyArray(result);
    testOnSimpleObject(result);
    testOnSimpleStringArray(result);
    testOnSimpleIntArray(result);
    testOnSimpleBoolArray(result);
    testOnSimpleObjectArray(result);
    return result;
  }

  /**
   * Test RFC 6902 TEST operation on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnEmptyObject(final TestResult result) {
    LOGGER.info(" - on empty JSON object");
    final JsonObject in = createEmptyObject();
    final String[] paths = new String[] { STR_PATH, INT_PATH, BOOL_PATH,
        OBJ_PATH };
    final Object[] values = new Object[] { STR_VALUE, INT_VALUE, BOOL_VALUE,
        OBJ_VALUE };
    // Go trough all values.
    for (int i = 0; i < values.length; i++) {
      // Go trough all paths.
      for (int j = 0; j < paths.length; j++) {
        // Everything shall fail on empty object.
        simpleOperationFail(result, in, paths[j], values[i]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Test RFC 6902 TEST operation on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnEmptyArray(final TestResult result) {
    LOGGER.info(" - on empty JSON array");
    final JsonArray in = createEmptyArray();
    final String[] paths = new String[] { "/-1", "/0", "/1", "/2", "/3", "/4",
        "/5", "/-" };
    final Object[] values = new Object[] { STR_VALUE, INT_VALUE, BOOL_VALUE,
        OBJ_VALUE };
    // Go trough all values.
    for (int i = 0; i < values.length; i++) {
      // Go trough all paths.
      for (int j = 0; j < paths.length; j++) {
        // Everything shall fail on empty object.
        simpleOperationFail(result, in, paths[j], values[i]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Test RFC 6902 TEST on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnSimpleObject(final TestResult result) {
    LOGGER.info(" - on simple JSON object");
    final JsonObject[] in = new JsonObject[] { createSimpleObjectStr(),
        createSimpleObjectInt(), createSimpleObjectBool(),
        createSimpleObjectObject() };
    final String[] paths = new String[] { STR_PATH, INT_PATH, BOOL_PATH,
        OBJ_PATH };
    final Object[] values = new Object[] { STR_VALUE, INT_VALUE, BOOL_VALUE,
        OBJ_VALUE };
    // go trough all source objects.
    for (int o = 0; o < in.length; o++) {
      // Go trough all values.
      for (int i = 0; i < values.length; i++) {
        // Go trough all paths.
        for (int j = 0; j < paths.length; j++) {
          if (o == i && o == j) {
            // TEST must pass for matching JsonObject, path and value
            simpleOperation(result, in[o], null, paths[j], values[i]);
          } else {
            // TEST must fail for non matching JsonObject, path and value
            simpleOperationFail(result, in[o], paths[j], values[i]);
          }
        }
      }
      // Whole document should pass on itself.
      simpleOperation(result, in[o], null, "", in[o]);
    }
  }

  /**
   * Test RFC 6902 TEST on simple JSON array of {@code String}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnSimpleStringArray(final TestResult result) {
    LOGGER.info(" - on simple JSON String array of size 5");
    final JsonArray in = createSimpleStringArray5();
    final String[] indexes = new String[] { "/-1", "/-", STR_PATH };
    final String[] values = new String[] { STR_VALUE_1, STR_VALUE_2,
        STR_VALUE_3, STR_VALUE_4, STR_VALUE_5 };
    // Go trough all array indexes.
    for (int i = 0; i <= 5; i++) {
      final String path = arrayPtr("/", i);
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        if (i == v) {
          // TEST must pass for matching index and value.
          simpleOperation(result, in, null, path, values[v]);
        } else {
          // TEST must fail for non matching index and value.
          simpleOperationFail(result, in, path, values[v]);
        }
      }
    }
    // Go trough all invalid indexes
    for (int i = 0; i < indexes.length; i++) {
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        // TEST must fail for non matching index and value.
        simpleOperationFail(result, in, indexes[i], values[v]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Test RFC 6902 TEST on simple JSON array of {@code int}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnSimpleIntArray(final TestResult result) {
    LOGGER.info(" - on simple JSON int array of size 5");
    final JsonArray in = createSimpleIntArray5();
    final String[] indexes = new String[] { "/-1", "/-", INT_PATH };
    final int[] values = new int[] { INT_VALUE_1, INT_VALUE_2, INT_VALUE_3,
        INT_VALUE_4, INT_VALUE_5 };
    // Go trough all array indexes.
    for (int i = 0; i <= 5; i++) {
      final String path = arrayPtr("/", i);
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        if (i == v) {
          // TEST must pass for matching index and value.
          simpleOperation(result, in, null, path, values[v]);
        } else {
          // TEST must fail for non matching index and value.
          simpleOperationFail(result, in, path, values[v]);
        }
      }
    }
    // Go trough all invalid indexes
    for (int i = 0; i < indexes.length; i++) {
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        // TEST must fail for non matching index and value.
        simpleOperationFail(result, in, indexes[i], values[v]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Test RFC 6902 TEST on simple JSON array of {@code boolean}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnSimpleBoolArray(final TestResult result) {
    LOGGER.info(" - on simple JSON boolean array of size 2");
    final JsonArray in = createBoolArray2();
    final String[] indexes = new String[] { "/-1", "/-", BOOL_PATH };
    final boolean[] values = new boolean[] { BOOL_TRUE, BOOL_FALSE };
    // Go trough all array indexes.
    for (int i = 0; i <= 2; i++) {
      final String path = arrayPtr("/", i);
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        if (i == v) {
          // TEST must pass for matching index and value.
          simpleOperation(result, in, null, path, values[v]);
        } else {
          // TEST must fail for non matching index and value.
          simpleOperationFail(result, in, path, values[v]);
        }
      }
    }
    // Go trough all invalid indexes
    for (int i = 0; i < indexes.length; i++) {
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        // TEST must fail for non matching index and value.
        simpleOperationFail(result, in, indexes[i], values[v]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Test RFC 6902 TEST on simple JSON array of {@code JsonObject}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOnSimpleObjectArray(final TestResult result) {
    LOGGER.info(" - on simple JSON JsonObject array of size 5");
    final JsonArray in = createSimpleObjectArray5();
    final String[] indexes = new String[] { "/-1", "/-", OBJ_PATH };
    final JsonObject[] values = new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_2,
        OBJ_VALUE_3, OBJ_VALUE_4, OBJ_VALUE_5 };
    // Go trough all array indexes.
    for (int i = 0; i <= 5; i++) {
      final String path = arrayPtr("/", i);
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        if (i == v) {
          // TEST must pass for matching index and value.
          simpleOperation(result, in, null, path, values[v]);
        } else {
          // TEST must fail for non matching index and value.
          simpleOperationFail(result, in, path, values[v]);
        }
      }
    }
    // Go trough all invalid indexes
    for (int i = 0; i < indexes.length; i++) {
      // Go trough all values.
      for (int v = 0; v < values.length; v++) {
        // TEST must fail for non matching index and value.
        simpleOperationFail(result, in, indexes[i], values[v]);
      }
    }
    // Whole document should pass on itself.
    simpleOperation(result, in, null, "", in);
  }

  /**
   * Build JSON array pointer for given prefix and index.
   * 
   * @param prefix
   *          JSON array pointer prefix.
   * @param index
   *          JSON array pointer index.
   * @return JSON array pointer.
   */
  private static String arrayPtr(final String prefix, final int index) {
    final int prefixLen = prefix != null ? prefix.length() : 0;
    final String indexStr = Integer.toString(index);
    final StringBuilder sb = new StringBuilder(prefixLen + indexStr.length());
    if (prefixLen > 0) {
      sb.append(prefix);
    }
    sb.append(indexStr);
    return sb.toString();
  }

  /**
   * Tested operation name {@code "TEST"}.
   * 
   * @return Operation name to be used in logs.
   */
  @Override
  protected String operationName() {
    return OPERATION;
  }

  /**
   * Create and initialize patch builder to contain TEST operation to be
   * applied.
   * 
   * @param path
   *          JSON path of value to be tested.
   * @param value
   *          Value to be compared against value on specified path.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    return builderTest(Json.createPatchBuilder(), path, value);
  }

  /**
   * Update patch builder to contain next TEST operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          JSON path of value to be tested.
   * @param value
   *          Value to be compared against value on specified path.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    return builderTest(builder, path, value);
  }

  /**
   * Add TEST {@code value} at {@code path} operation to provided JSON patch
   * builder.
   * 
   * @param builder
   *          Target JSON patch builder.
   * @param path
   *          JSON path of value to be tested.
   * @param value
   *          Value to be compared against value on specified path.
   * @return JSON patch builder containing new TEST operation.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  private static JsonPatchBuilder builderTest(final JsonPatchBuilder builder,
      final String path, final Object value) {
    switch (JsonValueType.getType(value.getClass())) {
    case String:
      return builder.test(path, (String) value);
    case Integer:
      return builder.test(path, ((Integer) value).intValue());
    case Boolean:
      return builder.test(path, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.test(path, (JsonValue) value);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
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
  @Override
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null;
  }

}

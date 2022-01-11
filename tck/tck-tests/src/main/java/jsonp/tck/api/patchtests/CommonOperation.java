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

import jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests.
 */
public abstract class CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(CommonOperation.class.getName());

  /** Message content template for test failure log message: operation name. */
  private static final String TEST_FAIL_OP = " operation";

  /** Message content template for test failure log message: path. */
  private static final String TEST_FAIL_FOR = " for ";

  /** Message content template for test failure log message: failed. */
  private static final String TEST_FAIL_FAI = " failed";

  /**
   * Message content template for test failure log message: on target type
   * prefix.
   */
  private static final String TEST_FAIL_ON1 = " on JSON ";

  /**
   * Message content template for test failure log message: on target type
   * suffix.
   */
  private static final String TEST_FAIL_ON2 = " value";

  /**
   * Message content template for test failure log message: patching execution
   * method.
   */
  private static final String TEST_FAIL_MET = " using ";

  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests.
   */
  protected CommonOperation() {
    super();
  }

  /**
   * Tested operation name, e.g. {@code "ADD"}, {@code "REPLACE"},
   * {@code "MOVE"}. Child class callback.
   * 
   * @return Operation name to be used in logs.
   */
  protected abstract String operationName();

  /**
   * Create and initialize patch builder to contain patch operation to be
   * applied. Child class callback.
   * 
   * @param path
   *          JSON path of operation.
   * @param value
   *          JSON value used in patch operation.
   * @return Patch builder containing operation to be applied.
   */
  protected abstract JsonPatchBuilder createOperationBuilder(final String path,
      final Object value);

  /**
   * Update patch builder to contain next patch operation to be applied. Child
   * class callback.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          JSON path of operation.
   * @param value
   *          JSON value used in patch operation.
   * @return Patch builder containing operation to be applied.
   */
  protected abstract JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value);

  /**
   * Test helper: Verify simple operation on provided JSON value and verify
   * result using provided expected JSON value. Operation execution is done
   * using all known methods to build and apply JSON patch.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          JSON value to be modified.
   * @param check
   *          Expected modified JSON object (used for operation check).
   * @param path
   *          JSON path of operation.
   * @param value
   *          JSON value used in patch operation.
   */
  protected void simpleOperation(final TestResult result, final JsonValue in,
                                 final JsonValue check, final String path, final Object value) {
    final JsonPatchBuilder builder = createOperationBuilder(path, value);
    final JsonPatch patch = builder.build();
    JsonValue out;
    try {
      out = SimpleValues.patchApply(patch, in);
    } catch (JsonException e) {
      out = null;
      LOGGER.info(
          "   Exception for path \"" + path + "\" on " + JsonAssert.valueToString(in));
      LOGGER.info("     " + e.getMessage());
    }
    if (operationFailed(check, out)) {
      final String targetClassName = in.getValueType().name().toLowerCase();
      final String operation = JsonAssert.valueToString(patch.toJsonArray());
      LOGGER.info("     " + operation);
      result.fail(testName(path, targetClassName),
          testMessage(operation, path, JsonAssert.valueToString(in)));
    }
  }

  /**
   * Test helper: Verify set of operations on provided JSON value and verify
   * result using provided expected JSON value. Verification is done using all
   * known methods to build and apply JSON patch. This method allows custom
   * patching of JSON array. Used for operations without value operand, e.g.
   * REMOVE. Operation builder callback will receive {@code null} as value.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          JSON array to be modified.
   * @param check
   *          Expected modified JSON array (used for operation check).
   * @param paths
   *          JSON paths array of operations.
   */
  protected void complexOperation(final TestResult result, final JsonArray in,
      final JsonArray check, final String[] paths) {
    final Object[] values = new Object[paths.length];
    for (int i = 0; i < paths.length; i++) {
      values[i] = null;
    }
    complexOperation(result, in, check, paths, values);
  }

  /**
   * Test helper: Verify set of operations on provided JSON value and verify
   * result using provided expected JSON value. Verification is done using all
   * known methods to build and apply JSON patch. This method allows custom
   * patching of JSON array.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          JSON array to be modified.
   * @param check
   *          Expected modified JSON array (used for operation check).
   * @param paths
   *          JSON paths array of operations. Pairs of {@code paths[i]} and
   *          {@code values[i]} are used for individual operations.
   * @param values
   *          JSON values array used in patch operations.
   */
  protected void complexOperation(final TestResult result, final JsonArray in,
      final JsonArray check, final String[] paths, final Object[] values) {
    if (paths.length != values.length) {
      throw new IllegalArgumentException(
          "Number of paths does not match number of indexes");
    }
    final JsonPatchBuilder builder = prepareComplexBuilder(paths, values);
    final JsonPatch patch = builder.build();
    final JsonValue out = SimpleValues.patchApply(patch, in);
    if (operationFailed(check, out)) {
      final String operations = JsonAssert.valueToString(patch.toJsonArray());
      final String targetClassName = in.getValueType().name().toLowerCase();
      LOGGER.info("     " + operations);
      result.fail(testName(paths, targetClassName),
          testMessage(operations, paths, JsonAssert.valueToString(in)));
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
    return out == null || !JsonAssert.assertEquals(check, out);
  }

  /**
   * Builds JSON patch builder with set of operations stored in {@code paths}
   * and {@code values}.
   * 
   * @param paths
   *          JSON paths array of operations. Pairs of {@code paths[i]} and
   *          {@code values[i]} are used for individual operations.
   * @param values
   *          JSON values array used in patch operations.
   */
  private JsonPatchBuilder prepareComplexBuilder(final String[] paths,
      final Object[] values) {
    JsonPatchBuilder builder = Json.createPatchBuilder();
    for (int i = 0; i < paths.length; i++) {
      builder = updateOperationBuilder(builder, paths[i], values[i]);
    }
    return builder;
  }

  /**
   * Test helper: Verify that operation on provided JSON value fails. Operation
   * execution is done using all known methods to build and apply JSON patch.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          JSON value to be modified.
   * @param path
   *          JSON path of operation.
   * @param value
   *          JSON value used in patch operation.
   */
  protected void simpleOperationFail(final TestResult result,
      final JsonValue in, final String path, final Object value) {
    try {
      final JsonPatch patch = createOperationBuilder(path, value).build();
      SimpleValues.patchApply(patch, in);
      final String targetClassName = in.getValueType().name().toLowerCase();
      final String operation = JsonAssert.valueToString(patch.toJsonArray());
      LOGGER.info(
          "   Failed for path \"" + path + "\" on " + JsonAssert.valueToString(in));
      LOGGER.info("     " + operation);
      result.fail(testName(path, targetClassName),
          testMessage(operation, path, JsonAssert.valueToString(in)));
    } catch (JsonException e) {
      // There are too many combinations to log them.
      // LOGGER.info(" - Expected exception: "+e.getMessage());
    }
  }

  /**
   * Get source class name.
   * 
   * @param value
   *          JSON value to search for class name.
   * @return Class name of provided JSON value or {@code null} when this value
   *         has been {@code null}.
   */
  protected String getSrcName(final Object value) {
    return value != null ? value.getClass().getSimpleName() : null;
  }

  /**
   * Get source classes names.
   * 
   * @param values
   *          JSON values to search for class name.
   * @return Class name of provided JSON value or {@code null} when this value
   *         has been {@code null}.
   */
  protected String[] getSrcNames(final Object[] values) {
    if (values == null) {
      return null;
    }
    final String[] names = new String[values.length];
    for (int i = 0; i < values.length; i++) {
      names[i] = values[i] != null ? values[i].getClass().getSimpleName()
          : null;
    }
    return names;
  }

  /**
   * Build test name for test failure log message.
   * 
   * @param path
   *          JSON patch operation source path.
   * @param targetType
   *          Name of target (JSON value being modified) value type.
   * @return Test name for test failure log message.
   */
  protected String testName(final String path, final String targetType) {
    final String operationName = operationName();
    final int pathLen = path != null ? path.length() + 1 : 0;
    final StringBuilder sb = new StringBuilder(
        operationName.length() + pathLen + targetType.length() + 1);
    sb.append(operationName);
    if (pathLen > 0) {
      sb.append(' ');
      sb.append(path);
    }
    sb.append(' ');
    sb.append(targetType);
    return sb.toString();
  }

  /**
   * Build test name for test failure log message.
   * 
   * @param paths
   *          JSON patch operation source paths.
   * @param targetType
   *          Name of target (JSON value being modified) value type.
   * @return Test name for test failure log message.
   */
  protected String testName(final String[] paths, final String targetType) {
    final String operationName = operationName();
    final int pathsLen = paths != null ? paths.length : 0;
    int pathsSize = 0;
    for (int i = 0; i < pathsLen; i++) {
      pathsSize += paths[i] != null ? paths[i].length() : SimpleValues.NULL.length();
      if (i > 0) {
        pathsSize += 1;
      }
    }
    if (pathsLen > 1) {
      pathsSize += 2;
    }
    final StringBuilder sb = new StringBuilder(
        operationName.length() + pathsSize + targetType.length() + 2);
    sb.append(operationName);
    sb.append(' ');
    if (pathsLen > 1) {
      sb.append('[');
    }
    for (int i = 0; i < pathsLen; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(paths[i] != null ? paths[i] : SimpleValues.NULL);
    }
    if (pathsLen > 1) {
      sb.append(']');
    }
    sb.append(' ');
    sb.append(targetType);
    return sb.toString();
  }

  /**
   * Build message content for test failure log message.
   * 
   * @param operation
   *          JSON patch operation being executed.
   * @param path
   *          JSON patch operation source path.
   * @param value
   *          Target value being modified.
   * @return Log message content.
   */
  protected String testMessage(final String operation, final String path,
      final String value) {
    final int tarLen = value != null
        ? TEST_FAIL_ON1.length() + TEST_FAIL_ON2.length() + value.length()
        : 0;
    final StringBuilder sb = new StringBuilder(
        operation.length() + TEST_FAIL_OP.length() + TEST_FAIL_FOR.length()
            + path.length() + TEST_FAIL_FAI.length() + tarLen);
    sb.append(operation);
    sb.append(TEST_FAIL_OP);
    sb.append(TEST_FAIL_FOR);
    sb.append(path);
    sb.append(TEST_FAIL_FAI);
    if (tarLen > 0) {
      sb.append(TEST_FAIL_ON1);
      sb.append(value);
      sb.append(TEST_FAIL_ON2);
    }
    return sb.toString();
  }

  /**
   * Build message content for test failure log message.
   * 
   * @param operation
   *          JSON patch operation being executed.
   * @param paths
   *          JSON patch operation source paths.
   * @param value
   *          Target value being modified.
   * @return Log message content.
   */
  protected String testMessage(final String operation, final String[] paths,
      final String value) {
    final int tarLen = value != null
        ? TEST_FAIL_ON1.length() + TEST_FAIL_ON2.length() + value.length()
        : 0;
    final int pathsLen = paths != null ? paths.length : 0;
    int pathsSize = 0;
    for (int i = 0; i < pathsLen; i++) {
      pathsSize += paths[i] != null ? paths[i].length() : SimpleValues.NULL.length();
      if (i > 0) {
        pathsSize += 1;
      }
    }
    if (pathsLen > 1) {
      pathsSize += 2;
    }
    final StringBuilder sb = new StringBuilder(
        operation.length() + TEST_FAIL_OP.length() + TEST_FAIL_FOR.length()
            + pathsSize + TEST_FAIL_FAI.length() + tarLen);
    sb.append(operation);
    sb.append(TEST_FAIL_OP);
    sb.append(TEST_FAIL_FOR);
    if (pathsLen > 1) {
      sb.append('[');
    }
    for (int i = 0; i < pathsLen; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(paths[i] != null ? paths[i] : SimpleValues.NULL);
    }
    if (pathsLen > 1) {
      sb.append(']');
    }
    sb.append(TEST_FAIL_FAI);
    if (tarLen > 0) {
      sb.append(TEST_FAIL_ON1);
      sb.append(value);
      sb.append(TEST_FAIL_ON2);
    }
    return sb.toString();
  }

  /**
   * Build message content for test failure log message.
   * 
   * @param paths
   *          JSON patch operation source path.
   * @param targetType
   *          Name of target (JSON value being modified) value type.
   * @return Log message content.
   */
  protected String testMessage(final String[] paths, final String targetType) {
    final String operationName = operationName();
    final int tarLen = targetType != null
        ? TEST_FAIL_ON1.length() + TEST_FAIL_ON2.length() + targetType.length()
        : 0;
    int pathsLen = 0;
    for (int i = 0; i < paths.length; i++) {
      pathsLen += paths[i] != null ? paths[i].length() : SimpleValues.NULL.length();
      if (i > 0) {
        pathsLen += 1;
      }
    }
    if (paths.length > 1) {
      pathsLen += 2;
    }
    final StringBuilder sb = new StringBuilder(
        operationName.length() + TEST_FAIL_OP.length() + TEST_FAIL_FOR.length()
            + pathsLen + TEST_FAIL_FAI.length() + tarLen);
    sb.append(operationName);
    sb.append(TEST_FAIL_OP);
    sb.append(TEST_FAIL_FOR);
    if (paths.length > 1) {
      sb.append('[');
    }
    for (int i = 0; i < paths.length; i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(paths[i] != null ? paths[i] : SimpleValues.NULL);
    }
    if (paths.length > 1) {
      sb.append(']');
    }
    sb.append(TEST_FAIL_FAI);
    if (tarLen > 0) {
      sb.append(TEST_FAIL_ON1);
      sb.append(targetType);
      sb.append(TEST_FAIL_ON2);
    }
    return sb.toString();
  }

}

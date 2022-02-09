/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.jsonp.api.mergetests;

import ee.jakarta.tck.jsonp.api.common.JsonAssert;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonValue;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests.<br>
 */
public abstract class MergeCommon {

  /** Message content: "MERGE" operation. */
  private static final String MERGE_STR = "MERGE";

  /** Message content: "DIFF" operation. */
  private static final String DIFF_STR = "DIFF";

  /** Message content template for test failure log message: patch. */
  private static final String TEST_FAIL_PATCH = "Patch ";

  /** Message content template for test failure log message: failed on. */
  private static final String TEST_FAIL_ON = " failed on ";

  /** Message content template for test failure log message: value. */
  private static final String TEST_FAIL_VAL = " value";

  /** Message content template for test failure log message: patch. */
  private static final String TEST_FAIL_FROM = "Diff from ";

  /** Message content template for test failure log message: failed on. */
  private static final String TEST_FAIL_TO = " to ";

  /** Message content template for test failure log message: value. */
  private static final String TEST_FAIL_FAIL = " failed";

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @param message
   *          Assert message.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check, final JsonValue out,
      final String message) {
    return out == null || !JsonAssert.assertEquals(check, out, message);
  }

  /**
   * Test helper: Verify merge of JSON patch on provided JSON value and verify
   * result using provided expected JSON value.
   * 
   * @param result
   *          Test suite result.
   * @param in
   *          Source JSON value to be modified.
   * @param patch
   *          JSON patch to be done on source value.
   * @param check
   *          Expected modified JSON object (used for operation check).
   */
  protected void simpleMerge(final TestResult result, final JsonValue in,
                             final JsonValue patch, final JsonValue check) {
    final JsonValue out = Json.createMergePatch(patch).apply(in);
    if (operationFailed(check, out, MERGE_STR + " mismatch")) {
      final String targetClassName = in.getValueType().name().toLowerCase();
      result.fail(testName(MERGE_STR, targetClassName),
          testMergeMessage(JsonAssert.valueToString(in), JsonAssert.valueToString(patch)));
    }
  }

  /**
   * Test helper: Verify diff on provided JSON values and verify result using
   * provided expected JSON value.
   * 
   * @param result
   *          Test suite result.
   * @param src
   *          Source JSON value for diff.
   * @param target
   *          Target JSON value for diff.
   * @param diff
   *          Expected diff JSON object (used for operation check).
   */
  protected void simpleDiff(final TestResult result, final JsonValue src,
      final JsonValue target, final JsonValue diff) {
    final JsonValue out = Json.createMergeDiff(src, target).toJsonValue();
    if (operationFailed(diff, out, DIFF_STR + " mismatch")) {
      final String srcClassName = src.getValueType().name().toLowerCase();
      final String targetClassName = target.getValueType().name().toLowerCase();
      result.fail(testName(DIFF_STR, srcClassName, targetClassName),
          testDiffMessage(JsonAssert.valueToString(src), JsonAssert.valueToString(target)));
    }
  }

  /**
   * Build test name for test failure log message.
   * 
   * @param operation
   *          Name of operation.
   * @param targetType
   *          Name of the target (JSON value being modified) value type.
   * @return Test name for test failure log message.
   */
  protected String testName(final String operation, final String targetType) {
    final StringBuilder sb = new StringBuilder(
        operation.length() + targetType.length() + 1);
    sb.append(operation);
    sb.append(' ');
    sb.append(targetType);
    return sb.toString();
  }

  /**
   * Build test name for test failure log message.
   * 
   * @param operation
   *          Name of operation.
   * @param srcType
   *          Name of the source (JSON value being used for modification) value
   *          type.
   * @param targetType
   *          Name of the target (JSON value being modified) value type.
   * @return Test name for test failure log message.
   */
  protected String testName(final String operation, final String srcType,
      final String targetType) {
    final StringBuilder sb = new StringBuilder(
        operation.length() + srcType.length() + targetType.length() + 2);
    sb.append(operation);
    sb.append(' ');
    sb.append(srcType);
    sb.append(',');
    sb.append(targetType);
    return sb.toString();
  }

  /**
   * Build message content for test failure log message.
   * 
   * @param in
   *          Source JSON value to be modified.
   * @param patch
   *          JSON patch to be done on source value.
   * @return Log message content.
   */
  protected String testMergeMessage(final String in, final String patch) {
    final StringBuilder sb = new StringBuilder(
        TEST_FAIL_PATCH.length() + TEST_FAIL_ON.length()
            + TEST_FAIL_VAL.length() + patch.length() + in.length());
    sb.append(TEST_FAIL_PATCH);
    sb.append(patch);
    sb.append(TEST_FAIL_ON);
    sb.append(in);
    sb.append(TEST_FAIL_VAL);
    return sb.toString();
  }

  /**
   * Build message content for test failure log message.
   * 
   * @param src
   *          Source JSON value for diff.
   * @param target
   *          Target JSON value for diff.
   * @return Log message content.
   */
  protected String testDiffMessage(final String src, final String target) {
    final StringBuilder sb = new StringBuilder(
        TEST_FAIL_FROM.length() + TEST_FAIL_TO.length()
            + TEST_FAIL_FAIL.length() + src.length() + target.length());
    sb.append(TEST_FAIL_FROM);
    sb.append(src);
    sb.append(TEST_FAIL_TO);
    sb.append(target);
    sb.append(TEST_FAIL_FAIL);
    return sb.toString();
  }

}

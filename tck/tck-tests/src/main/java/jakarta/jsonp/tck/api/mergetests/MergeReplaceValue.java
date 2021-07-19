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

package jakarta.jsonp.tck.api.mergetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.JsonObject;

import java.util.logging.Logger;

import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * RFC 7396: JavaScript Object Notation (JSON) Merge Patch compatibility
 * tests.<br>
 * Checks scenario described in
 * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
 * Introduction</a>}: If the target does contain the member, the value is
 * replaced.
 */
public class MergeReplaceValue extends MergeCommon {

  private static final Logger LOGGER = Logger.getLogger(MergeReplaceValue.class.getName());

  /**
   * Creates an instance of RFC 7396 value replacing test.
   */
  MergeReplaceValue() {
    super();
  }

  /**
   * Test RFC 7396: Adding non existing values. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 7396: Replace existing values");
    LOGGER.info("Testing RFC 7396: Replace existing values");
    testStringOnsimpleObject(result);
    testIntOnsimpleObject(result);
    testBoolOnsimpleObject(result);
    testObjectOnsimpleObject(result);
    return result;
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnsimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectMoveStr();
    final JsonObject check = createSimpleObjectMoveStr();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnsimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectMoveInt();
    final JsonObject check = createSimpleObjectMoveInt();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnsimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectMoveBool();
    final JsonObject check = createSimpleObjectMoveBool();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonObject} on compound JSON
   * object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testObjectOnsimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectMoveObject();
    final JsonObject check = createSimpleObjectMoveObject();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

}

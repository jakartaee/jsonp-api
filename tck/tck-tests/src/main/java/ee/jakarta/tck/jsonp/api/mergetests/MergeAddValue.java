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

package ee.jakarta.tck.jsonp.api.mergetests;

import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.JsonObject;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 7396: JavaScript Object Notation (JSON) Merge Patch compatibility
 * tests.<br>
 * Checks scenario described in
 * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
 * Introduction</a>}: If the provided merge patch contains members that do not
 * appear within the target, those members are added.
 */
public class MergeAddValue extends MergeCommon {

  private static final Logger LOGGER = Logger.getLogger(MergeAddValue.class.getName());
  
  /**
   * Creates an instance of RFC 7396 value adding test.
   */
  MergeAddValue() {
    super();
  }

  /**
   * Test RFC 7396: Adding non existing values. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 7396: Add non existing values");
    LOGGER.info("Testing RFC 7396: Add non existing values");
    testStringOnEmptyObject(result);
    testStringOnsimpleObject(result);
    testIntOnEmptyObject(result);
    testIntOnsimpleObject(result);
    testBoolOnEmptyObject(result);
    testBoolOnsimpleObject(result);
    testObjectOnEmptyObject(result);
    testObjectOnsimpleObject(result);
    return result;
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for String on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject patch = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createSimpleObjectStr();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnsimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject patch = SimpleValues.createSimpleObjectStr();
    final JsonObject check = SimpleValues.createSimpleObjectWithStr();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for int on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject patch = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createSimpleObjectInt();
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
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject patch = SimpleValues.createSimpleObjectInt();
    final JsonObject check = SimpleValues.createSimpleObjectWithInt();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for boolean on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject patch = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createSimpleObjectBool();
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
    final JsonObject in = SimpleValues.createSimpleObject();
    final JsonObject patch = SimpleValues.createSimpleObjectBool();
    final JsonObject check = SimpleValues.createSimpleObjectWithBool();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonObject} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testObjectOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonObject patch = SimpleValues.createSimpleObjectObject();
    final JsonObject check = SimpleValues.createSimpleObjectObject();
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
    LOGGER.info(" - for JsonObject on compound JSON object");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonObject patch = SimpleValues.createSimpleObjectObject();
    final JsonObject check = SimpleValues.createCompoundObjectWithObject();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

}

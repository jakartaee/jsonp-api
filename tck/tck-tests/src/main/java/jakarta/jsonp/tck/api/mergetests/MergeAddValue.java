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

package jakarta.jsonp.tck.api.mergetests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.JsonObject;

import static jakarta.jsonp.tck.api.common.SimpleValues.*;

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
    System.out.println("Testing RFC 7396: Add non existing values");
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
    System.out.println(" - for String on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject patch = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectStr();
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
    System.out.println(" - for String on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectWithStr();
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
    System.out.println(" - for int on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject patch = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectInt();
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
    System.out.println(" - for int on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectWithInt();
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
    System.out.println(" - for boolean on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject patch = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectBool();
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
    System.out.println(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObject();
    final JsonObject patch = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectWithBool();
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
    System.out.println(" - for JsonObject on empty JSON object");
    final JsonObject in = createEmptyObject();
    final JsonObject patch = createSimpleObjectObject();
    final JsonObject check = createSimpleObjectObject();
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
    System.out.println(" - for JsonObject on compound JSON object");
    final JsonObject in = createCompoundObject();
    final JsonObject patch = createSimpleObjectObject();
    final JsonObject check = createCompoundObjectWithObject();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

}

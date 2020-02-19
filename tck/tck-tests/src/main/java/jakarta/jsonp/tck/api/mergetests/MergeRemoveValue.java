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
 * Introduction</a>}: {@code null} values in the merge patch are given special
 * meaning to indicate the removal of existing values in the target.
 */
public class MergeRemoveValue extends MergeCommon {

  /**
   * Creates an instance of RFC 7396 value removal test.
   */
  MergeRemoveValue() {
    super();
  }

  /**
   * Test RFC 7396: Removing existing values. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "RFC 7396: Remove existing values");
    System.out.println("Testing RFC 7396: Remove existing values");
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
    System.out.println(" - for String to produce empty JSON object");
    final JsonObject in = createSimpleObjectStr();
    final JsonObject patch = createPatchRemoveStr();
    final JsonObject check = createEmptyObject();
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
    final JsonObject in = createSimpleObjectWithStr();
    final JsonObject patch = createPatchRemoveStr();
    final JsonObject check = createSimpleObject();
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
    System.out.println(" - for int to produce empty JSON object");
    final JsonObject in = createSimpleObjectInt();
    final JsonObject patch = createPatchRemoveInt();
    final JsonObject check = createEmptyObject();
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
    final JsonObject in = createSimpleObjectWithInt();
    final JsonObject patch = createPatchRemoveInt();
    final JsonObject check = createSimpleObject();
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
    System.out.println(" - for boolean to produce empty JSON object");
    final JsonObject in = createSimpleObjectBool();
    final JsonObject patch = createPatchRemoveBool();
    final JsonObject check = createEmptyObject();
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
    final JsonObject in = createSimpleObjectWithBool();
    final JsonObject patch = createPatchRemoveBool();
    final JsonObject check = createSimpleObject();
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
    System.out.println(" - for JsonObject to produce empty JSON object");
    final JsonObject in = createSimpleObjectObject();
    final JsonObject patch = createPatchRemoveObject();
    final JsonObject check = createEmptyObject();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testObjectOnsimpleObject(final TestResult result) {
    System.out.println(" - for JsonObject on compoubnd JSON object");
    final JsonObject in = createCompoundObjectWithObject();
    final JsonObject patch = createPatchRemoveObject();
    final JsonObject check = createCompoundObject();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

}

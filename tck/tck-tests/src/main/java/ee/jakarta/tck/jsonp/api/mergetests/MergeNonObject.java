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

import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 7396: JavaScript Object Notation (JSON) Merge Patch compatibility
 * tests.<br>
 * Checks scenario described in
 * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
 * Introduction</a>}: If the patch is anything other than an object, the result
 * will always be to replace the entire target with the entire patch.
 */
public class MergeNonObject extends MergeCommon {

  private static final Logger LOGGER = Logger.getLogger(MergeNonObject.class.getName());
  
  /**
   * Creates an instance of RFC 7396 non object patch test.
   */
  MergeNonObject() {
    super();
  }

  /**
   * Test RFC 7396: Non object patch. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 7396: Non object patch");
    LOGGER.info("Testing RFC 7396: Non object patch");
    testStringOnEmptyObject(result);
    testStringOnSimpleObject(result);
    testStringOnSimpleArray(result);
    testIntOnEmptyObject(result);
    testIntOnSimpleObject(result);
    testIntOnSimpleArray(result);
    testBoolOnEmptyObject(result);
    testBoolOnSimpleObject(result);
    testBoolOnSimpleArray(result);
    testArrayOnEmptyObject(result);
    testArrayOnCompoundObject(result);
    testArrayOnSimpleArray(result);
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
    final JsonValue patch = Json.createValue(SimpleValues.STR_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.STR_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithStr();
    final JsonValue patch = Json.createValue(SimpleValues.STR_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.STR_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code String} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array");
    final JsonArray in = SimpleValues.createStringArray2();
    final JsonValue patch = Json.createValue(SimpleValues.STR_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.STR_VALUE);
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
    final JsonValue patch = Json.createValue(SimpleValues.INT_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.INT_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithInt();
    final JsonValue patch = Json.createValue(SimpleValues.INT_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.INT_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code int} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array");
    final JsonArray in = SimpleValues.createIntArray2();
    final JsonValue patch = Json.createValue(SimpleValues.INT_VALUE);
    final JsonValue check = Json.createValue(SimpleValues.INT_VALUE);
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
    final JsonValue patch = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    final JsonValue check = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = SimpleValues.createSimpleObjectWithBool();
    final JsonValue patch = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    final JsonValue check = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code boolean} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array");
    final JsonArray in = SimpleValues.createBoolArray2();
    final JsonValue patch = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    final JsonValue check = SimpleValues.toJsonValue(SimpleValues.BOOL_VALUE);
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnEmptyObject(final TestResult result) {
    LOGGER.info(" - for JsonArray on empty JSON object");
    final JsonObject in = SimpleValues.createEmptyObject();
    final JsonArray patch = SimpleValues.createStringArray1();
    final JsonArray check = SimpleValues.createStringArray1();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnCompoundObject(final TestResult result) {
    LOGGER.info(" - for JsonArray on compound JSON object");
    final JsonObject in = SimpleValues.createCompoundObject();
    final JsonValue patch = SimpleValues.createStringArray2();
    final JsonValue check = SimpleValues.createStringArray2();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }

  /**
   * Test RFC 7396 patch and diff for {@code JsonArray} on empty JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testArrayOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonArray on simple JSON array");
    final JsonArray in = SimpleValues.createBoolArray2();
    final JsonValue patch = SimpleValues.createIntArray2();
    final JsonValue check = SimpleValues.createIntArray2();
    simpleMerge(result, in, patch, check);
    simpleDiff(result, in, check, patch);
  }
}

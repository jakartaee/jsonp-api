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

package ee.jakarta.tck.jsonp.api.patchtests;

import ee.jakarta.tck.jsonp.api.common.JsonAssert;
import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.*;

import java.util.logging.Logger;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests: {@link JsonPatch} API
 * factory methods added in JSON-P 1.1.<br>
 */
public class PatchCreate {

  private static final Logger LOGGER = Logger.getLogger(PatchCreate.class.getName());

  /**
   * Creates an instance of {@link JsonPatch} API factory methods added in
   * JSON-P 1.1 test.
   */
  PatchCreate() {
    super();
  }

  /**
   * Test {@link JsonPatch} factory method added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonPatch API factory methods added in JSON-P 1.1.");
    LOGGER.info("JsonPatch API factory methods added in JSON-P 1.1.");
    testCreateDiff(result);
    testCreatePatch(result);
    testCreatePatchBuilder(result);
    return result;
  }

  /**
   * Test {@link Json#createDiff(JsonStructure,JsonStructure)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateDiff(final TestResult result) {
    LOGGER.info(" - Json#createDiff(JsonStructure,JsonStructure)");
    final JsonObject src = SimpleValues.createSimpleObject();
    final JsonObject trg = SimpleValues.createSimpleObjectWithStr();
    final JsonPatch patch = Json.createDiff(src, trg);
    final JsonObject out = patch.apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createDiff(JsonStructure,JsonStructure)", "Builder output "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(trg));
    }
  }

  /**
   * Test {@link Json#createPatch(JsonArray)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreatePatch(final TestResult result) {
    LOGGER.info(" - Json#createPatch(JsonArray)");
    final JsonObject src = SimpleValues.createSimpleObject();
    final JsonObject trg = SimpleValues.createSimpleObjectWithStr();
    final JsonArray patchArray = Json.createDiff(src, trg).toJsonArray();
    final JsonPatch patch = Json.createPatch(patchArray);
    final JsonObject out = patch.apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createPatch(JsonArray)", "Builder output "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(trg));
    }
  }

  /**
   * Test {@link Json#createPatchBuilder(JsonArray)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreatePatchBuilder(final TestResult result) {
    LOGGER.info(" - Json#createPatchBuilder(JsonArray)");
    final JsonObject src = SimpleValues.createSimpleObject();
    final JsonObject trg = SimpleValues.createSimpleObjectWithStr();
    final JsonArray patchArray = Json.createDiff(src, trg).toJsonArray();
    final JsonPatchBuilder patchBuilder = Json.createPatchBuilder(patchArray);
    final JsonObject out = patchBuilder.build().apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createPatchBuilder(JsonArray)", "Builder output "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(trg));
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

}

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

package jakarta.jsonp.tck.api.patchtests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonValue;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests: {@link JsonPatch} API
 * factory methods added in JSON-P 1.1.<br>
 */
public class PatchCreate {

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
    System.out.println("JsonPatch API factory methods added in JSON-P 1.1.");
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
    System.out.println(" - Json#createDiff(JsonStructure,JsonStructure)");
    final JsonObject src = createSimpleObject();
    final JsonObject trg = createSimpleObjectWithStr();
    final JsonPatch patch = Json.createDiff(src, trg);
    final JsonObject out = patch.apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createDiff(JsonStructure,JsonStructure)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(trg));
    }
  }

  /**
   * Test {@link Json#createPatch(JsonArray)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreatePatch(final TestResult result) {
    System.out.println(" - Json#createPatch(JsonArray)");
    final JsonObject src = createSimpleObject();
    final JsonObject trg = createSimpleObjectWithStr();
    final JsonArray patchArray = Json.createDiff(src, trg).toJsonArray();
    final JsonPatch patch = Json.createPatch(patchArray);
    final JsonObject out = patch.apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createPatch(JsonArray)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(trg));
    }
  }

  /**
   * Test {@link Json#createPatchBuilder(JsonArray)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreatePatchBuilder(final TestResult result) {
    System.out.println(" - Json#createPatchBuilder(JsonArray)");
    final JsonObject src = createSimpleObject();
    final JsonObject trg = createSimpleObjectWithStr();
    final JsonArray patchArray = Json.createDiff(src, trg).toJsonArray();
    final JsonPatchBuilder patchBuilder = Json.createPatchBuilder(patchArray);
    final JsonObject out = patchBuilder.build().apply(src);
    if (operationFailed(trg, out)) {
      result.fail("createPatchBuilder(JsonArray)", "Builder output "
          + valueToString(out) + " value shall be " + valueToString(trg));
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
    return out == null || !assertEquals(check, out);
  }

}

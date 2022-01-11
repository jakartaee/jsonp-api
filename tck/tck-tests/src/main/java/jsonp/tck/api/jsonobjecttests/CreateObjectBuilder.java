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

package jsonp.tck.api.jsonobjecttests;

import jsonp.tck.api.common.TestResult;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonObjectBuilder} API factory methods added in JSON-P 1.1.<br>
 */
public class CreateObjectBuilder {

  private static final Logger LOGGER = Logger.getLogger(CreateObjectBuilder.class.getName());

  /**
   * Test {@link JsonObjectBuilder} factory method added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonObjectBuilder API factory methods added in JSON-P 1.1.");
    LOGGER.info("JsonObjectBuilder API factory methods added in JSON-P 1.1.");
    testCreateFromMap(result);
    testCreateFromJsonObject(result);
    return result;
  }

  /**
   * Test {@link Json#createObjectBuilder(Map<String,Object>)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromMap(final TestResult result) {
    LOGGER.info(" - Json#createObjectBuilder(Map<String,Object>)");
    final JsonObject check = SimpleValues.createSimpleObjectWithStr();
    Map<String, Object> values = new HashMap<>(2);
    values.put(SimpleValues.DEF_NAME, SimpleValues.DEF_VALUE);
    values.put(SimpleValues.STR_NAME, SimpleValues.STR_VALUE);
    final JsonObjectBuilder builder = Json.createObjectBuilder(values);
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("createObjectBuilder(Map<String,Object>)", "Builder output "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Test {@link Json#createObjectBuilder(JsonObject)} method.
   * 
   * @param result
   *          Test suite result.
   */
  private void testCreateFromJsonObject(final TestResult result) {
    LOGGER.info(" - Json#createObjectBuilder(JsonObject)");
    final JsonObject check = SimpleValues.createSimpleObjectWithStr();
    final JsonObjectBuilder builder = Json.createObjectBuilder(check);
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("reateObjectBuilder(JsonObject)", "Builder output "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
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

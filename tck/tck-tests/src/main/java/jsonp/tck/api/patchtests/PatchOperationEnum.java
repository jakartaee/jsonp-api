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
import jakarta.json.JsonPatch;

import java.util.logging.Logger;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Test {@link JsonPatch.Operation} enumeration.
 */
public class PatchOperationEnum {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationEnum.class.getName());

  /**
   * Creates an instance of {@link JsonPatch.Operation} enumeration test.
   */
  PatchOperationEnum() {
    super();
  }

  /**
   * Test {@link JsonPatch.Operation} enumeration. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonPatch.Operation enumeration test");
    LOGGER.info("JsonPatch.Operation enumeration test");
    testOperationName(result);
    testOperationValueOf(result);
    return result;
  }

  /**
   * Test {@link JsonPatch.Operation#fromOperationName(String)} and
   * {@link JsonPatch.Operation#operationName()} methods.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOperationName(final TestResult result) {
    LOGGER.info(" - fromOperationName(String) and operationName(String)");
    for (final JsonPatch.Operation op : JsonPatch.Operation.values()) {
      final String opName = op.operationName();
      final JsonPatch.Operation opOut = JsonPatch.Operation
          .fromOperationName(opName);
      final int opNameLen = opName.length();
      boolean opNameLc = true;
      for (int i = 0; opNameLc && i < opNameLen; i++) {
        opNameLc = Character.isLowerCase(opName.charAt(i));
      }
      if (!opNameLc) {
        result.fail("operationName(String)",
            "Returned value " + opName + " is not lower case String");
      }
      if (op != opOut) {
        result.fail("fromOperationName(String) and operationName(String)",
            "Returned operation " + opOut.name() + " shall be " + op.name());
      }
    }
  }

  /**
   * Test {@code JsonPatch.Operation#valueOf(String)} method.
   * 
   * @param result
   *          Tests result record.
   */
  private void testOperationValueOf(final TestResult result) {
    LOGGER.info(" - valueOf(String)");
    for (final JsonPatch.Operation op : JsonPatch.Operation.values()) {
      final String opName = op.name();
      final JsonPatch.Operation opOut = JsonPatch.Operation.valueOf(opName);
      if (op != opOut) {
        result.fail("valueOf(String)",
            "Returned operation " + opOut.name() + " shall be " + op.name());
      }
    }
  }

}

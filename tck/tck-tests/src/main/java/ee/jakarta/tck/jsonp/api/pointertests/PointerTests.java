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

package ee.jakarta.tck.jsonp.api.pointertests;

import ee.jakarta.tck.jsonp.api.common.TestResult;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

// $Id$
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: JavaScript
 * Object Notation (JSON) Pointer compatibility tests.<br>
 * JSON-P API defines {@link jakarta.json.JsonPointer} interface to work with RFC
 * 6901 JSON Pointer.
 */
public class PointerTests {

  /**
   * Test JSON-P API response on pointer resolving.<br>
   * Checks set of JSON pointers from sample object of RFC 6901.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-4">RFC 6901: 4.
   * Evaluation</a>} and
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-5">RFC 6901: 5.
   * JSON String Representation</a>}.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   * 
   * @testName: jsonPointerResolveTest
   * @assertion_ids: JSONP:JAVADOC:643; JSONP:JAVADOC:582; JSONP:JAVADOC:583;
   *                 JSONP:JAVADOC:584; JSONP:JAVADOC:661; JSONP:JAVADOC:662;
   *                 JSONP:JAVADOC:663;
   * @test_Strategy: Test API response on various JSON pointer values.
   */
  @Test
  public void jsonPointerResolveTest() {
    PointerResolve resolveTest = new PointerResolve();
    final TestResult result = resolveTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
   * 4.1. add</a>} operation using RFC 6901 pointer instance.<br>
   * Checks set of simple JSON values.<br>
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   * 
   * @testName: jsonPointerAddOperationTest
   * @assertion_ids: JSONP:JAVADOC:642; JSONP:JAVADOC:582; JSONP:JAVADOC:583;
   *                 JSONP:JAVADOC:584; JSONP:JAVADOC:661; JSONP:JAVADOC:662;
   *                 JSONP:JAVADOC:663;
   * @test_Strategy: Test API response on various JSON pointer values.
   */
  @Test
  public void jsonPointerAddOperationTest() {
    PointerAdd addTest = new PointerAdd();
    final TestResult result = addTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902:
   * 4.2. remove</a>} operation using RFC 6901 pointer instance.<br>
   * Checks set of simple JSON values.<br>
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   * 
   * @testName: jsonPointerRemoveOperationTest
   * @assertion_ids: JSONP:JAVADOC:644; JSONP:JAVADOC:582; JSONP:JAVADOC:661;
   * @test_Strategy: Test API response on various JSON pointer values.
   */
  @Test
  public void jsonPointerRemoveOperationTest() {
    PointerRemove removeTest = new PointerRemove();
    final TestResult result = removeTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
   * 4.3. replace</a>} operation using RFC 6901 pointer instance.<br>
   * Checks set of simple JSON values.<br>
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   * 
   * @testName: jsonPointerReplaceOperationTest
   * @assertion_ids: JSONP:JAVADOC:645; JSONP:JAVADOC:582; JSONP:JAVADOC:583;
   *                 JSONP:JAVADOC:584; JSONP:JAVADOC:661; JSONP:JAVADOC:662;
   *                 JSONP:JAVADOC:663;
   * @test_Strategy: Test API response on various JSON pointer values.
   */
  @Test
  public void jsonPointerReplaceOperationTest() {
    PointerReplace replaceTest = new PointerReplace();
    final TestResult result = replaceTest.test();
    result.eval();
  }

}

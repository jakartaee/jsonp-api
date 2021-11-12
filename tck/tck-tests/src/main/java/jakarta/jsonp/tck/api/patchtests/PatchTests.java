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

package jakarta.jsonp.tck.api.patchtests;

import jakarta.json.JsonPatch;
import jakarta.jsonp.tck.api.common.TestResult;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;


// $Id$
/*
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 */
public class PatchTests {

  /**
   * Test {@link JsonPatch} factory methods added in JSON-P 1.1.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonCreatePatch11Test
   *
   * @assertion_ids: JSONP:JAVADOC:574; JSONP:JAVADOC:579; JSONP:JAVADOC:581;
   *                 JSONP:JAVADOC:653; JSONP:JAVADOC:658; JSONP:JAVADOC:660;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:621;
   *
   * @test_Strategy: Tests JsonPatch API factory methods added in JSON-P 1.1.
   */
  @Test
  public void jsonCreatePatch11Test() {
    PatchCreate createTest = new PatchCreate();
    final TestResult result = createTest.test();
    result.eval();
  }

  /**
   * Test {@code JsonPatch.Operation} enumeration added in JSON-P 1.1.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonJsonPatchOperation11Test
   *
   * @assertion_ids: JSONP:JAVADOC:622; JSONP:JAVADOC:623; JSONP:JAVADOC:624;
   *                 JSONP:JAVADOC:625;
   *
   * @test_Strategy: Tests JsonPatch.Operation enumeration added in JSON-P 1.1.
   */
  @Test
  public void jsonJsonPatchOperation11Test() {
    PatchOperationEnum enumTest = new PatchOperationEnum();
    final TestResult result = enumTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on add operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">RFC 6902:
   * 4.1. add</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   * 
   * @testName: jsonPatchAddTest
   * @assertion_ids: JSONP:JAVADOC:626; JSONP:JAVADOC:627; JSONP:JAVADOC:628;
   *                 JSONP:JAVADOC:629; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of add operation.
   */
  @Test
  public void jsonPatchAddTest() {
    PatchOperationAdd addTest = new PatchOperationAdd();
    final TestResult result = addTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on remove operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">RFC 6902:
   * 4.2. remove</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonPatchRemoveTest
   * @assertion_ids: JSONP:JAVADOC:633; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of remove operation.
   */
  @Test
  public void jsonPatchRemoveTest() {
    PatchOperationRemove removeTest = new PatchOperationRemove();
    final TestResult result = removeTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on replace operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">RFC 6902:
   * 4.3. replace</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonPatchReplaceTest
   * @assertion_ids: JSONP:JAVADOC:634; JSONP:JAVADOC:635; JSONP:JAVADOC:636;
   *                 JSONP:JAVADOC:637; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of replace operation.
   */
  @Test
  public void jsonPatchReplaceTest() {
    PatchOperationReplace replaceTest = new PatchOperationReplace();
    final TestResult result = replaceTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on move operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.4. move</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonPatchMoveTest
   * @assertion_ids: JSONP:JAVADOC:632; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of move operation.
   */
  @Test
  public void jsonPatchMoveTest() {
    PatchOperationMove moveTest = new PatchOperationMove();
    final TestResult result = moveTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on copy operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.5">RFC 6902:
   * 4.5. copy</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonPatchCopyTest
   * @assertion_ids: JSONP:JAVADOC:631; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of copy operation.
   */
  @Test
  public void jsonPatchCopyTest() {
    PatchOperationCopy copyTest = new PatchOperationCopy();
    final TestResult result = copyTest.test();
    result.eval();
  }

  /**
   * Test JSONP API response on test operation.<br>
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.6">RFC 6902:
   * 4.6. test</a>}.
   *
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonPatchTestTest
   * @assertion_ids: JSONP:JAVADOC:638; JSONP:JAVADOC:639; JSONP:JAVADOC:640;
   *                 JSONP:JAVADOC:641; JSONP:JAVADOC:580; JSONP:JAVADOC:659;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:630;
   * @test_Strategy: Test API response on various usages of test operation.
   */
  @Test
  public void jsonPatchTestTest() {
    PatchOperationTest testTest = new PatchOperationTest();
    final TestResult result = testTest.test();
    result.eval();
  }

}

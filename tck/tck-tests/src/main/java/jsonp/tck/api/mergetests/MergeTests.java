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

package jsonp.tck.api.mergetests;

import jsonp.tck.api.common.TestResult;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

// $Id$
/**
 * RFC 7396: JavaScript Object Notation (JSON) Merge Patch compatibility
 * tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc7396">RFC 7396</a>}.
 */
public class MergeTests {

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
   * Introduction</a>}: If the provided merge patch contains members that do not
   * appear within the target, those members are added.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonMergeAddValueTest
   * @assertion_ids: JSONP:JAVADOC:575; JSONP:JAVADOC:576; JSONP:JAVADOC:616;
   *                 JSONP:JAVADOC:617; JSONP:JAVADOC:620; JSONP:JAVADOC:654;
   *                 JSONP:JAVADOC:655;
   * @test_Strategy: Test API response on various JSON values.
   */
  @Test
  public void jsonMergeAddValueTest() {
    MergeAddValue addTest = new MergeAddValue();
    final TestResult result = addTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
   * Introduction</a>}: If the target does contain the member, the value is
   * replaced.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonMergeReplaceValueTest
   * @assertion_ids: JSONP:JAVADOC:575; JSONP:JAVADOC:576; JSONP:JAVADOC:616;
   *                 JSONP:JAVADOC:617; JSONP:JAVADOC:654; JSONP:JAVADOC:655;
   * @test_Strategy: Test API response on various JSON values.
   */
  @Test
  public void jsonMergeReplaceValueTest() {
    MergeReplaceValue replaceTest = new MergeReplaceValue();
    final TestResult result = replaceTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
   * Introduction</a>}: {@code null} values in the merge patch are given special
   * meaning to indicate the removal of existing values in the target.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonMergeRemoveValueTest
   * @assertion_ids: JSONP:JAVADOC:575; JSONP:JAVADOC:576; JSONP:JAVADOC:616;
   *                 JSONP:JAVADOC:617; JSONP:JAVADOC:654; JSONP:JAVADOC:655;
   * @test_Strategy: Test API response on various JSON values.
   */
  @Test
  public void jsonMergeRemoveValueTest() {
    MergeRemoveValue removeTest = new MergeRemoveValue();
    final TestResult result = removeTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on
   * {@see <a href="https://tools.ietf.org/html/rfc7396#section-1">RFC 7396: 1.
   * Introduction</a>}: If the patch is anything other than an object, the
   * result will always be to replace the entire target with the entire patch.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonMergeNonObjectTest
   * @assertion_ids: JSONP:JAVADOC:575; JSONP:JAVADOC:576; JSONP:JAVADOC:616;
   *                 JSONP:JAVADOC:617; JSONP:JAVADOC:654; JSONP:JAVADOC:655;
   *                 JSONP:JAVADOC:583; JSONP:JAVADOC:584;
   * @test_Strategy: Test API response on various JSON values.
   */
  @Test
  public void jsonMergeNonObjectTest() {
    MergeNonObject nonObjTest = new MergeNonObject();
    final TestResult result = nonObjTest.test();
    result.eval();
  }

  /**
   * Test JSON-P API response on Test based on
   * {@see <a href="https://tools.ietf.org/html/rfc7396#section-3">RFC 7396: 3.
   * Example</a>} objects.
   * 
   * @throws AssertionFailedError
   *           when this test failed.
   *
   * @testName: jsonMergeRFCSampleTest
   * @assertion_ids: JSONP:JAVADOC:575; JSONP:JAVADOC:576; JSONP:JAVADOC:616;
   *                 JSONP:JAVADOC:617; JSONP:JAVADOC:654; JSONP:JAVADOC:655;
   * @test_Strategy: Test API response on RFC example objects.
   */
  @Test
  public void jsonMergeRFCSampleTest() {
    MergeRFCSample rfcSampleTest = new MergeRFCSample();
    final TestResult result = rfcSampleTest.test();
    result.eval();
  }

}

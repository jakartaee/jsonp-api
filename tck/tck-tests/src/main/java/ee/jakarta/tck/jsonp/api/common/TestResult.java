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

package ee.jakarta.tck.jsonp.api.common;

import org.opentest4j.AssertionFailedError;

import java.util.LinkedList;

/**
 * Tests result record.
 */
public class TestResult {

  /** Name of test suite. */
  private final String name;

  /** List of test failures. */
  private final LinkedList<TestFail> fails;

  /**
   * Creates an instance of tests result record.
   * 
   * @param name
   *          Name of test suite.
   */
  public TestResult(final String name) {
    this.name = name;
    this.fails = new LinkedList<>();
  }

  /**
   * Records test failure.
   * 
   * @param name
   *          Test name.
   * @param message
   *          Error message.
   */
  public void fail(final String name, final String message) {
    fails.addLast(new TestFail(name, message));
  }

  /**
   * Evaluate test results.
   * 
   * @throws AssertionFailedError
   *           when any test failed.
   */
  public void eval() {
    if (fails.isEmpty()) {
      return;
    }
    final StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(" failed:");
    for (TestFail fail : fails) {
      sb.append('\n');
      sb.append(fail.toString());
    }
    throw new AssertionFailedError(sb.toString());
  }

}

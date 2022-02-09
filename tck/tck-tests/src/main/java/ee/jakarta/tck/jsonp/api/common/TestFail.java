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

/**
 * Test failure notification.
 */
public class TestFail {

  /** Test failure name and message separator. */
  private static final String NM_SEP = ": ";

  /** Name of test that failed. */
  private final String name;

  /** Error message. */
  private final String message;

  /**
   * Creates an instance of test failure notification.
   * 
   * @param name
   *          Test name.
   * @param message
   *          Error message.
   */
  public TestFail(final String name, final String message) {
    this.name = name;
    this.message = message;
  }

  /**
   * Returns human readable content of test failure notification.
   * 
   * @return Human readable content of test failure notification.
   */
  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder(
        name.length() + message.length() + NM_SEP.length());
    sb.append(name);
    sb.append(NM_SEP);
    sb.append(message);
    return sb.toString();
  }

}

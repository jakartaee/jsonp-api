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

/*
 * $Id$
 */

package ee.jakarta.tck.jsonp.common;


import jakarta.json.stream.JsonLocation;

public class MyJsonLocation implements JsonLocation {

  private long lineNumber = -1;

  private long columnNumber = -1;

  private long streamOffset = -1;

  public MyJsonLocation() {
  }

  public MyJsonLocation(long lineNumber, long columnNumber, long streamOffset) {
    this.lineNumber = lineNumber;
    this.columnNumber = columnNumber;
    this.streamOffset = streamOffset;
  }

  public void setLineNumber(long lineNumber) {
    this.lineNumber = lineNumber;
  }

  public long getLineNumber() {
    return lineNumber;
  }

  public void setColumnNumber(long columnNumber) {
    this.columnNumber = columnNumber;
  }

  public long getColumnNumber() {
    return columnNumber;
  }

  public void setStreamOffset(long streamOffset) {
    this.streamOffset = streamOffset;
  }

  public long getStreamOffset() {
    return streamOffset;
  }
}

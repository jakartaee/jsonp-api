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

/*
 * $Id$
 */

package jsonp.tck.common;


import java.io.*;
import java.util.logging.Logger;

// A wrapper class to BufferedWriter class used to inject IOException errors
// when the throwIOException instance variable is set. All methods delegate
// to the parent super class and check whether or not to throw an IOException
// before delegation.

public class MyBufferedWriter extends BufferedWriter {

  private static final Logger LOGGER = Logger.getLogger(MyBufferedWriter.class.getName());

  private boolean throwIOException;

  public MyBufferedWriter(Writer out) {
    super(out);
  }

  public MyBufferedWriter(Writer out, int sz) {
    super(out, sz);
  }

  public MyBufferedWriter(Writer out, int sz, boolean throwIOException) {
    super(out, sz);
    this.throwIOException = throwIOException;
  }

  private void checkToTripIOException() throws IOException {
    if (throwIOException) {
      LOGGER.info("*** tripping an IOException ***");
      throw new IOException("tripping an IOException");
    }
  }

  public void setThrowIOException(boolean throwIOException) {
    this.throwIOException = throwIOException;
  }

  public void write(int c) throws IOException {
    checkToTripIOException();
    super.write(c);
  }

  public void write(char[] cbuf) throws IOException {
    checkToTripIOException();
    super.write(cbuf);
  }

  public void write(char[] cbuf, int offset, int length) throws IOException {
    checkToTripIOException();
    super.write(cbuf, offset, length);
  }

  public void write(String str) throws IOException {
    checkToTripIOException();
    super.write(str);
  }

  public void write(String str, int offset, int length) throws IOException {
    checkToTripIOException();
    super.write(str, offset, length);
  }

  public void close() throws IOException {
    checkToTripIOException();
    super.close();
  }

  public void flush() throws IOException {
    checkToTripIOException();
    super.flush();
  }
}

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

package ee.jakarta.tck.jsonp.common;


import java.io.*;
import java.util.logging.Logger;

// A wrapper class to BufferedReader class used to inject IOException errors
// when the throwIOException instance variable is set. All methods delegate
// to the parent super class and check whether or not to throw an IOException
// before delegation.

public class MyBufferedReader extends BufferedReader {

  private static final Logger LOGGER = Logger.getLogger(MyBufferedReader.class.getName());

  private boolean throwIOException;

  public MyBufferedReader(Reader in) {
    super(in);
  }

  public MyBufferedReader(Reader in, int sz) {
    super(in, sz);
  }

  public MyBufferedReader(Reader in, int sz, boolean throwIOException) {
    super(in, sz);
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

  public int read() throws IOException {
    checkToTripIOException();
    return super.read();
  }

  public int read(char[] cbuf, int off, int len) throws IOException {
    checkToTripIOException();
    return super.read(cbuf, off, len);
  }

  public String readLine() throws IOException {
    checkToTripIOException();
    return super.readLine();
  }

  public boolean ready() throws IOException {
    checkToTripIOException();
    return super.ready();
  }

  public void reset() throws IOException {
    checkToTripIOException();
    super.reset();
  }

  public void close() throws IOException {
    checkToTripIOException();
    super.close();
  }
}

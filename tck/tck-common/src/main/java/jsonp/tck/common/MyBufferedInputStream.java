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

// A wrapper class to BufferedInputStream class used to inject IOException errors
// when the throwIOException instance variable is set. All methods delegate
// to the parent super class and check whether or not to throw an IOException
// before delegation.

public class MyBufferedInputStream extends BufferedInputStream {

  private static final Logger LOGGER = Logger.getLogger(MyBufferedInputStream.class.getName());

  private boolean throwIOException = false;

  public MyBufferedInputStream(InputStream in) {
    super(in);
  }

  public MyBufferedInputStream(InputStream in, int sz) {
    super(in, sz);
  }

  public MyBufferedInputStream(InputStream in, boolean throwIOException) {
    super(in);
    this.throwIOException = throwIOException;
  }

  private void checkToTripIOException() throws IOException {
    if (throwIOException) {
      LOGGER.info(
          "MyBufferedInputStream->checkToTripIOException: *** tripping an IOException ***");
      throw new IOException("tripping an IOException");
    }
  }

  public void setThrowIOException(boolean throwIOException) {
    this.throwIOException = throwIOException;
  }

  public int read() throws IOException {
    checkToTripIOException();
    int c = super.read();
    return c;
  }

  public int read(byte[] b, int off, int len) throws IOException {
    checkToTripIOException();
    int c = super.read(b, off, len);
    return c;
  }

  public void close() throws IOException {
    checkToTripIOException();
    super.close();
  }
}

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

package jakarta.jsonp.tck.provider;

import jakarta.json.*;
import jakarta.json.stream.*;
import jakarta.json.spi.JsonProvider;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

/*
 * MyJsonReader is a Json Test Reader used by the pluggability tests
 * to test the Json SPI layer. This parser tracks that the proper callback
 * methods are invoked within the parser when Json API methods are called.
 */

public class MyJsonReader implements JsonReader {

  private static final Logger LOGGER = Logger.getLogger(MyJsonReader.class.getName());
  
  private InputStream in = null;

  private Reader reader = null;

  private void dumpInstanceVars() {
    LOGGER.info("reader=" + reader);
    LOGGER.info("in=" + in);
  }

  // call methods
  private static StringBuilder calls = new StringBuilder();

  public static String getCalls() {
    return calls.toString();
  }

  public static void clearCalls() {
    calls.delete(0, calls.length());
  }

  private static void addCalls(String s) {
    calls.append(s);
  }

  public MyJsonReader() {
  }

  public MyJsonReader(InputStream in) {
    this.in = in;
  }

  public MyJsonReader(Reader reader) {
    this.reader = reader;
  }

  public void close() {
    LOGGER.info("public void close()");
    addCalls("public void close()");
  }

  public JsonStructure read() {
    LOGGER.info("public void read()");
    addCalls("public void read()");
    return null;
  }

  public JsonArray readArray() {
    LOGGER.info("public void readArray()");
    addCalls("public void readArray()");
    return null;
  }

  public JsonObject readObject() {
    LOGGER.info("public void readObject()");
    addCalls("public void readObject()");
    return null;
  }
}

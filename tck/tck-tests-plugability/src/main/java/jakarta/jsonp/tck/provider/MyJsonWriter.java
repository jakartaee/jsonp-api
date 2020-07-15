/*
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * MyJsonWriter is a Json Test Writer used by the pluggability tests
 * to test the Json SPI layer. This parser tracks that the proper callback
 * methods are invoked within the parser when Json API methods are called.
 */
public class MyJsonWriter implements JsonWriter {
  private OutputStream out = null;

  private Writer writer = null;

  private Charset charset = Charset.forName("UTF-8");

  private void dumpInstanceVars() {
    System.out.println("writer=" + writer);
    System.out.println("out=" + out);
    System.out.println("charset=" + charset);
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

  public MyJsonWriter() {
  }

  public MyJsonWriter(OutputStream out) {
    this.out = out;
  }

  public MyJsonWriter(Writer writer) {
    this.writer = writer;
  }

  public void close() {
    System.out.println("public void close()");
    addCalls("public void close()");
  }

  public void write(JsonStructure value) {
    System.out.println("public void write(JsonStructure)");
    addCalls("public void write(JsonStructure)");
  }

  public void writeArray(JsonArray array) {
    System.out.println("public void writeArray(JsonArray)");
    addCalls("public void writeArray(JsonArray)");
  }

  public void writeObject(JsonObject object) {
    System.out.println("public void writeObject(JsonObject)");
    addCalls("public void writeObject(JsonObject)");
  }
}

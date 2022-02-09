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

package ee.jakarta.tck.jsonp.provider;

import jakarta.json.stream.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

/*
 * MyJsonGeneratorFactory is a Json Test GeneratorFactory used by the pluggability tests
 * to test the Json SPI layer. This parser tracks that the proper callback
 * methods are invoked within the parser when Json API methods are called.
 */

public class MyJsonGeneratorFactory implements JsonGeneratorFactory {

  private static final Logger LOGGER = Logger.getLogger(MyJsonGeneratorFactory.class.getName());

  private OutputStream out = null;

  private Writer writer = null;

  private Charset charset = Charset.forName("UTF-8");

  private Map<String, ?> config = null;

  private void dumpInstanceVars() {
    LOGGER.info("writer=" + writer);
    LOGGER.info("out=" + out);
    LOGGER.info("charset=" + charset);
    LOGGER.info("config=" + config);
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

  public MyJsonGeneratorFactory(Map<String, ?> config) {
    this.config = config;
  }

  public Map<String, ?> getConfigInUse() {
    LOGGER.info("public Map<String, ?> getConfigInUse()");
    addCalls("public Map<String, ?> getConfigInUse()");
    return config;
  }

  public JsonGenerator createGenerator(OutputStream out) {
    LOGGER.info("public JsonGenerator createGenerator(OutputStream)");
    addCalls("public JsonGenerator createGenerator(OutputStream)");
    this.out = out;
    return null;
  }

  public JsonGenerator createGenerator(OutputStream out, Charset charset) {
    LOGGER.info(
        "public JsonGenerator createGenerator(OutputStream, Charset)");
    addCalls("public JsonGenerator createGenerator(OutputStream, Charset)");
    this.out = out;
    this.charset = charset;
    return null;
  }

  public JsonGenerator createGenerator(Writer writer) {
    LOGGER.info("public JsonGenerator createGenerator(Writer)");
    addCalls("public JsonGenerator createGenerator(Writer)");
    this.writer = writer;
    return null;
  }
}

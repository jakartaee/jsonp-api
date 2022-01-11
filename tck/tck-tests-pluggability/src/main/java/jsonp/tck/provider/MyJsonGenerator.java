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

package jsonp.tck.provider;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import jakarta.json.*;
import jakarta.json.stream.*;

/*
 * MyJsonGenerator is a Json Test Generator used by the pluggability tests
 * to test the Json SPI layer. This generator tracks that the proper callback
 * methods are invoked within the generator when Json API methods are called.
 */

public class MyJsonGenerator implements JsonGenerator {

  private static final Logger LOGGER = Logger.getLogger(MyJsonGenerator.class.getName());

  private Writer writer = null;

  private OutputStream out = null;

  private final Charset charset = Charset.forName("UTF-8");

  private void dumpInstanceVars() {
    LOGGER.info("writer=" + writer);
    LOGGER.info("out=" + out);
    LOGGER.info("charset=" + charset);
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

  public MyJsonGenerator(Writer writer) {
    this.writer = writer;
  }

  public MyJsonGenerator(OutputStream out) {
    this.out = out;
  }

  @Override
  public void flush() {
    LOGGER.info("public void flush()");
    addCalls("public void flush()");
  }

  @Override
  public JsonGenerator writeStartObject() {
    LOGGER.info("public JsonGenerator writeStartObject()");
    addCalls("public JsonGenerator writeStartObject()");
    return null;
  }

  @Override
  public JsonGenerator writeStartObject(String name) {
    LOGGER.info("public JsonGenerator writeStartObject(String)");
    addCalls("public JsonGenerator writeStartObject(String)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, String value) {
    LOGGER.info("public JsonGenerator write(String,String)");
    addCalls("public JsonGenerator write(String,String)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, int value) {
    LOGGER.info("public JsonGenerator write(String,int)");
    addCalls("public JsonGenerator write(String,int)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, long value) {
    LOGGER.info("public JsonGenerator write(String,long)");
    addCalls("public JsonGenerator write(String,long)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, double value) {
    LOGGER.info("public JsonGenerator write(String,double)");
    addCalls("public JsonGenerator write(String,double)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigInteger value) {
    LOGGER.info("public JsonGenerator write(String,BigInteger)");
    addCalls("public JsonGenerator write(String,BigInteger)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigDecimal value) {
    LOGGER.info("public JsonGenerator write(String,BigDecimal)");
    addCalls("public JsonGenerator write(String,BigDecimal)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, boolean value) {
    LOGGER.info("public JsonGenerator write(String,boolean)");
    addCalls("public JsonGenerator write(String,boolean)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, JsonValue value) {
    LOGGER.info("public JsonGenerator write(String,JsonValue)");
    addCalls("public JsonGenerator write(String,JsonValue)");
    return null;
  }

  @Override
  public JsonGenerator writeNull(String name) {
    LOGGER.info("public JsonGenerator writeNull(String)");
    addCalls("public JsonGenerator writeNull(String)");
    return null;
  }

  @Override
  public JsonGenerator writeStartArray() {
    LOGGER.info("public JsonGenerator writeStartArray()");
    addCalls("public JsonGenerator writeStartArray()");
    return null;
  }

  @Override
  public JsonGenerator writeStartArray(String name) {
    LOGGER.info("public JsonGenerator writeStartArray(String)");
    addCalls("public JsonGenerator writeStartArray(String)");
    return null;
  }

  @Override
  public JsonGenerator write(String value) {
    LOGGER.info("public JsonGenerator write(String)");
    addCalls("public JsonGenerator write(String)");
    return null;
  }

  @Override
  public JsonGenerator write(int value) {
    LOGGER.info("public JsonGenerator write(int)");
    addCalls("public JsonGenerator write(int)");
    return null;
  }

  @Override
  public JsonGenerator write(long value) {
    LOGGER.info("public JsonGenerator write(long)");
    addCalls("public JsonGenerator write(long)");
    return null;
  }

  @Override
  public JsonGenerator write(double value) {
    LOGGER.info("public JsonGenerator write(double)");
    addCalls("public JsonGenerator write(double)");
    return null;
  }

  @Override
  public JsonGenerator write(BigInteger value) {
    LOGGER.info("public JsonGenerator write(BigInteger)");
    addCalls("public JsonGenerator write(BigInteger)");
    return null;
  }

  @Override
  public JsonGenerator write(BigDecimal value) {
    LOGGER.info("public JsonGenerator write(BigDecimal)");
    addCalls("public JsonGenerator write(BigDecimal)");
    return null;
  }

  @Override
  public JsonGenerator write(boolean value) {
    LOGGER.info("public JsonGenerator write(boolean)");
    addCalls("public JsonGenerator write(boolean)");
    return null;
  }

  @Override
  public JsonGenerator write(JsonValue value) {
    LOGGER.info("public JsonGenerator write(JsonValue)");
    addCalls("public JsonGenerator write(JsonValue)");
    return null;
  }

  @Override
  public JsonGenerator writeNull() {
    LOGGER.info("public JsonGenerator writeNull()");
    addCalls("public JsonGenerator writeNull()");
    return null;
  }

  @Override
  public JsonGenerator writeEnd() {
    LOGGER.info("public JsonGenerator writeEnd()");
    addCalls("public JsonGenerator writeEnd()");
    return null;
  }

  @Override
  public JsonGenerator writeKey(String name) {
    LOGGER.info("public JsonGenerator writeKey()");
    addCalls("public JsonGenerator writeKey()");
    return null;
  }

  @Override
  public void close() {
    LOGGER.info("public void close()");
    addCalls("public void close()");
  }

}

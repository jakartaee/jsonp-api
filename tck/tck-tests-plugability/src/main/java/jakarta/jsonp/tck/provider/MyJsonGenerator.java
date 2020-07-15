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

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import jakarta.json.*;
import jakarta.json.stream.*;

/*
 * MyJsonGenerator is a Json Test Generator used by the pluggability tests
 * to test the Json SPI layer. This generator tracks that the proper callback
 * methods are invoked within the generator when Json API methods are called.
 */

public class MyJsonGenerator implements JsonGenerator {
  private Writer writer = null;

  private OutputStream out = null;

  private final Charset charset = Charset.forName("UTF-8");

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

  public MyJsonGenerator(Writer writer) {
    this.writer = writer;
  }

  public MyJsonGenerator(OutputStream out) {
    this.out = out;
  }

  @Override
  public void flush() {
    System.out.println("public void flush()");
    addCalls("public void flush()");
  }

  @Override
  public JsonGenerator writeStartObject() {
    System.out.println("public JsonGenerator writeStartObject()");
    addCalls("public JsonGenerator writeStartObject()");
    return null;
  }

  @Override
  public JsonGenerator writeStartObject(String name) {
    System.out.println("public JsonGenerator writeStartObject(String)");
    addCalls("public JsonGenerator writeStartObject(String)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, String value) {
    System.out.println("public JsonGenerator write(String,String)");
    addCalls("public JsonGenerator write(String,String)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, int value) {
    System.out.println("public JsonGenerator write(String,int)");
    addCalls("public JsonGenerator write(String,int)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, long value) {
    System.out.println("public JsonGenerator write(String,long)");
    addCalls("public JsonGenerator write(String,long)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, double value) {
    System.out.println("public JsonGenerator write(String,double)");
    addCalls("public JsonGenerator write(String,double)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigInteger value) {
    System.out.println("public JsonGenerator write(String,BigInteger)");
    addCalls("public JsonGenerator write(String,BigInteger)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigDecimal value) {
    System.out.println("public JsonGenerator write(String,BigDecimal)");
    addCalls("public JsonGenerator write(String,BigDecimal)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, boolean value) {
    System.out.println("public JsonGenerator write(String,boolean)");
    addCalls("public JsonGenerator write(String,boolean)");
    return null;
  }

  @Override
  public JsonGenerator write(String name, JsonValue value) {
    System.out.println("public JsonGenerator write(String,JsonValue)");
    addCalls("public JsonGenerator write(String,JsonValue)");
    return null;
  }

  @Override
  public JsonGenerator writeNull(String name) {
    System.out.println("public JsonGenerator writeNull(String)");
    addCalls("public JsonGenerator writeNull(String)");
    return null;
  }

  @Override
  public JsonGenerator writeStartArray() {
    System.out.println("public JsonGenerator writeStartArray()");
    addCalls("public JsonGenerator writeStartArray()");
    return null;
  }

  @Override
  public JsonGenerator writeStartArray(String name) {
    System.out.println("public JsonGenerator writeStartArray(String)");
    addCalls("public JsonGenerator writeStartArray(String)");
    return null;
  }

  @Override
  public JsonGenerator write(String value) {
    System.out.println("public JsonGenerator write(String)");
    addCalls("public JsonGenerator write(String)");
    return null;
  }

  @Override
  public JsonGenerator write(int value) {
    System.out.println("public JsonGenerator write(int)");
    addCalls("public JsonGenerator write(int)");
    return null;
  }

  @Override
  public JsonGenerator write(long value) {
    System.out.println("public JsonGenerator write(long)");
    addCalls("public JsonGenerator write(long)");
    return null;
  }

  @Override
  public JsonGenerator write(double value) {
    System.out.println("public JsonGenerator write(double)");
    addCalls("public JsonGenerator write(double)");
    return null;
  }

  @Override
  public JsonGenerator write(BigInteger value) {
    System.out.println("public JsonGenerator write(BigInteger)");
    addCalls("public JsonGenerator write(BigInteger)");
    return null;
  }

  @Override
  public JsonGenerator write(BigDecimal value) {
    System.out.println("public JsonGenerator write(BigDecimal)");
    addCalls("public JsonGenerator write(BigDecimal)");
    return null;
  }

  @Override
  public JsonGenerator write(boolean value) {
    System.out.println("public JsonGenerator write(boolean)");
    addCalls("public JsonGenerator write(boolean)");
    return null;
  }

  @Override
  public JsonGenerator write(JsonValue value) {
    System.out.println("public JsonGenerator write(JsonValue)");
    addCalls("public JsonGenerator write(JsonValue)");
    return null;
  }

  @Override
  public JsonGenerator writeNull() {
    System.out.println("public JsonGenerator writeNull()");
    addCalls("public JsonGenerator writeNull()");
    return null;
  }

  @Override
  public JsonGenerator writeEnd() {
    System.out.println("public JsonGenerator writeEnd()");
    addCalls("public JsonGenerator writeEnd()");
    return null;
  }

  @Override
  public JsonGenerator writeKey(String name) {
    System.out.println("public JsonGenerator writeKey()");
    addCalls("public JsonGenerator writeKey()");
    return null;
  }

  @Override
  public void close() {
    System.out.println("public void close()");
    addCalls("public void close()");
  }

}

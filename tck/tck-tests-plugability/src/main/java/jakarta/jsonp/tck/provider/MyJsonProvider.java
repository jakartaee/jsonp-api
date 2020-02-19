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

package jakarta.jsonp.tck.provider;

import java.io.*;
import java.util.*;
import jakarta.json.*;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.*;

// $Id$
/*
 * MyJsonProvider is a Json Test Provider used by the pluggability tests
 * to test the Json SPI layer. This provider tracks that the proper callback
 * methods are invoked within the provider when Json API methods are called.
 */
public class MyJsonProvider extends JsonProvider {

  // Exception thrown when encoding or i/o error
  private final JsonException exception = new JsonException(
      "encoding or i/o error");

  // call methods
  private static final StringBuilder CALLS = new StringBuilder();

  public static String getCalls() {
    return CALLS.toString();
  }

  public static void clearCalls() {
    CALLS.delete(0, CALLS.length());
  }

  private static void addCalls(String s) {
    CALLS.append(s);
  }

  @Override
  public JsonArrayBuilder createArrayBuilder() {
    System.out.println("public JsonArrayBuilder createArrayBuilder()");
    addCalls("public JsonArrayBuilder createArrayBuilder()");
    return null;
  }

  @Override
  public JsonBuilderFactory createBuilderFactory(Map<String, ?> config) {
    System.out.println(
        "public JsonBuilderFactory createBuilderFactory(Map<String, ?>)");
    addCalls("public JsonBuilderFactory createBuilderFactory(Map<String, ?>)");
    return null;
  }

  @Override
  public JsonObjectBuilder createObjectBuilder() {
    System.out.println("public JsonObjectBuilder createObjectBuilder()");
    addCalls("public JsonObjectBuilder createObjectBuilder()");
    return null;
  }

  @Override
  public JsonReader createReader(Reader reader) {
    System.out.println("public JsonReader createReader(Reader)");
    addCalls("public JsonReader createReader(Reader)");
    return new MyJsonReader(reader);
  }

  @Override
  public JsonReader createReader(InputStream in) {
    System.out.println("public JsonReader createReader(InputStream)");
    addCalls("public JsonReader createReader(InputStream)");
    return new MyJsonReader(in);
  }

  @Override
  public JsonReaderFactory createReaderFactory(Map<String, ?> config) {
    System.out.println(
        "public JsonReaderFactory createReaderFactory(Map<String, ?>)");
    addCalls("public JsonReaderFactory createReaderFactory(Map<String, ?>)");
    return null;
  }

  @Override
  public JsonWriter createWriter(Writer writer) {
    System.out.println("public JsonWriter createWriter(Writer)");
    addCalls("public JsonWriter createWriter(Writer)");
    return new MyJsonWriter(writer);
  }

  @Override
  public JsonWriter createWriter(OutputStream out) {
    System.out.println("public JsonWriter createWriter(OutputStream)");
    addCalls("public JsonWriter createWriter(OutputStream)");
    return new MyJsonWriter(out);
  }

  @Override
  public JsonWriterFactory createWriterFactory(Map<String, ?> config) {
    System.out.println(
        "public JsonWriterFactory createWriterFactory(Map<String, ?>)");
    addCalls("public JsonWriterFactory createWriterFactory(Map<String, ?>)");
    return null;
  }

  @Override
  public JsonGenerator createGenerator(Writer writer) {
    System.out.println("public JsonGenerator createGenerator(Writer)");
    addCalls("public JsonGenerator createGenerator(Writer)");
    return new MyJsonGenerator(writer);
  }

  @Override
  public JsonGenerator createGenerator(OutputStream out) {
    System.out.println("public JsonGenerator createGenerator(OutputStream)");
    addCalls("public JsonGenerator createGenerator(OutputStream)");
    return new MyJsonGenerator(out);
  }

  @Override
  public JsonParser createParser(Reader reader) {
    System.out.println("public JsonParser createParser(Reader)");
    addCalls("public JsonParser createParser(Reader)");
    return new MyJsonParser(reader);
  }

  @Override
  public JsonParser createParser(InputStream in) {
    System.out.println("public JsonParser createParser(InputStream)");
    addCalls("public JsonParser createParser(InputStream)");
    if (in == null)
      throw exception;
    else
      return new MyJsonParser(in);
  }

  @Override
  public JsonParserFactory createParserFactory(Map<String, ?> config) {
    System.out.println(
        "public JsonParserFactory createParserFactory(Map<String, ?>)");
    addCalls("public JsonParserFactory createParserFactory(Map<String, ?>)");
    return null;
  }

  @Override
  public JsonGeneratorFactory createGeneratorFactory(Map<String, ?> config) {
    System.out.println(
        "public JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)");
    addCalls(
        "public JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)");
    return null;
  }

  @Override
  public JsonPatchBuilder createPatchBuilder() {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }

  @Override
  public JsonPatchBuilder createPatchBuilder(JsonArray ja) {
    throw new UnsupportedOperationException("Not supported yet."); // To change
                                                                   // body of
                                                                   // generated
                                                                   // methods,
                                                                   // choose
                                                                   // Tools |
                                                                   // Templates.
  }

}

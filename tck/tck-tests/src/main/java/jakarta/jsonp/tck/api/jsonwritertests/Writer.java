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

package jakarta.jsonp.tck.api.jsonwritertests;

import jakarta.jsonp.tck.api.common.JsonValueType;
import jakarta.jsonp.tck.api.common.SimpleValues;
import jakarta.jsonp.tck.api.common.TestResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.stream.JsonParser;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonWriter}.
 */
public class Writer {

  /** Tests input data. */
  private static final Object[] VALUES = new Object[] { OBJ_VALUE, // write(JsonValue)
                                                                   // for
                                                                   // JsonObject
      createEmptyArrayWithStr(), // write(JsonValue) for simple JsonArray
      STR_VALUE, // write(JsonValue) for String
      INT_VALUE, // write(JsonValue) for int
      LNG_VALUE, // write(JsonValue) for long
      DBL_VALUE, // write(JsonValue) for double
      BIN_VALUE, // write(JsonValue) for BigInteger
      BDC_VALUE, // write(JsonValue) for BigDecimal
      BOOL_VALUE, // write(JsonValue) for boolean
      null // write(JsonValue) for null
  };

  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonWriter}.
   */
  Writer() {
    super();
  }

  /**
   * {@link JsonWriter} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonWriter API methods added in JSON-P 1.1.");
    System.out.println("JsonWriter API methods added in JSON-P 1.1.");
    testWriteValue(result);
    testDoubleWriteValue(result);
    testIOExceptionOnWriteValue(result);
    return result;
  }

  /**
   * Test {@code void write(JsonValue)} method on all child types of
   * {@code JsonValue}.
   * 
   * @param result
   *          Test suite result.
   */
  private void testWriteValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      System.out.println(" - write(JsonValue) for " + typeName + " as an argument");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final StringWriter strWriter = new StringWriter();
      try (final JsonWriter writer = Json.createWriter(strWriter)) {
        writer.write(jsonValue);
      } catch (JsonException ex) {
        System.out.println("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("write(JsonValue)",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
      final String data = strWriter.toString();
      System.out.println("    - Data: " + data);
      final JsonParser parser = Json.createParser(new StringReader(data));
      parser.next();
      final JsonValue outValue = parser.getValue();
      if (operationFailed(jsonValue, outValue)) {
        result.fail("write(JsonValue)",
            "Writer output " + valueToString(outValue) + " value shall be "
                + valueToString(jsonValue));
      }
    }
  }

  /**
   * Test {@code void write(JsonValue)} method with duplicated {@code JsonValue}
   * write call. Second call is expected to throw {@code IllegalStateException}
   * exception.
   * 
   * @param result
   *          Test suite result.
   */
  private void testDoubleWriteValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      System.out.println(
          " - duplicate write(JsonValue) for " + typeName + " as an argument");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final StringWriter strWriter = new StringWriter();
      try (final JsonWriter writer = Json.createWriter(strWriter)) {
        // 1st attempt to write the data shall pass
        writer.write(jsonValue);
        try {
          // 2nd attempt to write the data shall throw IllegalStateException
          writer.write(jsonValue);
          result.fail("write(JsonValue)",
              "Duplicate call of write(JsonValue) shall throw IllegalStateException");
        } catch (IllegalStateException ex) {
          System.out.println("    - Expected exception: " + ex.getMessage());
        } catch (Throwable t) {
          result.fail("write(JsonValue)",
              "Duplicate call of write(JsonValue) shall throw IllegalStateException, not "
                  + t.getClass().getSimpleName());
        }
      } catch (JsonException ex) {
        System.out.println("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("write(JsonValue)",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code void write(JsonValue)} method with write call that causes
   * IOException. IOException shall be encapsulated in JsonException.
   * 
   * @param result
   *          Test suite result.
   */
  @SuppressWarnings("ConvertToTryWithResources")
  private void testIOExceptionOnWriteValue(final TestResult result) {
    System.out.println(" - write(JsonValue) into already closed file writer");
    final JsonValue jsonValue = SimpleValues.toJsonValue(DEF_VALUE);
    File temp = null;
    JsonWriter writer;
    // Close writer before calling write method.
    try {
      temp = File.createTempFile("testIOExceptionOnWriteValue", ".txt");
      System.out.println("    - Temporary file: " + temp.getAbsolutePath());
      final FileWriter fileWriter = new FileWriter(temp);
      writer = Json.createWriter(fileWriter);
      fileWriter.close();
    } catch (IOException ex) {
      System.out.println("Caught IOException: " + ex.getLocalizedMessage());
      result.fail("write(JsonValue)",
          "Caught IOException: " + ex.getLocalizedMessage());
      return;
    } finally {
      if (temp != null) {
        temp.delete();
      }
    }
    try {
      writer.write(jsonValue);
      result.fail("write(JsonValue)",
          "Call of write(JsonValue) on already closed file writer shall throw JsonException");
    } catch (JsonException ex) {
      System.out.println("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("write(JsonValue)",
          "Call of write(JsonValue) on already closed file writer shall throw JsonException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected modified JSON value.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonValue check,
      final JsonValue out) {
    return out == null || !assertEquals(check, out);
  }

}

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

package ee.jakarta.tck.jsonp.api.jsonreadertests;

import ee.jakarta.tck.jsonp.api.common.JsonAssert;
import ee.jakarta.tck.jsonp.api.common.JsonValueType;
import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParsingException;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonReader}.
 */
public class Reader {

  private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());
  
  /** Tests input data. */
  private static final Object[] VALUES = new Object[] { SimpleValues.OBJ_VALUE, // readValue()
                                                                   // for
                                                                   // JsonObject
      SimpleValues.createEmptyArrayWithStr(), // readValue() for simple JsonArray
      SimpleValues.STR_VALUE, // readValue() for String
      SimpleValues.INT_VALUE, // readValue() for int
      SimpleValues.LNG_VALUE, // readValue() for long
      SimpleValues.DBL_VALUE, // readValue() for double
      SimpleValues.BIN_VALUE, // readValue() for BigInteger
      SimpleValues.BDC_VALUE, // readValue() for BigDecimal
      SimpleValues.BOOL_VALUE, // readValue() for boolean
      null // readValue() for null
  };

  /**
   * Creates an instance of JavaScript Object Notation (JSON) compatibility
   * tests for {@link JsonReader}.
   */
  Reader() {
    super();
  }

  /**
   * {@link JsonReader} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonReader API methods added in JSON-P 1.1.");
    LOGGER.info("JsonReader API methods added in JSON-P 1.1.");
    testReadValue(result);
    testDoubleReadValue(result);
    testIOExceptionOnReadValue(result);
    testReadInvalidValue(result);
    return result;
  }

  /**
   * Test {@code JsonValue readValue()} method on all child types stored in
   * source data.
   * 
   * @param result
   *          Test suite result.
   */
  private void testReadValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - readValue() for " + typeName + " in source data");
      final JsonValue jsonValue = SimpleValues.toJsonValue(value);
      final String data = JsonValueType.toStringValue(value);
      LOGGER.info("    - Data: " + data);
      final StringReader strReader = new StringReader(data);
      JsonValue outValue = null;
      try (final JsonReader reader = Json.createReader(strReader)) {
        outValue = reader.readValue();
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("readValue()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
      if (operationFailed(jsonValue, outValue)) {
        result.fail("readValue()", "Reader output " + JsonAssert.valueToString(outValue)
            + " value shall be " + JsonAssert.valueToString(jsonValue));
      }
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with duplicated {@code JsonValue}
   * read call. Second call is expected to throw {@code IllegalStateException}
   * exception.
   * 
   * @param result
   *          Test suite result.
   */
  private void testDoubleReadValue(final TestResult result) {
    for (Object value : VALUES) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(
          " - duplicate readValue() for " + typeName + " in source data");
      final String data = JsonValueType.toStringValue(value);
      final StringReader strReader = new StringReader(data);
      try (final JsonReader reader = Json.createReader(strReader)) {
        // 1st attempt to read the data shall pass
        reader.readValue();
        try {
          // 2nd attempt to read the data shall throw IllegalStateException
          reader.readValue();
          result.fail("readValue()",
              "Duplicate call of readValue() shall throw IllegalStateException");
        } catch (IllegalStateException ex) {
          LOGGER.info("    - Expected exception: " + ex.getMessage());
        } catch (Throwable t) {
          result.fail("readValue()",
              "Duplicate call of readValue() shall throw IllegalStateException, not "
                  + t.getClass().getSimpleName());
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("readValue()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with read call that causes
   * IOException. IOException shall be encapsulated in JsonException.
   * 
   * @param result
   *          Test suite result.
   */
  @SuppressWarnings("ConvertToTryWithResources")
  private void testIOExceptionOnReadValue(final TestResult result) {
    LOGGER.info(" - readValue() from already closed file reader");
    File temp = null;
    JsonReader reader;
    // Close writer before calling write method.
    try {
      temp = File.createTempFile("testIOExceptionOnReadValue", ".txt");
      LOGGER.info("    - Temporary file: " + temp.getAbsolutePath());
      try (final FileWriter fileWriter = new FileWriter(temp)) {
        fileWriter.write(JsonValueType.toStringValue(SimpleValues.DEF_VALUE));
      }
      final FileReader fileReader = new FileReader(temp);
      reader = Json.createReader(fileReader);
      fileReader.close();
    } catch (IOException ex) {
      LOGGER.info("Caught IOException: " + ex.getLocalizedMessage());
      result.fail("write(JsonValue)",
          "Caught IOException: " + ex.getLocalizedMessage());
      return;
    } finally {
      if (temp != null) {
        temp.delete();
      }
    }
    try {
      reader.readValue();
      result.fail("readValue()",
          "Call of readValue() on already closed file reader shall throw JsonException");
    } catch (JsonException ex) {
      LOGGER.info("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("readValue()",
          "Call of readValue() on already closed file reader shall throw JsonException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonValue readValue()} method with read call on invalid JSON
   * data. JsonParsingException shall be thrown when reading invalid data.
   * 
   * @param result
   *          Test suite result.
   */
  private void testReadInvalidValue(final TestResult result) {
    LOGGER.info(" - readValue() on invalid JSON data");
    // Invalid JSON: starting an array, closing an object.
    final String data = "[" + SimpleValues.toJsonValue(SimpleValues.DEF_VALUE) + "}";
    final StringReader strReader = new StringReader(data);
    JsonValue outValue = null;
    try (final JsonReader reader = Json.createReader(strReader)) {
      reader.readValue();
      result.fail("readValue()",
          "Call of readValue() on invalid data shall throw JsonParsingException");
    } catch (JsonParsingException ex) {
      LOGGER.info("    - Expected exception: " + ex.getMessage());
    } catch (Throwable t) {
      result.fail("readValue()",
          "Call of readValue() on invalid data shall throw JsonParsingException, not "
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
    return out == null || !JsonAssert.assertEquals(check, out);
  }

}

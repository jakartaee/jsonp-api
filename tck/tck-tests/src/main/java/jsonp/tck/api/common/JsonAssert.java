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

package jsonp.tck.api.common;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jsonp.tck.common.JSONP_Util;

// $Id$
/**
 * JSON values assertions.
 */
public class JsonAssert {

  private static final Logger LOGGER = Logger.getLogger(JsonAssert.class.getName());

  private static boolean assertEquals(final JsonObject expected,
      final JsonObject actual, final String message) {
    if (actual.equals(expected)) {
      return true;
    } else {
      LOGGER.info("   " + message);
      LOGGER.info("     Expected: " + JSONP_Util.toStringJsonObject(expected));
      LOGGER.info("     Actual:   " + JSONP_Util.toStringJsonObject(actual));
      return false;
    }
  }

  private static boolean assertEquals(final JsonArray expected,
      final JsonArray actual, final String message) {
    if (actual.equals(expected)) {
      return true;
    } else {
      LOGGER.info("   " + message);
      LOGGER.info("     Expected: " + JSONP_Util.toStringJsonArray(expected));
      LOGGER.info("     Actual:   " + JSONP_Util.toStringJsonArray(actual));
      return false;
    }
  }

  private static boolean assertEquals(final JsonString expected,
      final JsonString actual, final String message) {
    if (actual.equals(expected)) {
      return true;
    } else {
      LOGGER.info("   " + message);
      LOGGER.info("     Expected: " + expected.getString());
      LOGGER.info("     Actual:   " + actual.getString());
      return false;
    }
  }

  private static boolean assertEquals(final JsonNumber expected,
      final JsonNumber actual, final String message) {
    if (actual.equals(expected)) {
      return true;
    } else {
      LOGGER.info("   " + message);
      LOGGER.info("     Expected: " + expected.toString());
      LOGGER.info("     Actual:   " + actual.toString());
      return false;
    }
  }

  public static boolean assertEquals(final JsonValue expected,
      final JsonValue actual, final String message) {
    switch (expected.getValueType()) {
    case OBJECT:
      return assertEquals((JsonObject) expected, (JsonObject) actual, message);
    case ARRAY:
      return assertEquals((JsonArray) expected, (JsonArray) actual, message);
    case STRING:
      return assertEquals((JsonString) expected, (JsonString) actual, message);
    case NUMBER:
      return assertEquals((JsonNumber) expected, (JsonNumber) actual, message);
    case TRUE:
    case FALSE:
      if (expected == actual) {
        return true;
      } else {
        LOGGER.info("   " + message);
        LOGGER.info("     Expected: " + expected.toString());
        LOGGER.info("     Actual:   " + actual.toString());
        return false;
      }
    default:
      if (actual.equals(expected)) {
        return true;
      } else {
        LOGGER.info("   " + message);
        LOGGER.info("     Expected: " + expected.toString());
        LOGGER.info("     Actual:   " + actual.toString());
        return false;
      }
    }
  }

  public static boolean assertEquals(final JsonValue expected,
      final JsonValue actual) {
    return assertEquals(expected, actual, "JSON mismatch");
  }

  /**
   * Operation result expected.
   * 
   * @param expected
   *          Expected modified JSON value.
   * @param actual
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  public static boolean assertEquals(final Object expected,
      final String actual) {
    if (actual == null) {
      return true;
    }
    try {
      switch (JsonValueType.getType(expected)) {
      case String:
        String exp = '\"' + (String) expected + '\"';
        return exp.equals(actual);
      case Integer:
        return Integer.parseInt(actual) == (Integer) expected;
      case Long:
        return Long.parseLong(actual) == (Long) expected;
      case BigInteger:
        return (new BigInteger(actual)).equals(expected);
      case Double:
        return Double.parseDouble(actual) == (Double) expected;
      case BigDecimal:
        return (new BigDecimal(actual)).equals(expected);
      case Boolean:
        return Boolean.parseBoolean(actual) == (Boolean) expected;
      case JsonValue:
        try (final JsonReader reader = Json
            .createReader(new StringReader(actual))) {
          final JsonValue actVal = reader.readValue();
          return assertEquals((JsonValue) expected, actVal);
        }
      case Null:
        try (final JsonReader reader = Json
            .createReader(new StringReader(actual))) {
          final JsonValue actVal = reader.readValue();
          return assertEquals(JsonValue.NULL, actVal);
        }
      default:
        throw new IllegalArgumentException(
            "Value does not match known JSON value type");
      }
    } catch (NumberFormatException ex) {
      return true;
    }
  }

  /**
   * Convert provided JSON value to human readable String.
   * 
   * @param value
   *          Value to be converted.
   * @return JSON value as human readable String.
   */
  public static String valueToString(final JsonValue value) {
    switch (value.getValueType()) {
    case OBJECT:
      return JSONP_Util.toStringJsonObject((JsonObject) value);
    case ARRAY:
      return JSONP_Util.toStringJsonArray((JsonArray) value);
    case STRING:
      return ((JsonString) value).getString();
    case NUMBER:
      return ((JsonNumber) value).toString();
    case TRUE:
      return Boolean.toString(true);
    case FALSE:
      return Boolean.toString(false);
    case NULL:
      return "null";
    default:
      throw new IllegalArgumentException("Unknown value type");
    }
  }

}

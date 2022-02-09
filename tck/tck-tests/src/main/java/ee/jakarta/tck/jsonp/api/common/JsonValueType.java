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

package ee.jakarta.tck.jsonp.api.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import jakarta.json.JsonValue;

// $Id$
/**
 * Identifiers of types used as JSON value.
 */
public enum JsonValueType {
  /** JsonValue is String. */
  String,
  /** JsonValue is Integer. */
  Integer,
  /** JsonValue is Long. */
  Long,
  /** JsonValue is BigInteger. */
  BigInteger,
  /** JsonValue is Double. */
  Double,
  /** JsonValue is BigDecimal. */
  BigDecimal,
  /** JsonValue is Boolean. */
  Boolean,
  /** JsonValue is common JSON value. */
  JsonValue,
  /** JsonValue is null. */
  Null;

  /** Size of this enumeration. */
  private static final int SIZE = JsonValueType.values().length;

  /** Name to value {@code Map}. */
  private static final Map<String, JsonValueType> VALUES = new HashMap<>(SIZE);
  // Name to value Map initialization.
  static {
    for (int i = 0; i < SIZE; i++)
      VALUES.put(JsonValueType.values()[i].name(), JsonValueType.values()[i]);
  }

  /**
   * Returns JSON value identifier for provided class.
   * 
   * @param c
   *          JSON value class.
   * @return JSON value identifier for provided class.
   */
  public static JsonValueType getType(final Class c) {
    JsonValueType type = VALUES.get(c.getSimpleName());
    if (type != null) {
      return type;
    }
    // Interface hierarchy is a tree so stack machine is required to go trough
    // it.
    final LinkedList<Class> stack = new LinkedList();
    for (final Class i : c.getInterfaces()) {
      stack.push(i);
    }
    while (!stack.isEmpty()) {
      final Class i = stack.pop();
      type = VALUES.get(i.getSimpleName());
      if (type != null) {
        return type;
      }
      for (final Class j : i.getInterfaces()) {
        stack.push(j);
      }
    }
    throw new IllegalArgumentException(
        "Unsupported JSON value type: " + c.getSimpleName());
  }

  /**
   * Returns JSON value identifier for provided value.
   * 
   * @param value
   *          JSON value.
   * @return JSON value identifier for provided class.
   */
  public static JsonValueType getType(final Object value) {
    return value != null ? getType(value.getClass()) : Null;
  }

  /**
   * Convert provided value to {@code String} which is part of JSON document.
   * 
   * @param value
   *          Value be be converted to {@code String}.
   * @return Value converted to {@code String}.
   */
  public static String toStringValue(final Object value) {
    switch (getType(value)) {
    case String:
      return '"' + ((String) value) + '"';
    case Integer:
      return ((Integer) value).toString();
    case Long:
      return ((Long) value).toString();
    case BigInteger:
      return ((BigInteger) value).toString();
    case Double:
      return ((Double) value).toString();
    case BigDecimal:
      return ((BigDecimal) value).toString();
    case Boolean:
      return ((Boolean) value).toString();
    case JsonValue:
      return JsonAssert.valueToString((JsonValue) value);
    case Null:
      return SimpleValues.NULL;
    default:
      throw new IllegalArgumentException(
          "Unsupported JSON value type: " + value.getClass().getSimpleName());
    }
  }

}

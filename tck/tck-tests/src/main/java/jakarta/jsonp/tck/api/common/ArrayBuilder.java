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

package jakarta.jsonp.tck.api.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonValue;

// $Id$
/**
 * {@link JsonArrayBuilder} manipulation helper.
 */
public class ArrayBuilder {

  /**
   * Add {@code value} to provided JSON array builder.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param value
   *          Value to be added at the end of the array.
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder add(final JsonArrayBuilder builder,
      final Object value) {
    switch (JsonValueType.getType(value)) {
    case String:
      return builder.add((String) value);
    case Integer:
      return builder.add(((Integer) value).intValue());
    case Long:
      return builder.add(((Long) value).intValue());
    case BigInteger:
      return builder.add(((BigInteger) value));
    case Double:
      return builder.add(((Double) value).doubleValue());
    case BigDecimal:
      return builder.add(((BigDecimal) value));
    case Boolean:
      return builder.add(((Boolean) value).booleanValue());
    case JsonValue:
      return builder.add((JsonValue) value);
    case Null:
      return builder.addNull();
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Add {@code value} at specified index to provided JSON array builder.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param index
   *          Array index of value to be added.
   * @param value
   *          Value to be added at the end of the array.
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder add(final JsonArrayBuilder builder,
      final int index, final Object value) {
    switch (JsonValueType.getType(value)) {
    case String:
      return builder.add(index, (String) value);
    case Integer:
      return builder.add(index, ((Integer) value).intValue());
    case Long:
      return builder.add(index, ((Long) value).longValue());
    case BigInteger:
      return builder.add(index, ((BigInteger) value));
    case Double:
      return builder.add(index, ((Double) value).doubleValue());
    case BigDecimal:
      return builder.add(index, ((BigDecimal) value));
    case Boolean:
      return builder.add(index, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.add(index, (JsonValue) value);
    case Null:
      return builder.addNull(index);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Add {@code null} to provided JSON array builder. Every call shall throw an
   * exception which depends on selected type.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param type
   *          Type of method argument to use..
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder add(final JsonArrayBuilder builder,
      final JsonValueType type) {
    switch (type) {
    case String:
      return builder.add((String) null);
    case Integer:
      throw new UnsupportedOperationException(
          "Value null is not supported for int");
    case Long:
      throw new UnsupportedOperationException(
          "Value null is not supported for long");
    case BigInteger:
      return builder.add((BigInteger) null);
    case Double:
      throw new UnsupportedOperationException(
          "Value null is not supported for double");
    case BigDecimal:
      return builder.add((BigDecimal) null);
    case Boolean:
      return builder.add((Boolean) null);
    case JsonValue:
      return builder.add((JsonValue) null);
    case Null:
      throw new UnsupportedOperationException(
          "Value null is not supported for addNull()");
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Add {@code null} to provided JSON array builder. Every call shall throw an
   * exception which depends on selected type.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param index
   *          Array index of value to be added.
   * @param type
   *          Type of method argument to use..
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder add(final JsonArrayBuilder builder,
      final int index, final JsonValueType type) {
    switch (type) {
    case String:
      return builder.add(index, (String) null);
    case Integer:
      throw new UnsupportedOperationException(
          "Value null is not supported for int");
    case Long:
      throw new UnsupportedOperationException(
          "Value null is not supported for long");
    case BigInteger:
      return builder.add(index, (BigInteger) null);
    case Double:
      throw new UnsupportedOperationException(
          "Value null is not supported for double");
    case BigDecimal:
      return builder.add(index, (BigDecimal) null);
    case Boolean:
      return builder.add(index, (Boolean) null);
    case JsonValue:
      return builder.add(index, (JsonValue) null);
    case Null:
      throw new UnsupportedOperationException(
          "Value null is not supported for addNull()");
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Set {@code value} at specified index to provided JSON array builder.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param index
   *          Array index of value to be added.
   * @param value
   *          Value to be set at the end of the array.
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder set(final JsonArrayBuilder builder,
      final int index, final Object value) {
    switch (JsonValueType.getType(value)) {
    case String:
      return builder.set(index, (String) value);
    case Integer:
      return builder.set(index, ((Integer) value).intValue());
    case Long:
      return builder.set(index, ((Long) value).longValue());
    case BigInteger:
      return builder.set(index, ((BigInteger) value));
    case Double:
      return builder.set(index, ((Double) value).doubleValue());
    case BigDecimal:
      return builder.set(index, ((BigDecimal) value));
    case Boolean:
      return builder.set(index, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.set(index, (JsonValue) value);
    case Null:
      return builder.setNull(index);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Set {@code null} to provided JSON array builder. Every call shall throw an
   * exception which depends on selected type.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param index
   *          Array index of value to be added.
   * @param type
   *          Type of method argument to use..
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder set(final JsonArrayBuilder builder,
      final int index, final JsonValueType type) {
    switch (type) {
    case String:
      return builder.set(index, (String) null);
    case Integer:
      throw new UnsupportedOperationException(
          "Value null is not supported for int");
    case Long:
      throw new UnsupportedOperationException(
          "Value null is not supported for long");
    case BigInteger:
      return builder.set(index, (BigInteger) null);
    case Double:
      throw new UnsupportedOperationException(
          "Value null is not supported for double");
    case BigDecimal:
      return builder.set(index, (BigDecimal) null);
    case Boolean:
      return builder.set(index, (Boolean) null);
    case JsonValue:
      return builder.set(index, (JsonValue) null);
    case Null:
      throw new UnsupportedOperationException(
          "Value null is not supported for addNull()");
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Remove {@code value} at specified index from provided JSON array builder.
   * 
   * @param builder
   *          Target JSON array builder.
   * @param index
   *          Array index of value to be added.
   * @return JSON array builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonArrayBuilder remove(final JsonArrayBuilder builder,
      final int index) {
    return builder.remove(index);
  }

}

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
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

// $Id$
/**
 * {@link JsonObjectBuilder} manipulation helper.
 */
public class ObjectBuilder {

  /**
   * Add {@code value} with {@code name} to provided JSON object builder.
   * 
   * @param builder
   *          Target JSON object builder.
   * @param name
   *          Name of value to be added.
   * @param value
   *          Value to be added.
   * @return JSON object builder containing new {@code value}.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonObjectBuilder add(final JsonObjectBuilder builder,
      final String name, final Object value) {
    switch (JsonValueType.getType(value)) {
    case String:
      return builder.add(name, (String) value);
    case Integer:
      return builder.add(name, ((Integer) value).intValue());
    case Long:
      return builder.add(name, ((Long) value).intValue());
    case BigInteger:
      return builder.add(name, ((BigInteger) value));
    case Double:
      return builder.add(name, ((Double) value).doubleValue());
    case BigDecimal:
      return builder.add(name, ((BigDecimal) value));
    case Boolean:
      return builder.add(name, ((Boolean) value).booleanValue());
    case JsonValue:
      return builder.add(name, (JsonValue) value);
    case Null:
      return builder.addNull(name);
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

}

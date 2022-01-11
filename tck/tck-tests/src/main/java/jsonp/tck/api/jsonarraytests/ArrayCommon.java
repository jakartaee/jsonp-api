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

package jsonp.tck.api.jsonarraytests;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonArray}
 * and {@link JsonArrayBuilder}.
 */
public abstract class ArrayCommon {

  /**
   * Create and initialize array builder to contain single value. Child class
   * callback.
   * 
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  protected abstract JsonArrayBuilder createArrayBuilder(final Object value);

  /**
   * Create and initialize array builder to contain single value. Child class
   * callback.
   * 
   * @param index
   *          Position in the array where value is added.
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  protected abstract JsonArrayBuilder createArrayBuilder(final int index,
      final Object value);

  /**
   * Update array builder to contain next value. Child class callback.
   * 
   * @param builder
   *          JSON array builder to update.
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder with value updated.
   */
  protected abstract JsonArrayBuilder updateOperationBuilder(
      final JsonArrayBuilder builder, final Object value);

  /**
   * Update array builder to contain next value. Child class callback.
   * 
   * @param builder
   *          JSON array builder to update.
   * @param index
   *          Position in the array where value is added.
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder with value updated.
   */
  protected abstract JsonArrayBuilder updateOperationBuilder(
      final JsonArrayBuilder builder, final int index, final Object value);

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

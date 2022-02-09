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

package ee.jakarta.tck.jsonp.api.jsonarraytests;

import ee.jakarta.tck.jsonp.api.common.ArrayBuilder;
import ee.jakarta.tck.jsonp.api.common.JsonAssert;
import ee.jakarta.tck.jsonp.api.common.JsonIO;
import ee.jakarta.tck.jsonp.api.common.JsonValueType;
import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonArrayBuilder} API remove() methods added in JSON-P 1.1.<br>
 */
public class ArrayBuildRemove extends ArrayCommon {

  private static final Logger LOGGER = Logger.getLogger(ArrayBuildRemove.class.getName());

  /**
   * Creates an instance of {@link JsonArrayBuilder} API remove() methods added
   * in JSON-P 1.1 test.
   */
  ArrayBuildRemove() {
    super();
  }

  /**
   * {@link JsonArrayBuilder} API remove() methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonArrayBuilder API remove() methods added in JSON-P 1.1.");
    LOGGER.info("JsonArrayBuilder API remove() methods added in JSON-P 1.1.");
    testRemove(result);
    testRemoveOutOfBounds(result);
    return result;
  }

  /**
   * Test {@code default JsonArrayBuilder remove(int, Object)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemove(final TestResult result) {
    final Object[] values = new Object[] { SimpleValues.OBJ_VALUE, // remove(int,JsonValue)
        SimpleValues.STR_VALUE, // remove(int,String)
        SimpleValues.INT_VALUE, // remove(int,int)
        SimpleValues.LNG_VALUE, // remove(int,long)
        SimpleValues.DBL_VALUE, // remove(int,double)
        SimpleValues.BIN_VALUE, // remove(int,BigInteger)
        SimpleValues.BDC_VALUE, // remove(int,BigDecimal)
        SimpleValues.BOOL_VALUE // remove(int,boolean)
    };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - remove(int," + typeName + ")");
      final String json = "[]";
      final JsonValue check = JsonIO.read(json);
      JsonArrayBuilder builder = ArrayBuilder.add(Json.createArrayBuilder(),
          value);
      builder = updateOperationBuilder(builder, 0, null);
      final JsonValue out = builder.build();
      if (operationFailed(check, out)) {
        result.fail("remove(" + typeName + ")", "Builder output "
            + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder remove(int, Object)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveOutOfBounds(final TestResult result) {
    final Object[] values = new Object[] { SimpleValues.OBJ_VALUE, // remove(int,JsonValue)
        SimpleValues.STR_VALUE, // remove(int,String)
        SimpleValues.INT_VALUE, // remove(int,int)
        SimpleValues.LNG_VALUE, // remove(int,long)
        SimpleValues.DBL_VALUE, // remove(int,double)
        SimpleValues.BIN_VALUE, // remove(int,BigInteger)
        SimpleValues.BDC_VALUE, // remove(int,BigDecimal)
        SimpleValues.BOOL_VALUE // remove(int,boolean)
    };
    final int[] indexes = new int[] { -1, 2, 3 };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - remove(int," + typeName + ")");
      final String json = "[" + JsonValueType.toStringValue(value) + "]";
      // Add value into the array for the first time to het array of size 1.
      JsonArrayBuilder builder = ArrayBuilder.add(Json.createArrayBuilder(),
          value);
      for (int index : indexes) {
        try {
          // Add value on out of bounds index
          builder = updateOperationBuilder(builder, index, null);
          result.fail("remove(int," + typeName + ")",
              "Calling method with out of bounds index=" + index
                  + " argument shall throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
          LOGGER.info("    - Expected exception for index=" + index + ": "
              + e.getMessage());
        } catch (Throwable t) {
          result.fail("remove(int,(" + typeName + ")null)",
              "Calling method with with out of bounds index=" + index
                  + " argument shall throw IndexOutOfBoundsException, not "
                  + t.getClass().getSimpleName());
        }
      }
    }
  }

  /**
   * Create and initialize array builder. Unsupported method call for remove()
   * method.
   * 
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  @Override
  protected JsonArrayBuilder createArrayBuilder(Object value) {
    throw new UnsupportedOperationException(
        "Method remove is not implemented.");
  }

  /**
   * Create and initialize array builder. Unsupported method call for remove()
   * method.
   * 
   * @param index
   *          Position in the array where value is added.
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  @Override
  protected JsonArrayBuilder createArrayBuilder(final int index,
      final Object value) {
    throw new UnsupportedOperationException(
        "Method remove is not implemented.");
  }

  /**
   * Update array builder. Unsupported method call for remove() method.
   * 
   * @param builder
   *          JSON array builder to update.
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder with value updated.
   */
  @Override
  protected JsonArrayBuilder updateOperationBuilder(JsonArrayBuilder builder,
      Object value) {
    throw new UnsupportedOperationException(
        "Method remove is not implemented.");
  }

  /**
   * Update array builder with value removal at specified index. Child class
   * callback.
   * 
   * @param builder
   *          JSON array builder to update.
   * @param index
   *          Position in the array where value is added.
   * @param value
   *          JSON value argument is ignored.
   * @return JSON array builder with value updated.
   */
  @Override
  protected JsonArrayBuilder updateOperationBuilder(
      final JsonArrayBuilder builder, final int index, final Object value) {
    return ArrayBuilder.remove(builder, index);
  }

}

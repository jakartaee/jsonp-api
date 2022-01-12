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

package jsonp.tck.api.jsonarraytests;

import jsonp.tck.api.common.ArrayBuilder;
import jsonp.tck.api.common.JsonIO;
import jsonp.tck.api.common.JsonValueType;
import jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jsonp.tck.api.common.JsonAssert;
import jsonp.tck.api.common.SimpleValues;

import java.util.logging.Logger;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonArrayBuilder} API set() methods added in JSON-P 1.1.<br>
 */
public class ArrayBuildSet extends ArrayCommon {

  private static final Logger LOGGER = Logger.getLogger(ArrayBuildSet.class.getName());

  /**
   * Creates an instance of {@link JsonArrayBuilder} API set() methods added in
   * JSON-P 1.1 test.
   */
  ArrayBuildSet() {
    super();
  }

  /**
   * {@link JsonArrayBuilder} API set() methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonArrayBuilder API set() methods added in JSON-P 1.1.");
    LOGGER.info("JsonArrayBuilder API set() methods added in JSON-P 1.1.");
    testSet(result);
    testSetOutOfBounds(result);
    testSetNullBuilder(result);
    testSetNull(result);
    testSetNullOutOfBounds(result);
    testSetArrayBuilder(result);
    testSetArrayBuilderNull(result);
    testSetArrayBuilderOutOfBounds(result);
    testSetObjectBuilder(result);
    testSetObjectBuilderNull(result);
    testSetObjectBuilderOutOfBounds(result);
    return result;
  }

  /**
   * Test {@code default JsonArrayBuilder set(int, Object)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSet(final TestResult result) {
    final Object[] values = new Object[] { SimpleValues.OBJ_VALUE, // set(int,JsonValue)
        SimpleValues.STR_VALUE, // set(int,String)
        SimpleValues.INT_VALUE, // set(int,int)
        SimpleValues.LNG_VALUE, // set(int,long)
        SimpleValues.DBL_VALUE, // set(int,double)
        SimpleValues.BIN_VALUE, // set(int,BigInteger)
        SimpleValues.BDC_VALUE, // set(int,BigDecimal)
        SimpleValues.BOOL_VALUE // set(int,boolean)
    };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - set(int," + typeName + ")");
      final String json = "[" + JsonValueType.toStringValue(value) + "]";
      final JsonValue check = JsonIO.read(json);
      final JsonArrayBuilder builder = updateOperationBuilder(
          Json.createArrayBuilder().add(SimpleValues.DEF_OBJ_VALUE), 0, value);
      final JsonValue out = builder.build();
      if (operationFailed(check, out)) {
        result.fail("set(" + typeName + ")", "Builder output "
            + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int, Object)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetOutOfBounds(final TestResult result) {
    final Object[] values = new Object[] { SimpleValues.OBJ_VALUE, // set(int,JsonValue)
        SimpleValues.STR_VALUE, // set(int,String)
        SimpleValues.INT_VALUE, // set(int,int)
        SimpleValues.LNG_VALUE, // set(int,long)
        SimpleValues.DBL_VALUE, // set(int,double)
        SimpleValues.BIN_VALUE, // set(int,BigInteger)
        SimpleValues.BDC_VALUE, // set(int,BigDecimal)
        SimpleValues.BOOL_VALUE // set(int,boolean)
    };
    final int[] indexes = new int[] { -1, 2, 3 };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - set(int," + typeName + ")");
      final String json = "[" + JsonValueType.toStringValue(value) + "]";
      JsonArrayBuilder builder = ArrayBuilder.add(Json.createArrayBuilder(),
          SimpleValues.DEF_OBJ_VALUE);
      for (int index : indexes) {
        try {
          builder = updateOperationBuilder(
              Json.createArrayBuilder().add(SimpleValues.DEF_OBJ_VALUE), index, value);
        } catch (IndexOutOfBoundsException e) {
          LOGGER.info("    - Expected exception for index=" + index + ": "
              + e.getMessage());
        } catch (Throwable t) {
          result.fail("set(int," + typeName + ")",
              "Calling method with with out of bounds index=" + index
                  + " argument shall throw IndexOutOfBoundsException, not "
                  + t.getClass().getSimpleName());
        }
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int, Object)} method on
   * {@code String} array with null value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetNullBuilder(final TestResult result) {
    final JsonValueType[] types = new JsonValueType[] { JsonValueType.JsonValue, // set(int,(JsonValue)null)
        JsonValueType.String, // set(int,(String)null)
        JsonValueType.BigInteger, // set(int,(BigInteger)null)
        JsonValueType.BigDecimal // set(int,(BigDecimal)null)
    };
    for (JsonValueType type : types) {
      final String typeName = type.name();
      LOGGER.info(" - set(int,(" + typeName + ")null)");
      try {
        ArrayBuilder.set(Json.createArrayBuilder(), 0, type);
        result.fail("set(int,(" + typeName + ")null)",
            "Calling method with null argument shall throw NullPointerException");
      } catch (NullPointerException e) {
        LOGGER.info("    - Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("set(int,(" + typeName + ")null)",
            "Calling method with null argument shall throw NullPointerException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder setNull(int)} method on {@code String}
   * array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetNull(final TestResult result) {
    LOGGER.info(" - setNull(int)");
    final Object value = null;
    final String json = "[" + JsonValueType.toStringValue(null) + "]";
    final JsonValue check = JsonIO.read(json);
    final JsonArrayBuilder builder = ArrayBuilder
        .set(Json.createArrayBuilder().add(SimpleValues.DEF_OBJ_VALUE), 0, value);
    final JsonValue out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("setNull(int)", "Builder output " + JsonAssert.valueToString(out)
          + " value shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Test {@code default JsonArrayBuilder setNull(int)} method on {@code String}
   * array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetNullOutOfBounds(final TestResult result) {
    final int[] indexes = new int[] { -1, 2, 3 };
    LOGGER.info(" - setNull(int)");
    final Object value = null;
    JsonArrayBuilder builder = ArrayBuilder.add(Json.createArrayBuilder(),
        value);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        builder = updateOperationBuilder(builder, index, value);
        result.fail("setNull(int)", "Calling method with out of bounds index="
            + index + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("setNull(int)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,JsonArrayBuilder)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetArrayBuilder(final TestResult result) {
    LOGGER.info(" - set(int,JsonArrayBuilder)");
    final JsonValue check = JsonIO
        .read("[[" + JsonValueType.toStringValue(SimpleValues.STR_VALUE_1) + ","
            + JsonValueType.toStringValue(SimpleValues.STR_VALUE_2) + ","
            + JsonValueType.toStringValue(SimpleValues.STR_VALUE_3) + ","
            + JsonValueType.toStringValue(SimpleValues.STR_VALUE_4) + "]]");
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_5);
    final JsonArrayBuilder arg = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_1)
        .add(SimpleValues.STR_VALUE_2).add(SimpleValues.STR_VALUE_3).add(SimpleValues.STR_VALUE_4);
    verifySetBuilder(result, check, 0, arg, in);
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,(JsonArrayBuilder)null)}
   * method on {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetArrayBuilderNull(final TestResult result) {
    LOGGER.info(" - set(int,(JsonArrayBuilder)null)");
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.DEF_VALUE);
    final JsonArrayBuilder arg = null;
    try {
      in.set(0, arg);
      result.fail("set(int,(JsonArrayBuilder)null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("set(int,(JsonArrayBuilder)null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,JsonArrayBuilder)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetArrayBuilderOutOfBounds(final TestResult result) {
    LOGGER.info(" - set(int,JsonArrayBuilder)");
    final int[] indexes = new int[] { -1, 5, 6 };
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_1)
        .add(SimpleValues.STR_VALUE_2).add(SimpleValues.STR_VALUE_3).add(SimpleValues.STR_VALUE_4);
    final JsonArrayBuilder arg = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_5);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        in.set(index, arg);
        result.fail("set(int,JsonArrayBuilder)",
            "Calling method with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("set(int,JsonArrayBuilder)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,JsonObjectBuilder)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetObjectBuilder(final TestResult result) {
    LOGGER.info(" - set(int,JsonObjectBuilder)");
    final JsonValue check = JsonIO
        .read("[{" + JsonValueType.toStringValue(SimpleValues.STR_NAME) + ":"
            + JsonValueType.toStringValue(SimpleValues.STR_VALUE) + "}]");
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_1);
    final JsonObjectBuilder arg = Json.createObjectBuilder().add(SimpleValues.STR_NAME,
        SimpleValues.STR_VALUE);
    verifySetBuilder(result, check, 0, arg, in);
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,(JsonObjectBuilder)null)}
   * method on {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetObjectBuilderNull(final TestResult result) {
    LOGGER.info(" - set(int,(JsonObjectBuilder)null)");
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.DEF_VALUE);
    final JsonObjectBuilder arg = null;
    try {
      in.set(0, arg);
      result.fail("set(int,(JsonObjectBuilder)null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("set(int,(JsonObjectBuilder)null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code default JsonArrayBuilder set(int,JsonObjectBuilder)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testSetObjectBuilderOutOfBounds(final TestResult result) {
    LOGGER.info(" - set(int,JsonObjectBuilder)");
    final int[] indexes = new int[] { -1, 5, 6 };
    final JsonArrayBuilder in = Json.createArrayBuilder().add(SimpleValues.STR_VALUE_1)
        .add(SimpleValues.STR_VALUE_2).add(SimpleValues.STR_VALUE_3).add(SimpleValues.STR_VALUE_4);
    final JsonObjectBuilder arg = Json.createObjectBuilder().add(SimpleValues.STR_NAME,
        SimpleValues.STR_VALUE);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        in.set(index, arg);
        result.fail("set(int,JsonObjectBuilder)",
            "Calling method with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("set(int,JsonObjectBuilder)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test helper: Verify
   * {@code default JsonArrayBuilder set(int,JsonArrayBuilder)} method on
   * provided builders.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param src
   *          Source builder (the one to be added).
   * @param target
   *          Target builder (to which to add).
   */
  private void verifySetBuilder(final TestResult result, final JsonValue check,
      final int index, final JsonArrayBuilder src,
      final JsonArrayBuilder target) {
    final JsonArray out = target.set(index, src).build();
    if (operationFailed(check, out)) {
      result.fail("set(int,JsonArrayBuilder)", "Output builder "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Test helper: Verify
   * {@code default JsonArrayBuilder set(int,JsonObjectBuilder)} method on
   * provided builders.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param src
   *          Source builder (the one to be added).
   * @param target
   *          Target builder (to which to add).
   */
  private void verifySetBuilder(final TestResult result, final JsonValue check,
      final int index, final JsonObjectBuilder src,
      final JsonArrayBuilder target) {
    final JsonArray out = target.set(index, src).build();
    if (operationFailed(check, out)) {
      result.fail("set(int,JsonObjectBuilder)", "Output builder "
          + JsonAssert.valueToString(out) + " value shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Create and initialize array builder to contain single value. Unsupported
   * method call for set() method.
   * 
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  @Override
  protected JsonArrayBuilder createArrayBuilder(Object value) {
    throw new UnsupportedOperationException("Method set is not implemented.");
  }

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
  @Override
  protected JsonArrayBuilder createArrayBuilder(final int index,
      final Object value) {
    return ArrayBuilder.set(Json.createArrayBuilder(), index, value);
  }

  /**
   * Update array builder to contain next value. Unsupported method call for
   * set() method.
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
    throw new UnsupportedOperationException("Method set is not implemented.");
  }

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
  @Override
  protected JsonArrayBuilder updateOperationBuilder(
      final JsonArrayBuilder builder, final int index, final Object value) {
    return ArrayBuilder.set(builder, index, value);
  }

}

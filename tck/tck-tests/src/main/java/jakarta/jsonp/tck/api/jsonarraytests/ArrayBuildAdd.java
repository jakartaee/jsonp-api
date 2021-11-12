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

package jakarta.jsonp.tck.api.jsonarraytests;

import jakarta.jsonp.tck.api.common.ArrayBuilder;
import jakarta.jsonp.tck.api.common.JsonIO;
import jakarta.jsonp.tck.api.common.JsonValueType;
import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests:
 * {@link JsonArrayBuilder} API add() methods added in JSON-P 1.1.<br>
 */
public class ArrayBuildAdd extends ArrayCommon {

  private static final Logger LOGGER = Logger.getLogger(ArrayBuildAdd.class.getName());

  /**
   * Creates an instance of {@link JsonArrayBuilder} API add() methods added in
   * JSON-P 1.1 test.
   */
  ArrayBuildAdd() {
    super();
  }

  /**
   * Test {@link JsonArrayBuilder} API add() methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonArrayBuilder API add() methods added in JSON-P 1.1.");
    LOGGER.info("JsonArrayBuilder API add() methods added in JSON-P 1.1.");
    testAdd(result);
    testAddNullBuilder(result);
    testAddOutOfBounds(result);
    testAddNull(result);
    testAddNullOutOfBounds(result);
    testAddArrayBuilder(result);
    testAddArrayBuilderNull(result);
    testAddArrayBuilderOutOfBounds(result);
    testAddObjectBuilder(result);
    testAddObjectBuilderNull(result);
    testAddObjectBuilderOutOfBounds(result);
    testAddAllString(result);
    testAddAllInt(result);
    testAddAllBool(result);
    testAddAllObject(result);
    testAddAllNull(result);
    return result;
  }

  /**
   * Test {@code default JsonArrayBuilder add(int, Object)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAdd(final TestResult result) {
    final Object[] values = new Object[] { OBJ_VALUE, // add(int,JsonValue)
        STR_VALUE, // add(int,String)
        INT_VALUE, // add(int,int)
        LNG_VALUE, // add(int,long)
        DBL_VALUE, // add(int,double)
        BIN_VALUE, // add(int,BigInteger)
        BDC_VALUE, // add(int,BigDecimal)
        BOOL_VALUE // add(int,boolean)
    };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - add(int," + typeName + ")");
      final String json = "[" + JsonValueType.toStringValue(value) + "]";
      final JsonValue check = JsonIO.read(json);
      final JsonArrayBuilder builder = createArrayBuilder(0, value);
      final JsonValue out = builder.build();
      if (operationFailed(check, out)) {
        result.fail("add(" + typeName + ")", "Builder output "
            + valueToString(out) + " value shall be " + valueToString(check));
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int, Object)} method on {@code String}
   * array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddOutOfBounds(final TestResult result) {
    final Object[] values = new Object[] { OBJ_VALUE, // add(int,JsonValue)
        STR_VALUE, // add(int,String)
        INT_VALUE, // add(int,int)
        LNG_VALUE, // add(int,long)
        DBL_VALUE, // add(int,double)
        BIN_VALUE, // add(int,BigInteger)
        BDC_VALUE, // add(int,BigDecimal)
        BOOL_VALUE // add(int,boolean)
    };
    final int[] indexes = new int[] { -1, 2, 3 };
    for (Object value : values) {
      final String typeName = JsonValueType.getType(value).name();
      LOGGER.info(" - add(int," + typeName + ")");
      final String json = "[" + JsonValueType.toStringValue(value) + "]";
      // Add value into the array for the first time to het array of size 1.
      JsonArrayBuilder builder = createArrayBuilder(value);
      for (int index : indexes) {
        try {
          // Add value on out of bounds index
          builder = updateOperationBuilder(builder, index, value);
          result.fail("add(int," + typeName + ")",
              "Calling method with out of bounds index=" + index
                  + " argument shall throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
          LOGGER.info("    - Expected exception for index=" + index + ": "
              + e.getMessage());
        } catch (Throwable t) {
          result.fail("add(int,(" + typeName + ")null)",
              "Calling method with with out of bounds index=" + index
                  + " argument shall throw IndexOutOfBoundsException, not "
                  + t.getClass().getSimpleName());
        }
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int, Object)} method on {@code String}
   * array with null value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddNullBuilder(final TestResult result) {
    final JsonValueType[] types = new JsonValueType[] { JsonValueType.JsonValue, // add(int,(JsonValue)null)
        JsonValueType.String, // add(int,(String)null)
        JsonValueType.BigInteger, // add(int,(BigInteger)null)
        JsonValueType.BigDecimal // add(int,(BigDecimal)null)
    };
    for (JsonValueType type : types) {
      final String typeName = type.name();
      LOGGER.info(" - add(int,(" + typeName + ")null)");
      try {
        ArrayBuilder.add(Json.createArrayBuilder(), 0, type);
        result.fail("add(int,(" + typeName + ")null)",
            "Calling method with null argument shall throw NullPointerException");
      } catch (NullPointerException e) {
        LOGGER.info("    - Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("add(int,(" + typeName + ")null)",
            "Calling method with null argument shall throw NullPointerException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder addNull(int)} method on {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddNull(final TestResult result) {
    LOGGER.info(" - addNull(int)");
    final Object value = null;
    final String json = "[" + JsonValueType.toStringValue(value) + "]";
    final JsonValue check = JsonIO.read(json);
    final JsonArrayBuilder builder = createArrayBuilder(0, value);
    final JsonValue out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("addNull(int)", "Builder output " + valueToString(out)
          + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test {@code JsonArrayBuilder addNull(int)} method on {@code String} array
   * with index being out of range ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddNullOutOfBounds(final TestResult result) {
    final int[] indexes = new int[] { -1, 2, 3 };
    LOGGER.info(" - addNull(int)");
    final Object value = null;
    JsonArrayBuilder builder = createArrayBuilder(value);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        builder = updateOperationBuilder(builder, index, value);
        result.fail("addNull(int)", "Calling method with out of bounds index="
            + index + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("addNull(int)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int,JsonArrayBuilder)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddArrayBuilder(final TestResult result) {
    LOGGER.info(" - add(int,JsonArrayBuilder)");
    final JsonValue checkBeg = JsonIO
        .read("[[" + JsonValueType.toStringValue(STR_VALUE_1) + ","
            + JsonValueType.toStringValue(STR_VALUE_2) + ","
            + JsonValueType.toStringValue(STR_VALUE_3) + ","
            + JsonValueType.toStringValue(STR_VALUE_4) + "],"
            + JsonValueType.toStringValue(STR_VALUE_5) + "]");
    final JsonValue checkEnd = JsonIO
        .read("[" + JsonValueType.toStringValue(STR_VALUE_1) + ",["
            + JsonValueType.toStringValue(STR_VALUE_2) + ","
            + JsonValueType.toStringValue(STR_VALUE_3) + ","
            + JsonValueType.toStringValue(STR_VALUE_4) + ","
            + JsonValueType.toStringValue(STR_VALUE_5) + "]]");
    final JsonArrayBuilder inBeg = createArrayBuilder(STR_VALUE_5);
    final JsonArrayBuilder inEnd = createArrayBuilder(STR_VALUE_1);
    final JsonArrayBuilder argBeg = createArrayBuilder(STR_VALUE_1)
        .add(STR_VALUE_2).add(STR_VALUE_3).add(STR_VALUE_4);
    final JsonArrayBuilder argEnd = createArrayBuilder(STR_VALUE_2)
        .add(STR_VALUE_3).add(STR_VALUE_4).add(STR_VALUE_5);
    verifyAddBuilder(result, checkBeg, 0, argBeg, inBeg);
    verifyAddBuilder(result, checkEnd, 1, argEnd, inEnd);
  }

  /**
   * Test {@code JsonArrayBuilder add(int,(JsonArrayBuilder)null)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddArrayBuilderNull(final TestResult result) {
    LOGGER.info(" - add(int,(JsonArrayBuilder)null)");
    final JsonArrayBuilder in = createArrayBuilder(DEF_VALUE);
    final JsonArrayBuilder arg = null;
    try {
      in.add(0, arg);
      result.fail("add(int,(JsonArrayBuilder)null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("add(int,(JsonArrayBuilder)null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int,JsonArrayBuilder)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddArrayBuilderOutOfBounds(final TestResult result) {
    LOGGER.info(" - add(int,JsonArrayBuilder)");
    final int[] indexes = new int[] { -1, 5, 6 };
    final JsonArrayBuilder in = createArrayBuilder(STR_VALUE_1).add(STR_VALUE_2)
        .add(STR_VALUE_3).add(STR_VALUE_4);
    final JsonArrayBuilder arg = createArrayBuilder(STR_VALUE_5);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        in.add(index, arg);
        result.fail("add(int,JsonArrayBuilder)",
            "Calling method with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("add(int,JsonArrayBuilder)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int,JsonObjectBuilder)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddObjectBuilder(final TestResult result) {
    LOGGER.info(" - add(int,JsonObjectBuilder)");
    final JsonValue checkBeg = JsonIO
        .read("[{" + JsonValueType.toStringValue(STR_NAME) + ":"
            + JsonValueType.toStringValue(STR_VALUE) + "},"
            + JsonValueType.toStringValue(STR_VALUE_1) + "]");
    final JsonValue checkEnd = JsonIO
        .read("[" + JsonValueType.toStringValue(STR_VALUE_1) + ",{"
            + JsonValueType.toStringValue(STR_NAME) + ":"
            + JsonValueType.toStringValue(STR_VALUE) + "}]");
    final JsonArrayBuilder inBeg = createArrayBuilder(STR_VALUE_1);
    final JsonArrayBuilder inEnd = createArrayBuilder(STR_VALUE_1);
    final JsonObjectBuilder argBeg = Json.createObjectBuilder().add(STR_NAME,
        STR_VALUE);
    final JsonObjectBuilder argEnd = Json.createObjectBuilder().add(STR_NAME,
        STR_VALUE);
    verifyAddBuilder(result, checkBeg, 0, argBeg, inBeg);
    verifyAddBuilder(result, checkEnd, 1, argEnd, inEnd);
  }

  /**
   * Test {@code JsonArrayBuilder add(int,(JsonObjectBuilder)null)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddObjectBuilderNull(final TestResult result) {
    LOGGER.info(" - add(int,(JsonObjectBuilder)null)");
    final JsonArrayBuilder in = createArrayBuilder(DEF_VALUE);
    final JsonObjectBuilder arg = null;
    try {
      in.add(0, arg);
      result.fail("add(int,(JsonObjectBuilder)null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("add(int,(JsonObjectBuilder)null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonArrayBuilder add(int,JsonObjectBuilder)} method on
   * {@code String} array with index being out of range
   * ({@code index < 0 || index > array size}).
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddObjectBuilderOutOfBounds(final TestResult result) {
    LOGGER.info(" - add(int,JsonObjectBuilder)");
    final int[] indexes = new int[] { -1, 5, 6 };
    final JsonArrayBuilder in = createArrayBuilder(STR_VALUE_1).add(STR_VALUE_2)
        .add(STR_VALUE_3).add(STR_VALUE_4);
    final JsonObjectBuilder arg = Json.createObjectBuilder().add(STR_NAME,
        STR_VALUE);
    for (int index : indexes) {
      try {
        // Add value on out of bounds index
        in.add(index, arg);
        result.fail("add(int,JsonObjectBuilder)",
            "Calling method with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException");
      } catch (IndexOutOfBoundsException e) {
        LOGGER.info("    - Expected exception for index=" + index + ": "
            + e.getMessage());
      } catch (Throwable t) {
        result.fail("add(int,JsonObjectBuilder)",
            "Calling method with with out of bounds index=" + index
                + " argument shall throw IndexOutOfBoundsException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * {@code String} array.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddAllString(final TestResult result) {
    LOGGER.info(" - addAll(JsonArrayBuilder) for String array");
    final JsonArray check = createSimpleStringArray5();
    final String[] strings = new String[] { STR_VALUE_1, STR_VALUE_2,
        STR_VALUE_3, STR_VALUE_4, STR_VALUE_5 };
    verifyAddAll(result, check, strings);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * {@code int) array. @param result Test suite result.
   */
  private void testAddAllInt(final TestResult result) {
    LOGGER.info(" - addAll(JsonArrayBuilder) for int array");
    final JsonArray check = createSimpleIntArray5();
    final Integer[] ints = new Integer[] { INT_VALUE_1, INT_VALUE_2,
        INT_VALUE_3, INT_VALUE_4, INT_VALUE_5 };
    verifyAddAll(result, check, ints);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * {@code boolean) array. @param result Test suite result.
   */
  private void testAddAllBool(final TestResult result) {
    LOGGER.info(" - addAll(JsonArrayBuilder) for boolean array");
    final JsonArray check = createSimpleBoolArray5();
    final Boolean[] bools = new Boolean[] { BOOL_FALSE, BOOL_TRUE, BOOL_TRUE,
        BOOL_FALSE, BOOL_TRUE };
    verifyAddAll(result, check, bools);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * {@code JsonObject) array. @param result Test suite result.
   */
  private void testAddAllObject(final TestResult result) {
    LOGGER.info(" - addAll(JsonArrayBuilder) for JsonObject array");
    final JsonArray check = createSimpleObjectArray5();
    final JsonObject[] bools = new JsonObject[] { OBJ_VALUE_1, OBJ_VALUE_2,
        OBJ_VALUE_3, OBJ_VALUE_4, OBJ_VALUE_5 };
    verifyAddAll(result, check, bools);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * {@code null} builder argument.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddAllNull(final TestResult result) {
    LOGGER.info(" - addAll(JsonArrayBuilder) for null builder argument");
    JsonArrayBuilder builder = Json.createArrayBuilder();
    try {
      builder.addAll((JsonArrayBuilder) null);
      result.fail("addAll(null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      LOGGER.info("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("addAll(null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test helper: Verify
   * {@code default JsonArrayBuilder addAll(JsonArrayBuilder)} method on
   * provided array.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param values
   *          Values used to build JSON array builder.
   */
  private void verifyAddAll(final TestResult result, final JsonArray check,
      final Object[] values) {
    JsonArrayBuilder builderIn = Json.createArrayBuilder();
    for (Object value : values) {
      builderIn = updateOperationBuilder(builderIn, value);
    }
    final JsonArrayBuilder builderOut = Json.createArrayBuilder();
    builderOut.addAll(builderIn);
    final JsonArray out = builderOut.build();
    if (operationFailed(check, out)) {
      result.fail("addAll(JsonArrayBuilder)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test helper: Verify
   * {@code default JsonArrayBuilder add(int,JsonArrayBuilder)} method on
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
  private void verifyAddBuilder(final TestResult result, final JsonValue check,
      final int index, final JsonArrayBuilder src,
      final JsonArrayBuilder target) {
    final JsonArray out = target.add(index, src).build();
    if (operationFailed(check, out)) {
      result.fail("add(int,JsonArrayBuilder)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test helper: Verify
   * {@code default JsonArrayBuilder add(int,JsonObjectBuilder)} method on
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
  private void verifyAddBuilder(final TestResult result, final JsonValue check,
      final int index, final JsonObjectBuilder src,
      final JsonArrayBuilder target) {
    final JsonArray out = target.add(index, src).build();
    if (operationFailed(check, out)) {
      result.fail("add(int,JsonObjectBuilder)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Create and initialize array builder to contain single value. Child class
   * callback.
   * 
   * @param value
   *          JSON value stored in the builder. Value of {@code null} is stored
   *          as JSON {@code null} keyword.
   * @return JSON array builder containing value.
   */
  @Override
  protected JsonArrayBuilder createArrayBuilder(final Object value) {
    return ArrayBuilder.add(Json.createArrayBuilder(), value);
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
    return ArrayBuilder.add(Json.createArrayBuilder(), index, value);
  }

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
  @Override
  protected JsonArrayBuilder updateOperationBuilder(
      final JsonArrayBuilder builder, final Object value) {
    return ArrayBuilder.add(builder, value);
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
    return ArrayBuilder.add(builder, index, value);
  }

}

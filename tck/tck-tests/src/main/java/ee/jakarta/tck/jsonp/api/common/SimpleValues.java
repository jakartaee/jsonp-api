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

package ee.jakarta.tck.jsonp.api.common;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonPatch;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Build various simple values for testing.
 */
public class SimpleValues {

  private static final Logger LOGGER = Logger.getLogger(SimpleValues.class.getName());

  /** Name of JSON {@code String} value used in tests. */
  public static final String STR_NAME = "address";

  /** Path of JSON {@code String} value used in tests. */
  public static final String STR_PATH = "/" + STR_NAME;

  /** JSON {@code String} value used in tests. */
  public static final String STR_VALUE = "In a galaxy far far away";

  /** JSON {@code String} second value used in tests. */
  public static final String STR_VALUE2 = "In a land of myth";

  /** Name of JSON {@code int} value used in tests. */
  public static final String INT_NAME = "age";

  /** Path of JSON {@code int} value used in tests. */
  public static final String INT_PATH = "/" + INT_NAME;

  /** JSON {@code int} value used in tests. */
  public static final int INT_VALUE = 42;

  /** JSON {@code int} second value used in tests. */
  public static final int INT_VALUE2 = 32;

  /** Name of JSON {@code boolean} value used in tests. */
  public static final String BOOL_NAME = "married";

  /** Path of JSON {@code boolean} value used in tests. */
  public static final String BOOL_PATH = "/" + BOOL_NAME;

  /** JSON {@code boolean} value used in tests. */
  public static final boolean BOOL_VALUE = true;

  /** JSON {@code boolean} second value used in tests. */
  public static final boolean BOOL_VALUE2 = false;

  /** Name of JSON {@code JsonObject} value used in tests. */
  public static final String OBJ_NAME = "wife";

  /** Path of JSON {@code JsonObject} value used in tests. */
  public static final String OBJ_PATH = "/" + OBJ_NAME;

  /** JSON {@code JsonObject} value used in tests. */
  public static final JsonObject OBJ_VALUE = createSimpleObject(
      new String[] { "name", "age" }, new Object[] { "Sarah Connor", 32 });

  /** JSON {@code JsonObject} second value used in tests. */
  public static final JsonObject OBJ_VALUE2 = createSimpleObject(
      new String[] { "name", "age" }, new Object[] { "Kyle Reese", 35 });

  /** Name of JSON default value stored in simple object. */
  public static final String DEF_NAME = "name";

  /** Name of JSON default value stored in simple object. */
  public static final String DEF_PATH = "/" + DEF_NAME;

  /** JSON default value stored in simple object. */
  public static final String DEF_VALUE = "John Smith";

  /** Name of JSON object stored in compound object. */
  public static final String DEF_OBJ_NAME = "child";

  /** Path of JSON object stored in compound object. */
  public static final String DEF_OBJ_PATH = "/child";

  /** JSON default object stored in simple object. */
  public static final JsonObject DEF_OBJ_VALUE = createSimpleObject(
      new String[] { "name", "age" }, new Object[] { "John Connor", 6 });

  /** JSON default object stored in simple object with name changed. */
  public static final JsonObject DEF_OBJ_VALUE2 = createSimpleObject(
      new String[] { "name", "age" }, new Object[] { "John Smith", 6 });

  /** Value of JSON {@code String} array at index 0. */
  public static final String STR_VALUE_1 = "First value";

  /** Value of JSON {@code String} array at index 1. */
  public static final String STR_VALUE_2 = "Second value";

  /** Value of JSON {@code String} array at index 2. */
  public static final String STR_VALUE_3 = "Third value";

  /** Value of JSON {@code String} array at index 3. */
  public static final String STR_VALUE_4 = "Fourth value";

  /** Value of JSON {@code String} array at index 4. */
  public static final String STR_VALUE_5 = "Fifth value";

  /** Value of JSON {@code String} array at index 5. */
  public static final String STR_VALUE_6 = "Sixth value";

  /** Value of JSON {@code String} array at index 6. */
  public static final String STR_VALUE_7 = "Seventh value";

  /** Value of JSON {@code int} array at index 0. */
  public static final int INT_VALUE_1 = 1;

  /** Value of JSON {@code int} array at index 1. */
  public static final int INT_VALUE_2 = 2;

  /** Value of JSON {@code int} array at index 2. */
  public static final int INT_VALUE_3 = 3;

  /** Value of JSON {@code int} array at index 3. */
  public static final int INT_VALUE_4 = 4;

  /** Value of JSON {@code int} array at index 4. */
  public static final int INT_VALUE_5 = 5;

  /** Value of JSON {@code JsonObject} array at index 0. */
  public static final JsonObject OBJ_VALUE_1 = createSimpleObject(
      new String[] { "first" }, new String[] { STR_VALUE_1 });

  /** Value of JSON {@code JsonObject} array at index 1. */
  public static final JsonObject OBJ_VALUE_2 = createSimpleObject(
      new String[] { "second" }, new String[] { STR_VALUE_2 });

  /** Value of JSON {@code JsonObject} array at index 2. */
  public static final JsonObject OBJ_VALUE_3 = createSimpleObject(
      new String[] { "third" }, new String[] { STR_VALUE_3 });

  /** Value of JSON {@code JsonObject} array at index 3. */
  public static final JsonObject OBJ_VALUE_4 = createSimpleObject(
      new String[] { "fourth" }, new String[] { STR_VALUE_4 });

  /** Value of JSON {@code JsonObject} array at index 4. */
  public static final JsonObject OBJ_VALUE_5 = createSimpleObject(
      new String[] { "fifth" }, new String[] { STR_VALUE_5 });

  /** JSON {@code boolean} value: {@code true}. */
  public static final boolean BOOL_TRUE = true;

  /** JSON {@code boolean} value: {@code false}. */
  public static final boolean BOOL_FALSE = false;

  /** JSON {@code long} value used in tests. */
  public static final long LNG_VALUE = Long.MAX_VALUE - 42;

  /** JSON {@code BigInteger} value used in tests. */
  public static final BigInteger BIN_VALUE = new BigInteger(
      "123456789012345678901234567890");

  /** JSON {@code double} value used in tests. */
  public static final double DBL_VALUE = 0x1.f5c926b3a0942P+1014;

  /** JSON {@code BigDecimal} value used in tests. */
  public static final BigDecimal BDC_VALUE = new BigDecimal(
      new BigInteger("1234567890123456789012345678901234567890"), 10);

  /** Message content: null String. */
  public static final String NULL = "null";

  /**
   * Creates empty JSON object.
   * 
   * @return Empty JSON object.
   */
  public static JsonObject createEmptyObject() {
    return Json.createObjectBuilder().build();
  }

  /**
   * Creates empty JSON object after ADD STR_NAME STR_VALUE operation.
   * 
   * @return Empty JSON object after ADD STR_NAME STR_VALUE operation.
   */
  public static JsonObject createSimpleObjectStr() {
    return Json.createObjectBuilder().add(STR_NAME, STR_VALUE).build();
  }

  /**
   * Creates empty JSON object after ADD INT_NAME INT_VALUE operation.
   * 
   * @return Empty JSON object after ADD INT_NAME INT_VALUE operation.
   */
  public static JsonObject createSimpleObjectInt() {
    return Json.createObjectBuilder().add(INT_NAME, INT_VALUE).build();
  }

  /**
   * Creates empty JSON object after ADD BOOL_NAME BOOL_VALUE operation.
   * 
   * @return Empty JSON object after ADD BOOL_NAME BOOL_VALUE operation.
   */
  public static JsonObject createSimpleObjectBool() {
    return Json.createObjectBuilder().add(BOOL_NAME, BOOL_VALUE).build();
  }

  /**
   * Creates empty JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   * 
   * @return Empty JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   */
  public static JsonObject createSimpleObjectObject() {
    return Json.createObjectBuilder().add(OBJ_NAME, OBJ_VALUE).build();
  }

  /**
   * Creates JSON patch to remove STR_NAME attribute from object.
   * 
   * @return JSON patch
   */
  public static JsonObject createPatchRemoveStr() {
    return Json.createObjectBuilder().add(STR_NAME, JsonValue.NULL).build();
  }

  /**
   * Creates JSON patch to remove INT_NAME attribute from object.
   * 
   * @return JSON patch
   */
  public static JsonObject createPatchRemoveInt() {
    return Json.createObjectBuilder().add(INT_NAME, JsonValue.NULL).build();
  }

  /**
   * Creates JSON patch to remove BOOL_NAME attribute from object.
   * 
   * @return JSON patch
   */
  public static JsonObject createPatchRemoveBool() {
    return Json.createObjectBuilder().add(BOOL_NAME, JsonValue.NULL).build();
  }

  /**
   * Creates JSON patch to remove OBJ_NAME attribute from object.
   * 
   * @return JSON patch
   */
  public static JsonObject createPatchRemoveObject() {
    return Json.createObjectBuilder().add(OBJ_NAME, JsonValue.NULL).build();
  }

  /**
   * Creates empty JSON array.
   * 
   * @return Empty JSON array.
   */
  public static JsonArray createEmptyArray() {
    return Json.createArrayBuilder().build();
  }

  /**
   * Creates empty JSON array after ADD STR_VALUE operation.
   * 
   * @return Empty JSON array after ADD STR_VALUE operation.
   */
  public static JsonArray createEmptyArrayWithStr() {
    return Json.createArrayBuilder().add(STR_VALUE).build();
  }

  /**
   * Creates empty JSON array after ADD INT_VALUE operation.
   * 
   * @return Empty JSON array after ADD INT_VALUE operation.
   */
  public static JsonArray createEmptyArrayWithInt() {
    return Json.createArrayBuilder().add(INT_VALUE).build();
  }

  /**
   * Creates empty JSON array after ADD BOOL_VALUE operation.
   * 
   * @return Empty JSON array after ADD BOOL_VALUE operation.
   */
  public static JsonArray createEmptyArrayWithBool() {
    return Json.createArrayBuilder().add(BOOL_VALUE).build();
  }

  /**
   * Creates empty JSON array after ADD OBJ_VALUE operation.
   * 
   * @return Empty JSON array after ADD OBJ_VALUE operation.
   */
  public static JsonArray createEmptyArrayWithObject() {
    return Json.createArrayBuilder().add(OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON object.
   * 
   * @return Simple JSON object.
   */
  public static JsonObject createSimpleObject() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE).build();
  }

  /**
   * Creates simple JSON object after ADD STR_NAME STR_VALUE operation.
   * 
   * @return Simple JSON object after ADD STR_NAME STR_VALUE operation.
   */
  public static JsonObject createSimpleObjectWithStr() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(STR_NAME, STR_VALUE).build();
  }

  /**
   * Creates simple JSON object after REPLACE STR_NAME STR_VALUE operation.
   * 
   * @return Simple JSON object after REPLACE STR_NAME STR_VALUE operation.
   */
  public static JsonObject createSimpleObjectReplaceStr() {
    return Json.createObjectBuilder().add(STR_NAME, STR_VALUE2).build();
  }

  /**
   * Creates simple JSON object after MOVE STR_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after MOVE STR_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectMoveStr() {
    return Json.createObjectBuilder().add(DEF_NAME, STR_VALUE).build();
  }

  /**
   * Creates simple JSON object after COPY STR_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after COPY STR_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectCopyStr() {
    return Json.createObjectBuilder().add(STR_NAME, STR_VALUE)
        .add(DEF_NAME, STR_VALUE).build();
  }

  /**
   * Creates simple JSON object after ADD INT_NAME INT_VALUE operation.
   * 
   * @return Simple JSON object after ADD INT_NAME INT_VALUE operation.
   */
  public static JsonObject createSimpleObjectWithInt() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(INT_NAME, INT_VALUE).build();
  }

  /**
   * Creates simple JSON object after REPLACE INT_NAME INT_VALUE operation.
   * 
   * @return Simple JSON object after REPLACE INT_NAME INT_VALUE operation.
   */
  public static JsonObject createSimpleObjectReplaceInt() {
    return Json.createObjectBuilder().add(INT_NAME, INT_VALUE2).build();
  }

  /**
   * Creates simple JSON object after MOVE INT_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after MOVE INT_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectMoveInt() {
    return Json.createObjectBuilder().add(DEF_NAME, INT_VALUE).build();
  }

  /**
   * Creates simple JSON object after COPY INT_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after COPY INT_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectCopyInt() {
    return Json.createObjectBuilder().add(INT_NAME, INT_VALUE)
        .add(DEF_NAME, INT_VALUE).build();
  }

  /**
   * Creates simple JSON object after ADD BOOL_NAME BOOL_VALUE operation.
   * 
   * @return Simple JSON object after ADD BOOL_NAME BOOL_VALUE operation.
   */
  public static JsonObject createSimpleObjectWithBool() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(BOOL_NAME, BOOL_VALUE).build();
  }

  /**
   * Creates simple JSON object after REPLACE BOOL_NAME BOOL_VALUE operation.
   * 
   * @return Simple JSON object after REPLACE BOOL_NAME BOOL_VALUE operation.
   */
  public static JsonObject createSimpleObjectReplaceBool() {
    return Json.createObjectBuilder().add(BOOL_NAME, BOOL_VALUE2).build();
  }

  /**
   * Creates simple JSON object after MOVE BOOL_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after MOVE BOOL_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectMoveBool() {
    return Json.createObjectBuilder().add(DEF_NAME, BOOL_VALUE).build();
  }

  /**
   * Creates simple JSON object after COPY BOOL_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after COPY BOOL_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectCopyBool() {
    return Json.createObjectBuilder().add(BOOL_NAME, BOOL_VALUE)
        .add(DEF_NAME, BOOL_VALUE).build();
  }

  /**
   * Creates simple JSON object.
   * 
   * @return Simple JSON object.
   */
  public static JsonObject createCompoundObject() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   * 
   * @return Simple JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   */
  public static JsonObject createCompoundObjectWithObject() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE).add(OBJ_NAME, OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   * 
   * @return Simple JSON object after ADD OBJ_NAME OBJ_VALUE operation.
   */
  public static JsonObject createCompoundObjectReplaceObject() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE).add(OBJ_NAME, OBJ_VALUE2).build();
  }

  /**
   * Creates simple JSON object after MOVE DEF_PATH DEF_OBJ_PATH+DEF_PATH.
   * 
   * @return Simple JSON object.
   */
  public static JsonObject createCompoundObjectMoveValue() {
    return Json.createObjectBuilder().add(DEF_OBJ_NAME, DEF_OBJ_VALUE2).build();
  }

  /**
   * Creates simple JSON object after COPY DEF_PATH DEF_OBJ_PATH+DEF_PATH.
   * 
   * @return Simple JSON object.
   */
  public static JsonObject createCompoundObjectCopyValue() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE2).build();
  }

  /**
   * Creates simple JSON object after MOVE OBJ_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after MOVE OBJ_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectMoveObject() {
    return Json.createObjectBuilder().add(DEF_NAME, OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON object after COPY OBJ_NAME DEF_NAME operation.
   * 
   * @return Simple JSON object after COPY OBJ_NAME DEF_NAME operation.
   */
  public static JsonObject createSimpleObjectCopyObject() {
    return Json.createObjectBuilder().add(OBJ_NAME, OBJ_VALUE)
        .add(DEF_NAME, OBJ_VALUE).build();
  }

  /**
   * Creates compound JSON object after ADD DEF_OBJ_NAME,
   * createSimpleStringArray5().
   * 
   * @return compound JSON object with ADD operation applied.
   */
  public static JsonObject createCompoundObjectWithObjectReplaced() {
    return Json.createObjectBuilder().add(DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, createSimpleStringArray5()).build();
  }

  /**
   * Creates simple JSON object with provided {@code name[i]} and
   * {@code value[i]} pairs.
   * 
   * @param names
   *          Names of JSON values to be added. Pairs of {@code names[i]} and
   *          {@code values[i]} are used for add operations.
   * @param values
   *          JSON values to be added for specified names.
   * @return Simple JSON object.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonObject createSimpleObject(final String[] names,
      final Object[] values) {
    if (names.length != values.length) {
      throw new IllegalArgumentException(
          "Number of paths does not match number of indexes");
    }
    JsonObjectBuilder builder = Json.createObjectBuilder();
    for (int i = 0; i < names.length; i++) {
      switch (JsonValueType.getType(values[i].getClass())) {
      case String:
        builder = builder.add(names[i], (String) values[i]);
        break;
      case Integer:
        builder = builder.add(names[i], ((Integer) values[i]).intValue());
        break;
      case Boolean:
        builder = builder.add(names[i], ((Boolean) values[i]).booleanValue());
        break;
      case JsonValue:
        builder = builder.add(names[i], (JsonValue) values[i]);
        break;
      default:
        throw new IllegalArgumentException(
            "Value does not match known JSON value type");
      }
    }
    return builder.build();
  }

  /**
   * Creates simple JSON array with specified {@code String} values.
   * 
   * @param values
   *          JSON array {@code String} values.
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray(final String... values) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    if (values != null) {
      for (final String value : values) {
        builder = builder.add(value);
      }
    }
    return builder.build();
  }

  /**
   * Creates simple JSON array with single {@code String}.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray1() {
    return Json.createArrayBuilder().add(STR_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD STR_VALUE operation before existing
   * element.
   * 
   * @return Simple JSON array after ADD STR_VALUE operation before existing
   *         element.
   */
  public static JsonArray createSimpleStringArrayWithStrBefore() {
    return Json.createArrayBuilder().add(STR_VALUE).add(STR_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD STR_VALUE operation after existing
   * element.
   * 
   * @return Simple JSON array after ADD STR_VALUE operation after existing
   *         element.
   */
  public static JsonArray createSimpleStringArrayWithStrAfter() {
    return Json.createArrayBuilder().add(STR_VALUE_1).add(STR_VALUE).build();
  }

  /**
   * Creates simple JSON array after REPLACE STR_VALUE operation on existing
   * element.
   * 
   * @return Simple JSON array after REPLACE STR_VALUE operation on existing
   *         element.
   */
  public static JsonArray createSimpleStringArrayReplaceStr() {
    return Json.createArrayBuilder().add(STR_VALUE).build();
  }

  /**
   * Creates simple JSON array with two {@code String} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray2() {
    return Json.createArrayBuilder().add(STR_VALUE_2).add(STR_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with two {@code String} values in reversed order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray2R() {
    return Json.createArrayBuilder().add(STR_VALUE_4).add(STR_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with two {@code String} values to be inserted
   * into another array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringInnerArray2() {
    return Json.createArrayBuilder().add(STR_VALUE_6).add(STR_VALUE_7).build();
  }

  /**
   * Creates simple JSON array with three {@code String} values as a result of
   * COPY 2nd element to the beginning of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray2Copy1to0() {
    return Json.createArrayBuilder().add(STR_VALUE_4).add(STR_VALUE_2)
        .add(STR_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with three {@code String} values as a result of
   * COPY 1st element to the end of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray2Copy0to2() {
    return Json.createArrayBuilder().add(STR_VALUE_2).add(STR_VALUE_4)
        .add(STR_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with three {@code String} values as a result of
   * COPY 1st element to the middle of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createStringArray2Copy0to1() {
    return Json.createArrayBuilder().add(STR_VALUE_2).add(STR_VALUE_2)
        .add(STR_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with five {@code String} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleStringArray5() {
    return Json.createArrayBuilder().add(STR_VALUE_1).add(STR_VALUE_2)
        .add(STR_VALUE_3).add(STR_VALUE_4).add(STR_VALUE_5).build();
  }

  /**
   * Creates simple JSON array with five {@code String} values in reversed
   * order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleStringArray5R() {
    return Json.createArrayBuilder().add(STR_VALUE_5).add(STR_VALUE_4)
        .add(STR_VALUE_3).add(STR_VALUE_2).add(STR_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD [STR_VALUE_6, STR_VALUE_7] operation
   * between two existing elements.
   * 
   * @return Simple JSON array after ADD operation.
   */
  public static JsonArray createStringArray2WithStringArrayInTheMiddle() {
    return Json.createArrayBuilder().add(STR_VALUE_2)
        .add(createStringInnerArray2()).add(STR_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with single {@code int}.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray1() {
    return Json.createArrayBuilder().add(INT_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD INT_VALUE operation before existing
   * element.
   * 
   * @return Simple JSON array after ADD INT_VALUE operation before existing
   *         element.
   */
  public static JsonArray createSimpleIntArrayWithIntBefore() {
    return Json.createArrayBuilder().add(INT_VALUE).add(INT_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD INT_VALUE operation after existing
   * element.
   * 
   * @return Simple JSON array after ADD INT_VALUE operation after existing
   *         element.
   */
  public static JsonArray createSimpleIntArrayWithIntAfter() {
    return Json.createArrayBuilder().add(INT_VALUE_1).add(INT_VALUE).build();
  }

  /**
   * Creates simple JSON array after REPLACE INT_VALUE operation on existing
   * element.
   * 
   * @return Simple JSON array after REPLACE INT_VALUE operation on existing
   *         element.
   */
  public static JsonArray createSimpleIntArrayReplaceInt() {
    return Json.createArrayBuilder().add(INT_VALUE).build();
  }

  /**
   * Creates simple JSON array with two {@code int} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray2() {
    return Json.createArrayBuilder().add(INT_VALUE_2).add(INT_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with two {@code int} values in reversed order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray2R() {
    return Json.createArrayBuilder().add(INT_VALUE_4).add(INT_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with three {@code int} values as a result of COPY
   * 2nd element to the beginning of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray2Copy1to0() {
    return Json.createArrayBuilder().add(INT_VALUE_4).add(INT_VALUE_2)
        .add(INT_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with three {@code int} values as a result of COPY
   * 1st element to the end of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray2Copy0to2() {
    return Json.createArrayBuilder().add(INT_VALUE_2).add(INT_VALUE_4)
        .add(INT_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with three {@code int} values as a result of COPY
   * 1st element to the middle of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createIntArray2Copy0to1() {
    return Json.createArrayBuilder().add(INT_VALUE_2).add(INT_VALUE_2)
        .add(INT_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with five {@code int} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleIntArray5() {
    return Json.createArrayBuilder().add(INT_VALUE_1).add(INT_VALUE_2)
        .add(INT_VALUE_3).add(INT_VALUE_4).add(INT_VALUE_5).build();
  }

  /**
   * Creates simple JSON array with five {@code int} values in reversed order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleIntArray5R() {
    return Json.createArrayBuilder().add(INT_VALUE_5).add(INT_VALUE_4)
        .add(INT_VALUE_3).add(INT_VALUE_2).add(INT_VALUE_1).build();
  }

  /**
   * Creates simple JSON array with single {@code boolean}.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray1() {
    return Json.createArrayBuilder().add(BOOL_TRUE).build();
  }

  /**
   * Creates simple JSON array after ADD BOOL_FALSE operation before existing
   * element (BOOL_TRUE).
   * 
   * @return Simple JSON array after ADD BOOL_FALSE operation before existing
   *         element.
   */
  public static JsonArray createSimpleBoolArrayWithBoolBefore() {
    return Json.createArrayBuilder().add(BOOL_FALSE).add(BOOL_TRUE).build();
  }

  /**
   * Creates simple JSON array after ADD BOOL_FALSE operation after existing
   * element (BOOL_TRUE).
   * 
   * @return Simple JSON array after ADD BOOL_FALSE operation after existing
   *         element.
   */
  public static JsonArray createSimpleBoolArrayWithBoolAfter() {
    return Json.createArrayBuilder().add(BOOL_TRUE).add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array after REPLACE BOOL_FALSE operation on existing
   * element.
   * 
   * @return Simple JSON array after REPLACE BOOL_FALSE operation on existing
   *         element.
   */
  public static JsonArray createSimpleBoolArrayReplaceBool() {
    return Json.createArrayBuilder().add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array with two {@code boolean} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray2() {
    return Json.createArrayBuilder().add(BOOL_TRUE).add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array with two {@code boolean} values in reverse order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray2R() {
    return Json.createArrayBuilder().add(BOOL_FALSE).add(BOOL_TRUE).build();
  }

  /**
   * Creates simple JSON array with three {@code boolean} values as a result of
   * COPY 2nd element to the beginning of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray2Copy1to0() {
    return Json.createArrayBuilder().add(BOOL_FALSE).add(BOOL_TRUE)
        .add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array with three {@code boolean} values as a result of
   * COPY 1st element to the end of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray2Copy0to2() {
    return Json.createArrayBuilder().add(BOOL_TRUE).add(BOOL_FALSE)
        .add(BOOL_TRUE).build();
  }

  /**
   * Creates simple JSON array with three {@code boolean} values as a result of
   * COPY 1st element to the middle of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createBoolArray2Copy0to1() {
    return Json.createArrayBuilder().add(BOOL_TRUE).add(BOOL_TRUE)
        .add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array with five {@code boolean} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleBoolArray5() {
    return Json.createArrayBuilder().add(BOOL_FALSE).add(BOOL_TRUE)
        .add(BOOL_TRUE).add(BOOL_FALSE).add(BOOL_TRUE).build();
  }

  /**
   * Creates simple JSON array with five {@code boolean} values in reversed
   * order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleBoolArray5R() {
    return Json.createArrayBuilder().add(BOOL_TRUE).add(BOOL_FALSE)
        .add(BOOL_TRUE).add(BOOL_TRUE).add(BOOL_FALSE).build();
  }

  /**
   * Creates simple JSON array with single {@code JsonObject}.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray1() {
    return Json.createArrayBuilder().add(OBJ_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD OBJ_VALUE operation before existing
   * element.
   * 
   * @return Simple JSON array after ADD OBJ_VALUE operation before existing
   *         element.
   */
  public static JsonArray createSimpleObjectArrayWithObjectBefore() {
    return Json.createArrayBuilder().add(OBJ_VALUE).add(OBJ_VALUE_1).build();
  }

  /**
   * Creates simple JSON array after ADD OBJ_VALUE operation after existing
   * element.
   * 
   * @return Simple JSON array after ADD OBJ_VALUE operation after existing
   *         element.
   */
  public static JsonArray createSimpleObjectArrayWithObjectAfter() {
    return Json.createArrayBuilder().add(OBJ_VALUE_1).add(OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON array after REPLACE OBJ_VALUE operation on existing
   * element.
   * 
   * @return Simple JSON array after REPLACE OBJ_VALUE operation on existing
   *         element.
   */
  public static JsonArray createSimpleObjectArrayReplaceObject() {
    return Json.createArrayBuilder().add(OBJ_VALUE).build();
  }

  /**
   * Creates simple JSON array with two {@code JsonObject} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray2() {
    return Json.createArrayBuilder().add(OBJ_VALUE_2).add(OBJ_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with two {@code JsonObject} values in reverse
   * order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray2R() {
    return Json.createArrayBuilder().add(OBJ_VALUE_4).add(OBJ_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with three {@code JsonObject} values as a result
   * of COPY 2nd element to the beginning of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray2Copy1to0() {
    return Json.createArrayBuilder().add(OBJ_VALUE_4).add(OBJ_VALUE_2)
        .add(OBJ_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with three {@code JsonObject} values as a result
   * of COPY 1st element to the end of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray2Copy0to2() {
    return Json.createArrayBuilder().add(OBJ_VALUE_2).add(OBJ_VALUE_4)
        .add(OBJ_VALUE_2).build();
  }

  /**
   * Creates simple JSON array with three {@code JsonObject} values as a result
   * of COPY 1st element to the middle of the array.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createObjectArray2Copy0to1() {
    return Json.createArrayBuilder().add(OBJ_VALUE_2).add(OBJ_VALUE_2)
        .add(OBJ_VALUE_4).build();
  }

  /**
   * Creates simple JSON array with five {@code JsonObject} values.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleObjectArray5() {
    return Json.createArrayBuilder().add(OBJ_VALUE_1).add(OBJ_VALUE_2)
        .add(OBJ_VALUE_3).add(OBJ_VALUE_4).add(OBJ_VALUE_5).build();
  }

  /**
   * Creates simple JSON array with five {@code JsonObject} values in reversed
   * order.
   * 
   * @return Newly created JSON array.
   */
  public static JsonArray createSimpleObjectArray5R() {
    return Json.createArrayBuilder().add(OBJ_VALUE_5).add(OBJ_VALUE_4)
        .add(OBJ_VALUE_3).add(OBJ_VALUE_2).add(OBJ_VALUE_1).build();
  }

  /**
   * Convert provided {@code Object} instance to {@code JsonValue}.
   * 
   * @param value
   *          {@code Object} instance to be converted.
   * @return JsonValue instance containing provided {@code Object};
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  public static JsonValue toJsonValue(final Object value) {
    if (value == null) {
      return JsonValue.NULL;
    }
    switch (JsonValueType.getType(value.getClass())) {
    case String:
      return Json.createValue((String) value);
    case Integer:
      return Json.createValue(((Integer) value).intValue());
    case Long:
      return Json.createValue(((Long) value).longValue());
    case BigInteger:
      return Json.createValue((BigInteger) value);
    case Double:
      return Json.createValue(((Double) value).doubleValue());
    case BigDecimal:
      return Json.createValue((BigDecimal) value);
    case Boolean:
      return ((Boolean) value).booleanValue() ? JsonValue.TRUE
          : JsonValue.FALSE;
    case JsonValue:
      return (JsonValue) value;
    case Null:
      return JsonValue.NULL;
    default:
      throw new IllegalArgumentException(
          "Value does not match known JSON value type");
    }
  }

  /**
   * Convert JSON value to {@code String} containing value stored in JSON
   * format.
   * 
   * @param value
   *          JSON value to be converted.
   * @return Provided value stored in JSON format.
   */
  public static String jsonData(final JsonValue value) {
    final StringWriter strWriter = new StringWriter();
    try (final JsonWriter writer = Json.createWriter(strWriter)) {
      writer.write(value);
    } catch (JsonException ex) {
      LOGGER.info(
          "Could not initialize JSON data: " + ex.getLocalizedMessage());
      throw ex;
    }
    return strWriter.toString();
  }

  /**
   * Apply patch on provided JSON {@code value} using {@code apply()} method of
   * {@code JsonPatch}.
   * 
   * @param patch
   *          {@code JsonPatch} with patch operations.
   * @param value
   *          JSON {@code value} to be patched.
   * @return Result of JSON {@code value} patching.
   */
  public static JsonValue patchApply(final JsonPatch patch,
      final JsonValue value) {
    switch (value.getValueType()) {
    case OBJECT:
      return patch.apply((JsonObject) value);
    case ARRAY:
      return patch.apply((JsonArray) value);
    default:
      throw new IllegalArgumentException(
          "Unsupported JSON value type to be pached");
    }
  }

  /**
   * Convert {@code boolean} value to {@code JsonValue}.
   * 
   * @param value
   *          Source {@code boolean} value.
   * @return {@code JsonValue.TRUE} if provided {@code value} is {@code true} or
   *         {@code JsonValue.FALSE} otherwise.
   */
  public static JsonValue booleanValue(final boolean value) {
    return value ? JsonValue.TRUE : JsonValue.FALSE;
  }

  /**
   * Creates an instance of RFC 6902 operation test.
   */
  private SimpleValues() {
  }

}

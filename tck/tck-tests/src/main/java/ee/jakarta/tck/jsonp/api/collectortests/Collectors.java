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

package ee.jakarta.tck.jsonp.api.collectortests;

import ee.jakarta.tck.jsonp.api.common.JsonAssert;
import ee.jakarta.tck.jsonp.api.common.SimpleValues;
import ee.jakarta.tck.jsonp.api.common.TestResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collector;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonCollectors;
import jakarta.json.stream.JsonParser;

// $Id$
/**
 * JavaScript Object Notation (JSON) Pointer compatibility tests.<br>
 * Test {@link jakarta.json.stream.JsonCollectors} class implementation. This
 * class was added to resolve
 * {@see <a href="https://java.net/jira/browse/JSON_PROCESSING_SPEC-68">RFE:
 * Support JSON queries using JDK's stream operations</a>}.
 */
public class Collectors {

  private static final Logger LOGGER = Logger.getLogger(Collectors.class.getName());

  /** Tests input data with JsonArray instances. */
  private static final JsonArray[] ARRAY_VALUES = new JsonArray[] {
      SimpleValues.createSimpleStringArray5(), // JsonArray with String
      SimpleValues.createSimpleIntArray5(), // JsonArray with int
      SimpleValues.createSimpleBoolArray5(), // JsonArray with boolean
      SimpleValues.createSimpleObjectArray5() // JsonArray with JsonObject
  };

  /** Tests input data with JsonArray instances. */
  private static final JsonObject[] OBJ_VALUES = new JsonObject[] {
      SimpleValues.createSimpleObjectWithStr(), // JsonObject with String
      SimpleValues.createSimpleObjectWithInt(), // JsonObject with int
      SimpleValues.createSimpleObjectWithBool(), // JsonObject with boolean
      SimpleValues.createCompoundObject() // JsonObject with JsonObject
  };

  /** Test input data for {@code groupingBy} methods. */
  private static final JsonArray OBJ_ARRAY_GROUP = Json.createArrayBuilder()
      .add(Json.createObjectBuilder().add("name", "Peter").add("office",
          "Green"))
      .add(Json.createObjectBuilder().add("name", "John").add("office", "Red"))
      .add(Json.createObjectBuilder().add("name", "Bob").add("office", "Blue"))
      .add(Json.createObjectBuilder().add("name", "Sarah").add("office", "Red"))
      .add(Json.createObjectBuilder().add("name", "Tom").add("office", "Blue"))
      .add(Json.createObjectBuilder().add("name", "Jane").add("office", "Blue"))
      .add(Json.createObjectBuilder().add("name", "Peggy").add("office",
          "Green"))
      .add(Json.createObjectBuilder().add("name", "Rick").add("office", "Red"))
      .build();

  /**
   * Creates an instance of {@link jakarta.json.stream.JsonCollectors} class
   * implementation tests.
   */
  Collectors() {
    super();
  }

  /**
   * Test {@link jakarta.json.stream.JsonCollectors} class implementation. Suite
   * entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonCollectors class implementation");
    LOGGER.info("JsonCollectors class implementation");
    testToJsonArrayCollector(result);
    // testSimpleToJsonObjectCollector(result);
    testToJsonObjectCollector(result);
    testSimpleGroupingByCollector(result);
    testSortingGroupingByCollector(result);
    return result;
  }

  /**
   * Test collector returned by {@code toJsonArray()} method. This collector
   * packs {@code Stream<JsonValue>} contend into a single JsonArray instance
   * which contains stream values in the same order at they were read. It can be
   * considered as {@link JsonParser#getArrayStream()} counterpart.
   * 
   * @param result
   *          Tests result record.
   */
  private void testToJsonArrayCollector(final TestResult result) {
    LOGGER.info(" - Collector returned by toJsonArray()");
    for (final JsonArray in : ARRAY_VALUES) {
      LOGGER.info("   - Input: " + JsonAssert.valueToString(in));
      final Collector<JsonValue, JsonArrayBuilder, JsonArray> col = JsonCollectors
          .toJsonArray();
      final JsonArray out = in.getValuesAs(JsonObject.class).stream()
          .collect(col);
      if (operationFailed(in, out)) {
        result.fail("toJsonArray()", "Output Stream value " + JsonAssert.valueToString(out)
            + " shall be " + JsonAssert.valueToString(in));
      }
    }
  }

  // TCK test for https://java.net/jira/browse/JSON_PROCESSING_SPEC-82 in case
  // it will be checked in.
  // /**
  // * Test collector returned by {@code toJsonObject()} method.
  // * This collector packs {@code Stream<JsonValue>} contend into a single
  // JsonArray instance which contains
  // * stream values in the same order at they were read. It can be considered
  // as {@link JsonParser#getArrayStream()}
  // * counterpart.
  // * @param result Tests result record.
  // */
  // private void testSimpleToJsonObjectCollector(final TestResult result) {
  // LOGGER.info(" - Collector returned by toJsonObject()");
  // for (final JsonObject in : OBJ_VALUES) {
  // LOGGER.info(" - Input: " + valueToString(in));
  // final Collector<Map.Entry<String,JsonValue>, JsonObjectBuilder, JsonObject>
  // col = JsonCollectors.toJsonObject();
  // final JsonObject out = (in.entrySet()).stream().collect(col);
  // if (operationFailed(in, out)) {
  // result.fail("toJsonObject()",
  // "Output Stream value " + valueToString(out) + " shall be " +
  // valueToString(in));
  // }
  // }
  // }

  /**
   * Test collector returned by
   * {@code toJsonObject(Function<JsonValue,String>, Function<JsonValue,JsonValue>)}
   * method. This collector does not pack content of
   * {@code Stream<Map.Entry<String,JsonValue>>} stream of
   * {@link JsonParser#getObjectStream()} output. So it's not counterpart of
   * this method. It works with {@code Stream<JsonValue>}, which is
   * {@link JsonParser#getArrayStream()} output and uses two {@link Function}
   * implementations to build target object keys and values from
   * {@code JsonValue} in the {@code Stream}.
   * 
   * @param result
   *          Tests result record.
   */
  private void testToJsonObjectCollector(final TestResult result) {
    LOGGER.info(" - Collector returned by toJsonObject(Function,Function)");
    final JsonArray in = Json.createArrayBuilder()
        .add(Json.createObjectBuilder().add("key", SimpleValues.STR_NAME).add("value",
            SimpleValues.STR_VALUE))
        .add(Json.createObjectBuilder().add("key", SimpleValues.INT_NAME).add("value",
            SimpleValues.INT_VALUE))
        .add(Json.createObjectBuilder().add("key", SimpleValues.BOOL_NAME).add("value",
            SimpleValues.BOOL_VALUE))
        .add(Json.createObjectBuilder().add("key", SimpleValues.OBJ_NAME).add("value",
            SimpleValues.OBJ_VALUE))
        .build();
    final JsonObject check = Json.createObjectBuilder().add(SimpleValues.STR_NAME, SimpleValues.STR_VALUE)
        .add(SimpleValues.INT_NAME, SimpleValues.INT_VALUE).add(SimpleValues.BOOL_NAME, SimpleValues.BOOL_VALUE)
        .add(SimpleValues.OBJ_NAME, SimpleValues.OBJ_VALUE).build();
    LOGGER.info("     Input: " + JsonAssert.valueToString(in));
    final Collector<JsonValue, JsonObjectBuilder, JsonObject> col = JsonCollectors
        .toJsonObject(
            // Build key from stream value.
            (JsonValue v) -> {
              if (v.getValueType() == JsonValue.ValueType.OBJECT)
                return v.asJsonObject().getString("key");
              throw new IllegalStateException("Value must be JsonObject");
            },
            // Build value from stream value.
            (JsonValue v) -> {
              if (v.getValueType() == JsonValue.ValueType.OBJECT)
                return v.asJsonObject().get("value");
              throw new IllegalStateException("Value must be JsonObject");
            });
    final JsonObject out = in.getValuesAs(JsonObject.class).stream()
        .collect(col);
    if (operationFailed(out, check)) {
      result.fail("toJsonObject(Function,Function)", "Output Stream value "
          + JsonAssert.valueToString(out) + " shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Test collector returned by {@code groupingBy(Function<JsonValue,String>)}
   * method. This collector allows grouping of {@code Stream<JsonValue>} using
   * provided {@code Function<JsonValue,String>)} function to define group
   * identifiers based on {@code JsonValue} content. Test just groups JSON
   * objects in the stream by {@code "office"} attribute. Default
   * {@code toJsonArray()} collector is used so output is unsorted.
   * 
   * @param result
   *          Tests result record.
   */
  private void testSimpleGroupingByCollector(final TestResult result) {
    LOGGER.info(" - Collector returned by groupingBy(Function)");
    final JsonObject check = Json.createObjectBuilder().add("Red", Json
        .createArrayBuilder()
        .add(
            Json.createObjectBuilder().add("name", "John").add("office", "Red"))
        .add(Json.createObjectBuilder().add("name", "Sarah").add("office",
            "Red"))
        .add(
            Json.createObjectBuilder().add("name", "Rick").add("office", "Red"))
        .build())
        .add("Blue",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("name", "Bob").add("office",
                    "Blue"))
                .add(Json.createObjectBuilder().add("name", "Tom").add("office",
                    "Blue"))
                .add(Json.createObjectBuilder().add("name", "Jane")
                    .add("office", "Blue"))
                .build())
        .add("Green",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("name", "Peter")
                    .add("office", "Green"))
                .add(Json.createObjectBuilder().add("name", "Peggy")
                    .add("office", "Green"))
                .build())
        .build();
    LOGGER.info("     Input: " + JsonAssert.valueToString(OBJ_ARRAY_GROUP));
    final Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject> col = JsonCollectors
        .groupingBy((JsonValue v) -> {
          if (v.getValueType() == JsonValue.ValueType.OBJECT)
            return v.asJsonObject().getString("office");
          throw new IllegalStateException("Value must be JsonObject");
        });
    final JsonObject out = OBJ_ARRAY_GROUP.getValuesAs(JsonObject.class)
        .stream().collect(col);
    if (operationFailed(out, check)) {
      result.fail("groupingBy(Function)", "Output Stream value "
          + JsonAssert.valueToString(out) + " shall be " + JsonAssert.valueToString(check));
    }
  }

  /**
   * Builder to create sorted JsonArray with ordering by {@code "name"}
   * attribute of {@code JsonObject}.
   */
  private class ValueBuilder implements JsonArrayBuilder {

    /** Sorted collection of values. */
    private final TreeSet<JsonValue> values;

    private ValueBuilder() {
      values = new TreeSet<>((JsonValue v1, JsonValue v2) -> {
        if (v1.getValueType() == JsonValue.ValueType.OBJECT
            && v1.getValueType() == JsonValue.ValueType.OBJECT) {
          return v1.asJsonObject().getString("name")
              .compareTo(v2.asJsonObject().getString("name"));
        }
        throw new IllegalStateException("Values must be JsonObject");
      });
    }

    /**
     * Builder accumulator method.
     * 
     * @param value
     *          Value to be added to {@code JsonArray}.
     * @return This builder instance.
     */
    @Override
    public JsonArrayBuilder add(JsonValue value) {
      values.add(value);
      return this;
    }

    /**
     * Builder combiner method.
     * 
     * @param builder
     *          Builder containing values to be added to {@code JsonArray}.
     * @return This builder instance.
     */
    public ValueBuilder addAll(ValueBuilder builder) {
      values.addAll(builder.values);
      return this;
    }

    /**
     * Builder finisher method.
     * 
     * @return {@code JsonArray} from current builder content.
     */
    @Override
    public JsonArray build() {
      JsonArrayBuilder builder = Json.createArrayBuilder();
      for (JsonValue value : values) {
        builder.add(value);
      }
      return builder.build();
    }

    @Override
    public JsonArrayBuilder add(String value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(BigDecimal value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(BigInteger value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(int value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(long value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(double value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(boolean value) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder addNull() {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(JsonObjectBuilder builder) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

    @Override
    public JsonArrayBuilder add(JsonArrayBuilder builder) {
      throw new UnsupportedOperationException("Not supported yet."); // To
                                                                     // change
                                                                     // body of
                                                                     // generated
                                                                     // methods,
                                                                     // choose
                                                                     // Tools |
                                                                     // Templates.
    }

  }

  /**
   * Test collector returned by
   * {@code groupingBy(Function<JsonValue,String>,Collector<JsonValue,JsonArrayBuilder,JsonArray>)}
   * method. This collector allows grouping of {@code Stream<JsonValue>} using
   * provided {@code Function<JsonValue,String>)} function to define group
   * identifiers based on {@code JsonValue} content and
   * {@code Collector<JsonValue,JsonArrayBuilder,JsonArray>)}. Test groups JSON
   * objects in the stream by {@code "office"} attribute. External collector is
   * building sorted {@code JsonArray} so arrays for each office group are
   * sorted.
   * 
   * @param result
   *          Tests result record.
   */
  private void testSortingGroupingByCollector(final TestResult result) {
    LOGGER.info(" - Collector returned by groupingBy(Function,Collector)");
    final JsonObject check = Json.createObjectBuilder().add("Red", Json
        .createArrayBuilder()
        .add(
            Json.createObjectBuilder().add("name", "John").add("office", "Red"))
        .add(
            Json.createObjectBuilder().add("name", "Rick").add("office", "Red"))
        .add(Json.createObjectBuilder().add("name", "Sarah").add("office",
            "Red"))
        .build())
        .add("Blue",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("name", "Bob").add("office",
                    "Blue"))
                .add(Json.createObjectBuilder().add("name", "Jane")
                    .add("office", "Blue"))
                .add(Json.createObjectBuilder().add("name", "Tom").add("office",
                    "Blue"))
                .build())
        .add("Green",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("name", "Peggy")
                    .add("office", "Green"))
                .add(Json.createObjectBuilder().add("name", "Peter")
                    .add("office", "Green"))
                .build())
        .build();
    Collector<JsonValue, JsonArrayBuilder, JsonArray> toArray = Collector.of(
        ValueBuilder::new, JsonArrayBuilder::add, JsonArrayBuilder::addAll,
        JsonArrayBuilder::build);
    LOGGER.info("     Input: " + JsonAssert.valueToString(OBJ_ARRAY_GROUP));
    final Collector<JsonValue, Map<String, JsonArrayBuilder>, JsonObject> col = JsonCollectors
        .groupingBy((JsonValue v) -> {
          if (v.getValueType() == JsonValue.ValueType.OBJECT)
            return v.asJsonObject().getString("office");
          throw new IllegalStateException("Value must be JsonObject");
        }, toArray);
    final JsonObject out = OBJ_ARRAY_GROUP.getValuesAs(JsonObject.class)
        .stream().collect(col);
    if (operationFailed(out, check)) {
      result.fail("groupingBy(Function,Collector)", "Output Stream value "
          + JsonAssert.valueToString(out) + " shall be " + JsonAssert.valueToString(check));
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
    LOGGER.info("     Checking " + JsonAssert.valueToString(out));
    return out == null || !JsonAssert.assertEquals(check, out);
  }

}

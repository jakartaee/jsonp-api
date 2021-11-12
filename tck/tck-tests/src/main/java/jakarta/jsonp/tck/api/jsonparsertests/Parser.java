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

package jakarta.jsonp.tck.api.jsonparsertests;

import jakarta.jsonp.tck.api.common.TestResult;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonStructure;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests: {@link JsonParser} API
 * methods added in JSON-P 1.1.
 */
public class Parser {

  private static final Logger LOGGER = Logger.getLogger(Parser.class.getName());
  
  /** Tests input data with various JsonValue instances. */
  private static final JsonValue[] VALUES = new JsonValue[] {
      toJsonValue(STR_VALUE), // Non JsonObject with String
      toJsonValue(INT_VALUE), // Non JsonObject with int
      toJsonValue(LNG_VALUE), // Non JsonObject with long
      toJsonValue(DBL_VALUE), // Non JsonObject with double
      toJsonValue(BOOL_VALUE), // Non JsonObject with boolean
      toJsonValue(BDC_VALUE), // Non JsonObject with BigDecimal
      toJsonValue(BIN_VALUE), // Non JsonObject with BigInteger
      createSimpleObjectStr(), // JsonObject with String
      createSimpleObjectInt(), // JsonObject with int
      createSimpleObjectBool(), // JsonObject with boolean
      createSimpleObjectObject(), // JsonObject with JsonObject
      createEmptyArrayWithStr(), // JsonArray with String
      createEmptyArrayWithInt(), // JsonArray with int
      createEmptyArrayWithBool(), // JsonArray with boolean
      createEmptyArrayWithObject() // JsonArray with JsonObject
  };

  // /** Tests input data with simple JsonValue instances. */
  // private static final JsonValue[] SIMPLE_VALUES = new JsonValue[] {
  // toJsonValue(STR_VALUE), // Non JsonObject with String
  // toJsonValue(INT_VALUE), // Non JsonObject with int
  // toJsonValue(LNG_VALUE), // Non JsonObject with long
  // toJsonValue(BOOL_VALUE), // Non JsonObject with boolean
  // toJsonValue(BDC_VALUE), // Non JsonObject with BigDecimal
  // toJsonValue(BIN_VALUE) // Non JsonObject with BigInteger
  // };

  /** Tests input data with compound JsonValue instances (object or array). */
  private static final JsonStructure[] COMPOUND_VALUES = new JsonStructure[] {
      createSimpleObjectStr(), // JsonObject with String
      createSimpleObjectInt(), // JsonObject with int
      createSimpleObjectBool(), // JsonObject with boolean
      createSimpleObjectObject(), // JsonObject with JsonObject
      createEmptyArrayWithStr(), // JsonArray with String
      createEmptyArrayWithInt(), // JsonArray with int
      createEmptyArrayWithBool(), // JsonArray with boolean
      createEmptyArrayWithObject() // JsonArray with JsonObject
  };

  /** Tests input data with empty JsonObject and JsonArray instances. */
  private static final JsonStructure[] EMPTY_VALUES = new JsonStructure[] {
      createEmptyObject(), // Empty JsonObject
      createEmptyArray() // Empty JsonArray
  };

  /** Tests input data with JsonObject instances. */
  private static final JsonObject[] OBJ_VALUES = new JsonObject[] {
      createSimpleObjectStr(), // JsonObject with String
      createSimpleObjectInt(), // JsonObject with int
      createSimpleObjectBool(), // JsonObject with boolean
      createSimpleObjectObject(), // JsonObject with JsonObject
      createSimpleObjectWithStr() // JsonObject with default value (String) and
                                  // another String

  };

  /** Tests input data with non JsonObject instances. */
  private static final JsonValue[] NON_OBJ_VALUES = new JsonValue[] {
      toJsonValue(STR_VALUE), // Non JsonObject with String
      toJsonValue(INT_VALUE), // Non JsonObject with int
      toJsonValue(LNG_VALUE), // Non JsonObject with long
      toJsonValue(DBL_VALUE), // Non JsonObject with double
      toJsonValue(BOOL_VALUE), // Non JsonObject with boolean
      toJsonValue(BDC_VALUE), // Non JsonObject with BigDecimal
      toJsonValue(BIN_VALUE), // Non JsonObject with BigInteger
      createEmptyArrayWithStr(), // JsonArray with String
      createEmptyArrayWithInt(), // JsonArray with int
      createEmptyArrayWithBool(), // JsonArray with boolean
      createEmptyArrayWithObject() // JsonArray with JsonObject
  };

  /** Tests input data with JsonArray instances. */
  private static final JsonArray[] ARRAY_VALUES = new JsonArray[] {
      createEmptyArrayWithStr(), // JsonArray with String
      createEmptyArrayWithInt(), // JsonArray with int
      createEmptyArrayWithBool(), // JsonArray with boolean
      createEmptyArrayWithObject() // JsonArray with JsonObject
  };

  /** Tests input data with non JsonArray instances. */
  private static final JsonValue[] NON_ARRAY_VALUES = new JsonValue[] {
      toJsonValue(STR_VALUE), // Non JsonObject with String
      toJsonValue(INT_VALUE), // Non JsonObject with int
      toJsonValue(LNG_VALUE), // Non JsonObject with long
      toJsonValue(DBL_VALUE), // Non JsonObject with double
      toJsonValue(BOOL_VALUE), // Non JsonObject with boolean
      toJsonValue(BDC_VALUE), // Non JsonObject with BigDecimal
      toJsonValue(BIN_VALUE), // Non JsonObject with BigInteger
      createSimpleObjectStr(), // JsonObject with String
      createSimpleObjectInt(), // JsonObject with int
      createSimpleObjectBool(), // JsonObject with boolean
      createSimpleObjectObject() // JsonObject with JsonObject
  };

  /**
   * Creates an instance of {@link JsonParser} API methods added in JSON-P 1.1
   * test.
   */
  Parser() {
    super();
  }

  /**
   * Test {@link JsonParser} API methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonParser API methods added in JSON-P 1.1.");
    LOGGER.info("JsonParser API methods added in JSON-P 1.1.");
    testGetObject(result);
    testGetNonObject(result);
    testGetArray(result);
    testGetNonArray(result);
    testGetValue(result);
    testGetIllegalValue(result);
    testGetObjectStream(result);
    testGetNonObjectStream(result);
    testGetArrayStream(result);
    testGetNonArrayStream(result);
    testGetValueStream(result);
    testGetCompoundValueStream(result);
    testSkipArray(result);
    testSkipNonArray(result);
    testSkipObject(result);
    testSkipNonObject(result);
    return result;
  }

  /**
   * Test {@code JsonParser getObject()} method on JSON object values.
   */
  private void testGetObject(final TestResult result) {
    for (JsonObject value : OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getObject() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        JsonObject out = parser.getObject();
        if (operationFailed(value, out)) {
          result.fail("getObject()", "Output value " + valueToString(out)
              + " shall be " + valueToString(value));
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("getObject()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser getObject()} method on non JSON object values.
   */
  private void testGetNonObject(final TestResult result) {
    for (JsonValue value : NON_OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getObject() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.getObject();
        result.fail("getObject()",
            "Calling method on non object value shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getObject()",
            "Calling method on non object value shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser getArray()} method on JSON array values.
   */
  private void testGetArray(final TestResult result) {
    for (JsonArray value : ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getArray() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        JsonArray out = parser.getArray();
        if (operationFailed(value, out)) {
          result.fail("getArray()", "Output value " + valueToString(out)
              + " shall be " + valueToString(value));
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("getArray()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser getArray()} method on non JSON object values.
   */
  private void testGetNonArray(final TestResult result) {
    for (JsonValue value : NON_ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getArray() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.getArray();
        result.fail("getArray()",
            "Calling method on non array value shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getArray()",
            "Calling method on non array value shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser getValue()} method on common JSON values.
   */
  private void testGetValue(final TestResult result) {
    for (JsonValue value : VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getValue() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        JsonValue out = parser.getValue();
        if (operationFailed(value, out)) {
          result.fail("getValue()", "Output value " + valueToString(out)
              + " shall be " + valueToString(value));
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("getValue()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser getValue()} method on END_OBJECT and END_ARRAY
   * lexical elements.
   */
  private void testGetIllegalValue(final TestResult result) {
    for (JsonValue value : EMPTY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getValue() on 2nd lexical element of " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next(); // Skip to START ELEMENT
        parser.next(); // Skip to END ELEMENT
        parser.getValue();
        result.fail("getValue()",
            "Calling method on END_OBJECT and END_ARRAY lexical elements shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getValue()",
            "Calling method on END_OBJECT and END_ARRAY lexical elements shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser getObjectStream()} method on JSON object values.
   */
  private void testGetObjectStream(final TestResult result) {
    for (JsonObject value : OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getObjectStream() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        Stream<Map.Entry<String, JsonValue>> out = parser.getObjectStream();
        if (operationFailed(value, out)) {
          result.fail("getObjectStream()",
              "Output Stream shall contain " + valueToString(value));
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("getObjectStream()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser getObjectStream()} method on non JSON object values.
   */
  private void testGetNonObjectStream(final TestResult result) {
    for (JsonValue value : NON_OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getObjectStream() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.getObjectStream();
        result.fail("getObjectStream()",
            "Calling method on non object value shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getObjectStream()",
            "Calling method on non object value shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser getArrayStream()} method on JSON array values.
   */
  private void testGetArrayStream(final TestResult result) {
    for (JsonArray value : ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getArrayStream() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        Stream<JsonValue> out = parser.getArrayStream();
        if (operationFailed(value, out)) {
          result.fail("getArrayStream()",
              "Output Stream shall contain " + valueToString(value));
        }
      } catch (JsonException ex) {
        LOGGER.info("Caught JsonException: " + ex.getLocalizedMessage());
        result.fail("getArrayStream()",
            "Caught JsonException: " + ex.getLocalizedMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser getArrayStream()} method on non JSON array values.
   */
  private void testGetNonArrayStream(final TestResult result) {
    for (JsonValue value : NON_ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getArrayStream() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.getArrayStream();
        result.fail("getArrayStream()",
            "Calling method on non array value shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getArrayStream()",
            "Calling method on non array value shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser getValueStream()} method on simple JSON values in
   * document root.
   */
  private void testGetValueStream(final TestResult result) {
    for (final JsonValue value : VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getValueStream() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        final Stream<JsonValue> outStream = parser.getValueStream();
        int count = 0;
        for (final Iterator<JsonValue> i = outStream.iterator(); i.hasNext();) {
          final JsonValue out = i.next();
          if (operationFailed(value, out)) {
            result.fail("getValueStream()", "Output Stream value "
                + valueToString(out) + " shall be " + valueToString(value));
          }
          count++;
        }
        if (count != 1) {
          LOGGER.info("     Output Stream contains "
              + Integer.toString(count) + " values, not 1");
          result.fail("getValueStream()",
              "Output Stream does not contain exactly 1 JSON value");
        }
      }
    }
  }

  /**
   * Test {@code JsonParser getValueStream()} method inside compound JSON values
   * in document root.
   */
  private void testGetCompoundValueStream(final TestResult result) {
    for (final JsonValue value : COMPOUND_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - getValueStream() inside " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.getValueStream();
        result.fail("getValueStream()",
            "Calling method on non object value shall throw IllegalStateException");
      } catch (IllegalStateException e) {
        LOGGER.info("      Expected exception: " + e.getMessage());
      } catch (Throwable t) {
        result.fail("getValueStream()",
            "Calling method on non object value shall throw IllegalStateException, not "
                + t.getClass().getSimpleName());
      }
    }
  }

  /**
   * Test {@code JsonParser skipArray()} method inside JSON array values.
   * Expected result: Parser shall advance to the end of the stream after
   * skipping an array which is the only value in the stream.
   */
  private void testSkipArray(final TestResult result) {
    for (final JsonArray value : ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - skipArray() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.skipArray();
        if (parser.hasNext()) {
          result.fail("skipArray()",
              "Parser did not davance to the end of the array");
        }
      } catch (Throwable t) {
        LOGGER.info(
            "     " + t.getClass().getSimpleName() + ": " + t.getMessage());
        result.fail("skipArray()",
            t.getClass().getSimpleName() + ": " + t.getMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser skipArray()} method outside JSON array values.
   * Expected result: Parser shall not advance anywhere when called outside an
   * array. Whole value shall match after {@code skipArray()} call because
   * nothing shall happen in it.
   */
  private void testSkipNonArray(final TestResult result) {
    for (final JsonValue value : NON_ARRAY_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - skipArray() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.skipArray();
        final JsonValue out = parser.getValue();
        if (operationFailed(value, out)) {
          result.fail("skipArray()",
              "Output value " + valueToString(out) + " shall be "
                  + valueToString(value) + " even after skipArray()");
        }
      } catch (Throwable t) {
        LOGGER.info(
            "     " + t.getClass().getSimpleName() + ": " + t.getMessage());
        result.fail("skipArray()",
            t.getClass().getSimpleName() + ": " + t.getMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser skipObject()} method inside JSON object values.
   * Expected result: Parser shall advance to the end of the stream after
   * skipping an object which is the only value in the stream.
   */
  private void testSkipObject(final TestResult result) {
    for (final JsonObject value : OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - skipObject() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.skipObject();
        if (parser.hasNext()) {
          result.fail("skipObject()",
              "Parser did not davance to the end of the object");
        }
      } catch (Throwable t) {
        LOGGER.info(
            "     " + t.getClass().getSimpleName() + ": " + t.getMessage());
        result.fail("skipObject()",
            t.getClass().getSimpleName() + ": " + t.getMessage());
      }
    }
  }

  /**
   * Test {@code JsonParser skipObject()} method outside JSON object values.
   * Expected result: Parser shall not advance anywhere when called outside an
   * object. Whole value shall match after {@code skipObject()} call because
   * nothing shall happen in it.
   */
  private void testSkipNonObject(final TestResult result) {
    for (final JsonValue value : NON_OBJ_VALUES) {
      final String data = jsonData(value);
      LOGGER.info(" - skipObject() on " + data);
      final StringReader strReader = new StringReader(data);
      try (final JsonParser parser = Json.createParser(strReader)) {
        parser.next();
        parser.skipObject();
        final JsonValue out = parser.getValue();
        if (operationFailed(value, out)) {
          result.fail("skipObject()",
              "Output value " + valueToString(out) + " shall be "
                  + valueToString(value) + " even after skipObject()");
        }
      } catch (Throwable t) {
        LOGGER.info(
            "     " + t.getClass().getSimpleName() + ": " + t.getMessage());
        result.fail("skipObject()",
            t.getClass().getSimpleName() + ": " + t.getMessage());
      }
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
    LOGGER.info("     Checking " + valueToString(out));
    return out == null || !assertEquals(check, out);
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected Stream content.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonObject check,
      final Stream<Map.Entry<String, JsonValue>> out) {
    // Operation failed for null.
    if (out == null) {
      LOGGER.info("     Output is null");
      return true;
    }
    final Set<String> keys = new HashSet<>(check.size());
    // Clone key Set
    for (final String key : check.keySet()) {
      keys.add(key);
    }
    for (final Iterator<Map.Entry<String, JsonValue>> i = out.iterator(); i
        .hasNext();) {
      final Map.Entry<String, JsonValue> item = i.next();
      final JsonValue checkValue = check.get(item.getKey());
      LOGGER.info("     Checking " + valueToString(item.getValue()));
      // Operation failed if values with the same key are not equal.
      if (!item.getValue().equals(checkValue)) {
        LOGGER.info("       check: " + valueToString(checkValue)
            + " stream: " + valueToString(checkValue));
        return true;
      }
      keys.remove(item.getKey());
    }
    // Operation failed if some unmatched keys remain in the set.
    return !keys.isEmpty();
  }

  /**
   * Operation result check.
   * 
   * @param check
   *          Expected Stream content.
   * @param out
   *          Operation output.
   * @return Value of {@code true} if operation passed or {@code false}
   *         otherwise.
   */
  protected boolean operationFailed(final JsonArray check,
      final Stream<JsonValue> out) {
    // Operation failed for null.
    if (out == null) {
      LOGGER.info("     Output is null");
      return true;
    }
    final Iterator<JsonValue> ci = check.iterator();
    final Iterator<JsonValue> oi = out.iterator();
    // To exit cycle, at least one of iterators does not have next value.
    while (ci.hasNext() && oi.hasNext()) {
      final JsonValue checkValue = ci.next();
      final JsonValue outValue = oi.next();
      LOGGER.info("     Checking " + valueToString(outValue));
      if (!checkValue.equals(outValue)) {
        LOGGER.info("       check: " + valueToString(checkValue)
            + " stream: " + valueToString(checkValue));
        return true;
      }
    }
    // Check still has values, something was missing in output.
    if (ci.hasNext()) {
      LOGGER.info("     Output contains less values than expected");
      return true;
    }
    // Output still has values, it contains more than expected.
    if (oi.hasNext()) {
      LOGGER.info("     Output contains more values than expected");
      return true;
    }
    return false;
  }

}

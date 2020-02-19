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

package jakarta.jsonp.tck.api.jsonobjecttests;

import jakarta.jsonp.tck.api.common.ObjectBuilder;
import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * JavaScript Object Notation (JSON) compatibility tests for {@link JsonObject}
 * and {@link JsonObjectBuilder}.
 */
public class ObjectBuild {

  /**
   * {@link JsonArrayBuilder} API remove() methods added in JSON-P 1.1.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult(
        "JsonObjectBuilder API methods added in JSON-P 1.1.");
    System.out.println("JsonObjectBuilder API methods added in JSON-P 1.1.");
    testAddString(result);
    testAddInt(result);
    testAddBool(result);
    testAddObject(result);
    testAddAllNull(result);
    testRemoveString(result);
    testRemoveInt(result);
    testRemoveBool(result);
    testRemoveObject(result);
    testRemoveNull(result);
    return result;
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonObjectBuilder)} method on
   * {@code String} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddString(final TestResult result) {
    System.out.println(" - addAll(JsonObjectBuilder) for String");
    final JsonObjectBuilder target = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE);
    final JsonObjectBuilder arg = ObjectBuilder.add(Json.createObjectBuilder(),
        STR_NAME, STR_VALUE);
    final JsonObject check = createSimpleObjectWithStr();
    verifyAddAll(result, check, target, arg);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonObjectBuilder)} method on
   * {@code int} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddInt(final TestResult result) {
    System.out.println(" - addAll(JsonObjectBuilder) for int");
    final JsonObjectBuilder target = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE);
    final JsonObjectBuilder arg = ObjectBuilder.add(Json.createObjectBuilder(),
        INT_NAME, INT_VALUE);
    final JsonObject check = createSimpleObjectWithInt();
    verifyAddAll(result, check, target, arg);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonObjectBuilder)} method on
   * {@code boolean} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddBool(final TestResult result) {
    System.out.println(" - addAll(JsonObjectBuilder) for boolean");
    final JsonObjectBuilder target = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE);
    final JsonObjectBuilder arg = ObjectBuilder.add(Json.createObjectBuilder(),
        BOOL_NAME, BOOL_VALUE);
    final JsonObject check = createSimpleObjectWithBool();
    verifyAddAll(result, check, target, arg);
  }

  /**
   * Test {@code JsonArrayBuilder addAll(JsonObjectBuilder)} method on
   * {@code JsonObject} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddObject(final TestResult result) {
    System.out.println(" - addAll(JsonObjectBuilder) for JsonObject");
    final JsonObjectBuilder target = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE);
    final JsonObjectBuilder arg = ObjectBuilder.add(Json.createObjectBuilder(),
        OBJ_NAME, OBJ_VALUE);
    final JsonObject check = createCompoundObjectWithObject();
    verifyAddAll(result, check, target, arg);
  }

  /**
   * Test {@code JsonObjectBuilder addAll(JsonObjectBuilder)} method with
   * {@code null} builder argument.
   * 
   * @param result
   *          Test suite result.
   */
  private void testAddAllNull(final TestResult result) {
    System.out.println(" - addAll(JsonObjectBuilder) for null builder argument");
    JsonObjectBuilder builder = Json.createObjectBuilder();
    try {
      builder.addAll((JsonObjectBuilder) null);
      result.fail("addAll(null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("addAll(null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test {@code JsonObjectBuilder remove(String)} method on {@code String}
   * value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveString(final TestResult result) {
    System.out.println(" - remove(String) for String");
    final JsonObjectBuilder in = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE)
        .add(STR_NAME, STR_VALUE);
    final JsonObjectBuilder builder = in.remove(STR_NAME);
    final JsonObject check = createSimpleObject();
    verifyRemovel(result, check, builder);
  }

  /**
   * Test {@code JsonObjectBuilder remove(String)} method on {@code int} value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveInt(final TestResult result) {
    System.out.println(" - remove(String) for int");
    final JsonObjectBuilder in = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE)
        .add(INT_NAME, INT_VALUE);
    final JsonObjectBuilder builder = in.remove(INT_NAME);
    final JsonObject check = createSimpleObject();
    verifyRemovel(result, check, builder);
  }

  /**
   * Test {@code JsonObjectBuilder remove(String)} method on {@code boolean}
   * value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveBool(final TestResult result) {
    System.out.println(" - remove(String) for boolean");
    final JsonObjectBuilder in = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE)
        .add(BOOL_NAME, BOOL_VALUE);
    final JsonObjectBuilder builder = in.remove(BOOL_NAME);
    final JsonObject check = createSimpleObject();
    verifyRemovel(result, check, builder);
  }

  /**
   * Test {@code JsonObjectBuilder remove(String)} method on {@code JsonObject}
   * value.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveObject(final TestResult result) {
    System.out.println(" - remove(String) for JsonObject");
    final JsonObjectBuilder in = ObjectBuilder
        .add(Json.createObjectBuilder(), DEF_NAME, DEF_VALUE)
        .add(DEF_OBJ_NAME, DEF_OBJ_VALUE).add(OBJ_NAME, OBJ_VALUE);
    final JsonObjectBuilder builder = in.remove(OBJ_NAME);
    final JsonObject check = createCompoundObject();
    verifyRemovel(result, check, builder);
  }

  /**
   * Test {@code JsonObjectBuilder remove(String)} method with {@code null}
   * name.
   * 
   * @param result
   *          Test suite result.
   */
  private void testRemoveNull(final TestResult result) {
    System.out.println(" - remove(String) for null name argument");
    JsonObjectBuilder builder = Json.createObjectBuilder();
    try {
      builder.remove((String) null);
      result.fail("remove(null)",
          "Calling method with null argument shall throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("    - Expected exception: " + e.getMessage());
    } catch (Throwable t) {
      result.fail("remove(null)",
          "Calling method with null argument shall throw NullPointerException, not "
              + t.getClass().getSimpleName());
    }
  }

  /**
   * Test helper: Verify {@code JsonObjectBuilder addAll(JsonObjectBuilder)}
   * method on provided builders.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param target
   *          Builder instance used to call {@code addAll(JsonObjectBuilder)}
   *          method.
   * @param arg
   *          Builder instance passed as an argument.
   */
  private void verifyAddAll(final TestResult result, final JsonObject check,
      JsonObjectBuilder target, final JsonObjectBuilder arg) {
    target.addAll(arg);
    final JsonObject out = target.build();
    if (operationFailed(check, out)) {
      result.fail("addAll(JsonObjectBuilder)", "Output builder "
          + valueToString(out) + " value shall be " + valueToString(check));
    }
  }

  /**
   * Test helper: Verify {@code JsonObjectBuilder remove(String)} method on
   * provided builder.
   * 
   * @param result
   *          Test suite result.
   * @param check
   *          Expected value (used for operation check).
   * @param builder
   *          Builder instance after {@code JsonObjectBuilder remove(String)}
   *          method was called.
   * @param arg
   *          Builder instance passed as an argument.
   */
  private void verifyRemovel(final TestResult result, final JsonObject check,
      JsonObjectBuilder builder) {
    final JsonObject out = builder.build();
    if (operationFailed(check, out)) {
      result.fail("remove(String)", "Output builder " + valueToString(out)
          + " value shall be " + valueToString(check));
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
    return out == null || !assertEquals(check, out);
  }

}

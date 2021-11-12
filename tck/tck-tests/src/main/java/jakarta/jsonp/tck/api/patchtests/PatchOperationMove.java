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

package jakarta.jsonp.tck.api.patchtests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonPatchBuilder;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;

import java.util.logging.Logger;

import static jakarta.jsonp.tck.api.common.JsonAssert.*;
import static jakarta.jsonp.tck.api.common.SimpleValues.*;

// $Id$
/**
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 * <p>
 * Implements
 * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
 * 4.4. move</a>} tests.
 */
public class PatchOperationMove extends CommonOperation {

  private static final Logger LOGGER = Logger.getLogger(PatchOperationMove.class.getName());

  /** Tested operation name. */
  private final String OPERATION = "MOVE";

  /**
   * Creates an instance of RFC 6902 replace operation test.
   */
  PatchOperationMove() {
    super();
  }

  /**
   * Test RFC 6902 MOVE operation. Suite entry point.
   * 
   * @return Result of all tests in this suite.
   */
  TestResult test() {
    final TestResult result = new TestResult("RFC 6902 move operation");
    LOGGER.info("Testing RFC 6902 move operation:");
    testMoveStringOnSimpleObject(result);
    testMoveStringOnSimpleArray(result);
    testMoveStringOnSimpleArray2(result);
    testMoveIntOnSimpleObject(result);
    testMoveIntOnSimpleArray(result);
    testMoveIntOnSimpleArray2(result);
    testMoveBoolOnSimpleObject(result);
    testMoveBoolOnSimpleArray(result);
    testMoveBoolOnSimpleArray2(result);
    testMoveObjectOnSimpleObject(result);
    testMoveObjectOnSimpleArray(result);
    testMoveObjectOnSimpleArray2(result);
    testMoveStringOnCompoundObject(result);
    testMoveOfNonExistingLocationInObject(result);
    testMoveOfNonExistingLocationInArray(result);
    testMoveVsRemoveAddOnSelfContainedPath(result);
    return result;
  }

  /**
   * Test RFC 6902 MOVE operation for {@code String} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveStringOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for String on simple JSON object");
    final JsonObject in = createSimpleObjectStr();
    final JsonObject check = createSimpleObjectMoveStr();
    simpleOperation(result, in, check, STR_PATH, DEF_PATH);
  }

  /**
   * Test RFC 6902 MOVE operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveStringOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 2");
    final JsonArray in = createStringArray2();
    final JsonArray check = createStringArray2R();
    simpleOperation(result, in, in, "/0", "/0");
    simpleOperation(result, in, check, "/1", "/0");
    simpleOperation(result, in, check, "/0", "/1");
    simpleOperation(result, in, check, "/0", "/-");
  }

  /**
   * Test RFC 6902 MOVE operation for {@code String} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveStringOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for String on simple JSON array of size 5");
    final JsonArray in = createSimpleStringArray5();
    final JsonArray check = createSimpleStringArray5R();
    complexOperation(result, in, check, new String[] { "/3", "/0", "/3", "/4" },
        new String[] { "/1", "/2", "/1", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/0", "/2" },
        new String[] { "/-", "/2", "/3", "/0" });
  }

  /**
   * Test RFC 6902 MOVE operation for {@code int} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveIntOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for int on simple JSON object");
    final JsonObject in = createSimpleObjectInt();
    final JsonObject check = createSimpleObjectMoveInt();
    simpleOperation(result, in, check, INT_PATH, DEF_PATH);
  }

  /**
   * Test RFC 6902 MOVE operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveIntOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 2");
    final JsonArray in = createIntArray2();
    final JsonArray check = createIntArray2R();
    simpleOperation(result, in, in, "/0", "/0");
    simpleOperation(result, in, check, "/1", "/0");
    simpleOperation(result, in, check, "/0", "/1");
    simpleOperation(result, in, check, "/0", "/-");
  }

  /**
   * Test RFC 6902 MOVE operation for {@code int} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveIntOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for int on simple JSON array of size 5");
    final JsonArray in = createSimpleIntArray5();
    final JsonArray check = createSimpleIntArray5R();
    complexOperation(result, in, check, new String[] { "/3", "/0", "/3", "/4" },
        new String[] { "/1", "/2", "/1", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/0", "/2" },
        new String[] { "/-", "/2", "/3", "/0" });
  }

  /**
   * Test RFC 6902 MOVE operation for {@code boolean} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveBoolOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON object");
    final JsonObject in = createSimpleObjectBool();
    final JsonObject check = createSimpleObjectMoveBool();
    simpleOperation(result, in, check, BOOL_PATH, DEF_PATH);
  }

  /**
   * Test RFC 6902 MOVE operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveBoolOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 2");
    final JsonArray in = createBoolArray2();
    final JsonArray check = createBoolArray2R();
    simpleOperation(result, in, in, "/0", "/0");
    simpleOperation(result, in, check, "/1", "/0");
    simpleOperation(result, in, check, "/0", "/1");
    simpleOperation(result, in, check, "/0", "/-");
  }

  /**
   * Test RFC 6902 MOVE operation for {@code boolean} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveBoolOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for boolean on simple JSON array of size 5");
    final JsonArray in = createSimpleBoolArray5();
    final JsonArray check = createSimpleBoolArray5R();
    complexOperation(result, in, check, new String[] { "/3", "/0", "/3", "/4" },
        new String[] { "/1", "/2", "/1", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/0", "/2" },
        new String[] { "/-", "/2", "/3", "/0" });
  }

  /**
   * Test RFC 6902 MOVE operation for {@code JsonObject} on simple JSON object.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveObjectOnSimpleObject(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON object");
    final JsonObject in = createSimpleObjectObject();
    final JsonObject check = createSimpleObjectMoveObject();
    simpleOperation(result, in, check, OBJ_PATH, DEF_PATH);
  }

  /**
   * Test RFC 6902 MOVE operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveObjectOnSimpleArray(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 2");
    final JsonArray in = createObjectArray2();
    final JsonArray check = createObjectArray2R();
    simpleOperation(result, in, in, "/0", "/0");
    simpleOperation(result, in, check, "/1", "/0");
    simpleOperation(result, in, check, "/0", "/1");
    simpleOperation(result, in, check, "/0", "/-");
  }

  /**
   * Test RFC 6902 MOVE operation for {@code JsonObject} on simple JSON array.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveObjectOnSimpleArray2(final TestResult result) {
    LOGGER.info(" - for JsonObject on simple JSON array of size 5");
    final JsonArray in = createSimpleObjectArray5();
    final JsonArray check = createSimpleObjectArray5R();
    complexOperation(result, in, check, new String[] { "/3", "/0", "/3", "/4" },
        new String[] { "/1", "/2", "/1", "/0" });
    complexOperation(result, in, check, new String[] { "/0", "/1", "/0", "/2" },
        new String[] { "/-", "/2", "/3", "/0" });
  }

  /**
   * Test RFC 6902 MOVE operation for {@code String} on compound JSON object.
   * Moved value overwrites an existing value.
   * 
   * @param result
   *          Tests result record.
   */
  private void testMoveStringOnCompoundObject(final TestResult result) {
    LOGGER.info(" - for String on compound JSON object");
    final JsonObject in = createCompoundObject();
    final JsonObject check = createCompoundObjectMoveValue();
    simpleOperation(result, in, check, DEF_PATH, DEF_OBJ_PATH + DEF_PATH);
  }

  // Tests based on RFC 6902 definitions and examples.

  /**
   * Test RFC 6902 MOVE operation for non existing location in object.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.4. move</a>} defines:<br>
   * The "from" location MUST exist for the operation to be successful.
   */
  private void testMoveOfNonExistingLocationInObject(final TestResult result) {
    LOGGER.info(" - for non existing location in JsonObject");
    final JsonObject[] objsIn = new JsonObject[] { createEmptyObject(),
        createSimpleObject(), createCompoundObject() };
    final String[] paths = new String[] { STR_PATH, INT_PATH, BOOL_PATH,
        OBJ_PATH };
    final Object[] values = new Object[] { OBJ_PATH, BOOL_PATH, INT_PATH,
        STR_PATH };
    // Go trough all objects
    for (int i = 0; i < objsIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, objsIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Test RFC 6902 MOVE operation for non existing location in array.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.4. move</a>} defines:<br>
   * The "from" location MUST exist for the operation to be successful.
   */
  private void testMoveOfNonExistingLocationInArray(final TestResult result) {
    LOGGER.info(" - for non existing location in JsonArray");
    final JsonArray[] arraysIn = new JsonArray[] { createEmptyArray(),
        createStringArray1(), createIntArray2(), createSimpleBoolArray5(),
        createObjectArray2() };
    final String[] paths = new String[] { "/", "/-1", "/-", "/5", "/0a", "/42",
        STR_PATH + "/0" };
    final Object[] values = new Object[] { "/0", "/1", "/2", "/5", "/1" };
    // Go trough all arrays
    for (int i = 0; i < arraysIn.length; i++) {
      // Go trough all paths
      for (int j = 0; j < paths.length; j++) {
        simpleOperationFail(result, arraysIn[i], paths[j], values[i]);
      }
    }
  }

  /**
   * Test RFC 6902 MOVE operation for moving existing path into path containing
   * source path as a prefix.
   * {@see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">RFC 6902:
   * 4.4. move</a>} defines:<br>
   * This operation is functionally identical to a "remove" operation on the
   * "from" location, followed immediately by an "add" operation at the target
   * location with the value that was just removed.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-7">RFC 6901: 7.
   * Error Handling</a>} defines:<br>
   * This specification does not define how errors are handled. An application
   * of JSON Pointer SHOULD specify the impact and handling of each type of
   * error.<br>
   * For example, some applications might stop pointer processing upon an error,
   * while others may attempt to recover from missing values by inserting
   * default ones.<br>
   * This means, that such an operation may fail on non existing target "path"
   * after ADD operation, but also missing pointer error recovery may take care
   * of creating missing "path" elements. In both cases MOVE and sequence of
   * REMOVE and ADD operations MUST produce the same result.
   */
  private void testMoveVsRemoveAddOnSelfContainedPath(final TestResult result) {
    LOGGER.info(" - for moving JsonObject under itself");
    final JsonObject in = createCompoundObject();
    final String targetPath = DEF_OBJ_PATH + DEF_PATH;
    final JsonPointer ptr = Json.createPointer(DEF_OBJ_PATH);
    final JsonValue value = ptr.getValue(in);
    final JsonPatchBuilder moveBuilder = Json.createPatchBuilder()
        .move(targetPath, DEF_OBJ_PATH);
    final JsonPatchBuilder remAddBuilder = Json.createPatchBuilder()
        .remove(DEF_OBJ_PATH).add(targetPath, value);
    final JsonPatch movePatch = moveBuilder.build();
    final JsonPatch remAddPatch = remAddBuilder.build();
    // Check REMOVE and ADD sequence first.
    JsonObject remAddOut;
    try {

      remAddOut = remAddPatch.apply(in);
      LOGGER.info("   REMOVE and ADD passed");
    } catch (JsonException e) {
      remAddOut = null;
      LOGGER.info("   REMOVE and ADD failed: " + e.getMessage());
    }
    // Check MOVE second
    JsonObject moveOut;
    try {
      moveOut = movePatch.apply(in);
      LOGGER.info("   MOVE passed");
    } catch (JsonException e) {
      moveOut = null;
      LOGGER.info("   MOVE failed: " + e.getMessage());
    }
    // Results evaluation
    if (remAddOut != null) {
      // Both output values are not null: Compare them
      if (moveOut != null) {
        if (!assertEquals(remAddOut, moveOut)) {
          result.fail("MOVE vs REMOVE and ADD",
              "Returned values are not equal");
        }
        // REMOVE and ADD output is not null but MOVE output is null
      } else {
        result.fail("MOVE vs REMOVE and ADD",
            "REMOVE and ADD failed but MOVE dit not");
      }
    } else {
      // REMOVE and ADD output is null but MOVE output is not null
      if (moveOut != null) {
        result.fail("MOVE vs REMOVE and ADD",
            "MOVE failed but REMOVE and ADD dit not");
      }
      // else: Both output values are null: both patch operations failed -> test
      // passed
    }
  }

  /**
   * Tested operation name {@code "MOVE"}.
   * 
   * @return Operation name to be used in logs.
   */
  @Override
  protected String operationName() {
    return OPERATION;
  }

  /**
   * Create and initialize patch builder to contain MOVE operation to be
   * applied.
   * 
   * @param path
   *          Source JSON path of MOVE operation.
   * @param value
   *          Target JSON path of MOVE operation. Must be instance of
   *          {@link String}.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder createOperationBuilder(final String path,
      final Object value) {
    if (value instanceof String) {
      // LOGGER.info(" MOVE "+path+" -> "+(String)value);
      return Json.createPatchBuilder().move((String) value, path);
    } else {
      throw new IllegalArgumentException(
          "Argument \"value\" is not an instance of String");
    }
  }

  /**
   * Update patch builder to contain next MOVE operation to be applied.
   * 
   * @param builder
   *          JSON patch builder to update.
   * @param path
   *          Source JSON path of MOVE operation.
   * @param value
   *          Target JSON path of MOVE operation. Must be instance of
   *          {@link String}.
   * @return Patch builder containing operation to be applied.
   */
  @Override
  protected JsonPatchBuilder updateOperationBuilder(
      final JsonPatchBuilder builder, final String path, final Object value) {
    if (value instanceof String) {
      // LOGGER.info(" MOVE "+path+" -> "+(String)value);
      return builder.move((String) value, path);
    } else {
      throw new IllegalArgumentException(
          "Argument \"value\" is not an instance of String");
    }
  }

}

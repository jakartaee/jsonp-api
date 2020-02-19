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

package jakarta.jsonp.tck.api.jsonarraytests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.*;

//$Id$

@RunWith(Arquillian.class)
public class ClientTests {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
  /* Tests */

  /*
   * @testName: jsonArrayTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:6; JSONP:JAVADOC:8; JSONP:JAVADOC:10;
   * JSONP:JAVADOC:12; JSONP:JAVADOC:14; JSONP:JAVADOC:16; JSONP:JAVADOC:18;
   * JSONP:JAVADOC:25; JSONP:JAVADOC:21; JSONP:JAVADOC:400; JSONP:JAVADOC:401;
   * JSONP:JAVADOC:402; JSONP:JAVADOC:403; JSONP:JAVADOC:404; JSONP:JAVADOC:406;
   * JSONP:JAVADOC:408; JSONP:JAVADOC:409;
   * 
   * @test_Strategy: Tests JsonArray/JsonArrayBuilder API's. Build a JsonArray
   * using the JsonArrayBuilder API's then verify that the list of JsonArray
   * values matches the expected list of JsonArray values.
   */
  @Test
  public void jsonArrayTest1() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      System.out.println("Create the expected list of JsonArray values");
      List<JsonValue> expList = new ArrayList<>();
      expList.add(JsonValue.FALSE);
      expList.add(JsonValue.TRUE);
      expList.add(JsonValue.NULL);
      expList.add(JSONP_Util.createJsonNumber(Double.MIN_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Integer.MIN_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Long.MIN_VALUE));
      expList.add(JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expList.add(
          JSONP_Util.createJsonNumber(BigDecimal.valueOf(123456789.123456789)));
      expList.add(JSONP_Util.createJsonNumber(new BigInteger("123456789")));
      expList.add(JSONP_Util.createJsonString("string1"));
      expList.add(object);
      expList.add(array);
      JSONP_Util.dumpList(expList, "Expected List");

      System.out.println("Create JsonArray using all JsonArrayBuilder API's");
      JsonArray myJsonArray = Json.createArrayBuilder() // Indices
          .add(JsonValue.FALSE) // 0
          .add(JsonValue.TRUE) // 1
          .add(JsonValue.NULL) // 2
          .add(Double.MIN_VALUE) // 3
          .add(Double.MAX_VALUE) // 4
          .add(Integer.MIN_VALUE) // 5
          .add(Integer.MAX_VALUE) // 6
          .add(Long.MIN_VALUE) // 7
          .add(Long.MAX_VALUE) // 8
          .add(BigDecimal.valueOf(123456789.123456789)) // 9
          .add(new BigInteger("123456789")) // 10
          .add("string1") // 11
          .add(object) // 12
          .add(array) // 13
          .build();

      List<JsonValue> actualList = myJsonArray;
      JSONP_Util.dumpList(actualList, "Actual List");
      System.out.println(
          "Compare actual list of JsonArray values with expected list of JsonArray values");
      pass = JSONP_Util.assertEqualsList(expList, actualList);
    } catch (Exception e) {
      throw new Fault("jsonArrayTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonArrayTest1 Failed");
  }

  /*
   * @testName: jsonArrayTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:6; JSONP:JAVADOC:8; JSONP:JAVADOC:10;
   * JSONP:JAVADOC:12; JSONP:JAVADOC:14; JSONP:JAVADOC:16; JSONP:JAVADOC:18;
   * JSONP:JAVADOC:105; JSONP:JAVADOC:106; JSONP:JAVADOC:108; JSONP:JAVADOC:96;
   * JSONP:JAVADOC:97; JSONP:JAVADOC:21; JSONP:JAVADOC:25; JSONP:JAVADOC:184;
   * JSONP:JAVADOC:400; JSONP:JAVADOC:401; JSONP:JAVADOC:402; JSONP:JAVADOC:403;
   * JSONP:JAVADOC:404; JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409;
   * 
   * 
   * @test_Strategy: Tests JsonArray/JsonArrayBuilder API's. Build a JsonArray
   * using the JsonArrayBuilder API's. Write the JsonArray to a JsonWriter and
   * read it back using a JsonReader. Verify that JsonArray written to the
   * JsonWriter and then read back using the JsonReader are equal.
   */
  @Test
  public void jsonArrayTest2() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      System.out.println(
          "Create JsonArray 'myJsonArray1' using all JsonArrayBuilder API's");
      JsonArray myJsonArray1 = Json.createArrayBuilder() // Indices
          .add(JsonValue.FALSE) // 0
          .add(JsonValue.TRUE) // 1
          .add(JsonValue.NULL) // 2
          .add(Double.MIN_VALUE) // 3
          .add(Double.MAX_VALUE) // 4
          .add(Integer.MIN_VALUE) // 5
          .add(Integer.MAX_VALUE) // 6
          .add(Long.MIN_VALUE) // 7
          .add(Long.MAX_VALUE) // 8
          .add(BigDecimal.valueOf(123456789.123456789)) // 9
          .add(new BigInteger("123456789")) // 10
          .add("string1") // 11
          .add(object) // 12
          .add(array) // 13
          .build();

      System.out.println("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      StringWriter sw = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sw)) {
        writer.writeArray(myJsonArray1);
        System.out.println("Close JsonWriter");
      }
      System.out.println("Save contents of the JsonWriter as a String");
      String contents = sw.toString();
      System.out.println("Dump contents of JsonWriter as a String");
      System.out.println("JsonWriterContents=" + contents);
      System.out.println("Read the JsonArray back into 'myJsonArray2' using a JsonReader");
      JsonArray myJsonArray2;
      try (JsonReader reader = Json.createReader(new StringReader(contents))) {
        myJsonArray2 = reader.readArray();
      }
      System.out.println("Dump contents of JsonArray read from String Contents");
      JSONP_Util.dumpJsonValue(myJsonArray2);

      System.out.println("Compare myJsonArray1 and myJsonArray2 for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(myJsonArray1, myJsonArray2);
    } catch (Exception e) {
      throw new Fault("jsonArrayTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonArrayTest2 Failed");
  }

  /*
   * @testName: jsonArrayTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:6; JSONP:JAVADOC:8; JSONP:JAVADOC:10;
   * JSONP:JAVADOC:430; JSONP:JAVADOC:12; JSONP:JAVADOC:14; JSONP:JAVADOC:16;
   * JSONP:JAVADOC:18; JSONP:JAVADOC:40; JSONP:JAVADOC:41; JSONP:JAVADOC:42;
   * JSONP:JAVADOC:44; JSONP:JAVADOC:45; JSONP:JAVADOC:46; JSONP:JAVADOC:48;
   * JSONP:JAVADOC:49; JSONP:JAVADOC:51; JSONP:JAVADOC:21; JSONP:JAVADOC:25;
   * JSONP:JAVADOC:101; JSONP:JAVADOC:102; JSONP:JAVADOC:262; JSONP:JAVADOC:263;
   * JSONP:JAVADOC:400; JSONP:JAVADOC:401; JSONP:JAVADOC:402; JSONP:JAVADOC:403;
   * JSONP:JAVADOC:404; JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409;
   * JSONP:JAVADOC:433; JSONP:JAVADOC:434; JSONP:JAVADOC:435; JSONP:JAVADOC:490;
   * JSONP:JAVADOC:493; JSONP:JAVADOC:496; JSONP:JAVADOC:499; JSONP:JAVADOC:506;
   * 
   * @test_Strategy: Tests JsonArray/JsonArrayBuilder API's. Build a JsonArray
   * using the JsonArrayBuilder API's. Verify contents of JsonArray using
   * JsonArray().getJsonNumber(int), JsonArray().getJsonString(int),
   * JsonArray().getJsonArray(int), JsonArray().getJsonObject().
   *
   * This also covers testing the following additional API's:
   *
   * JsonString.getString(), JsonNumber.bigDecimalValue(),
   * JsonNumber.bigIntegerValue(), JsonNumber.doubleValue(),
   * JsonNumber.intValue(), JsonNumber.longValue(), JsonNumber.isIntegral(),
   * JsonNumber.longValueExact(), JsonNumber.intValueExact(),
   * JsonNumber.bigIntegerValueExact(), JsonArray.getInt(int),
   * JsonArray.getString(int), JsonArrau.getBoolean(int),
   * JsonArray.getBoolean(int, boolean), JsonArray.getInt(int, int),
   * JsonArray.getString(int, String)
   */
  @SuppressWarnings("SuspiciousIndentAfterControlStatement")
  @Test
  public void jsonArrayTest3() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      int expInt[] = { -1, 1, 1, -1000, 1000, 1000, -2000, 2000, 2000,
          Integer.MAX_VALUE, Integer.MIN_VALUE };

      long expLong[] = { Long.MAX_VALUE, Long.MIN_VALUE };

      double expDouble[] = { Double.MAX_VALUE, Double.MIN_VALUE };

      System.out.println("Create myArray Jsonarray of 23 elements");
      JsonArray myArray = Json.createArrayBuilder() // Indices
          .add(-1).add(+1).add(1) // 0,1,2
          .add(-1e3).add(+1e3).add(1e3) // 3,4,5
          .add(-2E3).add(+2E3).add(2E3) // 6,7,8
          .add(Integer.MAX_VALUE) // 9
          .add(Integer.MIN_VALUE) // 10
          .add(Long.MAX_VALUE) // 11
          .add(Long.MIN_VALUE) // 12
          .add(Double.MAX_VALUE) // 13
          .add(Double.MIN_VALUE) // 14
          .add(BigDecimal.valueOf(123456789.123456789)) // 15
          .add(new BigInteger("123456789")) // 16
          .add(JsonValue.TRUE) // 17
          .add(JsonValue.FALSE) // 18
          .add(JsonValue.NULL) // 19
          .add(JSONP_Data.asciiCharacters) // 20
          .add(object) // 21
          .add(array) // 22
          .build();

      System.out.println("Array size=" + myArray.size());

      // Following array is used to test for Ints that could be one of following
      // types:
      boolean expectedIntTypes[] = { JSONP_Util.INTEGRAL,
          JSONP_Util.NON_INTEGRAL };
      // Verify JsonValueType=NUMBER and integer value equals expectedIntValue
      for (int i = 0; i < 11; i++) {
        System.out.println("Checking getValue(" + i + ") for correctness");
        System.out.println("Retrieve and verify (JsonValueType=NUMBER)");
        if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.NUMBER,
            myArray.getJsonNumber(i).getValueType()))
          pass = false;
        System.out.println("Retrieve and (expect JsonNumber NumberType be one of "
            + JSONP_Util.toStringJsonNumberTypes(expectedIntTypes) + ")");
        if (!JSONP_Util.assertEqualsJsonNumberTypes(expectedIntTypes,
            myArray.getJsonNumber(i).isIntegral()))
          pass = false;
        System.out.println("Retrieve and verify integer value via JsonNumber.intValue()");
        if (!JSONP_Util.assertEquals(expInt[i],
            myArray.getJsonNumber(i).intValue()))
          pass = false;
        System.out.println("Retrieve and verify integer value via JsonArray.getInt");
        if (!JSONP_Util.assertEquals(expInt[i], myArray.getInt(i)))
          pass = false;
        System.out.println(
            "Retrieve and verify integer value via JsonNumber.intValueExact()");
        if (!JSONP_Util.assertEquals(expInt[i],
            myArray.getJsonNumber(i).intValueExact()))
          pass = false;
      }

      // Verify JsonValueType=NUMBER and long value equals expectedLongValue
      for (int i = 11, j = 0; i < 13; i++, j++) {
        System.out.println("Checking getValue(" + i + ") for correctness");
        System.out.println("Retrieve and verify (JsonValueType=NUMBER)");
        if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.NUMBER,
            myArray.getJsonNumber(i).getValueType()))
          pass = false;
        System.out.println("Retrieve and (expect JsonNumber NumberType be INTEGRAL)");
        if (!JSONP_Util.assertEqualsJsonNumberType(JSONP_Util.INTEGRAL,
            myArray.getJsonNumber(i).isIntegral()))
          pass = false;
        System.out.println("Retrieve and verify long value via JsonNumber.longValue()");
        if (!JSONP_Util.assertEquals(expLong[j],
            myArray.getJsonNumber(i).longValue()))
          pass = false;
        System.out.println(
            "Retrieve and verify long value via JsonNumber.longValueExact()");
        if (!JSONP_Util.assertEquals(expLong[j],
            myArray.getJsonNumber(i).longValueExact()))
          pass = false;
      }

      // Following array is used to test for Doubles that could be one of
      // following types:
      boolean expectedDoubleTypes[] = { JSONP_Util.INTEGRAL,
          JSONP_Util.NON_INTEGRAL };

      // Verify JsonValueType=NUMBER and double value equals expectedDoubleValue
      for (int i = 13, j = 0; i < 15; i++, j++) {
        System.out.println("Checking getValue(" + i + ") for correctness");
        System.out.println("Retrieve and verify (JsonValueType=NUMBER)");
        if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.NUMBER,
            myArray.getJsonNumber(i).getValueType()))
          pass = false;
        System.out.println("Retrieve and (expect JsonNumber NumberType be one of "
            + JSONP_Util.toStringJsonNumberTypes(expectedDoubleTypes) + ")");
        if (!JSONP_Util.assertEqualsJsonNumberTypes(expectedDoubleTypes,
            myArray.getJsonNumber(i).isIntegral()))
          pass = false;
        System.out.println("Retrieve and verify double value via JsonNumber.doubleValue()");
        if (!JSONP_Util.assertEquals(expDouble[j],
            myArray.getJsonNumber(i).doubleValue()))
          pass = false;
      }

      // Verify JsonValueType=NUMBER and BigDecimalValue equals
      // expectedBigDecimal
      System.out.println("Checking getValue(15) for correctness");
      System.out.println("Retrieve and verify (JsonValueType=NUMBER)");
      if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.NUMBER,
          myArray.getJsonNumber(15).getValueType()))
        pass = false;
      System.out.println("Retrieve and (expect JsonNumber NumberType be one of "
          + JSONP_Util.toStringJsonNumberTypes(expectedDoubleTypes) + ")");
      if (!JSONP_Util.assertEqualsJsonNumberTypes(expectedDoubleTypes,
          myArray.getJsonNumber(15).isIntegral()))
        pass = false;
      System.out.println(
          "Retrieve and verify BigDecimal value via JsonNumber.bigDecimalValue()");
      if (!JSONP_Util.assertEquals(BigDecimal.valueOf(123456789.123456789),
          myArray.getJsonNumber(15).bigDecimalValue()))
        pass = false;

      // Verify JsonValueType=NUMBER and BigIntegerValue equals
      // expectedBigInteger
      System.out.println("Checking getValue(16) for correctness");
      System.out.println("Retrieve and verify (JsonValueType=NUMBER)");
      if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.NUMBER,
          myArray.getJsonNumber(16).getValueType()))
        pass = false;
      System.out.println("Retrieve and (expect JsonNumber NumberType be INTEGRAL)");
      if (!JSONP_Util.assertEqualsJsonNumberType(JSONP_Util.INTEGRAL,
          myArray.getJsonNumber(16).isIntegral()))
        pass = false;
      System.out.println(
          "Retrieve and verify BigInteger value via JsonNumber.bigIntegerValue()");
      if (!JSONP_Util.assertEquals(new BigInteger("123456789"),
          myArray.getJsonNumber(16).bigIntegerValue()))
        pass = false;
      System.out.println(
          "Retrieve and verify BigInteger value via JsonNumber.bigIntegerValueExact()");
      if (!JSONP_Util.assertEquals(new BigInteger("123456789"),
          myArray.getJsonNumber(16).bigIntegerValueExact()))
        pass = false;

      // Verify getBoolean(int)=true
      System.out.println("Retrieve and verify true value via JsonArray.getBoolean(int)");
      if (!JSONP_Util.assertEquals(true, myArray.getBoolean(17)))
        pass = false;

      // Verify getBoolean(int)=false
      System.out.println("Retrieve and verify false value via JsonArray.getBoolean(int)");
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(18)))
        pass = false;

      // Verify isNull(int)=true
      System.out.println("Retrieve and verify null value via JsonArray.isNull(int)");
      if (!JSONP_Util.assertEquals(true, myArray.isNull(19)))
        pass = false;

      // Verify isNull(int)=false
      System.out.println("Retrieve and verify non-null value via JsonArray.isNull(int)");
      if (!JSONP_Util.assertEquals(false, myArray.isNull(20)))
        pass = false;

      // Verify JsonValueType=STRING and getJsonString()=expectedString
      System.out.println("Checking getValue(20) for correctness");
      System.out.println("Retrieve and (expect JsonValueType=STRING)");
      if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.STRING,
          myArray.getJsonString(20).getValueType()))
        pass = false;
      System.out.println("Retrieve and verify string value via JsonString.getString()");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myArray.getJsonString(20).getString()))
        pass = false;
      System.out.println("Retrieve and verify string value via JsonArray.getString()");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myArray.getString(20)))
        pass = false;

      // Verify JsonValueType=OBJECT and getJsonObject()=expectedObject
      System.out.println("Checking getJsonObject(21) for correctness");
      System.out.println("Retrieve and (expect JsonValueType=OBJECT)");
      if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.OBJECT,
          myArray.getJsonObject(21).getValueType()))
        pass = false;
      System.out.println(
          "Retrieve and verify object value via JsonArray.getJsonObject(int)");
      if (!JSONP_Util.assertEqualsJsonObjects(object,
          myArray.getJsonObject(21)))
        pass = false;

      // Verify JsonValueType=ARRAY and getJsonArray()=expectedArray
      System.out.println("Checking getJsonArray(22) for correctness");
      System.out.println("Retrieve and (expect JsonValueType=ARRAY)");
      if (!JSONP_Util.assertEqualsJsonValueType(JsonValue.ValueType.ARRAY,
          myArray.getJsonArray(22).getValueType()))
        pass = false;
      System.out.println("Retrieve and verify array value via JsonArray.getJsonArray(int)");
      if (!JSONP_Util.assertEqualsJsonArrays(array, myArray.getJsonArray(22)))
        pass = false;

      // Verify calls to JsonArray.getBoolean(int)
      if (!JSONP_Util.assertEquals(true, myArray.getBoolean(17)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(18)))
        pass = false;

      // Verify calls to JsonArray.getBoolean(int, boolean)
      System.out.println(
          "Testing JsonArray.getBoolean(int, boolean) with/without default value setting.");
      if (!JSONP_Util.assertEquals(true, myArray.getBoolean(17, false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(0, false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(19, false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(20, false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(21, false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myArray.getBoolean(22, false)))
        pass = false;

      // Verify calls to JsonArray.getInt(int, int)
      System.out.println(
          "Testing JsonArray.getInt(int, int) with/without default value setting.");
      if (!JSONP_Util.assertEquals(-1, myArray.getInt(0, 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myArray.getInt(17, 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myArray.getInt(19, 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myArray.getInt(20, 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myArray.getInt(21, 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myArray.getInt(22, 10)))
        pass = false;

      // Verify calls to JsonArray.getString(int, String)
      System.out.println(
          "Testing JsonArray.getString(int, String) with/without default value setting.");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myArray.getString(20, "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myArray.getString(17, "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myArray.getString(19, "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myArray.getString(2, "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myArray.getString(21, "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myArray.getString(22, "foo")))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonArrayTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonArrayTest3 Failed");
  }

  /*
   * @testName: jsonArrayTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:6; JSONP:JAVADOC:8; JSONP:JAVADOC:14;
   * JSONP:JAVADOC:16; JSONP:JAVADOC:18; JSONP:JAVADOC:21; JSONP:JAVADOC:25;
   * JSONP:JAVADOC:106; JSONP:JAVADOC:400; JSONP:JAVADOC:401; JSONP:JAVADOC:402;
   * JSONP:JAVADOC:403; JSONP:JAVADOC:404; JSONP:JAVADOC:406; JSONP:JAVADOC:409;
   * 
   * @test_Strategy: Build a JsonArray and than write the JsonArray. Compare the
   * Json text from the writer contents with the expected Json text output
   * expected based on the JsonArray.
   */
  @Test
  public void jsonArrayTest4() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonArray for testing");
      JsonArray myJsonArray1 = JSONP_Util.createSampleJsonArray();
      System.out.println("Write the JsonArray 'myJsonArray1' out to a JsonWriter");
      StringWriter sw = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sw)) {
        writer.writeArray(myJsonArray1);
        System.out.println("Close JsonWriter");
      }
      System.out.println("Save contents of the JsonWriter as a String");
      String contents = sw.toString();
      System.out.println("Dump contents of JsonWriter as a String");
      System.out.println("JsonWriterContents=" + contents);
      System.out.println("Remove whitespace from contents.");
      String actJsonText = JSONP_Util.removeWhitespace(contents);
      System.out.println(
          "Compare expected JsonArray text with actual JsonArray text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONARRAY_TEXT, actJsonText);
    } catch (Exception e) {
      throw new Fault("jsonArrayTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonArrayTest4 Failed");
  }

  /*
   * @testName: jsonArrayGetValuesAsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:403; JSONP:JAVADOC:481; JSONP:JAVADOC:8;
   * JSONP:JAVADOC:14;
   * 
   * @test_Strategy: Build a Json array of values of the same type. Get the
   * values as a list for that type. Compare results in list to what is expected
   * for equality.
   *
   * APIs called: JsonArray array = Json.createArrayBuilder().add(...).build()
   * List<T> JsonArray.getValuesAs(Class)
   */
  @SuppressWarnings("SuspiciousIndentAfterControlStatement")
  @Test
  public void jsonArrayGetValuesAsTest() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonArray of JsonNumber types for testing");
      JsonArray jsonArr = Json.createArrayBuilder().add(100).add(500).build();

      System.out.println("Create the expected list of JsonArray values");
      List<JsonValue> expList = new ArrayList<>();
      expList.add(JSONP_Util.createJsonNumber(100));
      expList.add(JSONP_Util.createJsonNumber(500));
      JSONP_Util.dumpList(expList, "Expected List");

      System.out.println("Create the JsonNumber list of JsonArray values");
      List<JsonNumber> numList = jsonArr.getValuesAs(JsonNumber.class);

      System.out.println("Create the actual list of JsonArray values");
      List<JsonValue> actList = new ArrayList<>();
      for (JsonNumber num : numList)
        actList.add(num);

      System.out.println("Compare actual list with expected list for equality");
      pass = JSONP_Util.assertEqualsList(expList, actList);

      System.out.println("Create sample JsonArray of JsonString types for testing");
      jsonArr = Json.createArrayBuilder().add("hello").add("world").build();

      System.out.println("Create the list of JsonString values");
      List<JsonString> strList = jsonArr.getValuesAs(JsonString.class);

      System.out.println("Comparing JsonString list elements to expected values.");
      if (!JSONP_Util.assertEquals(jsonArr.getString(0),
          strList.get(0).getString()))
        pass = false;

      if (!JSONP_Util.assertEquals(jsonArr.getString(1),
          strList.get(1).getString()))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonArrayGetValuesAsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonArrayGetValuesAsTest Failed");
  }

  /*
   * @testName: jsonArrayExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:43; JSONP:JAVADOC:47; JSONP:JAVADOC:50;
   * JSONP:JAVADOC:20; JSONP:JAVADOC:377; JSONP:JAVADOC:432; JSONP:JAVADOC:379;
   * JSONP:JAVADOC:378; JSONP:JAVADOC:380; JSONP:JAVADOC:431; JSONP:JAVADOC:400;
   * JSONP:JAVADOC:401; JSONP:JAVADOC:402; JSONP:JAVADOC:403; JSONP:JAVADOC:404;
   * JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409; JSONP:JAVADOC:491;
   * JSONP:JAVADOC:492; JSONP:JAVADOC:494; JSONP:JAVADOC:495; JSONP:JAVADOC:497;
   * JSONP:JAVADOC:498; JSONP:JAVADOC:500; JSONP:JAVADOC:501; JSONP:JAVADOC:507;
   * 
   * @test_Strategy: Test JsonArray exception conditions. Trips the exceptions:
   * java.lang.IndexOutOfBoundsException java.lang.ArithmeticException
   * java.lang.NumberFormatException java.lang.ClassCastException
   * java.lang.UnsupportedOperationException
   */
  @Test
  public void jsonArrayExceptionTests() throws Fault {
    boolean pass = true;
    JsonObject testObject = null;
    JsonArray testArray = null;

    try {
      System.out.println("Create sample JsonObject for testing");
      testObject = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      testArray = JSONP_Util.createSampleJsonArray();
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonObject to JsonNumber via getJsonNumber(int)");
      JsonNumber value = testArray.getJsonNumber(0);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonArray to JsonNumber via getJsonNumber(int)");
      JsonNumber value = testArray.getJsonNumber(15);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonNumber to JsonString via getJsonString(int)");
      JsonString value = testArray.getJsonString(4);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonString to JsonNumber via getJsonNumber(int)");
      JsonNumber value = testArray.getJsonNumber(6);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonValue.TRUE to JsonNumber via getJsonNumber(int)");
      JsonNumber value = testArray.getJsonNumber(1);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonObject to JsonArray via getJsonArray(int)");
      JsonArray value = testArray.getJsonArray(0);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonArray to JsonObject via getJsonObject(int)");
      JsonObject value = testArray.getJsonObject(15);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonObject to JsonNumber via getInt(int)");
      int value = testArray.getInt(0);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonObject to JsonString via getString(int)");
      String value = testArray.getString(0);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonArray to JsonString via getString(int)");
      String value = testArray.getString(15);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonObject to boolean via getBoolean(int)");
      boolean value = testArray.getBoolean(0);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonArray to boolean via getBoolean(int)");
      boolean value = testArray.getBoolean(13);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonString to boolean via getBoolean(int)");
      boolean value = testArray.getBoolean(6);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip ClassCastException
    try {
      System.out.println(
          "Trip ClassCastException trying to cast a JsonNumber to boolean via getBoolean(int)");
      boolean value = testArray.getBoolean(4);
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getJsonNumber(int)");
      int myInt = testArray.getJsonNumber(-1).intValue();
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getJsonNumber(int)");
      JsonValue myJsonValue = testArray.getJsonNumber(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getJsonArray(int)");
      JsonValue myJsonValue = testArray.getJsonArray(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getJsonArray(int)");
      JsonValue myJsonValue = testArray.getJsonArray(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getJsonObject(int)");
      JsonValue myJsonValue = testArray.getJsonObject(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getJsonObject(int)");
      JsonValue myJsonValue = testArray.getJsonObject(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getJsonString(int)");
      JsonValue myJsonValue = testArray.getJsonString(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getJsonString(int)");
      JsonValue myJsonValue = testArray.getJsonString(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getInt(int)");
      int myInt = testArray.getInt(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getInt(int)");
      int myInt = testArray.getInt(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getString(int)");
      String myString = testArray.getString(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getString(int)");
      String myString = testArray.getString(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to getBoolean(int)");
      boolean myBoolean = testArray.getBoolean(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to getBoolean(int)");
      boolean myBoolean = testArray.getBoolean(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing -1 as index to isNull(int)");
      boolean myBoolean = testArray.isNull(-1);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip IndexOutOfBoundsException
    try {
      System.out.println(
          "Trip IndexOutOfBoundsException passing 10000 as index to isNull(int)");
      boolean myBoolean = testArray.isNull(10000);
      pass = false;
      System.err.println("Failed to throw IndexOutOfBoundsException");
    } catch (IndexOutOfBoundsException e) {
      System.out.println("Got expected IndexOutOfBoundsException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip NumberFormatException calling add(Double.NaN)
    try {
      System.out.println("Trip NumberFormatException calling add(Double.NaN)");
      JsonArray array = Json.createArrayBuilder().add(Double.NaN).build();
      pass = false;
      System.err.println("Failed to throw NumberFormatException");
    } catch (NumberFormatException e) {
      System.out.println("Got expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip NumberFormatException calling add(Double.NEGATIVE_INFINITY)
    try {
      System.out.println(
          "Trip NumberFormatException calling add(Double.NEGATIVE_INFINITY)");
      JsonArray array = Json.createArrayBuilder().add(Double.NEGATIVE_INFINITY)
          .build();
      pass = false;
      System.err.println("Failed to throw NumberFormatException");
    } catch (NumberFormatException e) {
      System.out.println("Got expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip NumberFormatException calling add(Double.POSITIVE_INFINITY)
    try {
      System.out.println(
          "Trip NumberFormatException calling add(Double.POSITIVE_INFINITY)");
      JsonArray array = Json.createArrayBuilder().add(Double.POSITIVE_INFINITY)
          .build();
      pass = false;
      System.err.println("Failed to throw NumberFormatException");
    } catch (NumberFormatException e) {
      System.out.println("Got expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Test for ArithmeticException
    try {
      System.out.println(
          "Trip ArithmeticException calling add(12345.12345) and attempting to extract as an exact integer value");
      JsonArray array = Json.createArrayBuilder().add(12345.12345).build();
      System.out.println("Call JsonArray.getJsonNumber(0).intValueExact()");
      int value = array.getJsonNumber(0).intValueExact();
      pass = false;
      System.err.println("Failed to throw ArithmeticException");
    } catch (ArithmeticException e) {
      System.out.println("Got expected ArithmeticException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Test for ArithmeticException
    try {
      System.out.println(
          "Trip ArithmeticException calling add(12345.12345) and attempting to extract as an exact long value");
      JsonArray array = Json.createArrayBuilder().add(12345.12345).build();
      System.out.println("Call JsonArray.getJsonNumber(0).longValueExact()");
      long value = array.getJsonNumber(0).longValueExact();
      pass = false;
      System.err.println("Failed to throw ArithmeticException");
    } catch (ArithmeticException e) {
      System.out.println("Got expected ArithmeticException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Test for ArithmeticException
    try {
      System.out.println(
          "Trip ArithmeticException calling add(12345.12345) and attempting to extract as an exact biginteger value");
      JsonArray array = Json.createArrayBuilder().add(12345.12345).build();
      System.out.println("Call JsonArray.getJsonNumber(0).bigIntegerValueExact()");
      BigInteger value = array.getJsonNumber(0).bigIntegerValueExact();
      pass = false;
      System.err.println("Failed to throw ArithmeticException");
    } catch (ArithmeticException e) {
      System.out.println("Got expected ArithmeticException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Tests for UnsupportedOperationException using Collection methods to
    // modify JsonArray List

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.add(E) trying to modify JsonArray list which should be immutable");
      testArray.add(JsonValue.FALSE);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.add(int,E) trying to modify JsonArray list which should be immutable");
      testArray.add(0, JsonValue.FALSE);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.addAll(C) trying to modify JsonArray list which should be immutable");
      testArray.addAll(testArray);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.addAll(int, C) trying to modify JsonArray list which should be immutable");
      testArray.addAll(0, testArray);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.clear() trying to modify JsonArray list which should be immutable");
      testArray.clear();
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.remove(int) trying to modify JsonArray list which should be immutable");
      testArray.remove(0);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonArray.removeAll(C) trying to modify JsonArray list which should be immutable");
      testArray.removeAll(testArray);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException trying to modify JsonArray list which should be immutable");
      testArray.remove(JsonValue.TRUE);
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonArrayExceptionTests Failed");
  }

  /*
   * @testName: jsonArrayNullValueExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:555; JSONP:JAVADOC:556; JSONP:JAVADOC:557;
   * JSONP:JAVADOC:558; JSONP:JAVADOC:559; JSONP:JAVADOC:560;
   * 
   * @test_Strategy: Test JSON NPE exception conditions when attempting to add a
   * specified value that is null.
   */
  @Test
  public void jsonArrayNullValueExceptionTests() throws Fault {
    boolean pass = true;
    JsonArrayBuilder jab = Json.createArrayBuilder();

    // Trip NullPointerException
    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(JsonValue) when JsonValue is null.");
      jab.add((JsonValue) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(BigInteger) when BigInteger is null.");
      jab.add((BigInteger) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(JsonArrayBuilder) when JsonArrayBuilder is null.");
      jab.add((JsonArrayBuilder) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(JsonObjectBuilder) when JsonObjectBuilder is null.");
      jab.add((JsonObjectBuilder) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(BigDecimal) when BigDecimal is null.");
      jab.add((BigDecimal) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip NullPointerException
    try {
      System.out.println(
          "Trip NullPointerException for JsonArrayBuilder.add(String) when String is null.");
      jab.add((String) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonArrayNullValueExceptionTests Failed");
  }

  /*
   * @testName: jsonCreateArrayBuilder11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:572; JSONP:JAVADOC:573; JSONP:JAVADOC:651;
   * JSONP:JAVADOC:652;
   *
   * @test_Strategy: Tests JsonArrayBuilder API factory methods added in JSON-P
   * 1.1.
   */
  @Test
  public void jsonCreateArrayBuilder11Test() throws Fault {
    ArrayBuilders createTest = new ArrayBuilders();
    final TestResult result = createTest.test();
    result.eval();
  }

  /*
   * @testName: jsonArrayBuilder11AddTest
   * 
   * @assertion_ids: JSONP:JAVADOC:589; JSONP:JAVADOC:590; JSONP:JAVADOC:591;
   * JSONP:JAVADOC:592; JSONP:JAVADOC:593; JSONP:JAVADOC:594; JSONP:JAVADOC:595;
   * JSONP:JAVADOC:596; JSONP:JAVADOC:597; JSONP:JAVADOC:598; JSONP:JAVADOC:599;
   * JSONP:JAVADOC:600; JSONP:JAVADOC:601;
   *
   * @test_Strategy: Tests JsonArrayBuilder API add() methods added in JSON-P
   * 1.1.
   */
  @Test
  public void jsonArrayBuilder11AddTest() throws Fault {
    ArrayBuildAdd addTest = new ArrayBuildAdd();
    final TestResult result = addTest.test();
    result.eval();
  }

  /*
   * @testName: jsonArrayBuilder11SetTest
   * 
   * @assertion_ids: JSONP:JAVADOC:603; JSONP:JAVADOC:604; JSONP:JAVADOC:605;
   * JSONP:JAVADOC:606; JSONP:JAVADOC:607; JSONP:JAVADOC:608; JSONP:JAVADOC:609;
   * JSONP:JAVADOC:610; JSONP:JAVADOC:611; JSONP:JAVADOC:612; JSONP:JAVADOC:613;
   * 
   * @test_Strategy: Tests JsonArrayBuilder API set() methods added in JSON-P
   * 1.1.
   */
  @Test
  public void jsonArrayBuilder11SetTest() throws Fault {
    ArrayBuildSet setTest = new ArrayBuildSet();
    final TestResult result = setTest.test();
    result.eval();
  }

  /*
   * @testName: jsonArrayBuilder11RemoveTest
   * 
   * @assertion_ids: JSONP:JAVADOC:602;
   * 
   * @test_Strategy: Tests JsonArrayBuilder API remove() methods added in JSON-P
   * 1.1.
   */
  @Test
  public void jsonArrayBuilder11RemoveTest() throws Fault {
    ArrayBuildRemove removeTest = new ArrayBuildRemove();
    final TestResult result = removeTest.test();
    result.eval();
  }

}

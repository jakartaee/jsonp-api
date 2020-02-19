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

/*
 * $Id$
 */
package jakarta.jsonp.tck.api.jsonobjecttests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.*;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
  /* Tests */

  /*
   * @testName: jsonObjectTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:58; JSONP:JAVADOC:61; JSONP:JAVADOC:64;
   * JSONP:JAVADOC:67; JSONP:JAVADOC:70; JSONP:JAVADOC:73; JSONP:JAVADOC:76;
   * JSONP:JAVADOC:80; JSONP:JAVADOC:86; JSONP:JAVADOC:400; JSONP:JAVADOC:401;
   * JSONP:JAVADOC:402; JSONP:JAVADOC:403; JSONP:JAVADOC:404; JSONP:JAVADOC:406;
   * JSONP:JAVADOC:408; JSONP:JAVADOC:409;
   * 
   * @test_Strategy: Tests JsonObject/JsonObjectBuilder API's. Build a
   * JsonObject using the JsonObjectBuilder API's then verify that the Map of
   * JsonObject values matches the expected Map of JsonObject values.
   */
  @Test
  public void jsonObjectTest1() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      System.out.println("Create the expected map of JsonObject values");
      Map<String, JsonValue> expMap = new HashMap<>();
      expMap.put("false", JsonValue.FALSE);
      expMap.put("true", JsonValue.TRUE);
      expMap.put("null", JsonValue.NULL);
      expMap.put("doublemin", JSONP_Util.createJsonNumber(Double.MIN_VALUE));
      expMap.put("doublemax", JSONP_Util.createJsonNumber(Double.MAX_VALUE));
      expMap.put("intmin", JSONP_Util.createJsonNumber(Integer.MIN_VALUE));
      expMap.put("intmax", JSONP_Util.createJsonNumber(Integer.MAX_VALUE));
      expMap.put("longmin", JSONP_Util.createJsonNumber(Long.MIN_VALUE));
      expMap.put("longmax", JSONP_Util.createJsonNumber(Long.MAX_VALUE));
      expMap.put("bigdecimal",
          JSONP_Util.createJsonNumber(BigDecimal.valueOf(123456789.123456789)));
      expMap.put("biginteger",
          JSONP_Util.createJsonNumber(new BigInteger("123456789")));
      expMap.put("string", JSONP_Util.createJsonString("string1"));
      expMap.put("false2", JsonValue.FALSE);
      expMap.put("true2", JsonValue.TRUE);
      expMap.put("null2", JsonValue.NULL);
      expMap.put("object", object);
      expMap.put("array", array);
      JSONP_Util.dumpMap(expMap, "Expected Map");

      System.out.println("Create JsonObject using all JsonObjectBuilder API's");
      JsonObject myJsonObject = Json.createObjectBuilder()
          .add("false", JsonValue.FALSE).add("true", JsonValue.TRUE)
          .add("null", JsonValue.NULL).add("doublemin", Double.MIN_VALUE)
          .add("doublemax", Double.MAX_VALUE).add("intmin", Integer.MIN_VALUE)
          .add("intmax", Integer.MAX_VALUE).add("longmin", Long.MIN_VALUE)
          .add("longmax", Long.MAX_VALUE)
          .add("bigdecimal", BigDecimal.valueOf(123456789.123456789))
          .add("biginteger", new BigInteger("123456789"))
          .add("string", "string1").add("false2", false).add("true2", true)
          .addNull("null2").add("object", object).add("array", array).build();

      Map<String, JsonValue> actMap = myJsonObject;
      JSONP_Util.dumpMap(actMap, "Actual Map");
      System.out.println(
          "Compare actual Map of JsonObject values with expected Map of JsonObject values");
      pass = JSONP_Util.assertEqualsMap(expMap, actMap);
    } catch (Exception e) {
      throw new Fault("jsonObjectTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonObjectTest1 Failed");
  }

  /*
   * @testName: jsonObjectTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:86; JSONP:JAVADOC:58; JSONP:JAVADOC:61;
   * JSONP:JAVADOC:64; JSONP:JAVADOC:67; JSONP:JAVADOC:70; JSONP:JAVADOC:73;
   * JSONP:JAVADOC:76; JSONP:JAVADOC:80; JSONP:JAVADOC:185; JSONP:JAVADOC:400;
   * JSONP:JAVADOC:401; JSONP:JAVADOC:402; JSONP:JAVADOC:403; JSONP:JAVADOC:404;
   * JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409;
   * 
   * @test_Strategy: Tests JsonObject/JsonObjectBuilder API's. Build a
   * JsonObject using the JsonObjectBuilder API's. Write the JsonObject to a
   * JsonWriter and read it back using a JsonReader. Verify that JsonObject
   * written to the JsonWriter and then read back using the JsonReader are
   * equal.
   */
  @Test
  public void jsonObjectTest2() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      System.out.println(
          "Create JsonObject 'myJsonObject1' using all JsonObjectBuilder API's");
      JsonObject myJsonObject1 = Json.createObjectBuilder()
          .add("false", JsonValue.FALSE).add("true", JsonValue.TRUE)
          .add("null", JsonValue.NULL).add("doublemin", Double.MIN_VALUE)
          .add("doublemax", Double.MAX_VALUE).add("intmin", Integer.MIN_VALUE)
          .add("intmax", Integer.MAX_VALUE).add("longmin", Long.MIN_VALUE)
          .add("longmax", Long.MAX_VALUE)
          .add("bigdecimal", BigDecimal.valueOf(123456789.123456789))
          .add("biginteger", new BigInteger("123456789"))
          .add("string", "string1").add("false2", false).add("true2", true)
          .addNull("null2").add("object", object).add("array", array).build();

      System.out.println("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      StringWriter sw = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sw)) {
        writer.writeObject(myJsonObject1);
        System.out.println("Close JsonWriter");
      }
      System.out.println("Save contents of the JsonWriter as a String");
      String contents = sw.toString();
      System.out.println("Dump contents of JsonWriter as a String");
      System.out.println("JsonWriterContents=" + contents);
      System.out.println(
          "Read the JsonObject back into 'myJsonObject2' using a JsonReader");
      JsonObject myJsonObject2;
      try (JsonReader reader = Json.createReader(new StringReader(contents))) {
        myJsonObject2 = reader.readObject();
        System.out.println("Save contents of the JsonReader as a String");
        contents = reader.toString();
      }
      System.out.println("Dump contents of JsonReader as a String");
      System.out.println("JsonReaderContents=" + contents);

      System.out.println("Compare myJsonObject1 and myJsonObject2 for equality");
      pass = JSONP_Util.assertEqualsJsonObjects(myJsonObject1, myJsonObject2);
    } catch (Exception e) {
      throw new Fault("jsonObjectTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonObjectTest2 Failed");
  }

  /*
   * @testName: jsonObjectTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:58; JSONP:JAVADOC:61; JSONP:JAVADOC:64;
   * JSONP:JAVADOC:67; JSONP:JAVADOC:70; JSONP:JAVADOC:73; JSONP:JAVADOC:76;
   * JSONP:JAVADOC:80; JSONP:JAVADOC:86; JSONP:JAVADOC:215; JSONP:JAVADOC:101;
   * JSONP:JAVADOC:403; JSONP:JAVADOC:264; JSONP:JAVADOC:265; JSONP:JAVADOC:436;
   * JSONP:JAVADOC:400; JSONP:JAVADOC:401; JSONP:JAVADOC:402; JSONP:JAVADOC:404;
   * JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409; JSONP:JAVADOC:439;
   * JSONP:JAVADOC:441; JSONP:JAVADOC:443; JSONP:JAVADOC:527; JSONP:JAVADOC:529;
   * JSONP:JAVADOC:531; JSONP:JAVADOC:533; JSONP:JAVADOC:539;
   * 
   * @test_Strategy: Tests JsonObject/JsonObjectBuilder API's. Build a
   * JsonObject using the JsonObjectBuilder API's. Verify contents of JsonObject
   * using JsonObject().getJsonArray(String),
   * JsonObject().getJsonNumber(String), JsonObject().getJsonObject(String),
   * JsonObject().getJsonString(String), JsonObject.getInt(String) and
   * JsonObject.getString(String), JsonObject.getBoolean(String),
   * JsonObject.getBoolean(String, boolean), JsonObject.getInt(String, int),
   * JsonObject.getString(String, String)
   */
  @Test
  public void jsonObjectTest3() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject object = JSONP_Util.createSampleJsonObject();

      System.out.println("Create sample JsonArray for testing");
      JsonArray array = JSONP_Util.createSampleJsonArray();

      System.out.println("Create myObject JsonObject with 22 name/value pairs");
      JsonObject myObject = Json.createObjectBuilder().add("key0", -1)
          .add("key1", +1).add("key2", 1).add("key3", -1e3).add("key4", +1e3)
          .add("key5", 1e3).add("key6", -2E3).add("key7", +2E3).add("key8", 2E3)
          .add("key9", Long.MAX_VALUE).add("key10", Long.MIN_VALUE)
          .add("key11", Integer.MAX_VALUE).add("key12", Integer.MIN_VALUE)
          .add("key13", Double.MAX_VALUE).add("key14", Double.MIN_VALUE)
          .add("key15", BigDecimal.valueOf(123456789.123456789))
          .add("key16", new BigInteger("123456789"))
          .add("key17", JsonValue.TRUE).add("key18", JsonValue.FALSE)
          .add("key19", JsonValue.NULL).add("key20", JSONP_Data.asciiCharacters)
          .add("key21", false).add("key22", true).addNull("key23")
          .add("key24", object).add("key25", array).build();
      System.out.println("Checking intValue of key0 for correctness");
      if (!JSONP_Util.assertEquals(-1,
          myObject.getJsonNumber("key0").intValue()))
        pass = false;
      System.out.println("key0 via JsonNumber.toString()="
          + myObject.getJsonNumber("key0").toString());
      System.out.println("Checking intValue of key1 for correctness");
      if (!JSONP_Util.assertEquals(1, myObject.getInt("key1")))
        pass = false;
      System.out.println("key1 via JsonNumber.toString()=" + myObject.getInt("key1"));
      System.out.println("Checking intValue of key2 for correctness");
      if (!JSONP_Util.assertEquals(1,
          myObject.getJsonNumber("key2").intValue()))
        pass = false;
      System.out.println("key2 via JsonNumber.toString()="
          + myObject.getJsonNumber("key2").toString());
      System.out.println("Checking intValue of key3 for correctness");
      if (!JSONP_Util.assertEquals(-1000, myObject.getInt("key3")))
        pass = false;
      System.out.println("key3 via JsonNumber.toString()="
          + myObject.getJsonNumber("key3").toString());
      System.out.println("Checking intValue of key4 for correctness");
      if (!JSONP_Util.assertEquals(1000,
          myObject.getJsonNumber("key4").intValue()))
        pass = false;
      System.out.println("key4 via JsonNumber.toString()="
          + myObject.getJsonNumber("key4").toString());
      System.out.println("Checking intValue of key5 for correctness");
      if (!JSONP_Util.assertEquals(1000,
          myObject.getJsonNumber("key5").intValue()))
        pass = false;
      System.out.println("key5 via JsonNumber.toString()="
          + myObject.getJsonNumber("key5").toString());
      System.out.println("Checking intValue of key6 for correctness");
      if (!JSONP_Util.assertEquals(-2000,
          myObject.getJsonNumber("key6").intValue()))
        pass = false;
      System.out.println("key6 via JsonNumber.toString()="
          + myObject.getJsonNumber("key6").toString());
      System.out.println("Checking intValue of key7 for correctness");
      if (!JSONP_Util.assertEquals(2000,
          myObject.getJsonNumber("key7").intValue()))
        pass = false;
      System.out.println("key7 via JsonNumber.toString()="
          + myObject.getJsonNumber("key7").toString());
      System.out.println("Checking intValue of key8 for correctness");
      if (!JSONP_Util.assertEquals(2000,
          myObject.getJsonNumber("key8").intValue()))
        pass = false;
      System.out.println("key8 via JsonNumber.toString()="
          + myObject.getJsonNumber("key8").toString());
      System.out.println("Checking longValue of key9 for correctness");
      if (!JSONP_Util.assertEquals(Long.MAX_VALUE,
          myObject.getJsonNumber("key9").longValue()))
        pass = false;
      System.out.println("LongMax via JsonNumber.toString()="
          + myObject.getJsonNumber("key9").toString());
      if (!JSONP_Util.assertEquals("" + Long.MAX_VALUE,
          myObject.getJsonNumber("key9").toString()))
        pass = false;
      System.out.println("Checking longValue of key10 for correctness");
      if (!JSONP_Util.assertEquals(Long.MIN_VALUE,
          myObject.getJsonNumber("key10").longValue()))
        pass = false;
      System.out.println("LongMin via JsonNumber.toString()="
          + myObject.getJsonNumber("key10").toString());
      if (!JSONP_Util.assertEquals("" + Long.MIN_VALUE,
          myObject.getJsonNumber("key10").toString()))
        pass = false;
      System.out.println("Checking intValue of key11 for correctness");
      if (!JSONP_Util.assertEquals(Integer.MAX_VALUE,
          myObject.getJsonNumber("key11").intValue()))
        pass = false;
      System.out.println("IntMax via JsonNumber.toString()="
          + myObject.getJsonNumber("key11").toString());
      if (!JSONP_Util.assertEquals("" + Integer.MAX_VALUE,
          myObject.getJsonNumber("key11").toString()))
        pass = false;
      System.out.println("Checking intValue of key12 for correctness");
      if (!JSONP_Util.assertEquals(Integer.MIN_VALUE,
          myObject.getJsonNumber("key12").intValue()))
        pass = false;
      System.out.println("IntMin via JsonNumber.toString()="
          + myObject.getJsonNumber("key12").toString());
      if (!JSONP_Util.assertEquals("" + Integer.MIN_VALUE,
          myObject.getJsonNumber("key12").toString()))
        pass = false;
      System.out.println("Checking doubleValue of key13 for correctness");
      if (!JSONP_Util.assertEquals(Double.MAX_VALUE,
          myObject.getJsonNumber("key13").doubleValue()))
        pass = false;
      System.out.println("Checking doubleValue of key14 for correctness");
      if (!JSONP_Util.assertEquals(Double.MIN_VALUE,
          myObject.getJsonNumber("key14").doubleValue()))
        pass = false;
      System.out.println("Checking bigDecimalValue of key15 for correctness");
      if (!JSONP_Util.assertEquals(BigDecimal.valueOf(123456789.123456789),
          myObject.getJsonNumber("key15").bigDecimalValue()))
        pass = false;
      System.out.println("Checking bigIntegerValue of key16 for correctness");
      if (!JSONP_Util.assertEquals(new BigInteger("123456789"),
          myObject.getJsonNumber("key16").bigIntegerValue()))
        pass = false;
      System.out.println("Checking getBoolean of key17 for correctness");
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key17")))
        pass = false;
      System.out.println("Checking getBoolean of key18 for correctness");
      if (!JSONP_Util.assertEquals(false, myObject.getBoolean("key18")))
        pass = false;
      System.out.println("Checking isNull of key19 for correctness");
      if (!JSONP_Util.assertEquals(true, myObject.isNull("key19")))
        pass = false;
      System.out.println("Checking getJsonString of key20 for correctness");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myObject.getJsonString("key20").getString()))
        pass = false;
      System.out.println("Checking getString of key20 for correctness");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myObject.getString("key20")))
        pass = false;
      System.out.println("Checking getBoolean of key21 for correctness");
      if (!JSONP_Util.assertEquals(false, myObject.getBoolean("key21")))
        pass = false;
      System.out.println("Checking getBoolean of key22 for correctness");
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key22")))
        pass = false;
      System.out.println("Checking isNull of key23 for correctness");
      if (!JSONP_Util.assertEquals(true, myObject.isNull("key23")))
        pass = false;
      System.out.println("Checking getJsonObject of key24 for correctness");
      if (!JSONP_Util.assertEqualsJsonObjects(object,
          myObject.getJsonObject("key24")))
        pass = false;
      System.out.println("Checking getJsonArray of key25 for correctness");
      if (!JSONP_Util.assertEqualsJsonArrays(array,
          myObject.getJsonArray("key25")))
        pass = false;

      // Verify calls to JsonObject.getBoolean(int)
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key17")))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myObject.getBoolean("key18")))
        pass = false;

      // Verify calls to JsonObject.getBoolean(String, boolean)
      System.out.println(
          "Testing JsonObject.getBoolean(String, boolean) with/without default value setting.");
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key17", false)))
        pass = false;
      if (!JSONP_Util.assertEquals(false, myObject.getBoolean("key18", true)))
        pass = false;
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key0", true)))
        pass = false;
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key19", true)))
        pass = false;
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key20", true)))
        pass = false;
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key24", true)))
        pass = false;
      if (!JSONP_Util.assertEquals(true, myObject.getBoolean("key25", true)))
        pass = false;

      // Verify calls to JsonObject.getInt(String, int)
      System.out.println(
          "Testing JsonObject.getInt(String, int) with/without default value setting.");
      if (!JSONP_Util.assertEquals(-1, myObject.getInt("key0", 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myObject.getInt("key18", 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myObject.getInt("key19", 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myObject.getInt("key20", 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myObject.getInt("key24", 10)))
        pass = false;
      if (!JSONP_Util.assertEquals(10, myObject.getInt("key25", 10)))
        pass = false;

      // Verify calls to JsonObject.getString(String, String)
      System.out.println(
          "Testing JsonObject.getString(String, String) with/without default value setting.");
      if (!JSONP_Util.assertEquals(JSONP_Data.asciiCharacters,
          myObject.getString("key20", "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myObject.getString("key0", "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myObject.getString("key18", "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myObject.getString("key19", "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myObject.getString("key24", "foo")))
        pass = false;
      if (!JSONP_Util.assertEquals("foo", myObject.getString("key25", "foo")))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonObjectTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonObjectTest3 Failed");
  }

  /*
   * @testName: jsonObjectTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:61; JSONP:JAVADOC:70; JSONP:JAVADOC:58;
   * JSONP:JAVADOC:400; JSONP:JAVADOC:401; JSONP:JAVADOC:403; JSONP:JAVADOC:404;
   * JSONP:JAVADOC:406; JSONP:JAVADOC:408; JSONP:JAVADOC:409; JSONP:JAVADOC:438;
   * 
   * @test_Strategy: Build a JsonObject and than write the JsonObject. Compare
   * the Json text from the writer contents with the expected Json text output
   * expected based on the JsonObject.
   */
  @Test
  public void jsonObjectTest4() throws Fault {
    boolean pass = true;
    try {
      System.out.println("Create sample JsonObject for testing");
      JsonObject myJsonObject1 = JSONP_Util.createSampleJsonObject();
      System.out.println("Write the JsonObject 'myJsonObject1' out to a JsonWriter");
      StringWriter sw = new StringWriter();
      try (JsonWriter writer = Json.createWriter(sw)) {
        writer.writeObject(myJsonObject1);
        System.out.println("Close JsonWriter");
      }
      System.out.println("Save contents of the JsonWriter as a String");
      String contents = sw.toString();
      System.out.println("Dump contents of JsonWriter as a String");
      System.out.println("JsonWriterContents=" + contents);
      System.out.println("Remove whitespace from contents.");
      String actJsonText = JSONP_Util.removeWhitespace(contents);
      System.out.println(
          "Compare expected JsonObject text with actual JsonObject text for equality");
      pass = JSONP_Util.assertEqualsJsonText(
          JSONP_Util.EXPECTED_SAMPLEJSONOBJECT_TEXT, actJsonText);
    } catch (Exception e) {
      throw new Fault("jsonObjectTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonObjectTest4 Failed");
  }

  /*
   * @testName: jsonObjectExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:43; JSONP:JAVADOC:47; JSONP:JAVADOC:50;
   * JSONP:JAVADOC:344; JSONP:JAVADOC:345; JSONP:JAVADOC:79; JSONP:JAVADOC:437;
   * JSONP:JAVADOC:440; JSONP:JAVADOC:442; JSONP:JAVADOC:528; JSONP:JAVADOC:530;
   * JSONP:JAVADOC:532; JSONP:JAVADOC:534; JSONP:JAVADOC:540;
   * 
   * @test_Strategy: Test JSON exception conditions. Trips the exceptions:
   * java.lang.ArithmeticException java.lang.ClassCastException
   * java.lang.NumberFormatException java.lang.UnsupportedOperationException
   * java.lang.NullPointerException
   */
  @Test
  public void jsonObjectExceptionTests() throws Fault {
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
          "Trip ClassCastException trying to cast a JsonObject to JsonNumber via getJsonNumber(String)");
      JsonNumber value = testObject.getJsonNumber("address");
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
          "Trip ClassCastException trying to cast a JsonObject to JsonString via getJsonString(String)");
      JsonString value = testObject.getJsonString("address");
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
          "Trip ClassCastException trying to cast a JsonObject to JsonArray via getJsonArray(String)");
      JsonArray value = testObject.getJsonArray("address");
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
          "Trip ClassCastException trying to cast a JsonArray to JsonNumber via getNumber(String)");
      JsonNumber value = testObject.getJsonNumber("phoneNumber");
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
          "Trip ClassCastException trying to cast a JsonArray to JsonString via getJsonString(String)");
      JsonString value = testObject.getJsonString("phoneNumber");
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
          "Trip ClassCastException trying to cast a JsonObject to String via getString(String)");
      String value = testObject.getString("address");
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
          "Trip ClassCastException trying to cast a JsonArray to String via getString(String)");
      String value = testObject.getString("phoneNumber");
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
          "Trip ClassCastException trying to cast a JsonObject to int via getInt(String)");
      int value = testObject.getInt("address");
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
          "Trip ClassCastException trying to cast a JsonArray to int via getInt(String)");
      int value = testObject.getInt("phoneNumber");
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
          "Trip ClassCastException trying to cast a JsonArray to JsonObject via getJsonObject(String)");
      JsonObject value = testObject.getJsonObject("phoneNumber");
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
          "Trip ClassCastException trying to cast a JsonNumber to JsonString via getJsonString(String)");
      JsonString value = testObject.getJsonString("age");
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
          "Trip ClassCastException trying to cast a JsonNumber to JsonObject via getJsonNumber(String)");
      JsonObject value = testObject.getJsonObject("age");
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
          "Trip ClassCastException trying to cast a JsonNumber to JsonArray via getJsonArray(String)");
      JsonArray value = testObject.getJsonArray("age");
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
          "Trip ClassCastException trying to cast a JsonString to JsonNumber via getJsonNumber(String)");
      JsonNumber value = testObject.getJsonNumber("firstName");
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
          "Trip ClassCastException trying to cast a JsonString to int via getInt(String)");
      int value = testObject.getInt("firstName");
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
          "Trip ClassCastException trying to cast a JsonString to JsonObject via getJsonString(String)");
      JsonObject value = testObject.getJsonObject("firstName");
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
          "Trip ClassCastException trying to cast a JsonString to JsonArray via getJsonArray(String)");
      JsonArray value = testObject.getJsonArray("firstName");
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
          "Trip ClassCastException trying to cast a JsonValue.FALSE to JsonNumber via getJsonNumber(String)");
      JsonNumber value = testObject.getJsonNumber("elderly");
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
          "Trip ClassCastException trying to cast a JsonValue.FALSE to JsonString via getJsonString(String)");
      JsonString value = testObject.getJsonString("elderly");
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
          "Trip ClassCastException trying to cast a JsonValue.FALSE to JsonObject via getJsonObject(String)");
      JsonObject value = testObject.getJsonObject("elderly");
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
          "Trip ClassCastException trying to cast a JsonValue.FALSE to JsonArray via getJsonArray(String)");
      JsonArray value = testObject.getJsonArray("elderly");
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
          "Trip ClassCastException trying to cast a non JsonValue.FALSE|JsonValue.TRUE to boolean via getBoolean(String)");
      boolean value = testObject.getBoolean("firstName");
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
          "Trip ClassCastException trying to cast a non JsonValue.FALSE|JsonValue.TRUE to boolean via getBoolean(String)");
      boolean value = testObject.getBoolean("age");
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
          "Trip ClassCastException trying to cast a non JsonValue.FALSE|JsonValue.TRUE to boolean via getBoolean(String)");
      boolean value = testObject.getBoolean("objectOfFooBar");
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
          "Trip ClassCastException trying to cast a non JsonValue.FALSE|JsonValue.TRUE to boolean via getBoolean(String)");
      boolean value = testObject.getBoolean("arrayOfFooBar");
      pass = false;
      System.err.println("Failed to throw ClassCastException");
    } catch (ClassCastException e) {
      System.out.println("Got expected ClassCastException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Testing NumberFormatException calling add(String, Double.NaN)
    try {
      System.out.println("Trip NumberFormatException calling add(String, Double.NaN)");
      JsonObject object = Json.createObjectBuilder().add("double", Double.NaN)
          .build();
      pass = false;
      System.err.println("Failed to throw NumberFormatException");
    } catch (NumberFormatException e) {
      System.out.println("Got expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Testing NumberFormatException calling add(String,
    // Double.NEGATIVE_INFINITY)
    try {
      System.out.println(
          "Trip NumberFormatException calling add(String, Double.NEGATIVE_INFINITY)");
      JsonObject object = Json.createObjectBuilder()
          .add("double", Double.NEGATIVE_INFINITY).build();
      pass = false;
      System.err.println("Failed to throw NumberFormatException");
    } catch (NumberFormatException e) {
      System.out.println("Got expected NumberFormatException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Testing NumberFormatException calling add(String,
    // Double.POSITIVE_INFINITY)
    try {
      System.out.println(
          "Trip NumberFormatException calling add(String, Double.POSITIVE_INFINITY)");
      JsonObject object = Json.createObjectBuilder()
          .add("double", Double.POSITIVE_INFINITY).build();
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
          "Trip ArithmeticException calling add(\"number\", 12345.12345) and attempting to extract as an exact integer value");
      JsonObject object = Json.createObjectBuilder().add("number", 12345.12345)
          .build();
      System.out.println("Call JsonObject.getJsonNumber(\"number\").intValueExact()");
      int value = object.getJsonNumber("number").intValueExact();
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
          "Trip ArithmeticException calling add(\"number\", 12345.12345) and attempting to extract as an exact long value");
      JsonObject object = Json.createObjectBuilder().add("number", 12345.12345)
          .build();
      System.out.println("Call JsonObject.getJsonNumber(\"number\").longValueExact()");
      long value = object.getJsonNumber("number").longValueExact();
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
          "Trip ArithmeticException calling add(\"number\", 12345.12345) and attempting to extract as an exact biginteger value");
      JsonObject object = Json.createObjectBuilder().add("number", 12345.12345)
          .build();
      System.out.println(
          "Call JsonObject.getJsonNumber(\"number\").bigIntegerValueExact()");
      BigInteger value = object.getJsonNumber("number").bigIntegerValueExact();
      pass = false;
      System.err.println("Failed to throw ArithmeticException");
    } catch (ArithmeticException e) {
      System.out.println("Got expected ArithmeticException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Tests for UnsupportedOperationException using Collection methods to
    // modify JsonObject Map

    // Trip UnsupportedOperationException
    try {
      System.out.println(
          "Trip UnsupportedOperationException JsonObject.put(K,V) trying to modify JsonObject map which should be immutable");
      testObject.put("foo", JsonValue.FALSE);
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
          "Trip UnsupportedOperationException JsonObject.putAll(Map) trying to modify JsonObject map which should be immutable");
      testObject.putAll(testObject);
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
          "Trip UnsupportedOperationException JsonObject.clear() trying to modify JsonObject map which should be immutable");
      testObject.clear();
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
          "Trip UnsupportedOperationException JsonObject.remove(K) trying to modify JsonObject map which should be immutable");
      testObject.remove("firstName");
      pass = false;
      System.err.println("Failed to throw UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      System.out.println("Got expected UnsupportedOperationException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    // Trip NullPointerException
    try {
      System.out.println(
          "Trip NullPointerException for JsonObject.getBoolean(String) when no mapping exists for name.");
      boolean value = testObject.getBoolean("foo");
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
          "Trip NullPointerException for JsonObject.getInt(String) when no mapping exists for name.");
      int value = testObject.getInt("foo");
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
          "Trip NullPointerException for JsonObject.getString(String) when no mapping exists for name.");
      String value = testObject.getString("foo");
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
          "Trip NullPointerException for JsonObject.isNull(String) when no mapping exists for name.");
      boolean value = testObject.isNull("foo");
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonObjectExceptionTests Failed");
  }

  /*
   * @testName: jsonObjectNullNameValueExceptionTests
   * 
   * @assertion_ids: JSONP:JAVADOC:561; JSONP:JAVADOC:562; JSONP:JAVADOC:563;
   * JSONP:JAVADOC:564; JSONP:JAVADOC:565; JSONP:JAVADOC:566; JSONP:JAVADOC:567;
   * JSONP:JAVADOC:568; JSONP:JAVADOC:569; JSONP:JAVADOC:570; JSONP:JAVADOC:571;
   * 
   * @test_Strategy: Test JSON NPE exception conditions when attempting to add a
   * specified name or value that is null.
   */
  @Test
  public void jsonObjectNullNameValueExceptionTests() throws Fault {
    boolean pass = true;
    JsonObjectBuilder job = Json.createObjectBuilder();

    // Trip NullPointerException
    try {
      System.out.println(
          "Trip NullPointerException for JsonObjectBuilder.add(String, JsonValue) when name is null.");
      job.add(null, JsonValue.TRUE);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, JsonValue) when value is null.");
      job.add("name", (JsonValue) null);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, String) when name is null.");
      job.add(null, "value");
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, String) when value is null.");
      job.add("name", (String) null);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, BigInteger) when name is null.");
      job.add(null, new BigInteger("123456789"));
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, BigInteger) when value is null.");
      job.add("name", (BigInteger) null);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, BigDecimal) when name is null.");
      job.add(null, new BigDecimal("123456789"));
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, BigDecimal) when value is null.");
      job.add("name", (BigDecimal) null);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, int) when name is null.");
      job.add(null, 123456789);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, long) when name is null.");
      job.add(null, 123456789L);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, double) when name is null.");
      job.add(null, 123456.789);
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
          "Trip NullPointerException for JsonObject.add(String, boolean) when name is null.");
      job.add(null, true);
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
          "Trip NullPointerException for JsonObject.addNull(String) when name is null.");
      job.addNull(null);
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, JsonObjectBuilder) when name is null.");
      job.add(null, Json.createObjectBuilder());
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
          "Trip NullPointerException for JsonObjectBuilder.add(String, JsonObjectBuilder) when value is null.");
      job.add("name", (JsonObjectBuilder) null);
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
          "Trip NullPointerException for JsonArrayBuilder.add(String, JsonArrayBuilder) when name is null.");
      job.add(null, Json.createArrayBuilder());
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
          "Trip NullPointerException for JsonArrayBuilder.add(String, JsonArrayBuilder) when value is null.");
      job.add("name", (JsonArrayBuilder) null);
      pass = false;
      System.err.println("Failed to throw NullPointerException");
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      pass = false;
      System.err.println("Caught unexpected exception: " + e);
    }

    if (!pass)
      throw new Fault("jsonObjectNullNameValueExceptionTests Failed");
  }

  /*
   * @testName: jsonCreateObjectBuilder11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:577; JSONP:JAVADOC:578; JSONP:JAVADOC:656;
   * JSONP:JAVADOC:657;
   *
   * @test_Strategy: Tests JsonObjectBuilder API factory methods added in JSON-P
   * 1.1.
   */
  @Test
  public void jsonCreateObjectBuilder11Test() throws Fault {
    CreateObjectBuilder createTest = new CreateObjectBuilder();
    final TestResult result = createTest.test();
    result.eval();
  }

  /*
   * @testName: jsonObjectBuilder11Test
   * 
   * @assertion_ids: JSONP:JAVADOC:618; JSONP:JAVADOC:619;
   * 
   * @test_Strategy: Tests JsonObjectBuilder API methods added in JSON-P 1.1.
   */
  @Test
  public void jsonObjectBuilder11Test() throws Fault {
    ObjectBuild buildTest = new ObjectBuild();
    final TestResult result = buildTest.test();
    result.eval();
  }

}

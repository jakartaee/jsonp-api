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
package jakarta.jsonp.tck.pluggability.jsonprovidertests;

import jakarta.json.*;
import jakarta.json.spi.JsonProvider;
import jakarta.json.stream.*;

import java.io.*;
import java.nio.charset.Charset;

import java.util.Properties;
import java.util.ServiceLoader;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;
import jakarta.jsonp.tck.provider.MyJsonProvider;
import jakarta.jsonp.tck.provider.MyJsonGenerator;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }

  private static final String MY_JSONPROVIDER_CLASS = "jakarta.jsonp.tck.provider.MyJsonProvider";

  private String providerPath = null;
  
  @After
  public void after() {
      MyJsonProvider.clearCalls();
      MyJsonGenerator.clearCalls();
  }

  /* Tests */

  /*
   * @testName: jsonProviderTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:152;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * static JsonProvider provider()
   */
  @Test
  public void jsonProviderTest1() throws Fault {
    boolean pass = true;
    try {
      // Load my provider
      JsonProvider provider = JsonProvider.provider();
      String providerClass = provider.getClass().getName();
      System.out.println("provider class=" + providerClass);
      if (providerClass.equals(MY_JSONPROVIDER_CLASS))
        System.out.println("Current provider is my provider - expected.");
      else {
        System.err.println("Current provider is not my provider - unexpected.");
        pass = false;
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        List<JsonProvider> providers = new ArrayList<>();
        while(it.hasNext()) {
            providers.add(it.next());
        }
        System.out.println("Providers: "+providers);
      }
    } catch (Exception e) {
      throw new Fault("jsonProviderTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest1 Failed");
  }

  /*
   * @testName: jsonProviderTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:144;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGenerator createGenerator(Writer)
   */
  @Test
  public void jsonProviderTest2() throws Fault {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(Writer)";
    String expString2 = "public JsonGenerator writeStartArray()";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonGenerator generator = Json.createGenerator(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartArray();
      String actString2 = MyJsonGenerator.getCalls();
      System.out.println("Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest2 Failed");
  }

  /*
   * @testName: jsonProviderTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:192;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGenerator createGenerator(OutputStream)
   */
  @Test
  public void jsonProviderTest3() throws Fault {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(OutputStream)";
    String expString2 = "public JsonGenerator writeStartObject()";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonGenerator generator = Json
          .createGenerator(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartObject();
      String actString2 = MyJsonGenerator.getCalls();
      System.out.println("Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest3 Failed");
  }

  /*
   * @testName: jsonProviderTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:146;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(Reader)
   */
  @Test
  public void jsonProviderTest4() throws Fault {
    boolean pass = true;
    String expString = "public JsonParser createParser(Reader)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonParser parser = Json.createParser(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest4 Failed");
  }

  /*
   * @testName: jsonProviderTest5
   * 
   * @assertion_ids: JSONP:JAVADOC:196;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(InputStream)
   */
  @Test
  public void jsonProviderTest5() throws Fault {
    boolean pass = true;
    String expString = "public JsonParser createParser(InputStream)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonParser parser = Json
          .createParser(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest5 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest5 Failed");
  }

  /*
   * @testName: jsonProviderTest6
   * 
   * @assertion_ids: JSONP:JAVADOC:465;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParserFactory createParserFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest6() throws Fault {
    boolean pass = true;
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest6 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest6 Failed");
  }

  /*
   * @testName: jsonProviderTest7
   * 
   * @assertion_ids: JSONP:JAVADOC:426;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParserFactory createParserFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest7() throws Fault {
    boolean pass = true;
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest7 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest7 Failed");
  }

  /*
   * @testName: jsonProviderTest8
   * 
   * @assertion_ids: JSONP:JAVADOC:425;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest8() throws Fault {
    boolean pass = true;
    String expString = "public JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest8 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest8 Failed");
  }

  /*
   * @testName: jsonProviderTest9
   * 
   * @assertion_ids: JSONP:JAVADOC:472;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriterFactory createWriterFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest9() throws Fault {
    boolean pass = true;
    String expString = "public JsonWriterFactory createWriterFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonWriterFactory factory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest9 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest9 Failed");
  }

  /*
   * @testName: jsonProviderTest10
   * 
   * @assertion_ids: JSONP:JAVADOC:223;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonParser createParser(InputStream) Tests the case where a JsonException
   * can be thrown. An InputStream of null will cause MyJsonProvider to throw
   * JsonException.
   */
  @Test
  public void jsonProviderTest10() throws Fault {
    boolean pass = true;
    String expString = "public JsonParser createParser(InputStream)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      InputStream in = null;
      JsonParser parser = Json.createParser(in);
      pass = false;
    } catch (JsonException e) {
      System.out.println("Caught expected JsonException: " + e);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest10 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest10 Failed");
  }

  /*
   * @testName: jsonProviderTest11
   * 
   * @assertion_ids: JSONP:JAVADOC:464;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonArrayBuilder createArrayBuilder()
   */
  @Test
  public void jsonProviderTest11() throws Fault {
    boolean pass = true;
    String expString = "public JsonArrayBuilder createArrayBuilder()";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest11 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest11 Failed");
  }

  /*
   * @testName: jsonProviderTest12
   * 
   * @assertion_ids: JSONP:JAVADOC:466;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonObjectBuilder createObjectBuilder()
   */
  @Test
  public void jsonProviderTest12() throws Fault {
    boolean pass = true;
    String expString = "public JsonObjectBuilder createObjectBuilder()";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest12 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest12 Failed");
  }

  /*
   * @testName: jsonProviderTest13
   * 
   * @assertion_ids: JSONP:JAVADOC:465;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonBuilderFactory createBuilderFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest13() throws Fault {
    boolean pass = true;
    String expString = "public JsonBuilderFactory createBuilderFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonBuilderFactory objectBuilder = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest13 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest13 Failed");
  }

  /*
   * @testName: jsonProviderTest14
   * 
   * @assertion_ids: JSONP:JAVADOC:467;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReader createReader(Reader)
   */
  @Test
  public void jsonProviderTest14() throws Fault {
    boolean pass = true;
    String expString = "public JsonReader createReader(Reader)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonReader reader = Json.createReader(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest14 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest14 Failed");
  }

  /*
   * @testName: jsonProviderTest15
   * 
   * @assertion_ids: JSONP:JAVADOC:468;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReader createReader(InputStream)
   */
  @Test
  public void jsonProviderTest15() throws Fault {
    boolean pass = true;
    String expString = "public JsonReader createReader(InputStream)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonReader reader = Json
          .createReader(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest15 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest15 Failed");
  }

  /*
   * @testName: jsonProviderTest16
   * 
   * @assertion_ids: JSONP:JAVADOC:470;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriter createWriter(Writer)
   */
  @Test
  public void jsonProviderTest16() throws Fault {
    boolean pass = true;
    String expString = "public JsonWriter createWriter(Writer)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest16 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest16 Failed");
  }

  /*
   * @testName: jsonProviderTest17
   * 
   * @assertion_ids: JSONP:JAVADOC:471;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonWriter createWriter(OutputStream)
   */
  @Test
  public void jsonProviderTest17() throws Fault {
    boolean pass = true;
    String expString = "public JsonWriter createWriter(OutputStream)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest17 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest17 Failed");
  }

  /*
   * @testName: jsonProviderTest18
   * 
   * @assertion_ids: JSONP:JAVADOC:469;
   * 
   * @test_Strategy: Test call of SPI provider method with signature: o public
   * JsonReaderFactory createReaderFactory(Map<String, ?>)
   */
  @Test
  public void jsonProviderTest18() throws Fault {
    boolean pass = true;
    String expString = "public JsonReaderFactory createReaderFactory(Map<String, ?>)";
    try {
      System.out.println("Calling SPI provider method: " + expString);
      JsonReaderFactory factory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      System.out.println("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
    } catch (Exception e) {
      throw new Fault("jsonProviderTest18 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonProviderTest18 Failed");
  }
}

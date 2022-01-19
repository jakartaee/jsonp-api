/*
 * Copyright (c) 2021, 2022 Oracle and/or its affiliates. All rights reserved.
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

import java.util.ServiceLoader;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import jakarta.jsonp.tck.common.JSONP_Util;
import jakarta.jsonp.tck.provider.MyJsonProvider;
import jakarta.jsonp.tck.provider.MyJsonGenerator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
public class ClientTests {

  private static final String MY_JSONPROVIDER_CLASS = "jakarta.jsonp.tck.provider.MyJsonProvider";
  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  private String providerPath = null;

  @Deployment
  public static WebArchive createTestArchive() {
    return ShrinkWrap.create(WebArchive.class)
        .addPackages(true, ClientTests.class.getPackage().getName());
  }
  
  @AfterEach
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
  public void jsonProviderTest1() {
    boolean pass = true;
    try {
      // Load my provider
      JsonProvider provider = JsonProvider.provider();
      String providerClass = provider.getClass().getName();
      LOGGER.info("provider class=" + providerClass);
      if (providerClass.equals(MY_JSONPROVIDER_CLASS))
        LOGGER.info("Current provider is my provider - expected.");
      else {
        LOGGER.warning("Current provider is not my provider - unexpected.");
        pass = false;
        ServiceLoader<JsonProvider> loader = ServiceLoader.load(JsonProvider.class);
        Iterator<JsonProvider> it = loader.iterator();
        List<JsonProvider> providers = new ArrayList<>();
        while(it.hasNext()) {
            providers.add(it.next());
        }
        LOGGER.info("Providers: "+providers);
      }
    } catch (Exception e) {
      fail("jsonProviderTest1 Failed: ", e);
    }
    assertTrue(pass, "jsonProviderTest1 Failed");
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
  public void jsonProviderTest2() {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(Writer)";
    String expString2 = "public JsonGenerator writeStartArray()";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonGenerator generator = Json.createGenerator(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartArray();
      String actString2 = MyJsonGenerator.getCalls();
      LOGGER.info("Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      fail("jsonProviderTest2 Failed: ", e);
    }
    assertTrue(pass, "jsonProviderTest2 Failed");
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
  public void jsonProviderTest3() {
    boolean pass = true;
    String expString = "public JsonGenerator createGenerator(OutputStream)";
    String expString2 = "public JsonGenerator writeStartObject()";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonGenerator generator = Json
          .createGenerator(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      pass = JSONP_Util.assertEquals(expString, actString);
      generator.writeStartObject();
      String actString2 = MyJsonGenerator.getCalls();
      LOGGER.info("Verify SPI generator method was called: " + expString2);
      pass = JSONP_Util.assertEquals(expString2, actString2);
    } catch (Exception e) {
      fail("jsonProviderTest3 Failed: ", e);
    }
    assertTrue(pass, "jsonProviderTest3 Failed");
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
  public void jsonProviderTest4() {
    String expString = "public JsonParser createParser(Reader)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonParser parser = Json.createParser(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest4 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest4 Failed: ", e);
    }
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
  public void jsonProviderTest5() {
    String expString = "public JsonParser createParser(InputStream)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonParser parser = Json
          .createParser(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest5 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest5 Failed: ", e);
    }
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
  public void jsonProviderTest6() {
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest5 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest6 Failed: ", e);
    }
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
  public void jsonProviderTest7() {
    String expString = "public JsonParserFactory createParserFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonParserFactory parserFactory = Json
          .createParserFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest7 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest7 Failed: ", e);
    }
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
  public void jsonProviderTest8() {
    String expString = "public JsonGeneratorFactory createGeneratorFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonGeneratorFactory generatorFactory = Json
          .createGeneratorFactory(new HashMap<String, Object>());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest8 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest8 Failed: ", e);
    }
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
  public void jsonProviderTest9() {
    String expString = "public JsonWriterFactory createWriterFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonWriterFactory factory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest9 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest9 Failed: ", e);
    }
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
  public void jsonProviderTest10() {
    String expString = "public JsonParser createParser(InputStream)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      InputStream in = null;
      JsonParser parser = Json.createParser(in);
      fail("jsonProviderTest10 Failed");
    } catch (JsonException e) {
      LOGGER.info("Caught expected JsonException: " + e);
    } catch (Exception e) {
      fail("jsonProviderTest10 Failed: ", e);
    }
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
  public void jsonProviderTest11() {
    String expString = "public JsonArrayBuilder createArrayBuilder()";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest11 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest11 Failed: ", e);
    }
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
  public void jsonProviderTest12() {
    String expString = "public JsonObjectBuilder createObjectBuilder()";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest12 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest12 Failed: ", e);
    }
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
  public void jsonProviderTest13() {
    String expString = "public JsonBuilderFactory createBuilderFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonBuilderFactory objectBuilder = Json
          .createBuilderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest13 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest13 Failed: ", e);
    }
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
  public void jsonProviderTest14() {
    String expString = "public JsonReader createReader(Reader)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonReader reader = Json.createReader(new StringReader("{}"));
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest14 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest14 Failed: ", e);
    }
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
  public void jsonProviderTest15() {
    String expString = "public JsonReader createReader(InputStream)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonReader reader = Json
          .createReader(JSONP_Util.getInputStreamFromString("{}"));
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest15 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest15 Failed: ", e);
    }
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
  public void jsonProviderTest16() {
    String expString = "public JsonWriter createWriter(Writer)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new StringWriter());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest16 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest16 Failed: ", e);
    }
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
  public void jsonProviderTest17() {
    String expString = "public JsonWriter createWriter(OutputStream)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonWriter writer = Json.createWriter(new ByteArrayOutputStream());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest17 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest17 Failed: ", e);
    }
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
  public void jsonProviderTest18() {
    String expString = "public JsonReaderFactory createReaderFactory(Map<String, ?>)";
    try {
      LOGGER.info("Calling SPI provider method: " + expString);
      JsonReaderFactory factory = Json
          .createReaderFactory(JSONP_Util.getEmptyConfig());
      String actString = MyJsonProvider.getCalls();
      LOGGER.info("Verify SPI provider method was called: " + expString);
      assertTrue(JSONP_Util.assertEquals(expString, actString), "jsonProviderTest18 Failed");
    } catch (Exception e) {
      fail("jsonProviderTest18 Failed: ", e);
    }
  }
}

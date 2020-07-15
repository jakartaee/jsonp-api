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
package jakarta.jsonp.tck.api.jsonwriterfactorytests;

import jakarta.json.*;
import jakarta.json.stream.*;

import java.io.*;
import java.nio.charset.Charset;

import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
  /* Tests */

  /*
   * @testName: jsonWriterFactoryTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:110; JSONP:JAVADOC:414; JSONP:JAVADOC:422;
   * JSONP:JAVADOC:452; JSONP:JAVADOC:463;
   * 
   * @test_Strategy: Tests the JsonWriterFactory API.
   *
   * JsonWriterFactory writerFactory = Json.createWriterFactory(Map<String, ?>);
   * JsonWriter writer1 = writerFactory.createWriter(Writer) JsonWriter writer2
   * = writerFactory.createWriter(Writer)
   */
  @Test
  public void jsonWriterFactoryTest1() throws Fault {
    boolean pass = true;
    JsonWriter writer1 = null;
    JsonWriter writer2 = null;
    String expString = "{}";
    String actString;
    JsonObject jsonObject = Json.createReader(new StringReader(expString))
        .readObject();
    try {
      System.out.println("Create JsonWriterFactory with Map<String, ?> with EMPTY config");
      JsonWriterFactory writerFactory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = writerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;
      System.out.println("--------------------------------------------------");
      System.out.println("TEST CASE [JsonWriterFactory.createWriter(Writer)]");
      System.out.println("--------------------------------------------------");
      System.out.println("Create 1st JsonWriter using JsonWriterFactory");
      Writer sWriter1 = new StringWriter();
      writer1 = writerFactory.createWriter(sWriter1);
      if (writer1 == null) {
        System.err.println("WriterFactory failed to create writer1");
        pass = false;
      } else {
        writer1.writeObject(jsonObject);
        writer1.close();
      }
      System.out.println("sWriter1=" + sWriter1.toString());
      actString = JSONP_Util.removeWhitespace(sWriter1.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      System.out.println("Create 2nd JsonWriter using JsonWriterFactory");
      Writer sWriter2 = new StringWriter();
      writer2 = writerFactory.createWriter(sWriter2);
      if (writer2 == null) {
        System.err.println("WriterFactory failed to create writer2");
        pass = false;
      } else {
        writer2.writeObject(jsonObject);
        writer2.close();
      }
      System.out.println("sWriter2=" + sWriter2.toString());
      actString = JSONP_Util.removeWhitespace(sWriter2.toString());
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonWriterFactoryTest1 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterFactoryTest1 Failed");
  }

  /*
   * @testName: jsonWriterFactoryTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:110; JSONP:JAVADOC:414; JSONP:JAVADOC:424;
   * JSONP:JAVADOC:452; JSONP:JAVADOC:463;
   * 
   * @test_Strategy: Tests the JsonWriterFactory API.
   *
   * JsonWriterFactory writerFactory = Json.createWriterFactory(Map<String,?>);
   * JsonWriter writer1 = writerFactory.createWriter(OutputStream, Charset)
   * JsonWriter writer2 = writerFactory.createWriter(OutputStream, Charset)
   *
   * Create writer with both UTF-8 and UTF-16BE.
   */
  @Test
  public void jsonWriterFactoryTest2() throws Fault {
    boolean pass = true;
    JsonWriter writer1 = null;
    JsonWriter writer2 = null;
    String expString = "{}";
    String actString;
    JsonObject jsonObject = Json.createReader(new StringReader(expString))
        .readObject();
    try {
      System.out.println("Create JsonWriterFactory with Map<String, ?> with EMPTY config");
      JsonWriterFactory writerFactory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = writerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println(
          "-----------------------------------------------------------------");
      System.out.println(
          "TEST CASE [JsonWriterFactory.createWriter(OutputStream, Charset)]");
      System.out.println(
          "-----------------------------------------------------------------");
      System.out.println(
          "Create 1st JsonWriter using JsonWriterFactory with UTF-8 encoding");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      writer1 = writerFactory.createWriter(baos1, JSONP_Util.UTF_8);
      if (writer1 == null) {
        System.err.println("WriterFactory failed to create writer1");
        pass = false;
      } else {
        writer1.writeObject(jsonObject);
        writer1.close();
      }
      System.out.println("baos1=" + baos1.toString("UTF-8"));
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      System.out.println(
          "Create 2nd JsonWriter using JsonWriterFactory with UTF-8 encoding");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      writer2 = writerFactory.createWriter(baos2, JSONP_Util.UTF_8);
      if (writer2 == null) {
        System.err.println("WriterFactory failed to create writer2");
        pass = false;
      } else {
        writer2.writeObject(jsonObject);
        writer2.close();
      }
      System.out.println("baos2=" + baos2.toString("UTF-8"));
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonWriterFactoryTest2 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterFactoryTest2 Failed");
  }

  /*
   * @testName: jsonWriterFactoryTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:110; JSONP:JAVADOC:414; JSONP:JAVADOC:423;
   * JSONP:JAVADOC:452; JSONP:JAVADOC:463;
   * 
   * @test_Strategy: Tests the JsonWriterFactory API.
   *
   * JsonWriterFactory writerFactory = Json.createWriterFactory(Map<String,?>);
   * JsonWriter writer1 = writerFactory.createWriter(OutputStream) JsonWriter
   * writer2 = writerFactory.createWriter(OutputStream)
   */
  @Test
  public void jsonWriterFactoryTest3() throws Fault {
    boolean pass = true;
    JsonWriter writer1 = null;
    JsonWriter writer2 = null;
    String expString = "{}";
    String actString;
    JsonObject jsonObject = Json.createReader(new StringReader(expString))
        .readObject();
    try {
      System.out.println("Create JsonWriterFactory with Map<String, ?> with EMPTY config");
      JsonWriterFactory writerFactory = Json
          .createWriterFactory(JSONP_Util.getEmptyConfig());
      System.out.println("Checking factory configuration properties");
      Map<String, ?> config = writerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("--------------------------------------------------------");
      System.out.println("TEST CASE [JsonWriterFactory.createWriter(OutputStream)]");
      System.out.println("--------------------------------------------------------");
      System.out.println("Create 1st JsonWriter using JsonWriterFactory");
      ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
      writer1 = writerFactory.createWriter(baos1);
      if (writer1 == null) {
        System.err.println("WriterFactory failed to create writer1");
        pass = false;
      } else {
        writer1.writeObject(jsonObject);
        writer1.close();
      }
      System.out.println("baos1=" + baos1.toString("UTF-8"));
      actString = JSONP_Util.removeWhitespace(baos1.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

      System.out.println("Create 2nd JsonWriter using JsonWriterFactory");
      ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
      writer2 = writerFactory.createWriter(baos2);
      if (writer2 == null) {
        System.err.println("WriterFactory failed to create writer2");
        pass = false;
      } else {
        writer2.writeObject(jsonObject);
        writer2.close();
      }
      System.out.println("baos2=" + baos2.toString("UTF-8"));
      actString = JSONP_Util.removeWhitespace(baos2.toString("UTF-8"));
      if (!JSONP_Util.assertEqualsJsonText(expString, actString))
        pass = false;

    } catch (Exception e) {
      throw new Fault("jsonWriterFactoryTest3 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterFactoryTest3 Failed");
  }

  /*
   * @testName: jsonWriterFactoryTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:452; JSONP:JAVADOC:463;
   * 
   * @test_Strategy: Tests the JsonWriterFactory API.
   *
   * JsonWriterFactory writerFactory = Json.createWriterFactory(Map<String, ?>);
   * Map<String, ?> config = JsonWriterFactory.getConfigInUse();
   *
   * Test for the following 3 scenarios: 1) no supported provider property
   * (empty config) 2) supported provider property 3) supported and non
   * supported provider property
   */
  @Test
  public void jsonWriterFactoryTest4() throws Fault {
    boolean pass = true;
    JsonWriterFactory writerFactory;
    Map<String, ?> config;
    try {
      System.out.println("----------------------------------------------");
      System.out.println("Test scenario1: no supported provider property");
      System.out.println("----------------------------------------------");
      System.out.println("Create JsonWriterFactory with Map<String, ?> with EMPTY config");
      writerFactory = Json.createWriterFactory(JSONP_Util.getEmptyConfig());
      config = writerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-------------------------------------------");
      System.out.println("Test scenario2: supported provider property");
      System.out.println("-------------------------------------------");
      System.out.println("Create JsonWriterFactory with Map<String, ?> with FOO config");
      writerFactory = Json.createWriterFactory(JSONP_Util.getFooConfig());
      config = writerFactory.getConfigInUse();
      String[] props = { JsonGenerator.PRETTY_PRINTING, };
      if (!JSONP_Util.doConfigCheck(config, 0))
        pass = false;

      System.out.println("-------------------------------------------------------------");
      System.out.println("Test scenario3: supported and non supported provider property");
      System.out.println("-------------------------------------------------------------");
      System.out.println("Create JsonGeneratorFactory with Map<String, ?> with all config");
      writerFactory = Json.createWriterFactory(JSONP_Util.getAllConfig());
      config = writerFactory.getConfigInUse();
      if (!JSONP_Util.doConfigCheck(config, 1, props))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonWriterFactoryTest4 Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonWriterFactoryTest4 Failed");
  }
}

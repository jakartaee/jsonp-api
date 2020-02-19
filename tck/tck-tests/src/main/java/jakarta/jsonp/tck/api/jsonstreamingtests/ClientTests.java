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
package jakarta.jsonp.tck.api.jsonstreamingtests;

import jakarta.json.*;
import jakarta.json.stream.*;

import java.io.*;
import java.util.*;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.jsonp.tck.common.*;
import jakarta.jsonp.tck.lib.harness.Fault;

@RunWith(Arquillian.class)
public class ClientTests {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ClientTests.class.getPackage().getName());
    }
  /*
   * @testName: streamingTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:97; JSONP:JAVADOC:106; JSONP:JAVADOC:107;
   * JSONP:JAVADOC:184;
   * 
   * @test_Strategy: Test Scenario: Generate stream of Json Text containing a
   * JsonArray Compare actual Json Text generated with expected Json Text for
   * equality Test passes if both JsonArray comparisons of Json Text are equal.
   *
   */
  @Test
  public void streamingTest1() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      // Set expected result
      String expJsonText = "[1,2,3,4,5,6,7,8,9,10]";
      System.out.println("expJsonText=" + expJsonText);

      System.out.println("Generate stream of Json Text containing a JsonArray");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(1).write(2).write(3).write(4).write(5)
          .write(6).write(7).write(8).write(9).write(10).writeEnd();
      generator.close();

      // Get actual result
      String actJsonText = JSONP_Util.removeWhitespace(sWriter.toString());
      System.out.println("actJsonText=" + actJsonText);

      System.out.println("Compare expJsonText and actJsonText for equality");
      pass = JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText);
    } catch (Exception e) {
      throw new Fault("streamingTest1 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("streamingTest1 Failed");
  }

  /*
   * @testName: streamingTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:97; JSONP:JAVADOC:106; JSONP:JAVADOC:107;
   * JSONP:JAVADOC:131;
   * 
   * @test_Strategy: Test Scenario: Generate data containing a JsonArray to a
   * Writer stream. Read data from Writer stream containing a JsonArray. Write
   * JsonArray out to a Writer stream. Re-read data from Writer stream
   * containing a JsonArray. Compare initial JsonArray with subsequent JsonArray
   * for equality. Test passes if both JsonArrays are equal.
   *
   */
  @Test
  public void streamingTest2() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    try {
      System.out.println("Generate data containing a JsonArray");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(2).write(4).write(6).write(8).write(10)
          .writeEnd();
      generator.close();

      System.out.println("Read data from Writer stream containing a JsonArray");
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonArray expJsonArray = reader.readArray();

      System.out.println("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      System.out.println("Write JsonArray out to a Writer stream");
      sWriter = new StringWriter();
      JsonWriter writer = Json.createWriter(sWriter);
      writer.writeArray(expJsonArray);
      System.out.println("Close JsonWriter");
      writer.close();

      System.out.println("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();
      System.out.println("writerContents=" + writerContents);

      System.out.println("Re-read data from Writer stream containing a JsonArray");
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      System.out.println("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      System.out.println("Compare expJsonArray and actJsonArray for equality");
      pass = JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray);
    } catch (Exception e) {
      throw new Fault("streamingTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("streamingTest2 Failed");
  }

  /*
   * @testName: streamingTest3
   * 
   * @assertion_ids: JSONP:JAVADOC:97; JSONP:JAVADOC:106; JSONP:JAVADOC:110;
   * JSONP:JAVADOC:131; JSONP:JAVADOC:172;
   * 
   * @test_Strategy: Test Scenario: Generate data containing a JsonObject to a
   * Write stream. Read data from Writer stream containing a JsonObject. Write
   * JsonObject out to a Writer stream. Parse data from Writer stream containing
   * a JsonObject stream. Test passes if parsing JsonObject events are correct.
   *
   */
  @Test
  public void streamingTest3() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    JsonParser parser = null;
    try {
      System.out.println("Generate data containing a JsonObject");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("two", 2).write("false", false)
          .writeEnd();
      generator.close();

      System.out.println("Read data from Writer stream containing a JsonObject");
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonObject expJsonObject = reader.readObject();

      System.out.println("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      System.out.println("Write JsonObject out to a Writer stream");
      sWriter = new StringWriter();
      JsonWriter writer = Json.createWriter(sWriter);
      writer.writeObject(expJsonObject);
      System.out.println("Close JsonWriter");
      writer.close();

      System.out.println("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();
      System.out.println("writerContents=" + writerContents);

      System.out.println("Parse data from Writer stream containing a JsonObject");
      parser = Json
          .createParser(JSONP_Util.getInputStreamFromString((writerContents)));

      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyIntegerValue(parser, "two", 2);
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      if (parseErrs != 0) {
        System.err.println("There were " + parseErrs + " parser errors that occurred.");
        pass = false;
      }

    } catch (Exception e) {
      throw new Fault("streamingTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
    if (!pass)
      throw new Fault("streamingTest3 Failed");
  }

  /*
   * @testName: streamingTest4
   * 
   * @assertion_ids: JSONP:JAVADOC:178; JSONP:JAVADOC:187; JSONP:JAVADOC:110;
   * JSONP:JAVADOC:168;
   * 
   * @test_Strategy: Test Scenario: Generate data containing a JsonObject to an
   * OutputStream. Compare expected JSON text from what was generated. Read data
   * from InputStream containing a JsonObject. Write JsonObject again out to an
   * OutputStream. Compare again expected JSON text from what was generated.
   * Test passes if JSON text comparisons are correct.
   *
   */
  @Test
  public void streamingTest4() throws Fault {
    boolean pass = true;
    JsonReader reader = null;
    JsonParser parser = null;
    String expJsonText = "{\"two\":2,\"false\":false}";
    try {
      System.out.println("Generate data containing a JsonObject to an OutputStream");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json.createGenerator(baos);
      generator.writeStartObject().write("two", 2).write("false", false)
          .writeEnd();
      baos.close();
      generator.close();

      System.out.println("Compare JSON text generated to what is expected.");
      if (!JSONP_Util.assertEqualsJsonText(expJsonText,
          JSONP_Util.removeWhitespace(baos.toString("UTF-8"))))
        pass = false;

      System.out.println("Read data from InputStream containing a JsonObject");
      reader = Json.createReader(
          JSONP_Util.getInputStreamFromString(baos.toString("UTF-8")));
      JsonObject expJsonObject = reader.readObject();
      System.out.println("Close JsonReader");
      reader.close();

      System.out.println("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      System.out.println("Write JsonObject back out to an OutputStream");
      baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriter(baos);
      writer.writeObject(expJsonObject);
      System.out.println("Close JsonWriter");
      baos.close();
      writer.close();

      System.out.println("Save contents of the JsonWriter as a String");
      String writerContents = baos.toString("UTF-8");
      System.out.println("writerContents=" + writerContents);

      System.out.println("Compare again JSON text generated to what is expected.");
      if (!JSONP_Util.assertEqualsJsonText(expJsonText,
          JSONP_Util.removeWhitespace(writerContents)))
        pass = false;

    } catch (Exception e) {
      throw new Fault("streamingTest4 Failed: ", e);
    }

    if (!pass)
      throw new Fault("streamingTest4 Failed");
  }
}

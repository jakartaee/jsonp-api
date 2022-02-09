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

/*
 * $Id$
 */
package ee.jakarta.tck.jsonp.api.jsonstreamingtests;

import ee.jakarta.tck.jsonp.common.JSONP_Util;
import jakarta.json.*;
import jakarta.json.stream.*;

import java.io.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
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
  public void streamingTest1() {
    JsonReader reader = null;
    try {
      // Set expected result
      String expJsonText = "[1,2,3,4,5,6,7,8,9,10]";
      LOGGER.info("expJsonText=" + expJsonText);

      LOGGER.info("Generate stream of Json Text containing a JsonArray");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(1).write(2).write(3).write(4).write(5)
          .write(6).write(7).write(8).write(9).write(10).writeEnd();
      generator.close();

      // Get actual result
      String actJsonText = JSONP_Util.removeWhitespace(sWriter.toString());
      LOGGER.info("actJsonText=" + actJsonText);

      LOGGER.info("Compare expJsonText and actJsonText for equality");
      assertTrue(JSONP_Util.assertEqualsJsonText(expJsonText, actJsonText), "streamingTest1 Failed");
    } catch (Exception e) {
      fail("streamingTest1 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
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
  public void streamingTest2() {
    JsonReader reader = null;
    try {
      LOGGER.info("Generate data containing a JsonArray");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartArray().write(2).write(4).write(6).write(8).write(10)
          .writeEnd();
      generator.close();

      LOGGER.info("Read data from Writer stream containing a JsonArray");
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonArray expJsonArray = reader.readArray();

      LOGGER.info("Dump of expJsonArray");
      JSONP_Util.dumpJsonArray(expJsonArray);

      LOGGER.info("Write JsonArray out to a Writer stream");
      sWriter = new StringWriter();
      JsonWriter writer = Json.createWriter(sWriter);
      writer.writeArray(expJsonArray);
      LOGGER.info("Close JsonWriter");
      writer.close();

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();
      LOGGER.info("writerContents=" + writerContents);

      LOGGER.info("Re-read data from Writer stream containing a JsonArray");
      reader = Json.createReader(new StringReader(writerContents));
      JsonArray actJsonArray = reader.readArray();

      LOGGER.info("Dump of actJsonArray");
      JSONP_Util.dumpJsonArray(actJsonArray);

      LOGGER.info("Compare expJsonArray and actJsonArray for equality");
      assertTrue(JSONP_Util.assertEqualsJsonArrays(expJsonArray, actJsonArray), "streamingTest2 Failed");
    } catch (Exception e) {
      fail("streamingTest2 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
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
  public void streamingTest3() {
    JsonReader reader = null;
    JsonParser parser = null;
    try {
      LOGGER.info("Generate data containing a JsonObject");
      StringWriter sWriter = new StringWriter();
      JsonGenerator generator = Json.createGenerator(sWriter);
      generator.writeStartObject().write("two", 2).write("false", false)
          .writeEnd();
      generator.close();

      LOGGER.info("Read data from Writer stream containing a JsonObject");
      reader = Json.createReader(new StringReader(sWriter.toString()));
      JsonObject expJsonObject = reader.readObject();

      LOGGER.info("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      LOGGER.info("Write JsonObject out to a Writer stream");
      sWriter = new StringWriter();
      JsonWriter writer = Json.createWriter(sWriter);
      writer.writeObject(expJsonObject);
      LOGGER.info("Close JsonWriter");
      writer.close();

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = sWriter.toString();
      LOGGER.info("writerContents=" + writerContents);

      LOGGER.info("Parse data from Writer stream containing a JsonObject");
      parser = Json
          .createParser(JSONP_Util.getInputStreamFromString((writerContents)));

      JSONP_Util.resetParseErrs();
      JSONP_Util.testEventType(parser, JsonParser.Event.START_OBJECT);
      JSONP_Util.testKeyIntegerValue(parser, "two", 2);
      JSONP_Util.testKeyFalseValue(parser, "false");
      JSONP_Util.testEventType(parser, JsonParser.Event.END_OBJECT);
      int parseErrs = JSONP_Util.getParseErrs();
      assertTrue(
          parseErrs == 0,
          "StreamingTest3 Failed. There were " + parseErrs + " parser errors that occurred."
      );
    } catch (Exception e) {
      fail("streamingTest3 Failed: ", e);
    } finally {
      if (reader != null)
        reader.close();
    }
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
  public void streamingTest4() {
    boolean pass = true;
    JsonReader reader = null;
    JsonParser parser = null;
    String expJsonText = "{\"two\":2,\"false\":false}";
    try {
      LOGGER.info("Generate data containing a JsonObject to an OutputStream");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JsonGenerator generator = Json.createGenerator(baos);
      generator.writeStartObject().write("two", 2).write("false", false)
          .writeEnd();
      baos.close();
      generator.close();

      LOGGER.info("Compare JSON text generated to what is expected.");
      if (!JSONP_Util.assertEqualsJsonText(expJsonText,
          JSONP_Util.removeWhitespace(baos.toString("UTF-8"))))
        pass = false;

      LOGGER.info("Read data from InputStream containing a JsonObject");
      reader = Json.createReader(
          JSONP_Util.getInputStreamFromString(baos.toString("UTF-8")));
      JsonObject expJsonObject = reader.readObject();
      LOGGER.info("Close JsonReader");
      reader.close();

      LOGGER.info("Dump of expJsonObject");
      JSONP_Util.dumpJsonObject(expJsonObject);

      LOGGER.info("Write JsonObject back out to an OutputStream");
      baos = new ByteArrayOutputStream();
      JsonWriter writer = Json.createWriter(baos);
      writer.writeObject(expJsonObject);
      LOGGER.info("Close JsonWriter");
      baos.close();
      writer.close();

      LOGGER.info("Save contents of the JsonWriter as a String");
      String writerContents = baos.toString("UTF-8");
      LOGGER.info("writerContents=" + writerContents);

      LOGGER.info("Compare again JSON text generated to what is expected.");
      if (!JSONP_Util.assertEqualsJsonText(expJsonText,
          JSONP_Util.removeWhitespace(writerContents)))
        pass = false;

    } catch (Exception e) {
      fail("streamingTest4 Failed: ", e);
    }

    assertTrue(pass, "streamingTest4 Failed");
  }
}

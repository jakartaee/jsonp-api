/*
 * Copyright (c) 2020, 2022 Oracle and/or its affiliates. All rights reserved.
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

package ee.jakarta.tck.jsonp.common;


import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import java.nio.charset.Charset;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.logging.Logger;

import jakarta.json.*;
import jakarta.json.stream.*;

public final class JSONP_Util {

  private static final Logger LOGGER = Logger.getLogger(JSONP_Util.class.getName());

  // Charset CONSTANTS for all the supported UTF encodings
  public static final Charset UTF_8 = Charset.forName("UTF-8");

  public static final Charset UTF_16 = Charset.forName("UTF-16");

  public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

  public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

  public static final Charset UTF_32BE = Charset.forName("UTF-32BE");

  public static final Charset UTF_32LE = Charset.forName("UTF-32LE");

  // Test Config properties
  public static final String FOO_CONFIG = "ee.jakarta.tck.jsonp.common.FOO_CONFIG";

  // Number of parser errors encountered
  private static int parseErrs = 0;

  // A JsonNumber NumberType is now INTEGRAL or NON_INTEGRAL based on
  // JsonNumber.isIntegral().
  // The following 2 constant definitions represent these NumberType boolean
  // values.
  public static final boolean INTEGRAL = true;

  public static final boolean NON_INTEGRAL = false;

  /*********************************************************************************
   * {@code void dumpContentsOfResource(String resource)}
   *********************************************************************************/
  public static void dumpContentsOfResource(String resource) {
    LOGGER.info("Dumping contents of Resource file: " + resource);
    BufferedReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null) {
        LOGGER.warning(
            "dumpContentsOfResource: no resource found in classpath or archive named "
                + resource);
        return;
      }
      reader = new BufferedReader(new InputStreamReader(iStream));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        LOGGER.info(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          LOGGER.warning("exception closing stream: " + e);
        }
    }
  }

  /*********************************************************************************
   * {@code void dumpFile(String file)}
   *********************************************************************************/
  public static void dumpFile(String file) {
    LOGGER.info("Dumping contents of file: " + file);
    BufferedReader reader = null;
    try {
      FileInputStream fis = new FileInputStream(file);
      if (fis == null) {
        LOGGER.warning("dumpFile: no file found named " + file);
        return;
      }
      reader = new BufferedReader(new InputStreamReader(fis));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        LOGGER.info(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          LOGGER.warning("exception closing stream: " + e);
        }
    }
  }

  /*********************************************************************************
   * {@code String getContentsOfResourceAsString(String resource)}
   *********************************************************************************/
  public static String getContentsOfResourceAsString(String resource) {
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null) {
        LOGGER.warning(
            "dumpContentsOfResource: no resource found in classpath or archive named "
                + resource);
        return null;
      }
      reader = new BufferedReader(new InputStreamReader(iStream));
      String thisLine;
      while ((thisLine = reader.readLine()) != null) {
        sb.append(thisLine);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        try {
          reader.close();
        } catch (Exception e) {
          LOGGER.warning("exception closing stream: " + e);
        }
    }
    return sb.toString();
  }

  /*********************************************************************************
   * {@code Reader getReaderFromResource(String resource)}
   *********************************************************************************/
  public static Reader getReaderFromResource(String resource) {
    InputStreamReader reader = null;
    try {
      InputStream iStream = JSONP_Util.class
          .getResourceAsStream("/" + resource);
      if (iStream == null)
        LOGGER.warning(
            "getReaderFromResource: no resource found in classpath or archive named "
                + resource);
      else
        reader = new InputStreamReader(iStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  /*********************************************************************************
   * {@code Reader getReaderFromString(String contents)}
   *********************************************************************************/
  public static Reader getReaderFromString(String contents) {
    InputStreamReader reader = null;
    try {
      InputStream iStream = new ByteArrayInputStream(contents.getBytes(UTF_8));
      if (iStream == null)
        LOGGER.warning("getReaderFromString: no input stream");
      else
        reader = new InputStreamReader(iStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return reader;
  }

  /*********************************************************************************
   * {@code InputStream getInputStreamFromResource(String resource)}
   *********************************************************************************/
  public static InputStream getInputStreamFromResource(String resource) {
    InputStream iStream = null;
    try {
      iStream = JSONP_Util.class.getResourceAsStream("/" + resource);
      if (iStream == null)
        LOGGER.warning(
            "getInputStreamFromResource: no resource found in classpath or archive named "
                + resource);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * {@code InputStream getInputStreamFromString(String contents)}
   *********************************************************************************/
  public static InputStream getInputStreamFromString(String contents) {
    InputStream iStream = null;
    try {
      iStream = new ByteArrayInputStream(contents.getBytes(UTF_8));
      if (iStream == null)
        LOGGER.warning("getInputStreamFromString: no input stream");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * {@code InputStream getInputStreamFromOutputStream(ByteArrayOutputStream baos)}
   *********************************************************************************/
  public static InputStream getInputStreamFromOutputStream(
      ByteArrayOutputStream baos) {
    InputStream iStream = null;
    try {
      iStream = new ByteArrayInputStream(baos.toByteArray());
      if (iStream == null)
        LOGGER.warning("getInputStreamFromOutputStream: no input stream");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return iStream;
  }

  /*********************************************************************************
   * {@code String removeWhitespace(String text)}
   *
   * NOTE: This does not remove whitespace of Json text if a quoted string which
   * can include whitespace.
   *********************************************************************************/
  public static String removeWhitespace(String text) {
    StringReader reader = new StringReader(text);
    StringWriter writer = new StringWriter();
    try {
      boolean quotedString = false;
      boolean backslash = false;
      int c;
      while ((c = reader.read()) != -1) {
        // Skip white space if not quoted string
        if (!quotedString) {
          if (Character.isWhitespace(c))
            continue;
        }
        writer.write(c);
        if (c == '"') {
          if (!backslash)
            quotedString = !quotedString;
          backslash = false;
        } else if (c == '\\')
          backslash = true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return writer.toString();
  }

  /*********************************************************************************
   * {@code JsonNumber createJsonNumber}
   *
   * The following method signatures are available:
   *
   * o JsonNumber createJsonNumber(double) o JsonNumber createJsonNumber(long) o
   * JsonNumber createJsonNumber(int) o JsonNumber createJsonNumber(BigDecimal)
   * o JsonNumber createJsonNumber(BigInteger)
   *********************************************************************************/
  public static JsonNumber createJsonNumber(double val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(long val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(int val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(BigDecimal val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  public static JsonNumber createJsonNumber(BigInteger val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonNumber(0);
  }

  /*********************************************************************************
   * {@code JsonString createJsonString(String val)}
   *********************************************************************************/
  public static JsonString createJsonString(String val) {
    JsonArray array = Json.createArrayBuilder().add(val).build();
    return array.getJsonString(0);
  }

  /*********************************************************************************
   * {@code void dumpJsonString(JsonString val)}
   *********************************************************************************/
  public static void dumpJsonString(JsonString value) {
    LOGGER.info("dumpJsonString->" + toStringJsonString(value));
  }

  /*********************************************************************************
   * {@code void dumpJsonArray(JsonArray value)}
   *********************************************************************************/
  public static void dumpJsonArray(JsonArray value) {
    LOGGER.info("dumpJsonArray->" + toStringJsonArray(value));
  }

  /*********************************************************************************
   * {@code void dumpJsonObject(JsonObject value)}
   *********************************************************************************/
  public static void dumpJsonObject(JsonObject value) {
    LOGGER.info("dumpJsonObject->" + toStringJsonObject(value));
  }

  /*********************************************************************************
   * {@code void dumpJsonConstant(JsonValue value)}
   *********************************************************************************/
  public static void dumpJsonConstant(JsonValue value) {
    LOGGER.info("dumpJsonConstant->" + toStringJsonConstant(value));
  }

  /*********************************************************************************
   * {@code void dumpJsonNumber(JsonNumber value)}
   *********************************************************************************/
  public static void dumpJsonNumber(JsonNumber value) {
    LOGGER.info("dumpJsonNumber->" + toStringJsonNumber(value));
  }

  /*********************************************************************************
   * {@code void dumpJsonValue(JsonValue value)}
   *********************************************************************************/
  public static void dumpJsonValue(JsonValue value) {
    if (value instanceof JsonNumber) {
      dumpJsonNumber((JsonNumber) value);
    } else if (value instanceof JsonString) {
      dumpJsonString((JsonString) value);
    } else if (value instanceof JsonArray) {
      dumpJsonArray((JsonArray) value);
    } else if (value instanceof JsonObject) {
      dumpJsonObject((JsonObject) value);
    } else
      dumpJsonConstant(value);
  }

  /*********************************************************************************
   * {@code String toStringJsonString(JsonString value)}
   *********************************************************************************/
  public static String toStringJsonString(JsonString value) {
    if (value == null)
      return ("JsonString is null");
    return ("\"" + value.getString() + "\"");
  }

  /*********************************************************************************
   * {@code String toStringJsonArray(JsonArray value)}
   *********************************************************************************/
  public static String toStringJsonArray(JsonArray value) {
    if (value == null)
      return ("JsonArray is null");
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    Iterator<JsonValue> iter = value.iterator();
    String comma = "";
    while (iter.hasNext()) {
      sb.append(comma + toStringJsonValue(iter.next()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("]");
    return (sb.toString());
  }

  /*********************************************************************************
   * {@code String toStringJsonObject(JsonObject value)}
   *********************************************************************************/
  public static String toStringJsonObject(JsonObject value) {
    if (value == null)
      return ("JsonObject is null");
    StringBuilder sb = new StringBuilder();
    sb.append("{");
    String comma = "";
    for (Map.Entry<String, JsonValue> entry : value.entrySet()) {
      sb.append(comma + "\"" + entry.getKey() + "\":"
          + toStringJsonValue(entry.getValue()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("}");
    return (sb.toString());
  }

  /*********************************************************************************
   * {@code String toStringJsonConstant(JsonValue value)}
   *********************************************************************************/
  public static String toStringJsonConstant(JsonValue value) {
    if (value == null)
      return ("JsonValue is null");
    if (value == JsonValue.FALSE)
      return "false";
    else if (value == JsonValue.TRUE)
      return "true";
    else if (value == JsonValue.NULL)
      return "null";
    else
      return "UNKNOWN";
  }

  /*********************************************************************************
   * {@code String toStringJsonNumber(JsonNumber value)}
   *********************************************************************************/
  public static String toStringJsonNumber(JsonNumber value) {
    if (value == null)
      return ("JsonNumber is null");
    if (value.isIntegral() == INTEGRAL)
      return ("" + value.longValue());
    else
      return ("" + value.bigDecimalValue());
  }

  /*********************************************************************************
   * {@code String toStringJsonValue(JsonValue value)}
   *********************************************************************************/
  public static String toStringJsonValue(JsonValue value) {
    if (value instanceof JsonNumber) {
      return toStringJsonNumber((JsonNumber) value);
    } else if (value instanceof JsonString) {
      return toStringJsonString((JsonString) value);
    } else if (value instanceof JsonArray) {
      return toStringJsonArray((JsonArray) value);
    } else if (value instanceof JsonObject) {
      return toStringJsonObject((JsonObject) value);
    } else
      return toStringJsonConstant(value);
  }

  /*********************************************************************************
   * {@code void dumpSet(Set<String> set, String msg)}
   *********************************************************************************/
  public static void dumpSet(Set<String> set, String msg) {
    LOGGER.info("*** Beg: Dumping List contents ***");
    if (msg != null)
      LOGGER.info("*** Message: " + msg);
    Iterator iterator = set.iterator();
    LOGGER.info("Set: (");
    while (iterator.hasNext()) {
      LOGGER.info((String) iterator.next());
    }
    LOGGER.info(")");
    LOGGER.info("*** End: Dumping Set contents ***");
  }

  /*********************************************************************************
   * {@code void dumpSet(Set<String> set)}
   *********************************************************************************/
  public static void dumpSet(Set<String> set) {
    dumpSet(set, null);
  }

  /*********************************************************************************
   * {@code String toStringSet(Set<String> set)}
   *********************************************************************************/
  public static String toStringSet(Set<String> set) {
    Iterator<String> iter = set.iterator();
    StringBuilder sb = new StringBuilder();
    sb.append("Set: (");
    while (iter.hasNext()) {
      sb.append(iter.next());
      if (iter.hasNext())
        sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }

  /*********************************************************************************
   * {@code boolean assertEqualsSet(Set<String>expSet, Set<String>actSet)}
   *********************************************************************************/
  public static boolean assertEqualsSet(Set<String> expSet,
      Set<String> actSet) {
    if (actSet.equals(expSet)) {
      LOGGER.info("Sets are equal - match (Success)");
      LOGGER.info("Expected: " + toStringSet(expSet));
      LOGGER.info("Actual:   " + toStringSet(actSet));
      return true;
    } else {
      LOGGER.info("Sets are not equal - mismatch (Failure)");
      LOGGER.warning("Expected: " + toStringSet(expSet));
      LOGGER.warning("Actual:   " + toStringSet(actSet));
      return false;
    }
  }

  /*********************************************************************************
   * {@code void dumpMap(Map<String,JsonValue> map, String msg)}
   *********************************************************************************/
  public static void dumpMap(Map<String, JsonValue> map, String msg) {
    LOGGER.info("*** Beg: Dumping Map contents ***");
    if (msg != null)
      LOGGER.info("*** Message: " + msg);
    LOGGER.info("Map: {");
    for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
      LOGGER.info(
          "\"" + entry.getKey() + "\":" + toStringJsonValue(entry.getValue()));
    }
    LOGGER.info("}");
    LOGGER.info("*** End: Dumping Map contents ***");
  }

  /*********************************************************************************
   * {@code void dumpMap(Map<String,JsonValue> map)}
   *********************************************************************************/
  public static void dumpMap(Map<String, JsonValue> map) {
    dumpMap(map, null);
  }

  /*********************************************************************************
   * {@code String toStringMap(Map<String,JsonValue> map)}
   *********************************************************************************/
  public static String toStringMap(Map<String, JsonValue> map) {
    StringBuilder sb = new StringBuilder();
    sb.append("Map: {");
    String comma = "";
    for (Map.Entry<String, JsonValue> entry : map.entrySet()) {
      sb.append(comma + "\"" + entry.getKey() + "\":"
          + toStringJsonValue(entry.getValue()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("}");
    return sb.toString();
  }

  /*********************************************************************************
   * {@code boolean assertEqualsMap(Map<String,JsonValue>expMap,
   * Map<String,JsonValue>actMap)}
   *********************************************************************************/
  public static boolean assertEqualsMap(Map<String, JsonValue> expMap,
      Map<String, JsonValue> actMap) {
    if (actMap.equals(expMap)) {
      LOGGER.info("Maps are equal - match (Success)");
      LOGGER.info("Expected: " + toStringMap(expMap));
      LOGGER.info("Actual:   " + toStringMap(actMap));
      return true;
    } else {
      LOGGER.info("Maps are not equal - mismatch (Failure)");
      LOGGER.warning("Expected: " + toStringMap(expMap));
      LOGGER.warning("Actual:   " + toStringMap(actMap));
      return false;
    }
  }

  /*********************************************************************************
   * {@code assertEqualsMap2}
   *********************************************************************************/
  public static boolean assertEqualsMap2(Map<String, JsonValue> expMap,
      Map<String, JsonValue> actMap) {
    LOGGER.info("*** Comparing Map expMap and Map actMap for equality ***");
    LOGGER.info("Expected: " + toStringMap(expMap));
    LOGGER.info("Actual:   " + toStringMap(actMap));
    LOGGER.info("Map expMap size should equal Map actMap size");
    if (expMap.size() != actMap.size()) {
      LOGGER.warning("Map sizes are not equal: expMap size " + expMap.size()
          + ", actMap size " + actMap.size());
      return false;
    } else {
      LOGGER.info("Map sizes are equal with size of " + expMap.size());
    }
    for (Map.Entry<String, ?> entry : expMap.entrySet()) {
      String key = entry.getKey();
      if (actMap.containsKey(key)) {
        if (expMap.get(key) != null && actMap.get(key) != null) {
          if (!expMap.get(key).equals(actMap.get(key))) {
            LOGGER.warning("key=" + key + ", expMap value " + expMap.get(key)
                + " does not equal actMap value " + actMap.get(key));
            return false;
          }
        }
      } else {
        LOGGER.warning("actMap does not contain key " + key);
        return false;
      }
    }
    LOGGER.info("Maps expMap and actMap are equal.");
    return true;
  }

  /*********************************************************************************
   * {@code void dumpList(List<JsonValue> list, String msg)}
   *********************************************************************************/
  public static void dumpList(List<JsonValue> list, String msg) {
    LOGGER.info("*** Beg: Dumping List contents ***");
    if (msg != null)
      LOGGER.info("*** Message: " + msg);
    Iterator<JsonValue> iter = list.iterator();
    LOGGER.info("List: [");
    while (iter.hasNext()) {
      LOGGER.info("" + toStringJsonValue(iter.next()));
    }
    LOGGER.info("]");
    LOGGER.info("*** End: Dumping List contents ***");
  }

  /*********************************************************************************
   * {@code void dumpList(List<JsonValue> list)}
   *********************************************************************************/
  public static void dumpList(List<JsonValue> list) {
    dumpList(list, null);
  }

  /*********************************************************************************
   * {@code String toStringList(List<JsonValue> list)}
   *********************************************************************************/
  public static String toStringList(List<JsonValue> list) {
    Iterator<JsonValue> iter = list.iterator();
    StringBuilder sb = new StringBuilder();
    sb.append("List: [");
    String comma = "";
    while (iter.hasNext()) {
      sb.append(comma + toStringJsonValue(iter.next()));
      if (comma.equals(""))
        comma = ",";
    }
    sb.append("]");
    return sb.toString();
  }

  /*********************************************************************************
   * {@code boolean assertEqualsList(List<JsonValue>expList, List<JsonValue>actList)}
   *********************************************************************************/
  public static boolean assertEqualsList(List<JsonValue> expList,
      List<JsonValue> actList) {
    if (actList.equals(expList)) {
      LOGGER.info("Lists are equal - match (Success)");
      LOGGER.info("Expected: " + toStringList(expList));
      LOGGER.info("Actual:   " + toStringList(actList));
      return true;
    } else {
      LOGGER.info("Lists are not equal - mismatch (Failure)");
      LOGGER.warning("Expected: " + toStringList(expList));
      LOGGER.warning("Actual:   " + toStringList(actList));
      return false;
    }
  }

  /*********************************************************************************
   * {@code assertEqualsList2}
   *********************************************************************************/
  public static boolean assertEqualsList2(List<JsonValue> expList,
      List<JsonValue> actList) {
    LOGGER.info(
        "*** Comparing contents of List expList and List actList for equality ***");
    LOGGER.info("Expected: " + toStringList(expList));
    LOGGER.info("Actual:   " + toStringList(actList));
    LOGGER.info("List expList size should equal List actList size");
    if (expList.size() != actList.size()) {
      LOGGER.warning("List sizes are not equal: expList size " + expList.size()
          + ", actList size " + actList.size());
      return false;
    }
    LOGGER.info("Compare Lists (all elements should MATCH)");
    for (int i = 0; i < expList.size(); i++) {
      if (expList.get(i).equals(actList.get(i))) {
        LOGGER.info("expList element " + i + " matches actList element " + i);
      } else {
        LOGGER.warning(
            "expList element " + i + " does not match actList element " + i);
        LOGGER.warning("expList[" + i + "]=" + expList.get(i));
        LOGGER.warning("actList[" + i + "]=" + actList.get(i));
        return false;
      }
    }
    LOGGER.info("Lists are equal (Success)");
    return true;
  }

  /*********************************************************************************
   * {@code void dumpIterator(Iterator<JsonValue> iterator, String msg)}
   *********************************************************************************/
  public static void dumpIterator(Iterator<JsonValue> iterator, String msg) {
    LOGGER.info("*** Beg: Dumping Iterator contents ***");
    if (msg != null)
      LOGGER.info("*** Message: " + msg);
    LOGGER.info("Iter: [");
    while (iterator.hasNext()) {
      LOGGER.info("" + toStringJsonValue(iterator.next()));
    }
    LOGGER.info("]");
    LOGGER.info("*** End: Dumping Iterator contents ***");
  }

  /*********************************************************************************
   * {@code void dumpIterator(Iterator<JsonValue> iterator)}
   *********************************************************************************/
  public static void dumpIterator(Iterator<JsonValue> iterator) {
    dumpIterator(iterator, null);
  }

  /*********************************************************************************
   * {@code String toStringIterator(Iterator<JsonValue> iterator)}
   *********************************************************************************/
  public static String toStringIterator(Iterator<JsonValue> iter) {
    StringBuilder sb = new StringBuilder();
    sb.append("Iterator: [");
    while (iter.hasNext()) {
      sb.append(toStringJsonValue(iter.next()));
      if (iter.hasNext())
        sb.append(",");
    }
    sb.append("]");
    return sb.toString();
  }

  /*********************************************************************************
   * {@code boolean assertEqualsIterator(Iterator<JsonValue>expIt,
   * Iterator<JsonValue>actIt)}
   *********************************************************************************/
  public static boolean assertEqualsIterator(Iterator<JsonValue> expIt,
      Iterator<JsonValue> actIt) {
    boolean pass = true;

    LOGGER.info(
        "*** Comparing contents of Iterator expIt and Iterator actIt for equality ***");
    int i = 0;
    while (expIt.hasNext()) {
      if (!actIt.hasNext()) {
        LOGGER.warning(
            "Iterator expIt contains more elements than Iterator actIt");
        return false;
      }
      ++i;
      JsonValue value1 = expIt.next();
      JsonValue value2 = actIt.next();
      if (assertEqualsJsonValues(value1, value2)) {
        LOGGER.info("Iterator expIt element  " + i
            + " matches Iterator actIt element " + i);
      } else {
        LOGGER.warning("Iterator expIt element " + i
            + " does not match Iterator actIt element " + i);
        pass = false;
      }
    }
    if (actIt.hasNext()) {
      LOGGER.warning("Iterator actIt contains more elements than Iterator expIt");
      return false;
    }
    if (pass)
      LOGGER.info("Iterators are equal (Success)");
    else
      LOGGER.info("Iterators are not equal (Failure)");
    return pass;
  }

  /*********************************************************************************
   * {@code boolean assertEqualsEmptyArrayList(List<JsonValue> actual)}
   *********************************************************************************/
  public static boolean assertEqualsEmptyArrayList(List<JsonValue> actual) {
    if (actual.isEmpty()) {
      LOGGER.info("Array List is empty - expected");
      return true;
    } else {
      LOGGER.warning("Array List is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsEmptyObjectMap(Map<String, JsonValue> actual)}
   *********************************************************************************/
  public static boolean assertEqualsEmptyObjectMap(
      Map<String, JsonValue> actual) {
    if (actual.isEmpty()) {
      LOGGER.info("Object Map is empty - expected");
      return true;
    } else {
      LOGGER.warning("Object Map is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsEmptyIterator(Map<String, JsonValue> actual)}
   *********************************************************************************/
  public static boolean assertEqualsEmptyIterator(Iterator<JsonValue> actual) {
    if (!actual.hasNext()) {
      LOGGER.info("Iterator is empty - expected");
      return true;
    } else {
      LOGGER.warning("Iterator is not empty - unexpected");
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonText(String expected, String actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonText(String expected, String actual) {
    if (actual.equals(expected)) {
      LOGGER.info("JSON text match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("JSON text mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonArrays(JsonArray expected, JsonArray actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonArrays(JsonArray expected,
      JsonArray actual) {
    if (actual.equals(expected)) {
      LOGGER.info("JsonArray match");
      LOGGER.info("Expected: " + toStringJsonArray(expected));
      LOGGER.info("Actual:   " + toStringJsonArray(actual));
      return true;
    } else {
      LOGGER.warning("JsonArray mismatch");
      LOGGER.warning("Expected: " + toStringJsonArray(expected));
      LOGGER.warning("Actual:   " + toStringJsonArray(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonObjects(JsonObject expected, JsonObject actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonObjects(JsonObject expected,
      JsonObject actual) {
    if (actual.equals(expected)) {
      LOGGER.info("JsonObject match");
      LOGGER.info("Expected: " + toStringJsonObject(expected));
      LOGGER.info("Actual:   " + toStringJsonObject(actual));
      return true;
    } else {
      LOGGER.warning("JsonObject mismatch");
      LOGGER.warning("Expected: " + toStringJsonObject(expected));
      LOGGER.warning("Actual:   " + toStringJsonObject(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonNumbers(JsonNumber expected, JsonNumber actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonNumbers(JsonNumber expected,
      JsonNumber actual) {
    boolean pass = true;

    if (actual.equals(expected)) {
      LOGGER.info("JsonNumber match");
      LOGGER.info("Expected: " + toStringJsonNumber(expected));
      LOGGER.info("Actual:   " + toStringJsonNumber(actual));
      return true;
    } else {
      LOGGER.warning("JsonNumber mismatch");
      LOGGER.warning("Expected: " + toStringJsonNumber(expected));
      LOGGER.warning("Actual:   " + toStringJsonNumber(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonStrings(JsonString expected, JsonString actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonStrings(JsonString expected,
      JsonString actual) {
    boolean pass = true;

    if (actual.equals(expected)) {
      LOGGER.info("JsonString match");
      LOGGER.info("Expected: " + toStringJsonString(expected));
      LOGGER.info("Actual:   " + toStringJsonString(actual));
      return true;
    } else {
      LOGGER.warning("JsonString mismatch");
      LOGGER.warning("Expected: " + toStringJsonString(expected));
      LOGGER.warning("Actual:   " + toStringJsonString(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonValues(JsonValue expected, JsonValue actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonValues(JsonValue expected,
      JsonValue actual) {
    boolean pass = true;

    // Comparing JsonNumbers
    if (expected instanceof JsonNumber) {
      if (!(actual instanceof JsonNumber)) {
        LOGGER.warning("expected type does not match actual type");
        LOGGER.warning("expected=" + toStringJsonValue(expected));
        LOGGER.warning("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonNumbers((JsonNumber) expected,
            (JsonNumber) actual);
      }
      // Comparing JsonStrings
    } else if (expected instanceof JsonString) {
      if (!(actual instanceof JsonString)) {
        LOGGER.warning("expected type does not match actual type");
        LOGGER.warning("expected=" + toStringJsonValue(expected));
        LOGGER.warning("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonStrings((JsonString) expected,
            (JsonString) actual);
      }
      // Comparing JsonArrays
    } else if (expected instanceof JsonArray) {
      if (!(actual instanceof JsonArray)) {
        LOGGER.warning("expected type does not match actual type");
        LOGGER.warning("expected=" + toStringJsonValue(expected));
        LOGGER.warning("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonArrays((JsonArray) expected, (JsonArray) actual);
      }
      // Comparing JsonObjects
    } else if (expected instanceof JsonObject) {
      if (!(actual instanceof JsonObject)) {
        LOGGER.warning("expected type does not match actual type");
        LOGGER.warning("expected=" + toStringJsonValue(expected));
        LOGGER.warning("actual=  " + toStringJsonValue(actual));
        pass = false;
      } else {
        pass = assertEqualsJsonObjects((JsonObject) expected,
            (JsonObject) actual);
      }
      // Comparing JsonValues
    } else if (expected.equals(actual)) {
      LOGGER.info("expected matches actual");
      LOGGER.info("expected=" + toStringJsonValue(expected));
      LOGGER.info("actual=  " + toStringJsonValue(actual));
    } else {
      LOGGER.warning("expected does not match actual");
      LOGGER.warning("expected=" + toStringJsonValue(expected));
      LOGGER.warning("actual=  " + toStringJsonValue(actual));
      pass = false;
    }
    return pass;
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonValueType(JsonValue.ValueType
   * expected,JsonValue.ValueType actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonValueType(JsonValue.ValueType expected,
      JsonValue.ValueType actual) {
    if (actual == expected) {
      LOGGER.info("JsonValue.ValueType match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("JsonValue.ValueType mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonNumberType(boolean expected,boolean actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonNumberType(boolean expected,
      boolean actual) {
    if (actual == expected) {
      LOGGER.info("Json NumberType match");
      LOGGER.info("Expected: " + toStringJsonNumberType(expected));
      LOGGER.info("Actual:   " + toStringJsonNumberType(actual));
      return true;
    } else {
      LOGGER.warning("Json NumberType mismatch");
      LOGGER.warning("Expected: " + toStringJsonNumberType(expected));
      LOGGER.warning("Actual:   " + toStringJsonNumberType(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEqualsJsonNumberTypes(boolean expected[],boolean actual)}
   *********************************************************************************/
  public static boolean assertEqualsJsonNumberTypes(boolean expected[],
      boolean actual) {
    for (int i = 0; i < expected.length; i++) {
      if (actual == expected[i]) {
        LOGGER.info("Json NumberType match");
        LOGGER.info("Expected: " + toStringJsonNumberType(expected[i]));
        LOGGER.info("Actual:   " + toStringJsonNumberType(actual));
        return true;
      }
    }
    LOGGER.warning("Json NumberType mismatch");
    LOGGER.warning("Expected: " + toStringJsonNumberTypes(expected));
    LOGGER.warning("Actual:   " + toStringJsonNumberType(actual));
    return false;
  }

  /*********************************************************************************
   * {@code String toStringJsonNumberType(boolean numberType)}
   *********************************************************************************/
  public static String toStringJsonNumberType(boolean numberType) {
    if (numberType == INTEGRAL)
      return "INTEGRAL";
    else
      return "NON_INTEGRAL";
  }

  /*********************************************************************************
   * {@code String toStringJsonNumberTypes(boolean expected[])}
   *********************************************************************************/
  public static String toStringJsonNumberTypes(boolean expected[]) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < expected.length; i++) {
      sb.append("" + toStringJsonNumberType(expected[i]));
      if (i + 1 < expected.length)
        sb.append("|");
    }
    return sb.toString();
  }

  /*********************************************************************************
   * {@code boolean assertEquals(Object, Object)}
   *********************************************************************************/
  public static boolean assertEquals(Object expected, Object actual) {
    if (actual.equals(expected)) {
      LOGGER.info("Object match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("Object mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(boolean, boolean)}
   *********************************************************************************/
  public static boolean assertEquals(boolean expected, boolean actual) {
    if (actual == expected) {
      LOGGER.info("boolean match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("boolean mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(short, short)}
   *********************************************************************************/
  public static boolean assertEquals(short expected, short actual) {
    if (actual == expected) {
      LOGGER.info("short match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("short mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(int, int)}
   *********************************************************************************/
  public static boolean assertEquals(int expected, int actual) {
    if (actual == expected) {
      LOGGER.info("int match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("int mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(long, long)}
   *********************************************************************************/
  public static boolean assertEquals(long expected, long actual) {
    if (actual == expected) {
      LOGGER.info("long match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("long mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(float, float)}
   *********************************************************************************/
  public static boolean assertEquals(float expected, float actual) {
    if (actual == expected) {
      LOGGER.info("float match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("float mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(double, double)}
   *********************************************************************************/
  public static boolean assertEquals(double expected, double actual) {
    if (actual == expected) {
      LOGGER.info("double match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("double mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(BigDecimal, BigDecimal)}
   *********************************************************************************/
  public static boolean assertEquals(BigDecimal expected, BigDecimal actual) {
    if (actual.equals(expected)) {
      LOGGER.info("BigDecimal match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("BigDecimal mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(BigInteger, BigInteger)}
   *********************************************************************************/
  public static boolean assertEquals(BigInteger expected, BigInteger actual) {
    if (actual.equals(expected)) {
      LOGGER.info("BigInteger match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("BigInteger mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(String, String)}
   *********************************************************************************/
  public static boolean assertEquals(String expected, String actual) {
    if (actual.equals(expected)) {
      LOGGER.info("String match");
      LOGGER.info("Expected: " + expected);
      LOGGER.info("Actual:   " + actual);
      return true;
    } else {
      LOGGER.warning("String mismatch");
      LOGGER.warning("Expected: " + expected);
      LOGGER.warning("Actual:   " + actual);
      return false;
    }
  }

  /*********************************************************************************
   * {@code boolean assertEquals(JsonValue, JsonValue)}
   *********************************************************************************/
  public static boolean assertEquals(JsonValue expected, JsonValue actual) {
    if (actual.equals(expected)) {
      LOGGER.info("JsonValue match");
      LOGGER.info("Expected: " + toStringJsonValue(expected));
      LOGGER.info("Actual:   " + toStringJsonValue(actual));
      return true;
    } else {
      LOGGER.warning("JsonValue mismatch");
      LOGGER.warning("Expected: " + toStringJsonValue(expected));
      LOGGER.warning("Actual:   " + toStringJsonValue(actual));
      return false;
    }
  }

  /*********************************************************************************
   * {@code String getNumberTypeString(boolean numberType)}
   *********************************************************************************/
  public static String getNumberTypeString(boolean numberType) {
    if (numberType == INTEGRAL)
      return "INTEGRAL";
    else
      return "NON_INTEGRAL";
  }

  /*********************************************************************************
   * {@code boolean getNumberType(String numberType)}
   *********************************************************************************/
  public static boolean getNumberType(String numberType) {
    if (numberType.equals("INTEGRAL"))
      return INTEGRAL;
    else
      return NON_INTEGRAL;
  }

  /*********************************************************************************
   * {@code String getValueTypeString(JsonValue.ValueType valueType)}
   *********************************************************************************/
  public static String getValueTypeString(JsonValue.ValueType valueType) {
    switch (valueType) {
    case ARRAY:
      return "ARRAY";
    case FALSE:
      return "FALSE";
    case NULL:
      return "NULL";
    case NUMBER:
      return "NUMBER";
    case OBJECT:
      return "OBJECT";
    case STRING:
      return "STRING";
    case TRUE:
      return "TRUE";
    default:
      return null;
    }
  }

  /*********************************************************************************
   * {@code JsonValue.ValueType getValueType(String valueType)}
   *********************************************************************************/
  public static JsonValue.ValueType getValueType(String valueType) {
    if (valueType.equals("ARRAY"))
      return JsonValue.ValueType.ARRAY;
    if (valueType.equals("FALSE"))
      return JsonValue.ValueType.FALSE;
    if (valueType.equals("NULL"))
      return JsonValue.ValueType.NULL;
    if (valueType.equals("NUMBER"))
      return JsonValue.ValueType.NUMBER;
    if (valueType.equals("OBJECT"))
      return JsonValue.ValueType.OBJECT;
    if (valueType.equals("STRING"))
      return JsonValue.ValueType.STRING;
    if (valueType.equals("TRUE"))
      return JsonValue.ValueType.TRUE;
    else
      return null;
  }

  /*********************************************************************************
   * {@code void dumpEventType(JsonParser.Event eventType)}
   *********************************************************************************/
  public static void dumpEventType(JsonParser.Event eventType) {
    LOGGER.info("JsonParser.Event=" + eventType);
  }

  /*********************************************************************************
   * {@code getEventTypeString(JsonParser.Event eventType)}
   *********************************************************************************/
  public static String getEventTypeString(JsonParser.Event eventType) {
    switch (eventType) {
    case START_ARRAY:
      return "START_ARRAY";
    case START_OBJECT:
      return "START_OBJECT";
    case KEY_NAME:
      return "KEY_NAME";
    case VALUE_STRING:
      return "VALUE_STRING";
    case VALUE_NUMBER:
      return "VALUE_NUMBER";
    case VALUE_TRUE:
      return "VALUE_TRUE";
    case VALUE_FALSE:
      return "VALUE_FALSE";
    case VALUE_NULL:
      return "VALUE_NULL";
    case END_OBJECT:
      return "END_OBJECT";
    case END_ARRAY:
      return "END_ARRAY";
    default:
      return null;
    }
  }

  /*********************************************************************************
   * {@code JsonParser.Event getEventType(String eventType)}
   *********************************************************************************/
  public static JsonParser.Event getEventType(String eventType) {
    if (eventType.equals("START_ARRAY"))
      return JsonParser.Event.START_ARRAY;
    if (eventType.equals("START_OBJECT"))
      return JsonParser.Event.START_OBJECT;
    if (eventType.equals("KEY_NAME"))
      return JsonParser.Event.KEY_NAME;
    if (eventType.equals("VALUE_STRING"))
      return JsonParser.Event.VALUE_STRING;
    if (eventType.equals("VALUE_NUMBER"))
      return JsonParser.Event.VALUE_NUMBER;
    if (eventType.equals("VALUE_TRUE"))
      return JsonParser.Event.VALUE_TRUE;
    if (eventType.equals("VALUE_FALSE"))
      return JsonParser.Event.VALUE_FALSE;
    if (eventType.equals("VALUE_NULL"))
      return JsonParser.Event.VALUE_NULL;
    if (eventType.equals("END_OBJECT"))
      return JsonParser.Event.END_OBJECT;
    if (eventType.equals("END_ARRAY"))
      return JsonParser.Event.END_ARRAY;
    else
      return null;
  }

  /*********************************************************************************
   * {@code String getConfigName(String configValue)}
   *********************************************************************************/
  public static String getConfigName(String configValue) {
    if (configValue.equals(JsonGenerator.PRETTY_PRINTING))
      return "JsonGenerator.PRETTY_PRINTING";
    else if (configValue.equals(JSONP_Util.FOO_CONFIG))
      return "JSONP_Util.FOO_CONFIG";
    else
      return null;
  }

  /*********************************************************************************
   * {@code String getConfigValue(String configProp)}
   *********************************************************************************/
  public static String getConfigValue(String configProp) {
    if (configProp.equals("JsonGenerator.PRETTY_PRINING"))
      return JsonGenerator.PRETTY_PRINTING;
    else if (configProp.equals("JSONP_Util.FOO_CONFIG"))
      return JSONP_Util.FOO_CONFIG;
    else
      return null;
  }

  /*********************************************************************************
   * {@code void dumpConfigMap(Map<String,?> map, String msg)}
   *********************************************************************************/
  public static void dumpConfigMap(Map<String, ?> map, String msg) {
    LOGGER.info("*** Beg: Dumping Config Map contents ***");
    if (msg != null)
      LOGGER.info("*** Message: " + msg);
    for (Map.Entry<String, ?> entry : map.entrySet()) {
      LOGGER.info("\"" + entry.getKey() + "\":" + entry.getValue());
    }
    LOGGER.info("*** End: Dumping Config Map contents ***");
  }

  /*********************************************************************************
   * {@code void dumpConfigMap(Map<String,?> map)}
   *********************************************************************************/
  public static void dumpConfigMap(Map<String, ?> map) {
    dumpConfigMap(map, null);
  }

  /*********************************************************************************
   * {@code boolean doConfigCheck(Map<String,?> config, int expectedSize)}
   *********************************************************************************/
  public static boolean doConfigCheck(Map<String, ?> config, int expectedSize) {
    return doConfigCheck(config, expectedSize, null);
  }

  public static boolean doConfigCheck(Map<String, ?> config, int expectedSize,
      String[] expectedProps) {
    boolean pass = true;
    dumpConfigMap(config);
    LOGGER.info("Checking factory configuration property size");
    if (config.size() != expectedSize) {
      LOGGER.warning("Expecting no of properties=" + expectedSize + ", got="
          + config.size());
      pass = false;
    } else {
      LOGGER.info("Expecting no of properties=" + expectedSize + ", got="
          + config.size());
    }
    if (expectedSize != 0 && expectedProps != null) {
      LOGGER.info("Checking factory configuration property name and value");
      for (int i = 0; i < expectedProps.length; i++) {
        if (config.containsKey(expectedProps[i])) {
          LOGGER.info("Does contain key: " + expectedProps[i] + " - expected.");
          if (!JSONP_Util.assertEquals(true, config.get(expectedProps[i]))) {
            pass = false;
          }
        } else {
          LOGGER.warning(
              "Does not contain key: " + expectedProps[i] + " - unexpected.");
          pass = false;
        }
      }
    }
    return pass;
  }

  /*********************************************************************************
   * {@code boolean isEmptyConfig(Map<String, ?> config)}
   *********************************************************************************/
  public boolean isEmptyConfig(Map<String, ?> config) {
    LOGGER.info("isEmptyConfig");
    return config.isEmpty();
  }

  /*********************************************************************************
   * {@code Map<String, ?> getEmptyConfig()}
   *********************************************************************************/
  public static Map<String, ?> getEmptyConfig() {
    LOGGER.info("getEmptyConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    return config;
  }

  /*********************************************************************************
   * {@code Map<String, ?> getPrettyPrintingConfig()}
   *********************************************************************************/
  public static Map<String, ?> getPrettyPrintingConfig() {
    LOGGER.info("getPrettyPrintConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    LOGGER.info("Added property: JsonGenerator.PRETTY_PRINTING");
    config.put(JsonGenerator.PRETTY_PRINTING, true);
    return config;
  }

  /*********************************************************************************
   * {@code Map<String, ?> getFooConfig()}
   *********************************************************************************/
  public static Map<String, ?> getFooConfig() {
    LOGGER.info("getFooConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    LOGGER.info("Added property: JSONP_Util.FOO_CONFIG");
    config.put(JSONP_Util.FOO_CONFIG, true);
    return config;
  }

  /*********************************************************************************
   * {@code Map<String, ?> getAllConfig()}
   *********************************************************************************/
  public static Map<String, ?> getAllConfig() {
    LOGGER.info("getAllConfig");
    Map<String, Object> config = new HashMap<String, Object>();
    LOGGER.info("Added property: JsonGenerator.PRETTY_PRINTING");
    config.put(JsonGenerator.PRETTY_PRINTING, true);
    LOGGER.info("Added property: JSONP_Util.FOO_CONFIG");
    config.put(JSONP_Util.FOO_CONFIG, true);
    return config;
  }

  /*********************************************************************************
   * {@code JsonObject createJsonObjectFromString(String jsonObjData)}
   *********************************************************************************/
  public static JsonObject createJsonObjectFromString(String jsonObjData) {
    JsonReader reader = null;
    JsonObject object = null;
    try {
      reader = Json.createReader(new StringReader(jsonObjData));
      object = reader.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        reader.close();
    }
    return object;
  }

  /*********************************************************************************
   * {@code JsonArray createJsonArrayFromString(String jsonArrData)}
   *********************************************************************************/
  public static JsonArray createJsonArrayFromString(String jsonArrData) {
    JsonReader reader = null;
    JsonArray array = null;
    try {
      reader = Json.createReader(new StringReader(jsonArrData));
      array = reader.readArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null)
        reader.close();
    }
    return array;
  }

  /*********************************************************************************
   * {@code void writeJsonObjectFromString(JsonWriter writer, String jsonObjData)}
   *********************************************************************************/
  public static void writeJsonObjectFromString(JsonWriter writer,
      String jsonObjData) {
    try {
      JsonObject jsonObject = createJsonObjectFromString(jsonObjData);
      writer.writeObject(jsonObject);
      writer.close();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * {@code void writeJsonArrayFromString(JsonWriter writer, String jsonArrData)}
   *********************************************************************************/
  public static void writeJsonArrayFromString(JsonWriter writer,
      String jsonArrData) {
    try {
      JsonArray jsonArray = createJsonArrayFromString(jsonArrData);
      writer.writeArray(jsonArray);
      writer.close();
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
    }
  }

  /*********************************************************************************
   * {@code void testKeyStringValue(JsonParser parser, String name, String value)}
   *********************************************************************************/
  public static void testKeyStringValue(JsonParser parser, String name,
      String value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_STRING) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_STRING)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    }
    String keyvalue = parser.getString();
    if (!keyvalue.equals(value)) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testKeyIntegerValue(JsonParser parser, String name, int value)}
   *********************************************************************************/
  public static void testKeyIntegerValue(JsonParser parser, String name,
      int value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    int keyvalue = parser.getInt();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testKeyDoubleValue(JsonParser parser, String name, double value)}
   *********************************************************************************/
  public static void testKeyDoubleValue(JsonParser parser, String name,
      double value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    double keyvalue = parser.getBigDecimal().doubleValue();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testKeyLongValue(JsonParser parser, String name, long value)}
   *********************************************************************************/
  public static void testKeyLongValue(JsonParser parser, String name,
      long value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    long keyvalue = parser.getLong();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testKeyBigDecimalValue(JsonParser parser, String name, BigDecimal
   * value)}
   *********************************************************************************/
  public static void testKeyBigDecimalValue(JsonParser parser, String name,
      BigDecimal value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    BigDecimal keyvalue = parser.getBigDecimal();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testKeyTrueValue(JsonParser parser, String name)}
   *********************************************************************************/
  public static void testKeyTrueValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_TRUE) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_TRUE)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code void testKeyFalseValue(JsonParser parser, String name)}
   *********************************************************************************/
  public static void testKeyFalseValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_FALSE) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_FALSE)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code void testKeyNullValue(JsonParser parser, String name)}
   *********************************************************************************/
  public static void testKeyNullValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.VALUE_NULL) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NULL)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code void testKeyStartObjectValue(JsonParser parser, String name)}
   *********************************************************************************/
  public static void testKeyStartObjectValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.START_OBJECT) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.START_OBJECT)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code void testKeyStartArrayValue(JsonParser parser, String name)}
   *********************************************************************************/
  public static void testKeyStartArrayValue(JsonParser parser, String name) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();

    if (e != JsonParser.Event.KEY_NAME) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.KEY_NAME)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyname = parser.getString();
    if (!name.equals(keyname)) {
      LOGGER.warning("Expected keyname: " + name + ", got keyname: " + keyname);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyname: " + keyname);
    }

    if (!checkNextParserEvent(parser))
      return;
    e = parser.next();
    if (e != JsonParser.Event.START_ARRAY) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.START_ARRAY)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code boolean checkNextParserEvent(JsonParser parser)}
   *********************************************************************************/
  public static boolean checkNextParserEvent(JsonParser parser) {
    if (!parser.hasNext()) {
      LOGGER.warning("no next parser event found - unexpected");
      parseErrs++;
      return false;
    } else
      return true;
  }

  /*********************************************************************************
   * {@code JsonParser.Event getNextParserEvent(JsonParser parser)}
   *********************************************************************************/
  public static JsonParser.Event getNextParserEvent(JsonParser parser) {
    if (parser.hasNext())
      return parser.next();
    else
      return null;
  }

  /*********************************************************************************
   * {@code JsonParser.Event getNextSpecificParserEvent(JsonParser parser,
   * JsonParser.Event thisEvent)}
   *********************************************************************************/
  public static JsonParser.Event getNextSpecificParserEvent(JsonParser parser,
      JsonParser.Event thisEvent) {
    while (parser.hasNext()) {
      JsonParser.Event event = parser.next();
      if (event == thisEvent)
        return event;
    }
    return null;
  }

  /*********************************************************************************
   * {@code void testEventType(JsonParser parser, JsonParser.Event expEvent)}
   *********************************************************************************/
  public static void testEventType(JsonParser parser,
      JsonParser.Event expEvent) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != expEvent) {
      LOGGER.warning("Expected event: " + getEventTypeString(expEvent)
          + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
  }

  /*********************************************************************************
   * {@code void testStringValue(JsonParser parser, String value)}
   *********************************************************************************/
  public static void testStringValue(JsonParser parser, String value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_STRING) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_STRING)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    String keyvalue = parser.getString();
    if (!keyvalue.equals(value)) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testIntegerValue(JsonParser parser, int value)}
   *********************************************************************************/
  public static void testIntegerValue(JsonParser parser, int value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    int keyvalue = parser.getInt();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testDoubleValue(JsonParser parser, double value)}
   *********************************************************************************/
  public static void testDoubleValue(JsonParser parser, double value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    double keyvalue = parser.getBigDecimal().doubleValue();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testLongValue(JsonParser parser, long value)}
   *********************************************************************************/
  public static void testLongValue(JsonParser parser, long value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    long keyvalue = parser.getLong();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testBigDecimalValue(JsonParser parser, BigDecimal value)}
   *********************************************************************************/
  public static void testBigDecimalValue(JsonParser parser, BigDecimal value) {
    if (!checkNextParserEvent(parser))
      return;
    JsonParser.Event e = parser.next();
    if (e != JsonParser.Event.VALUE_NUMBER) {
      LOGGER.warning(
          "Expected event: " + getEventTypeString(JsonParser.Event.VALUE_NUMBER)
              + ", got event: " + getEventTypeString(e));
      parseErrs++;
    } else {
      LOGGER.info("Got expected event: " + getEventTypeString(e));
    }
    BigDecimal keyvalue = parser.getBigDecimal();
    if (keyvalue != value) {
      LOGGER.warning(
          "Expected keyvalue: " + value + ", got keyvalue: " + keyvalue);
      parseErrs++;
    } else {
      LOGGER.info("Got expected keyvalue: " + keyvalue);
    }
  }

  /*********************************************************************************
   * {@code void testTrueValue(JsonParser parser, JsonParser.Event expEvent)}
   *********************************************************************************/
  public static void testTrueValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * {@code void testFalseValue(JsonParser parser, JsonParser.Event expEvent)}
   *********************************************************************************/
  public static void testFalseValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * {@code void testNullValue(JsonParser parser, JsonParser.Event expEvent)}
   *********************************************************************************/
  public static void testNullValue(JsonParser parser,
      JsonParser.Event expEvent) {
    testEventType(parser, expEvent);
  }

  /*********************************************************************************
   * {@code resetParseErrs()}
   *********************************************************************************/
  public static void resetParseErrs() {
    parseErrs = 0;
  }

  /*********************************************************************************
   * {@code int getParseErrs()}
   *********************************************************************************/
  public static int getParseErrs() {
    return parseErrs;
  }

  /*********************************************************************************
   * {@code String convertUnicodeCharToString(Char c)}
   *
   * Convert unicode to string value of form U+NNNN where NNNN are 4 hex digits
   *
   * Use a binary or with hex value '0x10000' when converting unicode char to
   * hex string and remove 1st char to get the 4 hex digits we need.
   *********************************************************************************/
  public static String convertUnicodeCharToString(char c) {
    return "\\u" + Integer.toHexString((int) c | 0x10000).substring(1);
  }

  /*********************************************************************************
   * {@code boolean isUnicodeControlChar(Char c)}
   *
   * The following unicode control chars:
   *
   * U+0000 - U+001F and U+007F U+0080 - U+009F
   *********************************************************************************/
  public static boolean isUnicodeControlChar(char c) {

    if ((c >= '\u0000' && c <= '\u001F') || (c == '\u007F')
        || (c >= '\u0080' && c <= '\u009F'))
      return true;
    else
      return false;
  }

  /*********************************************************************************
   * {@code void writeStringToFile(String string, String file, String encoding)}
   *********************************************************************************/
  public static void writeStringToFile(String string, String file,
      String encoding) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(fos, encoding);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * {@code String readStringFromFile(String file, String encoding)}
   *********************************************************************************/
  public static String readStringFromFile(String file, String encoding) {
    StringBuffer buffer = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis, encoding);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * {@code void writeStringToStream(String string, OutputStream os, String encoding)}
   *********************************************************************************/
  public static void writeStringToStream(String string, OutputStream os,
      String encoding) {
    try {
      Writer out = new OutputStreamWriter(os, encoding);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * {@code String readStringFromStream(InputStream is, String encoding)}
   *********************************************************************************/
  public static String readStringFromStream(InputStream is, String encoding) {
    StringBuffer buffer = new StringBuffer();
    try {
      InputStreamReader isr = new InputStreamReader(is, encoding);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * {@code void writeStringToFile(String string, String file, Charset charset)}
   *********************************************************************************/
  public static void writeStringToFile(String string, String file,
      Charset charset) {
    try {
      FileOutputStream fos = new FileOutputStream(file);
      Writer out = new OutputStreamWriter(fos, charset);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * {@code String readStringFromFile(String file, Charset charset)}
   *********************************************************************************/
  public static String readStringFromFile(String file, Charset charset) {
    StringBuffer buffer = new StringBuffer();
    try {
      FileInputStream fis = new FileInputStream(file);
      InputStreamReader isr = new InputStreamReader(fis, charset);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * {@code void writeStringToStream(String string, OutputStream os, Charset charset)}
   *********************************************************************************/
  public static void writeStringToStream(String string, OutputStream os,
      Charset charset) {
    try {
      Writer out = new OutputStreamWriter(os, charset);
      out.write(string);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*********************************************************************************
   * {@code String readStringFromStream(InputStream is, Charset charset)}
   *********************************************************************************/
  public static String readStringFromStream(InputStream is, Charset charset) {
    StringBuffer buffer = new StringBuffer();
    try {
      InputStreamReader isr = new InputStreamReader(is, charset);
      Reader in = new BufferedReader(isr);
      int ch;
      while ((ch = in.read()) > -1) {
        buffer.append((char) ch);
      }
      in.close();
      return buffer.toString();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /*********************************************************************************
   * {@code Charset getCharset(String encoding)}
   *********************************************************************************/
  public static Charset getCharset(String encoding) {
    Charset cs = null;

    try {
      cs = Charset.forName(encoding);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cs;
  }

  /*********************************************************************************
   * {@code void dumpLocation(JsonLocation location)}
   *********************************************************************************/
  public static void dumpLocation(JsonLocation location) {
    if (location != null) {
      LOGGER.info("JsonLocation info: lineNumber=" + location.getLineNumber()
              + ", columnNumber=" + location.getColumnNumber()
              + ", streamOffset=" + location.getStreamOffset());
    } else {
      LOGGER.info("JsonLocation is null - no location info");
    }
  }

  /*********************************************************************************
   * {@code void dumpLocation(JsonParser parser)}
   *********************************************************************************/
  public static void dumpLocation(JsonParser parser) {
    dumpLocation(parser.getLocation());
  }

  /*********************************************************************************
   * {@code boolean assertEquals(JsonLocation, JsonLocation)}
   *********************************************************************************/
  public static boolean assertEquals(JsonLocation expLoc, JsonLocation actLoc) {
    if (expLoc.getLineNumber() == actLoc.getLineNumber()
        && expLoc.getColumnNumber() == actLoc.getColumnNumber()
        && expLoc.getStreamOffset() == actLoc.getStreamOffset()) {
      LOGGER.info("JsonLocations equal - match (Success)");
      LOGGER.info(
          "Expected: JsonLocation info: lineNumber=" + expLoc.getLineNumber()
              + ", columnNumber=" + expLoc.getColumnNumber() + ", streamOffset="
              + expLoc.getStreamOffset());
      LOGGER.info(
          "Actual:   JsonLocation info: lineNumber=" + actLoc.getLineNumber()
              + ", columnNumber=" + actLoc.getColumnNumber() + ", streamOffset="
              + actLoc.getStreamOffset());
      return true;
    } else {
      LOGGER.warning("JsonLocations not equal - mismatch (Failure)");
      LOGGER.warning(
          "Expected: JsonLocation info: lineNumber=" + expLoc.getLineNumber()
              + ", columnNumber=" + expLoc.getColumnNumber() + ", streamOffset="
              + expLoc.getStreamOffset());
      LOGGER.warning(
          "Actual:   JsonLocation info: lineNumber=" + actLoc.getLineNumber()
              + ", columnNumber=" + actLoc.getColumnNumber() + ", streamOffset="
              + actLoc.getStreamOffset());
      return false;
    }
  }

  /*********************************************************************************
   * {@code void addFileToClassPath(String s)}
   *********************************************************************************/
  public static void addFileToClassPath(String s) throws Exception {
    addFileToClassPath(new File(s));
  }

  /*********************************************************************************
   * {@code void addFileToClassPath(File f)}
   *********************************************************************************/
  public static void addFileToClassPath(File f) throws Exception {
    addURLToClassPath((f.toURI()).toURL());
  }

  /*********************************************************************************
   * {@code void addURLToClassPath(URL url)}
   *********************************************************************************/
  public static void addURLToClassPath(URL url) throws Exception {
    LOGGER.info("addURLToClassPath-> " + url.toString());
    URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader
        .getSystemClassLoader();
    try {
      Class urlClassLoaderClass = URLClassLoader.class;
      Method method = urlClassLoaderClass.getDeclaredMethod("addURL",
          new Class[] { URL.class });
      method.setAccessible(true);
      method.invoke(urlClassLoader, new Object[] { url });
    } catch (Throwable t) {
      t.printStackTrace();
      throw new IOException("Error, could not add URL to system classloader");
    }
  }

  /*********************************************************************************
   * {@code JsonArray buildJsonArrayFooBar}
   *********************************************************************************/
  public static JsonArray buildJsonArrayFooBar() {
    try {
      JsonArray jsonArray = Json.createArrayBuilder().add("foo").add("bar")
          .build();
      return jsonArray;
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  public static final String JSONARRAYFOOBAR = "[\"foo\",\"bar\"]";

  /*********************************************************************************
   * {@code JsonObject buildJsonObjectFooBar()}
   *********************************************************************************/
  public static JsonObject buildJsonObjectFooBar() {
    try {
      JsonObject jsonObject = Json.createObjectBuilder().add("foo", "bar")
          .build();
      return jsonObject;
    } catch (Exception e) {
      LOGGER.warning("Exception occurred: " + e);
      return null;
    }
  }

  public static final String JSONOBJECTFOOBAR = "{\"foo\":\"bar\"}";

  /*********************************************************************************
   * {@code JsonObject createSampleJsonObject()}
   *
   * Assertion ids covered: 400/401/403/404/406/408/409
   *********************************************************************************/
  public static JsonObject createSampleJsonObject() throws Exception {
    JsonObject object = Json.createObjectBuilder().add("firstName", "John")
        .add("lastName", "Smith").add("age", 25).add("elderly", JsonValue.FALSE)
        .add("patriot", JsonValue.TRUE)
        .add("address",
            Json.createObjectBuilder().add("streetAddress", "21 2nd Street")
                .add("city", "New York").add("state", "NY")
                .add("postalCode", "10021"))
        .add("phoneNumber",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("type", "home")
                    .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder().add("type", "cell")
                    .add("number", "646 555-4567")))
        .add("objectOfFooBar",
            Json.createObjectBuilder()
                .add("objectFooBar", buildJsonObjectFooBar())
                .add("arrayFooBar", buildJsonArrayFooBar()))
        .add("arrayOfFooBar", Json.createArrayBuilder()
            .add(buildJsonObjectFooBar()).add(buildJsonArrayFooBar()))
        .build();
    return object;
  }

  /*********************************************************************************
   * {@code EXPECTED_SAMPLEJSONOBJECT_TEXT} Constant defining expected Json text output
   * of above sample JsonObject
   *********************************************************************************/
  public final static String EXPECTED_SAMPLEJSONOBJECT_TEXT = "{\"firstName\":\"John\",\"lastName\":\"Smith\",\"age\":25,\"elderly\":false,\"patriot\":true,"
      + "\"address\":{\"streetAddress\":\"21 2nd Street\",\"city\":\"New York\",\"state\":\"NY\","
      + "\"postalCode\":\"10021\"},\"phoneNumber\":[{\"type\":\"home\",\"number\":\"212 555-1234\"},"
      + "{\"type\":\"cell\",\"number\":\"646 555-4567\"}],\"objectOfFooBar\":{\"objectFooBar\":"
      + "{\"foo\":\"bar\"},\"arrayFooBar\":[\"foo\",\"bar\"]},\"arrayOfFooBar\":[{\"foo\":\"bar\"},"
      + "[\"foo\",\"bar\"]]}";

  /*********************************************************************************
   * {@code JsonObject createSampleJsonObject2()}
   *********************************************************************************/
  public static JsonObject createSampleJsonObject2() throws Exception {
    JsonObject object = Json.createObjectBuilder().add("firstName", "John")
        .add("lastName", "Smith").add("age", 25).add("elderly", JsonValue.FALSE)
        .add("patriot", JsonValue.TRUE)
        .add("address",
            Json.createObjectBuilder().add("streetAddress", "21 2nd Street")
                .add("city", "New York").add("state", "NY")
                .add("postalCode", "10021"))
        .add("phoneNumber",
            Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("type", "home")
                    .add("number", "212 555-1234"))
                .add(Json.createObjectBuilder().add("type", "cell")
                    .add("number", "535 444-1234")))
        .build();
    return object;
  }

  /*********************************************************************************
   * {@code JsonArray createSampleJsonArray()}
   *
   * Assertion ids covered: 400/401/402/403/404/406/409
   *********************************************************************************/
  public static JsonArray createSampleJsonArray() throws Exception { // Indices
    JsonArray array = Json.createArrayBuilder().add(Json.createObjectBuilder() // 0
        .add("name1", "value1").add("name2", "value2")).add(JsonValue.TRUE)
        .add(JsonValue.FALSE).add(JsonValue.NULL) // 1,2,3
        .add(100).add(200L).add("string") // 4,5,6
        .add(BigDecimal.valueOf(123456789)).add(new BigInteger("123456789"))// 7,8
        .add(Json.createObjectBuilder() // 9
            .add("name3", "value3").add("name4", "value4"))
        .add(true).add(false).addNull() // 10,11,12
        .add(Json.createArrayBuilder() // 13
            .add(2).add(4))
        .add(Json.createObjectBuilder() // 14
            .add("objectFooBar", buildJsonObjectFooBar())
            .add("arrayFooBar", buildJsonArrayFooBar()))
        .add(Json.createArrayBuilder() // 15
            .add(buildJsonObjectFooBar()).add(buildJsonArrayFooBar()))
        .build();
    return array;
  }

  /*********************************************************************************
   * {@code EXPECTED_SAMPLEJSONARRAY_TEXT} Constant defining expected Json text output
   * of above sample JsonArray
   *********************************************************************************/
  public final static String EXPECTED_SAMPLEJSONARRAY_TEXT = "[{\"name1\":\"value1\",\"name2\":\"value2\"},true,false,null,100,200,\"string\",123456789,123456789,"
      + "{\"name3\":\"value3\",\"name4\":\"value4\"},true,false,null,[2,4],{\"objectFooBar\":"
      + "{\"foo\":\"bar\"},\"arrayFooBar\":[\"foo\",\"bar\"]},[{\"foo\":\"bar\"},[\"foo\",\"bar\"]]]";

  /*********************************************************************************
   * {@code JsonArray createSampleJsonArray2()}
   *********************************************************************************/
  public static JsonArray createSampleJsonArray2() throws Exception { // Indices
    JsonArray array = Json.createArrayBuilder().add(Json.createObjectBuilder() // 0
        .add("name1", "value1").add("name2", "value2")).add(JsonValue.TRUE)
        .add(JsonValue.FALSE).add(JsonValue.NULL) // 1,2,3
        .add(Integer.MAX_VALUE).add(Long.MAX_VALUE).add("string") // 4,5,6
        .add(Json.createObjectBuilder() // 7
            .add("name3", "value3").add("name4", "value4"))
        .add(true).add(false).addNull() // 8,9,10
        .add(Json.createArrayBuilder() // 11
            .add(1).add(3))
        .build();
    return array;
  }
}

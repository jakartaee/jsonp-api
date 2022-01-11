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

package jsonp.tck.common;


import java.util.*;

public final class JSONP_Data {

  public static final String unicodeControlCharsEscaped = "\\u0000\\u0001\\u0002\\u0003\\u0004\\u0005\\u0006\\u0007\\u0008\\u0009"
      + "\\u000a\\u000b\\u000c\\u000d\\u000e\\u000f"
      + "\\u000A\\u000B\\u000C\\u000D\\u000E\\u000F"
      + "\\u0010\\u0011\\u0012\\u0013\\u0014\\u0015\\u0016\\u0017\\u0018\\u0019"
      + "\\u001a\\u001b\\u001c\\u001d\\u001e\\u001f\\u007f"
      + "\\u001A\\u001B\\u001C\\u001D\\u001E\\u001F\\u007F"
      + "\\u0080\\u0081\\u0082\\u0083\\u0084\\u0085\\u0086\\u0087\\u0088\\u0089"
      + "\\u008a\\u008b\\u008c\\u008d\\u008e\\u008f"
      + "\\u008A\\u008B\\u008C\\u008D\\u008E\\u008F"
      + "\\u0090\\u0091\\u0092\\u0093\\u0094\\u0095\\u0096\\u0097\\u0098\\u0099"
      + "\\u009a\\u009b\\u009c\\u009d\\u009e\\u009f"
      + "\\u009A\\u009B\\u009C\\u009D\\u009E\\u009F";

  // NOTE: For the unicode values u000a and u000d we need to use the Java
  // escape for both NL and CR as \n and \r respectively
  public static final String unicodeControlCharsNonEscaped = "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u0009"
      + "\n\u000b\u000c\r\u000e\u000f" + "\n\u000B\u000C\r\u000E\u000F"
      + "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019"
      + "\u001a\u001b\u001c\u001d\u001e\u001f\u007f"
      + "\u001A\u001B\u001C\u001D\u001E\u001F\u007F"
      + "\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089"
      + "\u008a\u008b\u008c\u008d\u008e\u008f"
      + "\u008A\u008B\u008C\u008D\u008E\u008F"
      + "\u0090\u0091\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099"
      + "\u009a\u009b\u009c\u009d\u009e\u009f"
      + "\u009A\u009B\u009C\u009D\u009E\u009F";

  // Standard backslash escape characters
  public static final String escapeCharsAsString = "\"\\/\b\f\n\r\t";

  public static final String asciiCharacters = "!@#$%^&*()_+|~1234567890-=;',./<>? "
      + "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

  public static final String jsonArrayTestData = "[ \"\", \"string\", 100, true, false, null, {}, { \"name\" : \"value\" }, [], [ \"one\", \"two\" ] ]";

  public static final String jsonObjectTestData = "{"
      + "\"emptyString\" : \"\"," + "\"emptyArray\" : [],"
      + "\"emptyObject\" : {}," + "\"string\" : \"string\","
      + "\"number\" :  100," + "\"true\" : true," + "\"false\" : false,"
      + "\"null\" : null," + "\"object\" : { \"name\" : \"value\" },"
      + "\"array\" : [ \"one\", \"two\" ]," + "}";

  public static final String jsonArrayTestData2 = "[" + "\"\"," + "[]," + "{},"
      + "\"string\"," + "100," + "true," + "false," + "null,"
      + "{ \"name\" : \"value\" }," + "[ \"one\", \"two\" ]," + "]";

  public static final String jsonObjectWithAllTypesOfData = "{"
      + "\"emptyString\" : \"\"," + "\"emptyArray\" : [],"
      + "\"emptyObject\" : {}," + "\"string\" : \"string\","
      + "\"number\" :  100," + "\"true\" : true," + "\"false\" : false,"
      + "\"null\" : null," + "\"object\" : {" + "\"emptyString\" : \"\","
      + "\"emptyArray\" : []," + "\"emptyObject\" : {},"
      + "\"string\" : \"string\"," + "\"number\" :  100," + "\"true\" : true,"
      + "\"false\" : false," + "\"null\" : null,"
      + "\"object\" : { \"name\" : \"value\" },"
      + "\"array\" : [ \"one\", \"two\" ]" + "},"
      + "\"array\" : [ \"string\", 100, true, false, null, { \"name\" : \"value\" }"
      + ", [ \"one\", \"two\" ] " + "]," + "\"intPositive\" : 100,"
      + "\"intNegative\" : -100," + "\"longMax\"     : 9223372036854775807,"
      + "\"longMin\"     : -9223372036854775808," + "\"fracPositive\" : 0.5,"
      + "\"fracNegative\" : -0.5," + "\"expPositive1\" : 7e3,"
      + "\"expPositive2\" : 7e+3," + "\"expPositive3\" : 9E3,"
      + "\"expPositive4\" : 9E+3," + "\"expNegative1\" : 7e-3,"
      + "\"expNegative2\" : 7E-3," + "\"asciiChars\" : \"" + asciiCharacters
      + "\"" + "}";

  public static final String jsonArrayWithAllTypesOfData = "[" + "\"\"," + "[],"
      + "{}," + "\"string\"," + "100," + "true," + "false," + "null," + "{"
      + "\"emptyString\" : \"\"," + "\"emptyArray\" : [],"
      + "\"emptyObject\" : {}," + "\"string\" : \"string\","
      + "\"number\" :  100," + "\"true\" : true," + "\"false\" : false,"
      + "\"null\" : null," + "\"object\" : { \"name\" : \"value\" },"
      + "\"array\" : [ \"one\", \"two\" ]" + "},"
      + "[ \"string\", 100, true, false, null, { \"name\" : \"value\" }"
      + ", [ \"one\", \"two\" ] " + "]," + "100," + "-100,"
      + "9223372036854775807," + "-9223372036854775808," + "0.5," + "-0.5,"
      + "7e3," + "7e+3," + "9E3," + "9E+3," + "7e-3," + "7E-3," + "\""
      + asciiCharacters + "\"" + "]";

  public static final String jsonObjectWithLotsOfNestedObjectsData = "{"
      + "\"nested1\" : {" + "\"name1\" : \"value1\"," + "\"nested2\" : {"
      + "\"name2\" : \"value2\"," + "\"nested3\" : {"
      + "\"name3\" : \"value3\"," + "\"nested4\" : {"
      + "\"name4\" : \"value4\"," + "\"nested5\" : {"
      + "\"name5\" : \"value5\"," + "\"nested6\" : {"
      + "\"name6\" : \"value6\"," + "\"nested7\" : {"
      + "\"name7\" : \"value7\"," + "\"nested8\" : {"
      + "\"name8\" : \"value8\"," + "\"nested9\" : {"
      + "\"name9\" : \"value9\"," + "\"nested10\" : {"
      + "\"name10\" : \"value10\"," + "\"nested11\" : {"
      + "\"name11\" : \"value11\"," + "\"nested12\" : {"
      + "\"name12\" : \"value12\"," + "\"nested13\" : {"
      + "\"name13\" : \"value13\"," + "\"nested14\" : {"
      + "\"name14\" : \"value14\"," + "\"nested15\" : {"
      + "\"name15\" : \"value15\"," + "\"nested16\" : {"
      + "\"name16\" : \"value16\"," + "\"nested17\" : {"
      + "\"name17\" : \"value17\"," + "\"nested18\" : {"
      + "\"name18\" : \"value18\"," + "\"nested19\" : {"
      + "\"name19\" : \"value19\"," + "\"nested20\" : {"
      + "\"name20\" : \"value20\"," + "\"nested21\" : {"
      + "\"name21\" : \"value21\"," + "\"nested22\" : {"
      + "\"name22\" : \"value22\"," + "\"nested23\" : {"
      + "\"name23\" : \"value23\"," + "\"nested24\" : {"
      + "\"name24\" : \"value24\"," + "\"nested25\" : {"
      + "\"name25\" : \"value25\"," + "\"nested26\" : {"
      + "\"name26\" : \"value26\"," + "\"nested27\" : {"
      + "\"name27\" : \"value27\"," + "\"nested28\" : {"
      + "\"name28\" : \"value28\"," + "\"nested29\" : {"
      + "\"name29\" : \"value29\"," + "\"nested30\" : {"
      + "\"name30\" : \"value30\"" + "}" + "}" + "}" + "}" + "}" + "}" + "}"
      + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}"
      + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}";

  public static final String jsonArrayWithLotsOfNestedObjectsData = "[" + "{"
      + "\"name1\" : \"value1\"," + "\"nested2\" : {"
      + "\"name2\" : \"value2\"," + "\"nested3\" : {"
      + "\"name3\" : \"value3\"," + "\"nested4\" : {"
      + "\"name4\" : \"value4\"," + "\"nested5\" : {"
      + "\"name5\" : \"value5\"," + "\"nested6\" : {"
      + "\"name6\" : \"value6\"," + "\"nested7\" : {"
      + "\"name7\" : \"value7\"," + "\"nested8\" : {"
      + "\"name8\" : \"value8\"," + "\"nested9\" : {"
      + "\"name9\" : \"value9\"," + "\"nested10\" : {"
      + "\"name10\" : \"value10\"," + "\"nested11\" : {"
      + "\"name11\" : \"value11\"," + "\"nested12\" : {"
      + "\"name12\" : \"value12\"," + "\"nested13\" : {"
      + "\"name13\" : \"value13\"," + "\"nested14\" : {"
      + "\"name14\" : \"value14\"," + "\"nested15\" : {"
      + "\"name15\" : \"value15\"," + "\"nested16\" : {"
      + "\"name16\" : \"value16\"," + "\"nested17\" : {"
      + "\"name17\" : \"value17\"," + "\"nested18\" : {"
      + "\"name18\" : \"value18\"," + "\"nested19\" : {"
      + "\"name19\" : \"value19\"," + "\"nested20\" : {"
      + "\"name20\" : \"value20\"," + "\"nested21\" : {"
      + "\"name21\" : \"value21\"," + "\"nested22\" : {"
      + "\"name22\" : \"value22\"," + "\"nested23\" : {"
      + "\"name23\" : \"value23\"," + "\"nested24\" : {"
      + "\"name24\" : \"value24\"," + "\"nested25\" : {"
      + "\"name25\" : \"value25\"," + "\"nested26\" : {"
      + "\"name26\" : \"value26\"," + "\"nested27\" : {"
      + "\"name27\" : \"value27\"," + "\"nested28\" : {"
      + "\"name28\" : \"value28\"," + "\"nested29\" : {"
      + "\"name29\" : \"value29\"," + "\"nested30\" : {"
      + "\"name30\" : \"value30\"" + "}" + "}" + "}" + "}" + "}" + "}" + "}"
      + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}"
      + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "}" + "]";

  public static final String jsonArrayWithMultipleArraysData = "[ \"string\", 100, true, false, null, { \"object\" : \"object\" }, [ \"one\","
      + "\"two\" ], [ 100, 7e7, true, false, null, { \"object2\" : \"object2\" } ] ]";
}

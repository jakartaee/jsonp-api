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

package jakarta.jsonp.tck.api.common;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/*
 * $Id$
 */
/**
 * {@see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>}: JavaScript
 * Object Notation (JSON) Pointer compatibility sample object.<br>
 * Object structure is defined in
 * {@see <a href="https://tools.ietf.org/html/rfc6901#section-5">RFC 6901: 5.
 * JSON String Representation</a>}.
 */
public class PointerRFCObject {

  // Following values define JSON object from
  // RFC 6901: 5. JSON String Representation
  // https://tools.ietf.org/html/rfc6901#section-5
  /** RFC 6901 sample JSON object key for the whole document. */
  public static final String RFC_KEY_WHOLE = "";

  /** RFC 6901 sample JSON object key for 1st value. */
  public static final String RFC_KEY1 = "foo";

  /** RFC 6901 sample JSON object pointer for 1st value. */
  public static final String RFC_PTR1 = "/foo";

  /** RFC 6901 sample JSON object pointer for 1st item of 1st value. */
  public static final String RFC_PTR1_ITEM1 = "/foo/0";

  /** RFC 6901 sample JSON object pointer for 2nd item of 1st value. */
  public static final String RFC_PTR1_ITEM2 = "/foo/1";

  /** RFC 6901 sample JSON object 1st value: array 1st item. */
  public static final String RFC_VAL1_ITEM1 = "bar";

  /** RFC 6901 sample JSON object 1st value: array 2nd item. */
  public static final String RFC_VAL1_ITEM2 = "baz";

  /** RFC 6901 sample JSON object 1st value. */
  public static final JsonArray RFC_VAL1 = SimpleValues
      .createStringArray(RFC_VAL1_ITEM1, RFC_VAL1_ITEM2);

  /** RFC 6901 sample JSON object key for 2nd value. */
  public static final String RFC_KEY2 = "";

  /** RFC 6901 sample JSON object pointer for 2nd value. */
  public static final String RFC_PTR2 = "/";

  /** RFC 6901 sample JSON object 2nd value. */
  public static final int RFC_VAL2 = 0;

  /** RFC 6901 sample JSON object key for 3rd value. */
  public static final String RFC_KEY3 = "a/b";

  /** RFC 6901 sample JSON object pointer for 3rd value. */
  public static final String RFC_PTR3_ENC = "/a~1b";

  /** RFC 6901 sample JSON object pointer for 3rd value. */
  public static final String RFC_PTR3 = "/a/b";

  /** RFC 6901 sample JSON object 3rd value. */
  public static final int RFC_VAL3 = 1;

  /** RFC 6901 sample JSON object key for 4th value. */
  public static final String RFC_KEY4 = "c%d";

  /** RFC 6901 sample JSON object pointer for 4th value. */
  public static final String RFC_PTR4 = "/c%d";

  /** RFC 6901 sample JSON object 4th value. */
  public static final int RFC_VAL4 = 2;

  /** RFC 6901 sample JSON object key for 5th value. */
  public static final String RFC_KEY5 = "e^f";

  /** RFC 6901 sample JSON object pointer for 5th value. */
  public static final String RFC_PTR5 = "/e^f";

  /** RFC 6901 sample JSON object 5th value. */
  public static final int RFC_VAL5 = 3;

  /** RFC 6901 sample JSON object key for 6th value. */
  public static final String RFC_KEY6 = "g|h";

  /** RFC 6901 sample JSON object pointer for 6th value. */
  public static final String RFC_PTR6 = "/g|h";

  /** RFC 6901 sample JSON object 6th value. */
  public static final int RFC_VAL6 = 4;

  /** RFC 6901 sample JSON object key for 7th value. */
  public static final String RFC_KEY7 = "i\\j";

  /** RFC 6901 sample JSON object pointer for 7th value. */
  public static final String RFC_PTR7 = "/i\\j";

  /** RFC 6901 sample JSON object 7th value. */
  public static final int RFC_VAL7 = 5;

  /** RFC 6901 sample JSON object key for 8th value. */
  public static final String RFC_KEY8 = "k\"l";

  /** RFC 6901 sample JSON object pointer for 8th value. */
  public static final String RFC_PTR8 = "/k\"l";

  /** RFC 6901 sample JSON object 8th value. */
  public static final int RFC_VAL8 = 6;

  /** RFC 6901 sample JSON object key for 9th value. */
  public static final String RFC_KEY9 = " ";

  /** RFC 6901 sample JSON object pointer for 9th value. */
  public static final String RFC_PTR9 = "/ ";

  /** RFC 6901 sample JSON object 9th value. */
  public static final int RFC_VAL9 = 7;

  /** RFC 6901 sample JSON object key for 10th value. */
  public static final String RFC_KEY10 = "m~n";

  /** RFC 6901 sample JSON object encoded pointer for 10th value. */
  public static final String RFC_KEY10_ENC = "/m~0n";

  /** RFC 6901 sample JSON object pointer for 10th value. */
  public static final String RFC_PTR10 = "/m~n";

  /** RFC 6901 sample JSON object 10th value. */
  public static final int RFC_VAL10 = 8;

  /** RFC 6901 sample JSON object key for 11th value. */
  public static final String RFC_KEY11 = "o~1p";

  /** RFC 6901 sample JSON object encoded pointer for 11th value. */
  public static final String RFC_PTR11_ENC = "/o~01p";

  /** RFC 6901 sample JSON object pointer for 11th value. */
  public static final String RFC_PTR11 = "/o~1p";

  /** RFC 6901 sample JSON object 11th value. */
  public static final int RFC_VAL11 = 9;

  /**
   * Creates RFC 6901 sample JSON object.
   * {@see <a href="https://tools.ietf.org/html/rfc6901#section-5">RFC 6901: 5.
   * JSON String Representation</a>}
   * 
   * @return RFC 6901 sample JSON object.
   */
  public static JsonObject createRFC6901Object() {
    return Json.createObjectBuilder().add(RFC_KEY1, RFC_VAL1)
        .add(RFC_KEY2, RFC_VAL2).add(RFC_KEY3, RFC_VAL3).add(RFC_KEY4, RFC_VAL4)
        .add(RFC_KEY5, RFC_VAL5).add(RFC_KEY6, RFC_VAL6).add(RFC_KEY7, RFC_VAL7)
        .add(RFC_KEY8, RFC_VAL8).add(RFC_KEY9, RFC_VAL9)
        .add(RFC_KEY10, RFC_VAL10).add(RFC_KEY11, RFC_VAL11).build();
  }

  /**
   * Create an instance of RFC 6901 object class is not allowed.
   */
  private PointerRFCObject() {
  }

}

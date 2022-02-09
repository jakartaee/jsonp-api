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

package ee.jakarta.tck.jsonp.api.common;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

/*
 * $Id$
 */
/**
 * {@see <a href="https://tools.ietf.org/html/rfc7396">RFC 7396</a>}: JavaScript
 * Object Notation (JSON) Merge Patch compatibility sample object.<br>
 * Object structure is defined in
 * {@see <a href="https://tools.ietf.org/html/rfc7396#section-3">RFC 7396: 3.
 * Example</a>}.
 */
public class MergeRFCObject {

  // Following values define JSON object keys from RFC 7396: 3. Example
  /** RFC 7396 sample JSON object key for {@code /title}. */
  public static final String KEY_TITLE = "title";

  /** RFC 7396 sample JSON object key for {@code /author/givenName}. */
  public static final String KEY_GIVEN_NAME = "givenName";

  /** RFC 7396 sample JSON object key for {@code /author/familyName}. */
  public static final String KEY_FAMILY_NAME = "familyName";

  /** RFC 7396 sample JSON object key for {@code /author}. */
  public static final String KEY_AUTHOR = "author";

  /** RFC 7396 sample JSON object key for {@code /tags}. */
  public static final String KEY_TAGS = "tags";

  /** RFC 7396 sample JSON object key for {@code /content}. */
  public static final String KEY_CONTENT = "content";

  /** RFC 7396 sample JSON object key for {@code /phoneNumber}. */
  public static final String KEY_PHONE_NUMBER = "phoneNumber";

  // Following values define JSON object source values from RFC 7396: 3. Example
  /** RFC 7396 sample JSON object source value for {@code /title}. */
  public static final String VAL_SRC_TITLE = "Goodbye!";

  /** RFC 7396 sample JSON object source value for {@code /author/givenName}. */
  public static final String VAL_SRC_GIVEN_NAME = "John";

  /**
   * RFC 7396 sample JSON object source value for {@code /author/familyName}.
   */
  public static final String VAL_SRC_FAMILY_NAME = "Doe";

  /** RFC 7396 sample JSON object source value for {@code /author}. */
  public static final JsonObject VAL_SRC_AUTHOR = SimpleValues
      .createSimpleObject(new String[] { KEY_GIVEN_NAME, KEY_FAMILY_NAME },
          new Object[] { VAL_SRC_GIVEN_NAME, VAL_SRC_FAMILY_NAME });

  /** RFC 7396 sample JSON object source value for {@code /tags/0}. */
  public static final String VAL_SRC_TAGS_0 = "example";

  /** RFC 7396 sample JSON object source value for {@code /tags/1}. */
  public static final String VAL_SRC_TAGS_1 = "sample";

  /** RFC 7396 sample JSON object source value for {@code /tags}. */
  public static final JsonArray VAL_SRC_TAGS = SimpleValues
      .createStringArray(new String[] { VAL_SRC_TAGS_0, VAL_SRC_TAGS_1 });

  /** RFC 7396 sample JSON object source value for {@code /content}. */
  public static final String VAL_SRC_CONTENT = "This will be unchanged";

  // Following values define JSON object target values from RFC 7396: 3. Example
  /** RFC 7396 sample JSON object target value for {@code /title}. */
  public static final String VAL_TRG_TITLE = "Hello!";

  /** RFC 7396 sample JSON object target value for {@code /author}. */
  public static final JsonObject VAL_TRG_AUTHOR = SimpleValues
      .createSimpleObject(new String[] { KEY_GIVEN_NAME },
          new Object[] { VAL_SRC_GIVEN_NAME });

  /** RFC 7396 sample JSON object target value for {@code /phoneNumber}. */
  public static final String VAL_TRG_PHONE_NUMBER = "+01-123-456-7890";

  /** RFC 7396 sample JSON object target value for {@code /tags/0}. */
  public static final String VAL_TRG_TAGS_0 = "example";

  /** RFC 7396 sample JSON object target value for {@code /tags}. */
  public static final JsonArray VAL_TRG_TAGS = SimpleValues
      .createStringArray(new String[] { VAL_TRG_TAGS_0 });

  // Following values define JSON object patch values from RFC 7396: 3. Example
  /** RFC 7396 sample JSON object patch value for {@code /author/familyName}. */
  public static final JsonValue VAL_PATCH_FAMILY_NAME = JsonValue.NULL;

  /** RFC 7396 sample JSON object patch value for {@code /author}. */
  public static final JsonObject VAL_PATCH_AUTHOR = SimpleValues
      .createSimpleObject(new String[] { KEY_FAMILY_NAME },
          new Object[] { VAL_PATCH_FAMILY_NAME });

  /**
   * Create {@see <a href="https://tools.ietf.org/html/rfc7396#section-3">RFC
   * 7396 example</a>} source JSON object.
   * 
   * @return Source object from example.
   */
  public static JsonObject createRFCSourceObject() {
    return Json.createObjectBuilder().add(KEY_TITLE, VAL_SRC_TITLE)
        .add(KEY_AUTHOR, VAL_SRC_AUTHOR).add(KEY_TAGS, VAL_SRC_TAGS)
        .add(KEY_CONTENT, VAL_SRC_CONTENT).build();
  }

  /**
   * Create {@see <a href="https://tools.ietf.org/html/rfc7396#section-3">RFC
   * 7396 example</a>} target JSON object.
   * 
   * @return Target object from example.
   */
  public static JsonObject createRFCTargetObject() {
    return Json.createObjectBuilder().add(KEY_TITLE, VAL_TRG_TITLE)
        .add(KEY_AUTHOR, VAL_TRG_AUTHOR).add(KEY_TAGS, VAL_TRG_TAGS)
        .add(KEY_CONTENT, VAL_SRC_CONTENT)
        .add(KEY_PHONE_NUMBER, VAL_TRG_PHONE_NUMBER).build();
  }

  /**
   * Create {@see <a href="https://tools.ietf.org/html/rfc7396#section-3">RFC
   * 7396 example</a>} patch JSON object.
   * 
   * @return Patch object from example.
   */
  public static JsonObject createRFCPatchObject() {
    return Json.createObjectBuilder().add(KEY_TITLE, VAL_TRG_TITLE)
        .add(KEY_PHONE_NUMBER, VAL_TRG_PHONE_NUMBER)
        .add(KEY_AUTHOR, VAL_PATCH_AUTHOR).add(KEY_TAGS, VAL_TRG_TAGS).build();
  }

}

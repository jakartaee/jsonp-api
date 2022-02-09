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
package ee.jakarta.tck.jsonp.api.jsonparsereventtests;

import ee.jakarta.tck.jsonp.common.JSONP_Util;
import jakarta.json.stream.*;

import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* Tests */

  /*
   * @testName: jsonValueOfTest
   * 
   * @assertion_ids: JSONP:JAVADOC:128;
   * 
   * @test_Strategy: Test JsonParser.Event.valueOf() API method call with all
   * JsonParser.Event types.
   *
   */
  @Test
  public void jsonValueOfTest() {
    boolean pass = true;

    String eventTypeStrings[] = { "END_ARRAY", "END_OBJECT", "KEY_NAME",
        "START_ARRAY", "START_OBJECT", "VALUE_FALSE", "VALUE_NULL",
        "VALUE_NUMBER", "VALUE_STRING", "VALUE_TRUE" };

    for (String eventTypeString : eventTypeStrings) {
      JsonParser.Event eventType;
      try {
        LOGGER.info(
            "Testing enum value for string constant name " + eventTypeString);
        eventType = JsonParser.Event.valueOf(eventTypeString);
        LOGGER.info("Got enum type " + eventType + " for enum string constant named "
            + eventTypeString);
      } catch (Exception e) {
        LOGGER.warning("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    LOGGER.info("Testing negative test case for NullPointerException");
    try {
      JsonParser.Event.valueOf(null);
      LOGGER.warning("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      LOGGER.info("Got expected NullPointerException");
    } catch (Exception e) {
      LOGGER.warning("Got unexpected exception " + e);
      pass = false;
    }

    LOGGER.info("Testing negative test case for IllegalArgumentException");
    try {
      JsonParser.Event.valueOf("INVALID");
      LOGGER.warning("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      LOGGER.info("Got expected IllegalArgumentException");
    } catch (Exception e) {
      LOGGER.warning("Got unexpected exception " + e);
      pass = false;
    }

    assertTrue(pass, "jsonValueOfTest Failed");
  }

  /*
   * @testName: jsonValuesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:129;
   * 
   * @test_Strategy: Test JsonParser.Event.values() API method call and verify
   * enums returned.
   *
   */
  @Test
  public void jsonValuesTest() {

    LOGGER.info(
        "Testing API method JsonParser.Event.values() to return array of enums.");
    JsonParser.Event[] values = JsonParser.Event.values();

    for (JsonParser.Event eventType : values) {
      String eventString = JSONP_Util.getEventTypeString(eventType);
      if (eventString == null) {
        fail("jsonValuesTest Failed. Got no value for enum " + eventType);
      } else
        LOGGER.info("Got " + eventString + " for enum " + eventType);
    }
  }
}

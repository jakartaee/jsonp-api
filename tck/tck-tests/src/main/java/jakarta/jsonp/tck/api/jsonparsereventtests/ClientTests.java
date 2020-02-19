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
package jakarta.jsonp.tck.api.jsonparsereventtests;

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
  public void jsonValueOfTest() throws Fault {
    boolean pass = true;

    String eventTypeStrings[] = { "END_ARRAY", "END_OBJECT", "KEY_NAME",
        "START_ARRAY", "START_OBJECT", "VALUE_FALSE", "VALUE_NULL",
        "VALUE_NUMBER", "VALUE_STRING", "VALUE_TRUE" };

    for (String eventTypeString : eventTypeStrings) {
      JsonParser.Event eventType;
      try {
        System.out.println(
            "Testing enum value for string constant name " + eventTypeString);
        eventType = JsonParser.Event.valueOf(eventTypeString);
        System.out.println("Got enum type " + eventType + " for enum string constant named "
            + eventTypeString);
      } catch (Exception e) {
        System.err.println("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    System.out.println("Testing negative test case for NullPointerException");
    try {
      JsonParser.Event.valueOf(null);
      System.err.println("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      System.out.println("Got expected NullPointerException");
    } catch (Exception e) {
      System.err.println("Got unexpected exception " + e);
      pass = false;
    }

    System.out.println("Testing negative test case for IllegalArgumentException");
    try {
      JsonParser.Event.valueOf("INVALID");
      System.err.println("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      System.out.println("Got expected IllegalArgumentException");
    } catch (Exception e) {
      System.err.println("Got unexpected exception " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("jsonValueOfTest Failed");
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
  public void jsonValuesTest() throws Fault {
    boolean pass = true;

    System.out.println(
        "Testing API method JsonParser.Event.values() to return array of enums.");
    JsonParser.Event[] values = JsonParser.Event.values();

    for (JsonParser.Event eventType : values) {
      String eventString = JSONP_Util.getEventTypeString(eventType);
      if (eventString == null) {
        System.err.println("Got no value for enum " + eventType);
        pass = false;
      } else
        System.out.println("Got " + eventString + " for enum " + eventType);
    }

    if (!pass)
      throw new Fault("jsonValuesTest Failed");
  }
}

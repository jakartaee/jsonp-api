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
package jsonp.tck.api.exceptiontests;

import jakarta.json.*;
import jakarta.json.stream.*;

import jsonp.tck.common.*;
import jsonp.tck.common.JSONP_Util;
import jsonp.tck.common.MyJsonLocation;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());
  
  /* Tests */

  /*
   * @testName: jsonExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:37;
   * 
   * @test_Strategy: Test API: JsonException ret = new JsonException(String)
   */
  @Test
  public void jsonExceptionConstructorTest1() {
    try {
      String message = "This JSON is incorrect.";

      LOGGER.info("Test JsonException(String)");
      JsonException exception = new JsonException(message);
      try {
        throw exception;
      } catch (JsonException e) {
        assertEquals(message, e.getMessage(), "jsonExceptionConstructorTest1 failed");
      }
    } catch (Exception e) {
      fail("jsonExceptionConstructorTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:38;
   * 
   * @test_Strategy: Test API: JsonException ret = new JsonException(String,
   * Throwable)
   */
  @Test
  public void jsonExceptionConstructorTest2() {
    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      LOGGER.info("Test JsonException(String, Throwable)");
      JsonException exception = new JsonException(message, foo);

      try {
        throw exception;
      } catch (JsonException e) {
        assertTrue(isPass(foo, e, message), "jsonExceptionConstructorTest2 failed");
      }

    } catch (Exception e) {
      fail("jsonExceptionConstructorTest2 Failed: ", e);
    }
  }

  /*
   * @testName: jsonParsingExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:285; JSONP:JAVADOC:478; JSONP:JAVADOC:474;
   * JSONP:JAVADOC:475; JSONP:JAVADOC:476;
   * 
   * @test_Strategy: Test API: JsonParsingException ret = new
   * JsonParsingException(String, JsonLocation)
   */
  @Test
  public void jsonParsingExceptionConstructorTest1() {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      LOGGER.info("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      LOGGER.info("Test JsonParsingException(String, JsonLocation)");
      JsonParsingException exception = new JsonParsingException(message,
          expLoc);
      try {
        throw exception;
      } catch (JsonParsingException e) {
        if (!e.getMessage().equals(message)) {
          LOGGER.warning("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
      JsonLocation actLoc = exception.getLocation();
      LOGGER.info("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      fail("jsonParsingExceptionConstructorTest1 Failed: ", e);
    }
    assertTrue(pass, "jsonParsingExceptionConstructorTest1 failed");
  }

  /*
   * @testName: jsonParsingExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:286; JSONP:JAVADOC:478; JSONP:JAVADOC:474;
   * JSONP:JAVADOC:475; JSONP:JAVADOC:476;
   * 
   * @test_Strategy: Test API: JsonParsingException ret = new
   * JsonParsingException(String, Throwable, JsonLocation)
   */
  @Test
  public void jsonParsingExceptionConstructorTest2() {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      LOGGER.info("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      LOGGER.info("Test JsonParsingException(String, Throwable)");
      JsonParsingException exception = new JsonParsingException(message, foo,
          expLoc);

      try {
        throw exception;
      } catch (JsonParsingException e) {
        pass = isPass(foo, e, message);
      }
      JsonLocation actLoc = exception.getLocation();
      LOGGER.info("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      fail("jsonParsingExceptionConstructorTest2 Failed: ", e);
    }
    assertTrue(pass, "jsonParsingExceptionConstructorTest2 failed");
  }

  /*
   * @testName: jsonGenerationExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:278;
   * 
   * @test_Strategy: Test API: JsonGenerationException ret = new
   * JsonGenerationException(String)
   */
  @Test
  public void jsonGenerationExceptionConstructorTest1() {
    try {
      String message = "This JSON is incorrect.";

      LOGGER.info("Test JsonGenerationException(String)");
      JsonGenerationException exception = new JsonGenerationException(message);
      try {
        throw exception;
      } catch (JsonGenerationException e) {
        assertEquals(message, e.getMessage(), "jsonGenerationExceptionConstructorTest1 failed: Incorrect message");
      }
    } catch (Exception e) {
      fail("jsonGenerationExceptionConstructorTest1 Failed: ", e);
    }
  }

  /*
   * @testName: jsonGenerationExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:279;
   * 
   * @test_Strategy: Test API: JsonGenerationException ret = new
   * JsonGenerationException(String, Throwable)
   */
  @Test
  public void jsonGenerationExceptionConstructorTest2() {
    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      LOGGER.info("Test JsonGenerationException(String, Throwable)");
      JsonGenerationException exception = new JsonGenerationException(message,
          foo);

      try {
        throw exception;
      } catch (JsonGenerationException e) {
        assertTrue(isPass(foo, e, message), "jsonGenerationExceptionConstructorTest2 failed");
      }

    } catch (Exception e) {
      fail("jsonGenerationExceptionConstructorTest2 Failed: ", e);
    }
  }

  private boolean isPass(Exception toCompare, Exception actual, String errorMessage) {
    boolean pass = true;
    if (!actual.getCause().equals(toCompare)) {
      LOGGER.warning("Incorrect cause: expected " + toCompare + ", received "
          + actual.getCause());
      pass = false;
    }
    if (!actual.getMessage().equals(errorMessage)) {
      LOGGER.warning("Incorrect message: expected " + errorMessage + ", received "
          + actual.getMessage());
      pass = false;
    }
    return pass;
  }

}

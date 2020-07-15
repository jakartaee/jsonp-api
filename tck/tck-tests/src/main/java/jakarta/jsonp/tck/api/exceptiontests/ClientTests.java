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
package jakarta.jsonp.tck.api.exceptiontests;

import jakarta.json.*;
import jakarta.json.stream.*;

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
   * @testName: jsonExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:37;
   * 
   * @test_Strategy: Test API: JsonException ret = new JsonException(String)
   */
  @Test
  public void jsonExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";

      System.out.println("Test JsonException(String)");
      JsonException exception = new JsonException(message);
      try {
        throw exception;
      } catch (JsonException e) {
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonExceptionConstructorTest1 Failed:");
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
  public void jsonExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      System.out.println("Test JsonException(String, Throwable)");
      JsonException exception = new JsonException(message, foo);

      try {
        throw exception;
      } catch (JsonException e) {
        if (!e.getCause().equals(foo)) {
          System.err.println("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonExceptionConstructorTest2 Failed:");
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
  public void jsonParsingExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      System.out.println("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      System.out.println("Test JsonParsingException(String, JsonLocation)");
      JsonParsingException exception = new JsonParsingException(message,
          expLoc);
      try {
        throw exception;
      } catch (JsonParsingException e) {
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
      JsonLocation actLoc = exception.getLocation();
      System.out.println("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParsingExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonParsingExceptionConstructorTest1 Failed:");
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
  public void jsonParsingExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      System.out.println("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      System.out.println("Test JsonParsingException(String, Throwable)");
      JsonParsingException exception = new JsonParsingException(message, foo,
          expLoc);

      try {
        throw exception;
      } catch (JsonParsingException e) {
        if (!e.getCause().equals(foo)) {
          System.err.println("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
      JsonLocation actLoc = exception.getLocation();
      System.out.println("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParsingExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonParsingExceptionConstructorTest2 Failed:");
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
  public void jsonGenerationExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";

      System.out.println("Test JsonGenerationException(String)");
      JsonGenerationException exception = new JsonGenerationException(message);
      try {
        throw exception;
      } catch (JsonGenerationException e) {
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonGenerationExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonGenerationExceptionConstructorTest1 Failed:");
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
  public void jsonGenerationExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      System.out.println("Test JsonGenerationException(String, Throwable)");
      JsonGenerationException exception = new JsonGenerationException(message,
          foo);

      try {
        throw exception;
      } catch (JsonGenerationException e) {
        if (!e.getCause().equals(foo)) {
          System.err.println("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          System.err.println("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonGenerationExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonGenerationExceptionConstructorTest2 Failed:");
  }
}

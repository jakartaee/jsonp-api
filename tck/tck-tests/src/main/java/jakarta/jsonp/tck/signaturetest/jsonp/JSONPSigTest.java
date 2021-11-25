/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.jsonp.tck.signaturetest.jsonp;

import java.io.PrintWriter;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import jakarta.jsonp.tck.signaturetest.SigTestEE;
import jakarta.jsonp.tck.signaturetest.SignatureTestDriver;
import jakarta.jsonp.tck.signaturetest.SignatureTestDriverFactory;
import jakarta.jsonp.tck.signaturetest.SigTestResult;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Properties;


/*
 * This class is a simple example of a signature test that extends the
 * SigTest framework class.  This signature test is run outside of the
 * Java EE containers.  This class also contains the boilerplate
 * code necessary to create a signature test using the test framework.
 * To see a complete TCK example see the javaee directory for the Java EE
 * TCK signature test class.
 */
public class JSONPSigTest extends SigTestEE {

  public JSONPSigTest(){
    setup();
  }

  /***** Abstract Method Implementation *****/

  /**
   * Returns a list of strings where each string represents a package name. Each
   * package name will have it's signature tested by the signature test
   * framework.
   * 
   * @return String[] The names of the packages whose signatures should be
   *         verified.
   */
  protected String[] getPackages(String vehicleName) {
    return new String[] { "jakarta.json", "jakarta.json.spi",
        "jakarta.json.stream", };

  }

  /***** Boilerplate Code *****/


  /*
   * The following comments are specified in the base class that defines the
   * signature tests. This is done so the test finders will find the right class
   * to run. The implementation of these methods is inherited from the super
   * class which is part of the signature test framework.
   */

  // NOTE: If the API under test is not part of your testing runtime
  // environment, you may use the property sigTestClasspath to specify
  // where the API under test lives. This should almost never be used.
  // Normally the API under test should be specified in the classpath
  // of the VM running the signature tests. Use either the first
  // comment or the one below it depending on which properties your
  // signature tests need. Please do not use both comments.

  /*
   * @class.setup_props: ts_home, The base path of this TCK; sigTestClasspath;
   */
  /*
   * @testName: signatureTest
   * 
   * @assertion: A JSONB container must implement the required classes and APIs
   * specified in the JSONB Specification.
   * 
   * @test_Strategy: Using reflection, gather the implementation specific
   * classes and APIs. Compare these results with the expected (required)
   * classes and APIs.
   *
   */
  @Test
  public void signatureTest() throws Fault {
    System.out.println("$$$ JSONBSigTest.signatureTest() called");
    SigTestResult results = null;
    String mapFile = System.getProperty("signature.mapfile");
    String repositoryDir = System.getProperty("signature.repositoryDir");
    String[] packages = getPackages(testInfo.getVehicle());
    String[] classes = getClasses(testInfo.getVehicle());
    String packageFile = System.getProperty("signature.packagelist");
    String testClasspath = System.getProperty("signature.sigTestClasspath");
    String optionalPkgToIgnore = testInfo.getOptionalTechPackagesToIgnore();

    // unlisted optional packages are technology packages for those optional
    // technologies (e.g. jsr-88) that might not have been specified by the
    // user.
    // We want to ensure there are no full or partial implementations of an
    // optional technology which were not declared
    ArrayList<String> unlistedTechnologyPkgs = getUnlistedOptionalPackages();

    // If testing with Java 9+, extract the JDK's modules so they can be used
    // on the testcase's classpath.
    Properties sysProps = System.getProperties();
    String version = (String) sysProps.get("java.version");
    if (!version.startsWith("1.")) {
      String jimageDir = testInfo.getJImageDir();
      File f = new File(jimageDir);
      f.mkdirs();

      String javaHome = (String) sysProps.get("java.home");
      System.out.println("Executing JImage");

      try {
        ProcessBuilder pb = new ProcessBuilder(javaHome + "/bin/jimage", "extract", "--dir=" + jimageDir, javaHome + "/lib/modules");
        System.out.println(javaHome + "/bin/jimage extract --dir=" + jimageDir + " " + javaHome + "/lib/modules");
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        BufferedReader out = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        while ((line = out.readLine()) != null) {
          System.out.println(line);
        }

        int rc = proc.waitFor();
        System.out.println("JImage RC = " + rc);
        out.close();
      } catch (Exception e) {
        System.out.println("Exception while executing JImage!  Some tests may fail.");
        e.printStackTrace();
      }
    }

    try {
      results = getSigTestDriver().executeSigTest(packageFile, mapFile,
          repositoryDir, packages, classes, testClasspath,
          unlistedTechnologyPkgs, optionalPkgToIgnore);
      System.out.println(results.toString());
      if (!results.passed()) {
        System.out.println("results.passed() returned false");
        throw new Exception();
      }

      // Call verifyJtaJarTest based on some conditions, please check the
      // comment for verifyJtaJarTest.
      if ("standalone".equalsIgnoreCase(testInfo.getVehicle())) {
        Properties mapFileAsProps = getSigTestDriver().loadMapFile(mapFile);
        if (mapFileAsProps == null || mapFileAsProps.size() == 0) {
          // empty signature file, something unusual
          System.out.println("JSONBSigTest.signatureTest() returning, " +
              "as signature map file is empty.");
          return;
        }

        boolean isJTASigTest = false;

        // Determine whether the signature map file contains package 
        // jakarta.transaction
        String jtaVersion = mapFileAsProps.getProperty("jakarta.transaction");
        if (jtaVersion == null || "".equals(jtaVersion.trim())) {
          System.out.println("JSONBSigTest.signatureTest() returning, " +
              "as this is neither JTA TCK run, not Java EE CTS run.");
          return;
        }

        System.out.println("jtaVersion " + jtaVersion);  
        // Signature map packaged in JTA TCK will contain a single package 
        // jakarta.transaction
        if (mapFileAsProps.size() == 1) {
            isJTASigTest = true;
        }

        if (isJTASigTest || !jtaVersion.startsWith("1.2")) {
          verifyJtaJarTest();
        }
      }
      System.out.println("$$$ JSONBSigTest.signatureTest() returning");
    } catch (Exception e) {
      if (results != null && !results.passed()) {
        throw new Fault("JSONBSigTest.signatureTest() failed!, diffs found");
      } else {
        System.out.println("Unexpected exception " + e.getMessage());
        throw new Fault("signatureTest failed with an unexpected exception", e);
      }
    }
  }


} // end class JSONBSigTest

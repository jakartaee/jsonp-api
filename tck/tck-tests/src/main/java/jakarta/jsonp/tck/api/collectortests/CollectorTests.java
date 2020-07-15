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

package jakarta.jsonp.tck.api.collectortests;

import jakarta.jsonp.tck.api.common.TestResult;
import jakarta.jsonp.tck.lib.harness.Fault;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

// $Id$
/**
 * JavaScript Object Notation (JSON) Pointer compatibility tests.<br>
 * Test {@link jakarta.json.stream.JsonCollectors} class implementation.
 */
@RunWith(Arquillian.class)
public class CollectorTests {
    
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, CollectorTests.class.getPackage().getName());
    }

  /**
   * Test JSON-P {@link jakarta.json.stream.JsonCollectors} class implementation.
   * 
   * @throws Fault
   *           when this test failed.
   * 
   * @testName: jsonCollectorTest
   * @assertion_ids: JSONP:JAVADOC:668; JSONP:JAVADOC:669; JSONP:JAVADOC:670;
   *                 JSONP:JAVADOC:671;
   * @test_Strategy: Test all collectors returned by API.
   */
  @Test
  public void jsonCollectorTest() throws Fault {
    Collectors collectorTest = new Collectors();
    final TestResult result = collectorTest.test();
    result.eval();
  }
}

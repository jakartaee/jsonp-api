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
package jakarta.jsonp.tck.api.jsoncoding;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.json.Json;
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
   * @testName: jsonEncodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:681; JSONP:JAVADOC:682;
   * 
   * @test_Strategy: Encode and decode Json Pointer as defined by RFC 6901
   */
  @Test
  public void jsonEncodeTest() throws Fault {
    String DECODED = "/a/~b/c";
    String ENCODED = "~1a~1~0b~1c";
    StringBuilder error = new StringBuilder();
    System.out.println("----------------------------------------------");
    System.out.println("Test encode " + DECODED);
    System.out.println("----------------------------------------------");
    String encoded = Json.encodePointer(DECODED);
    if (!ENCODED.equals(encoded))
      error.append("The pointer ").append(DECODED)
          .append(" has been encoded as ").append(encoded).append('\n');

    System.out.println("----------------------------------------------");
    System.out.println("Test decode " + ENCODED);
    String decoded = Json.decodePointer(ENCODED);
    if (!DECODED.equals(decoded))
      error.append("The pointer ").append(ENCODED)
          .append(" has been decoded as ").append(decoded).append('\n');
    if (error.length() != 0)
      throw new Fault(error.toString());
    System.out.println("----------------------------------------------");
  }
}

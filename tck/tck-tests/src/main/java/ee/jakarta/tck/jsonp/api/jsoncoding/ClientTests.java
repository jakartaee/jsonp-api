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
package ee.jakarta.tck.jsonp.api.jsoncoding;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTests {

  private static final Logger LOGGER = Logger.getLogger(ClientTests.class.getName());

  /* Tests */

  /*
   * @testName: jsonEncodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:681; JSONP:JAVADOC:682;
   * 
   * @test_Strategy: Encode and decode Json Pointer as defined by RFC 6901
   */
  @Test
  public void jsonEncodeTest() {
    String DECODED = "/a/~b/c";
    String ENCODED = "~1a~1~0b~1c";
    StringBuilder error = new StringBuilder();
    LOGGER.info("----------------------------------------------");
    LOGGER.info("Test encode " + DECODED);
    LOGGER.info("----------------------------------------------");
    String encoded = Json.encodePointer(DECODED);
    if (!ENCODED.equals(encoded))
      error.append("The pointer ").append(DECODED)
          .append(" has been encoded as ").append(encoded).append('\n');

    LOGGER.info("----------------------------------------------");
    LOGGER.info("Test decode " + ENCODED);
    String decoded = Json.decodePointer(ENCODED);
    if (!DECODED.equals(decoded))
      error.append("The pointer ").append(ENCODED)
          .append(" has been decoded as ").append(decoded).append('\n');
    assertEquals(0, error.length(), error.toString());
    LOGGER.info("----------------------------------------------");
  }
}

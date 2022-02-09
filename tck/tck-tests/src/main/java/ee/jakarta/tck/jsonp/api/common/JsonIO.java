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

import java.io.StringReader;
import jakarta.json.Json;
import jakarta.json.JsonValue;

// $Id$
/**
 * Read and write JSON values.
 */
public class JsonIO {
  /**
   * Reads JSON value from {@code String}.
   * 
   * @param json
   *          JSON value to be read.
   * @return JSON value from provided {@code String}.
   */
  public static JsonValue read(final String json) {
    return Json.createReader(new StringReader(json)).readValue();
  }

}

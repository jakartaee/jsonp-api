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

/*
 * Usage: java StringUTFConverter inputstring encoding outputfile
 *
 * Utility program to convert an input string to a UTF encoded output file.
 *
 * Example(s):
 *
 * java StringUTFConverter "foo" UTF-16LE fooUTF16LE
 *
 * The above converts an input string "foo" and outputs to UTF-16LE encoded
 * output file (fooUTF16LE).
 */

package ee.jakarta.tck.jsonp.util;

import java.io.*;
import java.util.logging.Logger;

public class StringUTFConverter {

  private static final Logger LOGGER = Logger.getLogger(StringUTFConverter.class.getName());

  private static final String USAGE = "Usage : java StringUTFConverter inputstring encoding outputfile";

  public static void main(String args[]) {
    try {
      if (args.length != 3) {
        LOGGER.warning(USAGE);
        System.exit(1);
      }

      // Convert string to specified UTF encoded output file
      LOGGER.info(
          "StringtoUTF-> convert string (" + args[0] + "), to encoding ("
              + args[1] + ") and write to output file (" + args[2] + ")");
      FileOutputStream fos = new FileOutputStream(args[2]);
      BufferedWriter bw = new BufferedWriter(
          new OutputStreamWriter(fos, args[1]));
      bw.write(args[0]);
      bw.flush();
      bw.close();

      System.exit(0);

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}

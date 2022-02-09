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
 * Usage: java FileUTFConverter [-toUTF|-fromUTF] encoding infile outfile
 *
 * Utility program for converting a UTF-8 file to various UTF
 * encoded files and vice-versa.
 *
 * Example(s):
 *
 * java FileUTFConverter -toUTF UTF-16 jsonObjectUTF8.json jsonObjectUTF16.json
 *
 * The above takes a UTF-8 encoded input file (jsonObjectUTF8.json) and converts
 * it to a UTF-16 encoded output file (jsonObjectUTF16.json).
 *
 * java FileUTFConverter -fromUTF UTF-16 jsonObjectUTF16.json jsonObjectUTF8.json
 *
 * The above takes a UTF-16 encoded input file (jsonObjectUTF16.json) and
 * converts it to a UTF-8 encoded output file (jsonObjectUTF8.json).
 *
 * All UTF encodings can be used:
 *
 * UTF-8
 * UTF-16
 * UTF-16BE
 * UTF-16LE
 * UTF-32BE
 * UTF-32LE
 */

package ee.jakarta.tck.jsonp.util;

import java.io.*;
import java.util.logging.Logger;

public class FileUTFConverter {

  private static final Logger LOGGER = Logger.getLogger(FileUTFConverter.class.getName());

  private static final String USAGE = "Usage : java FileUTFConverter [-toUTF|-fromUTF] encoding infile outfile";

  public static void main(String args[]) {
    try {
      if (args.length != 4) {
        LOGGER.warning(USAGE);
        System.exit(1);
      }

      // Convert UTF-8 input file to specified UTF encoded output file
      if (args[0].equals("-toUTF")) {
        LOGGER.info("FileUTFConverter-> convert UTF-8 encoded input file ("
                + args[2] + "), to encoding (" + args[1]
                + ") and write to output file (" + args[3] + ")");
        FileInputStream fis = new FileInputStream(args[2]);
        BufferedReader br = new BufferedReader(
            new InputStreamReader(fis, "UTF-8"));
        FileOutputStream fos = new FileOutputStream(args[3]);
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(fos, args[1]));
        for (String s = ""; (s = br.readLine()) != null;) {
          bw.write(s + System.getProperty("line.separator"));
          bw.flush();
        }
        bw.close();
        br.close();
        // Convert specified UTF encoded input file to UTF-8 encoded output file
      } else if (args[0].equals("-fromUTF")) {
        LOGGER.info("FileUTFConverter-> convert UTF encoded input file ("
            + args[2] + "), from encoding (" + args[1]
            + ") and write to UTF-8 encoded output file (" + args[3] + ")");
        FileInputStream fis = new FileInputStream(args[2]);
        BufferedReader br = new BufferedReader(
            new InputStreamReader(fis, args[1]));
        FileOutputStream fos = new FileOutputStream(args[3]);
        BufferedWriter bw = new BufferedWriter(
            new OutputStreamWriter(fos, "UTF-8"));
        for (String s = ""; (s = br.readLine()) != null;) {
          bw.write(s + System.getProperty("line.separator"));
          bw.flush();
        }
        bw.close();
        br.close();
      } else {
        LOGGER.warning(USAGE);
        System.exit(1);
      }

      System.exit(0);

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}

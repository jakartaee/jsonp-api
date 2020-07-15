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

package jakarta.jsonp.tck.util;

import java.io.*;

/*
 * Used to generate an encoded file preceded by N null characters.
 *
 * This utility takes a valid Json text file as input and generates
 * an encoded version as output file with N null characters preceded by
 * each character. For empty files it will just output the N null chars.
 *
 */
public class MyEncoder {

  private static final int NULL = '\0';

  private static final String USAGE = "Usage : java MyEncoder #nulls infile outfile";

  public static void main(String args[]) {
    if (args.length != 3) {
      System.err.println(USAGE);
      System.exit(1);
    }

    FileReader inputStream = null;
    FileWriter outputStream = null;

    try {
      int n = Integer.parseInt(args[0]);
      inputStream = new FileReader(args[1]);
      outputStream = new FileWriter(args[2]);

      System.out.println("Null  chars: " + args[0]);
      System.out.println("Input  file: " + args[1]);
      System.out.println("Output file: " + args[2]);

      System.out
          .println("\nCreating an encoded file with each char preceded by " + n
              + " null chars.\n");

      int c;
      int nchars = 0;
      while ((c = inputStream.read()) != -1) {
        nchars++;
        for (int i = 0; i < n; i++)
          outputStream.write(NULL);
        outputStream.write(c);
      }
      if (nchars == 0) {
        for (int i = 0; i < n; i++)
          outputStream.write(NULL); // if empty file at least write the nulls
                                    // out
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (Exception e) {
        }
      }
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (Exception e) {
        }
      }
    }
    System.exit(0);
  }
}

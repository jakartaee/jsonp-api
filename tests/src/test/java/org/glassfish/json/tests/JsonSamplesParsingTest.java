/*
 * Copyright (c) 2013, 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json.tests;

import junit.framework.TestCase;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.stream.JsonParser;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * JsonParser tests for sample files
 *
 * @author Jitendra Kotamraju
 */
public class JsonSamplesParsingTest extends TestCase {

    public void testSampleFiles() {
        String[] fileNames = {
                "facebook.json", "facebook1.json", "facebook2.json",
                "twitter.json"
        };
        for(String fileName: fileNames) {
            try {
                testSampleFile(fileName);
            } catch(Exception e) {
                throw new JsonException("Exception while parsing "+fileName, e);
            }
        }
    }

    private void testSampleFile(String fileName) {
        Reader reader = new InputStreamReader(
                JsonSamplesParsingTest.class.getResourceAsStream("/"+fileName), StandardCharsets.UTF_8);
        JsonParser parser = null;
        try {
            parser = Json.createParser(reader);
            while(parser.hasNext()) {
                parser.next();
            }
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

}

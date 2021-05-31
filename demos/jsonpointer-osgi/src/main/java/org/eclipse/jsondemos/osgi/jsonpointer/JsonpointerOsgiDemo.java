/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsondemos.osgi.jsonpointer;

import java.io.IOException;
import java.io.InputStream;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * JsonPointer demo with object model API to run inside an OSGI container
 */
public class JsonpointerOsgiDemo implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        testWiki();
        testPointer();
        System.out.println("Both tests PASSED !!!");
        registerService(context);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("OSGI bundle stopped");
    }

    private void registerService(BundleContext context) {
        context.registerService(JsonPointerService.class, new JsonPointerServiceImpl(), null);
    }

    private void testWiki() throws IOException {
        try (
                InputStream is = JsonpointerOsgiDemo.class.getResourceAsStream("/wiki.json");
                JsonReader rdr = Json.createReader(is)
        ) {
            JsonObject person = rdr.readObject();

            assertEquals("NY", getString(person, "/address/state"));
            assertEquals("212 555-1234", getString(person, "/phoneNumber/0/number"));
        }
    }

    private void testPointer() throws IOException {
        try (InputStream is = JsonpointerOsgiDemo.class.getResourceAsStream("/jsonpointer.json");
             JsonReader rdr = Json.createReader(is)) {
            JsonObject root = rdr.readObject();

            assertEquals(root, get(root, ""));
            assertEquals(root.get("foo"), get(root, "/foo"));
            assertEquals(root.getJsonArray("foo").get(0), get(root, "/foo/0"));
            assertEquals(root.get(""), get(root, "/"));
            assertEquals(root.get("a/b"), get(root, "/a~1b"));
            assertEquals(root.get("c%d"), get(root, "/c%d"));
            assertEquals(root.get("e^f"), get(root, "/e^f"));
            assertEquals(root.get("k\"l"), get(root, "/k\"l"));
            assertEquals(root.get("i\\j"), get(root, "/i\\j"));
            assertEquals(root.get(" "), get(root, "/ "));
            assertEquals(root.get("m~n"), get(root, "/m~0n"));

            // Adding a parent to current root and try with it
            JsonObject doc = Json.createObjectBuilder().add("doc", root).build();
            root = doc.getJsonObject("doc");
            assertEquals(doc, get(doc, ""));
            assertEquals(root.get("foo"), get(doc, "/doc/foo"));
            assertEquals(root.getJsonArray("foo").get(0), get(doc, "/doc/foo/0"));
            assertEquals(root.get(""), get(doc, "/doc/"));
            assertEquals(root.get("a/b"), get(doc, "/doc/a~1b"));
            assertEquals(root.get("c%d"), get(doc, "/doc/c%d"));
            assertEquals(root.get("e^f"), get(doc, "/doc/e^f"));
            assertEquals(root.get("k\"l"), get(doc, "/doc/k\"l"));
            assertEquals(root.get("i\\j"), get(doc, "/doc/i\\j"));
            assertEquals(root.get(" "), get(doc, "/doc/ "));
            assertEquals(root.get("m~n"), get(doc, "/doc/m~0n"));
        }
    }

    private String getString(JsonValue root, String pointer) {
        return ((JsonString) get(root, pointer)).getString();
    }

    private JsonValue get(JsonValue root, String pointer) {
        if (pointer.isEmpty()) {
            return root;
        }
        if (pointer.charAt(0) != '/') {
            throw new IllegalArgumentException(
                    "JsonPointer " + pointer + " doesn't start with /");
        }

        StringBuilder referenceToken = new StringBuilder();
        for (int i = 1; i < pointer.length(); i++) {   // 1 to skip first /
            char ch = pointer.charAt(i);
            if (ch == '/') {
                return get(newRoot(root, referenceToken.toString()), pointer.substring(i));
            } else if (ch == '~') {
                // handle escaping ~0, ~1
                if (i + 1 == pointer.length()) {
                    throw new IllegalArgumentException("Illegal escaping: expected ~0 or ~1, but got only ~ in pointer=" + pointer);
                }
                ch = pointer.charAt(++i);
                if (ch == '0') {
                    referenceToken.append('~');
                } else if (ch == '1') {
                    referenceToken.append('/');
                } else {
                    throw new IllegalArgumentException("Illegal escaping: expected ~0 or ~1, but got ~" + ch + " in pointer=" + pointer);
                }
            } else {
                referenceToken.append(ch);
            }
        }
        return newRoot(root, referenceToken.toString());
    }

    private JsonValue newRoot(JsonValue root, String referenceToken) {
        if (root instanceof JsonObject) {
            return ((JsonObject) root).get(referenceToken);
        } else if (root instanceof JsonArray) {
            return ((JsonArray) root).get(Integer.parseInt(referenceToken));
        }
        throw new IllegalArgumentException("Illegal reference token=" + referenceToken + " for value=" + root);
    }

    private void assertEquals(JsonValue exp, JsonValue got) {
        if (exp != got) {
            throw new RuntimeException("Expected = " + exp + " but got = " + got);
        }
    }

    private void assertEquals(String exp, String got) {
        if (!exp.equals(got)) {
            throw new RuntimeException("Expected = " + exp + " but got = " + got);
        }
    }
}

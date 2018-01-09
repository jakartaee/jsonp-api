/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.jsondemos.servlet;

import javax.json.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Writes a JsonArray using HttpServletResponse#getWriter
 * http://localhost:8080/jsondemos-servlet/array
 *
 * Writes a JsonArray using HttpServletResponse#getOutputStream
 * http://localhost:8080/jsondemos-servlet/array?stream
 *
 *
 * @author Jitendra Kotamraju
 */
@WebServlet("/array")
public class ArrayServlet extends HttpServlet {
    private static final JsonBuilderFactory bf = Json.createBuilderFactory(null);
    private static final JsonWriterFactory wf = Json.createWriterFactory(null);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        JsonArray array = bf.createArrayBuilder()
                .add(bf.createObjectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(bf.createObjectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567"))
                .build();
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String q = req.getQueryString();
        boolean isStream = q != null && q.equals("stream");
        JsonWriter writer = isStream
                ? wf.createWriter(res.getOutputStream())
                : wf.createWriter(res.getWriter());
        writer.write(array);
        // not closing writer intentionally
    }

}

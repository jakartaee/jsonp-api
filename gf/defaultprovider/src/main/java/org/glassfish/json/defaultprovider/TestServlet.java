/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json.defaultprovider;

import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import jakarta.json.*;
import jakarta.json.stream.*;
import java.io.*;

/**
 * @author Jitendra Kotamraju
 */
@WebServlet("/json")
public class TestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {
            res.setStatus(200);
            res.setContentType("application/json");
            OutputStream os = res.getOutputStream();
            JsonGenerator generator = Json.createGenerator(os);
            generator.writeStartArray().writeEnd();
            generator.close();
        } catch(IOException ioe) {
            throw new ServletException(ioe);
        }
    }

}

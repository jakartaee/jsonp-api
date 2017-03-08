/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
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

package org.glassfish.jsondemos.twitter;

import javax.servlet.annotation.*;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import javax.json.*;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.json.stream.*;
import javax.json.stream.JsonParser.Event;

/**
 * @author Jitendra Kotamraju
 */
@WebServlet("/json")
public class TwitterServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {
			res.setStatus(200);
			res.setContentType("text/plain; charset=UTF-8");
            writeTwitterFeed(res.getOutputStream());
        } catch(IOException ioe) {
            throw new ServletException(ioe);
        }
    }

    private void writeTwitterFeed(OutputStream os) throws IOException {
        URL url = new URL("http://search.twitter.com/search.json?q=%23javaone");
        try(InputStream is = url.openStream();
            JsonParser parser = Json.createParser(is);
            PrintWriter ps = new PrintWriter(new OutputStreamWriter(os, "UTF-8"))) {

            Iterator<Event> it = parser.iterator();
            while(it.hasNext()) {
                Event e = it.next();
                if (e == Event.KEY_NAME) {
                    if (parser.getString().equals("from_user")) {
                        e = it.next();
                        ps.print(parser.getString());
                        ps.print(": ");
                    } else if (parser.getString().equals("text")) {
                        e = it.next();
                        ps.println(parser.getString());
                        ps.println("---------");
                    }
                }
            }
        }
	}

}

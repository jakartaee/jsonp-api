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
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
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

package org.glassfish.jsondemos.jaxrs;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.URL;

/**
 * Filters JSON from flicker photo search REST API
 *
 * {
 *    photos : {
 *        photo: [
 *           { id: "9889087315", secret: "40aeb70c83", server: "3818",farm: 4, ..},
 *           { id: "9889087315", secret: "40aeb70c83", server: "3818",farm: 4, ..}
 *           ...
 *        ],
 *        ...
 *    }
 * }
 *
 * @author Jitendra Kotamraju
 */
@Path("/parser")
public class ParserResource {

    @GET
    @Produces("text/html")
    public StreamingOutput doGet() {
        return new StreamingOutput() {
            public void write(OutputStream os) throws IOException {
                writeFlickerFeed(os);
            }
        };
    }

    private void writeFlickerFeed(OutputStream os) throws IOException {
        URL url = new URL("http://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=221160312e1c22ec60ecf336951b0e77&format=json&nojsoncallback=1&per_page=20");
        try(InputStream is = url.openStream();
            JsonParser parser = Json.createParser(is);
            PrintWriter ps = new PrintWriter(new OutputStreamWriter(os, "UTF-8"))) {
            String id = null;
            String server = null;
            String secret = null;

            ps.println("<html><body>");
            while(parser.hasNext()) {
                Event e = parser.next();
                if (e == Event.KEY_NAME) {
                    String str = parser.getString();
                    switch (str) {
                        case "id" :
                            parser.next();
                            id = parser.getString();
                            break;
                        case "farm" :
                            parser.next();
                            String farm = parser.getString();
                            ps.println("<img src=\"http://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+".jpg\">");
                            break;
                        case "server" :
                            parser.next();
                            server = parser.getString();
                            break;
                        case "secret" :
                            parser.next();
                            secret = parser.getString();
                            break;
                    }
                }
            }
            ps.println("</body></html>");
            ps.flush();
        }
	}

}

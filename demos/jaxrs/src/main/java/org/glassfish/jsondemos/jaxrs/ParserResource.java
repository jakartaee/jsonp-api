/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
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

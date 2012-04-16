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

package javax.json;

import java.io.Closeable;
import java.io.Reader;
import java.io.StringReader;

/**
 * A JSON reader.
 *
 * <p>
 * This reader reads a JSON object or array from the stream. For example:
 *
 * <code>
 * <pre>
 * An empty JSON array can be created as follows:
 *
 * JsonReader jsonReader = new JsonReader(new StringReader("[]"));
 * JsonValue value = jsonReader.readObject();
 * jsonReader.close();
 * </pre>
 * </code>
 *
 * @author Jitendra Kotamraju
 */
public class JsonReader implements /*Auto*/Closeable {

    /**
     * Creates a JSON reader from a character stream
     *
     * @param reader a reader from which JSON is to be read
     * @return a JSON reader
     */
    public JsonReader(Reader reader) {
    }

    /**
     * Returns a JSON array or object that is represented in
     * the character stream. This method needs to be called
     * only once for a reader instance.
     *
     * @return a {@link JsonArray} or {@code JsonObject}
     * @throws JsonException if a JsonObject or JsonArray cannot
     *     be created due to i/o error or incorrect representation
     * @throws IllegalStateException if this method or close method is
     *     already called
     */
    public JsonValue readObject() {
        return null;
    }

    /**
     * Closes this reader and frees any resources associated with the
     * reader. This doesn't close the underlying input source.
     */
    @Override
    public void close() {
    }

    private void test() {
        JsonReader jsonReader = new JsonReader(new StringReader("[]"));
        JsonValue value = jsonReader.readObject();
        jsonReader.close();
    }

    private void test1() {
        JsonReader jsonReader = new JsonReader(new StringReader("{}"));
        JsonValue value = jsonReader.readObject();
        jsonReader.close();
    }

}
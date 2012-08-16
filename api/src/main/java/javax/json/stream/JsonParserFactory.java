/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2011-2012 Oracle and/or its affiliates. All rights reserved.
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

package javax.json.stream;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.InputStream;
import java.io.Reader;

/**
 * Factory to create {@link JsonParser} instances. If a factory
 * instance is configured with some configuration, that would be
 * used to configure the created parser instances.
 *
 * <p>
 * {@link JsonParser} can also be created using {@link javax.json.Json Json}'s
 * {@code createParser} methods. If multiple parser instances are created,
 * then creating them using a parser factory is preferred.
 *
 * <p>
 * <b>For example:</b>,
 * <code>
 * <pre>
 * JsonParserFactory factory = Json.createParserFactory();
 * JsonParser parser1 = factory.createParser(...);
 * JsonParser parser2 = factory.createParser(...);
 * </pre>
 * </code>
 *
 * <p> All of the methods in this class are safe for use by multiple concurrent
 * threads.</p>
 *
 * @author Jitendra Kotamraju
 */
public interface JsonParserFactory {

    /**
     * Creates a JSON parser from a character stream
     *
     * @param reader a i/o reader from which JSON is to be read
     */
    public JsonParser createParser(Reader reader);

    /**
     * Creates a JSON parser from the specified byte stream.
     * The character encoding of the stream is determined
     * as per the <a href="http://tools.ietf.org/rfc/rfc4627.txt">RFC</a>.
     *
     * @param in i/o stream from which JSON is to be read
     * @throws javax.json.JsonException if encoding cannot be determined
     *         or i/o error
     */
    public JsonParser createParser(InputStream in);

    /**
     * Creates a JSON parser from the specified byte stream.
     * The bytes of the stream are decoded to characters using the
     * specified encoding.
     *
     * @param in i/o stream from which JSON is to be read
     * @param encoding the name of character
     * {@link java.nio.charset.Charset </code>encoding<code>} of the stream.
     * @throws javax.json.JsonException if the named encoding is not supported.
     * The cause of the exception would be
     * {@link java.io.UnsupportedEncodingException UnsupportedEncodingException}
     *
     * @see java.nio.charset.Charset
     */
    public JsonParser createParser(InputStream in, String encoding);

    /**
     * Creates a JSON parser from the specified JSON object.
     *
     * @param obj JSON object
     */
    public JsonParser createParser(JsonObject obj);

    /**
     * Creates a JSON parser from the specified JSON array.
     *
     * @param array JSON array
     */
    public JsonParser createParser(JsonArray array);

}
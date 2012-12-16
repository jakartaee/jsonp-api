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

package org.glassfish.json;

import javax.json.JsonException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * A filter stream that detects the unicode for the original
 * stream
 *
 * @author Jitendra Kotamraju
 */
class UnicodeDetectingInputStream extends FilterInputStream {
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final Charset UTF_16BE = Charset.forName("UTF-16BE");
    private static final Charset UTF_16LE = Charset.forName("UTF-16LE");
    //private static final Charset UTF_16 = Charset.forName("UTF-16");
    private static final Charset UTF_32LE = Charset.forName("UTF-32LE");
    private static final Charset UTF_32BE = Charset.forName("UTF-32BE");

    private final byte[] buf = new byte[4];
    private int bufLen;
    private int curIndex;
    private final Charset charset;

    UnicodeDetectingInputStream(InputStream is) {
        super(is);
        charset = detectEncoding();
    }

    Charset getCharset() {
        return charset;
    }

    private void fillBuf() {
        int b1;
        int b2;
        int b3;
        int b4;

        try {
            b1 = in.read();
            if (b1 == -1) {
                return;
            }

            b2 = in.read();
            if (b2 == -1) {
                bufLen = 1;
                buf[0] = (byte)b1;
                return;
            }

            b3 = in.read();
            if (b3 == -1) {
                bufLen = 2;
                buf[0] = (byte)b1;
                buf[1] = (byte)b2;
                return;
            }

            b4 = in.read();
            if (b4 == -1) {
                bufLen = 3;
                buf[0] = (byte)b1;
                buf[1] = (byte)b2;
                buf[2] = (byte)b3;
                return;
            }
            bufLen = 4;
            buf[0] = (byte)b1;
            buf[1] = (byte)b2;
            buf[2] = (byte)b3;
            buf[3] = (byte)b4;
        } catch (IOException ioe) {
            throw new JsonException(ioe);
        }
    }

    private Charset detectEncoding() {
        fillBuf();
        if (bufLen < 2) {
            throw new JsonException("Cannot detect encoding, not enough chars");
        } else if (bufLen == 4) {
            if (buf[0] == 0 && buf[1] == 0 && buf[2] == 0) {
                return UTF_32BE;
            } else if (buf[0] == 0 && buf[2] == 0) {
                return UTF_16BE;
            } else if (buf[1] == 0 && buf[2] == 0 && buf[3] == 0) {
                return UTF_32LE;
            } else if (buf[1] == 0 && buf[3] == 0) {
                return UTF_16LE;
            }
        }
        return UTF_8;
    }

    @Override
    public int read() throws IOException {
        if (curIndex < bufLen) {
            return buf[curIndex++];
        }
        return in.read();
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        if (curIndex < bufLen) {
            if (len == 0) {
                return 0;
            }
            if (off < 0 || len < 0 || len > b.length -off) {
                throw new IndexOutOfBoundsException();
            }
            int min = Math.min(bufLen-curIndex, len);
            System.arraycopy(buf, curIndex, b, off, min);
            curIndex += min;
            return min;
        }
        return in.read(b, off, len);
    }

}

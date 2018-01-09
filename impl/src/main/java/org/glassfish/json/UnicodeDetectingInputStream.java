/*
 * Copyright (c) 2012, 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.json;

import javax.json.JsonException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A filter stream that detects the unicode encoding for the original
 * stream
 *
 * @author Jitendra Kotamraju
 */
class UnicodeDetectingInputStream extends FilterInputStream {

    private static final Charset UTF_32LE = Charset.forName("UTF-32LE");
    private static final Charset UTF_32BE = Charset.forName("UTF-32BE");

    private static final byte FF = (byte)0xFF;
    private static final byte FE = (byte)0xFE;
    private static final byte EF = (byte)0xEF;
    private static final byte BB = (byte)0xBB;
    private static final byte BF = (byte)0xBF;
    private static final byte NUL = (byte)0x00;

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
            throw new JsonException(JsonMessages.PARSER_INPUT_ENC_DETECT_IOERR(), ioe);
        }
    }

    private Charset detectEncoding() {
        fillBuf();
        if (bufLen < 2) {
            throw new JsonException(JsonMessages.PARSER_INPUT_ENC_DETECT_FAILED());
        } else if (bufLen == 4) {
            // Use BOM to detect encoding
            if (buf[0] == NUL && buf[1] == NUL && buf[2] == FE && buf[3] == FF) {
                curIndex = 4;
                return UTF_32BE;
            } else if (buf[0] == FF && buf[1] == FE && buf[2] == NUL && buf[3] == NUL) {
                curIndex = 4;
                return UTF_32LE;
            } else if (buf[0] == FE && buf[1] == FF) {
                curIndex = 2;
                return StandardCharsets.UTF_16BE;
            } else if (buf[0] == FF && buf[1] == FE) {
                curIndex = 2;
                return StandardCharsets.UTF_16LE;
            } else if (buf[0] == EF && buf[1] == BB && buf[2] == BF) {
                curIndex = 3;
                return StandardCharsets.UTF_8;
            }
            // No BOM, just use JSON RFC's encoding algo to auto-detect
            if (buf[0] == NUL && buf[1] == NUL && buf[2] == NUL) {
                return UTF_32BE;
            } else if (buf[0] == NUL && buf[2] == NUL) {
                return StandardCharsets.UTF_16BE;
            } else if (buf[1] == NUL && buf[2] == NUL && buf[3] == NUL) {
                return UTF_32LE;
            } else if (buf[1] == NUL && buf[3] == NUL) {
                return StandardCharsets.UTF_16LE;
            }
        }
        return StandardCharsets.UTF_8;
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

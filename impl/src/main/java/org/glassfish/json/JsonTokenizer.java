/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
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
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParsingException;
import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * JSON Tokenizer
 *
 * @author Jitendra Kotamraju
 */
final class JsonTokenizer implements Closeable {

    private boolean unread;
    private int prevChar;
    private long lineNo = 1;
    private long columnNo = 1;
    private long streamOffset = 0;

    private final Reader reader;

    private char[] buf = new char[4096];
    // XXXssssssssssssXXXXXXXXXXXXXXXXXXXXXXrrrrrrrrrrrrrrXXXXXX
    //    ^           ^                     ^             ^
    //    |           |                     |             |
    //   storeBegin  storeEnd            readBegin      readEnd
    private int readBegin;
    private int readEnd;
    private int storeBegin;
    private int storeEnd;
    private boolean minus;
    private boolean fracOrExp;

    private String value;
    private BigDecimal bd;

    enum JsonToken {
        CURLYOPEN,  SQUAREOPEN,
        COLON, COMMA,
        STRING, NUMBER, TRUE, FALSE, NULL,
        CURLYCLOSE, SQUARECLOSE,
        EOF
    }

    JsonTokenizer(Reader reader) {
        this.reader = reader;
    }

    private int read() {
        int ch;
        if (this.unread) {
            this.unread = false;
            ch = this.prevChar;
        } else {
            ch = readChar();
        }

        if (ch == '\r' || (prevChar != '\r' && ch == '\n')) {
            ++lineNo;
            columnNo = 1;
        } else if (prevChar != '\r' || ch != '\n') {
            ++columnNo;
        }
        ++streamOffset;
        prevChar = ch;
        return ch;
    }

    private void unread(int ch) {
        prevChar = ch;
        unread = true;
        --streamOffset;
        --columnNo;
    }

    private void readString() {
        storeBegin = storeEnd = readBegin;
        boolean escaped = false;
        int ch;
        do {
            ch = read();
            switch (ch) {
                case -1:
                    throw new JsonException("Unexpected EOF");
                case '\\':
                    escaped = true;
                    int ch2 = read();
                    switch (ch2) {
                        case 'b':
                            buf[storeEnd++] = '\b';
                            break;
                        case 't':
                            buf[storeEnd++] = '\t';
                            break;
                        case 'n':
                            buf[storeEnd++] = '\n';
                            break;
                        case 'f':
                            buf[storeEnd++] = '\f';
                            break;
                        case 'r':
                            buf[storeEnd++] = '\r';
                            break;
                        case '"':
                        case '\\':
                        case '/':
                            buf[storeEnd++] = (char)ch2;
                            break;
                        case 'u': {
                            char unicode = 0;
                            for (int i = 0; i < 4; i++) {
                                int ch3 = read();
                                unicode <<= 4;
                                if (ch3 >= '0' && ch3 <= '9') {
                                    unicode |= ((char) ch3) - '0';
                                } else if (ch3 >= 'a' && ch3 <= 'f') {
                                    unicode |= (((char) ch3) - 'a') + 0xA;
                                } else if (ch3 >= 'A' && ch3 <= 'F') {
                                    unicode |= (((char) ch3) - 'A') + 0xA;
                                } else {
                                    throw new JsonParsingException("Unexpected Char="+ch3, getLastCharLocation());
                                }
                            }
                            buf[storeEnd++] = (char) (unicode & 0xffff);
                            break;
                        }
                        default:
                            throw new JsonParsingException("Unexpected Char="+ch2, getLastCharLocation());
                    }
                    break;
                case '"':
                    break;
                default:
                    if ((ch >= 0x0000 && ch <= 0x001F) ||
                            (ch >= 0x007F && ch <= 0x009F)) {
                        throw new JsonException("Unexpected Char="+ch);
                    }
                    if (!escaped) {
                        storeEnd++;
                    } else {
                        buf[storeEnd++] = (char)ch;
                    }
            }
        } while (ch != '"');
    }

    private void readNumber(int ch)  {
        storeBegin = storeEnd = readBegin-1;
        // sign
        if (ch == '-') {
            storeEnd++;
            this.minus = true;

            ch = read();
            if (ch < '0' || ch >'9') {
                throw new JsonParsingException("Unexpected Char="+ch, getLastCharLocation());
            }
        }

        // int
        if (ch == '0') {
            storeEnd++;
            ch = read();
        } else {
            do {
                storeEnd++;
                ch = read();
            } while (ch >= '0' && ch <= '9');
        }

        // frac
        if (ch == '.') {
            this.fracOrExp = true;
            int count = 0;
            do {
                storeEnd++;
                ch = read();
                count++;
            } while (ch >= '0' && ch <= '9');
            if (count == 1) {
                throw new JsonParsingException("Unexpected Char="+ch, getLastCharLocation());
            }
        }

        // exp
        if (ch == 'e' || ch == 'E') {
            storeEnd++;
            this.fracOrExp = true;
            ch = read();
            if (ch == '+' || ch == '-') {
                storeEnd++;
                ch = read();
            }
            int count;
            for (count = 0; ch >= '0' && ch <= '9'; count++) {
                storeEnd++;
                ch = read();
            }
            if (count == 0) {
                throw new JsonParsingException("Unexpected Char="+ch, getLastCharLocation());
            }
        }
        unread(ch);
    }

    private void readTrue() {
        char ch1 = (char) read();
        if (ch1 != 'r') {
            throw new JsonParsingException("Unexpected Char="+ch1+" expecting 'r'", getLastCharLocation());
        }
        char ch2 = (char) read();
        if (ch2 != 'u') {
            throw new JsonParsingException("Unexpected Char="+ch2+" expecting 'u'", getLastCharLocation());
        }
        char ch3 = (char) read();
        if (ch3 != 'e') {
            throw new JsonParsingException("Unexpected Char="+ch3+" expecting 'e'", getLastCharLocation());
        }
    }

    private void readFalse() {
        char ch1 = (char) read();
        if (ch1 != 'a') {
            throw new JsonParsingException("Unexpected Char="+ch1+" expecting 'a'", getLastCharLocation());
        }
        char ch2 = (char) read();
        if (ch2 != 'l') {
            throw new JsonParsingException("Unexpected Char="+ch2+" expecting 'l'", getLastCharLocation());
        }
        char ch3 = (char) read();
        if (ch3 != 's') {
            throw new JsonParsingException("Unexpected Char="+ch3+" expecting 's'", getLastCharLocation());
        }
        char ch4 = (char) read();
        if (ch4 != 'e') {
            throw new JsonParsingException("Unexpected Char="+ch4+" expecting 'e'", getLastCharLocation());
        }
    }

    private void readNull() {
        char ch1 = (char) read();
        if (ch1 != 'u') {
            throw new JsonParsingException("Unexpected Char="+ch1+" expecting 'u'", getLastCharLocation());
        }
        char ch2 = (char) read();
        if (ch2 != 'l') {
            throw new JsonParsingException("Unexpected Char="+ch2+" expecting 'l'", getLastCharLocation());
        }
        char ch3 = (char) read();
        if (ch3 != 'l') {
            throw new JsonParsingException("Unexpected Char="+ch3+" expecting 'l'", getLastCharLocation());
        }
    }

    JsonToken nextToken() throws IOException {
        
        reset();
        int ch = read();

        // whitespace
        while (ch == 0x20 || ch == 0x09 || ch == 0x0a || ch == 0x0d) {
            ch = read();
        }

        switch (ch) {
            case '"':
                readString();
                return JsonToken.STRING;
            case '{':
                return JsonToken.CURLYOPEN;
            case '[':
                return JsonToken.SQUAREOPEN;
            case ':':
                return JsonToken.COLON;
            case ',':
                return JsonToken.COMMA;
            case 't':
                readTrue();
                return JsonToken.TRUE;
            case 'f':
                readFalse();
                return JsonToken.FALSE;
            case 'n':
                readNull();
                return JsonToken.NULL;
            case ']':
                return JsonToken.SQUARECLOSE;
            case '}':
                return JsonToken.CURLYCLOSE;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '-':
                readNumber(ch);
                return JsonToken.NUMBER;
            case -1:
                return JsonToken.EOF;
            default:
                throw new JsonParsingException("Unexpected char="+(char)ch, getLastCharLocation());
        }
    }

    // Gives the location of the last char. Used for
    // JsonParsingException.getLocation
    JsonLocation getLastCharLocation() {
        // Already read the char, so subtracting -1
        return new JsonLocationImpl(lineNo, columnNo-1, streamOffset-1);
    }

    // Gives the parser location. Used for JsonParser.getLocation
    JsonLocation getLocation() {
        return new JsonLocationImpl(lineNo, columnNo, streamOffset);
    }

    private int readChar() {
        try {
            if (readBegin == readEnd) {     // need to fill the buffer
                if ((storeEnd-storeBegin) == buf.length) {
                    // buffer is full, double the capacity
                    buf = Arrays.copyOf(buf, 2* buf.length);
                } else {
                    // Left shift all the required data to make space
                    if (storeEnd-storeBegin > 0) {
                        System.arraycopy(buf, storeBegin, buf, 0, storeEnd-storeBegin);
                        storeEnd = storeEnd - storeBegin;
                        storeBegin = 0;
                    }
                }
                // Fill the rest of the buf
                int len = reader.read(buf, storeEnd, buf.length-storeEnd);
                if (len == -1) {
                    return -1;
                }
                assert len != 0;
                readBegin = storeEnd;
                readEnd = readBegin+len;
            }
            return buf[readBegin++];
        } catch (IOException ioe) {
            throw new JsonException("I/O error while tokenizing JSON", ioe);
        }
    }

    private void reset() {
        storeBegin = 0;
        storeEnd = 0;
        value = null;
        bd = null;
        minus = false;
        fracOrExp = false;
    }

    String getValue() {
        if (value == null) {
            value = new String(buf, storeBegin, storeEnd-storeBegin);
        }
        return value;
    }

    BigDecimal getBigDecimal() {
        if (bd == null) {
            bd = new BigDecimal(buf, storeBegin, storeEnd-storeBegin);
        }
        return bd;
    }

    int getInt() {
        // no need to create BigDecimal for common integer values (1-9 digits)
        int storeLen = storeEnd-storeBegin;
        if (!fracOrExp && (storeLen <= 9 || (minus && storeLen == 10))) {
            int num = 0;
            int i = minus ? 1 : 0;
            for(; i < storeLen; i++) {
                num = num * 10 + (buf[storeBegin+i] - '0');
            }
            return minus ? -num : num;
        } else {
            return getBigDecimal().intValue();
        }
    }

    boolean isIntegral() {
        return !fracOrExp || getBigDecimal().scale() == 0;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
    
}
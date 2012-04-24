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
import java.io.IOException;
import java.io.Reader;

/**
 * JSON Tokenizer
 *
 * @author Jitendra Kotamraju
 */
final class JsonTokenizer {

    private final TokenizerReader reader;
    private int pushbackChar = -1;

    enum JsonToken {
        CURLYOPEN,  SQUAREOPEN,
        COLON, COMMA,
        STRING, NUMBER, TRUE, FALSE, NULL,
        CURLYCLOSE, SQUARECLOSE,
        EOF
    }

    JsonTokenizer(Reader reader) {
        this.reader = new DirectReader(reader);
    }

    private int read() {
        if (pushbackChar != -1) {
            int tempCh = pushbackChar;
            pushbackChar = -1;
            return tempCh;
        }
        return reader.readChar();
    }

    private void store(char ch) {
        reader.storeChar(ch);
    }

    private void unread(int ch) {
        pushbackChar = ch;
    }

    private void readString() {
        int ch;
        do {
            ch = read();
            switch (ch) {
                case -1:
                    throw new JsonException("Unexpected EOF");
                case '\\':
                    int ch2 = read();
                    switch (ch2) {
                        case 'b':
                            store('\b');
                            break;
                        case 't':
                            store('\t');
                            break;
                        case 'n':
                            store('\n');
                            break;
                        case 'f':
                            store('\f');
                            break;
                        case 'r':
                            store('\r');
                            break;
                        case '"':
                        case '\\':
                        case '/':
                            store((char) ch2);
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
                                    throw new JsonException("Unexpected Char="+ch3);
                                }
                            }
                            store((char) (unicode & 0xffff));
                            break;
                        }
                        default:
                            throw new JsonException("Unexpected Char="+ch2);
                    }
                    break;
                case '"':
                    break;
                default:
                    if ((ch >= 0x0000 && ch <= 0x001F) ||
                            (ch >= 0x007F && ch <= 0x009F)) {
                        throw new JsonException("Unexpected Char="+ch);
                    }
                    store((char) ch);
            }
        } while (ch != '"');
    }

    private void readNumber(int ch)  {

        // sign
        if (ch == '-') {
            store((char) ch);

            ch = read();
            if (ch < '0' || ch >'9') {
                throw new JsonException("Unexpected Char="+ch);
            }
        }

        // int
        if (ch == '0') {
            store((char) ch);
            ch = read();
        } else {
            do {
                store((char) ch);
                ch = read();
            } while (ch >= '0' && ch <= '9');
        }

        // frac
        if (ch == '.') {
            int count = 0;
            do {
                store((char) ch);
                ch = read();
                count++;
            } while (ch >= '0' && ch <= '9');
            if (count == 1) {
                throw new JsonException("Unexpected Char="+ch);
            }
        }

        // exp
        if (ch == 'e' || ch == 'E') {
            store((char) ch);
            ch = read();
            if (ch == '+' || ch == '-') {
                store((char) ch);
                ch = read();
            }
            int count;
            for (count = 0; ch >= '0' && ch <= '9'; count++) {
                store((char) ch);
                ch = read();
            }
            if (count == 0) {
                throw new JsonException("Unexpected Char="+ch);
            }
        }
        unread(ch);
    }

    private void readTrue() {
        char ch1 = (char) read();
        char ch2 = (char) read();
        char ch3 = (char) read();
    }

    private void readFalse() {
        char ch1 = (char) read();
        char ch2 = (char) read();
        char ch3 = (char) read();
        char ch4 = (char) read();
    }

    private void readNull() {
        char ch1 = (char) read();
        char ch2 = (char) read();
        char ch3 = (char) read();
    }

    JsonToken nextToken() throws JsonException, IOException {
        
        reader.reset();
        int ch = read();

        // whitespace
        while (ch == 0x20 || ch == 0x09 || ch == 0x0a || ch == 0x0d) {
            ch = read();
        }

        switch (ch) {
            case -1:
                return JsonToken.EOF;
            case '{':
                return JsonToken.CURLYOPEN;
            case '[':
                return JsonToken.SQUAREOPEN;
            case ':':
                return JsonToken.COLON;
            case ',':
                return JsonToken.COMMA;
            case '"':
                readString();
                return JsonToken.STRING;
            case '-':
                readNumber(ch);
                return JsonToken.NUMBER;
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
            default:
                if (ch >= '0' && ch <= '9') {
                    readNumber(ch);
                    return JsonToken.NUMBER;
                }
                throw new JsonException("Unexpected char="+(char)ch);
        }
    }

    // returns string or number values
    String getValue() {
        return reader.getValue();
    }
    
    private static interface TokenizerReader {
        int readChar();
        void storeChar(int ch);
        void reset();
        String getValue();
    }
    
    private static class BufReader implements TokenizerReader {
        // Buffer for parsing
        private char[] buf = new char[8192];
        private int length;
        private int curPtr;

        // Current string/number starting ptr in the buffer and the length
        private int valuePtr = -1;
        private int valueLength;
        private final Reader reader;

        boolean eof;

        BufReader(Reader reader) {
            this.reader = reader;
        }

        public int readChar() {
            if (eof) {
                return -1;
            } else if (curPtr < length) {
                return buf[curPtr++];
            } else {
                if (valuePtr == -1) {
                    // Not tracking any value. Use the entire buf
                    curPtr = 0;
                    length = 0;
                } else if (valuePtr == 0 && valueLength == buf.length) {
                    // partial value uses entire buf. Double the buf.
                    assert length == buf.length;
                    assert curPtr == buf.length;
                    char[] temp = new char[2*buf.length];
                    System.arraycopy(buf, 0, temp, 0, valueLength);
                    buf = temp;
                } else if (valuePtr == 0 && valueLength < buf.length) {
                    // partial value (that is at the beginning of buf) uses part of buf
                    curPtr = valueLength;
                    length = valueLength;
                } else {
                    // Partial value uses part of buf
                    // value is copied to the beginning of the buf
                    System.arraycopy(buf, valuePtr, buf, 0, valueLength);
                    length = valueLength;
                    curPtr = valueLength;
                    valuePtr = 0;
                }
                // Now fill the buf starting from curPtr
                while(curPtr <= length) {
                    int read;
                    try {
                        read = reader.read(buf, curPtr, buf.length-length);
                    } catch(IOException ioe) {
                        throw new JsonException(ioe);
                    }
                    if (read == -1) {
                        eof = true;
                        return -1;
                    } else {
                        length += read;
                    }
                }
                return buf[curPtr++];
            }
        }

        public void storeChar(int ch) {
            if (valuePtr == -1) {
                valuePtr = curPtr-1;
            }
            buf[valuePtr+valueLength] = (char)ch;
            valueLength++;
        }

        public void reset() {

        }

        public String getValue() {
            return null;
        }
        
    }
    
    private static class DirectReader implements TokenizerReader {
        private final Reader reader;
        private StringBuilder builder;

        DirectReader(Reader reader) {
            this.reader = reader;
            this.builder = new StringBuilder();
        }
        
        public int readChar() {
            try {
                return reader.read();
            } catch (IOException ioe) {
                throw new JsonException(ioe);
            }
        }

        public void storeChar(int ch) {
            builder.append((char)ch);
        }

        public void reset() {
            if (builder.length() != 0 ) {
                builder = new StringBuilder();
            }
        }

        public String getValue() {
            return builder.toString();
        }
        
    }
    
}
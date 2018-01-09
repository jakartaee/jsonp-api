/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.glassfish.json.customprovider;

import javax.json.JsonException;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Jitendra Kotamraju
 */
public class TestGenerator implements JsonGenerator {
    private final Writer writer;

    public TestGenerator(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void flush() {
    }

    @Override
    public JsonGenerator writeStartObject() {
        return null;
    }

    @Override
    public JsonGenerator writeStartObject(String name) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, String fieldValue) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, int value) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, long value) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, double value) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, BigInteger value) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, BigDecimal value) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, boolean value) {
        return null;
    }

    @Override
    public JsonGenerator writeNull(String name) {
        return null;
    }

    @Override
    public JsonGenerator write(JsonValue value) {
        return null;
    }

    @Override
    public JsonGenerator writeStartArray() {
        try {
            writer.write("[");
        } catch(IOException ioe) {
            throw new JsonException("I/O error", ioe);
        }
        return this;
    }

    @Override
    public JsonGenerator writeStartArray(String name) {
        return null;
    }

    @Override
    public JsonGenerator write(String name, JsonValue value) {
        return null;
    }

    @Override
    public JsonGenerator write(String value) {
        return null;
    }


    @Override
    public JsonGenerator write(int value) {
        return null;
    }

    @Override
    public JsonGenerator write(long value) {
        return null;
    }

    @Override
    public JsonGenerator write(double value) {
        return null;
    }

    @Override
    public JsonGenerator write(BigInteger value) {
        return null;
    }

    @Override
    public JsonGenerator write(BigDecimal value) {
        return null;
    }

    @Override
    public JsonGenerator write(boolean value) {
        return null;
    }

    @Override
    public JsonGenerator writeNull() {
        return null;
    }

    @Override
    public JsonGenerator writeEnd() {
        try {
            writer.write("]");
        } catch(IOException ioe) {
            throw new JsonException("I/O error", ioe);
        }
        return this;
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch(IOException ioe) {
            throw new JsonException("I/O error", ioe);
        }
    }

    @Override
    public JsonGenerator writeKey(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

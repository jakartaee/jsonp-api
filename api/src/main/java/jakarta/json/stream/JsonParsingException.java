/*
 * Copyright (c) 2012, 2021 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.json.stream;

import jakarta.json.JsonException;

/**
 * {@code JsonParsingException} is used when an incorrect JSON is
 * being parsed.
 */
public class JsonParsingException extends JsonException {

    /** for serialization */
    private static final long serialVersionUID = 9073566598484238797L;

    /**
     * The location of the incorrect JSON.
     */
    private final JsonLocation location;

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param location the location of the incorrect JSON
     */
    public JsonParsingException(String message, JsonLocation location) {
        super(message);
        this.location = location;
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A <code>null</code> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @param location the location of the incorrect JSON
     */
    public JsonParsingException(String message, Throwable cause, JsonLocation location) {
        super(message, cause);
        this.location = location;
    }

    /**
     * Return the location of the incorrect JSON.
     *
     * @return the non-null location of the incorrect JSON
     */
    public JsonLocation getLocation() {
        return location;
    }

}


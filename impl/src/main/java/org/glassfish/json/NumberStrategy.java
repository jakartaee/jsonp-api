/*
 * Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Defines how should java types {@link Long}, {@link BigDecimal} and {@link BigInteger} be serialized into JSON.
 * <p>
 * These types may contain values that are outside of IEEE754 64bit (double precision) range. A good interoperability
 * between different JSON generators and parsers can be achieved by constraining numbers outside of
 * IEEE754 64bit precision range to be serialized as string values.
 * <p>
 *
 * <pre>
 *  JSON_NUMBER:
 *    - Always serialize {@link Long}, {@link BigDecimal} and {@link BigInteger} as JSON number type.
 *
 *  JSON_STRING:
 *    - Always serialize {@link Long}, {@link BigDecimal} and {@link BigInteger} as JSON string type.
 *
 * </pre>
 *
 */
public interface NumberStrategy {

    /**
     * Always serialize as JSON number type.
     */
    String JSON_NUMBER = "JSON_NUMBER";

    /**
     * Always serialize as JSON string type.
     */
    String JSON_STRING = "JSON_STRING";

    void write(Long value);

    void write(BigInteger value);

    void write(BigDecimal value);
}

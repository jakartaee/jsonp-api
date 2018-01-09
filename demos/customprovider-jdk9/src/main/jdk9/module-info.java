/*
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

module org.glassfish.java.json.demos.customprovider {
    requires transitive java.json;
    exports org.glassfish.json.customprovider;
    provides javax.json.spi.JsonProvider with org.glassfish.json.customprovider.TestProvider;
}

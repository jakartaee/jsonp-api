/*
 * Copyright (c) 2017, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

module org.eclipse.jsonp.demos.customprovider {
    requires transitive jakarta.json;
    exports org.eclipse.jsonp.demos.customprovider;
    provides jakarta.json.spi.JsonProvider with org.eclipse.jsonp.demos.customprovider.TestProvider;
}

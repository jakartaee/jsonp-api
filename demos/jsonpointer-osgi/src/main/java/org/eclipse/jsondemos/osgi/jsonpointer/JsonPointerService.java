/*
 * Copyright (c) 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package org.eclipse.jsondemos.osgi.jsonpointer;

import jakarta.json.JsonObject;

/**
 * Implementation of this interface will be registered in OSGI container as a service.
 */
public interface JsonPointerService {

    JsonObject read(String pathToResource);
}

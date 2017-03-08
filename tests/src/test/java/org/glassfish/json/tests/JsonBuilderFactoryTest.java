/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
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
package org.glassfish.json.tests;

import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author lukas
 */
public class JsonBuilderFactoryTest {
    
    @Test
    public void testArrayBuilder() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        Assert.assertNotNull(builderFactory.createArrayBuilder());
    }
    
    @Test(expected = NullPointerException.class)
    public void testArrayBuilderNPE() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        builderFactory.createArrayBuilder(null);
    }

    @Test
    public void testArrayBuilderFromArray() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        JsonArrayBuilder builder = builderFactory.createArrayBuilder(JsonBuilderTest.buildPhone());
        Assert.assertEquals(JsonBuilderTest.buildPhone(), builder.build());
    }

    @Test
    public void testObjectBuilder() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        Assert.assertNotNull(builderFactory.createObjectBuilder());
    }
    
    @Test(expected = NullPointerException.class)
    public void testObjectBuilderNPE() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        builderFactory.createObjectBuilder((JsonObject) null);
    }

    @Test(expected = NullPointerException.class)
    public void testObjectBuilderNPE_map() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        builderFactory.createObjectBuilder((Map<String, Object>) null);
    }

    @Test
    public void testObjectBuilderFromObject() {
        JsonBuilderFactory builderFactory = Json.createBuilderFactory(null);
        JsonObjectBuilder builder = builderFactory.createObjectBuilder(JsonBuilderTest.buildPerson());
        Assert.assertEquals(JsonBuilderTest.buildPerson(), builder.build());
    }
}

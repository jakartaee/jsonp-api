<!--
    Copyright (c) 2026 Contributors to the Eclipse Foundation

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
-->

# Jakarta EE JSON Processing Technology Compatibility Kit, Version 2.2

## Kit Contents

The Jakarta EE JSON Processing, Version 2.2 Technology Compatibility Kit
(TCK) includes the following items:

- **Jakarta EE JSON Processing TCK tests signature, API, and pluggability tests:**

  - A **signature test** that checks that all of the public APIs are supported in the
    Jakarta EE JSON Processing Version 2.2 implementation that is being tested

  - **API tests** for all of the public APIs under the `jakarta.json`,
    `jakarta.json.spi`, `jakarta.json.stream` package

  - **Pluggability tests** for testing the SPI provider interface for supplying
    your own JsonProvider

---

## TCK Facts

The test suite bundle contains the following:

- 1 signature test
- 178 API tests
- 18 pluggability tests

---

## Platform Notes

The Jakarta EE JSON Processing TCK tests have been built with JDK 17
and tested with Java™ Platform, Standard Edition 17+

The Jakarta EE JSON Processing TCK tests have been run against the
following Jakarta EE JSON Processing 2.2 Compatible Implementations:

- Eclipse Parsson <!-- TODO 2.2 Parsson Version -->

---

## Documentation

The HTML and PDF versions of the TCK User's Guide can be found in the `docs` folder.

The [Javadoc Assertion List](assertions/JavadocAssertions.md) lists
all the javadoc assertions that are tested by the Jakarta EE JSON Processing 2.2 TCK.

Updated release notes may be available at the Jakarta EE Specification web site:
<https://jakarta.ee/specifications/jsonp/2.2/>

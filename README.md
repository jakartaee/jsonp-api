[//]: # " Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved. "
[//]: # "  "
[//]: # " This program and the accompanying materials are made available under the "
[//]: # " terms of the Eclipse Public License v. 2.0, which is available at "
[//]: # " http://www.eclipse.org/legal/epl-2.0. "
[//]: # "  "
[//]: # " This Source Code may also be made available under the following Secondary "
[//]: # " Licenses when the conditions for such availability set forth in the "
[//]: # " Eclipse Public License v. 2.0 are satisfied: GNU General Public License, "
[//]: # " version 2 with the GNU Classpath Exception, which is available at "
[//]: # " https://www.gnu.org/software/classpath/license.html. "
[//]: # "  "
[//]: # " SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 "

[![Build Status](https://travis-ci.org/eclipse-ee4j/jsonp.svg?branch=master)](https://travis-ci.org/eclipse-ee4j/jsonp)

# Jakarta JSON Processing

Jakarta JSON Processing provides portable APIs to parse, generate, transform, and query JSON documents.
This project contains Jakarta JSON Processing specification, API and a compatible implementation.

## Build

Use the following command:
```bash
mvn -U -C clean install -Dnon.final=true
```

## License

* Most of the Jakarta JSON Processing project source code is licensed
under the [Eclipse Public License (EPL) v2.0](https://projects.eclipse.org/license/epl-2.0)
and [GNU General Public License (GPL) v2 with Classpath Exception](https://www.gnu.org/software/classpath/license.html);
see the license information at the top of each source file.
* The source code for the demo programs is licensed
under the [Eclipse Distribution License (EDL) v1.0.](https://www.eclipse.org/org/documents/edl-v10.php).
* The binary jar files published to the Maven repository are licensed
under the same licenses as the corresponding source code;
see the file `META-INF/LICENSE.txt` in each jar file.

You’ll find the text of the licenses in the workspace in various `LICENSE.txt` or `LICENSE.md` files.
Don’t let the presence of these license files in the workspace confuse you into thinking
that they apply to all files in the workspace.

You should always read the license file included with every download, and read
the license text included in every source file.

## Links

- [Jakarta JSON Processing official web site](https://eclipse-ee4j.github.io/jsonp)
- [Jakarta JSON Processing @ Eclipse](https://projects.eclipse.org/projects/ee4j.jsonp)

## Contributing

We use [contribution policy](CONTRIBUTING.md), which means we can only accept contributions under
the terms of [Eclipse Contributor Agreement](http://www.eclipse.org/legal/ECA.php).

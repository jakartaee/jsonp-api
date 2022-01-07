#!/usr/bin/env bash
#
# Copyright (c) 2021, 2022 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License v. 2.0, which is available at
# http://www.eclipse.org/legal/epl-2.0.
#
# This Source Code may also be made available under the following Secondary
# Licenses when the conditions for such availability set forth in the
# Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
# version 2 with the GNU Classpath Exception, which is available at
# https://www.gnu.org/software/classpath/license.html.
#
# SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
#

##script to install the artifact directory contents into a local maven repository

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
  VERSION="2.1.0-SNAPSHOT"
fi

# Parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-project -Dversion="$VERSION" -Dpackaging=pom

# pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common -Dversion="$VERSION" -Dpackaging=pom

# jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common -Dversion="$VERSION" -Dpackaging=jar

# sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common-sources -Dversion="$VERSION" -Dpackaging=jar

# pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests -Dversion="$VERSION" -Dpackaging=pom

# jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests -Dversion="$VERSION" -Dpackaging=jar

# sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-sources -Dversion="$VERSION" -Dpackaging=jar

# pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability -Dversion="$VERSION" -Dpackaging=pom

# jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability -Dversion="$VERSION" -Dpackaging=jar

# sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability-sources -Dversion="$VERSION" -Dpackaging=jar

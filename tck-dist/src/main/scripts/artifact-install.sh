#!/usr/bin/env bash
#
# Copyright (c) 2021, 2025 Oracle and/or its affiliates. All rights reserved.
# Copyright (c) 2026 Contributors to the Eclipse Foundation
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

# This script is distributed alongside the artifacts, so operate from its directory
cd "$(dirname "${BASH_SOURCE[0]}")" || exit 1

if [[ $1 =~ ^[0-9]+\.[0-9]+\.[0-9]+.*$ ]]; then
  VERSION="$1"
else
  # Derive the version from the project parent pom file name:
  # jakarta.json-parent-<version>.pom
  VERSION=""
  for f in jakarta.json-parent-*.pom; do
    [[ -e $f ]] || continue
    VERSION="${f#jakarta.json-parent-}"
    VERSION="${VERSION%.pom}"
    break
  done
  if [[ -z $VERSION ]]; then
    echo "Unable to determine TCK version: no jakarta.json-parent-<version>.pom found in $(pwd)" >&2
    exit 1
  fi
fi

# Project parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-parent-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-parent -Dversion="$VERSION" -Dpackaging=pom

# Jakarta JSON API jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-api-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-api -Dversion="$VERSION" -Dpackaging=jar

# TCK parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck -Dversion="$VERSION" -Dpackaging=pom

# TCK common pom
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common -Dversion="$VERSION" -Dpackaging=pom

# TCK common jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common -Dversion="$VERSION" -Dpackaging=jar

# TCK common sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-common-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-common-sources -Dversion="$VERSION" -Dpackaging=jar

# TCK tests pom
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests -Dversion="$VERSION" -Dpackaging=pom

# TCK tests jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests -Dversion="$VERSION" -Dpackaging=jar

# TCK tests sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-sources -Dversion="$VERSION" -Dpackaging=jar

# TCK tests pluggability pom
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION".pom -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability -Dversion="$VERSION" -Dpackaging=pom

# TCK tests pluggability jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION".jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability -Dversion="$VERSION" -Dpackaging=jar

# TCK tests pluggability sources jar
mvn org.apache.maven.plugins:maven-install-plugin:3.2.0:install-file \
-Dfile=jakarta.json-tck-tests-pluggability-"$VERSION"-sources.jar -DgroupId=jakarta.json \
-DartifactId=jakarta.json-tck-tests-pluggability-sources -Dversion="$VERSION" -Dpackaging=jar

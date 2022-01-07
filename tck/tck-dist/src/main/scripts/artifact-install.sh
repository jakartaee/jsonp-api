#!/usr/bin/env bash

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

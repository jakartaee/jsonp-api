* standalone/jakarta.json-${project.version}.jar contains both "Jakarta JSON Processing 1.1" API
  and its default provider implementation. Keep it in classpath for both compiling and running your application.
  Automatic module name is: 'java.json'

For running on JPMS, following modules are provided:
* mods/jakarta.json-api-${project.version}.jar - 'java.json' module containing only API classes
* mods/jakarta.json-${project.version}-module.jar - 'org.glassfish.java.json' module containing implementation

Integration with JAX-RS: Java API for RESTful Web Services (JAX-RS) is provided through
* jaxrs/jsonp-jaxrs-${project.version}.jar


IMPORTANT NOTE: module names are not yet final and may change in the future releases


* If you are running with maven, you can use the following maven coordinates:

for standalone reference implementation which includes APIs and implementation classes:
  <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>jakarta.json</artifactId>
      <version>${project.version}</version>
  </dependency>

for APIs:
  <dependency>
      <groupId>jakarta.json</groupId>
      <artifactId>jakarta.json-api</artifactId>
      <version>${project.version}</version>
  </dependency>

for implementation only:
  <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>jakarta.json</artifactId>
      <classifier>module</classifier>
      <version>${project.version}</version>
  </dependency>

for JAX-RS integration module:
  <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>jsonp-jaxrs</artifactId>
      <version>${project.version}</version>
  </dependency>


* GlassFish 5.x already bundles latest JSON Processing implementation and JAX-RS integration module.
If you deploy an application with GlassFish 5.x, your application (war/ear) doesn't have to bundle APIs nor the ri jar.

* Samples can be run from https://github.com/javaee/glassfish-samples

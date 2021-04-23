For running on JPMS, following modules are provided:
* mods/jakarta.json-api-${project.version}.jar - 'jakarta.json' module containing only API classes
* mods/jsonp-${project.version}.jar - 'org.eclipse.jsonp' module containing implementation

Integration with Jakarta RESTful Web Services:
* jaxrs/jsonp-jaxrs-${project.version}.jar

* standalone/jakarta.json-${project.version}.jar - 'jakarta.json' module containing API classes and implementation, which servers as a fallback for environments
willing to support different implementations of Jakarta JSON Processing API while also providing some default implementation, such as application servers.
When this library is used, then Eclipse JSONP is the default implementation being provided.

NOTE: jakarta.json-api.jar and jakarta.json.jar CAN NOT co-exist together when running on JPMS. One can be used as a drop-in
replacement of the other wrt Jakarta JSON Processing API.

IMPORTANT NOTE: module names are not yet final and may change in the future releases


* If you are running with maven, you can use the following maven coordinates:

for the implementation:
  <dependency>
      <groupId>org.eclipse.jsonp</groupId>
      <artifactId>jsonp</artifactId>
      <version>${project.version}</version>
  </dependency>

for APIs:
  <dependency>
      <groupId>jakarta.json</groupId>
      <artifactId>jakarta.json-api</artifactId>
      <version>${api.version}</version>
  </dependency>

for Jakarta RESTful Web Services integration module:
  <dependency>
      <groupId>org.eclipse.jsonp</groupId>
      <artifactId>jsonp-jaxrs</artifactId>
      <version>${project.version}</version>
  </dependency>

for Jakarta JSON Processing API which uses Eclipse JSONP as a default provider:
  <dependency>
      <groupId>org.eclipse.jsonp</groupId>
      <artifactId>jakarta.json</artifactId>
      <version>${project.version}</version>
  </dependency>

* GlassFish 6.x already bundles latest Jakarta JSON Processing implementation and Jakarta RESTful Web Services integration module.
If you deploy an application with GlassFish 6.x, your application (war/ear) doesn't have to bundle APIs nor the implementation.

* Samples can be run from https://github.com/eclipse-ee4j/glassfish-samples

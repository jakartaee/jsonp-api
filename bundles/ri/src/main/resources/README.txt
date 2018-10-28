* javax.json-${project.version}.jar contains both "JSR 374 : Java API for JSON Processing 1.1" API and its default provider implementation. Keep it in classpath for both compiling and running your application.

* If you are running with maven, you can use the following maven coordinates:

for APIs:
  <dependency>
      <groupId>jakarta.json</groupId>
      <artifactId>jakarta.json-api</artifactId>
      <version>${project.version}</version>
  </dependency>

for reference implementation:

  <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>javax.json</artifactId>
      <version>${project.version}</version>
  </dependency>

* GlassFish 5.x already bundles latest JSON Processing implementation and JAX-RS integration module. If you deploy an application with GlassFish 5.x, your application (war/ear) doesn't have to bundle APIs nor the ri jar.

* Samples can be run from https://github.com/javaee/glassfish-samples

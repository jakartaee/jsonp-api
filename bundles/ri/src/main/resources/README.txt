* javax.json-1.0.jar contains both "JSR 353 : Java API for JSON Processing" API and its default provider implementation. Keep it in classpath for both compiling and running your application.

* If you are running with maven, you can use the following maven coordinates:

  <dependency>
      <groupId>org.glassfish</groupId>
      <artifactId>javax.json</artifactId>
      <version>1.0</version>
  </dependency>

* GlassFix 4.x already bundles latest JSON Processing implementation and JAX-RS integration module. If you deploy an application with GlassFish 4.x, your application (war/ear) doesn't have to bundle the ri jar.

* Samples can be run from http://glassfish-samples.java.net

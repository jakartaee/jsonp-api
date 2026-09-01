# JMH performance tests for Jakarta JSON Processing.

Binaries for performance testing should never be uploaded into Maven repository.

## Running tests
 - build it with maven
 - run from commandline


### Build it

```sh
mvn package -f perf/pom.xml
```

### Run it

For all JMH options available:

```sh
java -jar perf/target/jsonp-jmh.jar -h
```

Run examples:

```sh
java -jar perf/target/jsonp-jmh.jar
java -jar perf/target/jsonp-jmh.jar -i 3 -t 5 -f 3 -prof stack
java -jar perf/target/jsonp-jmh.jar FactoryTest.createObjectBuilder_viaJson
```
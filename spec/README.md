Jakarta JSON Processing Specification
======================================

This project generates the Jakarta JSON Processing Specification.

Building
--------

Prerequisites:

* JDK 17+
* Maven 3.9.0+

Run the full build with DRAFT status:

`mvn install`

Run the full build with custom status (ex. "Final Release"):

`mvn install -Dstatus=“Final Release”` 

Locate the html files:
- `target/generated-docs/jsonp-spec-<version>.html`

Locate the PDF files:
- `target/generated-docs/jsonp-spec-<version>.pdf`

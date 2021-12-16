///////////////////////////////////////////////////////////////////////
NOTE TO WRITERS:
This include file allows for additional instructions for running the TCK.
///////////////////////////////////////////////////////////////////////


[[sthref9]][[running-the-pluggability-tests]]

5.6 Running the Pluggability Tests
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Use the following modes to run the pluggability tests:

* link:#BCGFHIDI[Section 5.6.1, "To Run the Pluggability Tests in GUI
Mode"]
* link:#BCGEGBGE[Section 5.6.2, "To Run the Pluggability Tests in
Command-Line Mode"]

[[BCGFHIDI]][[to-run-the-pluggability-tests-in-gui-mode]]

5.6.1 To Run the Pluggability Tests in GUI Mode
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

When running JavaTest in GUI mode as described above, you are prompted
to specify whether you want to run all or a subset of the test suite.
Since the pluggability tests must be run separately from
the other tests, select Yes.  Select the tests you want to run from the
displayed test tree, then select Next.  You can select entire branches
of the test tree, or use Ctrl+Click or Shift+Click to select multiple
tests or ranges of tests, respectively. Select all tests except
pluggability tests by unchecking the pluggability tests.

After running the other tests, run the pluggability tests, which
require a separate test run:

1.  If the JavaTest GUI is running, exit the GUI.
2.  Change to the `<TS_HOME>/bin` directory and reconfigure environment
to use the `JsonProvider` class that is supplied with the test suite and
restart the JavaTest GUI: +
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant enable.alternate.jsonp.provider
ant gui
----
3.  Select Configure, then select Edit Configuration.
4.  Select Tests to Run.
5.  Select the tests you want to run from the displayed test tree, then
select Next. +
Select the pluggability tests only by unchecking all of the other tests.
6.  Select Done.
7.  Select Run Tests, then select Start.
8.  Exit the JavaTest GUI when finished.
9.  Disable the alternate JSON provider that you enabled in Step 2,
before you initiated the test run for the pluggability tests: +
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant disable.alternate.jsonp.provider
----

[[BCGEGBGE]][[to-run-the-pluggability-tests-in-command-line-mode]]

5.6.2 To Run the Pluggability Tests in Command-Line Mode
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

There are two ways to run the pluggability tests from the command line.
It is highly recommended to use the first method, since it automatically
calls the Ant targets to enable and disable the test provider. If you
choose to use the second method, you will need to execute those Ant
targets manually before and after you run the tests.

The first method is by executing the `run.pluggability` target in Ant:

1.  Execute the `run.pluggability` target using Ant: +
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant run.pluggability
----
The `run.pluggability` target calls Ant targets that enable the test
provider, run the pluggability tests, then disable the test provider.

The pluggability tests can also be run an alternate way, albeit one that
requires more manual intervention:

1.  Execute the `enable.alternate.jsonp.provider` target using Ant: +
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant enable.alternate.jsonp.provider
----
The `enable.alternate.jsonp.provider` target enables the alternate test
provider.
2.  Change to the `<TS_HOME/src/com/sun/ts/tests/jsonp/pluggability>`
directory: +
[source,oac_no_warn]
----
cd <TS_HOME/src/com/sun/ts/tests/jsonp/pluggability>
----
3.  Start the pluggability test run by executing the following command: +
[source,oac_no_warn]
----
ant runclient
----
This will run just the pluggability tests under the
<`TS_HOME/src/com/sun/ts/tests/jsonp/pluggability` directory, using a
test-supplied `JsonProvider Class` for testing the SPI. All
`JsonProvider` method calls will be invoked and verified by the tests to
ensure SPI layer is working.
4.  Execute the `disable.alternate.jsonp.provider` target using Ant: +
[source,oac_no_warn]
----
cd <TS_HOME>/bin
ant disable.alternate.jsonp.provider
----
The `disable.alternate.jsonp.provider` target disables the alternate
test provider.

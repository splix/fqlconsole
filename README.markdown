FQL Console
===========

Simle tool for querying Facebook's FQL queries. Useful for testing purposes when you are developing an app
for Facebook.

Downloading
-----------

Note: you have to install Java 1.6+ before using FQLConsole.

Download binary pacakged as ZIP from download section ther, unpack and run it's
`fqlconsole.sh` or `fqlconsole.bat`, depending of your OS.

And then open http://localhost:8081/fqlconsole using your browser.

Running from sources
--------------------

Note: it requires Maven 2 for using it from sources.

Clone it as
`git clone git@github.com:splix/fqlconsole.git`

and run as:
`mvn package jetty:run`

Now you can open http://localhost:8080/fqlconsole using your browser.
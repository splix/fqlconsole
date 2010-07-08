@echo off

REM I have no any Windows system where i can test this, so, if you found a bug,
REM or it simply doesn't runs, please fix it and send me fixed copy of this .bat
REM to igor@artamonov.ru. Thanks.

set FQLCONSOLE=.
set LIB="%FQLCONSOLE%/libs"
set JARS=%LIB%/jetty-6.1.19.jar;%LIB%/jetty-util-6.1.19.jar;%LIB%/servlet-api-2.5-20081211.jar;%LIB%/start-6.1.19.jar

start $JAVA_HOME/bin/java -classpath "$JARS" org.mortbay.start.Main jetty.xml

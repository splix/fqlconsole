#!/bin/sh

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set FQLCONSOLE if not already set
if [ -z "$FQLCONSOLE" ]; then
  FQLCONSOLE=`cd "$PRGDIR" ; pwd`
fi

LIB="$FQLCONSOLE/libs/"
JARS=$LIB/jetty-6.1.19.jar:$LIB/jetty-util-6.1.19.jar:$LIB/servlet-api-2.5-20081211.jar:$LIB/start-6.1.19.jar

$JAVA_HOME/bin/java -classpath "$JARS" org.mortbay.start.Main jetty.xml

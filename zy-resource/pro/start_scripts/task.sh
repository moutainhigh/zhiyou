#!/bin/bash

set -e

JAVA_OPS="
-server
-Xms1g
-Xmx1g
-XX:+UseG1GC
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/heapError/
"

export JAVA_HOME=/usr/local/jdk1.8.0_102
export PATH=:$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

PID=`jps -l | grep zy-task | grep -v grep | cut -d' ' -f 1`

if [ -n "$PID" ]; then
echo "kill -9 pid : $PID"
kill -9 $PID
fi

rm -rf /opt/optspace/jar/zy-service.impl.log

nohup java $JAVA_OPS -jar /opt/workspace/jar/zy-task.jar >/opt/workspace/jar/zy-task.log 2>&1 &
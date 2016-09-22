#!/bin/bash

set -e

JAVA_OPS="
-Djava.security.egd=file:/dev/./urandom
-server
-Xms8g
-Xmx8g
-Xss256k
-XX:+UseCompressedOops
-XX:+AggressiveOpts
-XX:+UseFastAccessorMethods
-XX:+UseStringDeduplication
-XX:+UseG1GC
-XX:+OptimizeStringConcat
-XX:CICompilerCount=8
-XX:+ScavengeBeforeFullGC
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/opt/heapError/
"

export JAVA_HOME=/usr/local/jdk1.8.0_102
export PATH=:$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

PID=`jps -l | grep zy-service-impl | grep -v grep | cut -d' ' -f 1`

if [ -n "$PID" ]; then
echo "kill -9 pid : $PID"
kill -9 $PID
fi

rm -rf /opt/optspace/jar/zy-service.impl.log

nohup java $JAVA_OPS -jar /opt/workspace/jar/zy-service-impl.jar >/opt/workspace/jar/zy-service-impl.log 2>&1 &
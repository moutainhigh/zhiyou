#!/bin/bash

set -e

export JAVA_HOME=/usr/local/jdk1.8.0_102
export PATH=:$PATH:$JAVA_HOME/bin
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

if [ "X$1" = "X" ]; then
        echo "arg1 tomcat path"
       	exit 1
fi

if [ "$1" -eq "/" ]; then
	echo "tomcat path cannot be /"
	exit 1
fi

if [ "X$2" = "X" ]; then
        echo "arg2 tomcat dir name"
        exit 1
fi

if [ "X$3" = "X"  ]; then
        echo "arg3 is tomcat war file"
        exit 1
fi

if [ "X$4" = "X" ] ;then
        echo "arg4 is tomca workspace path"
        exit 1
fi

PID=`jps -v |grep -v Jps | grep $2 | cut -d' ' -f 1`
if [ -z "$PID" ] ; then
        echo " tomcat is shutdown!!"
else
                echo "killd -9 $2 tomcat pid is [$PID]"
		kill -9 $PID
		sleep 2s
fi

echo " clean tomcat catalina.out"

rm -rf  $1/logs/*
echo "clean workapce [$4/*]"
rm -rf $4/*
unzip $3 -d $4/
echo "started tomcat ,app name is $2"
$1/bin/startup.sh
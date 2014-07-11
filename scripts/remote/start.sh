#!/bin/bash

####
#
# Example usage: ./start.sh -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar
#
####

set -e

usage() {
    echo " USAGE:
    start.sh
    [-p || --port <port>]
    [-j || --jar <jar name>]
    [-e || --env <environment>]
    [-l || --logDirectory <log directory>]
    [-n || --name <name of server>]
    "
    exit 1
}

now=$(date +"%Y%m%d_%H%M")
port=
jar=
logDirectory=
environment=
name=

realargs="$@"
while [ $# -gt 0 ]; do
    case "$1" in
        -j | --jar)
            jar=$2
            shift
        ;;
        -p | --port)
            port=$2
            shift
        ;;
        -e | --env)
            environment=$2
            shift
        ;;
        -l | --logDirectory)
            logDirectory=$2
            shift
        ;;
        -n | --name)
            name=$2
            shift
        ;;
        *)
            usage
        ;;
    esac
    shift
done
set -- $realargs

if [ -z "$jar" ] || [ -z "$port" ] || [ -z "$logDirectory" ] || [ -z "$environment" ]; then
    usage
    return
fi

echo "==============================="
echo "START"
echo "==============================="

pid=`ps ax | grep java | grep "ratpack" | grep "$name" | awk '{print $1}'`
if [ -n "$pid" ]; then
    kill -9 $pid
    echo "[STOP   ] Existing instance is killed [pid:$pid]"
fi

pid=`ps ax | grep java | grep "ratpack" | grep "$name" | awk '{print $1}'`
if [ -n "$pid" ]; then
    echo "[ABORTED] Process [id:$pid] cannot be killed. Aborted."
    exit 1
fi

echo "[START  ] Service is starting"
nohup /opt/java/bin/java -Dratpack.port=$port -DlogDirectory=$logDirectory -Denvironment=$environment -Dname=$name -jar /opt/$jar 2>/dev/null &
sleep 10
echo "[START  ] Service started"

cp "/opt/${jar}" "/opt/archive/packages/${now}-${jar}"
echo "[ARCHIVE] Package is archived to /opt/archive/packages/ directory"

cp -r "$logDirectory" "/opt/archive/logs/${now}-logs"
echo "[ARCHIVE] Logs are archived to /opt/archive/logs/ directory"

pid=`ps ax | grep java | grep "ratpack" | grep "$name" | awk '{print $1}'`
if [ -z "$pid" ]; then
    echo "[ABORTED] Process [id:$pid] cannot be killed. Aborted."
    exit 1
else
    echo "[DONE   ] Process [id:$pid] is started. Done."
fi

echo "==============================="

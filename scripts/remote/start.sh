#!/bin/bash

####
#
# Example usage: /start.sh -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar
#
####

set -e

usage() {
    echo "USAGE: start.sh [-p | --port <port>] [-j | --jar <jar name>]"
    exit 1
}

port=
jar=

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
        *)
            usage
        ;;
    esac
    shift
done
set -- $realargs

if [ -z "$jar" ] || [ -z "$port" ]; then
    usage
    return
fi

echo "==============================="
echo "DEPLOYMENT"
echo "==============================="

pid=`ps ax | grep java | grep "ratpack" | awk '{print $1}'`
if [ pid ] && [[ $pid != '' ]]; then
    kill -HUP $pid
    echo "[CLEANUP] Existing instance is killed [pid:$pid]"
fi

echo "[START  ] Service is starting"
nohup /opt/java/bin/java -Dratpack.port=$port -jar /opt/$jar 2>/dev/null &
sleep 10
echo "[DONE   ] Service started"

echo "==============================="

#!/bin/bash

####
#
# Example usage:
# ./start.sh -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-all.jar -e dev -l /opt/logs/repository -n octopus-repository-service
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

echo "==============================="
echo "START"
echo "==============================="

if [ -z "$jar" ] || [ -z "$port" ] || [ -z "$logDirectory" ] || [ -z "$environment" ] || [ -z "$name" ]; then
    echo "[ABORTED] Mandatory fields are missing. Please check the usage."
    usage
    return
fi

if [ ! -f "/opt/shared/to_deploy/$jar" ]; then
    echo "UberJar does not exist. Build it with \"gradle shadowJar\" command and deploy."
    echo "Quited"
    exit 0
fi

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

mkdir -p "$logDirectory"
echo "[START  ] Service is starting"
nohup /opt/java/bin/java -Dratpack.port=$port -DlogDirectory=$logDirectory -Denvironment=$environment -Dname=$name -jar /opt/shared/to_deploy/$jar 2>/dev/null &
sleep 10
echo "[START  ] Service started"

mkdir -p "/opt/archive/packages/"
cp "/opt/shared/to_deploy/${jar}" "/opt/archive/packages/${now}-${jar}"
echo "[ARCHIVE] Package is archived to /opt/archive/packages/ directory"

mkdir -p "/opt/archive/logs/"
cp -r "$logDirectory" "/opt/archive/logs/${now}-logs"
echo "[ARCHIVE] Logs are archived to /opt/archive/logs/ directory"

pid=`ps ax | grep java | grep "ratpack" | grep "$name" | awk '{print $1}'`
if [ -z "$pid" ]; then
    echo "[ABORTED] Process seems missing. Aborted."
    exit 1
else
    echo "[DONE   ] Process [id:$pid] is started. Done."
fi

echo "==============================="

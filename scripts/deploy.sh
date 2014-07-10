#!/bin/bash

####
#
# Run this script from the root folder of your project
#
# Example usage:
#    Deploy via vagrant: ./scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -v 169.254.14.248 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar
#    Deploy directly   : ./scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar
#    Restart directly  : ./scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar -r
#
####

set -e

usage() {
    echo "USAGE: deploy.sh
     [-i || --ip <server ip>]
     [-u || --username <username>]
     [-p || --port <port>]
     [-v || --vagrant <vagrant ip>]
     [-r || --restart]
     [-j || --jar <name of fatjar>]"
    exit 1
}

fatJarName=
ip=
userName=
port=
vagrant=
restart=

realargs="$@"
while [ $# -gt 0 ]; do
    case "$1" in
        -i | --ip)
            ip=$2
            shift
        ;;
        -u | --username)
            userName=$2
            shift
        ;;
        -p | --port)
            port=$2
            shift
        ;;
        -v | --vagrant)
            vagrant=$2
            shift
        ;;
        -j | --jar)
            fatJarName=$2
            shift
        ;;
        -r | --restart)
            restart=true
        ;;
            *)
            usage
        ;;
    esac
    shift
done
set -- $realargs

if [ -z "$ip" ] || [ -z "$userName" ] || [ -z "$port" ] || [ -z "$fatJarName" ]; then
    usage
    return
fi

fatJarPath="build/libs/$fatJarName"

if [ ! -f "$fatJarPath" ]; then
    echo "FatJar does not exist. Build it with \"gradle fatjar\" command."
    echo "Quited"
    exit 0
fi

if [ -z "$vagrant" ];then
    if [ -z "$restart" ]; then
        scp $fatJarPath $userName@$ip:/opt
    fi
    ssh -t -x $userName@$ip "/opt/start.sh -p $port -j fatJarName"
else
    if [ -z "$restart" ]; then
        scp $fatJarPath vagrant@$vagrant:
        ssh -t -x vagrant@$vagrant "scp $fatJarName $userName@$ip:/opt"
    fi
    ssh -t -x vagrant@$vagrant "ssh -t -x $userName@$ip \"/opt/start.sh -p $port -j $fatJarName\""
fi

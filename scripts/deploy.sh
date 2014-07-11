#!/bin/bash

set -e

usage() {
    echo "
NAME
      deploy.sh - Deploys Octopus3 microservice jars to remote servers

SYNOPSIS
      deploy.sh
            [-i || --ip <server ip>]
            [-u || --username <username>]
            [-p || --port <port>]
            [-v || --vagrant <vagrant ip>]
            [-e || --env <environment>]
            [-l || --logDirectory <log directory>]
            [-r || --restart]
            [-j || --jar <name of fatjar>]
            [-n || --name <name of the application>]

DESCRIPTION
      deploy.sh is a Bash script for deploying microservices to remote servers and run them.

OPTIONS
      -i, --ip
          IP of the remote server
      -u, --username
          Username of the server
      -p, --port
          Port of ratpack
      -v, --vagrant
          If you are deploying through vagrant, this is the IP of your vagrant instance
      -l, --logDirectory
          The path where all log files are stored
      -e, --environment
          Environment to set. It is one among dev, testqa, staging or production
      -n, --name
          Name of the application, like octopus3-repository-service
      -r, --restart
          Defines if this is just a restart or not. Restart means we don't deploy the package, just restart it
      -h, --help
          Prints usage information for help.

USAGE
      Run this script from the root folder of your project. For example, even if you are in octopus3-repository-service
      project folder, you can call the script like \"../octopus3-commons/scripts/deploy.sh\". I assume you already cloned
      octopus3-commons project.

      It is adviced to upload your public key to dev servers. In order to do that, follow the following steps.
      1) [Local machive] run \"ssh-keygen\" command and create your public key. If you want to connect to dev without password
         leave \"pass phrase\" empty when ssh-keygen asks.
      2) [Local machine] open ~/.ssh/id_rsa.pub file and copy the line.
      3) [Remote machine] login to a dev server. Open \"~/.ssh/authorized_keys\" file and paste the line you copied previously

      Now you should connect to dev server via ssh without entering any password. You should try these steps for each
      remote server one by one.

      Example usage:
        Deploy via vagrant: ../octopus3-commons/scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar -l /opt/logs/repository -e dev -n octopus3-repository-service -v 169.254.14.248
        Deploy directly   : ../octopus3-commons/scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar -l /opt/logs/repository -e dev -n octopus3-repository-service
        Restart directly  : ../octopus3-commons/scripts/deploy.sh -i 43.216.132.90 -u GWTSYN_dev_rw -p 9091 -j octopus3-repository-service-1.0-SNAPSHOT-fat.jar -l /opt/logs/repository -e dev -n octopus3-repository-service -r

DISCUSSIONS
      The script first uploads the file remote server and runs \"start.sh\" to make it deployed and started.
"
    exit 1
}

fatJarName=
ip=
userName=
port=
vagrant=
restart=
logDirectory=
environment=
name=

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

if [ -z "$ip" ] || [ -z "$userName" ] || [ -z "$port" ] || [ -z "$fatJarName" ] || [ -z "$logDirectory" ] || [ -z "$environment" ] || [ -z "$name" ]; then
    echo "[ABORTED] Mandatory fields are missing. Please check the usage."
    usage
    return
fi

echo "==============================="
echo "DEPLOYMENT"
echo "==============================="

fatJarPath="build/libs/$fatJarName"

if [ ! -f "$fatJarPath" ]; then
    echo "FatJar does not exist. Build it with \"gradle fatjar\" command."
    echo "Quited"
    exit 0
fi

if [ -z "$vagrant" ];then
    if [ -z "$restart" ]; then
        scp $fatJarPath $userName@$ip:/opt
        echo "[UPLOAD ] Package is uploaded to server"
    fi
    ssh -t -x $userName@$ip "/opt/start.sh -p $port -l $logDirectory -e $environment -n $name -j fatJarName"
    echo "[TRIGGER] Start script is triggered"
else
    if [ -z "$restart" ]; then
        scp $fatJarPath vagrant@$vagrant:
        echo "[UPLOAD ] Package is uploaded to vagrant"
        ssh -t -x vagrant@$vagrant "scp $fatJarName $userName@$ip:/opt"
        echo "[UPLOAD ] Package is uploaded to server"
    fi
    echo "[TRIGGER] Start script is triggered"
    ssh -t -x vagrant@$vagrant "ssh -t -x $userName@$ip \"/opt/start.sh -p $port -l $logDirectory -e $environment -n $name -j $fatJarName\""
fi

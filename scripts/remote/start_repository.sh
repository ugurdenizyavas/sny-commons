#!/bin/bash
WORKING_DIR="/opt/shared/scripts"

sh ${WORKING_DIR}/start.sh -j octopus3-repository-service-app.jar -e dev -p 9091 -l /opt/logs/repository -n octopus3-repository-service


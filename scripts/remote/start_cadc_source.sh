#!/bin/bash
WORKING_DIR="/opt/shared/scripts"

sh ${WORKING_DIR}/start.sh -p 9092 -j octopus3-cadc-source-service-app.jar -e dev -l /opt/logs/cadc-source -n octopus3-cadc-source-service


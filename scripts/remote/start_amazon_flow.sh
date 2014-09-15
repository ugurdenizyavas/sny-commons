#!/bin/bash
WORKING_DIR="/opt/shared/scripts"


sh ${WORKING_DIR}/start.sh -p 9093 -j octopus3-amazon-flow-service-app.jar -e dev -l /opt/logs/amazon-flow -n octopus3-amazon-flow-service


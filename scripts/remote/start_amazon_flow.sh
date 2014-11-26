#!/bin/bash
WORKING_DIR="/opt/shared/scripts"
TEMP_DIR="/opt/shared/repository/temp/amazon_feed"

rm -rf ${TEMP_DIR}

echo "Temp dir ${TEMP_DIR} is deleted"

sh ${WORKING_DIR}/start.sh -p 9093 -j octopus3-amazon-flow-service-app.jar -e dev -l /opt/logs/amazon-flow -n octopus3-amazon-flow-service


#!/bin/bash
WORKING_DIR="/opt/shared/scripts"

sh ${WORKING_DIR}/start.sh -p 9094 -j octopus3-flixmedia-flow-service-app.jar -e dev -l /opt/logs/flixmedia-flow -n octopus3-flixmedia-flow-service


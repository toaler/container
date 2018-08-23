#!/bin/bash

# run.sh <war>
JAVA_HOME=/Users/btoal/blt/tools/Darwin/jdk/jdk1.8.0_66_x64
DRIVER_HOME=/Users/btoal/git/container/container-driver
LOG=`date +%Y%m%d%H%M%S`.log
#TODO GRAB PIDFILE NAME FROM $1
PIDFILE=/tmp/container-example-webapp-0.0.1-SNAPSHOT.pid

start() {
  if pgrep -F ${PIDFILE}
  then
    echo "Application already running"
  else
    nohup ${JAVA_HOME}/bin/java -Dwc.context.path=/ -Xbootclasspath/p:${DRIVER_HOME}/lib/alpn-boot-8.1.11.v20170118.jar -cp ${DRIVER_HOME}/target/container-driver-0.0.1-SNAPSHOT.jar:${DRIVER_HOME}/lib/* container.driver.Main $1 | tee ${LOG} &
    echo "Started application"
  fi 
}

stop() {
  curl -X POST 'http://localhost:8888/shutdown?token=elmo'
}

status() {
  curl http://localhost:8888/v1/healthcheck/status
  echo
}

### main logic ###
case "$1" in
start)
  start $2
  ;;
stop)
  stop
  ;;
status)
  status
  ;;
restart|reload|condrestart)
  stop
  start $2
  ;;
*)
  echo $"Usage: $0 {start|stop|restart|reload|status}"
  exit 1
esac
exit 0

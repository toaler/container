#!/bin/bash

# run.sh <war>
JAVA_HOME=/Users/btoal/blt/tools/Darwin/jdk/jdk1.8.0_66_x64
DRIVER_HOME=/Users/btoal/git/container/container-driver
#TODO GRAB PIDFILE NAME FROM $1
PIDFILE=/tmp/container-example-webapp-0.0.1-SNAPSHOT.pid
CMD="${JAVA_HOME}/bin/java -Dwc.context.path=/ -Xbootclasspath/p:${DRIVER_HOME}/lib/alpn-boot-8.1.11.v20170118.jar -cp ${DRIVER_HOME}/target/container-driver-0.0.1-SNAPSHOT.jar:${DRIVER_HOME}/lib/* container.driver.Main"

start() {
  if pgrep -F ${PIDFILE}
  then
    echo "Application already running"
  else
    if [ "$2" = true ]
    then
      nohup $CMD $1 &
      echo "Started application in background, see status to validate state and log to troubleshoot"
    else
      $CMD $1
    fi
  fi 
}

stop() {
  if curl --connect-timeout 5 --max-time 5 -X POST 'http://localhost:8888/shutdown?token=elmo'; then
    echo "Issued shutdown command successfully, app shutdown will occur asynchronously"
    # TODO write code to wait for process to be killed for X seconds before
    # calling SIGTERM then SIGKILL
  else
    pid=`cat ${PIDFILE}`
    echo "Attempt to call shutdown endpoint timed out, kill -9 ${pid} will be issued"
    kill -9 ${pid}
  fi
}

status() {
  curl http://localhost:8888/v1/healthcheck/status
  echo
}

### main logic ###
case "$1" in
startfg)
  start $2 false
  ;;
start)
  start $2 true
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

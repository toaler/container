#!/bin/bash

set +xv

# run.sh <war>
PORT=8888
DRIVER_HOME=${HOME}/git/container/container-driver
#TODO GRAB PIDFILE NAME FROM $1
PIDFILE=/tmp/container-example-webapp-0.0.1-SNAPSHOT.pid
CMD="${JAVA_HOME}/bin/java -Dwc.context.path=/ --patch-module java.base=target/libs/alpn-boot-8.1.6.v20151105.jar --add-exports java.base/org.eclipse.jetty.alpn=ALL-UNNAMED -cp ${DRIVER_HOME}/target/container-driver-0.0.1-SNAPSHOT.jar:${DRIVER_HOME}/lib/* container.driver.Main"
TOKEN=elmo

declare -a levels=([DEBUG]=0 [INFO]=1 [WARN]=2 [ERROR]=3)
script_logging_level="DEBUG"

error () {
  local log_message=$1
  log "$log_message" "ERROR"
}

info() {
  local log_message=$1
  log "$log_message" "INFO"
}

debug() {
  local log_message=$1
  log "$log_message" "DEBUG"
}

log(){ 
  local log_message=$1
  local log_priority=$2

  #check if level exists
  [[ ${levels[$log_priority]} ]] || return 1

  #check if level is enough
  (( ${levels[$log_priority]} < ${levels[$script_logging_level]} )) && return 2

  printf '%-5s : %s\n' "${log_priority}" "${log_message}"
}

start() {
	if [ -f "${PIDFILE}" ] && [ $(pgrep -F "${PIDFILE}") ]
  then
    info "Application already running"
  else
    if [ "$2" = true ]
    then
      nohup $CMD $1 &
      info "Started application in background, see status to validate state and log to troubleshoot"
    else
      $CMD $1
    fi
  fi 
}

is_process_alive() {
  if ps -p $1 > /dev/null
  then
    echo 1;
  else
    echo 0;
  fi
}

graceful_shutdown() {
  pid=$1
  attempts=$2

  info "Attempting 'kill -SIGTERM ${pid}'"
  kill -SIGTERM ${pid}
  wait_for_process $pid $attempts
  if [  $? = "0" ]
  then
    info "Process ${pid} killed by SIGTERM, exiting."
    return 0
  fi

  info "Attempting 'kill -SIGKILL ${pid}'"
  kill -SIGKILL ${pid}
  wait_for_process $pid $attempts
  if [ $? = "0" ]
  then
    info "Process ${pid} killed by SIGKILL, exiting."
    return 0
  fi

  error "Could not kill ${pid}, exiting"
  return 1
}

wait_for_process() {
  i=$2;
  while [ "$i" -ne 0 ]
  do
    is_alive=$(is_process_alive $1)
    debug "is_alive = ${is_alive}"
    if [ ${is_alive} = "0" ]
    then
      info "$1 is dead"
      return 0; 
    fi
    i=$((i-1))
    sleep 1
  done

  return 1
}

stop() {
  pid=`cat ${PIDFILE}`

  if curl --connect-timeout 5 --max-time 5 -X POST "http://localhost:${PORT}/shutdown?token=${TOKEN}"; then
    info "Issued shutdown command successfully, app shutdown will occur asynchronously"
    # TODO write code to wait for process to be killed for X seconds before
    # calling SIGTERM then SIGKILL

    wait_for_process $pid 5 
    if [  $? = "0" ]
    then
      info "Process ${pid} killed by SIGTERM, exiting."
      return 0
    fi

    info "Application still running even after shutdown management API attempt, start graceful shutdown"
    graceful_shutdown ${pid} 5 
  else
    info "Attempt to call shutdown endpoint timed out, kill -9 ${pid} will be issued"
    graceful_shutdown ${pid} 5 
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
  info "process successfully stopped"
  ;;
status)
  status
  ;;
restart|reload|condrestart)
  stop
  start $2
  ;;
*)
  info $"Usage: $0 {start|stop|restart|reload|status}"
  exit 1
esac
exit 0

#!/bin/bash

java  -Xbootclasspath/p:lib/alpn-boot-8.1.11.v20170118.jar  -cp target/container-driver-0.0.1-SNAPSHOT.jar:lib/* container.driver.Main

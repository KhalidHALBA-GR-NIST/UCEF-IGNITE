#!/bin/bash
rootdir=`pwd`

cd $rootdir/AV_generated
mvn clean install

cd $rootdir/AV_deployment
mvn clean install

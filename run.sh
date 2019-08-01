#!/bin/bash

##### GLOBAL VARIABLES ######
sudo ifconfig enp0s8 down
root_directory=/home/vagrant/Desktop/AVCOA
logs_directory=$root_directory/logs

fedmgr_host=127.0.0.1
fedmgr_port=8083

timestamp=`date +"%F_%T"`

##### FUNCTION DEFINITIONS ######

function getNumberJoined {
    if (( $# != 1 ))
    then
        echo bad syntax: getNumberJoined federateType
        exit 1
    fi
    federateList=$(curl -s X GET http://$fedmgr_host:$fedmgr_port/federates -H "Content-Type: application/json")
    # JSON Query:
    #   .[] = process all values in the input object
    #   select(...) = exclude entries for resigned federates (resignTime defined) and federates that are not the desired TYPE
    #   enclosing [ ] = combine the result from the above queries into a single JSON array
    #   length = count the number of elements in the combined array
    echo $federateList | jq --arg TYPE "$1" '[.[] | select(.resignTime == null and .federateType == $TYPE)] | length'
}

function waitUntilJoined {
    if (( $# != 2 ))
    then
        echo bad syntax: waitUntilJoined federateType expectedNumber
        exit 1
    fi
    federateType=$1
    expectedNumber=$2

    if (( $expectedNumber < 1 ))
    then
        echo "illegal argument: expectedNumber of federates cannot be $expectedNumber"
        exit 1
    fi

    printf "Waiting for $expectedNumber instances of $federateType to join.."
    while (( $(getNumberJoined $federateType) != $expectedNumber))
    do
        printf "."
        sleep 5
    done
    printf "\n"
}

##### SCRIPT START #####

nc -z $fedmgr_host $fedmgr_port
if [ $? -eq 0 ]; then
    echo Cannot start the federation manager on port $fedmgr_port
    exit 1
fi

if [ ! -d $logs_directory ]; then
    echo Creating the $logs_directory directory
    mkdir $logs_directoryf
fi

# run the federation manager
cd $root_directory/AV_deployment
xterm -fg white -bg black -l -lf $logs_directory/federation-manager-${timestamp}.log -T "Federation Manager" -geometry 140x40+0+0 -e "export CPSWT_ROOT=`pwd` && mvn exec:java -P FederationManagerExecJava" &

printf "Waiting for the federation manager to come online.."
until $(curl -o /dev/null -s -f -X GET http://$fedmgr_host:$fedmgr_port/fedmgr); do
    printf "."
    sleep 5
done
printf "\n"

curl -o /dev/null -s -X POST http://$fedmgr_host:$fedmgr_port/fedmgr --data '{"action": "START"}' -H "Content-Type: application/json"








# run the other federates
cd $root_directory/AV_generated/AV-java-federates/AV-impl-java/ABS/target
xterm -fg green -bg black -l -lf $logs_directory/ABS-${timestamp}.log -T "ABS" -geometry 140x40+180+60 -e "java  -Dlog4j.configurationFile=conf/log4j2.xml -jar ABS-0.1.0-SNAPSHOT.jar  -federationId=AV -configFile=conf/ABSConfig.json" &
waitUntilJoined ABS 1

cd $root_directory/AV_generated/AV-java-federates/AV-impl-java/BMS/target
xterm -fg orange -bg black -l -lf $logs_directory/BMS-${timestamp}.log -T "BMS" -geometry 140x40+180+60 -e "java  -Dlog4j.configurationFile=conf/log4j2.xml -jar BMS-0.1.0-SNAPSHOT.jar  -federationId=AV -configFile=conf/BMSConfig.json" &
waitUntilJoined BMS 1



cd $root_directory/AV_generated/AV-java-federates/AV-impl-java/VCU/target
xterm -fg cyan -bg black -l -lf $logs_directory/VCU-${timestamp}.log -T "VCU" -geometry 140x40+180+60 -e "java  -Dlog4j.configurationFile=conf/log4j2.xml -jar VCU-0.1.0-SNAPSHOT.jar  -federationId=AV -configFile=conf/VCUConfig.json" &
waitUntilJoined VCU 1



cd $root_directory/AV_generated/AV-java-federates/AV-impl-java/MCU/target
xterm -fg red -bg black -l -lf $logs_directory/MCU-${timestamp}.log -T "MCU" -geometry 140x40+180+60 -e "java  -Dlog4j.configurationFile=conf/log4j2.xml -jar MCU-0.1.0-SNAPSHOT.jar  -federationId=AV -configFile=conf/MCUConfig.json
" &
waitUntilJoined MCU 1




cd $root_directory/ucef-database/target
xterm -fg yellow -bg black -l -lf $logs_directory/Database-${timestamp}.log -T "Database" -geometry 140x40+180+60 -e "java -Dlog4j.configurationFile=conf/log4j2.xml -Djava.net.preferIPv4Stack=true -jar Database-0.0.1-SNAPSHOT.jar conf/Database.json" &
waitUntilJoined Database 1



sudo ifconfig enp0s8 up
printf " UCEF-IGNITE interface up \n"






# terminate the simulation
read -n 1 -r -s -p "Press any key to terminate the federation execution..."
printf "\n"

curl -o /dev/null -s -X POST http://$fedmgr_host:$fedmgr_port/fedmgr --data '{"action": "TERMINATE"}' -H "Content-Type: application/json"

printf "Waiting for the federation manager to terminate.."
while $(curl -o /dev/null -s -f -X GET http://$fedmgr_host:$fedmgr_port/fedmgr); do
   printf "."
   sleep 5
done
printf "\n"


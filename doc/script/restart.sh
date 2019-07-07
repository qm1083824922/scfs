#!/bin/bash
app_name='apache-tomcat-scfs-prod'

process_list=`ps -ef |grep "/$app_name" |grep java |grep -v "grep" |awk '{print $2}'`
if [ -z "$process_list" ]; then
    echo "$app_name not running..."
fi
for pid in ${process_list}
do
    isRunning=`ps -p $pid --no-header -o comm|grep "java" |wc -l`
    if [ "$isRunning" -gt 0  ]; then
        echo "current pid is:$pid, will kill"
        kill -9 ${pid}
    else
        echo "$app_name not running..."
    fi
done

sleep 2
echo "Stop scfs success"

cd /opt/soft/apache-tomcat-scfs-prod/bin
./startup.sh

sleep 2
echo "Restart scfs success"

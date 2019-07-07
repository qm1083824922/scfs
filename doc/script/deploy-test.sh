#!/bin/bash
app_name='apache-tomcat-scfs-test'

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
echo "Stop scfs-test success!"

cd /usr/local/code/scfs
git pull
mvn clean package -Ptest -Dmaven.test.skip=true
rm -rf /opt/soft/apache-tomcat-scfs-test/webapps/scfs
cp -R /usr/local/code/scfs/scfs-web/target/scfs-web-1.0.0-SNAPSHOT /opt/soft/apache-tomcat-scfs-test/webapps
mv /opt/soft/apache-tomcat-scfs-test/webapps/scfs-web-1.0.0-SNAPSHOT /opt/soft/apache-tomcat-scfs-test/webapps/scfs
sed -i "s@https://47.96.224.13/scfs/@http://47.96.224.13:8001/scfs/@g" /opt/soft/apache-tomcat-scfs-test/webapps/scfs/front/js/common.js 

cd /opt/soft/apache-tomcat-scfs-test/bin
./startup.sh
sleep 2
echo "Start scfs-test success!"

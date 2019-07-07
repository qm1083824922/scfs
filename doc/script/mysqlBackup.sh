#!/bin/bash
# fileName：mysqlBackup.sh
# create: 2018/07/10
# 每天的3点零1分执行该脚本(加 & 为后台执行)
# crontab -e
# 1 3 * * * /bin/bash /usr/local/backup/mysqlBackup.sh &
#mysql安装路径
mysql_dir=/usr/bin
#设置用户名和密码
mysql_user=root
mysql_passwd=XXX
#备份路径
backup_base_dir=/opt/soft/backup/
backup_dir=/opt/soft/backup/$(date +%Y%m%d)
#存放路径
save_dir=/data/backup/db
#备份数据库
backup_db=scfs
#备份文件夹
current_day=$(date +%Y%m%d)
#备份的时间
current_time=$(date +%Y%m%d-%H:%M:%S)
if [ ! -d "$backup_dir" ]; then
    mkdir -p  "$backup_dir"
fi
mysqldump -u$mysql_user -p$mysql_passwd $backup_db > $backup_dir/scfs_$current_time.sql
cd $backup_base_dir
if [ ! -d "$save_dir" ]; then
    mkdir -p "$save_dir"
fi
tar -zcvf $current_day.tar.gz $current_day/
#移动文件到nfs
mv $current_day.tar.gz $save_dir
rm -rf $backup_base_dir
#删除15天前的备份
find $save_dir -mtime +15 -name "*.*" -exec rm -rf {} \;
bye
EOF
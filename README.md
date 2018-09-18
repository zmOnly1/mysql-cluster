wget -i -c http://dev.mysql.com/get/mysql57-community-release-el7-10.noarch.rpm
yum localinstall mysql57-community-release-el7-10.noarch.rpm
yum -y install mysql-community-server


https://repo.mysql.com/yum/mysql-5.7-community/el/7/x86_64/

mysql-community-common-5.7.23-1.el7.x86_64.rpm
mysql-community-libs-5.7.23-1.el7.x86_64.rpm                                                                                   
mysql-community-libs-compat-5.7.23-1.el7.x86_64.rpm                                                                            
mysql-community-client-5.7.23-1.el7.x86_64.rpm
mysql-community-server-5.7.23-1.el7.x86_64.rpm                                                                                 

systemctl start mysqld
grep 'temporary password' /var/log/mysqld.log

mysql -u root -p

SET PASSWORD = PASSWORD('Qq123456$');

show variables like '%password%';

0 or LOW
1 or MEDIUM
2 or STRONG

set global validate_password_policy=0;
set global validate_password_length=1;

alter user 'root'@'localhost' identified by 'root';


1.create database mytest;
2.drop database mytest;
3.create database mytest default charset utf8;
4.show create database mytest;
5.create user 'test'@'%' identified by 'test';
6.select user,host from mysql.user;
7.alter user 'test'@'%' identified by '1234';
8.grant replication slave on *.* to 'test'@'%';
9.revoke all on *.* from 'test'@'%';  
10.drop user 'test';
11.show databases;
12.create table user (id int primary key auto_increment, name vachar(10));;
13.show tables;
14.desc user;
15.show create table user\G;
16.drop table user;
17.insert into user (name) values ('hello');
18.select * from user;
19.update user set name='hello4' where id=5;
20.delete from user where id=1;

21.mysqldump -uroot -p mytest > /root/mysqlbak.sql
22.mysql -uroot -p mytest < /root/mysqlbak.sql

create user 'repl'@'%' identified by 'repl';
grant privilege_name on *.* to usernmae@'host' identified by 'password';

Error log
General query log
Slow query log
Binary log

开启bin-log
show variables like '%log_bin%';

vim /etc/my.cnf
server-id=1
log-bin=/var/lib/mysql/mysql-bin

ls /var/lib/mysql
mysql-bin.00001
mysql-bin.index

查看binlog
mysqlbinlog mysql-bin.00001

show binlog events;
show binlog events in 'filename';

事件头
事件体

每次重启会调用flush logs，生成一个新的binlog

show master status;查看日志当前状态
show master logs;查看所有的日志文件
reset master;清空所有的日志文件

1.配置主节点
1.1：创建用户，赋予权限 repl reply replication slave
1.2：开启binlog日志
2.配置从节点
1.配置同步日志
2.指定主节点的ip，端口，用户。。。
3.启动从节点

log-bin=

server-id=
re lay-log=
change master to master_host='192.168.1.102' ,master_port=3306 ,master_user='repl',master_password='repl',master_log_file='mysql-bin.000001',master_log_pos=0;
start slave;

show slave status\G;
Slave_IO_Runnint=yes;
Slave_SQL_Runnint=yes;

cd /var/lib/mysql
cat auto.cnf
server-uuid集群里不能重复 

1.
yum install -y gcc-c++
yum install openssl openssl-devel

2. ./configure --prefix=/usr/local/keepalived

3. make && make install

4.路径：
配置：/usr/local/etc/keepalived
执行：/usr/local/sbin/keepalived

5.启动
./keepalived -D -f /usr/local/etc/keepalived/keepalived.conf

发邮件25
yum install postfix*

vim /etc/postfix/main.cf 
myhostname=mail.yzy.cn
mydomain=yzy.cn
myorigin=$myhostname
myorigin=$mydomain

inet_interfaces=all
mydestination=$myhostname, $mydomain, localhost
mynetworks=192.168.1.0/28, 127.0.0.0/8
replay_domains=$mydestination

收邮件110
yum install dovecot

vim /etc/dovecot/devecot.conf
protocols=imap pop3 lmtp

useradd user1
passwd user1

useradd user2
passwd user2

service iptables status
setenforce 0

vim /etc/selinux/config
SELINUX=disabled
reboot

telnet ip port
mail from:user1@yzy.cn
rcpt to:user2@yzy.cn
data 开始编辑发送数据
send data 要发送的数据
. 结束编辑发送的数据
quit 退出

su -user2 切换用户
mail 查看接收到的邮件
1 查看全文
d 删除邮件

netstat -tunlp|grep 110

yum install nc
vim mysql.sh
#!/bin/bash
nc -w2 localhost 3306
if [ $? -ne 0 ]
then
	echo "mysql's 3306 port is down" | mail user1@yzy.cn -s "myql is down"
fi

vim keepalived.conf
vrrp_script chk_mysql{
	script "root/mysql.sh"
	interval 10
}
vrrp_instance VI_1{
	track_script{
		chk_mysql
	}
}

抢占优先级要大于50以上才能抢占
可以配置不抢占

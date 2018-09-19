1.为HAProxy创建用户和用户组，此例中用户和用户组都是"ha"。注意，如果想要让HAProxy监听1024以下的端口，则需要以root用户来启动

2.下载并解压

wget http://www.haproxy.org/download/1.7/src/haproxy-1.7.2.tar.gz
tar -xzf haproxy-1.7.2.tar.gz

3.编译并安装
make PREFIX=/opt/haproxy TARGET=linux2628
make install PREFIX=/opt/haproxy
PREFIX为指定的安装路径，TARGET则根据当前操作系统内核版本指定：

- linux22     for Linux 2.2
- linux24     for Linux 2.4 and above (default)
- linux24e    for Linux 2.4 with support for a working epoll (> 0.21)
- linux26     for Linux 2.6 and above
- linux2628   for Linux 2.6.28, 3.x, and above (enables splice and tproxy)
此例中，我们的操作系统内核版本为3.10.0，所以TARGET指定为linux2628

4.创建HAProxy配置文件
mkdir -p /opt/haproxy/conf
vi /opt/haproxy/conf/haproxy.cnf

global #全局属性
	daemon  #以daemon方式在后台运行
	nbproc 1
	maxconn 256  #最大同时256连接
	pidfile /opt/haproxy/conf/haproxy.pid  #指定保存HAProxy进程号的文件

defaults #默认参数
	mode tcp  #默认的模式mode{tcp|http|health},tcp是4层，http是7层，health只会返回OK
	retries 2	#两次连接失败就认为是服务器不可用，也可以通过后面设置
	option redispatch	#当serverId对应的服务器挂掉后，强制定向到其他健康的服务器
	option abortonclose	#当服务器负载很高的时候，自动结束掉当前队列处理比较久的链接
	maxconn 4096		#默认的最大链接数
	timeout connect 5000ms  #连接server端超时5s
	timeout client 30000ms  #客户端响应超时30s
	timeout server 30000ms  #server端响应超时30s
	log 127.0.0.1 local0 err	#[err warning info debug]
	balance	source			#负载均衡算法有8种，
							#1.roundrobin 轮询
							#2.static-rr 根据权重
							#3.leastconn 最少链接者优先
							#4.source 根据请求源IP
							#5.uri 根据请求uri
							#6.url_param 根据请求uri参数
							#7.hdr(name) 根据http请求头来锁定每一次HTTP请求
							#8.rdp-cookie(name) 根据cookie(name)来锁定并哈希每一次TCP请求
	
listen test1			#负载均衡的名字，可以任意
	bind 0.0.0.0:3306	#这里是监听的IP地址和端口，端口号可以在0-65535之间
	mode tcp
	#maxconn 4096
	#log 127.0.0.1 local0 debug
	server s1 192.168.1.102:3306 check port 3306	#check加心跳检测
	server s2 192.168.1.105:3306 check port 3306	#check加心跳检测
	
listen admin_status		#haproxy的监控管理界面
	bind 0.0.0.0:8888
	mode http
	status uri /test_haproxy
	status auth admin:admin
	
更加详细的配置会在后面章节中进行说明
注意：HAProxy要求系统的ulimit -n参数大于[maxconn*2+18]，在设置较大的maxconn时，注意检查并修改ulimit -n参数

5.启动
/opt/haproxy/sbin/haproxy -f /opt/haproxy/conf/haproxy.cnf


vi /etc/init.d/haproxy
#! /bin/sh
set -e

PATH=/sbin:/bin:/usr/sbin:/usr/bin:/home/ha/haproxy/sbin
PROGDIR=/home/ha/haproxy
PROGNAME=haproxy
DAEMON=$PROGDIR/sbin/$PROGNAME
CONFIG=$PROGDIR/conf/$PROGNAME.cfg
PIDFILE=$PROGDIR/conf/$PROGNAME.pid
DESC="HAProxy daemon"
SCRIPTNAME=/etc/init.d/$PROGNAME

# Gracefully exit if the package has been removed.
test -x $DAEMON || exit 0

start()
{
       echo -e "Starting $DESC: $PROGNAME\n"
       $DAEMON -f $CONFIG
       echo "."
}

stop()
{
       echo -e "Stopping $DESC: $PROGNAME\n"
       haproxy_pid="$(cat $PIDFILE)"
       kill $haproxy_pid
       echo "."
}

restart()
{
       echo -e "Restarting $DESC: $PROGNAME\n"
       $DAEMON -f $CONFIG -p $PIDFILE -sf $(cat $PIDFILE)
       echo "."
}

case "$1" in
 start)
       start
       ;;
 stop)
       stop
       ;;
 restart)
       restart
       ;;
 *)
       echo "Usage: $SCRIPTNAME {start|stop|restart}" >&2
       exit 1
       ;;
esac

exit 0

service haproxy start
service haproxy stop
service haproxy restart

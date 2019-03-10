# Commons-DataCache Component

## 1. MongoDB

### 1.1 MongoDB整合Spring的版本选择问题

#### 1.1.1 版本对比

| Mongo-Java-Driver | Spring-data-mongodb | Spring |
| ----------------- | ------------------- | ------ |
| 2.x               | 1.x                 | 4.x    |
| 3.x               | 2.x                 | 5.x    |

#### 1.1.2 Maven对比

##### 1.1.2.1 Mongo-Java-Driver 2.x

```xml
<!--MongoDB-Spring整合-->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongo-java-driver</artifactId>
    <version>2.14.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-mongodb</artifactId>
    <version>1.10.7.RELEASE</version>
</dependency>
```

##### 1.1.2.2 Mongo-Java-Driver 3.x

```xml
<!--MongoDB-Spring整合-->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongo-java-driver</artifactId>
    <version>3.5.0</version>
</dependency>
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-mongodb</artifactId>
    <version>2.0.1.RELEASE</version>
</dependency>
```

#### 1.1.3 Spring XML配置

##### 1.1.3.1 Mongo-Java-Driver 2.x

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mongo="http://www.springframework.org/schema/data/mongo"
       xsi:schemaLocation="
          http://www.springframework.org/schema/data/mongo
          http://www.springframework.org/schema/data/mongo/spring-mongo-1.0.xsd
          http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
 
    <mongo:mongo replica-set="${mongo.hostport}">
 
        <mongo:options connections-per-host="${mongo.connectionsPerHost}"
                       threads-allowed-to-block-for-connection-multiplier="${mongo.threadsAllowedToBlockForConnectionMultiplier}"
                       connect-timeout="${mongo.connectTimeout}"
                       max-wait-time="${mongo.maxWaitTime}"
                       auto-connect-retry="true"
                       socket-keep-alive="true"
                       socket-timeout="${mongo.socketTimeout}"
                       slave-ok="true"
                       write-number="1"
                       write-timeout="0"
                       write-fsync="true"
        />
    </mongo:mongo>
    <mongo:db-factory username="root" password="root" dbname="admin" mongo-ref="mongo"/>
 
    <bean id="MongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
        <constructor-arg name="mongoDbFactory" ref="mongoDbFactory"/>
    </bean>
</beans>
```

Properties文件：

```properties
mongo.hostport=127.0.0.1:27017
mongo.connectionsPerHost=8
mongo.threadsAllowedToBlockForConnectionMultiplier=4
#连接超时时间
mongo.connectTimeout=1000
#等待时间
mongo.maxWaitTime=1500
#Socket超时时间
mongo.socketTimeout=1500
#admin登录信息
mongo.username=root
mongo.password=root
#DataBaseName
#认证数据库名
mongo.authenticationDbname=admin
#要链接的数据库名
mongo.databaseName=test
```

##### 1.1.3.2 Mongo-Java-Driver 3.x

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mongo="http://www.springframework.org/schema/data/mongo"
       xsi:schemaLocation="http://www.springframework.org/schema/data/mongo
                http://www.springframework.org/schema/data/mongo/spring-mongo.xsd
                http://www.springframework.org/schema/beans
                 http://www.springframework.org/schema/beans/spring-beans.xsd">
 
 
    <mongo:mongo-client id="mongoClient" replica-set="${mongo.hostport}"
                        credentials="${mongo.username}:${mongo.password}@${mongo.authenticationDbname}">
        <mongo:client-options connections-per-host="${mongo.connectionsPerHost}"
                              threads-allowed-to-block-for-connection-multiplier="${mongo.threadsAllowedToBlockForConnectionMultiplier}"
                              connect-timeout="${mongo.connectTimeout}"
                              max-wait-time="${mongo.maxWaitTime}"
                              socket-timeout="${mongo.socketTimeout}"
        />
    </mongo:mongo-client>
 
    <mongo:db-factory dbname="${mongo.databaseName}" mongo-ref="mongoClient"/>
 
    <mongo:template db-factory-ref="mongoDbFactory"/>
 
</beans>
```

#### 1.1.4 Spring Annotation配置

##### 1.1.4.1 配置说明

本例只给出Spring5.x的配置（即Mongo-Java-Driver 3.x版本），这个版本有两种方式初始化MongoClient，即如下两个类：

- `com.mongodb.client.MongoClient`
  - `com.mongodb.client.MongoClients`可以创建MongoClient
  - 继承 `org.springframework.data.mongodb.config.AbstractMongoClientConfiguration` 可以注解配置相应的Mongodb Client
  - 本节采用此种方式，但是未继承AbstractMongoClientConfiguration类（因为本组件是一个数据缓存组件，需要适配多种缓存中间件，故而自己实现了，不过大致过程是一致的）
- `com.mongodb.MongoClient`
  - 继承自 `com.mongodb.Mongo`（已废弃）
  - 继承 `org.springframework.data.mongodb.config.AbstractMongoConfiguration` 可以注解配置响应的MongoDb Client
  - 1.1.3.2其实就是该种配置的XML配置版本

##### 1.4.1.2 配置类

```java
@Configuration
@ComponentScan("org.landy.commons.datacache")
public class MongoConfig extends AbstractCacheConfig {

    private static final String MONGODB_PREFIX = "mongodb://";

    @Value("${mongodb.database}")
    private String mongoDBName;
    @Value("${mongodb.collectionName}")
    private String mongoCollectionName;

    @Bean("mongoCollection")
    public MongoCollection<Document> mongoCollection(@Qualifier("mongoTemplate") MongoOperations mongoTemplate) {
        return mongoTemplate.getCollection(mongoCollectionName);
    }

    @Bean("mongoTemplate")
    public MongoOperations mongoTemplate(@Qualifier("mongoDbFactory") MongoDbFactory mongoDbFactory) {
        MongoOperations mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;
    }

    @Bean("mongoDbFactory")
    public MongoDbFactory mongoDbFactory(@Qualifier("mongoClient") MongoClient mongoClient) {
        MongoDbFactory mongoDbFactory = new SimpleMongoClientDbFactory(mongoClient, mongoDBName);
        return mongoDbFactory;
    }

    @Bean("mongoClient")
    public MongoClient mongoClient(@Qualifier("connectionString") final ConnectionString connectionString) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        return mongoClient;
    }

    @Bean("connectionString")
    public ConnectionString connectionString() {
        //mongodb://sysop:moon@localhost/records
        String connString = MONGODB_PREFIX + super.getHubCacheAccount() + ":" + super.getHubCachePassword() +
                            "@" + super.getHubCacheServer() + ":" +super.getHubCachePort() + "/" + mongoDBName;
        ConnectionString connectionString = new ConnectionString(connString);
        return connectionString;
    }

    ......
}
```

### 1.2 MongoDB安全认证

用户管理官方文档链接：https://docs.mongodb.com/manual/reference/method/js-user-management/

#### 1.2.1 安全认证

mongodb存储所有的用户信息在admin数据库的集合system.users中，保存用户名、密码和数据库信息。mongodb默认不启用权限认证，只要能连接到该服务器，就可连接到mongod。若要启用安全认证，需要更改配置文件参数authorization，也可以简写为auth。

##### 1.2.1.1 服务端配置

mongodb 4.x配置如下：

```
#vim /etc/mongod.conf
security:
  authorization: enabled #注意缩进，参照其他的值来改，若是缩进不对可能导致后面服务不能重启
```

##### 1.2.1.2 创建用户权限认证信息

先连接

```
mongo --host 127.0.0.1 --port 28028
```

然后执行以下命令

```
db.createUser(
   {
     user: "fortune",
     pwd: "123456",
     roles: [
        { role: "readWrite", db: "fortune" },
        { role: "dbAdmin", db: "fortune" }
     ]
   }
)
```

关于角色配置可以参考以下文章：

https://www.centos.bz/2017/08/mongodb-secure-intro-user-auth/

##### 1.2.1.3 重启服务

配置好了以后重新启动服务

```
sudo service mongod restart
```

##### 1.2.1.4 验证

执行以下命令返回1即验证成功

```
db.auth("fortune","123456")
```

## 2. MemCached

### 2.1 MemCached安装（CentOS）

#### 2.1.1 自动安装

##### 2.1.1.1 安装libevent库

```
rpm -q libevent //查看一下是否已经安装libevent库
yum install libevent libevent-devel
```

##### 2.1.1.2 安装SASL

```
rpm -qa | grep sasl
yum list all | grep sasl
yum install -y cyrus-sasl-devel
```

##### 2.1.1.3 安装MemCached

```
rpm -qa|grep memcache #查看是否已经安装
yum install memcached
```

#### 2.1.2 手动安装（推荐）

- 安装目录均为/usr/local/

- 编译需要使用g++，未安装则需要先安装

  ```
  yum -y update gcc
  yum -y install gcc+ gcc-c++ kernel-devel
  ```

##### 2.1.2.1 安装libevent库

下载地址：http://libevent.org/

```
tar zxvf libevent-2.0.21-stable.tar.gz
cd libevent-2.0.21-stable
./configure --prefix=/usr/local/libevent
make && make install
```

##### 2.1.2.2 安装SASL

下载地址：http://www.linuxfromscratch.org/blfs/view/cvs/postlfs/cyrus-sasl.html

```
rpm -qa |grep cyrus-sasl
yum erase cyrus-sasl -y #卸载
#由于Cyrus SASL 依赖 OpenSSL1.0.0
./configure --prefix=/usr/local/sasl --sysconfdir=/etc --enable-auth-sasldb --with-dbpath=/var/lib/sasl/sasldb2 --with-saslauthd=/var/run/saslauthd
make
make install &&
install -v -dm755                          /usr/share/doc/cyrus-sasl-2.1.27/html &&
install -v -m644  saslauthd/LDAP_SASLAUTHD /usr/share/doc/cyrus-sasl-2.1.27      &&
install -v -m644  doc/html/*.html          /usr/share/doc/cyrus-sasl-2.1.27/html &&
install -v -dm700 /var/lib/sasl
```



##### 2.1.2.3 安装MemCached

https://www.memcached.org/

下载地址：https://code.google.com/archive/p/memcached/downloads?page=1

```
wget http://memcached.org/latest                    下载最新版本
tar -zxvf memcached-1.x.x.tar.gz                    解压源码
cd memcached-1.x.x                                  进入目录
./configure --prefix=/usr/local/memcached --with-libevent=/usr/local/libevent --enable-sasl           									   配置
make && make test                                   编译
sudo make install                                   安装
```

**<u>注意</u>**：要能够使用SASL服务应要确保有cyrus-sasl-devel和cyrus-sasl-lib两个包，如果缺少这两个包，在安装带—enable-sasl参数的Memcached时会有错误提示： 

```
configure: error: Failed to locate the library containing sasl_server_init
```

##### 2.1.2.4 安装libmemcached

> libmemcached 是一个 memcached 的库，客户端库，C 和 C++ 语言实现的客户端库，具有低内存占用率、线程安全、并提供对memcached功能的全面支持。

下载地址：https://libmemcached.org/libMemcached.html

```
tar -zxvf libmemcached-1.0.16.tar.gz
cd libmemcached-1.0.16
./configure -prefix=/usr/local/libmemcached --with-memcached=/usr/local/memcached
make
make install
```



### 2.2 Memcached配置

#### 2.2.1 环境配置

- 环境变量配置，memcache.sh 这个文件中加入以下代码即可
  - 先进入这个文件：vim /etc/profile.d/memcache.sh 
  - export PATH=$PATH:/usr/local/memcached/bin/
  - source /etc/profile
  - 然后就可以用以下命令验证了：memcached -h

```
memcached 1.4.15
-p <num>      TCP port number to listen on (default: 11211)
-U <num>      UDP port number to listen on (default: 11211, 0 is off)
-s <file>     UNIX socket path to listen on (disables network support)
-a <mask>     access mask for UNIX socket, in octal (default: 0700)
-l <addr>     interface to listen on (default: INADDR_ANY, all addresses)
              <addr> may be specified as host:port. If you don't specify
              a port number, the value you specified with -p or -U is
              used. You may specify multiple addresses separated by comma
              or by using -l multiple times
-d            run as a daemon
-r            maximize core file limit
-u <username> assume identity of <username> (only when run as root)
-m <num>      max memory to use for items in megabytes (default: 64 MB)
......
```

- 

#### 2.2.2 SASL配置

https://github.com/memcached/memcached/wiki/SASLHowto

> --enable-sasl  是启用sasl认证，因为memcached默认不支持认证，所以私密数据会存在安全隐患，开启sasl认证后，memcached可以借助sasl进行认证。在需要的时候可以开启此选项，但是开启sasl之前要确认cyrus-sasl-devel的开发库是否已经安装，没有的话，则安装之；

##### 2.2.2.1 环境配置（手动安装）

环境变量配置，sasl.sh 这个文件中加入以下代码即可

- 先进入这个文件：vim /etc/profile.d/sasl.sh 
- export PATH=$PATH:/usr/local/sasl/sbin/
- source /etc/profile
- 验证：saslauthd -v

##### 2.2.2.2 其他配置

- saslauthd -v

  ```
  saslauthd 2.1.26
  authentication mechanisms: getpwent kerberos5 pam rimap shadow ldap httpform
  ```

- vi /etc/sysconfig/saslauthd (#修改/etc/sysconfig/saslauthd文件)

  ```
  # Mechanism to use when checking passwords.  Run "saslauthd -v" to get a list
  # of which mechanism your installation was compiled with the ablity to use.
  MECH=shadow
  ```

- saslpasswd2 -c -a memcached memuser #设置用户的SASL认证密码,执行命令后让你输入密码(为memuser 设置密码123456)

  - 添加的用户必须是系统中的用户，上面命令的意思就是，给memcached服务添加可访问的用户，没有添加的系统用户仍然是不可以访问该服务的。
  - 查看系统用户：cat /etc/passwd
  - 切换用户命令：su memcached
  - 不能切换的话：usermod -s /bin/bash memcached

- sasldblistusers2 #可以查看当前的SASL用户

- chkconfig saslauthd on (系统自启动)

- saslauthd -a shadow -c -r memcached@123456 (启动服务)

- ps aux | grep saslauthd

```
#修改/etc/sysconfig/saslauthd文件
MECH=shadow
#设置用户的SASL认证密码
# Create a user for memcached.
saslpasswd2 -a memcached -c cacheuser
#最终生成的DB文件在/etc/下
-rw-r—– 1 root root 12288 Mar  6 11:52 /etc/sasldb2
#可以查看当前的SASL用户
sasldblistusers2
```

#### 2.2.3 创建系统服务

- 代码如下所示，将其建立为/etc/init.d/memcached文件：

```
#!/bin/bash
#
# Init file for memcached
#
# chkconfig: - 86 14
# description: Distributed memory caching daemon
#
# processname: memcached
# config: /etc/sysconfig/memcached

. /etc/rc.d/init.d/functions

## Default variables
PORT="11211"
USER="nobody"
MAXCONN="1024"  
CACHESIZE="64"
OPTIONS=""

[ -f /etc/sysconfig/memcached ] && . /etc/sysconfig/memcached

RETVAL=0
prog="/usr/local/memcached/bin/memcached"
desc="Distributed memory caching"
lockfile="/var/lock/subsys/memcached"

start() {
        echo -n $"Starting $desc (memcached): "
        daemon $prog -d -p $PORT -u $USER -c $MAXCONN -m $CACHESIZE "$OPTIONS"  #启动失败，去掉-o
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && touch $lockfile
        return $RETVAL
}

stop() {
        echo -n $"Shutting down $desc (memcached): "
        killproc $prog
        RETVAL=$?
        echo
        [ $RETVAL -eq 0 ] && rm -f $lockfile
        return $RETVAL
}

restart() {
        stop
        start
}

reload() {
        echo -n $"Reloading $desc ($prog): "
        killproc $prog -HUP
        RETVAL=$?
        echo
        return $RETVAL
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
  condrestart)
        [ -e $lockfile ] && restart
        RETVAL=$?
        ;;       
  reload)
        reload
        ;;
  status)
        status $prog
        RETVAL=$?
        ;;
   *)
        echo $"Usage: $0 {start|stop|restart|condrestart|status}"
        RETVAL=1
esac

exit $RETVAL
```

- 使用如下命令配置memcached成为系统服务：

  ```
  # chmod +x /etc/init.d/memcached
  # chkconfig --add memcached
  # chkconfig memcached on
  # service memcached start
  ```

### 2.3 Memcached 运行

#### 2.3.1 帮助命令

```
$ /usr/memcached/bin/memcached -h                           命令帮助
```

> 注意：如果使用自动安装 memcached 命令位于 **/usr/bin/memcached**。(利用which memcached可以查到安装目录)
>
> **启动选项：**
>
> - -d是启动一个守护进程；
> - -m是分配给Memcache使用的内存数量，单位是MB；
> - -u是运行Memcache的用户；
> - -l是监听的服务器IP地址，可以有多个地址；
> - -U <num>：指定监听的UDP端口，默认为11211，0表示关闭UDP端口；
> - -p是设置Memcache监听的端口，，最好是1024以上的端口；
> - -c是最大运行的并发连接数，默认是1024；
> - -P是设置保存Memcache的pid文件。
> - -t <threads>：用于处理入站请求的最大线程数，仅在memcached编译时开启了支持线程才有效；
> - -f <num>：设定Slab Allocator定义预先分配内存空间大小固定的块时使用的增长因子；
> - -M：当内存空间不够使用时返回错误信息，而不是按LRU算法利用空间；
> - -n: 指定最小的slab chunk大小；单位是字节；
> - -S: 启用sasl进行用户认证；
> - -v: 显示详细信息
> - -vv: 显示更加详尽的信息

#### 2.3.2 启动参数

自动安装：启动的相关参数在 /etc/init.d/memcached 和 /etc/sysconfig/memcached 中定义，若要修改启动参数，只有修改 /etc/sysconfig/memcached 才有效，修改 /etc/init.d/memcached 不会生效。

查看端口：netstat -ntlp

#### 2.3.3 运行

##### 2.3.3.1 前台运行

```
memcached -p 11211 -m 64m -vv #监听TCP端口11211，最大内存使用量为64M
```

##### 2.3.3.2 后台运行

```
# memcached -p 11211 -m 64m -d
或者
# memcached -d -m 64M -u memcached -p 11234 -c 256 -S -P /usr/local/memcached/memcached.pid
```

#### 2.3.4 查看状态

- **Memcached运行状态**

  ```
  memcached-tool 127.0.0.1:11211 stats
  ```

- **查看Memcached服务状态**

  ```
  memstat --servers=127.0.0.1:11211              //需安装libmemcached
  ```

  
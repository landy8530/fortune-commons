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




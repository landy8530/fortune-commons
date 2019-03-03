# Commons NOSQL Component
## MongoDB

> The Spring Data MongoDB 2.x binaries require JDK level 8.0 and above and [Spring Framework](https://spring.io/docs) 5.1.5.RELEASE and above.
>
> In terms of document stores, you need at least version 2.6 of [MongoDB](https://www.mongodb.org/).

依赖包Maven坐标如下：

```xml
<properties>
    <!--<mongodb.version>2.9.3</mongodb.version>-->
    <mongodb.version>3.10.1</mongodb.version>
    <spring.data.version>2.1.5.RELEASE</spring.data.version>
</properties>
<!--mongodb驱动包：包括bson，mongo-java-drive和mongo-java-core-->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>${mongodb.version}</version>
</dependency>
<!--mongodb Spring核心包-->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-mongodb</artifactId>
    <version>${spring.data.version}</version>
    <exclusions>
        <exclusion>
            <groupId>org.mongodb</groupId>
            <artifactId>mongo-java-driver</artifactId>
        </exclusion>
        <exclusion>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-commons</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--mongodb Spring依赖包-->
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-commons</artifactId>
    <version>${spring.data.version}</version>
    <scope>compile</scope>
</dependency>
```
## MemCache

## Redis
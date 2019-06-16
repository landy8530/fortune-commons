# Fortune Commons Component

## 项目介绍

### 项目特色

项目深受Java 劝退师，Apache 和 Spring Cloud 等知名开源架构成员[小马哥](<https://segmentfault.com/u/mercyblitz>)（Github:[mercyblitz](<https://github.com/mercyblitz>)）的思想影响，本项目采用了现在普遍采用的自动化配置、注解化开发等特点，开发过程非常的便利。代码如下所示：

```Java
/**
 * @author: Landy
 * @date: 2019/4/7 00:04
 * @description: WebApplicationContext配置类,需要根据具体的业务逻辑进行配置，比如缓存门面类的配置
 * 需要在web端工程里具体配置
 */
@Configuration
@EnableAspectJAutoProxy // 相当于 xml 中的 <aop:aspectj-autoproxy/>
@EnableTransactionManagement // 开启注解事务
@Import({BeanInitializeCompletedConfiguration.class})
public class RootApplicationContextConfiguration {

    // 配置DataCacheFacade
    @Bean(name = DataCacheFacade.BEAN_NAME_DATA_CACHE_FACADE)
    public DataCacheFacade dataCacheFacade() {
        DataCacheFacade dataCacheFacade = new DataCacheFacade();
        List<CacheDataLoadAdapter> cacheDataAdapterList = new ArrayList<>();
        cacheDataAdapterList.add(new CodeCacheDataLoadAdapter());
        dataCacheFacade.setCacheDataAdapterList(cacheDataAdapterList);
        return dataCacheFacade;
    }

    public class CodeCacheDataLoadAdapter extends CacheDataLoadAdapter {
        private List<String> keys=new ArrayList<String>();
        @Override
        public boolean loadData() {
            ...
            return true;
        }

        @Override
        public List<String> getStoreKeys() {
            return keys;
        }
    }
}
```

### 项目名称

由于绝大多数程序员都是命苦的娃，都希望有朝一日能够通过代码改变自己的命运，所以我为此项目定了一个fortune的名称，希望我们大家都能够通过代码改变世界（哈哈）！

### 版本约束

- Spring：5.x+ 
- JDK：1.8+

### 版本计划

为适应目前国内各个行业不同的技术分布，目前计划三个大版本的计划，说明如下：

| 版本 | 功能说明                                                     |
| ---- | ------------------------------------------------------------ |
| 1.x  | 数据缓存处理和excel/pdf导出组件/文件上传服务组件，集成Spring MVC |
| 2.x  | 数据缓存处理和excel/pdf导出组件/文件上传服务组件，集成Restful API |
| 3.x  | 数据缓存处理和excel/pdf导出组件/文件上传服务组件，集成Restful API，并且计划加入Spring Boot/Spring Cloud等 |

注意：以上各个版本都可以增加其他组件。

### 工程说明

目前最新版本为v1.0.x，含有以下子工程（子模块），分别说明如下(也可参考[wiki](<https://github.com/landy8530/fortune-commons/wiki>))：

#### commons-core

主要是本项目所需的一些核心功能实现，比如BeanCopier工具封装，读取yml文件工具，Freemarker解析实现，ApplicationContext工具类，Spring容器初始化后统一操作的listener实现以及其他一些工具类支持。

#### commons-doc-common

文件上传服务组件公共类库，依赖于module [fortune-commons-core].

```xml
<dependency>
    <groupId>org.fortune</groupId>
    <artifactId>commons-core</artifactId>
</dependency>
```

#### commons-doc-client

文件上传服务客户端，使用简单，只需要引入commons-doc-client，即可以实现文件的上传/替换/删除等操作。

实现方式计划采用以下两种方式实现：

- 采用Netty实现文件的转储操作
- 采用原生HTTP方式实现（利用Spring MVC）

#### commons-doc-server

文件上传服务服务端，实现方式计划采用以下两种方式实现：

- 采用Netty实现文件的转储操作
- 采用原生HTTP方式实现（利用Spring MVC）

#### commons-datacache

本模块动态实现了各主流缓存中间件的实现，可以自由切换，依赖于commons-nosql模块。目前实现了以下几种：

- 本地内存（Memory）
- Mongodb
- Memcached
- Redis(即将实现)

#### commons-nosql

NoSql模拟关系型数据库的CRUD操作，目前有Mongodb实现。

#### commons-export

实现了excel和pdf导出组件

#### commons-web

封装了web端常见的一些配置操作

#### commons-web-springmvc

封装了spring mvc的一些配置操作，依赖于commons-web子模块。

#### fortune-commons-example

本项目的演示模块，主要是用于测试用途。

## 如何对该开源项目进行贡献

1. 代码大多是手敲，所以难免会有错误，你可以帮我提交Bug issues或者PR。
2. 很多知识点我可能没有涉及到，所以你可以对其他知识点进行补充或者加入其他的组件。
3. 为了使项目更加的透明化，便利化，也可以参与[wiki](<https://github.com/landy8530/fortune-commons/wiki>)的编写工作。

## 为什么要做这个开源组件？

初始想法源于自己工作中遇到的各种坑，主要目的是为了通过这个开源平台来帮助一些在学习 Java 或者直接在自己公司中使用或者扩展自己的项目。

## 操作指引

- [多JDK版本安装说明（Windows）](https://github.com/landy8530/fortune-commons/wiki/Multiple-JDK-Version-in-Windows-System)
- [Git操作说明](https://github.com/landy8530/fortune-commons/wiki/Git-Instruction)
- [Idea基于Maven的多JDK版本配置指引](https://github.com/landy8530/fortune-commons/wiki/Maven-JDK-Profile)
- [Maven多项目依赖下的版本控制指引](https://github.com/landy8530/fortune-commons/wiki/Maven-Version-Control)


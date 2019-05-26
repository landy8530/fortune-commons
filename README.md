# Fortune Commons Component

## 版本说明

### 版本约束

- Spring：5.x+ 
- JDK：1.8+

### 版本计划

为适应目前国内各个行业不同的技术分布，目前计划三个大版本的计划，说明如下：

| 版本 | 功能说明                                                     |
| ---- | ------------------------------------------------------------ |
| 1.x  | 数据缓存处理和excel/pdf导出组件，集成Spring MVC              |
| 2.x  | 数据缓存处理和excel/pdf导出组件，集成Restful API             |
| 3.x  | 数据缓存处理和excel/pdf导出组件，集成Restful API，并且计划加入Spring Boot/Spring Cloud等 |

注意：以上各个版本都可以增加其他组件。

### 工程说明

目前最新版本为v1.0.x，含有以下子工程（子模块），分别说明如下：

#### commons-core

主要是本项目所需的一些核心功能实现，比如BeanCopier工具封装，读取yml文件工具，Freemarker解析实现，ApplicationContext工具类，Spring容器初始化后统一操作的listener实现以及其他一些工具类支持。

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

1. 代码大多是手敲，所以难免会有错误，你可以帮我Bug，提交issues或者PR。
2. 很多知识点我可能没有涉及到，所以你可以对其他知识点进行补充或者加入其他的组件。

## 为什么要做这个开源组件？

初始想法源于自己工作中遇到的各种坑，主要目的是为了通过这个开源平台来帮助一些在学习 Java 或者直接在自己公司中使用或者扩展自己的项目。

## Git操作说明

### 切换分支

fork本工程后可以按照如下操作即可，

- 切换到master分支，并且更新最新远程库中的代码

  - git checkout master
  - git pull/git fetch

### 创建分支

- 创建自己的本地分支，以master为源创建

  - git checkout -b fortune-commons-export

- 查看是否创建成功

  - git branch

  ```
    fortune-commons-beanutils
  * fortune-commons-export
    fortune-commons-memcached
    master
  ```

  星号(*)表示当前所在分支。现在的状态是成功创建的新的分支并且已经切换到新分支上。

### 同步分支

- 把新建的本地分支push到远程服务器，远程分支与本地分支同名（当然可以随意起名）

  - git push origin fortune-commons-export:fortune-commons-export

### 创建标签

- git tag -a v1.0.1 -m "fortune commons v1.0.1"
- git push origin v1.0.1






# Fortune Commons Component

## 版本说明

### 版本约束

- Spring：5.x+ 
- JDK：1.8+

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






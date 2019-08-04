# 通用文件服务组件（HTTP实现版本）

> 

## 1. 简介

### 1.1 实现原理



### 1.2 组件功能介绍

该组件基于Apache HTTPComponents组件实现，具有如下功能：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。使用简单，只需要引入commons-doc-client-http，即可以实现文件的以上操作。

本组件分为三个module，分别为：

- commons-doc-server-http：Http实现文件服务组件的服务端
- commons-doc-common：Http文件服务组件公共组件
- commons-doc-client-http：Http文件服务组件的客户端

## 2. 服务端

### 2.1 功能简介



### 2.2 实现步骤



## 3. 客户端

### 3.1 功能简介



### 3.2 实现步骤



## 4. 操作指引

该文件服务组件的使用需要分为两个部分，一个是服务端配置与启动，一个是客户端的配置与启动。

> 配置基本上与Netty实现版本是一致的，只是细微的差别。

### 4.1 服务端配置与启动

#### 4.1.1 配置

服务端的配置采用yml文件的配置，更加的简洁明了，主要的注意点是文件存放位置的配置，在开发过程中，可以有两种方式配置：

- Idea自启动方式：如果采用此种方式则需要把rootPath配置到工程路径下（target目录），如下所示：

  ```yml
  # 在idea中执行的话，需要配置target目录下的打包文件
      rootPath: C:\03_code\idea_workspace\fortune-commons\commons-doc-server-http\target\commons-doc-server-http\ #上传文件的根目录,实际工作环境按照实际情况更改即可
  ```

- 打包后在tomcat独立启动方式

  ```yml
  # 也可以单独把打包后的war包拷贝到tomcat webapp目录下直接运行也可以
      rootPath: C:\05_webserver\apache-tomcat-8.5.42\webapps\doc-server #上传文件的根目录,实际工作环境按照实际情况更改即可
  ```

#### 4.1.2 启动

本文采用的是idea自启动方式，则需要配置一下tomcat路径，以及引入相应的module即可，如下图所示：



需要注意的是，在Deployment页签，需要配置该项目访问的Application Context，否则有可能启动后出现404的情况。如下图所示：



配置完成即可启动文件服务组件，如下图即为启动信息日志：

```

```

然后就可以访问如下页面，显示如下：



### 4.2 客户端配置与启动

#### 4.2.1 配置

客户端的配置比较简单，也是采用yml文件方式配置如下：

```yml
#上传成功后，访问文件的正确方式为：
# ${host}:${port}/${domainName}/${type}/${path}
# appName为远程文件服务app名称，比如doc-server
# type 为images/attaches
# path为文件上传后服务端返回的文件相对路径
#http://localhost:8080/doc-server/images/fortune/20190629/1561769031017.jpg
upload:
  server:
    port: 8080 #需要配置的是远程文件服务器的tomcat的端口号，非远程文件服务netty端口号
    host: 127.0.0.1
    domainName: doc-server
  userName: fortune
  password: fortune
```

需要注意的是，文件访问的端口号跟远程文件服务器netty对应的端口是不一样的，这点需要特别注意。在调用文件服务返回的路径的时候，需要用到服务端访问文件的地址，进而访问相应的文件内容。可通过方法 `org.fortune.doc.client.DocClientContainer#getDocServerUrl`得到相应的服务端地址，再拼接上返回的相对路径，即可得到文件的完整地址了。

#### 4.2.2 启动/调用

客户端单元测试用例如下：

```java
package org.fortune.doc.client;

import org.fortune.commons.core.conf.ApplicationContextHelperConfiguration;
import org.fortune.commons.core.conf.SettingsConfiguration;
import org.fortune.doc.client.conf.DocClientConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author: landy
 * @date: 2019/5/30 23:37
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@ContextConfiguration(classes = {
        SettingsConfiguration.class,  // common settings configuration
        DocClientConfiguration.class,
        ApplicationContextHelperConfiguration.class
}) //加载Spring配置文件
public class DocClientTest {

    @Test
    public void test() {
        //DocClient.doImageUpload("psb.jpg", new File("C:\\06_temp\\psb.jpg"));
        //DocClient.doImageDelete("images\\fortune\\b\\190707114835_9573.jpg");
        DocClient.doImageReplace("images\\fortune\\f\\\\190707114616_3742.JPG", new File("C:\\06_temp\\psb.jpg"));
    }

    @Test
    public void docServerUrl() {
        System.out.println(DocClientContainer.getInstance().getDocServerUrl());
    }

}
```

同样的服务端也会显示出上述相似的日志信息，

```

```

观察服务端target目录下，又多了一个文件，显示如下，

跟日志信息打印出来的路径是一致的，我们可以通过返回的地址信息进行访问文件，



## 5. 常见问题



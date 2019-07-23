# 通用文件服务组件（Netty实现版本）

> 本文所述文件服务组件在笔者此前一篇文章中已有阐述([基于netty的文件上传下载组件](https://www.jianshu.com/p/bf85de3e102e))，不过本文将基于之前这个实现再次进行升级改造，利用基于注解的方式进行自动装配。

## 1. 简介

### 1.1 Netty简介

Netty是一个异步事件驱动的网络应用程序框架，用于快速开发可维护的高性能协议服务器和客户端。关于其详细的介绍可以参考[Netty官方网站](https://netty.io/index.html)。

> Netty is a NIO client server framework which enables quick and easy development of network applications such as protocol servers and clients. It greatly simplifies and streamlines network programming such as TCP and UDP socket server.

Netty主要特点：

- Unified API for various transport types - blocking and non-blocking socket（统一API）
- Based on a flexible and extensible event model which allows clear separation of concerns（事件模型）
- Highly customizable thread model - single thread, one or more thread pools such as SEDA（线程模型）
- True connectionless datagram socket support (since 3.1)（无连接数据报文Socket支持）

### 1.2 组件功能介绍

该组件基于netty3.6.3实现，具有如下功能：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。使用简单，只需要引入commons-doc-client-netty，即可以实现文件的以上操作。

本组件分为三个module，分别为：

- commons-doc-server-netty：Netty实现文件服务组件的服务端
- commons-doc-common：Netty文件服务组件公共组件
- commons-doc-client-http：Netty文件服务组件的客户端

## 2. 服务端

### 2.1 功能简介

服务端组件实现以下功能：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。代码结构如下图所示，

![file-server](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/file-server.png)

所有的文件服务都是基于接口`DocServerProcessor`进行的，主要有以下几个实现类：

- `UploadDocServerHandler`实现文件上传服务
- `ReplaceDocServerHandler`实现文件替换服务
- `DeleteDocServerHandler`实现文件删除服务
- `CreateThumbPictureServerHandler`实现创建图片缩略图服务

### 2.2 实现步骤

具体实现步骤以文件上传为例。

首先 `org.fortune.doc.server.support.DocServerHandler`类会持续监听客户端的请求，如果是文件处理动作，则会进入messageReceived方法进行相应的处理逻辑。该类定义了以下成员变量：

```java
//http请求
private HttpRequest request;
//是否需要断点续传作业
private boolean readingChunks;
//接收到的文件内容
private final StringBuffer responseContent = new StringBuffer();
//解析收到的文件
private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); //16384L
//post请求的解码类,它负责把字节解码成Http请求。
private HttpPostRequestDecoder decoder;
//请求参数
private RequestParam requestParams = new RequestParam();
```

该方法实现中，如果文件大小小于chunked的最小值，则直接进行文件上传操作。否则，需要进行分块处理。然后进行文件上传操作。
文件大小小于1k的操作：

```java
if (request.isChunked()) { //说明还没有请求完成，继续
    this.readingChunks = true;
    LOGGER.info("文件分块操作....");
} else {
    LOGGER.info("文件大小小于1KB，文件接收完成，直接进行相应的文件处理操作....");
    //请求完成，则接收请求参数，进行初始化请求参数
    RequestParamParser.parseParams(this.decoder, this.requestParams);
    //根据请求参数进行相应的文件操作
    LOGGER.info("文件处理开始....requestParams参数解析：{}",requestParams);
    String result = DocServerHandlerFactory.process(this.requestParams);
    LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果：{}",result);
    this.responseContent.append(result);
    //给客户端响应信息
    writeResponse(e.getChannel());

    e.getFuture().addListener(ChannelFutureListener.CLOSE);
}
```

需要分块处理操作：

```java
HttpChunk chunk = (HttpChunk) e.getMessage();
try {
    //chunk.getContent().capacity();
    LOGGER.info("文件分块操作....文件大小：{} bytes",chunk.getContent().capacity());
    this.decoder.offer(chunk);
} catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
    e1.printStackTrace();
    this.responseContent.append(e1.getMessage());
    writeResponse(e.getChannel());
    Channels.close(e.getChannel());
    return;
}

if (chunk.isLast()) {
    //文件末尾
    this.readingChunks = false;
    LOGGER.info("到达文件内容的末尾，进行相应的文件处理操作....start");
    RequestParamParser.parseParams(this.decoder, this.requestParams);

    LOGGER.info("文件处理开始....requestParams参数解析：{}",requestParams);
    String result = DocServerHandlerFactory.process(this.requestParams);
    LOGGER.info("文件处理结束....FileServerHandlerFactory处理结果：{}",result);

    this.responseContent.append(result);
    //给客户端响应信息
    writeResponse(e.getChannel());

    e.getFuture().addListener(ChannelFutureListener.CLOSE);
    LOGGER.info("到达文件内容的末尾，进行相应的文件处理操作....end");
}
```

以上操作主要有两个注意点：

1. 请求参数的解析工作（根据HttpDataType进行相应参数的赋值操作）
2. 根据解析的参数进行相应的文件处理操作（根据文件操作类型，选择相应的处理句柄进行文件处理）

## 3. 客户端

### 3.1 功能简介

客户端组件主要提供对外访问服务端组件的接口，提供以下接口：文件上传，文件替换，文件删除，如果是图片的话，还可以生成缩略图等功能。代码结构如下：

![file-client](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/file-client.png)

`org.fortune.doc.client.DocClient`类是对外提供接口的工具类,具有以下主要方法：

1. uploadFile 文件上传，对应文件处理句柄类为：`org.fortune.doc.client.handler.UploadDocClientHandler`
2. deleteFile 删除服务端文件，对应文件处理句柄类为：`org.fortune.doc.client.handler.DeleteDocClientHandler`
3. replaceFile 替换服务端文件，对应文件处理句柄类为：`org.fortune.doc.client.handler.ReplaceDocClientHandler`
4. createThumbPicture 生成缩略图，对应文件处理句柄类为：`org.fortune.doc.client.handler.CreateThumbPictureClientHandler`

### 3.2 实现步骤

实现步骤以上传文件为例，其他类似实现。直接上代码：

```java
/**
     * 文件上传
     * @param file 需要上传的文件
     * @param fileName 文件名称
     * @param thumbMark 是否需要生成缩略图
     * @return
     * @author:landyChris
     */
    public static String uploadFile(File file, String fileName,
                                    boolean thumbMark) {
        DocClientPipelineFactory clientPipelineFactory = new DocClientPipelineFactory();
        //辅助类。用于帮助我们创建NETTY服务
        ClientBootstrap bootstrap = createClientBootstrap(clientPipelineFactory);
        String strThumbMark = Constants.THUMB_MARK_NO;
        if (thumbMark) {
            strThumbMark = Constants.THUMB_MARK_YES;
        }
        //具体处理上传文件逻辑
        uploadFile(bootstrap, DocClientContainer.getInstance().getHost(),
                DocClientContainer.getInstance().getPort(), file, fileName, strThumbMark,
                DocClientContainer.getInstance().getUserName(),
                DocClientContainer.getInstance().getPassword());
        Result result = clientPipelineFactory.getResult();
        if ((result != null) && (result.isSuccess())) {
            return result.getFilePath();
        }
        return null;
    }
```

具有三个参数，前面几行代码都是很一些netty的初始化工作，具体看一个私有方法uploadFile，如下代码所示：

```java
private static void uploadFile(ClientBootstrap bootstrap, String host,
                                   int port, File file, String fileName, String thumbMark,
                                   String userName, String pwd) {
        //1.构建uri对象
        URI uri = getUri(host, port);
        //2.连接netty服务端
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
                port));
        //3.异步获取Channel对象
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        //4.初始化文件上传句柄对象
        AbstractDocClientHandler handler = new UploadDocClientHandler(host, uri,
                file, fileName, thumbMark, userName, pwd);
        //5.获取Request对象
        HttpRequest request = handler.getRequest();
        //6.获取Http数据处理工厂
        HttpDataFactory factory = getHttpDataFactory();
        //7.进行数据的包装处理，主要是进行上传文件所需要的参数的设置，此时调用的句柄是具体的UploadFileClientHandler对象
        HttpPostRequestEncoder bodyRequestEncoder = handler
                .wrapRequestData(factory);
        //8.把request写到管道中，传输给服务端
        channel.write(request);
        //9.做一些关闭资源的动作
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        bodyRequestEncoder.cleanFiles();
        channel.getCloseFuture().awaitUninterruptibly();

        bootstrap.releaseExternalResources();
        factory.cleanAllHttpDatas();
    }
```

主要有以下实现步骤：

1. 构建uri对象
2. 连接netty服务端
3. 异步获取Channel对象
4. 初始化文件上传句柄对象
5. 获取Request对象
6. 获取Http数据处理工厂
7. 进行数据的包装处理，主要是进行上传文件所需要的参数的设置，此时调用的句柄是具体的UploadFileClientHandler对象
8. 把request写到管道中，传输给服务端
9. 做一些关闭资源的动作
   具体细节实现请参考github上的代码。如果各位读者喜欢的话，可以加个star哈。

## 4. 操作指引

该文件服务组件的使用需要分为两个部分，一个是服务端配置与启动，一个是客户端的配置与启动。

### 4.1 服务端配置与启动

#### 4.1.1 配置

服务端的配置采用yml文件的配置，更加的简洁明了，主要的注意点是文件存放位置的配置，在开发过程中，可以有两种方式配置：

- Idea自启动方式：如果采用此种方式则需要把rootPath配置到工程路径下（target目录），如下所示：

  ```yml
  # 在idea中执行的话，需要配置target目录下的打包文件
      rootPath: C:\03_code\idea_workspace\fortune-commons\commons-doc-server-netty\target\commons-doc-server-netty\ #上传文件的根目录,实际工作环境按照实际情况更改即可
  ```

- 打包后在tomcat独立启动方式

  ```yml
  # 也可以单独把打包后的war包拷贝到tomcat webapp目录下直接运行也可以
      rootPath: C:\05_webserver\apache-tomcat-8.5.42\webapps\doc-server #上传文件的根目录,实际工作环境按照实际情况更改即可
  ```

#### 4.1.2 启动

本文采用的是idea自启动方式，则需要配置一下tomcat路径，以及引入相应的module即可，如下图所示：

![1563636405662](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/idea-tomcat-config.png)

需要注意的是，在Deployment页签，需要配置该项目访问的Application Context，否则有可能启动后出现404的情况。如下图所示：

![1563894965783](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/idea-tomcat-config-note.png)

配置完成即可启动文件服务组件，如下图即为启动信息日志：

```
....
2019-07-20  23:48:56.174 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'mvcViewResolver'
2019-07-20  23:48:56.182 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.beans.factory.support.DefaultListableBeanFactory - Creating shared instance of singleton bean 'viewResolver'
2019-07-20  23:48:56.208 [RMI TCP Connection(3)-127.0.0.1] INFO  org.fortune.commons.core.help.BeanInitializeCompletedListener - spring conf 容器初始化完毕..处理启动之后事件--start
2019-07-20  23:48:56.212 [RMI TCP Connection(3)-127.0.0.1] INFO  org.fortune.doc.server.DocServerContainer - 加入账户：fortune
2019-07-20  23:48:56.212 [RMI TCP Connection(3)-127.0.0.1] INFO  org.fortune.doc.server.DocServerContainer - 加入账户：fortune0
2019-07-20  23:48:56.213 [RMI TCP Connection(3)-127.0.0.1] INFO  org.fortune.doc.server.DocServerContainer - 加入默认账户：Account{userName='default_account', password='lyx', rootPath='C:\05_webserver\apache-tomcat-8.5.42\bin\', level=1, thumbHeight=20, thumbWidth=20}
2019-07-20  23:48:56.296 [RMI TCP Connection(3)-127.0.0.1] INFO  org.fortune.commons.core.help.BeanInitializeCompletedListener - spring conf 容器初始化完毕..处理启动之后事件--end
2019-07-20  23:48:56.302 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.jndi.JndiTemplate - Looking up JNDI object with name [java:comp/env/spring.liveBeansView.mbeanDomain]
2019-07-20  23:48:56.303 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.jndi.JndiLocatorDelegate - Converted JNDI name [java:comp/env/spring.liveBeansView.mbeanDomain] not found - trying original name [spring.liveBeansView.mbeanDomain]. javax.naming.NameNotFoundException: Name [spring.liveBeansView.mbeanDomain] is not bound in this Context. Unable to find [spring.liveBeansView.mbeanDomain].
2019-07-20  23:48:56.303 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.jndi.JndiTemplate - Looking up JNDI object with name [spring.liveBeansView.mbeanDomain]
2019-07-20  23:48:56.304 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.jndi.JndiPropertySource - JNDI lookup for name [spring.liveBeansView.mbeanDomain] threw NamingException with message: Name [spring.liveBeansView.mbeanDomain] is not bound in this Context. Unable to find [spring.liveBeansView.mbeanDomain].. Returning null.
2019-07-20  23:48:56.307 [RMI TCP Connection(3)-127.0.0.1] INFO  org.springframework.web.context.ContextLoader - Root WebApplicationContext initialized in 2216 ms
2019-07-20  23:48:56.311 [RMI TCP Connection(3)-127.0.0.1] DEBUG org.springframework.web.filter.CharacterEncodingFilter - Filter 'characterEncodingFilter' configured for use
2019-07-20  23:48:56.320 [RMI TCP Connection(3)-127.0.0.1] INFO  org.springframework.web.servlet.DispatcherServlet - Initializing Servlet 'doc-server'
....
```

然后就可以访问如下页面，显示如下：

![1563637832150](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/doc-server-start.png)

不过我们配置的netty端口是9999，我们试着访问一下，

![1563637878257](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/doc-server-demo.png)

此时我们就可以不依赖客户端，直接在网页端进行文件上传的测试操作了。输入账户，密码，选择相应的文件即可上传，上传成功的返回页面如下：

![1563637962089](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/doc-server-upload-success.png)

后台控制台打印出的日志信息如下：

```log
2019-07-20  23:52:12.833 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....
2019-07-20  23:52:12.835 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：1024 bytes
2019-07-20  23:52:12.849 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：2048 bytes
2019-07-20  23:52:12.849 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：2048 bytes
2019-07-20  23:52:12.850 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：3072 bytes
2019-07-20  23:52:12.850 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：4096 bytes
2019-07-20  23:52:12.850 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：6144 bytes
2019-07-20  23:52:12.852 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8192 bytes
2019-07-20  23:52:12.852 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8192 bytes
2019-07-20  23:52:12.852 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：3072 bytes
2019-07-20  23:52:12.853 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8192 bytes
2019-07-20  23:52:12.853 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：7168 bytes
2019-07-20  23:52:12.853 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8192 bytes
2019-07-20  23:52:12.854 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8192 bytes
2019-07-20  23:52:12.854 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：6144 bytes
2019-07-20  23:52:12.856 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：8157 bytes
2019-07-20  23:52:12.862 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件分块操作....文件大小：0 bytes
2019-07-20  23:52:12.862 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 到达文件内容的末尾，进行相应的文件处理操作....start
2019-07-20  23:52:12.865 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件处理开始....requestParams参数解析：
NETTY WEB Server
===================================


UserName=fortune
pwd=fortune
action=uploadFile
fileContentType=image/jpeg
fileSize=81 KB 
getform=POST
Send=Send

2019-07-20  23:52:12.867 [New I/O worker #1] INFO  org.fortune.doc.server.handler.factory.DocServerHandlerFactory - 进行文件上传操作....
2019-07-20  23:52:12.869 [New I/O worker #1] INFO  org.fortune.doc.server.handler.UploadDocServerHandler - --srcFileName--psb.jpg
2019-07-20  23:52:12.871 [New I/O worker #1] INFO  org.fortune.doc.server.handler.UploadDocServerHandler - 文件上传成功,保存路径为:fortune\l\190720235212_1027.jpg,真实路径为：C:\03_code\idea_workspace\fortune-commons\commons-doc-server-netty\target\commons-doc-server-netty\/fortune\l\190720235212_1027.jpg
2019-07-20  23:52:12.871 [New I/O worker #1] DEBUG org.fortune.doc.server.handler.UploadDocServerHandler - 生成缩略图
2019-07-20  23:52:12.872 [New I/O worker #1] INFO  org.fortune.doc.server.handler.UploadDocServerHandler - 生成缩略图的名称为:190720235212_1027_thumb.jpg,路径为:C:\03_code\idea_workspace\fortune-commons\commons-doc-server-netty\target\commons-doc-server-netty\/fortune\l\190720235212_1027_thumb.jpg
2019-07-20  23:52:13.130 [New I/O worker #1] DEBUG org.fortune.doc.server.handler.factory.DocServerHandlerFactory - 执行结果:{"action":"uploadFile","code":1,"filePath":"fortune\\l\\190720235212_1027.jpg","msg":"文件上传成功","success":true}
2019-07-20  23:52:13.130 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 文件处理结束....FileServerHandlerFactory处理结果：{"action":"uploadFile","code":1,"filePath":"fortune\\l\\190720235212_1027.jpg","msg":"文件上传成功","success":true}
2019-07-20  23:52:13.133 [New I/O worker #1] INFO  org.fortune.doc.server.support.DocServerHandler - 到达文件内容的末尾，进行相应的文件处理操作....end
```

再观察一下netty 代码中的target目录下就有上传的文件了，如下所示：

![1563638108710](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/server-file.png)

我们可以通过如下地址进行访问，

`http://localhost:8080/doc-server/fortune//l//190720235212_1027.jpg`，关于如何得到图片地址，则在客户端工具类中有相应的方法获取。

### 4.2 客户端配置与启动

#### 4.2.1 配置

客户端的配置比较简单，也是采用yml文件方式配置如下：

```yml
#上传成功后，在需要访问文件web工程中配置以下服务器地址，正确格式为：
# ${host}/${port}/${appName}/${path}
# appName为远程文件服务app名称，比如doc-server
# path为文件上传后服务端返回的文件相对路径
#http://localhost:8080/doc-server/fortune//p//190629082821_8300.jpg
upload:
  server:
    port: 9999 #需要配置的是远程文件服务器netty服务端口号
    host: 127.0.0.1
    access: #访问文件
      port: 8080 #访问文件 web 服务端口号
      domainName: doc-server #访问文件 web 服务应用名称
  userName: fortune
  password: fortune
```

需要注意的是，文件访问的端口号跟远程文件服务器netty对应的端口是不一样的，这点需要特别注意。在调用文件服务返回的路径的时候，需要用到服务端访问文件的地址，进而访问相应的文件内容。可通过方法 `org.fortune.doc.client.DocClientContainer#getDocServerUrl`得到相应的服务端地址，再拼接上返回的相对路径，即可得到文件的完整地址了。

#### 4.2.2 启动/调用

客户端单元测试用例如下：

```java
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
        DocClient.uploadFile(new File("C:\\06_temp\\psb.jpg"), "psb.jpg",false);
    }
}
```

同样的服务端也会显示出上述相似的日志信息，

```
2019-07-21  00:04:26.704 [New I/O worker #4] INFO  org.fortune.doc.server.handler.UploadDocServerHandler - --srcFileName--psb.jpg
2019-07-21  00:04:26.705 [New I/O worker #4] INFO  org.fortune.doc.server.handler.UploadDocServerHandler - 文件上传成功,保存路径为:fortune\w\190721000426_6348.jpg,真实路径为：C:\03_code\idea_workspace\fortune-commons\commons-doc-server-netty\target\commons-doc-server-netty\/fortune\w\190721000426_6348.jpg
2019-07-21  00:04:26.705 [New I/O worker #4] DEBUG org.fortune.doc.server.handler.factory.DocServerHandlerFactory - 执行结果:{"action":"uploadFile","code":1,"filePath":"fortune\\w\\190721000426_6348.jpg","msg":"文件上传成功","success":true}
2019-07-21  00:04:26.705 [New I/O worker #4] INFO  org.fortune.doc.server.support.DocServerHandler - 文件处理结束....FileServerHandlerFactory处理结果：{"action":"uploadFile","code":1,"filePath":"fortune\\w\\190721000426_6348.jpg","msg":"文件上传成功","success":true}
2019-07-21  00:04:26.706 [New I/O worker #4] INFO  org.fortune.doc.server.support.DocServerHandler - 到达文件内容的末尾，进行相应的文件处理操作....end
2019-07-21  00:05:50.539 [http-nio-8080-exec-6] DEBUG org.springframework.web.servlet.DispatcherServlet - GET "/doc-server/fortune//w//190721000426_6348.jpg", parameters={}
2019-07-21  00:05:50.540 [http-nio-8080-exec-6] DEBUG org.springframework.web.servlet.handler.SimpleUrlHandlerMapping - Mapped to org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler@4fcd8ee1
2019-07-21  00:05:50.542 [http-nio-8080-exec-6] DEBUG org.springframework.web.servlet.DispatcherServlet - Completed 200 OK
```

观察服务端target目录下，又多了一个文件，显示如下，

![1563638855753](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/server-file-2.png)

跟日志信息打印出来的路径是一致的，我们可以通过返回的地址信息进行访问文件，

![1563638765629](https://github.com/landy8530/fortune-commons/raw/master/commons-doc-common/docs/netty/server-file-browse.png)

## 5. 常见问题

### 5.1 Maven依赖问题

如果某些jar包无法下载的话，可以手动下载然后自己手动执行maven命令安装到本地仓库即可（Then, install it using the command）。

```
mvn install:install-file -DgroupId=com.fasterxml.jackson.core -DartifactId=jackson-core -Dversion=2.9.9.1 -Dpackaging=jar -Dfile=/path/to/file
```

或者直接利用远程仓库地址进行安装也可（ Alternatively, if you host your own repository you can deploy the file there: ），

```
mvn deploy:deploy-file -DgroupId=com.fasterxml.jackson.core -DartifactId=jackson-core -Dversion=2.9.9.1 -Dpackaging=jar -Dfile=/path/to/file -Durl=[url] -DrepositoryId=[id]
```


# Maven 多项目依赖下的版本控制

通常情况下，一个项目会根据功能划分为多个Module达到模块化的目的，但是这样一来就有一个版本问题，如果需要更改一个版本，如果手动更改，则需要一个个module去修改，会非常繁琐。

## 1. Maven属性介绍

在介绍Maven版本控制之前，先简单介绍一下Maven的属性管理功能。通过<properties>元素用户可以自定义一个或多个Maven属性，然后在POM的其他地方使用${属性名}的方式引用该属性，这种做法的最大意义在于消除重复和统一管理。Maven总共有6类属性，内置属性、POM属性、自定义属性、Settings属性、java系统属性和环境变量属性。

参考链接：

<http://maven.apache.org/guides/introduction/introduction-to-the-pom.html>

<http://maven.apache.org/settings.html>

<http://maven.apache.org/pom.html#Properties>

### 1.1 内置属性

主要有两个常用内置属性（maven预定义，用户可以直接使用的）：

```
${basedir}表示项目根目录，即包含pom.xml文件的目录
${version}表示项目版本
${project.basedir}同${basedir}
```

### 1.2 POM属性

使用POM属性可以引用到pom.xml文件对应的元素的值。

```
${project.build.sourceDirectory}:项目的主源码目录，默认为src/main/java/.
${project.build.testSourceDirectory}:项目的测试源码目录，默认为/src/test/java/.
${project.build.directory}:项目构建输出目录，默认为target/.
${project.outputDirectory}:项目主代码编译输出目录，默认为target/classes/.
${project.testOutputDirectory}:项目测试代码编译输出目录，默认为target/testclasses/.
${project.groupId}:项目的groupId.
${project.artifactId}:项目的artifactId.
${project.version}:项目的version,和${version}等价 
${project.build.finalName}:项目打包输出文件的名称，默认为${project.artifactId}-${project.version}.
```

### 1.3 自定义属性

在pom.xml文件的<properties>标签下定义的maven属性。

### 1.4 Settings属性

与POM属性同理，用户使用以settings. 开头的属性引用settings.xml文件中的XML元素的值。

### 1.5 Java系统属性

所有java系统属性都可以用Maven属性引用，如${user.home}指向了用户目录。使用mvn help:system命令可查看所有的Java系统属性。

> System.getProperties()可得到所有的Java属性。

### 1.6 环境变量属性

所有的环境变量都可以用以env.开头的Maven属性引用，**使用mvn help:system命令可查看所有环境变量**。

```
${env.JAVA_HOME}表示JAVA_HOME环境变量的值。
```

## 2. Maven版本控制方案

使用versions-maven-plugin插件，官网：<http://www.mojohaus.org/versions-maven-plugin/index.html>

### 2.1 插件配置

```xml
<plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>versions-maven-plugin</artifactId>
        <version>2.7</version>
    </plugin>
</plugins>
```

### 2.2 版本配置

当使用此插件在父Maven项目时，运行如下命令将更新全部项目的版本号，包括子项目之间的依赖也都同步更新：

```
mvn versions:set -DnewVersion=1.0.4
```

运行如下结果：

```
[INFO] Scanning for projects...
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Build Order:
[INFO]
[INFO] fortune-commons                                                    [pom]
[INFO] commons-core                                                       [jar]
[INFO] commons-nosql                                                      [jar]
[INFO] commons-datacache                                                  [jar]
[INFO] commons-export                                                     [jar]
[INFO] commons-web                                                        [jar]
[INFO] commons-web-springmvc                                              [jar]
[INFO] fortune-commons-example                                            [war]
[INFO] commons-doc-common                                                 [jar]
[INFO] commons-doc-client-netty                                           [jar]
[INFO] commons-doc-server-netty                                           [war]
[INFO] commons-doc-server-http                                            [war]
[INFO] commons-doc-client-http                                            [jar]
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.7/versions-maven-plugin-2.7.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.7/versions-maven-plugin-2.7.pom (0 B at 0 B/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/mojo-parent/40/mojo-parent-40.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/mojo-parent/40/mojo-parent-40.pom (0 B at 0 B/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.7/versions-maven-plugin-2.7.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/mojo/versions-maven-plugin/2.7/versions-maven-plugin-2.7.jar (0 B at 0 B/s)
[INFO]
[INFO] --------------------< org.fortune:fortune-commons >---------------------
[INFO] Building fortune-commons 1.0.3                                    [1/13]
[INFO] --------------------------------[ pom ]---------------------------------
[INFO]
[INFO] --- versions-maven-plugin:2.7:set (default-cli) @ fortune-commons ---
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-component-annotations/1.5.4/plexus-component-annotations-1.5.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-component-annotations/1.5.4/plexus-component-annotations-1.5.4.pom (0 B at 0 B/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-containers/1.5.4/plexus-containers-1.5.4.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-containers/1.5.4/plexus-containers-1.5.4.pom (0 B at 0 B/s)
.....
[INFO] Searching for local aggregator root...
[INFO] Local aggregation root: C:\03_code\idea_workspace\fortune-commons
[INFO] Processing change of org.fortune:fortune-commons:1.0.3 -> 1.0.4
[INFO] Processing org.fortune:fortune-commons
[INFO]     Updating project org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-core
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-core
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-datacache
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-datacache
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-doc-client-http
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-doc-client-http
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-doc-client-netty
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-doc-client-netty
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-doc-common
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-doc-common
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-doc-server-http
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-doc-server-http
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-doc-server-netty
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-doc-server-netty
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-export
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-export
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-nosql
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-nosql
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-web-springmvc
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-web-springmvc
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:commons-web
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:commons-web
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] Processing org.fortune:fortune-commons-example
[INFO]     Updating parent org.fortune:fortune-commons
[INFO]         from version 1.0.3 to 1.0.4
[INFO]     Updating project org.fortune:fortune-commons-example
[INFO]         from version 1.0.3 to 1.0.4
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for fortune-commons 1.0.3:
[INFO]
[INFO] fortune-commons .................................... SUCCESS [ 26.739 s]
[INFO] commons-core ....................................... SKIPPED
[INFO] commons-nosql ...................................... SKIPPED
[INFO] commons-datacache .................................. SKIPPED
[INFO] commons-export ..................................... SKIPPED
[INFO] commons-web ........................................ SKIPPED
[INFO] commons-web-springmvc .............................. SKIPPED
[INFO] fortune-commons-example ............................ SKIPPED
[INFO] commons-doc-common ................................. SKIPPED
[INFO] commons-doc-client-netty ........................... SKIPPED
[INFO] commons-doc-server-netty ........................... SKIPPED
[INFO] commons-doc-server-http ............................ SKIPPED
[INFO] commons-doc-client-http ............................ SKIPPED
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  29.875 s
[INFO] Finished at: 2019-06-16T00:21:07+08:00
[INFO] ------------------------------------------------------------------------


```

当进入到子Maven项目时，运行如下命令将更新全部项目对子项目引用的版本号：

```
mvn versions:set -DnewVersion=2.1-SNAPSHOT
```

当更改版本号时有问题，可以通过以下命令进行版本号回滚：

```
mvn versions:revert
```

如果一切都没有问题，那就直接提交版本号：

```
mvn versions:commit
```

提交显示如下信息：

```
......
[INFO] Reactor Summary for fortune-commons 1.0.4:
[INFO]
[INFO] fortune-commons .................................... SUCCESS [  0.727 s]
[INFO] commons-core ....................................... SUCCESS [  0.019 s]
[INFO] commons-nosql ...................................... SUCCESS [  0.017 s]
[INFO] commons-datacache .................................. SUCCESS [  0.020 s]
[INFO] commons-export ..................................... SUCCESS [  0.024 s]
[INFO] commons-web ........................................ SUCCESS [  0.017 s]
[INFO] commons-web-springmvc .............................. SUCCESS [  0.024 s]
[INFO] fortune-commons-example ............................ SUCCESS [  0.022 s]
[INFO] commons-doc-common ................................. SUCCESS [  0.019 s]
[INFO] commons-doc-client-netty ........................... SUCCESS [  0.022 s]
[INFO] commons-doc-server-netty ........................... SUCCESS [  0.022 s]
[INFO] commons-doc-server-http ............................ SUCCESS [  0.018 s]
[INFO] commons-doc-client-http ............................ SUCCESS [  0.020 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.318 s
[INFO] Finished at: 2019-06-16T00:41:26+08:00
[INFO] ------------------------------------------------------------------------

```

## 3. Maven 打包遇到的问题

### 3.1 外部Jar包不存在

在Maven的打包过程中，如果遇到项目中使用的类库不存在的情况，则需要引入相应的jar包，比如本项目中打包的过程就出现过`com.sun.image.codec.jpeg`不存在的问题，解决方案如下，利用maven打包编译插件解决。

#### 3.1.1 包含进相应jar包

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.0</version>
    <configuration>
        <source>${maven.compiler.source}</source> <!-- 源代码使用的开发版本 -->
        <target>${maven.compiler.target}</target> <!-- 需要生成的目标class文件的编译版本 -->
        <!-- 这下面的是可选项 -->
        <meminitial>128m</meminitial>
        <maxmem>512m</maxmem>
        <fork>true</fork> <!-- fork is enable,用于明确表示编译版本配置的可用 -->
        <encoding>${project.build.sourceEncoding}</encoding>
        <compilerVersion>${maven.compiler}</compilerVersion>
        <!-- 这个选项用来传递编译器自身不包含但是却支持的参数选项 -->
        <compilerArguments>
            <verbose/>
            <bootclasspath>${env.JAVA_HOME}\jre\lib\rt.jar;${env.JAVA_HOME}\jre\lib\jce.jar</bootclasspath>
        </compilerArguments>
    </configuration>
</plugin>
```

#### 3.1.2 包含jar的文件夹

```xml
<compilerArguments>
    <extdirs>${env.JAVA_HOME}/jre/lib</extdirs>
</compilerArguments>

```

#### 3.1.3 把jar放到webapp/WEB-INF/lib路径下

```xml
<compilerArguments>
    <extdirs>${basedir}/src/main/webapp/WEB-INF/lib</extdirs>
</compilerArguments>
```

#### 3.1.4 注意点

> - <bootclasspath><extdirs>两个标签，如果配置多个数据，mac,linux用冒号(:)，而windows用分号(;)
> - <bootclasspath><extdirs>两个标签，windows路径用\，mac，linux用/

### 3.2 打包控制台出现乱码

> Setting->maven->runner
>
> - VMoptions: -Dfile.encoding=GB2312

* [Idea基于Maven配置多JDK版本](#idea基于maven配置多jdk版本)
  * [1. 配置settings.xml](#1-配置settingsxml)
  * [2. 配置pom.xml](#2-配置pomxml)
  * [3. 配置project structure](#3-配置project-structure)
    * [3.1 配置SDK](#31-配置sdk)
    * [3.2 检查Modules相应SDK配置](#32-检查modules相应sdk配置)
  * [4. 配置Maven插件](#4-配置maven插件)
    * [4.1 Maven基本配置](#41-maven基本配置)
    * [4.2 配置Maven运行的JDK](#42-配置maven运行的jdk)
  * [5. 配置编译JDK](#5-配置编译jdk)
  * [6. 编译打包](#6-编译打包)
    * [6.1 使用Maven插件](#61-使用maven插件)
    * [6.2 使用命令](#62-使用命令)

# Idea基于Maven配置多JDK版本

如果在本地开发环境中安装了多个JDK版本的话，需要在idea中自由切换，就需要利用Maven进行配置，配置后就可以非常方便的进行版本切换。配置步骤如下：

## 1. 配置settings.xml

在Maven的本地配置中，添加以下配置，配置JAVA_HOME和JAVA_VERSION两个属性。

```xml
<profiles>
    <profile>
        <id>java6-compiler</id>
        <properties>
            <JAVA_HOME>C:\SoftCommon\Java\jdk1.6.0_45</JAVA_HOME>
            <JAVA_VERSION>1.6</JAVA_VERSION>
        </properties>
    </profile>
    <profile>
        <id>java7-compiler</id>
        <properties>
            <JAVA_HOME>C:\SoftCommon\Java\jdk1.7.0_67</JAVA_HOME>
            <JAVA_VERSION>1.7</JAVA_VERSION>
        </properties>
    </profile>
    <profile>
        <id>java8-compiler</id>
        <properties>
            <JAVA_HOME>C:\SoftCommon\Java\jdk1.8.0_202</JAVA_HOME>
            <JAVA_VERSION>1.8</JAVA_VERSION>
        </properties>
        <!-- activeByDefault=true代表如果不指定某个固定id的profile，那么就使用这个环境 -->
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
    </profile>
    <profile>
        <id>java9-compiler</id>
        <properties>
            <JAVA_HOME>C:\SoftCommon\Java\jdk-9</JAVA_HOME>
            <JAVA_VERSION>1.9</JAVA_VERSION>
        </properties>
    </profile>
    <profile>
        <id>java11-compiler</id>
        <properties>
            <JAVA_HOME>C:\SoftCommon\Java\jdk-11.0.2</JAVA_HOME>
            <JAVA_VERSION>11</JAVA_VERSION>
        </properties>
    </profile>
</profiles>
```

## 2. 配置pom.xml

在相应的项目工程中配置pom.xml中配置如下，主要是添加一个build标签，添加maven-compiler-plugin编译插件。

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.home>${JAVA_HOME}</java.home>
    <java.version>${JAVA_VERSION}</java.version>
    <maven.compiler.source>${JAVA_VERSION}</maven.compiler.source>
    <maven.compiler.target>${JAVA_VERSION}</maven.compiler.target>
</properties>
<build>
    <!--pluginmanagement标签一般用在父pom中，子元素可以包含plugins插件-->
    <pluginManagement>
        <plugins>
            <!-- 一个好习惯，就是在此配置JDK的版本，这样就可以方便代码迁移 By Landy 2019.01.04-->
            <!--一般而言，target与source是保持一致的。但是，有时候为了让程序能在其他版本的jdk中运行(对于低版本目标jdk，源代码中需要没有使用低版本jdk中不支持的语法)，会存在target不同于source的情况 。-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.7.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source> <!-- 源代码使用的开发版本 -->
                    <target>${maven.compiler.target}</target> <!-- 需要生成的目标class文件的编译版本 -->
                    <!-- 这下面的是可选项 -->
                    <meminitial>128m</meminitial>
                    <maxmem>512m</maxmem>
                    <fork>true</fork> <!-- fork is enable,用于明确表示编译版本配置的可用 -->
                    <compilerVersion>${java.version}</compilerVersion>
                    <!-- 这个选项用来传递编译器自身不包含但是却支持的参数选项 -->
                    <!--<compilerArgument>-verbose -bootclasspath ${java.home}\lib\rt.jar</compilerArgument>-->
                    <executable>${java.home}/bin/javac</executable>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```

注意：pluginmanagement主要是用于父工程（module），然后在相应的子工程（module）中添加如下配置即可加入maven-compiler-plugin插件，这样就可以让子工程决定是否加载相应的Maven插件。

```xml
<build>
    <!--然后，在子pom文件中就可以这样使用,省去了版本、配置等信息，只需指定groupId和artifactId即可。-->
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

以上配置详情可以参看本例项目配置。配置完成刷新一下Maven插件（reimport），即可展示上面所配置的profile列表。如下所示：

![Maven_Profiles](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Maven_Profiles.png)

## 3. 配置project structure

### 3.1 配置SDK

打开idea中的project structure，配置相应的project sdk和project language level，配置如下（本案例采用JDK11），

![Idea_project_structure](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Idea_project_structure.png)

### 3.2 检查Modules相应SDK配置

在Modules中有可能会没能自动刷新相应的sdk language level配置，这时候就需要手动设置。

![Sdk_language_level](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Sdk_language_level.png)

注意：经过本人现场测试，发现idea 2018.03版本的情况，他只能自动刷新的language level是JDK1.8+的版本。操作方法：点击Maven插件中的reimport，如下所示，

![Maven_Profiles](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Maven_Profiles.png)

## 4. 配置Maven插件

### 4.1 Maven基本配置

在Idea中需要配置一下Maven settings.xml文件和Maven本地仓库的位置，如果有本地安装的Maven也可以进行配置（没有则使用idea默认自带的Maven）。file --> settings --> maven 如下所示：

![Maven_settings_config](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Maven_settings_config.png)

### 4.2 配置Maven运行的JDK

为了每次切换了Project JDK后，不需要再次进行设置可以选择Project JDK选项。如下所示，

![Maven_Runner_JRE](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Maven_Runner_JRE.png)

## 5. 配置编译JDK

这时候，一般情况下，经过上面步骤，基本上就就可以进行相应的maven编译打包的操作了，但是有时候会遇到如下的问题，

```
Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.7.0:compile (default-compile) on project xxxx Fatal error compiling: 无效的标记: -parameters -> [Help 1]
```

这个错误是由于你项目所需jdk版本和你当前使用的jdk版本不一致导致的，因为我项目的pom.xml中定义了java版本为11，但是我实际idea中run这个项目却是1.8.

要是你在intellij idea里面的maven窗口点击的打包编译的话，就在intellij idea设置项目jdk版本，直接Ctrl+Alt+s进入设置界面。

![Java_compiler_config](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Java_compiler_config.png)

## 6. 编译打包

通过以上步骤就可以利用maven进行编译打包的操作了。

### 6.1 使用Maven插件

如果使用idea，则可以直接使用maven插件进行打包操作。如下图所示，

![Maven_compile_package](https://github.com/landy8530/deep-in-java/raw/master/00DOC/Maven_compile_package.png)

### 6.2 使用命令

如果使用Maven命令，如下所示，

`mvn -s D:\mvn_repository\settings.xml clean package -P java11-compiler`

java11-compiler即为上面配置的jdk版本的profile id，并且需要制定相应的settings文件即可。


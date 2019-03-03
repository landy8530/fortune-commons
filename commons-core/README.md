# Commons Core Component

## 1. Spring @Value用法

### 1.1 两种用法

- @Value("#{configProperties['key']}")
- @Value("${key}")

### 1.2 配置

#### 1.2.1 @Value("#{configProperties['key']}")使用

##### 1.2.1.1 配置文件

配置方法1.

```xml
<bean id="configProperties" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
    <property name="locations">
        <list>
            <value>classpath:value.properties</value>
        </list>
    </property>
</bean>
```

配置方法2

```xml
<util:properties id="configProperties" location="classpath:value.properties"></util:properties>
```

> 注：配置1和配置2等价，这种方法需要util标签，要引入util的xsd：
>
> http://www.springframework.org/schema/util
>
> http://www.springframework.org/schema/util/spring-util-3.0.xsd"

##### 1.2.1.2 使用

value.properties

```properties
key=1
```

```java
@Component
public class ValueDemo {
    @Value("#{configProperties['key']}")
    private String value;
 
    public String getValue() {
        return value;
    }
}
```

#### 1.2.2 @Value("${key}")使用

##### 1.2.2.1 配置文件

方法1：在1.2.1.1的配置文件基础上增加：

```xml
<bean id="propertyConfigurer" class="org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer">
    <property name="properties" ref="configProperties"/>
</bean>
```

方法2：直接指定配置文件，完整的配置：

```xml
<bean id="appProperty"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
    <property name="locations">
        <array>
            <value>classpath:value.properties</value>
        </array>
    </property>
</bean>
```

##### 1.2.2.2 使用

```java
@Component
public class ValueDemo {
    @Value("${key}")
    private String value;
 
    public String getValue() {
        return value;
    }
}
```


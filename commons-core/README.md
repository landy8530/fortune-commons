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

## 2. 对象拷贝用法及性能比较

在现在的很多业务系统中，基本上都会使用对象之间的拷贝操作，当属性非常多的时候，自然就需要一些工具来操作对象之间的拷贝操作。

### 2.1 目前流行对象拷贝工具类

#### 2.1.1 Apache版本（反射机制）

- org.apache.commons.beanutils.BeanUtils#copyProperties
- org.apache.commons.beanutils.PropertyUtils#copyProperties

#### 2.1.2 Spring版本（反射机制）

- org.springframework.beans.BeanUtils#copyProperties(java.lang.Object, java.lang.Object)

#### 2.1.3 CGLib版本（动态代理）

- org.springframework.cglib.beans.BeanCopier#copy
  - Spring3.2以后把CGLib的jar包集成进Spring core包中了
- net.sf.cglib.beans.BeanCopier#copy

### 2.2 实现原理

#### 2.2.1 Apache版本

使用静态类调用，最终转化为两个单例的工具对象。默认的初始化BeanUtilsBean方法如下：

```java
/**
 * <p>Constructs an instance using new property
 * and conversion instances.</p>
*/
public BeanUtilsBean() {
    this(new ConvertUtilsBean(), new PropertyUtilsBean());
}
```

默认的获取BeanUtilsBean实例方法如下：

```java
/**
     * Contains <code>BeanUtilsBean</code> instances indexed by context classloader.
     */
private static final ContextClassLoaderLocal<BeanUtilsBean>
            BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal<BeanUtilsBean>() {
                        // Creates the default instance used when the context classloader is unavailable
                        @Override
                        protected BeanUtilsBean initialValue() {
                            return new BeanUtilsBean();
                        }
                    };

/**
     * Gets the instance which provides the functionality for {@link BeanUtils}.
     * This is a pseudo-singleton - an single instance is provided per (thread) context classloader.
     * This mechanism provides isolation for web apps deployed in the same container.
     *
     * @return The (pseudo-singleton) BeanUtils bean instance
     */
public static BeanUtilsBean getInstance() {
    return BEANS_BY_CLASSLOADER.get();
}
```

ConvertUtilsBean可以通过ConvertUtils全局自定义注册。

`org.apache.commons.beanutils.ConvertUtilsBean#register(org.apache.commons.beanutils.Converter, java.lang.Class<?>)`

PropertyUtilsBean的copyProperties方法实现了拷贝的算法。

- 动态bean：orig instanceof DynaBean：Object value = ((DynaBean)orig).get(name);然后把value复制到动态bean类
- Map类型：orig instanceof Map：key值逐个拷贝
- 其他普通类：从beanInfo【每一个对象都有一个缓存的bean信息，包含属性字段等】取出name，然后把sourceClass和targetClass逐个拷贝
  - 即采用`PropertyDescriptor`类实现

#### 2.2.2 Spring版本

也是采用`PropertyDescriptor`类的实现，不过跟Apache版本有细微的差别。通过targetPd的writeMethod和sourcePd的readMethod进行操作，如果targetPd的writeMethod的第一个参数类型跟readMethod的返回值类型一致，则会进行复制操作。

```java
for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
						catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
```

#### 2.2.3 CGLib版本

copier = BeanCopier.create(source.getClass(), target.getClass(), false);

copier.copy(source, target, null);

Create对象过程：产生sourceClass-》TargetClass的拷贝代理类，放入jvm中，所以创建的代理类的时候比较耗时。最好保证这个对象的单例模式，可以参照本工程中的做法。Copy属性过程：调用生成的代理类，代理类的代码和手工操作的代码很类似，效率非常高。

创建过程：源码见net.sf.cglib.beans.BeanCopier.Generator.generateClass(ClassVisitor)

1. 获取sourceClass的所有public get 方法-》PropertyDescriptor[] getters
2. 获取TargetClass 的所有 public set 方法-》PropertyDescriptor[] setters
3. 遍历setters的每一个属性，执行4和5
4. 按setters的name生成sourceClass的所有setter方法-》PropertyDescriptor getter【不符合javabean规范的类将会可能出现空指针异常】
5. PropertyDescriptor[] setters-》PropertyDescriptor setter
6. 将setter和getter名字和类型 配对，生成代理类的拷贝方法。

### 2.3 缺陷对比

https://blog.csdn.net/jianhua0902/article/details/8155368

| 功能说明                                            | Apache- PropertyUtils | Apache- BeanUtils                       | Spring-  BeanUtils | Cglib-BeanCopier     |
| --------------------------------------------------- | --------------------- | --------------------------------------- | ------------------ | -------------------- |
| 是否可以扩展useConvete功能                          | NO                    | YES                                     | YES                | YES(难用)            |
| （sourceObject，targetObject）的顺序                | 逆序                  | 逆序                                    | 顺序               | 顺序                 |
| 对sourceObject特殊属性的限制：(Date，BigDecimal等） | OK                    | OK                                      | OK                 | OK                   |
| 相同属性名，且类型不匹配时候的处理                  | 异常                  | OK，并能进行初级转换，Long和Integer互转 | 拷贝部分属性       | OK，但是该属性不拷贝 |
| Get和set方法不匹配的处理                            | OK                    | OK                                      | OK                 | OK                   |

### 2.4 性能对比

通过实际试验（1000000次对象拷贝操作），得出以下结论（实际代码请参看github），

- CGLib BeanCopier花费时间:1111ms
- Spring BeanUtils花费时间:1462ms
- Apache BeanUtils花费时间:4109ms
- Apache PropertyUtils花费时间:2833ms

由试验也得出CGLib具备高性能，效率高的优点。次之为Spring BeanUtils。


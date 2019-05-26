package org.fortune.commons.datacache.conf;

/**
 * @author: Landy
 * @date: 2019/4/6 23:41
 * @description:
 */

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * 在此，必须要使用@PropertySource注解导入需要预先导入的一些资源属性文件，比如在这里的一些前期配置：data.cache.common.cacheStrategy=memcached
 * 因为，org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass(org.springframework.context.annotation.ConfigurationClass, org.springframework.context.annotation.ConfigurationClassParser.SourceClass)
 * 中可以清晰的看到，解析processPropertySource(propertySource);方法比this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN))
 * 这个条件化配置方法提前，所以如果没有用@PropertySource注解，则在相应的条件化注解的实现类中，则读取不到需要根据配置文件中动态配置的条件，如
 * @ConditionalOnProperty(value = "data.cache.strategy",havingValue = "memcached")
 * 注意：这个类需要在调用方（客户端）配置（比如Web端的配置）
 */
@Configuration
@ComponentScan("org.fortune.commons")
@PropertySource(value = "classpath:applicationContext.properties")
@Import({LocalConfig.class,MemCachedConfig.class,MongoConfig.class})
public class DataCacheConfiguration {

}

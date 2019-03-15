package org.landy.commons.core.setting.conf;

import org.landy.commons.core.setting.Settings2PropertiesFactoryBean;
import org.landy.commons.core.setting.SettingsFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class SettingsConfiguration {
    private static final String FILE_ENCODING = "UTF-8";
    private static final String PROPERTY_SOURCE_KEY_DATA_CACHE = "data-cache";

    @Bean(name="settingsFactoryBean")
    public SettingsFactoryBean settingsFactoryBean() {
        return new SettingsFactoryBean();
    }

    @Bean(name="applicationProperties")
    public PropertiesFactoryBean applicationProperties(@Qualifier("settingsFactoryBean") SettingsFactoryBean settingsFactoryBean) throws Exception {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Settings2PropertiesFactoryBean settings2PropertiesFactoryBean = new Settings2PropertiesFactoryBean();
        settings2PropertiesFactoryBean.setSettings(settingsFactoryBean.getObject());
        propertiesFactoryBean.setProperties(settings2PropertiesFactoryBean.getObject());
        return propertiesFactoryBean;
    }

    /**
     * PlaceholderConfigurer相当于以下配置
     *      <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer" >
     *         <property name="locations">
     *             <value>application.properties</value>
     *         </property>
     *         <property name="fileEncoding" value="UTF-8" />
     *     </bean>
     *     或者
     *     <context:property-placeholder location="classpath:application.properties" file-encoding="UTF-8" />
     */
//    @Bean("propertyPlaceholderConfigurer")
//    public static final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(@Qualifier("applicationProperties") PropertiesFactoryBean applicationProperties) throws IOException {
//        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
//        propertyPlaceholderConfigurer.setProperties(applicationProperties.getObject());
//        propertyPlaceholderConfigurer.setFileEncoding(FILE_ENCODING);
//        return propertyPlaceholderConfigurer;
//    }
    @Bean("propertyPlaceholderConfigurer")
    public static final PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer(@Qualifier("applicationProperties") PropertiesFactoryBean applicationProperties) throws IOException {
        final PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        //在这里，通过使用自定义的settings转化为properties
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("config/data-cache.yml"));
//        propertyPlaceholderConfigurer.setProperties(yaml.getObject());

        Properties properties = applicationProperties.getObject();
        propertyPlaceholderConfigurer.setProperties(properties);
//        Map<String, Object> map = new HashMap<String, Object>((Map) properties);
//        PropertySource propertySource = new MapPropertySource(PROPERTY_SOURCE_KEY_DATA_CACHE,map);
//        MutablePropertySources propertySources = new MutablePropertySources();
//        propertySources.addFirst(propertySource);
//        propertyPlaceholderConfigurer.setPropertySources(propertySources);

//        SettingsEnvironment settingsEnvironment = new SettingsEnvironment(propertySource);
//        propertyPlaceholderConfigurer.setEnvironment(settingsEnvironment);
        propertyPlaceholderConfigurer.setFileEncoding(FILE_ENCODING);
        return propertyPlaceholderConfigurer;
    }

    static class SettingsEnvironment extends StandardEnvironment {
        private PropertySource propertySource;
        public SettingsEnvironment(PropertySource propertySource) {
            this.propertySource = propertySource;
        }

        @Override
        protected void customizePropertySources(MutablePropertySources propertySources) {
            super.customizePropertySources(propertySources);
            propertySources.addFirst(propertySource);
        }
    }
}

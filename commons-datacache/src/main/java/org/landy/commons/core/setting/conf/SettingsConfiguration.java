package org.landy.commons.core.setting.conf;

import org.landy.commons.core.setting.Settings2PropertiesFactoryBean;
import org.landy.commons.core.setting.SettingsFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@ComponentScan("org.landy.commons.core.setting")
@Configuration
public class SettingsConfiguration {
    private static final String FILE_ENCODING = "UTF-8";

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
    @Bean("propertyPlaceholderConfigurer")
    public static final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(@Qualifier("applicationProperties") PropertiesFactoryBean applicationProperties) throws IOException {
        final PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setProperties(applicationProperties.getObject());
        propertyPlaceholderConfigurer.setFileEncoding(FILE_ENCODING);
        return propertyPlaceholderConfigurer;
    }
}

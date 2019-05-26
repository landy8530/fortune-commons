package org.fortune.commons.core.setting;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Properties;

public class Settings2PropertiesFactoryBean implements FactoryBean<Properties> {

    @Autowired
    private Settings settings;

    @Override
    public Properties getObject() {
        Properties result = new Properties();
        result.putAll(settings.getAsMap());
        return result;
    }

    @Override
    public Class<Properties> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}

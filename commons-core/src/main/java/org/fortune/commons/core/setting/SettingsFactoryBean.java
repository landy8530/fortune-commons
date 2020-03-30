package org.fortune.commons.core.setting;

import org.fortune.commons.core.setting.exception.ResolveFailedConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

public class SettingsFactoryBean extends AbstractFactoryBean<Settings> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsFactoryBean.class);

    private SettingsFileConfig settingsFileConfig = new SettingsFileConfig();

    @Override
    protected Settings createInstance() {
        ImmutableSettings.DefaultSettingsBuilder builder = new ImmutableSettings.DefaultSettingsBuilder();
        try {
            Resource[] resources = resolveConfig();
            for (Resource resource : resources) {
                LOGGER.info("Start to resolve the config file: {}", resource.getURL());
                builder.loadFromUrl(resource.getURL());
            }
        } catch (IOException e) {
            throw new ResolveFailedConfigException("Failed to resolve config path ["+ settingsFileConfig.getConfigPath()+"]", e);
        }
        return builder.build();
    }

    @Override
    public Class<?> getObjectType() {
        return Settings.class;
    }

    private Resource[] resolveConfig() throws IOException {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources(settingsFileConfig.getConfigPath());
        return resources;
    }

    public void setConfigFilePath(String configFilePath) {
        settingsFileConfig.setConfigPath(configFilePath);
    }
}

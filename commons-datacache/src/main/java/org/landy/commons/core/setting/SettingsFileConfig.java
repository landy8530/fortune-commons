package org.landy.commons.core.setting;

public class SettingsFileConfig {

    public final static String DEFAULT_CONFIG_PATH =  "classpath*:config/*.yml";

    private String configPath = DEFAULT_CONFIG_PATH;

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }
}

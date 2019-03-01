package org.landy.commons.core.setting.loader;

public class SettingsLoaderFactory {

    private SettingsLoaderFactory() {
    }

    public static SettingsLoader createSettingsLoader() {
        return new YamlSettingsLoader();
    }
}

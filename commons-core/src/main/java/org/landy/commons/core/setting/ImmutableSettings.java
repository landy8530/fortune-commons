package org.landy.commons.core.setting;

import org.landy.commons.core.setting.exception.SettingsException;
import org.landy.commons.core.setting.loader.SettingsLoader;
import org.landy.commons.core.setting.loader.SettingsLoaderFactory;
import org.landy.commons.core.setting.util.StreamUtil;
import org.landy.commons.core.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class ImmutableSettings implements Settings {
    private static final String FILE_ENCODING = "UTF-8";

    private Map<String, String> settings;

    private ImmutableSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    @Override
    public Map<String, String> getAsMap() {
        return Collections.unmodifiableMap(settings);
    }

    @Override
    public Settings getByPrefix(String prefix) {
        DefaultSettingsBuilder builder = new DefaultSettingsBuilder();
        for (Map.Entry<String, String> entry : getAsMap().entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                if (entry.getKey().length() < prefix.length()) {
                    // ignore this one
                    continue;
                }
                builder.put(entry.getKey().substring(prefix.length()), entry.getValue());
            }
        }
        return builder.build();
    }

    @Override
    public List<String> getAsList(String prefix) {
        Settings setting = getByPrefix(prefix);
        return new ArrayList<String>(setting.getAsMap().values());
    }

    @Override
    public String get(String setting) {
        String retVal = settings.get(setting);
        if (retVal != null) {
            return retVal;
        }
        return settings.get(StringUtil.toCamelCase(setting));
    }

    @Override
    public String get(String setting, String defaultValue) {
        String retVal = settings.get(setting);
        return retVal == null ? defaultValue : retVal;
    }

    @Override
    public Float getAsFloat(String setting) throws SettingsException {
        return getAsFloat(setting, null);
    }

    @Override
    public Float getAsFloat(String setting, Float defaultValue) throws SettingsException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Float.parseFloat(sValue);
        } catch (NumberFormatException e) {
            throw new SettingsException("Failed to parse float setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Double getAsDouble(String setting) throws SettingsException {
        return getAsDouble(setting, null);
    }

    @Override
    public Double getAsDouble(String setting, Double defaultValue) throws SettingsException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(sValue);
        } catch (NumberFormatException e) {
            throw new SettingsException("Failed to parse double setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }
    @Override
    public Integer getAsInt(String setting) throws SettingsException {
        return getAsInt(setting, null);
    }

    @Override
    public Integer getAsInt(String setting, Integer defaultValue) throws SettingsException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException e) {
            throw new SettingsException("Failed to parse int setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Long getAsLong(String setting) throws SettingsException {
        return getAsLong(setting, null);
    }

    @Override
    public Long getAsLong(String setting, Long defaultValue) throws SettingsException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        try {
            return Long.parseLong(sValue);
        } catch (NumberFormatException e) {
            throw new SettingsException("Failed to parse long setting [" + setting + "] with value [" + sValue + "]", e);
        }
    }

    @Override
    public Boolean getAsBoolean(String setting) throws SettingsException {
        return getAsBoolean(setting, null);
    }

    @Override
    public Boolean getAsBoolean(String setting, Boolean defaultValue) throws SettingsException {
        String sValue = get(setting);
        if (sValue == null) {
            return defaultValue;
        }
        return !("false".equals(sValue) || "0".equals(sValue) || "off".equals(sValue) || "no".equals(sValue));
    }

    public static class DefaultSettingsBuilder implements Settings.Builder {

        private final Map<String, String> map = new LinkedHashMap<String, String>();

        public DefaultSettingsBuilder put(Settings settings) {
            map.putAll(settings.getAsMap());
            return this;
        }

        public DefaultSettingsBuilder loadFromUrl(URL url) throws SettingsException {
            try {
                return loadFromStream(url.toExternalForm(),url.openStream());
            } catch (IOException e) {
                throw new SettingsException("Failed to open stream for url [" + url.toExternalForm() + "]", e);
            }
        }

        public DefaultSettingsBuilder loadFromStream(String resourceName, InputStream is) throws SettingsException {
            SettingsLoader settingsLoader = SettingsLoaderFactory.createSettingsLoader();
            try {
                Map<String, String> loadedSettings = settingsLoader.load(StreamUtil.copyToString(new InputStreamReader(is, FILE_ENCODING)));
                put(loadedSettings);
            } catch (Exception e) {
                throw new SettingsException("Failed to load settings from [" + resourceName + "]", e);
            }
            return this;
        }

        public DefaultSettingsBuilder put(Map<String, String> settings) {
            map.putAll(settings);
            return this;
        }

        public DefaultSettingsBuilder put(String key, String value) {
            map.put(key, value);
            return this;
        }


        @Override
        public Settings build() {
            return new ImmutableSettings(map);
        }
    }
}

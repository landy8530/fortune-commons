package org.fortune.commons.core.setting;

import org.fortune.commons.core.setting.exception.SettingsException;

import java.util.List;
import java.util.Map;

public interface Settings {

    Map<String, String> getAsMap();

    Settings getByPrefix(String prefix);

    List<String> getAsList(String prefix);

    String get(String setting);

    String get(String setting, String defaultValue);

    Float getAsFloat(String setting) throws SettingsException;

    Float getAsFloat(String setting, Float defaultValue) throws SettingsException;

    Double getAsDouble(String setting) throws SettingsException;

    Double getAsDouble(String setting, Double defaultValue) throws SettingsException;

    Integer getAsInt(String setting) throws SettingsException;

    Integer getAsInt(String setting, Integer defaultValue) throws SettingsException;

    Long getAsLong(String setting) throws SettingsException;

    Long getAsLong(String setting, Long defaultValue) throws SettingsException;

    Boolean getAsBoolean(String setting) throws SettingsException;

    Boolean getAsBoolean(String setting, Boolean defaultValue) throws SettingsException;


    interface Builder {

        /**
         * Builds the settings.
         */
        Settings build();
    }
}

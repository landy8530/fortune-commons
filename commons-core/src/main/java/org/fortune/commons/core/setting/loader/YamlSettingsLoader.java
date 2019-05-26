package org.fortune.commons.core.setting.loader;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

public class YamlSettingsLoader implements SettingsLoader {


    @Override
    public Map<String, String> load(String source) throws IOException {
        // replace tabs with whitespace (yaml does not accept tabs, but many users might use it still...)
        source = source.replace("\t", "  ");
        Yaml yaml = new Yaml();
        Map<Object, Object> yamlMap = (Map<Object, Object>) yaml.load(source);
        return Helper.loadNestedFromMap(yamlMap);
    }

}

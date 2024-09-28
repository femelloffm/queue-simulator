package org.pucrs.br.util;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    private final Map<String, Object> config;

    public ConfigLoader(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new RuntimeException("Configuration file not found: " + filePath);
            }
            this.config = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Error loading configuration file.", e);
        }
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}

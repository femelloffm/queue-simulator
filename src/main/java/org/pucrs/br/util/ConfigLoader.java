package org.pucrs.br.util;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {
    private final Map<String, Object> config;

    // Construtor que realiza a leitura do YAML
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

    // Obtém a configuração completa
    public Map<String, Object> getConfig() {
        return config;
    }

    // Métodos para acessar as seções específicas do YAML

    // Obtém a configuração das filas (queues)
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getQueuesConfig() {
        return (List<Map<String, Object>>) config.get("queues");
    }

    // Obtém a configuração das chegadas (arrivals)
    @SuppressWarnings("unchecked")
    public Map<String, Object> getArrivalsConfig() {
        return (Map<String, Object>) config.get("arrivals");
    }

    // Obtém a configuração da rede (network)
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getNetworkConfig() {
        return (List<Map<String, Object>>) config.get("network");
    }

    // Obtém os números pseudo-aleatórios, se existirem
    @SuppressWarnings("unchecked")
    public List<Double> getRndNumbers() {
        return (List<Double>) config.get("rndnumbers");
    }

    // Obtém as sementes (seeds) para geração de números aleatórios, se existirem
    @SuppressWarnings("unchecked")
    public List<Integer> getSeeds() {
        return (List<Integer>) config.get("seeds");
    }

    // Obtém a quantidade de números pseudo-aleatórios por semente
    public int getRndNumbersPerSeed() {
        return (int) config.getOrDefault("rndnumbersPerSeed", 0);  // Retorna 0 se não estiver definido
    }
}

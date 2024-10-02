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

    // Obtém a configuração das filas (queues)
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, Object>> getQueuesConfig() {
        Object queuesObj = config.get("queues");
        if (queuesObj instanceof Map<?, ?>) {
            return (Map<String, Map<String, Object>>) queuesObj;
        }
        System.out.println("Queues object is of type: " + (queuesObj != null ? queuesObj.getClass() : "null"));
        throw new RuntimeException("Invalid queues configuration format");
    }

    // Obtém a configuração das chegadas (arrivals)
    @SuppressWarnings("unchecked")
    public Map<String, Object> getArrivalsConfig() {
        Object arrivalsObj = config.get("arrivals");
        if (arrivalsObj instanceof Map<?, ?>) {
            return (Map<String, Object>) arrivalsObj;
        }
        throw new RuntimeException("Invalid arrivals configuration format");
    }

    // Obtém a configuração da rede (network)
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getNetworkConfig() {
        Object networkObj = config.get("network");
        if (networkObj instanceof List<?>) {
            return (List<Map<String, Object>>) networkObj;
        }
        throw new RuntimeException("Invalid network configuration format");
    }

    // Obtém os números pseudo-aleatórios, se existirem
    @SuppressWarnings("unchecked")
    public List<Double> getRndNumbers() {
        Object rndNumbersObj = config.get("rndnumbers");
        if (rndNumbersObj instanceof List<?>) {
            return (List<Double>) rndNumbersObj;
        }
        throw new RuntimeException("Invalid rndnumbers configuration format");
    }

    // Obtém as sementes (seeds) para geração de números aleatórios, se existirem
    @SuppressWarnings("unchecked")
    public List<Integer> getSeeds() {
        Object seedsObj = config.get("seeds");
        if (seedsObj instanceof List<?>) {
            return (List<Integer>) seedsObj;
        }
        throw new RuntimeException("Invalid seeds configuration format");
    }

    // Obtém a quantidade de números pseudo-aleatórios por semente
    public int getRndNumbersPerSeed() {
        Object rndNumbersPerSeedObj = config.get("rndnumbersPerSeed");
        if (rndNumbersPerSeedObj instanceof Integer) {
            return (int) rndNumbersPerSeedObj;
        }
        throw new RuntimeException("Invalid rndnumbersPerSeed configuration format");
    }
}
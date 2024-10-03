package org.pucrs.br;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.DestinationProbabilty;
import org.pucrs.br.dto.Event;
import org.pucrs.br.simulator.GeneralQueueSimulator;
import org.pucrs.br.util.ConfigLoader;
import org.pucrs.br.util.EventComparator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    private static final String QUEUE_NAME_PREFIX = "Q";
    private static final int randomNumberCount = 100000; // Número de números aleatórios
    private static final double firstArrivalTime = 2.0; // Tempo do primeiro evento de chegada

    public static void main(String[] args) {
        String filePath = "/queue-config.yaml"; // Substitua pelo caminho correto do seu arquivo YAML
        ConfigLoader configLoader = new ConfigLoader(filePath);

        try {
            Map<String, Map<String, Object>> queueConfig = configLoader.getQueuesConfig();
            List<Map<String, Object>> networkConfig = configLoader.getNetworkConfig();

            // Verificação adicional
            if (queueConfig == null) {
                throw new RuntimeException("Queue configuration is null.");
            }

            // Inicializa o gerador de números aleatórios
            RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(
                    16544105325.0, // a
                    13147,         // c
                    Math.pow(2, 35), // m
                    0,             // semente inicial (exemplo)
                    randomNumberCount
            );

            // Cria a fila de eventos
            PriorityQueue<Event> scheduler = new PriorityQueue<>(new EventComparator());

            // Cria um array de filas a partir da configuração
            Queue[] queues = new Queue[queueConfig.size()]; // O número de filas é igual ao tamanho da configuração

            int index = 0;
            for (String queueName : queueConfig.keySet()) {
                Map<String, Object> queueDetails = queueConfig.get(queueName);

                // Verificações e extração dos parâmetros
                int servers = queueDetails.containsKey("servers") ? (int) queueDetails.get("servers") : 1; // Valor padrão
                int capacity = queueDetails.containsKey("capacity") ? (int) queueDetails.get("capacity") : Integer.MAX_VALUE; // Valor padrão
                double arrivalMinTime = queueDetails.containsKey("minArrival") ? (double) queueDetails.get("minArrival") : 0.0;
                double arrivalMaxTime = queueDetails.containsKey("maxArrival") ? (double) queueDetails.get("maxArrival") : 0.0;
                double serviceMinTime = queueDetails.containsKey("minService") ? (double) queueDetails.get("minService") : 0.0;
                double serviceMaxTime = queueDetails.containsKey("maxService") ? (double) queueDetails.get("maxService") : 0.0;

                // Obtém as probabilidades de cada um dos roteamentos da fila, ordenados de menos para mais provavel
                List<DestinationProbabilty> queueNetworkProbabilities = networkConfig.stream()
                        .filter(config -> config.get("source").equals(queueName))
                        .map(Main::mapNetworkConfigToProbability)
                        .sorted(Comparator.comparingDouble(DestinationProbabilty::probability))
                        .toList();

                // Cria a fila com a configuração carregada
                queues[index++] = new Queue(servers, capacity, arrivalMinTime, arrivalMaxTime, serviceMinTime, serviceMaxTime, queueNetworkProbabilities);
            }

            // Cria uma instância do simulador de filas
            GeneralQueueSimulator generalQueueSimulator = new GeneralQueueSimulator(randomNumberGenerator, scheduler, queues, firstArrivalTime);

            // Executa a simulação
            generalQueueSimulator.simulate();

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static DestinationProbabilty mapNetworkConfigToProbability(Map<String, Object> config) {
        int destinationIndex;
        String targetString = (String) config.get("target");
        if (targetString.equals("exit")) {
            destinationIndex = -1;
        } else {
            String queueNumberAsString = targetString.split(QUEUE_NAME_PREFIX)[1];
            destinationIndex = Integer.parseInt(queueNumberAsString) - 1;
        }

        double probability = (double) config.get("probability");
        return new DestinationProbabilty(destinationIndex, probability);
    }
}

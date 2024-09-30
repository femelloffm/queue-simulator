package org.pucrs.br;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.simulator.GeneralQueueSimulator; // Altere para importar o GeneralQueueSimulator
import org.pucrs.br.util.ConfigLoader;
import org.pucrs.br.util.EventComparator;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    private static final int randomNumberCount = 100000;
    private static final double firstArrivalTime = 2.0;

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader("/queue-config.yaml");
        Map<String, Object> config = configLoader.getConfig();

        // Inicializando o gerador de números aleatórios
        List<Double> rndNumbers = configLoader.getRndNumbers();
        List<Integer> seeds = configLoader.getSeeds();
        int rndNumbersPerSeed = configLoader.getRndNumbersPerSeed();

        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(
                16544105325.0, // a
                13147,         // c
                Math.pow(2, 35), // m
                seeds.get(0), // Usando a primeira semente para o gerador
                randomNumberCount
        );

        PriorityQueue<Event> scheduler = new PriorityQueue<>(new EventComparator());

        List<Map<String, Object>> queuesConfig = configLoader.getQueuesConfig();
        Queue[] queues = new Queue[queuesConfig.size()];
        for (int i = 0; i < queuesConfig.size(); i++) {
            Map<String, Object> queueConfig = queuesConfig.get(i);
            int servers = (int) queueConfig.get("servers");
            int capacity = (int) queueConfig.get("capacity");
            double arrivalMinTime = queueConfig.containsKey("arrival") ?
                    (double) ((Map<String, Object>) queueConfig.get("arrival")).get("min") : 0;
            double arrivalMaxTime = queueConfig.containsKey("arrival") ?
                    (double) ((Map<String, Object>) queueConfig.get("arrival")).get("max") : 0;
            double serviceMinTime = (double) ((Map<String, Object>) queueConfig.get("service")).get("min");
            double serviceMaxTime = (double) ((Map<String, Object>) queueConfig.get("service")).get("max");

            queues[i] = new Queue(servers, capacity, arrivalMinTime, arrivalMaxTime, serviceMinTime, serviceMaxTime);
        }

        GeneralQueueSimulator generalQueueSimulator = new GeneralQueueSimulator(randomNumberGenerator, scheduler, queues, firstArrivalTime);

        generalQueueSimulator.simulate();
    }
}
package org.pucrs.br;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.simulator.SimpleQueueSimulator;
import org.pucrs.br.util.ConfigLoader;
import org.pucrs.br.util.EventComparator;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {
    private static final double a = 16544105325.0;
    private static final double c = 13147;
    private static final double m = Math.pow(2, 35);
    private static final double seed = 53492443;

    private static final int randomNumberCount = 100000;
    private static final double firstArrivalTime = 2.0;

    public static void main(String[] args) {
        // Inicializando o gerador de números aleatórios
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(a, c, m, seed, randomNumberCount);
        PriorityQueue<Event> scheduler = new PriorityQueue<>(new EventComparator());

        // Carregando configurações do arquivo YAML
        ConfigLoader configLoader = new ConfigLoader("/queue-config.yaml");
        Map<String, Object> config = configLoader.getConfig();
        List<Map<String, Object>> queuesConfig = (List<Map<String, Object>>) config.get("queues");

        // Inicializando as filas a partir da configuração
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

        // Criando o simulador de filas
        SimpleQueueSimulator simpleQueueSimulator = new SimpleQueueSimulator(randomNumberGenerator, scheduler, queues[0], queues[1], firstArrivalTime);

        // Iniciando a simulação
        simpleQueueSimulator.simulate();
    }
}

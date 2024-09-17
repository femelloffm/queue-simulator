package org.pucrs.br;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.simulator.SimpleQueueSimulator;
import org.pucrs.br.util.EventComparator;

import java.util.PriorityQueue;

public class Main {
    private static final double a = 16544105325.0;
    private static final double c = 13147;
    private static final double m = Math.pow(2, 35);
    private static final double seed = 53492443;

    // Fila 1 (G/G/2/3): 2 servidores, capacidade 3
    private static final int firstQueueServers = 2;
    private static final int firstQueueCapacity = 3;
    private static final double firstQueueArrivalMinTime = 1.0;
    private static final double firstQueueArrivalMaxTime = 4.0;
    private static final double firstQueueServiceMinTime = 3.0;
    private static final double firstQueueServiceMaxTime = 4.0;

    // Fila 2 (G/G/1/5): 1 servidor, capacidade 5
    private static final int secondQueueServers = 1;
    private static final int secondQueueCapacity = 5;
    private static final double secondQueueServiceMinTime = 2.0;
    private static final double secondQueueServiceMaxTime = 3.0;

    private static final int randomNumberCount = 100000;
    private static final double firstArrivalTime = 2.0;

    public static void main(String[] args) {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(a, c, m, seed, randomNumberCount);
        PriorityQueue<Event> scheduler = new PriorityQueue<>(new EventComparator());

        // Inicializando a primeira fila (G/G/2/3)
        Queue firstQueue = new Queue(firstQueueServers, firstQueueCapacity, firstQueueArrivalMinTime,
                firstQueueArrivalMaxTime, firstQueueServiceMinTime, firstQueueServiceMaxTime);

        // Inicializando a segunda fila (G/G/1/5)
        Queue secondQueue = new Queue(secondQueueServers, secondQueueCapacity, firstQueueArrivalMinTime,
                firstQueueArrivalMaxTime, secondQueueServiceMinTime, secondQueueServiceMaxTime);

        SimpleQueueSimulator simpleQueueSimulator = new SimpleQueueSimulator(randomNumberGenerator, scheduler,
                firstQueue, secondQueue, firstArrivalTime);
        simpleQueueSimulator.simulate();
    }
}
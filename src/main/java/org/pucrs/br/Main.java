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
    private static final int numberOfServers = 1;
    private static final int queueCapacity = 5;
    private static final double arrivalMinTime = 2.0;
    private static final double arrivalMaxTime = 5.0;
    private static final double serviceMinTime = 3.0;
    private static final double serviceMaxTime = 5.0;
    private static final int randomNumberCount = 100000;
    private static final double firstArrivalTime = 2.0;

    public static void main(String[] args) {
        RandomNumberGenerator randomNumberGenerator = new RandomNumberGenerator(a, c, m, seed, randomNumberCount);
        PriorityQueue<Event> scheduler = new PriorityQueue<>(new EventComparator());
        Queue firstQueue = new Queue(numberOfServers, queueCapacity, arrivalMinTime, arrivalMaxTime, serviceMinTime, serviceMaxTime);
        Queue secondQueue = new Queue(numberOfServers, queueCapacity, arrivalMinTime, arrivalMaxTime, serviceMinTime, serviceMaxTime);

        SimpleQueueSimulator simpleQueueSimulator = new SimpleQueueSimulator(randomNumberGenerator, scheduler,
                firstQueue, secondQueue, firstArrivalTime);
        simpleQueueSimulator.simulate();
    }
}
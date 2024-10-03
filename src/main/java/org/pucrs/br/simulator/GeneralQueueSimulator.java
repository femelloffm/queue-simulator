package org.pucrs.br.simulator;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.DestinationProbabilty;
import org.pucrs.br.dto.Event;
import org.pucrs.br.dto.EventType;

import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

public class GeneralQueueSimulator {
    private final RandomNumberGenerator randomNumberGenerator;
    private final Queue[] queues;
    private final PriorityQueue<Event> scheduler;
    private double globalTime;

    public GeneralQueueSimulator(RandomNumberGenerator randomNumberGenerator, PriorityQueue<Event> scheduler,
                                 Queue[] queues, double firstArrivalTime) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.scheduler = scheduler;
        this.queues = queues;
        this.globalTime = 0.0;

        // Inicializa o primeiro evento de chegada
        scheduler.add(new Event(EventType.IN, firstArrivalTime, -1, 0));
    }

    public void simulate() {
        while (randomNumberGenerator.getCount() > 0) {
            Event event = nextEvent();
            if (event.type() == EventType.IN) {
                entry(event);
            } else if (event.type() == EventType.OUT) {
                exit(event);
            } else if (event.type() == EventType.PASS) {
                pass(event);
            }
        }
        showStatistics();
    }

    private void entry(Event event) {
        accumulateTime(event.time());
        boolean success = queues[event.destinationIndex()].in();
        if (success && queues[event.destinationIndex()].status() <= queues[event.destinationIndex()].getServers()) {
            scheduleEventByProbability(event.destinationIndex());
        }
        scheduleEntry(event.destinationIndex());
    }

    private void exit(Event event) {
        accumulateTime(event.time());
        queues[event.sourceIndex()].out();
        if (queues[event.sourceIndex()].status() >= queues[event.sourceIndex()].getServers()) {
            scheduleEventByProbability(event.sourceIndex());
        }
    }

    private void pass(Event event) {
        accumulateTime(event.time());
        queues[event.sourceIndex()].out();
        if (queues[event.sourceIndex()].status() >= queues[event.sourceIndex()].getServers()) {
            scheduleEventByProbability(event.sourceIndex());
        }
        boolean success = queues[event.destinationIndex()].in();
        if (success && queues[event.destinationIndex()].status() <= queues[event.destinationIndex()].getServers()) {
            scheduleExit(event.destinationIndex());
        }
    }

    private void scheduleEntry(int queueIndex) {
        double entryTime = ((queues[queueIndex].getMaxArrival() - queues[queueIndex].getMinArrival()) * randomNumberGenerator.next()) + queues[queueIndex].getMinArrival();
        scheduler.add(new Event(EventType.IN, globalTime + entryTime, -1, queueIndex));
    }

    private void scheduleExit(int queueIndex) {
        double exitTime = ((queues[queueIndex].getMaxService() - queues[queueIndex].getMinService()) * randomNumberGenerator.next()) + queues[queueIndex].getMinService();
        scheduler.add(new Event(EventType.OUT, globalTime + exitTime, queueIndex, -1));
    }

    private void schedulePass(int sourceIndex, int destinationIndex) {
        double passTime = ((queues[sourceIndex].getMaxService() - queues[sourceIndex].getMinService()) * randomNumberGenerator.next()) + queues[sourceIndex].getMinService();
        scheduler.add(new Event(EventType.PASS, globalTime + passTime, sourceIndex, destinationIndex));
    }

    private Event nextEvent() {
        Event nextEvent = scheduler.poll();
        if (Objects.isNull(nextEvent)) {
            throw new RuntimeException("There are no events scheduled");
        }

        return nextEvent;
    }

    private void accumulateTime(double eventTime) {
        for (Queue queue : queues) {
            queue.updateTime(eventTime - globalTime);  // Atualiza o tempo de cada fila
        }
        globalTime = eventTime;
    }

    private void showStatistics() {
        System.out.println("---------- SIMULATION COMPLETED ----------");
        System.out.println("Global time = " + globalTime);
        for (int i = 0; i < queues.length; i++) {
            System.out.println("--> QUEUE " + (i + 1));
            queues[i].showStatistics(globalTime);  // Exibe as estatísticas da fila
        }
    }

    private void scheduleEventByProbability(int sourceQueueIndex) {
        int destinationIndex = decideDestination(sourceQueueIndex);
        if (destinationIndex == -1) {
            scheduleExit(sourceQueueIndex);
        } else {
            schedulePass(sourceQueueIndex, destinationIndex);
        }
    }

    private int decideDestination(int sourceQueueIndex) {
        List<DestinationProbabilty> queueProbabilities = queues[sourceQueueIndex].getProbabilities();
        double randomNumber = randomNumberGenerator.next();
        for (DestinationProbabilty destinationProbabilty : queueProbabilities) {
            if (randomNumber <= destinationProbabilty.probability()) {
                return destinationProbabilty.destinationIndex();
            }
        }
        return queueProbabilities.getLast().destinationIndex();
    }
}
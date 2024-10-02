package org.pucrs.br.simulator;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.dto.EventType;

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
        scheduler.add(new Event(EventType.IN, firstArrivalTime));
    }

    public void simulate() {
        while (randomNumberGenerator.getCount() > 0) {
            Event event = nextEvent();
            if (event.getType() == EventType.IN) {
                entry(event);
            } else if (event.getType() == EventType.OUT) {
                exit(event);
            } else if (event.getType() == EventType.PASS) {
                pass(event);
            }
        }
        showStatistics();
    }

    private void entry(Event event) {
        accumulateTime(event.getTime());
        boolean success = queues[0].in();
        if (success && queues[0].status() <= queues[0].getServers()) {
            schedulePass(0);
        } else if (!success) {
            queues[0].loss();  // Cliente perdido
        }
        scheduleEntry();
    }

    private void exit(Event event) {
        accumulateTime(event.getTime());
        queues[queues.length - 1].out();
    }

    private void pass(Event event) {
        accumulateTime(event.getTime());
        for (int i = 0; i < queues.length - 1; i++) {
            if (queues[i].status() >= queues[i].getServers()) {
                schedulePass(i);
            }
            boolean success = queues[i + 1].in();
            if (success && queues[i + 1].status() <= queues[i + 1].getServers()) {
                scheduleExit(i + 1);
            } else if (!success) {
                queues[i + 1].loss();  // Cliente perdido na próxima fila
            }
        }
    }

    private void scheduleEntry() {
        double entryTime = ((queues[0].getMaxArrival() - queues[0].getMinArrival()) * randomNumberGenerator.next()) + queues[0].getMinArrival();
        scheduler.add(new Event(EventType.IN, globalTime + entryTime));
    }

    private void scheduleExit(int queueIndex) {
        double exitTime = ((queues[queueIndex].getMaxService() - queues[queueIndex].getMinService()) * randomNumberGenerator.next()) + queues[queueIndex].getMinService();
        scheduler.add(new Event(EventType.OUT, globalTime + exitTime));
    }

    private void schedulePass(int queueIndex) {
        double passTime = ((queues[queueIndex].getMaxService() - queues[queueIndex].getMinService()) * randomNumberGenerator.next()) + queues[queueIndex].getMinService();
        scheduler.add(new Event(EventType.PASS, globalTime + passTime));
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

    public void showStatistics() {
        System.out.println("---------- SIMULATION COMPLETED ----------");
        System.out.println("Global time = " + globalTime);
        for (int i = 0; i < queues.length; i++) {
            System.out.println("--> QUEUE " + (i + 1));
            queues[i].showStatistics(globalTime);  // Exibe as estatísticas da fila
        }
    }
}
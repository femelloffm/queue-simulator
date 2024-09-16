package org.pucrs.br.simulator;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.dto.EventType;

import java.util.Objects;
import java.util.PriorityQueue;

public class SimpleQueueSimulator {
    private final RandomNumberGenerator randomNumberGenerator;
    private final Queue firstQueue;
    private final Queue secondQueue;
    private final PriorityQueue<Event> scheduler;
    private double globalTime;

    public SimpleQueueSimulator(RandomNumberGenerator randomNumberGenerator, PriorityQueue<Event> scheduler,
                                Queue firstQueue, Queue secondQueue, double firstArrivalTime) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.scheduler = scheduler;
        this.firstQueue = firstQueue;
        this.secondQueue = secondQueue;
        this.globalTime = 0.0;

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
        boolean success = firstQueue.in();
        if (success && firstQueue.status() <= firstQueue.getServers()) {
            schedulePass();
        }
        scheduleEntry();
    }

    private void exit(Event event) {
        accumulateTime(event.getTime());
        secondQueue.out();
        if (secondQueue.status() >= secondQueue.getServers()) {
            scheduleExit();
        }
    }

    private void pass(Event event) {
        accumulateTime(event.getTime());
        firstQueue.out();
        if (firstQueue.status() >= firstQueue.getServers()) {
            schedulePass();
        }
        boolean success = secondQueue.in();
        if (success && secondQueue.status() <= secondQueue.getServers()) {
            scheduleExit();
        }
    }

    private void scheduleEntry() {
        double entryTime = ((firstQueue.getMaxArrival() - firstQueue.getMinArrival()) * randomNumberGenerator.next()) + firstQueue.getMinArrival();
        scheduler.add(new Event(EventType.IN, globalTime + entryTime));
    }

    private void scheduleExit() {
        double exitTime = ((secondQueue.getMaxService() - secondQueue.getMinService()) * randomNumberGenerator.next()) + secondQueue.getMinService();
        scheduler.add(new Event(EventType.OUT, globalTime + exitTime));
    }

    private void schedulePass() {
        double passTime = ((firstQueue.getMaxService() - firstQueue.getMinService()) * randomNumberGenerator.next()) + firstQueue.getMinService();
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
        firstQueue.updateTime(eventTime - globalTime);
        secondQueue.updateTime(eventTime - globalTime);
        globalTime = eventTime;
    }

    private void showStatistics() {
        System.out.println("---------- SIMULATION COMPLETED ----------");
        System.out.println("Global time = " + globalTime);
        System.out.println("--> FIRST QUEUE");
        firstQueue.showStatistics(globalTime);
        System.out.println("--> SECOND QUEUE");
        secondQueue.showStatistics(globalTime);
    }
}
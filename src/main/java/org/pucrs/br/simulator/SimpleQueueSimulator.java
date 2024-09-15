package org.pucrs.br.simulator;

import org.pucrs.br.component.Queue;
import org.pucrs.br.component.RandomNumberGenerator;
import org.pucrs.br.dto.Event;
import org.pucrs.br.dto.EventType;

import java.util.Objects;
import java.util.PriorityQueue;

public class SimpleQueueSimulator {
    private final RandomNumberGenerator randomNumberGenerator;
    private final Queue queue;
    private final PriorityQueue<Event> scheduler;
    private double globalTime;

    public SimpleQueueSimulator(RandomNumberGenerator randomNumberGenerator, PriorityQueue<Event> scheduler,
                                Queue queue, double firstArrivalTime) {
        this.randomNumberGenerator = randomNumberGenerator;
        this.scheduler = scheduler;
        this.queue = queue;
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
            }
        }
        queue.showStatistics(globalTime);
    }

    private void entry(Event event) {
        boolean success = queue.in(event.getTime() - globalTime);
        globalTime = event.getTime();
        if (success) {
            if (queue.status() <= queue.getServers()) {
                scheduleExit();
            }
        }
        scheduleEntry();
    }

    private void exit(Event event) {
        queue.out(event.getTime() - globalTime);
        globalTime = event.getTime();
        if (queue.status() >= queue.getServers()) {
            scheduleExit();
        }
    }

    private void scheduleEntry() {
        double entryTime = ((queue.getMaxArrival() - queue.getMinArrival()) * randomNumberGenerator.next()) + queue.getMinArrival();
        scheduler.add(new Event(EventType.IN, globalTime + entryTime));
    }

    private void scheduleExit() {
        double exitTime = ((queue.getMaxService() - queue.getMinService()) * randomNumberGenerator.next()) + queue.getMinService();
        scheduler.add(new Event(EventType.OUT, globalTime + exitTime));
    }

    private Event nextEvent() {
        Event nextEvent = scheduler.poll();
        if (Objects.isNull(nextEvent)) {
            throw new RuntimeException("There are no events scheduled");
        }

        return nextEvent;
    }
}
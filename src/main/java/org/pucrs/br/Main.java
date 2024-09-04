package org.pucrs.br;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final double a = 16544105325.0;
    private static final double c = 13147;
    private static final double m = Math.pow(2, 35);
    private static final double seed = 53492443;
    private static final int numberOfServers = 1;
    private static final int queueCapacity = 5;
    private static final int entryMinTime = 2;
    private static final int entryMaxTime = 5;
    private static final int serviceMinTime = 3;
    private static final int serviceMaxTime = 5;
    private static final double[] queueTimes = new double[queueCapacity + 1];
    private static final List<Event> scheduledEvents = initializeScheduler();
    private static int randomNumberCount = 100000;
    private static int lossCount = 0;
    private static double previous = seed;
    private static int queueCurrentSize = 0;
    private static double globalTime = 0.0;

    public static void main(String[] args) {
        while (randomNumberCount > 0) {
            Event event = nextEvent();
            if (event.getType() == EventType.ENTRY) {
                entry(event);
            } else if (event.getType() == EventType.EXIT) {
                exit(event);
            }
        }
        showSimulationStats();
    }

    private static void entry(Event event) {
        updateTimeCount(event.getTime());
        if (queueCurrentSize < queueCapacity) {
            queueCurrentSize++;
            if (queueCurrentSize <= numberOfServers) {
                scheduleExit();
            }
        } else {
            lossCount++;
        }
        scheduleEntry();
    }

    private static void exit(Event event) {
        updateTimeCount(event.getTime());
        queueCurrentSize--;
        if (queueCurrentSize >= numberOfServers) {
            scheduleExit();
        }
    }

    private static void scheduleEntry() {
        double entryTime = ((entryMaxTime - entryMinTime) * nextRandom()) + entryMinTime;
        scheduledEvents.add(new Event(EventType.ENTRY, globalTime + entryTime));
    }

    private static void scheduleExit() {
        double exitTime = ((serviceMaxTime - serviceMinTime) * nextRandom()) + serviceMinTime;
        scheduledEvents.add(new Event(EventType.EXIT, globalTime + exitTime));
    }

    private static void updateTimeCount(double eventTime) {
        double duration = eventTime - globalTime;
        queueTimes[queueCurrentSize] = queueTimes[queueCurrentSize] + duration;

        globalTime = eventTime;
    }

    private static Event nextEvent() {
        Event event = scheduledEvents.stream()
                .min(Comparator.comparingDouble(Event::getTime))
                .orElseThrow(() -> new RuntimeException("There are no events scheduled"));
        scheduledEvents.remove(event);

        return event;
    }

    private static double nextRandom() {
        previous = ((a * previous) + c) % m;
        randomNumberCount--;
        return previous / m;
    }

    private static void showSimulationStats() {
        System.out.println("---------- SIMULATION COMPLETED ----------");
        System.out.println("Global time = " + globalTime);
        System.out.println("Time spent in each queue state:");
        for (int index = 0; index <= queueCapacity; index++) {
            BigDecimal probability = new BigDecimal(queueTimes[index] / globalTime)
                    .setScale(10, RoundingMode.HALF_UP);
            System.out.println(index + ": " + queueTimes[index] + " (" + probability + "%)");
        }
        System.out.println("Loss count = " + lossCount);
    }

    private static List<Event> initializeScheduler() {
        List<Event> eventList = new ArrayList<>();
        eventList.add(new Event(EventType.ENTRY, 2.0));
        return eventList;
    }
}
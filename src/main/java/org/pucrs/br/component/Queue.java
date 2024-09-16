package org.pucrs.br.component;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Queue {
    private final int servers;
    private final int capacity;
    private final double minArrival;
    private final double maxArrival;
    private final double minService;
    private final double maxService;
    private final double[] times;
    private int customerCount;
    private int lossCount;

    public Queue(final int servers, final int capacity, final double minArrival, final double maxArrival,
                 final double minService, final double maxService) {
        this.servers = servers;
        this.capacity = capacity;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.customerCount = 0;
        this.lossCount = 0;
        this.times = new double[capacity + 1];
    }

    public int status() {
        return customerCount;
    }

    public int getServers() {
        return servers;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getMinArrival() {
        return minArrival;
    }

    public double getMaxArrival() {
        return maxArrival;
    }

    public double getMinService() {
        return minService;
    }

    public double getMaxService() {
        return maxService;
    }

    public void loss() {
        lossCount++;
    }

    public boolean in() {
        if (customerCount < capacity) {
            customerCount++;
            return true;
        } else {
            lossCount++;
            return false;
        }
    }

    public void updateTime(double duration) {
        times[customerCount] += duration;
    }

    public void out() {
        customerCount--;
    }

    public void showStatistics(double globalTime) {
        System.out.println("Time spent in each queue state:");
        for (int index = 0; index <= capacity; index++) {
            BigDecimal probability = new BigDecimal(times[index] / globalTime)
                    .setScale(10, RoundingMode.HALF_UP);
            System.out.println(index + ": " + times[index] + " (" + probability + "%)");
        }
        System.out.println("Loss count = " + lossCount);
    }
}

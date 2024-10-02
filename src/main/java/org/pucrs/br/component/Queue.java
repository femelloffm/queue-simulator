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
    private final double[] times; // Array to track time spent in each state
    private int customerCount; // Current number of customers in the queue
    private int lossCount; // Counter for lost customers
    private int servedCount; // Total number of served customers

    // Constructor
    public Queue(final int servers, final int capacity, final double minArrival, final double maxArrival,
                 final double minService, final double maxService) {
        this.servers = servers;
        this.capacity = capacity;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.customerCount = 0; // Initially no customers
        this.lossCount = 0; // Initially no customers are lost
        this.servedCount = 0; // Initially no customers served
        this.times = new double[capacity + 1]; // Array to track time for each possible queue state
    }

    // Returns the current number of customers in the queue
    public int status() {
        return customerCount;
    }

    // Getter methods for queue configuration parameters
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

    // Increment the number of lost customers (when the queue is full)
    public void loss() {
        lossCount++;
    }

    // Method to handle a new customer trying to enter the queue
    public boolean in() {
        if (customerCount < capacity) {
            customerCount++; // Increment customer count if there's space
            return true;
        } else {
            lossCount++; // Increment loss count if the queue is full
            return false;
        }
    }

    // Update the time spent in the current queue state (based on customer count)
    public void updateTime(double duration) {
        times[customerCount] += duration;
    }

    // Method to handle a customer exiting the queue
    public void out() {
        if (customerCount > 0) {
            customerCount--; // Decrease the customer count when someone leaves
            servedCount++; // Increment the count of served customers
        }
    }

    // Show statistics at the end of the simulation
    public void showStatistics(double globalTime) {
        System.out.println("Time spent in each queue state:");
        for (int index = 0; index <= capacity; index++) {
            BigDecimal probability = new BigDecimal(times[index] / globalTime * 100) // Probability as a percentage
                    .setScale(2, RoundingMode.HALF_UP); // Round to two decimal places
            System.out.println("State " + index + " customers: " + times[index] + " units (" + probability + "% of total time)");
        }
        System.out.println("Number of lost customers = " + lossCount);
        System.out.println("Total customers served = " + servedCount);
    }

    // Method to reset the queue for a new simulation
    public void reset() {
        customerCount = 0;
        lossCount = 0;
        servedCount = 0;
        for (int i = 0; i <= capacity; i++) {
            times[i] = 0; // Reset time tracking
        }
    }
}
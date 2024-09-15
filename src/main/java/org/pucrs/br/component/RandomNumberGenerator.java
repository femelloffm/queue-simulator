package org.pucrs.br.component;

public class RandomNumberGenerator {
    private final double a;
    private final double c;
    private final double m;
    private int count;
    private double previous;

    public RandomNumberGenerator(final double a, final double c, final double m, final double seed, final int maxCount) {
        this.a = a;
        this.c = c;
        this.m = m;
        this.count = maxCount;
        this.previous = seed;
    }

    public double next() {
        previous = ((a * previous) + c) % m;
        count--;
        return previous / m;
    }

    public int getCount() {
        return count;
    }
}

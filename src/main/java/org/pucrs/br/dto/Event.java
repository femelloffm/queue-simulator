package org.pucrs.br.dto;

public class Event {
    private final EventType type;
    private final Double time;
    private int sourceIndex;
    private int destinationIndex;

    public Event(EventType type, Double time) {
        this.type = type;
        this.time = time;
    }

    public Event(EventType type, Double time, int sourceIndex) {
        this.type = type;
        this.time = time;
        this.sourceIndex = sourceIndex;
    }

    public Event(EventType type, Double time, int sourceIndex, int destinationIndex) {
        this.type = type;
        this.time = time;
        this.sourceIndex = sourceIndex;
        this.destinationIndex = destinationIndex;
    }

    public EventType getType() {
        return type;
    }

    public Double getTime() {
        return time;
    }

    public int getSourceIndex() {
        return sourceIndex;
    }

    public int getDestinationIndex() {
        return destinationIndex;
    }
}

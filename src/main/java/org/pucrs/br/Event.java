package org.pucrs.br;

public class Event {
    private EventType type;
    private Double time;

    public Event(EventType type, Double time) {
        this.type = type;
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public Double getTime() {
        return time;
    }
}

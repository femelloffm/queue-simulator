package org.pucrs.br.util;

import org.pucrs.br.dto.Event;

import java.io.Serializable;
import java.util.Comparator;

public class EventComparator implements Serializable, Comparator<Event> {
    private static final long serialVersionUID = 7035536459374704283L;

    @Override
    public int compare(Event firstEvent, Event secondEvent) {
       return Double.compare(firstEvent.time(), secondEvent.time());
    }

}
package org.pucrs.br.dto;

public record Event(EventType type, Double time, int sourceIndex, int destinationIndex) {
}

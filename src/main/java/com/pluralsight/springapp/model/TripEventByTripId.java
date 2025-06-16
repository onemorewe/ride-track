package com.pluralsight.springapp.model;

import com.pluralsight.springapp.controller.dto.TripEvent;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("trip_events_by_trip_id")
@Data
public class TripEventByTripId {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID tripId;
    private UUID eventId;
    private EventType eventType;
    private Instant timeStamp;

    public TripEventByTripId(TripEvent tripEvent) {
        this.tripId = UUID.fromString(tripEvent.getTripId());
        this.eventId = UUID.fromString(tripEvent.getEventId());
        this.eventType = EventType.valueOf(tripEvent.getEventType());
        this.timeStamp = LocalDateTime.parse(tripEvent.getEventTime()).toInstant(java.time.ZoneOffset.UTC);
    }

    public boolean isInOrder(TripEvent tripEvent) {
        return this.eventType == EventType.REQUESTED ||
                this.eventType.getValue() < EventType.valueOf(tripEvent.getEventType()).getValue();
        //TODO
    }

    @RequiredArgsConstructor
    @Getter
    public enum EventType {
        REQUESTED(0),
        DRIVER_ACCEPTED(1),
        DRIVER_ARRIVED(2),
        RIDE_STARTED(3),
        RIDE_ENDED(4),
        PAYMENT_CAPTURED(5);
        private final int value;
    }
}


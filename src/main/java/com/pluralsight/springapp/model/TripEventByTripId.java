package com.pluralsight.springapp.model;

import com.pluralsight.springapp.controller.dto.TripEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("trip_events_by_trip_id")
@Data
@NoArgsConstructor
public class TripEventByTripId {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID tripId;
    private UUID driverId;
    private UUID riderId;
    private UUID eventId;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private EventType eventType;
    private Instant timeStamp;

    public TripEventByTripId(TripEvent tripEvent, UUID eventId) {
        this.eventId = eventId;
        this.tripId = UUID.fromString(tripEvent.getTripId());
        this.driverId = tripEvent.getDriverId() != null ? UUID.fromString(tripEvent.getDriverId()) : null;
        this.riderId = tripEvent.getRiderId() != null ? UUID.fromString(tripEvent.getRiderId()) : null;
        this.eventType = EventType.valueOf(tripEvent.getEventType());
        this.timeStamp = Instant.now();
    }

    public boolean isInOrder(TripEvent tripEvent) {
        return this.eventType.getOrder() <= EventType.valueOf(tripEvent.getEventType()).getOrder();
    }


}


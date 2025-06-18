package com.pluralsight.springapp.model;

import com.pluralsight.springapp.controller.dto.TripEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("trip_event_by_driver_id")
@Data
@NoArgsConstructor
public class TripEventByDriverId {
    private UUID tripId;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID driverId;
    private UUID riderId;
    private EventType eventType;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED)
    private Instant timeStamp;

    public TripEventByDriverId(TripEvent tripEvent) {
        this.tripId = UUID.fromString(tripEvent.getTripId());
        this.driverId = UUID.fromString(tripEvent.getDriverId());
        this.riderId = tripEvent.getRiderId() != null ? UUID.fromString(tripEvent.getRiderId()) : null;
        this.eventType = EventType.valueOf(tripEvent.getEventType());
        this.timeStamp = Instant.now();
    }
}

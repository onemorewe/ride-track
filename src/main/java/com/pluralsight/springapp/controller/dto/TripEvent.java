package com.pluralsight.springapp.controller.dto;

import com.pluralsight.springapp.model.TripEventByDriverId;
import com.pluralsight.springapp.model.TripEventByTripId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TripEvent {
    private String tripId;
    private String driverId;
    private String riderId;
    private String eventType;
    private String eventTime;

    public TripEvent(TripEventByTripId tripEventByTripId) {
        this.tripId = tripEventByTripId.getTripId().toString();
        this.driverId = tripEventByTripId.getDriverId() != null ? tripEventByTripId.getDriverId().toString() : null;
        this.riderId = tripEventByTripId.getRiderId() != null ? tripEventByTripId.getRiderId().toString() : null;
        this.eventType = tripEventByTripId.getEventType().name();
        this.eventTime = tripEventByTripId.getTimeStamp().toString();
    }

    public TripEvent(TripEventByDriverId tripEventByDriverId) {
        this.tripId = tripEventByDriverId.getTripId().toString();
        this.driverId = tripEventByDriverId.getDriverId().toString();
        this.riderId = tripEventByDriverId.getRiderId() != null ? tripEventByDriverId.getRiderId().toString() : null;
        this.eventType = tripEventByDriverId.getEventType().name();
        this.eventTime = tripEventByDriverId.getTimeStamp().toString();
    }
}

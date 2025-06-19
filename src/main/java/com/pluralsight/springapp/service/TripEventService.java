package com.pluralsight.springapp.service;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.exception.NotInOrderException;
import com.pluralsight.springapp.model.EventType;
import com.pluralsight.springapp.model.TripEventByDriverId;
import com.pluralsight.springapp.model.TripEventByTripId;
import com.pluralsight.springapp.repository.TripEventByDriverIdRepository;
import com.pluralsight.springapp.repository.TripEventByTripIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.CassandraBatchOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripEventService {

    private final TripEventByTripIdRepository tripEventByTripIdRepository;
    private final TripEventByDriverIdRepository tripEventByDriverIdRepository;
    private final CassandraTemplate cassandraTemplate;

    public void createTripEvent(TripEvent tripEvent) {
        TripEventByTripId trip = tripEventByTripIdRepository.findFirstByTripId(UUID.fromString(tripEvent.getTripId()));
        if (trip != null && !trip.isInOrder(tripEvent)) {
            throw new NotInOrderException("Trip event is not in order" +
                                          " for tripId: " + tripEvent.getTripId() +
                                          ", eventType: " + tripEvent.getEventType() +
                                          ", lastEventType: " + trip.getEventType());
        }
        UUID eventId = Uuids.timeBased();
        CassandraBatchOperations operations = cassandraTemplate.batchOps()
                .insert(new TripEventByDriverId(tripEvent, eventId))
                .insert(new TripEventByTripId(tripEvent, eventId));
        if (tripEvent.getEventType().equalsIgnoreCase(EventType.RIDE_STARTED.name())) {
            saveTrips(tripEvent, operations);
        }
        operations.execute();
    }

    private static void saveTrips(TripEvent tripEvent, CassandraBatchOperations operations) {
        operations.addStatement(SimpleStatement.builder(
                "INSERT INTO trip_by_driver_id (trip_id, driver_id, rider_id, trip_start_time) " +
                "VALUES (?, ?, ?, ?) IF NOT EXISTS")
                .addPositionalValues(
                        UUID.fromString(tripEvent.getTripId()),
                        UUID.fromString(tripEvent.getDriverId()),
                        tripEvent.getRiderId() != null ? UUID.fromString(tripEvent.getRiderId()) : null,
                        tripEvent.getEventTime()
                ).build()
        );
        operations.addStatement(SimpleStatement.builder(
                 "INSERT INTO trip_by_rider_id (trip_id, driver_id, rider_id, trip_start_time) " +
                 "VALUES (?, ?, ?, ?) IF NOT EXISTS")
                 .addPositionalValues(
                         UUID.fromString(tripEvent.getTripId()),
                         UUID.fromString(tripEvent.getDriverId()),
                         tripEvent.getRiderId() != null ? UUID.fromString(tripEvent.getRiderId()) : null,
                         tripEvent.getEventTime()
                 ).build()
         );
    }

    public TripEvent getActiveTripByDriverId(UUID driverId) {
        TripEventByDriverId tripEventByDriverId = tripEventByDriverIdRepository.findFirstByDriverIdOrderByEventId(driverId);
        if (tripEventByDriverId == null) {
            return null;
        }
        return new TripEvent(tripEventByDriverId);
    }

    public TripEvent getTripEvenByTripId(UUID tripId) {
        TripEventByTripId tripEventByTripId = tripEventByTripIdRepository.findFirstByTripId(tripId);
        if (tripEventByTripId == null) {
            return null;
        }
        return new TripEvent(tripEventByTripId);
    }
}

package com.pluralsight.springapp.service;

import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.exception.NotInOrderException;
import com.pluralsight.springapp.model.TripEventByDriverId;
import com.pluralsight.springapp.model.TripEventByTripId;
import com.pluralsight.springapp.repository.TripEventByDriverIdRepository;
import com.pluralsight.springapp.repository.TripEventByTripIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripEventService {

    private final TripEventByTripIdRepository tripEventByTripIdRepository;
    private final TripEventByDriverIdRepository tripEventByDriverIdRepository;

    //@Transactional ??
    public TripEvent createTripEvent(TripEvent tripEvent) {
        TripEventByTripId trip = tripEventByTripIdRepository.findByTripId(UUID.fromString(tripEvent.getTripId()));
        if (trip != null && !trip.isInOrder(tripEvent)) {
            throw new NotInOrderException("Trip event is not in order" +
                                          " for tripId: " + tripEvent.getTripId() +
                                          ", eventType: " + tripEvent.getEventType() +
                                          ", lastEventType: " + trip.getEventType());
        }
        tripEventByDriverIdRepository.save(new TripEventByDriverId(tripEvent));
        return new TripEvent(tripEventByTripIdRepository.save(new TripEventByTripId(tripEvent)));
    }

    public TripEvent getActiveTripByDriverId(UUID driverId) {
        TripEventByDriverId tripEventByDriverId = tripEventByDriverIdRepository.findByDriverId(driverId);
        if (tripEventByDriverId == null) {
            return null;
        }
        return new TripEvent(tripEventByDriverId);
    }

    public TripEvent getTripByTripId(UUID tripId) {
        TripEventByTripId tripEventByTripId = tripEventByTripIdRepository.findByTripId(tripId);
        if (tripEventByTripId == null) {
            return null;
        }
        return new TripEvent(tripEventByTripId);
    }
}

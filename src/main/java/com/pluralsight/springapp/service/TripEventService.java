package com.pluralsight.springapp.service;

import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.model.TripEventByTripId;
import com.pluralsight.springapp.repository.TripEventsByTripIdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripEventService {

    private final TripEventsByTripIdRepository tripEventsByTripIdRepository;

    public UUID createTripEvent(TripEvent tripEvent) {
        TripEventByTripId trip = tripEventsByTripIdRepository.findByTripId(UUID.fromString(tripEvent.getTripId()));
        if (trip != null && trip.isInOrder(tripEvent)) {
            throw new IllegalArgumentException("Trip event is not in order");
        }
        return tripEventsByTripIdRepository.save(new TripEventByTripId(tripEvent)).getEventId();
    }
}

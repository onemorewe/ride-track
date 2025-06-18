package com.pluralsight.springapp.controller;

import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.service.TripEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trip-events")
@RequiredArgsConstructor
public class TripEventController {

    private final TripEventService tripEventService;

    @PostMapping()
    public TripEvent createTrip(@RequestBody TripEvent tripEvent) {
        return tripEventService.createTripEvent(tripEvent);
    }

    @GetMapping("/drivers/{driverId}/active-trip")
    public TripEvent getActiveTripByDriverId(@PathVariable String driverId) {
        return tripEventService.getActiveTripByDriverId(UUID.fromString(driverId));
    }

    @GetMapping("trips/{tripId}/snapshot")
    public TripEvent getTripSnapshot(@PathVariable String tripId) {
        return tripEventService.getTripByTripId(UUID.fromString(tripId));
    }
}

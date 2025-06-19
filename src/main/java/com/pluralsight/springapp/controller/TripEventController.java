package com.pluralsight.springapp.controller;

import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.service.TripEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripEventController {

    private final TripEventService tripEventService;

    @PostMapping("{tripId}/events")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createTrip(@RequestBody TripEvent tripEvent) {
        tripEventService.createTripEvent(tripEvent);
    }

    @GetMapping("/drivers/{driverId}/active-trip")
    public TripEvent getActiveTripByDriverId(@PathVariable String driverId) {
        return tripEventService.getActiveTripByDriverId(UUID.fromString(driverId));
    }

    @GetMapping("/{tripId}/events")
    public TripEvent getTripSnapshot(@PathVariable String tripId) {
        return tripEventService.getTripEvenByTripId(UUID.fromString(tripId));
    }
}

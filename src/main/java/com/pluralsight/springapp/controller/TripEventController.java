package com.pluralsight.springapp.controller;

import com.pluralsight.springapp.controller.dto.TripEvent;
import com.pluralsight.springapp.service.TripEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/trip-events")
@RequiredArgsConstructor
public class TripEventController {

    private final TripEventService tripEventService;

    @PostMapping()
    public UUID createTrip(@RequestBody TripEvent tripEvent) {
        return tripEventService.createTripEvent(tripEvent);
    }
}

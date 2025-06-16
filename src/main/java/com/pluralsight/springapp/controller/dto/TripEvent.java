package com.pluralsight.springapp.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TripEvent {
    private String tripId;
    private String eventId;
    private String eventType;
    private String eventTime;
}

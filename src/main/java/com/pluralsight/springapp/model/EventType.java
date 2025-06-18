package com.pluralsight.springapp.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EventType {
    REQUESTED(0),
    DRIVER_ACCEPTED(1),
    DRIVER_ARRIVED(2),
    RIDE_STARTED(3),
    RIDE_ENDED(4),
    PAYMENT_CAPTURED(5);
    private final int order;
}
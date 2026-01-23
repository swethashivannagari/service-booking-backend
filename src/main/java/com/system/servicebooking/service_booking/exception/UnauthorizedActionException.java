package com.system.servicebooking.service_booking.exception;

public class UnauthorizedActionException  extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}


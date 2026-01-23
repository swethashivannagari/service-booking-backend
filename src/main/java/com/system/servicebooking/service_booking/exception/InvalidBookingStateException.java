package com.system.servicebooking.service_booking.exception;

public class InvalidBookingStateException extends RuntimeException{
    public InvalidBookingStateException(String message){
        super(message);
    }
}

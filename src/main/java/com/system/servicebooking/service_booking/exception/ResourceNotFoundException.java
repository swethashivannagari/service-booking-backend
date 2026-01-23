package com.system.servicebooking.service_booking.exception;

public class ResourceNotFoundException  extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}

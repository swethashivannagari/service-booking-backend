package com.system.servicebooking.service_booking.common;

import com.system.servicebooking.service_booking.enums.BookingStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class BookingStatusRules {
    private static final Map<BookingStatus, Set<BookingStatus>>ALLOWED = new EnumMap<>(BookingStatus.class);
    static {
        ALLOWED.put(BookingStatus.REQUESTED, EnumSet.of(BookingStatus.ACCEPTED,BookingStatus.CANCELLED));
        ALLOWED.put(BookingStatus.ACCEPTED,EnumSet.of(BookingStatus.COMPLETED,BookingStatus.CANCELLED));
        ALLOWED.put(BookingStatus.COMPLETED,EnumSet.noneOf(BookingStatus.class));
        ALLOWED.put(BookingStatus.CANCELLED,EnumSet.noneOf(BookingStatus.class));
    }

    private BookingStatusRules(){
    }

    public static boolean canTransition(BookingStatus from,BookingStatus to){
        return ALLOWED.getOrDefault(from,Set.of()).contains(to);
    }
}

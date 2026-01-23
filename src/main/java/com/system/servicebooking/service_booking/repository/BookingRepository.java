package com.system.servicebooking.service_booking.repository;

import com.system.servicebooking.service_booking.enums.BookingStatus;
import com.system.servicebooking.service_booking.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;

public interface BookingRepository extends MongoRepository<Booking,String> {
    public Page<Booking> findByUserId(String userId, Pageable pageable);
    public Page<Booking>findByProviderId(String providerId,Pageable pageable);
    public List<Booking>findByStatus(BookingStatus status);
}

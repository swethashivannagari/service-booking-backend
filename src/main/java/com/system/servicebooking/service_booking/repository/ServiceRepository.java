package com.system.servicebooking.service_booking.repository;

import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends MongoRepository<Service,String>
{
    public List<Service> findByProviderId(String providerId);
    public Page<Service>findAll(Pageable pageable);
    Optional<Service> findByIdAndIsDeletedFalse(String id);
}

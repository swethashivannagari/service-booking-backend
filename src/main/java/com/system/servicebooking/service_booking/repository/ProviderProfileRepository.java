package com.system.servicebooking.service_booking.repository;

import com.system.servicebooking.service_booking.model.ProviderProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProviderProfileRepository extends MongoRepository<ProviderProfile,String> {

    Optional<ProviderProfile> findByUserId(String userId);
    List<ProviderProfile>findByServicesOfferedContaining(String serviceId);
//    List<ProviderProfile>findByService(String service);
}

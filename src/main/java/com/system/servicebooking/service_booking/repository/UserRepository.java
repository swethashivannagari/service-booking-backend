package com.system.servicebooking.service_booking.repository;


import com.system.servicebooking.service_booking.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmailAndDeletedFalse(String email);
}

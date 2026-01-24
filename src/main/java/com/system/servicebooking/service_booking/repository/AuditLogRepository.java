package com.system.servicebooking.service_booking.repository;

import com.system.servicebooking.service_booking.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog,String> {
}

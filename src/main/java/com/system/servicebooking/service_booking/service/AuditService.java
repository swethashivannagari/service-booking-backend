package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.model.AuditLog;
import com.system.servicebooking.service_booking.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository){
        this.auditLogRepository=auditLogRepository;
    }

    public void log(String entityType,String entityId,String action,String performedBy, String oldValue,String newValue){
        AuditLog log =new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setPerformedBy(performedBy);
        log.setNewValue(newValue);
        log.setOldValue(oldValue);
        auditLogRepository.save(log);


    }
}

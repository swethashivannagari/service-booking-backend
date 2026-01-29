package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.common.Helper;
import com.system.servicebooking.service_booking.dto.ServiceRequestDTO;
import com.system.servicebooking.service_booking.dto.ServiceResponseDTO;
import com.system.servicebooking.service_booking.enums.BookingStatus;
import com.system.servicebooking.service_booking.exception.ResourceNotFoundException;
import com.system.servicebooking.service_booking.exception.UnauthorizedActionException;
import com.system.servicebooking.service_booking.mapper.ServiceMapper;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.Service;
import com.system.servicebooking.service_booking.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.system.servicebooking.service_booking.enums.BookingStatus.CANCELLED;
import static com.system.servicebooking.service_booking.enums.BookingStatus.REQUESTED;

@org.springframework.stereotype.Service
public class ServicesService {
    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    AuditService auditService;

    @Autowired
    Helper helper;

    @Autowired
    ServiceMapper serviceMapper;

    public ServiceResponseDTO createService(ServiceRequestDTO service) {
        Service serviceResponse = serviceRepository.save(serviceMapper.toEntity(service));
        return serviceMapper.toResponseDTO(serviceResponse);
    }

    public ServiceResponseDTO getServiceById(String serviceId){
        Service service=serviceRepository.findById(serviceId).orElseThrow(()-> new ResourceNotFoundException("Service not found"));
        return serviceMapper.toResponseDTO(service);
    }

//    public List<ServiceResponseDTO> getServiceByProviderId(String providerId){
//        List<Service> services= serviceRepository.findByProviderId(providerId);
//        return services.stream().map(serviceMapper::toResponseDTO).collect(Collectors.toList());
//    }

    public ServiceResponseDTO updateService(String serviceId, ServiceRequestDTO newService){
        Service oldService=serviceRepository.findById(serviceId).orElseThrow(()->new ResourceNotFoundException("service not found"));

        oldService.setName(newService.getName());
        oldService.setBasePrice(newService.getBasePrice());

        oldService.setCategory(newService.getCategory());
        oldService.setDescription(newService.getDescription());

        serviceRepository.save(oldService);
        return serviceMapper.toResponseDTO(oldService);

    }

    public void deleteService(String serviceId){

       Service actualService=serviceRepository.findByIdAndIsDeletedFalse(serviceId)
               .orElseThrow(()->new ResourceNotFoundException("Service not found"));
        actualService.setDeleted(true);
        actualService.setDeleteAt(LocalDateTime.now());
        serviceRepository.save(actualService);
        auditService.log("SERVICE",serviceId,"SOFT_DELETE", helper.getCurrentUserId(), "ACTIVE","DELETED");


    }
    
    public Page<ServiceResponseDTO> getAllServices(int page,int size){
        PageRequest pagable=PageRequest.of(page,size);
        Page<Service>response=serviceRepository.findAll(pagable);
        return response.map(serviceMapper::toResponseDTO);
        
    }
}

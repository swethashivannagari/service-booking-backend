package com.system.servicebooking.service_booking.controller;

import com.system.servicebooking.service_booking.common.ApiResponse;
import com.system.servicebooking.service_booking.dto.ServiceRequestDTO;
import com.system.servicebooking.service_booking.dto.ServiceResponseDTO;
import com.system.servicebooking.service_booking.model.Service;

import com.system.servicebooking.service_booking.service.ServicesService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/services")
public class ServiceController {

    @Autowired
    private ServicesService servicesService;

    @PreAuthorize("hasRole('PROVIDER','ADMIN)")
    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> createService(@Valid @RequestBody ServiceRequestDTO service) {
        ServiceResponseDTO createdService = servicesService.createService(service);
        ApiResponse<ServiceResponseDTO> response=new ApiResponse<>(true,"service created successfully",createdService);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/{serviceId}")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> getServiceById(@PathVariable String serviceId) {
        ServiceResponseDTO service = servicesService.getServiceById(serviceId);
        ApiResponse<ServiceResponseDTO> response= new ApiResponse<>(true,"fetched service successfully",service);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ServiceResponseDTO>>> getAllService(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10")int size){

        Page<ServiceResponseDTO> services = servicesService.getAllServices(page,size);
        ApiResponse<Page<ServiceResponseDTO>> response= new ApiResponse<>(true,"fetched service successfully",services);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 3️⃣ Get services by providerId
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ApiResponse<List<ServiceResponseDTO>>> getServiceByProviderId(@PathVariable String providerId) {
        List<ServiceResponseDTO> service = servicesService.getServiceByProviderId(providerId);
        ApiResponse<List<ServiceResponseDTO>> response= new ApiResponse<>(true,"fetched services successfully",service);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 4️⃣ Update service
    @PutMapping("/{serviceId}")
    @PreAuthorize("hasRole('PROVIDER','ADMIN)")
    public ResponseEntity<ApiResponse<ServiceResponseDTO>> updateService(
            @PathVariable String serviceId,
            @Valid @RequestBody ServiceRequestDTO service) {

        ServiceResponseDTO updatedService = servicesService.updateService(serviceId, service);
        ApiResponse<ServiceResponseDTO> response= new ApiResponse<>(true,"service updated successfully",updatedService);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PROVIDER','ADMIN)")
    @DeleteMapping("/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable String serviceId) {
        servicesService.deleteService(serviceId);
        return new ResponseEntity<>("Service deleted successfully", HttpStatus.OK);
    }
}

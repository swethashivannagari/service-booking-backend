package com.system.servicebooking.service_booking.controller;

import com.system.servicebooking.service_booking.common.ApiResponse;
import com.system.servicebooking.service_booking.dto.ProviderProfileRequestDTO;
import com.system.servicebooking.service_booking.dto.ProviderProfileResponseDTO;
import com.system.servicebooking.service_booking.dto.ServiceRequestDTO;
import com.system.servicebooking.service_booking.dto.ServiceResponseDTO;
import com.system.servicebooking.service_booking.mapper.ProviderProfileMapper;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import com.system.servicebooking.service_booking.repository.ProviderProfileRepository;
import com.system.servicebooking.service_booking.service.ProviderProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/providers")
public class ProviderProfileController {

    @Autowired
    ProviderProfileService providerProfileService;


    @PreAuthorize("hasRole('PROVIDER')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProviderProfileResponseDTO>> createProfile(@Valid @RequestBody ProviderProfileRequestDTO profile){
       ProviderProfileResponseDTO profileResponse= providerProfileService.createProfile(profile);
       ApiResponse<ProviderProfileResponseDTO> response=new ApiResponse<>(true,"Profile created successfully",profileResponse);
       return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderProfileResponseDTO>> getProviderProfileById(@PathVariable String id){
        ProviderProfileResponseDTO profile= providerProfileService.getProviderById(id);
        ApiResponse<ProviderProfileResponseDTO> response= new ApiResponse<>(true,"fetched profile successfully",profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<ProviderProfileResponseDTO>> getProviderProfileByUserId(@PathVariable String userId){
        ProviderProfileResponseDTO profile= providerProfileService.getProfileByUserId(userId);
        ApiResponse<ProviderProfileResponseDTO> response= new ApiResponse<>(true,"fetched profile successfully",profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('PROVIDER')")
    @PutMapping("/{providerId}")
    public ResponseEntity<ApiResponse<ProviderProfileResponseDTO>> updateProfile(
            @PathVariable String providerId,
            @Valid @RequestBody ProviderProfileRequestDTO profile) {

       ProviderProfileResponseDTO profileResponse=providerProfileService.updateProfile(providerId,profile);
        ApiResponse<ProviderProfileResponseDTO> response= new ApiResponse<>(true,"service updated successfully",profileResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

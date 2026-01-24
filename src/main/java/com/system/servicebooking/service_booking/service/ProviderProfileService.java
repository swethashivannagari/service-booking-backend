package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.common.Helper;
import com.system.servicebooking.service_booking.dto.ProviderProfileRequestDTO;
import com.system.servicebooking.service_booking.dto.ProviderProfileResponseDTO;
import com.system.servicebooking.service_booking.dto.ServiceRequestDTO;
import com.system.servicebooking.service_booking.dto.ServiceResponseDTO;
import com.system.servicebooking.service_booking.exception.ResourceNotFoundException;
import com.system.servicebooking.service_booking.exception.UnauthorizedActionException;
import com.system.servicebooking.service_booking.mapper.ProviderProfileMapper;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import com.system.servicebooking.service_booking.model.User;
import com.system.servicebooking.service_booking.repository.ProviderProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderProfileService {

    @Autowired
    ProviderProfileRepository providerProfileRepository;
    @Autowired
    ProviderProfileMapper providerProfileMapper;
    @Autowired
    Helper helper;

    public List<ProviderProfile> getAllProviders(){
        return providerProfileRepository.findAll();
    }
    public ProviderProfileResponseDTO getProviderById(String id){


        ProviderProfile providerProfile= providerProfileRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("profile id not found"));
        return providerProfileMapper.toResponseDTO(providerProfile);
    }
    public ProviderProfileResponseDTO createProfile(ProviderProfileRequestDTO profile){
        String userId= helper.getCurrentUserId();

        ProviderProfile providerProfile=providerProfileMapper.toEntity(profile);
        providerProfile.setUserId(userId);
        return providerProfileMapper.toResponseDTO(providerProfileRepository.save(providerProfile));

    }

    public ProviderProfileResponseDTO getProfileByUserId(String userId){
        String tokenUserId=helper.getCurrentUserId();
        if(!helper.isAdmin() && !userId.equals(tokenUserId))
            throw new UnauthorizedActionException("You can only modify your profile");

        ProviderProfile providerProfile=  providerProfileRepository.findByUserId(userId).orElseThrow(()->new ResourceNotFoundException("user id not found"));
        return providerProfileMapper.toResponseDTO(providerProfile);
    }

    public ProviderProfileResponseDTO updateProfile(String profileId, ProviderProfileRequestDTO profileRequest){
//        String tokenUserId=helper.getCurrentUserId();
//        if(!helper.isAdmin() && !userId.equals(tokenUserId))
//            throw new UnauthorizedActionException("You can only modify your profile");
        ProviderProfile profile=providerProfileRepository.findById(profileId).orElseThrow(()->new ResourceNotFoundException("Profile not found"));
        profile.setAvailability(profileRequest.getAvailability());
        profile.setExperience(profileRequest.getExperience());
        profile.setLocation(profileRequest.getLocation());
        profile.setRating(profileRequest.getRating());
        profile.setServicesOffered(profileRequest.getServicesOffered());
        providerProfileRepository.save(profile);
        return providerProfileMapper.toResponseDTO(profile);

        }





//    public List<ProviderProfile> getProvidersByService(String service){
//        return ProviderProfileRepository.findByService(service);
//    }
}

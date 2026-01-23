package com.system.servicebooking.service_booking.mapper;

import com.system.servicebooking.service_booking.dto.ProviderProfileRequestDTO;
import com.system.servicebooking.service_booking.dto.ProviderProfileResponseDTO;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface ProviderProfileMapper {
    @Mapping(target = "availability",constant = "Active")
    ProviderProfile toEntity(ProviderProfileRequestDTO dto);
    ProviderProfileResponseDTO toResponseDTO(ProviderProfile providerProfile);

}

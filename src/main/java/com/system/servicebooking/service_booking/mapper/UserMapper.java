package com.system.servicebooking.service_booking.mapper;

import com.system.servicebooking.service_booking.dto.*;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public abstract User toEntity(RegisterRequest dto);
    public abstract UserResponseDTO toResponseDTO(User user);

}
package com.system.servicebooking.service_booking.mapper;

import com.system.servicebooking.service_booking.dto.BookingRequestDTO;
import com.system.servicebooking.service_booking.dto.BookingResponseDTO;
import com.system.servicebooking.service_booking.dto.ServiceRequestDTO;
import com.system.servicebooking.service_booking.dto.ServiceResponseDTO;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    Service toEntity(ServiceRequestDTO dto);
    ServiceResponseDTO toResponseDTO(Service service);

}
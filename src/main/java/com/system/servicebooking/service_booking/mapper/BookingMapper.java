package com.system.servicebooking.service_booking.mapper;

import com.system.servicebooking.service_booking.dto.BookingRequestDTO;
import com.system.servicebooking.service_booking.dto.BookingResponseDTO;
import com.system.servicebooking.service_booking.model.Booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import static com.system.servicebooking.service_booking.enums.BookingStatus.REQUESTED;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "status",constant = "REQUESTED")
    public abstract Booking toEntity(BookingRequestDTO dto);
    public abstract BookingResponseDTO toResponseDTO(Booking booking);

}

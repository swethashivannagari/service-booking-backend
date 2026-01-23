package com.system.servicebooking.service_booking.dto;

import com.system.servicebooking.service_booking.enums.UsersRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    String id;
    public String name;
    String email;
    String phone;
    UsersRole role;
}

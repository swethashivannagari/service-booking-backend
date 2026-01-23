package com.system.servicebooking.service_booking.dto;

import com.system.servicebooking.service_booking.enums.UsersRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class RegisterRequest {
    @NotBlank
    String name;
    @NotBlank @Size(min = 8)
    String password;
    @NotBlank @Email
    String email;
    @Size (min = 10)
    String phone;
    @NotNull
    UsersRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UsersRole getRole() {
        return role;
    }

    public void setRole(UsersRole role) {
        this.role = role;
    }



}

package com.system.servicebooking.service_booking.controller;

import com.system.servicebooking.service_booking.common.ApiResponse;
import com.system.servicebooking.service_booking.dto.*;
import com.system.servicebooking.service_booking.model.User;
import com.system.servicebooking.service_booking.service.AuthService;
import com.system.servicebooking.service_booking.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login (@Valid  @RequestBody LoginRequestDTO loginRequest){
        LoginResponseDTO response= authService.login(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(true,"Login successful",response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> register(@Valid @RequestBody RegisterRequest request){
        UserResponseDTO saved=authService.register(request);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"User created successfully", saved)
        );
    }


}

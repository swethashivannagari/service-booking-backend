package com.system.servicebooking.service_booking.controller;

import com.system.servicebooking.service_booking.common.ApiResponse;
import com.system.servicebooking.service_booking.dto.RegisterRequest;
import com.system.servicebooking.service_booking.dto.UserRequestDTO;
import com.system.servicebooking.service_booking.dto.UserResponseDTO;

import com.system.servicebooking.service_booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Users fetched successfully", userService.getAllUsers())
        );
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(
                new ApiResponse<>(true,"Users fetched successfully", userService.getUserById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(
            @Valid @RequestBody RegisterRequest userRequestDTO) {

        UserResponseDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.ok(
                new ApiResponse<>(true,"User created successfully", createdUser)
        );
    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable String id,
            @RequestBody UserRequestDTO userRequestDTO) {

        UserResponseDTO user= userService.
                updateUser(id, userRequestDTO);
        ApiResponse<UserResponseDTO> response=new ApiResponse<>(true,"User updated successfully", user);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {

        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(true,"User deleted successfully", null));
        }
        return ResponseEntity.notFound().build();
    }
}

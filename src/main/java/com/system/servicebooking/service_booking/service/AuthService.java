package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.config.PasswordConfig;
import com.system.servicebooking.service_booking.dto.LoginRequestDTO;
import com.system.servicebooking.service_booking.dto.LoginResponseDTO;
import com.system.servicebooking.service_booking.dto.RegisterRequest;
import com.system.servicebooking.service_booking.dto.UserResponseDTO;
import com.system.servicebooking.service_booking.exception.InvalidBookingStateException;
import com.system.servicebooking.service_booking.exception.UserAlreadyExistsException;
import com.system.servicebooking.service_booking.mapper.BookingMapper;
import com.system.servicebooking.service_booking.mapper.UserMapper;
import com.system.servicebooking.service_booking.model.User;
import com.system.servicebooking.service_booking.repository.UserRepository;
import com.system.servicebooking.service_booking.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserMapper mapper;

    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtUtil){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    public UserResponseDTO register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User already present!!");
        }

        User user=mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User userResponse= userRepository.save(user);
        return mapper.toResponseDTO(userResponse);
    }

    public LoginResponseDTO login(LoginRequestDTO request){
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(()->new InvalidBookingStateException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new InvalidBookingStateException("Invalid password");
        }

        String token=jwtUtil.generateToken(user.getId(),user.getRole());
        LoginResponseDTO responseDTO=new LoginResponseDTO();
        responseDTO.setToken(token);
        responseDTO.setRole(user.getRole().name());
        return responseDTO;
    }
}

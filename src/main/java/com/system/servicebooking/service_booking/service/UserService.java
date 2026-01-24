package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.common.Helper;
import com.system.servicebooking.service_booking.dto.RegisterRequest;
import com.system.servicebooking.service_booking.dto.UserRequestDTO;
import com.system.servicebooking.service_booking.dto.UserResponseDTO;
import com.system.servicebooking.service_booking.exception.ResourceNotFoundException;
import com.system.servicebooking.service_booking.exception.UnauthorizedActionException;
import com.system.servicebooking.service_booking.mapper.UserMapper;
import com.system.servicebooking.service_booking.model.User;
import com.system.servicebooking.service_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuditService auditService;

    @Autowired
    Helper helper;

    private UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponseDTO).collect(Collectors.toList());

    }
    public UserResponseDTO getUserById(String id){
        String tokenUserId=helper.getCurrentUserId();
        if(!helper.isAdmin() && !id.equals(tokenUserId))
            throw new UnauthorizedActionException("You can only modify your profile");
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        return userMapper.toResponseDTO(user);
    }
    public UserResponseDTO createUser(RegisterRequest user){

       User userResponse= userRepository.save(userMapper.toEntity(user));
        return userMapper.toResponseDTO(userResponse);
    }
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        String tokenUserId=helper.getCurrentUserId();
        if(!helper.isAdmin() && !id.equals(tokenUserId))
            throw new UnauthorizedActionException("You can only modify your profile");
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));

            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            user.setPhone(userRequestDTO.getPhone());

            User updatedUser = userRepository.save(user);
            return userMapper.toResponseDTO(updatedUser);

    }


    public boolean deleteUser(String id) {
        String tokenUserId=helper.getCurrentUserId();
        if(!helper.isAdmin() && !id.equals(tokenUserId))
            throw new UnauthorizedActionException("Only admin can delete users");
        User user=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        user.setDeleted(true);
        user.setDeleteAt(LocalDateTime.now());
        userRepository.save(user);
        auditService.log("USER",user.getId(),"SOFT_DELETE", helper.getCurrentUserId(), "ACTIVE","DELETED");
        return true;

//        if (userRepository.existsById(id)) {
//            userRepository.deleteById(id);
//            return true;
//        }
//        return false;
    }
}

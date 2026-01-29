package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.common.BookingStatusRules;
import com.system.servicebooking.service_booking.common.Helper;
import com.system.servicebooking.service_booking.dto.BookingRequestDTO;
import com.system.servicebooking.service_booking.dto.BookingResponseDTO;
import com.system.servicebooking.service_booking.enums.BookingStatus;
import com.system.servicebooking.service_booking.exception.InvalidBookingStateException;
import com.system.servicebooking.service_booking.exception.ResourceNotFoundException;
import com.system.servicebooking.service_booking.exception.UnauthorizedActionException;
import com.system.servicebooking.service_booking.mapper.BookingMapper;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import com.system.servicebooking.service_booking.model.User;
import com.system.servicebooking.service_booking.repository.BookingRepository;
import com.system.servicebooking.service_booking.repository.ProviderProfileRepository;
import com.system.servicebooking.service_booking.repository.ServiceRepository;
import com.system.servicebooking.service_booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.system.servicebooking.service_booking.model.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.system.servicebooking.service_booking.enums.BookingStatus.*;

@org.springframework.stereotype.Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProviderProfileRepository providerProfileRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    BookingMapper mapper;

    @Autowired
    Helper helper;



    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO,String idempotencyKey){
        if (idempotencyKey.isBlank() || idempotencyKey==null){
            throw new UnauthorizedActionException("Idempotency key header is required");
        }
        validateSlot(bookingDTO.getScheduledTime());
        Optional<Booking> existing=bookingRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()){
            return mapper.toResponseDTO(existing.get());
        }
        String userId=helper.getCurrentUserId();
        List<ProviderProfile> providers= providerProfileRepository.findByServicesOfferedContaining(bookingDTO.getServiceId());
        ProviderProfile assignedProvider=null;
        for (ProviderProfile profile:providers){
            User providerUser=userRepository.findById(profile.getUserId()).orElseThrow(()->new ResourceNotFoundException("Provider user not found"));
            if(providerUser.isDeleted())
                continue;
            boolean booked=bookingRepository.existsByProviderIdAndScheduledTimeAndStatusIn(profile.getUserId(),bookingDTO.getScheduledTime(),List.of(REQUESTED,ACCEPTED));
            if (!booked){
                assignedProvider=profile;
                break;
            }

        }
        if (assignedProvider==null){
            throw new ResourceNotFoundException("No provider available at selected slot");
        }
        Booking booking=mapper.toEntity(bookingDTO);

        booking.setProviderId(assignedProvider.getUserId());
        booking.setStatus(REQUESTED);
        booking.setUserId(userId);
        booking.setIdempotencyKey(idempotencyKey);
        bookingRepository.save(booking);
        return mapper.toResponseDTO(booking);
    }

    public Booking getBookingById(String bookingId){
        return bookingRepository.findById(bookingId).get();
    }

    public List<BookingResponseDTO> getALlBookings(){
        List<Booking> bookings= bookingRepository.findAll();
        return bookings.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    public BookingResponseDTO acceptBooking(String bookingId){
      Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
      String loggedProviderToken=helper.getCurrentUserId();
      if(!booking.getProviderId().equals(loggedProviderToken)){
          throw new UnauthorizedActionException("not your booking!");
      }
        if (!BookingStatusRules.canTransition(booking.getStatus(),ACCEPTED)){
          throw new InvalidBookingStateException("Only Requested bookings can be accepted");
      }
      booking.setStatus(ACCEPTED);
       bookingRepository.save(booking);
       return mapper.toResponseDTO(booking);
    }

    public BookingResponseDTO completeBooking(String bookingId){
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        String loggedProviderToken=helper.getCurrentUserId();
        if(!booking.getProviderId().equals(loggedProviderToken)){
            throw new UnauthorizedActionException("not your booking!");
        }
        if (!BookingStatusRules.canTransition(booking.getStatus(),COMPLETED)){
            throw new InvalidBookingStateException("Only Accepted bookings can be completed");
        }
        booking.setStatus(COMPLETED);
         bookingRepository.save(booking);
         return mapper.toResponseDTO(booking);

    }

   public Page<BookingResponseDTO> getBookingsByUserId(int page, int size){
        String userId=helper.getCurrentUserId();
       PageRequest pageable=PageRequest.of(page,size);
        Page<Booking> bookings= bookingRepository.findByUserId(userId,pageable);

       Page<BookingResponseDTO> responseBookings= bookings.map(mapper::toResponseDTO);
       responseBookings.getContent().forEach(booking ->{
           Service service= serviceRepository.findById(booking.getServiceId())
                   .orElseThrow(()->new ResourceNotFoundException("Provider not found"));
           User provider = userRepository.findById(booking.getProviderId())
                   .orElseThrow(()->new ResourceNotFoundException("provider not found"));
           booking.setServiceName(service.getName());
           booking.setProviderName(provider.getName());
       });
       return responseBookings;
    }

    public Page<BookingResponseDTO> getBookingsByProviderId(int page, int size){
        String providerId=helper.getCurrentUserId();
        PageRequest pageable=PageRequest.of(page,size);
        Page<Booking> bookings= bookingRepository.findByProviderId(providerId,pageable);
        Page<BookingResponseDTO> responseBookings= bookings.map(mapper::toResponseDTO);
        responseBookings.getContent().forEach(booking ->{
            Service service= serviceRepository.findById(booking.getServiceId())
                    .orElseThrow(()->new ResourceNotFoundException("Provider not found"));
            User provider = userRepository.findById(booking.getProviderId())
                    .orElseThrow(()->new ResourceNotFoundException("provider not found"));
            booking.setServiceName(service.getName());
            booking.setProviderName(provider.getName());
        });
        return responseBookings;
    }

    public List<BookingResponseDTO> getBookingsByStatus(BookingStatus status){
        List<Booking>bookings= bookingRepository.findByStatus(status);
        return bookings.stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }
    
    public Booking updateStatus(String bookingId, BookingStatus status){
        Booking booking=bookingRepository.findById(bookingId).get();
        booking.setStatus(status);
        bookingRepository.save(booking);
        return booking;

    }

    public BookingResponseDTO cancelBooking(String bookingId){
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Booking not found"));
        String loggedProviderToken=helper.getCurrentUserId();
        if(!booking.getUserId().equals(loggedProviderToken)){
            throw new UnauthorizedActionException("not your booking!");
        }

        if (!BookingStatusRules.canTransition(booking.getStatus(),CANCELLED)){
            throw new InvalidBookingStateException("Completed Bookings cannot be cancelled");
        }
        booking.setStatus(CANCELLED);
        bookingRepository.save(booking);
        return mapper.toResponseDTO(booking);

    }

    private void validateSlot(LocalDateTime time){
        if(time.getMinute()!=0 || time.getSecond()!=0){
            throw new UnauthorizedActionException("Bookings must start at the beginning of the hor(e.g:10.00,11:00)");
        }
        if (time.isBefore(LocalDateTime.now())){
            throw new UnauthorizedActionException("Scheduled time must be in future");
        }
    }



}

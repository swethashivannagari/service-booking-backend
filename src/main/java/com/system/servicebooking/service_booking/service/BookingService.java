package com.system.servicebooking.service_booking.service;

import com.system.servicebooking.service_booking.common.Helper;
import com.system.servicebooking.service_booking.dto.BookingRequestDTO;
import com.system.servicebooking.service_booking.dto.BookingResponseDTO;
import com.system.servicebooking.service_booking.enums.BookingStatus;
import com.system.servicebooking.service_booking.exception.InvalidBookingStateException;
import com.system.servicebooking.service_booking.exception.ResourceNotFoundException;
import com.system.servicebooking.service_booking.exception.UnauthorizedActionException;
import com.system.servicebooking.service_booking.mapper.BookingMapper;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.system.servicebooking.service_booking.enums.BookingStatus.*;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingMapper mapper;

    @Autowired
    Helper helper;

    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO){
        Booking booking=mapper.toEntity(bookingDTO);
        booking.setStatus(REQUESTED);
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
      if (booking.getStatus()!=REQUESTED){
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
        if (booking.getStatus()!=ACCEPTED){
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
        return bookings.map(mapper::toResponseDTO);
    }

    public Page<BookingResponseDTO> getBookingsByProviderId(int page, int size){
        String providerId=helper.getCurrentUserId();
        PageRequest pageable=PageRequest.of(page,size);
        Page<Booking> bookings= bookingRepository.findByProviderId(providerId,pageable);
        return bookings.map(mapper::toResponseDTO);
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
        if (booking.getStatus()==COMPLETED){
            throw new InvalidBookingStateException("Completed Bookings cannot be cancelled");
        }
        booking.setStatus(CANCELLED);
        bookingRepository.save(booking);
        return mapper.toResponseDTO(booking);

    }



}

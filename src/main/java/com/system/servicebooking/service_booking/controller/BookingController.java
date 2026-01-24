package com.system.servicebooking.service_booking.controller;


import com.system.servicebooking.service_booking.common.ApiResponse;
import com.system.servicebooking.service_booking.dto.BookingRequestDTO;
import com.system.servicebooking.service_booking.dto.BookingResponseDTO;
import com.system.servicebooking.service_booking.enums.BookingStatus;
import com.system.servicebooking.service_booking.model.Booking;
import com.system.servicebooking.service_booking.model.ProviderProfile;
import com.system.servicebooking.service_booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Service/bookings")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings(){
        List<BookingResponseDTO> bookings=bookingService.getALlBookings();
        ApiResponse<List<BookingResponseDTO>> response = new ApiResponse<>(true,"Bookings fetched successfully",bookings);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Page<BookingResponseDTO>>> getBookingsByUserId(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10")int size){
        Page<BookingResponseDTO> bookings=bookingService.getBookingsByUserId(page, size);
        ApiResponse<Page<BookingResponseDTO>> response = new ApiResponse<>(true,"Bookings fetched successfully",bookings);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasAnyRole('PROVIDER','ADMIN')")
    @GetMapping("/provider")
    public ResponseEntity<ApiResponse<Page<BookingResponseDTO>>> getBookingsByProviderId(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10")int size){
        Page<BookingResponseDTO> bookings=bookingService.getBookingsByProviderId(page,size);
        ApiResponse<Page<BookingResponseDTO>> response = new ApiResponse<>(true,"Bookings fetched successfully",bookings);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getBookingsByStatus(@PathVariable BookingStatus status){
        List<BookingResponseDTO> bookings=bookingService.getBookingsByStatus(status);
        ApiResponse<List<BookingResponseDTO>> response = new ApiResponse<>(true,"Bookings fetched successfully",bookings);
        return ResponseEntity.ok(response);

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponseDTO>> createBooking(@RequestHeader String idempotencyKey,@Valid  @RequestBody BookingRequestDTO booking){
        BookingResponseDTO savedBooking = bookingService.createBooking(booking,idempotencyKey);
        ApiResponse<BookingResponseDTO> response = new ApiResponse<>(true,"Booking created successfully",savedBooking);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<String>updateStatus(@PathVariable String bookingId, @RequestParam BookingStatus status){
        bookingService.updateStatus(bookingId,status);
        return ResponseEntity.ok("Booking status updated");
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("{bookingId}/cancel")
    public ResponseEntity<ApiResponse<BookingResponseDTO>>cancelBooking(@PathVariable String bookingId){
        BookingResponseDTO booking = bookingService.cancelBooking(bookingId);
        ApiResponse<BookingResponseDTO> response = new ApiResponse<>(true,"Booking cancelled successfully",booking);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("{bookingId}/accept")
    public ResponseEntity<ApiResponse<BookingResponseDTO>>acceptBooking(@PathVariable String bookingId){
        BookingResponseDTO booking = bookingService.acceptBooking(bookingId);
        ApiResponse<BookingResponseDTO> response = new ApiResponse<>(true,"Booking accepted successfully",booking);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("{bookingId}/complete")
    public ResponseEntity<ApiResponse<BookingResponseDTO>>completeBooking(@PathVariable String bookingId){
        BookingResponseDTO booking = bookingService.completeBooking(bookingId);
        ApiResponse<BookingResponseDTO> response = new ApiResponse<>(true,"Booking completed successfully",booking);
        return ResponseEntity.ok(response);
    }

}

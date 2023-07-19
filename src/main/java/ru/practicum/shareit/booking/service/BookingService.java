package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto postBooking(Long userId, BookingDto bookingDto);

    BookingDto patchBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookings(Long userId, String state);

    List<BookingDto> getBookingsByOwner(Long userId, String state);
}
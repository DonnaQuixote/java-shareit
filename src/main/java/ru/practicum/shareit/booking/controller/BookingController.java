package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDto postBooking(@RequestBody @Valid BookingDto bookingDto,
                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return service.postBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDto patchBooking(@PathVariable Long id, @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                   @RequestParam Boolean approved) {
        return service.patchBooking(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id, @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return service.getBooking(userId, id);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                        @RequestParam(required = false, defaultValue = "ALL") String state) {
        return service.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return service.getBookingsByOwner(userId, state);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleBadRequest(final ValidationException e) {
        return Map.of("ОШИБКА", e.getMessage());
    }
}
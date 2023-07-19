package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingService service;

    @PostMapping
    public BookingDto postBooking(@RequestBody @Valid BookingDto bookingDto,
                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос POST /bookings от пользователя %d", userId));
        return service.postBooking(userId, bookingDto);
    }

    @PatchMapping("/{id}")
    public BookingDto patchBooking(@PathVariable Long id, @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                   @RequestParam Boolean approved) {
        log.debug(String.format("Получен запрос PATCH /bookings/%d от пользователя %d", id, userId));
        return service.patchBooking(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getBooking(@PathVariable Long id, @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET /bookings/%d от пользователя %d", id, userId));
        return service.getBooking(userId, id);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                        @RequestParam(required = false, defaultValue = "ALL") String state,
                                        @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug(String.format("Получен запрос GET /bookings от пользователя %d", userId));
        return service.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug(String.format("Получен запрос GET /bookings/owner от пользователя %d", userId));
        return service.getBookingsByOwner(userId, state, from, size);
    }
}
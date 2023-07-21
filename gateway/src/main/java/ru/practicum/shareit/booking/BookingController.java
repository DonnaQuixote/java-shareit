package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(defaultValue = "ALL") String state,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState stateConverted = BookingState.from(state).orElseThrow(IllegalArgumentException::new);
		log.debug(String.format("Получен запрос GET /bookings от пользователя %d", userId));
		return bookingClient.getBookings(userId, stateConverted, from, size);
	}

	@PostMapping
	public ResponseEntity<Object> postBooking(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestBody @Valid BookingDto requestDto) {
		log.debug(String.format("Получен запрос POST /bookings от пользователя %d", userId));
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable Long bookingId) {
		log.debug(String.format("Получен запрос GET /bookings/%d от пользователя %d", userId, userId));
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> patchBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
											   @PathVariable Long bookingId,
											   @RequestParam Boolean approved) {
		log.debug(String.format("Получен запрос PATCH /bookings/%d от пользователя %d", userId, userId));
		return bookingClient.patchBooking(userId, bookingId, approved);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
													 @RequestHeader(name = "X-Sharer-User-Id") Long userId,
													 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
													 @RequestParam(defaultValue = "10") @Positive Integer size) {
		log.debug(String.format("Получен запрос GET /bookings/owner от пользователя %d", userId));
		BookingState stateConverted = BookingState.from(state).orElseThrow(IllegalArgumentException::new);
		return bookingClient.getBookingsByOwner(userId, stateConverted, from, size);
	}
}
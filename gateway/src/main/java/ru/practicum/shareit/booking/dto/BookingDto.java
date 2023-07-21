package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
	private long itemId;
	@FutureOrPresent(message = "Время начала аренды не может быть в прошлом")
	@NotNull(message = "Необходимо время начала аренды")
	private LocalDateTime start;
	@Future(message = "Время конца аренды не может быть в прошлом")
	@NotNull(message = "Необходимо время конца аренды")
	private LocalDateTime end;
}
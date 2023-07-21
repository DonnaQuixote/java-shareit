package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    @Future(message = "Время начала аренды не может быть в прошлом")
    @NotNull(message = "Необходимо время начала аренды")
    private LocalDateTime start;
    @Future(message = "Время конца аренды не может быть в прошлом")
    @NotNull(message = "Необходимо время конца аренды")
    private LocalDateTime end;
    private Long itemId;
    private ItemDto item;
    private Long bookerId;
    private User booker;
    private BookingStatus status;
}
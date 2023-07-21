package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(message = "требуется поле name")
    @NotBlank(message = "поле name не должно быть пустым")
    private String name;
    @NotNull(message = "требуется поле description")
    @NotBlank(message = "поле description не должно быть пустым")
    private String description;
    private Long owner;
    @NotNull(message = "требуется поле available")
    private Boolean available;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
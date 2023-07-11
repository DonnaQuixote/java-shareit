package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(message = "требуется поле name")
    @NotBlank(message = "поле name не должно быть пустым")
    private String name;
    @NotNull(message = "требуется поле description")
    @NotBlank(message = "поле description не должно быть пустым")
    private String description;
    @NotNull(message = "требуется поле available")
    private Boolean available;
    private Long request;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
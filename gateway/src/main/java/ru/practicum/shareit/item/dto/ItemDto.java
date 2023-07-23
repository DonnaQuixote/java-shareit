package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public final class ItemDto {
    @NotNull(message = "требуется поле name")
    @NotBlank(message = "поле name не должно быть пустым")
    private final String name;
    @NotNull(message = "требуется поле description")
    @NotBlank(message = "поле description не должно быть пустым")
    private final String description;
    @NotNull(message = "требуется поле available")
    private final Boolean available;
    private final Long requestId;
}
package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    @NotNull(message = "Необходимо описание")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    private LocalDateTime created;
    private UserDto requester;
    private List<ItemDto> items;
}

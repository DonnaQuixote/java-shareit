package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
public final class RequestDto {
    @NotNull(message = "Необходимо описание")
    @NotBlank(message = "Описание не может быть пустым")
    private final String description;
}
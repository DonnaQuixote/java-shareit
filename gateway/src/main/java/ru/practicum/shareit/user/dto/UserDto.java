package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public final class UserDto {
    @NotBlank(message = "name не может быть пустым")
    @NotNull(message = "требуется поле name")
    private final String name;
    @NotNull(message = "требуется email")
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email имеет некорректный формат")
    private final String email;
}
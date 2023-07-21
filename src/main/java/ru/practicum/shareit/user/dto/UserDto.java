package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "name не может быть пустым")
    @NotNull(message = "требуется поле name")
    private String name;
    @NotNull(message = "требуется email")
    @NotBlank(message = "email не может быть пустым")
    @Email(message = "email имеет некорректный формат")
    private String email;
}
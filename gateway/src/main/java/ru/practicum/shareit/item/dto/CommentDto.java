package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor(onConstructor=@__(@JsonCreator))
public final class CommentDto {
    @NotNull(message = "Необходим текст комментария")
    @NotBlank(message = "Текст комментария не может быть пустым")
    private final String text;
}
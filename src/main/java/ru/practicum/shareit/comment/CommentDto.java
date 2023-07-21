package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotNull(message = "Необходим текст комментария")
    @NotBlank(message = "Текст комментария не может быть пустым")
    private String text;
    private Long item;
    private Long author;
    private String authorName;
    private LocalDateTime created;
}
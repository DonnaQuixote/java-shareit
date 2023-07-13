package ru.practicum.shareit.comment;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsDto.add(toCommentDto(comment));
        }
        return commentsDto;
    }
}
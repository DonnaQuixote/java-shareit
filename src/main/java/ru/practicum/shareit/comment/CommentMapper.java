package ru.practicum.shareit.comment;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getItem(),
                commentDto.getAuthor(),
                commentDto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                null,
                comment.getCreated()
        );
    }
}
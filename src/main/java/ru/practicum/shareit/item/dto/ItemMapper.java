package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                null,
                null,
                CommentMapper.toCommentDto(item.getComments())
        );
    }

    public static Item toItem(Long ownerId, ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                itemDto.getRequest()
        );
    }
}
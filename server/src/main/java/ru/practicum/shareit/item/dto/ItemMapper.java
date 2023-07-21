package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwner(),
                item.getAvailable(),
                item.getRequest() == null ? null : item.getRequest().getId(),
                null,
                null,
                item.getComments().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwner()
        );
    }

    public static List<ItemDto> toItemDto(List<Item> items) {
        return items == null ? null : items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
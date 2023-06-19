package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto postItem(Long userId, ItemDto itemDto);

    ItemDto patchItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getItem(Long id);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String text);
}
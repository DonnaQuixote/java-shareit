package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dao.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserStorage userStorage;

    @Override
    public ItemDto postItem(Long userId, ItemDto itemDto) {
        if (userStorage.getUser(userId) != null) {
            return ItemMapper.toItemDto(storage.addItem(ItemMapper.toItem(userId, itemDto)));
        } else throw new NoSuchElementException("Пользователь не найден");
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, ItemDto itemDto) {
        if (storage.getItem(itemId) != null && Objects.equals(userId, storage.getItem(itemId).getOwner())) {
            return ItemMapper.toItemDto(storage.patchItem(itemId, ItemMapper.toItem(userId, itemDto)));
        } else throw new NoSuchElementException(String.format("Вы не являетесь владельцем вещи c id %d", itemId));
    }

    @Override
    public ItemDto getItem(Long id) {
        if (storage.getItem(id) != null) {
            return ItemMapper.toItemDto(storage.getItem(id));
        } else throw new NoSuchElementException("Вещь не найдена");
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> foundItems = storage.getItems(userId);
        List<ItemDto> foundItemsDto = new ArrayList<>();
        for (Item item : foundItems) {
            foundItemsDto.add(ItemMapper.toItemDto(item));
        }
        return foundItemsDto;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (!text.isBlank()) {
            text = text.toLowerCase().trim();
            List<Item> items = storage.getItems();
            List<ItemDto> foundItemsDto = new ArrayList<>();
            for (Item item : items) {
                if (item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text) && item.getAvailable()) {
                    foundItemsDto.add(ItemMapper.toItemDto(item));
                }
            }
            return foundItemsDto;
        } else return new ArrayList<>();
    }
}
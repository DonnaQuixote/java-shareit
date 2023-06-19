package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item addItem(Item item);

    Item patchItem(Long id, Item item);

    Item getItem(Long id);

    List<Item> getItems(Long userId);

    List<Item> getItems();
}
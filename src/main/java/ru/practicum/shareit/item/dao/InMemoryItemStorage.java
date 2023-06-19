package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Long, Item> items  = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Item addItem(Item item) {
        item.setId(++counter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item patchItem(Long itemId, Item item) {
        Item oldItem = items.get(itemId);
        if (item.getName() != null) oldItem.setName(item.getName());
        if (item.getDescription() != null) oldItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) oldItem.setAvailable(item.getAvailable());
        return oldItem;
    }

    @Override
    public Item getItem(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getItems(Long userId) {
        List<Item> found = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwner().equals(userId)) found.add(item);
        }
        return found;
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(items.values());
    }
}
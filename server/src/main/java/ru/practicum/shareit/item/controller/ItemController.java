package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto postItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestBody ItemDto item) {
        log.debug(String.format("Получен запрос POST от пользователя %d", userId));
        return service.postItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                             @PathVariable Long itemId, @RequestBody ItemDto item) {
        log.debug(String.format("Получен запрос PATCH от пользователя %d на вещь %d", userId, itemId));
        return service.patchItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET вещи с id %d", itemId));
        return service.getItem(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        log.debug(String.format("Получен запрос GET на вещи пользователя %d", userId));
        return service.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug(String.format("Получен запрос GET на поиск по строке: %s", text));
        return service.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId, @RequestBody CommentDto comment,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос POST на комментарий к вещи %d от пользователя %d", itemId, userId));
        return service.postComment(itemId,userId,comment);
    }
}
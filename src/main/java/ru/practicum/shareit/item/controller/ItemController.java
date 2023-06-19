package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto postItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto item) {
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
    public ItemDto getItem(@PathVariable Long itemId) {
        log.debug(String.format("Получен запрос GET вещи с id %d", itemId));
        return service.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET на вещи пользователя %d", userId));
        return service.getItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.debug(String.format("Получен запрос GET на поиск по строке: %s", text));
        return service.searchItems(text);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String,String> handleNotFound(final NoSuchElementException e) {
        return Map.of("ОШИБКА", "id пользователя или вещи некорректен");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> handleNoHeader(final MissingRequestHeaderException e) {
        return Map.of("ОШИБКА", "id пользователя не обнаружен");
    }
}
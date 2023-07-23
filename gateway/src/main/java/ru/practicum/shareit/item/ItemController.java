package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @RequestBody @Valid ItemDto item) {
        log.debug(String.format("Получен запрос POST от пользователя %d", userId));
        return itemClient.postItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                             @PathVariable Long itemId, @RequestBody ItemDto item) {
        log.debug(String.format("Получен запрос PATCH от пользователя %d на вещь %d", userId, itemId));
        return itemClient.patchItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET вещи с id %d", itemId));
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug(String.format("Получен запрос GET на вещи пользователя %d", userId));
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug(String.format("Получен запрос GET на поиск по строке: %s", text));
        return itemClient.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable Long itemId, @RequestBody @Valid CommentDto comment,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос POST на комментарий к вещи %d от пользователя %d", itemId, userId));
        return itemClient.postComment(itemId,userId,comment);
    }
}
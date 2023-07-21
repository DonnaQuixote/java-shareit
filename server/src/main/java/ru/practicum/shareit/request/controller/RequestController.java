package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {
    private final RequestService service;

    @PostMapping
    public RequestDto postRequest(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                  @RequestBody RequestDto request) {
        log.debug(String.format("Получен запрос POST /requests от пользователя %d", userId));
        return service.postRequest(userId, request);
    }

    @GetMapping
    public List<RequestDto> getRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET /requests от пользователя %d", userId));
        return service.getRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                           @RequestParam Integer from,
                                           @RequestParam Integer size) {
        log.debug(String.format("Получен запрос GET /requests/all от пользователя %d", userId));
        return service.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @PathVariable Long requestId) {
        log.debug(String.format("Получен запрос GET /requests/%d от пользователя %d", requestId, userId));
        return service.getRequest(userId, requestId);
    }
}
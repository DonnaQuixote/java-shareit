package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> postRequest(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                      @RequestBody @Valid RequestDto request) {
        log.debug(String.format("Получен запрос POST /requests от пользователя %d", userId));
        return requestClient.postRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(String.format("Получен запрос GET /requests от пользователя %d", userId));
        return requestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug(String.format("Получен запрос GET /requests/all от пользователя %d", userId));
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                 @PathVariable Long requestId) {
        log.debug(String.format("Получен запрос GET /requests/%d от пользователя %d", requestId, userId));
        return requestClient.getRequest(userId, requestId);
    }
}
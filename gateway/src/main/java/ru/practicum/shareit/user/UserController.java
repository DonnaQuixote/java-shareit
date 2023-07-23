package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@Validated
@Slf4j
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.debug(String.format("Получен запрос GET пользователя с id %d", userId));
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.debug("Получен запрос GET для всех пользователей");
        return userClient.getUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@PathVariable Long userId, @RequestBody UserDto user) {
        log.debug(String.format("Получен запрос PATCH для пользователя с id %d", userId));
        return userClient.patchUser(userId, user);
    }

    @PostMapping
    public ResponseEntity<Object> postUser(@RequestBody @Valid UserDto user) {
        log.debug("Получен запрос POST юзера");
        return userClient.postUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug(String.format("Получен запрос DELETE пользователя с id %d", userId));
        userClient.deleteUser(userId);
    }
}
package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.debug(String.format("Получен запрос GET пользователя с id %d", userId));
        return service.getUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.debug("Получен запрос GET для всех пользователей");
        return service.getUsers();
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@PathVariable Long userId, @RequestBody UserDto user) {
        log.debug(String.format("Получен запрос PATCH для пользователя с id %d", userId));
        return service.patchUser(userId, user);
    }

    @PostMapping
    public UserDto postUser(@RequestBody @Valid UserDto user) {
        log.debug("Получен запрос POST юзера");
        return service.postUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.debug(String.format("Получен запрос DELETE пользователя с id %d", userId));
        service.deleteUser(userId);
    }
}
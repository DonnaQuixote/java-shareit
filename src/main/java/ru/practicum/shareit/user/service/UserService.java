package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public UserDto getUser(Long id) {
        if (storage.getUser(id) != null) {
            return UserMapper.toUserDto(storage.getUser(id));
        } else throw new NoSuchElementException("Пользователь не найден");
    }

    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        List<User> users = storage.getUsers();
        for (User user : users) {
            usersDto.add(UserMapper.toUserDto(user));
        }
        return usersDto;
    }

    public UserDto postUser(UserDto user) {
        if (storage.isEmailNotExists(null, user.getEmail())) {
            return UserMapper.toUserDto(storage.postUser(UserMapper.toUser(user)));
        } else throw new ValidationException("Пользователь с данным email уже существует");
    }

    public UserDto patchUser(Long id, UserDto user) {
        if (storage.isEmailNotExists(id, user.getEmail())) {
            return UserMapper.toUserDto(storage.patchUser(id, UserMapper.toUser(user)));
        } else throw new ValidationException("Пользователь с данным email уже существует");
    }

    public void deleteUser(Long id) {
        storage.deleteUser(id);
    }
}
package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDto getUser(Long id) {
        if (repository.findById(id).isPresent()) {
            return UserMapper.toUserDto(repository.findById(id).get());
        } else throw new NoSuchElementException("Пользователь не найден");
    }

    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        List<User> users = repository.findAll();
        users.forEach(user -> usersDto.add(UserMapper.toUserDto(user)));
        return usersDto;
    }

    public UserDto postUser(UserDto user) {
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(user)));
    }

    public UserDto patchUser(Long id, UserDto user) {
        User oldUser = repository.findById(id).orElseThrow();
        if (user.getName() != null) oldUser.setName(user.getName());
        if (user.getEmail() != null) oldUser.setEmail(user.getEmail());
        return UserMapper.toUserDto(repository.save(oldUser));
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
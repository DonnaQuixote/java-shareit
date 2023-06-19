package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User getUser(Long id);

    List<User> getUsers();

    User postUser(User user);

    User patchUser(Long id, User user);

    void deleteUser(Long id);

    boolean isEmailNotExists(Long id, String email);
}
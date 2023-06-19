package ru.practicum.shareit.user.dao;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Data
@Repository
public class InMemoryUserStorage implements UserStorage {
    private Long counter = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User postUser(User user) {
        user.setId(++counter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User patchUser(Long id, User user) {
        User oldUser = users.get(id);
        if (user.getName() != null) oldUser.setName(user.getName());
        if (user.getEmail() != null) oldUser.setEmail(user.getEmail());
        return oldUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    @Override
    public boolean isEmailNotExists(Long id, String email) {
        boolean notExists = true;
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email) && !Objects.equals(user.getId(), id)) {
                notExists = false;
                break;
            }
        }
        return notExists;
    }
}
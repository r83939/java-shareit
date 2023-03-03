package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> users;

    private long id;

    public UserRepositoryImpl() {
        this.users = new HashMap<>();
        this.id = 0;
    }

    private long setId() {
        id++;
        return id;
    }

    public boolean isEmailExist(String email) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserExist(long userId) {
       return users.containsKey(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getOne(long userId) {
        return users.get(userId);
    }

    @Override
    public User save(User user) {
        if (user.getId() == 0) { // новый пользователь
            user.setId(setId());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User delete(long userId) {
        return users.remove(userId);
    }

    public User getUserByEmail(String email) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return entry.getValue();
            }
        }
        return null;
    }
}

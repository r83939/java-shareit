package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getOne(long userId);

    User save(User user);

    User delete(long userId);
}

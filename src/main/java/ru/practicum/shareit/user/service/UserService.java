package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityAlreadyExistException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    User getUserById(long userId);

    List<User> getAllUsers();

    User addUser(User user) throws EntityAlreadyExistException, DuplicateEmailException;

    User updateUser(User user) throws EntityAlreadyExistException, EntityNotFoundException, DuplicateEmailException;

    User deleteUser(long userId);
}

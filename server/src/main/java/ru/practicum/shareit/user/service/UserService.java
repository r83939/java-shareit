package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto getUserById(long userId) throws EntityNotFoundException;

    List<UserDto> getAllUsers();

    UserDto addUser(User user) throws InvalidParameterException;

    UserDto updateUser(User user) throws EntityNotFoundException, DuplicateEmailException;

    UserDto deleteUser(long userId) throws InvalidParameterException, EntityNotFoundException;
}
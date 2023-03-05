package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepositoryImpl;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepositoryImpl userRepositoryImpl, UserMapper userMapper) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.userMapper = userMapper;
    }


    @Override
    public UserDto getUserById(long userId) {
        return userMapper.toUserDto(userRepositoryImpl.getOne(userId));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepositoryImpl.getAll().stream()
                .map(u -> userMapper.toUserDto(u))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(User user) throws DuplicateEmailException, InvalidParameterException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidParameterException("поле email должно быть заполнено.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new InvalidParameterException("поле name должно быть заполнено.");
        }
        if (userRepositoryImpl.isEmailExist(user.getEmail())) {
            throw new DuplicateEmailException("этот еmail: " + user.getEmail() + " уже используется");
        }
        return userMapper.toUserDto(userRepositoryImpl.save(user));
    }

    @Override
    public UserDto updateUser(User user) throws  EntityNotFoundException, DuplicateEmailException {
        User updateUser = userRepositoryImpl.getOne(user.getId());
        if (updateUser == null) {
            throw new EntityNotFoundException("Нет пользователя с id: " + user.getId());
        }
        if ((user.getEmail() == null || user.getEmail().isBlank())) {
            user.setEmail(updateUser.getEmail());
        }
        if ((user.getName() == null || user.getName().isBlank())) {
            user.setName(updateUser.getName());
        }
        if (!updateUser.getEmail().equals(user.getEmail()) && (getUserByEmail(user.getEmail()) != null)) {
                throw new DuplicateEmailException("этот еmail: " + user.getEmail() + " уже используется другим пользователем");
        }

        return userMapper.toUserDto(userRepositoryImpl.save(user));
    }

    @Override
    public UserDto deleteUser(long userId) throws InvalidParameterException {
        if (userId <= 0) {
            throw new InvalidParameterException("id - должно быть целым числом, вы передали " +  userId);
        }
        return userMapper.toUserDto(userRepositoryImpl.delete(userId));
    }

    public User getUserByEmail(String email) {
        return userRepositoryImpl.getUserByEmail(email);
    }
}

package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.domain.validator.UserValidator;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepository") UserRepository userRepo, UserMapper userMapper, UserValidator userValidator) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    @Override
    public UserDto getUserById(long userId) throws EntityNotFoundException {
        if (userRepo.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        return userMapper.toUserDto(userRepo.findById(userId).get());
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(u -> userMapper.toUserDto(u))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto addUser(User user) throws InvalidParameterException {
        userValidator.newUserValidate(user);
        return userMapper.toUserDto(userRepo.save(user));
    }

    @Override
    public UserDto updateUser(User user) throws EntityNotFoundException, DuplicateEmailException {
        Optional<User> updateUser = userRepo.findById(user.getId());
        if (updateUser.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + user.getId());
        }
        if ((user.getEmail() == null || user.getEmail().isBlank())) {
            user.setEmail(updateUser.get().getEmail());
        }
        if ((user.getName() == null || user.getName().isBlank())) {
            user.setName(updateUser.get().getName());
        }
        if (!updateUser.get().getEmail().equals(user.getEmail()) && (userRepo.existsByEmail(user.getEmail()))) {
            throw new DuplicateEmailException("этот еmail: " + user.getEmail() + " уже используется другим пользователем");
        }
        return userMapper.toUserDto(userRepo.save(user));
    }

    @Override
    public UserDto deleteUser(long userId) throws InvalidParameterException, EntityNotFoundException {
        if (userId <= 0) {
            throw new InvalidParameterException("id - должно быть целым числом, вы передали " +  userId);
        }
        Optional<User> deleteUser = userRepo.findById(userId);
        if (deleteUser.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        userRepo.deleteById(userId);
        return userMapper.toUserDto(deleteUser.get());
    }
}

package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void getUserById_whenUserFound_thenReturnUser() throws EntityNotFoundException {
        long userId = 0L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserDto actualUser = userService.getUserById(userId);

        assertEquals(UserMapper.toUserDto(expectedUser), actualUser);
    }

    @Test
    void getUserById_whenUserNotFound_thenReturnNotFoundException() throws EntityNotFoundException {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void addUser_whenNewUserValid_thenSaveUser() throws InvalidParameterException, DuplicateEmailException {
        User addUser = new User();
        addUser.setEmail("user1@mail.ru");
        addUser.setName("user1");
        when(userRepository.save(addUser)).thenReturn(addUser);

        UserDto addedUser = userService.addUser(addUser);

        assertEquals(UserMapper.toUserDto(addUser), addedUser);
        verify(userRepository).save(addUser);
    }

    @Test
    void addUser_whenNewUserInvalid_thenUserNotSaved() throws InvalidParameterException, DuplicateEmailException {
        User addUser = new User();
        addUser.setId(1L);
        addUser.setEmail("user1@mail.ru");
        addUser.setName("user1");
        when(userService.addUser(addUser)).thenThrow(DuplicateEmailException.class);


        assertThrows(DuplicateEmailException.class,
                () -> userService.addUser(addUser));

        verify(userRepository, never()).save(addUser);
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}
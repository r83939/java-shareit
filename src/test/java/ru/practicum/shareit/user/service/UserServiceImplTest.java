package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.domain.validator.UserValidator;
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
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

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
        doThrow(InvalidParameterException.class)
                .when(userValidator).newUserValidate(addUser);


        assertThrows(InvalidParameterException.class,
                () -> userService.addUser(addUser));

        verify(userRepository, never()).save(addUser);
    }

    @Test
    void updateUser() throws DuplicateEmailException, EntityNotFoundException {
        Long userId = 1L;
        User oldUser = new User();
        oldUser.setId(0L);
        oldUser.setEmail("user1@mail.ru");
        oldUser.setName("user1");

        User newUser = new User();
        newUser.setId(1L);
        newUser.setEmail("user2@mail.ru");
        newUser.setName("user2");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(newUser)).thenReturn(newUser);

        UserDto actualUser = userService.updateUser(newUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        UserDto savedUser = UserMapper.toUserDto(userArgumentCaptor.getValue());

        assertEquals("user2", savedUser.getName());
        assertEquals("user2@mail.ru", savedUser.getEmail());
    }

    @Test
    void deleteUser( ) {
    }
}
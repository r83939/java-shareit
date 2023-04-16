package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
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
        expectedUser.setName("user1");
        expectedUser.setEmail("user1@mail.ru");
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
        User user1 = new User(1,"User1", "user1@mail.ru");
        User user2 = new User(2,"User2", "user2@mail.ru");
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2));
        List<UserDto> expectUsers = users.stream().map(u -> UserMapper.toUserDto(u)).collect(Collectors.toList());
        when(userRepository.findAll()).thenReturn(users) ;

        List<UserDto> actualUsers = userService.getAllUsers();

        assertEquals(expectUsers, actualUsers);
        assertEquals(2, actualUsers.size());
    }

    @Test
    void addUser_whenNewUserValid_thenSaveUser() throws InvalidParameterException {
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
        doThrow(InvalidParameterException.class).when(userValidator).newUserValidate(addUser);

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

    @SneakyThrows
    @Test
    void deleteUser()  {
        User user = new User(1L, "user1", "user1@email.com");
        UserDto expectUserDto = UserMapper.toUserDto(user);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        willDoNothing().given(userRepository).deleteById(1L);
        UserDto actualUserDto =  userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
        assertEquals(expectUserDto, actualUserDto);
    }
}
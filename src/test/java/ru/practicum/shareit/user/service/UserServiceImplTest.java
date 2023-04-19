package ru.practicum.shareit.user.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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

    User expectedUser;
    User user1;
    User user2;
    List<User> users;
    List<UserDto> expectUsers;

    @BeforeEach
    public void init() {

        expectedUser = new User();
        expectedUser.setName("user1");
        expectedUser.setEmail("user1@mail.ru");
        user1 = new User(1,"User1", "user1@mail.ru");
        user2 = new User(2,"User2", "user2@mail.ru");
        users = new ArrayList<>(Arrays.asList(user1, user2));
        expectUsers = users.stream().map(u -> UserMapper.toUserDto(u)).collect(Collectors.toList());
    }


    @Test
    void getUserById_whenUserFound_thenReturnUser() throws EntityNotFoundException {

        when(userRepository.findById(0L)).thenReturn(Optional.of(expectedUser));

        UserDto actualUser = userService.getUserById(0L);

        assertEquals(UserMapper.toUserDto(expectedUser), actualUser);
    }

    @Test
    void getUserById_whenUserNotFound_thenReturnNotFoundException() throws EntityNotFoundException {

        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(0L));
    }

    @Test
    void getAllUsers() {

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> actualUsers = userService.getAllUsers();

        assertEquals(expectUsers, actualUsers);
        assertEquals(2, actualUsers.size());
    }

    @Test
    void addUser_whenNewUserValid_thenSaveUser() throws InvalidParameterException {

        when(userRepository.save(expectedUser)).thenReturn(expectedUser);

        UserDto addedUser = userService.addUser(expectedUser);

        assertEquals(UserMapper.toUserDto(expectedUser), addedUser);
        verify(userRepository).save(expectedUser);
    }

    @Test
    void addUser_whenNewUserInvalid_thenUserNotSaved() throws InvalidParameterException {

        doThrow(InvalidParameterException.class).when(userValidator).newUserValidate(user1);

        assertThrows(InvalidParameterException.class,
                () -> userService.addUser(user1));

        verify(userRepository, never()).save(user1);
    }

    @Test
    void updateUser() throws DuplicateEmailException, EntityNotFoundException {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        user1.setName("Updated User");
        when(userRepository.save(user1)).thenReturn(user1);

        UserDto actualUser = userService.updateUser(user1);

        verify(userRepository).save(userArgumentCaptor.capture());
        UserDto savedUser = UserMapper.toUserDto(userArgumentCaptor.getValue());

        assertEquals("Updated User", savedUser.getName());
        assertEquals("user1@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserFail() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(user1));
    }

    @Test
    void updateUser_whenNoEmail() throws DuplicateEmailException, EntityNotFoundException {
        User updateUser = new User(1L, "user1", null);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(updateUser)).thenReturn(updateUser);
;
        userService.updateUser(updateUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        UserDto savedUser = UserMapper.toUserDto(userArgumentCaptor.getValue());

        assertEquals("user1", savedUser.getName());
        assertEquals("user1@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenNoName() throws DuplicateEmailException, EntityNotFoundException {
        User updateUser = new User(1L, null, "user1@mail.ru");
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.save(updateUser)).thenReturn(updateUser);
        ;
        userService.updateUser(updateUser);

        verify(userRepository).save(userArgumentCaptor.capture());
        UserDto savedUser = UserMapper.toUserDto(userArgumentCaptor.getValue());

        assertEquals("User1", savedUser.getName());
        assertEquals("user1@mail.ru", savedUser.getEmail());
    }

    @Test
    void updateUser_whenDublicateEmail() {

        User updateUser = new User(1L, "User1", "user4@mail.ru");
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.existsByEmail("user4@mail.ru")).thenReturn(true);


        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(updateUser));
    }




    @SneakyThrows
    @Test
    void deleteUser() {

        UserDto expectUserDto = UserMapper.toUserDto(user1);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        willDoNothing().given(userRepository).deleteById(1L);
        UserDto actualUserDto =  userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
        assertEquals(expectUserDto, actualUserDto);
    }

    @SneakyThrows
    @Test
    void deleteUser_whenUserIdFail() {

        assertThrows(InvalidParameterException.class, () -> userService.deleteUser(-1));
    }

    @SneakyThrows
    @Test
    void deleteUser_whenNoUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1));
    }
}
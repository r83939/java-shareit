package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserRepository userRepository;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    @SneakyThrows
    public void getUser_whenUserExist_returnStatusOkAndUser()  {
        long userId = 1L;
        User user1 = new User(1, "user1@mail.ru", "user1");
        UserDto userDto = UserMapper.toUserDto(user1);

        when(userServiceImpl.getUserById(userId))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userServiceImpl).getUserById(userId);
    }

    @Test
    public void getAllUsers() throws Exception {
        UserDto user1 = new UserDto(1, "user1@mail.ru", "user1");
        UserDto user2 = new UserDto(2, "user2@mail.ru", "user2");
        UserDto user3 = new UserDto(3, "user3@mail.ru", "user3");
        List<UserDto> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

        Mockito.when(userServiceImpl.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].email", is("user2@mail.ru")))
                .andExpect(jsonPath("$[1].name", is("user2")));
    }


    @Test
    void createUser() throws Exception {
        User newUser = new User(1, "user1","user1@mail.ru");
        UserDto addedUser = UserMapper.toUserDto(newUser);
        when(userServiceImpl.addUser(Mockito.any(User.class))).thenReturn(addedUser);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(newUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(newUser)));
    }

    @Test
    public void addUserCheckJsonTest() throws Exception {
        User newUser = new User(1, "user1","user1@mail.ru");
        UserDto addedUser = UserMapper.toUserDto(newUser);
        when(userServiceImpl.addUser(Mockito.any(User.class))).thenReturn(addedUser);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(newUser))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("user1")))
                .andExpect(jsonPath("$.email", is("user1@mail.ru")));
        verify(userServiceImpl, times(1)).addUser(Mockito.any(User.class));
    }

    @Test
    @SneakyThrows
    void updateUser() {
        Long userId = 0L;
        User oldUser = new User(userId, "user1", "user1@mail.ru");

        User newUser = new User(userId, "user2", "user2@mail.ru");

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        UserDto actualUserDto = userServiceImpl.updateUser(newUser);

        verify(userServiceImpl).updateUser(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals("user2", savedUser.getName());
        assertEquals("user2@mail.ru", savedUser.getEmail());
    }

    @Test
    @SneakyThrows
    public void deleteUserByIdTest() {
        mockMvc.perform(delete("/users/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito
                .verify(userServiceImpl, Mockito.times(1))
                .deleteUser(1);
    }
}
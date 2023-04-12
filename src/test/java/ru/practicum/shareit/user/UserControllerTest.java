package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    UserDto user1 = new UserDto(1, "user1@mail.ru", "user1");
    UserDto user2 = new UserDto(2, "user2@mail.ru", "user2");
    UserDto user3 = new UserDto(3, "user3@mail.ru", "user3");
    List<UserDto> users = new ArrayList<>(Arrays.asList(user1, user2, user3));

    @Test
    @SneakyThrows
    public void getUser()  {
        long userId = 0L;
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userServiceImpl).getUserById(userId);


    }

    @Test
    public void getAllUsers() throws Exception {
        Mockito.when(userServiceImpl.getAllUsers()).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].email", is("user2@mail .ru")))
                .andExpect(jsonPath("$[1].name", is("user2")));
    }

    @Test
    void createUser() throws Exception {
        User newUser = new User(1, "user3","user3@mail.ru" );
        UserDto addedUser = new UserDto(1L,"user3@mail.ru", "user3");
        when(userServiceImpl.addUser(newUser)).thenReturn(addedUser);

        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(addedUser), result);
    }

    @Test
    void updateUser() {
        Long userId = 0L;
        User userToUpdate = new User();
        //userToUpdate.setName();
    }

    @Test
    void deleteUser() {
    }
}
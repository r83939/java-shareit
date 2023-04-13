package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    //private final UserMapper userMapper;


    @Test
    void toUserDto() {
        UserDto expextedUserDto = new UserDto(1L, "user1@mail.ru", "user1");
        User user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user1@mail.ru");

        UserDto actualUser = UserMapper.toUserDto(user);
        assertEquals(expextedUserDto.getId(), actualUser.getId());
        assertEquals(expextedUserDto.getName(), actualUser.getName());
        assertEquals(expextedUserDto.getEmail(), actualUser.getEmail());
    }
}
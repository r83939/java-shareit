package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {
    @Test
    void testHashCode() {
        UserDto userDto1 = new UserDto(1L, "user1@mail.ru", "user1");
        UserDto userDto2 = new UserDto(1L, "user1@mail.ru", "user1");

        assertEquals(userDto1.hashCode(), userDto2.hashCode());
    }
}
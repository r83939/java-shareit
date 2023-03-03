package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String email;

    public UserDto(long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}

package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

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

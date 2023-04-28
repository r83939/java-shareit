package ru.practicum.shareit.user.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id && name.equals(userDto.name) && email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}

package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
public class User {
    private long id;     // уникальный идентификатор пользователя

    private String name; // имя или логин пользователя

    @Email(message = "Отсутствует email или неверный формат")
    private String email; // уникальный адрес электронной почты
}

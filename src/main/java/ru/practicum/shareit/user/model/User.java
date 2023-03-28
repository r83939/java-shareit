package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;


@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                                    // уникальный идентификатор пользователя

    @Column(name = "name")
    private String name;                                // имя или логин пользователя

    @Column(name = "email")
    @Email(message = "Отсутствует email или неверный формат")
    private String email;                              // уникальный адрес электронной почты
}

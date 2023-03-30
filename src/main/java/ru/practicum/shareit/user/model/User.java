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
    @GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_USERS", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;                                    // уникальный идентификатор пользователя

    @Column(name = "name")
    private String name;                                // имя или логин пользователя

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Отсутствует email или неверный формат")
    private String email;                              // уникальный адрес электронной почты
}

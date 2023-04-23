package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;


@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_USERS", allocationSize = 1)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Отсутствует email или неверный формат")
    private String email;
}

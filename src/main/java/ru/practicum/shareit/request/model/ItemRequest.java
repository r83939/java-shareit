package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "requests")
@Getter @Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;            // уникальный идентификатор запроса

    @Column(name = "description")
    private String description; // текст запроса, содержащий описание требуемой вещи

    @Column(name = "user_id")
    private long requester;     // пользователь, создавший запрос


    @Column(name = "created")
    private Date created;       // дата и время создания запроса
}

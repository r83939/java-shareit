package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Entity
@Table(name = "items", schema = "public")
@Getter @Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;              // уникальный идентификатор вещи

    @NotEmpty
    @Column(name="name")
    private String name;          // краткое название

    @NotEmpty
    @Column(name="description")
    private String description;   // развёрнутое описание

    @NotNull (message = "поле available не должно быть пустым.")
    @Column(name="available")
    private Boolean available;    // статус о том, доступна или нет вещь для аренды

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;           // владелец вещи

    @Column(name="request_id")
    private long request;        // ссылка на запрос от другого пользователя на создание вещи

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private List<Comment> comments;

}

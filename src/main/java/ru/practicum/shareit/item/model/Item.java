package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_ITEMS", allocationSize=1)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private long id;              // уникальный идентификатор вещи

    @NotEmpty
    @Column(name = "name")
    private String name;          // краткое название

    @NotEmpty
    @Column(name = "description")
    private String description;   // развёрнутое описание

    @NotNull (message = "поле available не должно быть пустым.")
    @Column(name = "available")
    private Boolean available;    // статус о том, доступна или нет вещь для аренды

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;           // владелец вещи

   // @Column(name = "request_id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private ItemRequest request;        // ссылка на запрос от другого пользователя на создание вещи
}

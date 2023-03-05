package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
public class Item {
    private long id;              // уникальный идентификатор вещи
    @NotEmpty
    private String name;          // краткое название
    @NotEmpty
    private String description;   // развёрнутое описание
    @NotNull (message = "поле available не должно быть пустым.")
    private Boolean available;    // статус о том, доступна или нет вещь для аренды
    private User owner;           // владелец вещи
    private ItemRequest request;  // ссылка на запрос от другого пользователя на создание вещи

}

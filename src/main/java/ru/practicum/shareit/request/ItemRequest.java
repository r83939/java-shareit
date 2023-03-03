package ru.practicum.shareit.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
public class ItemRequest {
    private long id;            // уникальный идентификатор запроса
    private String description; // текст запроса, содержащий описание требуемой вещи
    private User requester;     // пользователь, создавший запрос
    private Date created;       // дата и время создания запроса
}

package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentResponceDto {
    private long id;
    private String message;
    private User user;
    private Item item;

    public CommentResponceDto(long id, String message, User user, Item item) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.item = item;
    }
}

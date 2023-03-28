package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;

public class ItemRequestResponceDto {

    private long id;

    private String description;

    private User requester;

    private Date created;

    public ItemRequestResponceDto(long id, String description, User requester, Date created) {
        this.id = id;
        this.description = description;
        this.requester = requester;
        this.created = created;
    }
}

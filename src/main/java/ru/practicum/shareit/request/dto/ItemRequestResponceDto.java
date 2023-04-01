package ru.practicum.shareit.request.dto;

import lombok.Builder;
import ru.practicum.shareit.user.model.User;

import java.util.Date;

@Builder
public class ItemRequestResponceDto {
    private long id;

    private String description;

    private User requester;

    private Date created;
}

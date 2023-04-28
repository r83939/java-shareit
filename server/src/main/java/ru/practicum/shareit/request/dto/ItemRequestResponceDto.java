package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ItemRequestResponceDto {

    private long id;

    private String description;

    private User requester;

    private LocalDateTime created;
}

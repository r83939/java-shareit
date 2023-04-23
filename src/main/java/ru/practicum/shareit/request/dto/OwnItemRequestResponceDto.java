package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
public class OwnItemRequestResponceDto {

    private long id;

    private String description;

    private User requester;

    private LocalDateTime created;

    private List<ItemDto> items;
}

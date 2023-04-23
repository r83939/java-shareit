package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@Setter
@Builder
public class ItemResponceDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long requestId;
    private List<CommentResponceDto> comments;
}

package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemResponceDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private long request;

    private List<Comment> comments;

    public ItemResponceDto(long id, String name, String description, Boolean available, User owner, long request, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
        this.comments = comments;
    }
}

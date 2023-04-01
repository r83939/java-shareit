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
    private Long request;
    private List<CommentResponceDto> comments;

//    public ItemResponceDto(long id, String name, String description, Boolean available, User owner, Long request, List<CommentResponceDto> comments) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.available = available;
//        this.owner = owner;
//        this.request = request;
//        this.comments = comments;
//    }
}

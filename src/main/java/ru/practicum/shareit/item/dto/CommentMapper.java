package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {
    public static CommentResponceDto toCommentResponceDto(Comment comment) {
        return new CommentResponceDto(
                comment.getId(),
                comment.getMessage(),
                comment.getUser(),
                comment.getItem()
        );
    }
}

package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String message;
    private Long userId;
    private Long itemId;
}

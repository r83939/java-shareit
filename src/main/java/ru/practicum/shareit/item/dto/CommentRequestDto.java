package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
public class CommentRequestDto {
    @NotEmpty
    private String text;

    public CommentRequestDto() {
    }

    public CommentRequestDto(String text) {
        this.text = text;
    }
}

package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ItemWithBookingResponceDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private SpecialBookingDto lastBooking;
    private SpecialBookingDto nextBooking;
    private Long request;
    private List<CommentResponceDto> comments;
}

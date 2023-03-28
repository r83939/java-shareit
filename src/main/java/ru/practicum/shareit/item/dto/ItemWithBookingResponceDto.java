package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Getter
@Setter
public class ItemWithBookingResponceDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private SpecialBookingDto lastBooking;
    private SpecialBookingDto nextBooking;
    private long request;
    private List<CommentResponceDto> comments;
    public ItemWithBookingResponceDto(long id,
                                      String name,
                                      String description,
                                      Boolean available,
                                      User owner,
                                      SpecialBookingDto lastBooking,
                                      SpecialBookingDto nextBooking,
                                      long request,
                                      List<CommentResponceDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
        this.request = request;
        this.comments = comments;
    }
}

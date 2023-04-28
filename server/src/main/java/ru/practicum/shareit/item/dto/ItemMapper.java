package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemMapper {
    public static ItemResponceDto toItemResponceDto(Item item, List<CommentResponceDto> comments) {
        return new ItemResponceDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                comments
        );
    }

    public static SpecialBookingDto toSpecialBookingDto(Booking booking) {
        return new SpecialBookingDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }

    public static CommentResponceDto commentResponceDto(Comment comment) {
        return new CommentResponceDto(comment.getId(), comment.getText(), comment.getUser().getName(),comment.getCreated());
    }
}


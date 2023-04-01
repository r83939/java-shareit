package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Component
public class ItemMapper {

//    public static ItemRequestDto toItemDto(Item item) {
//       return new ItemRequestDto(
//              item.getId(),
//                item.getName(),
//                item.getDescription(),
//                item.getAvailable(),
//                item.getOwner()
//        );
//    }


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

//    public static  ItemWithBookingResponceDto(Item item,
//                                             List<CommentResponceDto> comments,
//                                             SpecialBookingDto specialLastBooking,
//                                             SpecialBookingDto specialNextBooking) {
//        return new ItemWithBookingResponceDto(
//                item.getId(),
//                item.getName(),
//                item.getDescription(),
//                item.getAvailable(),
//                item.getOwner(),
//                specialLastBooking,
//                specialNextBooking,
//                item.getRequest() != null ? item.getRequest().getId() : null,
//                comments
//        );
//    }
}


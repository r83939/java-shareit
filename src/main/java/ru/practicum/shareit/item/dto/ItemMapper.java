package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public static ItemRequestDto toItemDto(Item item) {
        return new ItemRequestDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner()
        );
    }

    public static ItemResponceDto toItemResponceDto(Item item) {
        return new ItemResponceDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                item.getRequest()
        );
    }

    public static SpecialBookingDto toSpecialBookingDto(Booking booking) {
        return new SpecialBookingDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }
}


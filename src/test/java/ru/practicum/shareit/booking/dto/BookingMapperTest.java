package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    @Test
    void toBookingResponceDto() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user1,
                Status.WAITING);

        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);

        assertEquals(booking1.getId(), bookingResponceDto.getId());
        assertEquals(booking1.getStart(), bookingResponceDto.getStart());
        assertEquals(booking1.getEnd(), bookingResponceDto.getEnd());
        assertEquals(booking1.getItem(), bookingResponceDto.getItem());
        assertEquals(booking1.getBooker(), bookingResponceDto.getBooker());
        assertEquals(booking1.getStatus(), bookingResponceDto.getStatus());
    }
}
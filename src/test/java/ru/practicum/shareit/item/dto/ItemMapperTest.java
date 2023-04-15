package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    @Test
    void toItemResponceDto() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1,
                List.of(new CommentResponceDto(1L, "Классная вещь!", user1.getName(), LocalDateTime.parse("2023-05-09 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))));
        assertEquals(item1.getId(), itemResponceDto.getId());
        assertEquals(item1.getName(), itemResponceDto.getName());
        assertEquals(item1.getDescription(), itemResponceDto.getDescription());
        assertEquals(item1.getAvailable(), itemResponceDto.getAvailable());
        assertEquals(item1.getOwner(), itemResponceDto.getOwner());
        assertEquals(item1.getRequest().getId(), itemResponceDto.getRequestId());
        assertEquals(comment.getId(), itemResponceDto.getComments().get(0).getId());

    }

    @Test
    void toSpecialBookingDto() {
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

        SpecialBookingDto specialBookingDto = ItemMapper.toSpecialBookingDto(booking1);
        assertEquals(booking1.getId(), specialBookingDto.getId());
        assertEquals(booking1.getBooker().getId(), specialBookingDto.getBookerId());
    }
}
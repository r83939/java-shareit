package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    private void initContent() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(2L, "user2", "user2@mail.ru");
        User user3 = new User(3L, "user3", "user3@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        ItemRequest expectedItemRequest2 = new ItemRequest(2L,"Запрос вещи2",
                user2, LocalDateTime.parse("2023-04-02 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        ItemRequest expectedItemRequest3 = new ItemRequest(3L,"Запрос вещи3",
                user3, LocalDateTime.parse("2023-04-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        itemRequestRepository.save(expectedItemRequest1);
        itemRequestRepository.save(expectedItemRequest2);
        itemRequestRepository.save(expectedItemRequest3);

        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Item item2 = new Item(2L, "Пила", "Инструмент", true, user2, expectedItemRequest2);
        Item item3 = new Item(3L, "Автомобиль", "Автотранспорт", true, user3, expectedItemRequest3);
        Item item4 = new Item(4L, "Сварочный аппарат", "Инструмент", true, user3, expectedItemRequest3);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);

        Comment comment = new Comment(1L, "Текст Комментария", user1, item2,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        commentRepository.save(comment);

        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1, user1, Status.WAITING);
        Booking booking2 = new Booking(2L,
                LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-04-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1, user2, Status.APPROVED);
        Booking booking3 = new Booking(3L,
                LocalDateTime.parse("2023-05-17 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-05-17 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1, user1, Status.WAITING);
        Booking booking4 = new Booking(3L,
                LocalDateTime.parse("2023-04-19 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-04-20 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1, user1, Status.WAITING);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
    }

    @AfterEach
    private void deleteUsers() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "comments", "bookings", "items", "requests", "users");
        jdbcTemplate.execute("ALTER SEQUENCE SEQ_COMMENTS RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE SEQ_BOOKINGS RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE SEQ_ITEMS RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE SEQ_REQUESTS RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE SEQ_USERS RESTART");
    }

    @Test
    void findBookingsByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findBookingsByBookerIdOrderByStartDesc(1L);
        assertEquals(2, bookings.size());
        assertEquals(1, bookings.get(0).getBooker().getId());
    }

    @Test
    void findBookingsWithPagination() {
        List<Booking> bookings = bookingRepository.findBookingsWithPagination(1L, 0, 4);
        assertEquals(2, bookings.size());
    }

    @Test
    void findBookingByOwnerIdWithPagination() {
        List<Booking> bookings = bookingRepository.findBookingByOwnerIdWithPagination(1L, 0, 3);
        assertEquals(3, bookings.size());
    }

    @Test
    void findBookingByOwnerId() {
        List<Booking> bookings = bookingRepository.findBookingByOwnerId(1L);
        assertEquals(3, bookings.size());
    }

    @Test
    void getLastBookingByItemId() {
        Booking lastBooking = bookingRepository.getLastBookingByItemId(1L);
        assertEquals(2, lastBooking.getId());
    }

    @Test
    void getNextBookingByItemId() {
        Booking nextBooking = bookingRepository.getNextBookingByItemId(1L);
        assertEquals(3, nextBooking.getId());
    }

    @Test
    void existsByBookerAndItem() {
        int countBooking = bookingRepository.existsByBookerAndItem(2L, 1L);
        assertEquals(1, countBooking);
    }
}
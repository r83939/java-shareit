package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void initContent() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(2L, "user2", "user2@mail.ru");
        User user3 = new User(3L, "user3", "user3@mail.ru");
        User user4 = new User(3L, "user3", "user3@mail.ru");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        ItemRequest expectedItemRequest2 = new ItemRequest(2L,"Запрос вещи2",
                user2, LocalDateTime.parse("2023-04-02 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        ItemRequest expectedItemRequest3 = new ItemRequest(3L,"Запрос вещи3",
                user3, LocalDateTime.parse("2023-04-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        ItemRequest expectedItemRequest4 = new ItemRequest(4L,"Запрос вещи4",
                user1, LocalDateTime.parse("2023-04-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        itemRequestRepository.save(expectedItemRequest1);
        itemRequestRepository.save(expectedItemRequest2);
        itemRequestRepository.save(expectedItemRequest3);
        itemRequestRepository.save(expectedItemRequest4);
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
    void getAllByRequesterIdOrderByCreatedDesc() {
        List<ItemRequest> itemRequests = itemRequestRepository.getAllByRequesterIdOrderByCreatedDesc(1L);
        assertEquals(2, itemRequests.size());
        assertEquals(4, itemRequests.get(0).getId());
        assertEquals(1, itemRequests.get(1).getId());
    }

    @Test
    void getAllNotOwnRequests() {
        List<ItemRequest> itemRequests = itemRequestRepository.getAllNotOwnRequests(1L);
        assertEquals(2, itemRequests.size());
        assertEquals(3, itemRequests.get(0).getId());
        assertEquals(2, itemRequests.get(1).getId());
    }

    @Test
    void getAllNotOwnRequestsWithPagination() {
        List<ItemRequest> itemRequests = itemRequestRepository.getAllNotOwnRequestsWithPagination(1L, 1, 2);
        assertEquals(1, itemRequests.size());
    }
}
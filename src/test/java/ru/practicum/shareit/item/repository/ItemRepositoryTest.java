package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;
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
    void findUserIdById() {
        Optional<User> findUser = userRepository.findById(1L);
        assertTrue(findUser.isPresent());
        assertEquals(1, findUser.get().getId());
        assertEquals("user1", findUser.get().getName());
        assertEquals("user1@mail.ru", findUser.get().getEmail());
    }

    @Test
    void search() {
        List<Item> items = itemRepository.search("Инструмент", true );
        assertEquals(3, items.size());
    }

    @Test
    void findAllByOwner() {
        List<Item> items = itemRepository.findAllByOwner(3L);
        assertEquals(2, items.size());
        assertEquals(3, items.get(0).getId());
        assertEquals(4, items.get(1).getId());
    }

    @Test
    void findAllByRequestId() {
        List<Item> items = itemRepository.findAllByRequestId(1L);
        assertEquals(1, items.size());
    }
}
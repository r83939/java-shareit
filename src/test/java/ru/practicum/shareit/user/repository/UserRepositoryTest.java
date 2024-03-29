package ru.practicum.shareit.user.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;

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
    void existsByEmail() {
        User user = new User(0L, "user1", "user1@mail.ru");
        userRepository.save(user);
        assertEquals(true, userRepository.existsByEmail(user.getEmail()));
    }

    @Test
    void addUser() {
        User user = new User(0L, "user1", "user1@mail.ru");
        User actualUser = userRepository.save(user);
        assertEquals(1L, actualUser.getId());
        assertEquals("user1", actualUser.getName());
        assertEquals("user1@mail.ru", actualUser.getEmail());
    }

    @Test
    void deleteUser() {
        User user = new User(1L, "user1", "user1@mail.ru");
        User addedUser =  userRepository.save(user);
        userRepository.deleteById(1L);
        Optional<User> expectedUser =  userRepository.findById(addedUser.getId());
        assertTrue(expectedUser.isEmpty());
    }

    @Test
    void updateUser() {
        User user = new User(0L, "user1", "user1@mail.ru");
        User savedUser = userRepository.save(user);

        assertEquals("user1", user.getName());

        User updateUser = new User(savedUser.getId(), "updatedName", savedUser.getEmail());
        User actualUser = userRepository.save(updateUser);

        User expectedUser = userRepository.getById(savedUser.getId());
        assertEquals("updatedName", expectedUser.getName());
    }
}
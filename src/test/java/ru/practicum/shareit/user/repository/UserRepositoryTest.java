package ru.practicum.shareit.user.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    private void deleteUsers() {
        userRepository.deleteAll();
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
        User user = new User(0L, "user1", "user1@mail.ru");
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
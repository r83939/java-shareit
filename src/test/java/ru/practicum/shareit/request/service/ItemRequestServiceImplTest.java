package ru.practicum.shareit.request.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.dto.OwnItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;

    @Test
    @SneakyThrows
    void addItemRequest() {
        long requestId = 1;
        long userId = 2;
        LocalDateTime created = LocalDateTime.parse("2023-04-01 10:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ItemRequest saveItemRequest = new ItemRequest(requestId,"Запрос вещи", new User(userId, "user2", "user2@mail.ru"), created);
        User expectedUser = new User(userId, "user1", "user1@mail.ru");
        ItemRequestRequestDto addRequestDto = new ItemRequestRequestDto();
        addRequestDto.setDescription("Запрос вещи");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.save(any())).thenReturn(saveItemRequest);

        ItemRequestResponceDto addedItemRequest = itemRequestService.addItemRequest(2L, addRequestDto);

        assertEquals("Запрос вещи", addedItemRequest.getDescription());
    }


    @Test
    void getItemRequests() {

    }

    @Test
    void getOwnItemRequests() {
    }

    @Test
    @SneakyThrows
    void getItemRequest() {
        long requestId = 1;
        long userId = 1;
        LocalDateTime created = LocalDateTime.parse("2023-04-01 10:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ItemRequest expectedItemRequest = new ItemRequest(requestId,"Запрос вещи", new User(2L, "user2", "user2@mail.ru"), created);
        User expectedUser = new User(userId, "user1", "user1@mail.ru");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(expectedItemRequest));

        OwnItemRequestResponceDto actualRequestDto = itemRequestService.getItemRequest(userId, requestId);

        assertEquals(expectedItemRequest.getId() , actualRequestDto.getId() );
        assertEquals(expectedItemRequest.getDescription() , actualRequestDto.getDescription());
        assertEquals(expectedItemRequest.getCreated() , actualRequestDto.getCreated() );
        assertEquals(expectedItemRequest.getRequester().getId() , actualRequestDto.getRequester().getId() );
    }

    @Test
    void updateItemRequest() {
        long requestId = 1;
        long userId = 1;
        LocalDateTime created = LocalDateTime.parse("2023-04-01 10:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ItemRequest oldItemRequest = new ItemRequest(requestId,"Запрос вещи", new User(2L, "user2", "user2@mail.ru"), created);
        ItemRequest newItemRequest = new ItemRequest(requestId,"Новый запрос вещи", new User(2L, "user2", "user2@mail.ru"), created);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(1L, "user1", "user1@mail.ru")));
        when(itemRequestRepository.save(newItemRequest)).thenReturn(newItemRequest);

        //ItemRequestResponceDto updatedItemRequestResponceDto = itemRequestService.updateItemRequest()
        //when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(oldItemRequest));

    }

    @Test
    void deleteItemRequest() {
    }

    @Test
    void testGetItemRequests() {
    }
}
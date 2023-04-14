package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {

    @Test
    void toItemRequestResponceDto() {
        Long userId = 1L;
        User user = new User(userId, "user1", "user1@mail.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Запрос на аренду вещи");
        itemRequest.setRequester(user);
        LocalDateTime created = LocalDateTime.parse("2023-04-01 10:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        itemRequest.setCreated(created);

        ItemRequestResponceDto actualItemRequestResponceDto = ItemRequestMapper.
                toItemRequestResponceDto(itemRequest);

        assertEquals(itemRequest.getDescription(),actualItemRequestResponceDto.getDescription());
        assertEquals(itemRequest.getRequester().getId(),actualItemRequestResponceDto.getRequester().getId());
        assertEquals(itemRequest.getCreated(),actualItemRequestResponceDto.getCreated());
    }

    @Test
    void toOwnItemRequestResponceDto() {
        LocalDateTime created = LocalDateTime.parse("2023-04-01 10:00",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        ItemRequest itemRequest = new ItemRequest(1L,"Запрос вещи", new User(), created);
        OwnItemRequestResponceDto ownItemRequestResponceDto = ItemRequestMapper.
                toOwnItemRequestResponceDto(itemRequest, new ArrayList<ItemDto>());
        assertEquals(itemRequest.getId(), ownItemRequestResponceDto.getId());
        assertEquals(itemRequest.getDescription(), ownItemRequestResponceDto.getDescription());
        assertEquals(itemRequest.getCreated(), ownItemRequestResponceDto.getCreated());
    }

    @Test
    void toItemDto() {
        Item item = new Item(1L,"Дрель","Инструмент", true, new User(), new ItemRequest());
        ItemDto itemDto = ItemRequestMapper.toItemDto(item);

        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
    }
}
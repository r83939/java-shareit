package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @MockBean
    private ItemRequestServiceImpl itemRequestServiceImpl;
    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;

    ItemRequestControllerTest() {
    }


    @Test
    @SneakyThrows
    void createItemRequestRequest() {
        long itemRequestId = 1L;
        long userId = 1L;
        User user = new User(userId, "user1", "user1@mail.ru");

        ItemRequestRequestDto itemRequestRequestDto = new ItemRequestRequestDto();
        itemRequestRequestDto.setDescription("Новый запрос");

        ItemRequestResponceDto itemRequestResponceDto =  ItemRequestResponceDto.builder().build();
        itemRequestResponceDto.setDescription("Новый запрос");

        when(itemRequestServiceImpl.addItemRequest(anyLong(), Mockito.any(ItemRequestRequestDto.class)))
                .thenReturn(itemRequestResponceDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestResponceDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is("Новый запрос")));
    }

    @Test
    @SneakyThrows
    void getRequestById() {
        Long userId = 1L;
        User user = new User(userId, "user1", "user1@mail.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Новый запрос");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        OwnItemRequestResponceDto ownItemRequestResponceDto = ItemRequestMapper.toOwnItemRequestResponceDto(itemRequest, new ArrayList<ItemDto>());

        when(itemRequestServiceImpl.getItemRequest(anyLong(), anyLong())).thenReturn(ownItemRequestResponceDto);
        mockMvc.perform(get("/requests/{id}", 1L)
                        .content(objectMapper.writeValueAsString(ownItemRequestResponceDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ownItemRequestResponceDto)));
    }

    @Test
    @SneakyThrows
    void getOwnRequests() {
        Long userId = 1L;
        User user = new User(userId, "user1", "user1@mail.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("Новый запрос");
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        OwnItemRequestResponceDto ownItemRequestResponceDto = ItemRequestMapper.toOwnItemRequestResponceDto(itemRequest, new ArrayList<ItemDto>());

        when(itemRequestServiceImpl.getOwnItemRequests(Mockito.anyLong()))
                .thenReturn(List.of(ownItemRequestResponceDto));
        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(jsonPath("$.*", is(hasSize(1))))
                .andExpect(jsonPath("$.[0].id", is(ownItemRequestResponceDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(ownItemRequestResponceDto.getDescription())))
                .andExpect(jsonPath("$.[0].requester.id", is(ownItemRequestResponceDto.getRequester().getId()), Long.class))
                .andExpect(jsonPath("$.[0].created", is(Matchers.notNullValue())))
                .andExpect(jsonPath("$.[0].items", is(Matchers.notNullValue())));
    }

    @Test
    void getRequests() {
    }

    @Test
    void updateItemRequestRequest() {
    }

    @Test
    void deleteItemRequestRequest() {
    }
}
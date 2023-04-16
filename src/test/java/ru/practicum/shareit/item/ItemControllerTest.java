package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemServiceImpl itemService;

    @Test
    @SneakyThrows
    void getItem() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));

        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemWithBookingResponceDto);

        mockMvc.perform(get("/items/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Инструмент")))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    @SneakyThrows
    void getAllItem() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));
        List<ItemWithBookingResponceDto> itemWithBookingResponceDtos = List.of(itemWithBookingResponceDto);

        when(itemService.getAllItemsByUserId(anyLong())).thenReturn(itemWithBookingResponceDtos);

        mockMvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", is(hasSize(1))));
    }

    @Test
    @SneakyThrows
    void createItem() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));
        ItemRequestDto itemRequest = new ItemRequestDto(1L, "Дрель", "Инструмент", true, new User(), 0);

        when(itemService.addItem(anyLong(), any(ItemRequestDto.class))).thenReturn(itemResponceDto);

        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель")))
                .andExpect(jsonPath("$.description", is("Инструмент")))
                .andExpect(jsonPath("$.available", is(true)))
                .andExpect(jsonPath("$.owner.id", is(1)))
                .andExpect(jsonPath("$.requestId", is(1)))
                .andExpect(jsonPath("$.comments[0].id", is(1)));
    }

    @Test
    @SneakyThrows
    void updateItem() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Cупер Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));
        ItemRequestDto itemRequest = new ItemRequestDto(1L, "Cупер Дрель", "Инструмент", true, new User(), 0);

        when(itemService.updateItem(anyLong(), any(ItemRequestDto.class))).thenReturn(itemResponceDto);

        mockMvc.perform(patch("/items/{id}", 1)
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(itemRequest.getName())));
    }

    @Test
    @SneakyThrows
    void searchItems() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Cупер Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));
        ItemRequestDto itemRequest = new ItemRequestDto(1L, "Cупер Дрель", "Инструмент", true, new User(), 0);
        List<ItemResponceDto> itemResponceDtos = List.of(itemResponceDto);

        when(itemService.searchItems(1L, "дрель")).thenReturn(itemResponceDtos);

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", is(hasSize(1))))
                .andExpect(jsonPath("$.[0].name", is("Cупер Дрель")));
    }

    @Test
    @SneakyThrows
    void addComment() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User user2 = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Cупер Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Comment comment1 = new Comment(1L, "Текст Коммментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        CommentRequestDto commentRequestDto = new CommentRequestDto("Текст Коммментария");
        CommentResponceDto commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        ItemResponceDto itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        ItemWithBookingResponceDto  itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), expectedItemRequest1.getId(), List.of(commentResponceDto1));
        ItemRequestDto itemRequest = new ItemRequestDto(1L, "Cупер Дрель", "Инструмент", true, new User(), 0);

        when(itemService.addComment(anyLong(), anyLong(), any(CommentRequestDto.class))).thenReturn(commentResponceDto1);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Текст Коммментария")))
                .andExpect(jsonPath("$.authorName", is("user1")))
                .andExpect(jsonPath("$.created", is("2023-05-07T10:00:00")));
    }
}
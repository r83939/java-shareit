package ru.practicum.shareit.item.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private ItemServiceImpl itemService;
    @Mock
    private ItemServiceImpl mockItemService;
    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    User user1;
    User user2;
    ItemRequest itemRequest1;
    Item item1;
    ItemResponceDto itemResponceDto;
    List<ItemResponceDto> itemResponceDtos;
    ItemRequestDto itemRequest;
    ItemWithBookingResponceDto itemWithBookingResponceDto;
    List<ItemWithBookingResponceDto> itemWithBookingResponceDtos;
    Booking booking1;
    BookingRequestDto bookingRequestDto;
    BookingResponceDto bookingResponceDto;
    Comment comment1;
    CommentRequestDto commentRequestDto;
    CommentResponceDto commentResponceDto1;

    List<CommentResponceDto> commentResponceDtos;

    @BeforeEach
    public void init() {

        user1 = new User(1L, "user1", "user1@mail.ru");
        user2 = new User(2L, "user1", "user1@mail.ru");
        itemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user2, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        item1 = new Item(1L, "Дрель", "Инструмент", true, user1, itemRequest1);
        comment1 = new Comment(1L, "Текст Комментария", user1, item1,
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        bookingRequestDto = new BookingRequestDto(
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1.getId(), user2.getId());
        bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);
        commentRequestDto = new CommentRequestDto("Текст Комментария");
        commentResponceDto1 = new CommentResponceDto(1L, comment1.getText(), comment1.getUser().getName(), comment1.getCreated());
        commentResponceDtos = List.of(commentResponceDto1);
        itemResponceDto = ItemMapper.toItemResponceDto(item1, List.of(commentResponceDto1));
        itemResponceDtos = List.of(itemResponceDto);
        itemWithBookingResponceDto = new ItemWithBookingResponceDto(1L, "Дрель", "Инструмент", true,
                user1, SpecialBookingDto.builder().build(), SpecialBookingDto.builder().build(), itemRequest1.getId(), List.of(commentResponceDto1));
        itemWithBookingResponceDtos = List.of(itemWithBookingResponceDto);
        itemRequest = new ItemRequestDto(1L, "Cупер Дрель", "Инструмент", true, new User(), 0);
    }

    @Test
    @SneakyThrows
    void getItemById() {

        when(mockItemService.getItemById(any(Long.class), any(Long.class))).thenReturn(itemWithBookingResponceDto);

        ItemWithBookingResponceDto result = mockItemService.getItemById(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId() {

        when(mockItemService.getAllItemsByUserId(any(Long.class))).thenReturn(itemWithBookingResponceDtos);

        List<ItemWithBookingResponceDto> result = mockItemService.getAllItemsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @SneakyThrows
    void addItem() {

        when(mockItemService.addItem(1L, itemRequest)).thenReturn(itemResponceDto);

        ItemResponceDto result = mockItemService.addItem(1L, itemRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Дрель", result.getName());
        assertEquals("Инструмент", result.getDescription());
    }

    @Test
    @SneakyThrows
    void updateItem() {

        when(mockItemService.updateItem(1L, itemRequest)).thenReturn(itemResponceDto);

        ItemResponceDto result = mockItemService.updateItem(1L, itemRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Дрель", result.getName());
        assertEquals("Инструмент", result.getDescription());
    }

    @Test
    @SneakyThrows
    void searchItems() {

        when(mockItemService.searchItems(1L, "перфор")).thenReturn(itemResponceDtos);

        List<ItemResponceDto> result = mockItemService.searchItems(1L, "перфор");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Дрель", result.get(0).getName());
        assertEquals("Инструмент", result.get(0).getDescription());
    }

    @Test
    @SneakyThrows
    void getCommentResponceDtos() {

        when(mockItemService.getCommentResponceDtos(1L)).thenReturn(commentResponceDtos);

        List<CommentResponceDto> result = mockItemService.getCommentResponceDtos(1L);

        assertNotNull(result);
        assertEquals(1L, result.size());
    }

    @Test
    @SneakyThrows
    void addComment() {

        when(mockItemService.addComment(1L, 1L, commentRequestDto)).thenReturn(commentResponceDto1);

        CommentResponceDto result = mockItemService.addComment(1L, 1L, commentRequestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Текст Комментария", result.getText());
    }
}
package ru.practicum.shareit.item.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;
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
    Booking booking2;
    BookingRequestDto bookingRequestDto;
    BookingResponceDto bookingResponceDto;
    Comment comment1;
    CommentRequestDto commentRequestDto;
    CommentResponceDto commentResponceDto1;
    List<Comment> comments;
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
        comments = List.of(comment1);
        booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.APPROVED);
        booking2 = new Booking(2L,
                LocalDateTime.parse("2023-03-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-03-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
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

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(bookingRepository.getLastBookingByItemId(1L)).thenReturn(booking1);
        when(bookingRepository.getNextBookingByItemId(1L)).thenReturn(booking1);
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment1));

        ItemWithBookingResponceDto result = itemService.getItemById(1L, 1L);

        Mockito.verify(itemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository).findAllByItemId(anyLong());
        Mockito.verify(bookingRepository).getLastBookingByItemId(anyLong());
        Mockito.verify(bookingRepository).getNextBookingByItemId(anyLong());
    }

    @Test
    @SneakyThrows
    void getAllItemsByUserId() {

        when(itemRepository.findAllByOwner(1L)).thenReturn(List.of(item1));
        when(bookingRepository.getLastBookingByItemId(1L)).thenReturn(booking2);
        when(bookingRepository.getNextBookingByItemId(1L)).thenReturn(booking1);
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment1));

        itemService.getAllItemsByUserId(1L);

        Mockito.verify(itemRepository, Mockito.times(1)).findAllByOwner(1L);
        Mockito.verify(commentRepository).findAllByItemId(anyLong());
        Mockito.verify(bookingRepository).getLastBookingByItemId(anyLong());
        Mockito.verify(bookingRepository).getNextBookingByItemId(anyLong());
    }

    @Test
    @SneakyThrows
    void addItem() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest1));
        when(itemRepository.save(Mockito.any())).thenReturn(item1);

        itemService.addItem(1, itemRequest);

        Mockito.verify(itemRepository).save(Mockito.any());
    }

    @Test
    @SneakyThrows
    void updateItem() {

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment1));
        when(itemRepository.save(Mockito.any())).thenReturn(item1);

        itemService.updateItem(1, itemRequest);

        Mockito.verify(itemRepository).save(Mockito.any());
    }

    @Test
    @SneakyThrows
    void searchItems() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(itemRepository.search(anyString(), anyBoolean())).thenReturn(anyList());

        itemService.searchItems(1L, "Инструмент");

        Mockito.verify(itemRepository, Mockito.times(1)).search(anyString(), anyBoolean());

        Mockito.verify(itemRepository).search(anyString(), anyBoolean());


    }

    @Test
    @SneakyThrows
    void getCommentResponceDtos() {
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment1));

        itemService.getCommentResponceDtos(1L);

        Mockito.verify(commentRepository, Mockito.times(1)).findAllByItemId(1L);
        Mockito.verify(commentRepository).findAllByItemId(1L);


    }

    @Test
    @SneakyThrows
    void addComment() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.existsByBookerAndItem(1L, 1L)).thenReturn(1);
        when(commentRepository.save(Mockito.any())).thenReturn(comment1);

        itemService.addComment(1L, 1L, commentRequestDto);

        Mockito.verify(commentRepository).save(any(Comment.class));
    }
}
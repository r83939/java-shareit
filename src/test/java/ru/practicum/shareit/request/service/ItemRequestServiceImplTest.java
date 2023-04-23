package ru.practicum.shareit.request.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
    @Mock
    private ItemRequestServiceImpl mockItemRequestService;
    @InjectMocks
    private UserServiceImpl userService;
    @Captor
    private ArgumentCaptor<ItemRequest> itemRequestArgumentCaptor;

    User user1;
    User user2;
    User expectedUser;
    ItemRequest itemRequest1;
    List<ItemRequest> itemRequests;
    Item item1;
    ItemResponceDto itemResponceDto;
    List<ItemResponceDto> itemResponceDtos;
    ItemRequestDto itemRequest;
    ItemWithBookingResponceDto itemWithBookingResponceDto;
    OwnItemRequestResponceDto itemRequestResponceDto;
    List<ItemDto> itemDtos;
    List<ItemWithBookingResponceDto> itemWithBookingResponceDtos;
    List<OwnItemRequestResponceDto> ownItemRequestResponceDtos;
    Booking booking1;
    BookingRequestDto bookingRequestDto;
    BookingResponceDto bookingResponceDto;
    Comment comment1;
    CommentRequestDto commentRequestDto;
    CommentResponceDto commentResponceDto1;

    List<CommentResponceDto> commentResponceDtos;

    ItemRequestRequestDto addRequestDto;
    ItemRequest saveItemRequest;
    ItemRequest expectedItemRequest;

    @BeforeEach
    public void init() {
        long requestId = 1;
        long userId = 2;
        user1 = new User(1L, "user1", "user1@mail.ru");
        user2 = new User(1L, "user1", "user1@mail.ru");
        expectedUser = new User(userId, "user1", "user1@mail.ru");
        addRequestDto = new ItemRequestRequestDto();
        addRequestDto.setDescription("Запрос вещи");
        saveItemRequest = new ItemRequest(requestId,"Запрос вещи", new User(userId, "user2", "user2@mail.ru"),
                LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        itemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user2, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        itemRequests = List.of(itemRequest1);
        item1 = new Item(1L, "Перфоратор", "Классный Инструмент", true, user1, itemRequest1);
        itemDtos = List.of(new ItemDto(1L,"Перфоратор", "Классный Инструмент", true, 1L));
        itemRequestResponceDto = ItemRequestMapper.toOwnItemRequestResponceDto(itemRequest1, itemDtos);
        ownItemRequestResponceDtos = List.of(itemRequestResponceDto);
        expectedItemRequest = new ItemRequest(requestId,"Запрос вещи", new User(2L, "user2", "user2@mail.ru"),
                LocalDateTime.parse("2023-05-07 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    @SneakyThrows
    void addItemRequest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.save(any())).thenReturn(saveItemRequest);

        itemRequestService.addItemRequest(2L, addRequestDto);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    @SneakyThrows
    void addItemRequest_whenUserFail_returnNotFoundException() {

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> itemRequestService.addItemRequest(10L, addRequestDto));
    }

    @Test
    @SneakyThrows
    void getItemRequestsWithoutPagination() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.getAllNotOwnRequests(2L)).thenReturn(itemRequests);

        itemRequestService.getItemRequests(2L, null, null);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRequestRepository).getAllNotOwnRequests(2L);
    }

    @Test
    @SneakyThrows
    void getItemRequestsWithPagination() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.getAllNotOwnRequestsWithPagination(2L, 0, 2)).thenReturn(itemRequests);

        itemRequestService.getItemRequests(2L, 0, 10);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRequestRepository).getAllNotOwnRequestsWithPagination(2L, 0, 10);
    }

    @Test
    @SneakyThrows
    void getItemRequests_whenUserFail() {

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequests(10L, 0, 10));
    }

    @Test
    @SneakyThrows
    void getItemRequests_whenIncorrectPagination() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestService.getItemRequests(1L, 0, 1)).thenThrow(RuntimeException.class);

        assertThrows(InvalidParameterException.class, () -> itemRequestService.getItemRequests(10L, -1, -1));
    }

    @Test
    @SneakyThrows
    void getOwnItemRequests() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.getAllByRequesterIdOrderByCreatedDesc(2L)).thenReturn(itemRequests);

        itemRequestService.getOwnItemRequests(1L);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRequestRepository).getAllByRequesterIdOrderByCreatedDesc(1L);
    }

    @Test
    @SneakyThrows
    void getOwnItemRequests_whenUserFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getOwnItemRequests(1L));
    }

    @Test
    @SneakyThrows
    void getItemRequest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(expectedItemRequest));

        itemRequestService.getItemRequest(2L, 1L);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRequestRepository).findById(1L);

    }

    @Test
    @SneakyThrows
    void getItemRequest_whenUserFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequest(1L, 1L));
    }

    @Test
    @SneakyThrows
    void getItemRequest_whenItemRequestFail() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemRequestService.getItemRequest(1L, 1L));
    }
}
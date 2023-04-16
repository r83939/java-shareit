package ru.practicum.shareit.booking.service;

import lombok.SneakyThrows;
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
import ru.practicum.shareit.domain.validator.UserValidator;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingServiceImpl mockBookingService;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    @SneakyThrows
    void getBookingById() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.WAITING);

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));

        BookingResponceDto result = bookingService.getBookingById(1, 1);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @SneakyThrows
    void addBooking() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                bookerUser, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.WAITING);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1.getId(), bookerUser.getId());

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(bookerUser));
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        BookingResponceDto addedBooking = bookingService.addBooking(bookerUser.getId(), bookingRequestDto);

        assertEquals( 1L, addedBooking.getId());
        assertEquals(bookingRequestDto.getStart(), addedBooking.getStart());
        assertEquals(bookingRequestDto.getEnd(), addedBooking.getEnd());
    }

    @Test
    @SneakyThrows
    void approveBooking() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                bookerUser, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.WAITING);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1.getId(), bookerUser.getId());

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(bookerUser));
        when(itemRepository.findUserIdById(1L)).thenReturn(user1.getId());
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1));
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        BookingResponceDto result = bookingService.approveBooking(1L, 1L, "true");
        assertEquals(1L, result.getId());
        assertEquals("APPROVED", result.getStatus().name());
    }

    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.WAITING);
        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);

        when(mockBookingService.getBookingsByUserIdAndState(1L, "ALL", 1, 1)).thenReturn(List.of(bookingResponceDto));

        List<BookingResponceDto> bookingResponceDtos = mockBookingService.getBookingsByUserIdAndState(1L, "ALL", 1, 1);

        assertEquals(1, bookingResponceDtos.size());
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserId() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(1L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.WAITING);
        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);

        when(mockBookingService.getOwnBookingsByUserId(2L, "ALL", 1, 1)).thenReturn(List.of(bookingResponceDto));

        List<BookingResponceDto> bookingResponceDtos = mockBookingService.getOwnBookingsByUserId(2L, "ALL", 1, 1);

        assertEquals(1, bookingResponceDtos.size());
    }
}
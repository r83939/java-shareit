package ru.practicum.shareit.booking.service;

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
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.exception.InvalidStateBookingException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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

    public String str;

    User user1;
    User user2;
    ItemRequest expectedItemRequest1;
    Item item1;
    Item item2;
    Booking booking1;
    Booking booking2;
    Booking booking3;
    List<Booking> bookings;
    BookingRequestDto bookingRequestDto;
    BookingResponceDto bookingResponceDto;

    @BeforeEach
    public void init() {

        user1 = new User(1L, "user1", "user1@mail.ru");
        user2 = new User(2L, "user1", "user1@mail.ru");
        expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user2, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        item2 = new Item(2L, "Дрель", "Инструмент", true, user2, expectedItemRequest1);
        booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.WAITING);
        booking2 = new Booking(2L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                user2,
                Status.APPROVED);
        booking3 = new Booking(3L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item2,
                user2,
                Status.APPROVED);
        bookings = List.of(booking1, booking2);
        bookingRequestDto = new BookingRequestDto(
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1.getId(), user2.getId());

        bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);
    }

    @Test
    @SneakyThrows
    void getBookingById() {

        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));

        bookingService.getBookingById(1, 1);

        Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookingRepository).findById(anyLong());
    }

    @Test
    @SneakyThrows
    void getBookingById_whenNoBooking() {

        when(bookingRepository.findById(10L)).thenReturn(Optional.empty()).thenThrow(RuntimeException.class);

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 10L));
    }

    @Test
    @SneakyThrows
    void getBookingById_whenNotOwnBooking() {

        when(bookingRepository.findById(2L)).thenReturn(Optional.ofNullable(booking2));

        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(6L, 2L));
    }

    @Test
    @SneakyThrows
    void addBooking() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(item2));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        bookingService.addBooking(1L, bookingRequestDto);

        Mockito.verify(userRepository).findById(anyLong());
        Mockito.verify(itemRepository).findById(anyLong());
        Mockito.verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    @SneakyThrows
    void addBooking_whenUserFail() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(10L, bookingRequestDto));
    }

    @Test
    @SneakyThrows
    void addBooking_whenNoItem() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(2L)).thenReturn(Optional.empty()).thenThrow(RuntimeException.class);
        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(1L, bookingRequestDto));
    }

    @Test
    @SneakyThrows
    void addBooking_whenOwnItem() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1)).thenThrow(RuntimeException.class);
        assertThrows(EntityNotFoundException.class, () -> bookingService.addBooking(1L, bookingRequestDto));
    }

    @Test
    @SneakyThrows
    void addBooking_whenItemNotAvalaible() {
        item1.setAvailable(false);
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1)).thenThrow(RuntimeException.class);
        assertThrows(InvalidParameterException.class, () -> bookingService.addBooking(1L, bookingRequestDto));
    }

    @Test
    @SneakyThrows
    void addBooking_whenStartLaterEnd() {
        bookingRequestDto.setStart(LocalDateTime.parse("2023-06-04 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1)).thenThrow(RuntimeException.class);
        assertThrows(InvalidParameterException.class, () -> bookingService.addBooking(1L, bookingRequestDto));
    }

    @Test
    @SneakyThrows
    void addBooking_whenStartEqualEnd() {
        bookingRequestDto.setStart(LocalDateTime.parse("2023-06-04 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        bookingRequestDto.setEnd(LocalDateTime.parse("2023-06-04 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item1)).thenThrow(RuntimeException.class);
        assertThrows(InvalidParameterException.class, () -> bookingService.addBooking(1L, bookingRequestDto));
    }



    @Test
    @SneakyThrows
    void approveBooking() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findUserIdById(1L)).thenReturn(user1.getId());
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        BookingResponceDto result = bookingService.approveBooking(1L, 1L, "true");

        assertEquals(1L, result.getId());
        assertEquals("APPROVED", result.getStatus().name());
        Mockito.verify(bookingRepository).save(any(Booking.class));

    }

    @Test
    @SneakyThrows
    void approveBooking_wnenApprovedFalse() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findUserIdById(1L)).thenReturn(user1.getId());
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        BookingResponceDto result = bookingService.approveBooking(1L, 1L, "false");

        assertEquals(1L, result.getId());
        assertEquals("REJECTED", result.getStatus().name());
        Mockito.verify(bookingRepository).save(any(Booking.class));

    }
    @Test
    @SneakyThrows
    void approveBooking_wnenApprovedError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findUserIdById(1L)).thenReturn(user1.getId());
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));

        assertThrows(InvalidParameterException.class, () -> bookingService.approveBooking(1L, 1L, "error"));
    }

    @Test
    @SneakyThrows
    void approveBooking_whenUserFail() {

        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.approveBooking(10L, 1L, "true"));
   }

    @Test
    @SneakyThrows
    void approveBooking_whenNoBooking() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.approveBooking(1L, 1L, "true"));
    }

    @Test
    @SneakyThrows
    void approveBooking_whenStatusApproved() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));
        when(itemRepository.findUserIdById(1L)).thenReturn(user1.getId());
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking2);

        bookingService.approveBooking(1L, 2L, "true");

        assertThrows(InvalidParameterException.class, () -> bookingService.approveBooking(1L, 2L, "true"));
    }

    @Test
    @SneakyThrows
    void approveBooking_whenNotOwenBooking() {
        item1.setOwner(user2);
        booking1.setBooker(user2);
        when(userRepository.findById(2L)).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(booking1));

        assertThrows(EntityNotFoundException.class, () -> bookingService.approveBooking(2L, 1L, "true"));
    }



    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Mockito.when(bookingRepository.findBookingsWithPagination(1L, 0, 2)).thenReturn(bookings);

        bookingService.getBookingsByUserIdAndState(1L, State.ALL.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);

        bookingService.getBookingsByUserIdAndState(1L, State.CURRENT.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);

        bookingService.getBookingsByUserIdAndState(1L, State.PAST.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);

        bookingService.getBookingsByUserIdAndState(1L, State.FUTURE.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);

        bookingService.getBookingsByUserIdAndState(1L, State.WAITING.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);

        bookingService.getBookingsByUserIdAndState(1L, State.REJECTED.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingsWithPagination(1L, 0, 2);
    }

    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState_whenUserFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingsByUserIdAndState(1L, "ALL", 0, 10));
    }

    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState_whenStateError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidStateBookingException.class, () -> bookingService.getBookingsByUserIdAndState(1L, "Error", 0, 10));
    }

    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState_whenFromError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidParameterException.class, () -> bookingService.getBookingsByUserIdAndState(1L, "ALL", -1, 10));
    }

    @Test
    @SneakyThrows
    void getBookingsByUserIdAndState_whenSizeError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidParameterException.class, () -> bookingService.getBookingsByUserIdAndState(1L, "ALL", 0, -1));
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserId() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        Mockito.when(bookingRepository.findBookingByOwnerIdWithPagination(1L, 0, 2)).thenReturn(bookings);;

        bookingService.getOwnBookingsByUserId(1L, State.ALL.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        bookingService.getOwnBookingsByUserId(1L, State.CURRENT.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        bookingService.getOwnBookingsByUserId(1L, State.PAST.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        bookingService.getOwnBookingsByUserId(1L, State.FUTURE.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        bookingService.getOwnBookingsByUserId(1L, State.WAITING.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        bookingService.getOwnBookingsByUserId(1L, State.REJECTED.name(), 0, 2);
        Mockito.verify(bookingRepository).findBookingByOwnerIdWithPagination(1L, 0, 2);

        assertThrows(InvalidStateBookingException.class, () -> bookingService.getOwnBookingsByUserId(1L, null, 0, 10));

        assertThrows(InvalidParameterException.class, () -> bookingService.getOwnBookingsByUserId(1L, "ALL",-1, -1));
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserId_whenUserFail() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookingService.getOwnBookingsByUserId(1L, "ALL", 0, 10));

    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserIdAndState_whenStateError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidStateBookingException.class, () -> bookingService.getOwnBookingsByUserId(1L, "Error", 0, 10));
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserIdAndState_whenFromError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidParameterException.class, () -> bookingService.getOwnBookingsByUserId(1L, "ALL", -1, 10));
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserIdAndState_whenSizeError() {

        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user1));

        assertThrows(InvalidParameterException.class, () -> bookingService.getOwnBookingsByUserId(1L, "ALL", 0, -1));
    }
}
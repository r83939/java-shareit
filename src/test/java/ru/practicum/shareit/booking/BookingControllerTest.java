package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingServiceImpl bookingService;
    @MockBean
    private BookingRepository bookingRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    @Test
    @SneakyThrows
    void getBooking() {
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

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingResponceDto);
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponceDto)));
    }

    @Test
    @SneakyThrows
    void createBooking() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
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
        BookingRequestDto bookingRequestDto = new BookingRequestDto(booking1.getStart(),booking1.getEnd(), booking1.getItem().getId(), booking1.getBooker().getId());

        when(bookingService.addBooking(anyLong(), Mockito.any(BookingRequestDto.class))).thenReturn(bookingResponceDto);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is("2023-06-01T10:00:00")))
                .andExpect(jsonPath("$.end", is("2023-06-03T10:00:00")))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(2)))
                .andExpect(jsonPath("$.status", is("WAITING")));

    }

    @Test
    @SneakyThrows
    void approveBooking() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.APPROVED);
        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(booking1.getStart(),booking1.getEnd(), booking1.getItem().getId(), booking1.getBooker().getId());

        when(bookingService.approveBooking(anyLong(), anyLong(), Mockito.any(String.class))).thenReturn(bookingResponceDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .content(objectMapper.writeValueAsString(bookingRequestDto))
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.start", is("2023-06-01T10:00:00")))
                .andExpect(jsonPath("$.end", is("2023-06-03T10:00:00")))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(2)))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    @SneakyThrows
    void getBookings() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.APPROVED);
        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);
        List<BookingResponceDto> bookingResponceDtos =  List.of(bookingResponceDto);

        when(bookingService.getBookingsByUserIdAndState(anyLong(), Mockito.any(String.class),  anyInt(), anyInt())).thenReturn(bookingResponceDtos);

        mockMvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", is(hasSize(1))));
    }

    @Test
    @SneakyThrows
    void getOwnBookingsByUserId() {
        User user1 = new User(1L, "user1", "user1@mail.ru");
        User bookerUser = new User(2L, "user1", "user1@mail.ru");
        ItemRequest expectedItemRequest1 = new ItemRequest(1L,"Запрос вещи1",
                user1, LocalDateTime.parse("2023-04-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        Item item1 = new Item(1L, "Дрель", "Инструмент", true, user1, expectedItemRequest1);
        Booking booking1 = new Booking(1L,
                LocalDateTime.parse("2023-06-01 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse("2023-06-03 10:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                item1,
                bookerUser,
                Status.APPROVED);
        BookingResponceDto bookingResponceDto = BookingMapper.toBookingResponceDto(booking1);
        BookingRequestDto bookingRequestDto = new BookingRequestDto(booking1.getStart(),booking1.getEnd(), booking1.getItem().getId(), booking1.getBooker().getId());
        List<BookingResponceDto> bookingResponceDtos =  List.of(bookingResponceDto);

        when(bookingService.getOwnBookingsByUserId(anyLong(), Mockito.any(String.class),  anyInt(), anyInt())).thenReturn(bookingResponceDtos);

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", is(hasSize(1))));
    }
}
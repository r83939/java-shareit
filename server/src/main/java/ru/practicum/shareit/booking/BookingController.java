package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.exception.InvalidStateBookingException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private static final String DEFAULT_FROM = "0";
    private static final String DEFAULT_SIZE = "10";
    private static final String DEFAULT_STATE = "ALL";
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{bookingId}")
    public BookingResponceDto getBooking(@RequestHeader(value = USER_ID, required = true) Long userId,
                                         @PathVariable long bookingId) throws EntityNotFoundException {
        return bookingService.getBookingById(userId, bookingId);
    }

    @PostMapping()
    public BookingResponceDto addBooking(@RequestHeader(value = USER_ID, required = true) Long userId,
                                            @Valid @RequestBody BookingRequestDto booking) throws EntityNotFoundException, InvalidParameterException {

        return bookingService.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponceDto approveBooking(@RequestHeader(value = USER_ID, required = true) Long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam(value = "approved", required = true) String approved) throws InvalidParameterException, EntityNotFoundException {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping()
    public List<BookingResponceDto> getBookings(@RequestHeader(value = USER_ID) Long userId,
                                                @RequestParam(value = "state", defaultValue = DEFAULT_STATE) String state,
                                                @RequestParam(value = "from", defaultValue = DEFAULT_FROM) Integer from,
                                                @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingService.getBookingsByUserIdAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponceDto> getOwnBookingsByUserId(@RequestHeader(value = USER_ID) Long userId,
                                                           @RequestParam(value = "state", defaultValue = DEFAULT_STATE) String state,
                                                           @RequestParam(value = "from", defaultValue = DEFAULT_FROM) Integer from,
                                                           @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingService.getOwnBookingsByUserId(userId, state, from, size);
    }
}
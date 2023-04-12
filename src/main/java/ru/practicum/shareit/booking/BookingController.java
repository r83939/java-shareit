package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.exception.InvalidStateBookingException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    static final String USERID = "X-Sharer-User-Id";
    static final String DEFAULT_FROM = "0";
    static final String DEFAULT_SIZE = "10";
    static final String DEFAULT_STATE = "ALL";
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public BookingController(BookingServiceImpl bookingServiceImpl) {
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @GetMapping("/{bookingId}")
    public BookingResponceDto getBooking(@RequestHeader(value = USERID, required = true) Long userId,
                                         @PathVariable long bookingId) throws EntityNotFoundException {
        return bookingServiceImpl.getBookingById(userId, bookingId);
    }

    @PostMapping()
    public BookingResponceDto createBooking(@RequestHeader(value = USERID, required = true) Long userId,
                                            @Valid @RequestBody BookingRequestDto booking) throws EntityNotFoundException, InvalidParameterException {

        return bookingServiceImpl.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponceDto approveBooking(@RequestHeader(value = USERID, required = true) Long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam(value = "approved", required = true) String approved) throws InvalidParameterException, EntityNotFoundException {
        return bookingServiceImpl.approveBooking(userId, bookingId, approved);
    }

    @GetMapping()
    public List<BookingResponceDto> getBookings(@RequestHeader(value = USERID) Long userId,
                                                @RequestParam(value = "state", defaultValue = DEFAULT_STATE) String state,
                                                @RequestParam(value = "from", defaultValue = DEFAULT_FROM) Integer from,
                                                @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingServiceImpl.getBookingsByUserIdAndState(userId, state, from, size);
    }

    @GetMapping("/")
    public List<BookingResponceDto> getBookings1(@RequestHeader(value = USERID, required = true) Long userId,
                                                @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                @RequestParam(value = "from", required = false) Integer from,
                                                @RequestParam(value = "size", required = false) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingServiceImpl.getBookingsByUserIdAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponceDto> getOwnBookingsByUserId(@RequestHeader(value = USERID) Long userId,
                                                           @RequestParam(value = "state", defaultValue = DEFAULT_STATE) String state,
                                                           @RequestParam(value = "from", defaultValue = DEFAULT_FROM) Integer from,
                                                           @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingServiceImpl.getOwnBookingsByUserId(userId, state, from, size);
    }

    @GetMapping("/owner/1")
    public List<BookingResponceDto> getOwnBookingsByUserId1(@RequestHeader(value = USERID, required = true) Long userId,
                                                           @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
                                                           @RequestParam(value = "from", required = false) Integer from,
                                                           @RequestParam(value = "size", required = false) Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        return bookingServiceImpl.getOwnBookingsByUserId(userId, state, from, size);
    }
}

package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public BookingController(BookingServiceImpl bookingServiceImpl) {
        this.bookingServiceImpl = bookingServiceImpl;
    }

    @GetMapping("/{bookingId}")
    public BookingResponceDto getBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                                         @PathVariable long bookingId) {
        return bookingServiceImpl.getBookingById(userId, bookingId);
    }

    @PostMapping()
    public BookingResponceDto createBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                                            @Valid @RequestBody BookingRequestDto booking) throws EntityNotFoundException, InvalidParameterException {

        return bookingServiceImpl.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}?approved={approved}")
    public BookingResponceDto approveBooking(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam(value = "approved", required = true) String approved) throws InvalidParameterException, EntityNotFoundException {
        return bookingServiceImpl.approveBooking(userId, bookingId ,approved );
    }

    @GetMapping()
    public List<BookingResponceDto> getBookingsByUserId(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                                                       @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        return bookingServiceImpl.getBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponceDto> getOwnBookingsByUserId(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                                                          @RequestParam(value = "state", required = false, defaultValue = "ALL") String state) {
        return bookingServiceImpl.getOwnBookingsByUserId(userId, state);
    }






}

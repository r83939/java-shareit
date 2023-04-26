package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.exception.InvalidStateBookingException;

import java.util.List;

public interface BookingService {
    BookingResponceDto getBookingById(long userId, long bookingId) throws EntityNotFoundException;

    BookingResponceDto addBooking(Long userId, BookingRequestDto bookingRequestDto) throws EntityNotFoundException, InvalidParameterException;

    BookingResponceDto approveBooking(Long userId, long bookingId, String approved) throws EntityNotFoundException, InvalidParameterException;

    List<BookingResponceDto> getBookingsByUserIdAndState(Long userId, String state, Integer from, Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException;

    List<BookingResponceDto> getOwnBookingsByUserId(Long userId, String state, Integer from, Integer size) throws EntityNotFoundException;
}

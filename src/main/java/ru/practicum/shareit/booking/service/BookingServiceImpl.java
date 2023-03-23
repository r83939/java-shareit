package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponceDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookingServiceImpl {
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;


    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepo, UserRepository userRepo, ItemRepository itemRepo) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
    }

    public BookingResponceDto getBookingById(long id, long bookingId) {
        return null;
    }


    public BookingResponceDto addBooking(Long userId, BookingRequestDto bookingRequestDto) throws EntityNotFoundException, InvalidParameterException {

        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Optional<Item> item = itemRepo.findById(bookingRequestDto.getItemId());
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Нет позиции с id: " + bookingRequestDto.getItemId());
        }
        if (!item.get().getAvailable()) {
            throw new InvalidParameterException("Позиция с id: " + item.get().getId() + " недоступна для бронирования.");
        }
        if (bookingRequestDto.getEnd().before(bookingRequestDto.getStart())) {
            throw new InvalidParameterException("Неверно указано время бронирования");
        }
        Booking booking = new Booking();
        booking.setItem(item.get());
        booking.setBooker(user.get());
        return BookingMapper.toBookingResponceDto(bookingRepo.save(booking));
    }

    public BookingResponceDto updateBooking(Long userId, BookingRequestDto booking) {
        return null;
    }

    public BookingResponceDto approveBooking(Long userId, long bookingId, String approved) throws EntityNotFoundException, InvalidParameterException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Optional<Booking> booking = bookingRepo.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Нет бронирования с id: " + bookingId);
        }
        if (itemRepo.findUserIdById(booking.get().getItem().getId()) != userId) {
           throw new InvalidParameterException("Вы не можете изменить статус этого бронирования");
        }
        if (approved.equals("true")) {
            booking.get().setStatus(Status.APPROVED);
            return BookingMapper.toBookingResponceDto(bookingRepo.save(booking.get()));
        }
        if (approved.equals("false")) {
            booking.get().setStatus(Status.REJECTED);
            return BookingMapper.toBookingResponceDto(bookingRepo.save(booking.get()));
        }
        throw new InvalidParameterException("Указан неверный параметр: approved");
    }

    public List<BookingResponceDto> getBookingsByUserId(Long userId, String state) {
        return null;
    }

    public List<BookingResponceDto> getOwnBookingsByUserId(Long userId, String state) {
        return null;
    }


}

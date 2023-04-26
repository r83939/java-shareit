package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class BookingServiceImpl {
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepo,
                              UserRepository userRepo,
                              ItemRepository itemRepo,
                              BookingMapper bookingMapper) {
        this.bookingRepo = bookingRepo;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.bookingMapper = bookingMapper;
    }

    public BookingResponceDto getBookingById(long userId, long bookingId) throws EntityNotFoundException {

        Optional<Booking> booking = bookingRepo.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Нет бронирования с id: " + bookingId);
        }
        if ((booking.get().getItem().getOwner().getId() == userId) || (booking.get().getBooker().getId() == userId)) {
            return BookingMapper.toBookingResponceDto(booking.get());
        }
        throw new EntityNotFoundException("Вы не можете просматривать это бронирование");
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
        if (item.get().getOwner().getId() == user.get().getId()) {
            throw new EntityNotFoundException("Пользователь не может бронировать собственную вещь");
        }
        if (!item.get().getAvailable()) {
            throw new InvalidParameterException("Позиция с id: " + item.get().getId() + " недоступна для бронирования.");
        }
        if (bookingRequestDto.getEnd().isBefore(bookingRequestDto.getStart())) {
            throw new InvalidParameterException("Неверно указано время бронирования");
        }
        if (bookingRequestDto.getEnd().equals(bookingRequestDto.getStart())) {
            throw new InvalidParameterException("Время окончания бронирования не должно совпадать с временем начала.");
        }

        Booking booking = new Booking();
        booking.setItem(item.get());
        booking.setBooker(user.get());
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        return BookingMapper.toBookingResponceDto(bookingRepo.save(booking));
    }

    public BookingResponceDto approveBooking(Long userId, long bookingId, String approved) throws EntityNotFoundException, InvalidParameterException {

        boolean isApproved = ("true").equals(approved); // сюда придут только "true" или "false", проверка на другие варианты будет в gateway

        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Optional<Booking> booking = bookingRepo.findById(bookingId);
        if (booking.isEmpty()) {
            throw new EntityNotFoundException("Нет бронирования с id: " + bookingId);
        }
        if (booking.get().getStatus().equals(Status.APPROVED) && approved.equals("true")) {
            throw new InvalidParameterException("Статус бронирования уже : " + approved);
        }
        if (itemRepo.findUserIdById(booking.get().getItem().getId()) != userId) {
           throw new EntityNotFoundException("Вы не можете изменить статус этого бронирования");
        }
        if (isApproved) {
            booking.get().setStatus(Status.APPROVED);
        }
        else {
            booking.get().setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingResponceDto(bookingRepo.save(booking.get()));
    }

    public List<BookingResponceDto> getBookingsByUserIdAndState(Long userId, String state, Integer from, Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        if (userRepo.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }

        List<BookingResponceDto> bookingResponceDtos = new ArrayList<>();
        switch (State.valueOf(state)) {
                case ALL:
                    bookingResponceDtos = bookingRepo.findBookingsWithPagination(userId, from, size).stream()
                        .map(b -> bookingMapper.toBookingResponceDto(b))
                        .collect(Collectors.toList());
                    break;
                case CURRENT:
                    bookingResponceDtos = bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case PAST:
                    bookingResponceDtos = bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case FUTURE:
                    bookingResponceDtos = bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case WAITING:
                    bookingResponceDtos = bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case REJECTED:
                    bookingResponceDtos = bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
            }
            return bookingResponceDtos;
    }

    public List<BookingResponceDto> getOwnBookingsByUserId(Long userId, String state, Integer from, Integer size) throws EntityNotFoundException {
        if (userRepo.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }

        List<BookingResponceDto> bookingResponceDtos = new ArrayList<>();
        switch (State.valueOf(state)) {
                case ALL:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerIdWithPagination(userId,from, size).stream()
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case CURRENT:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case PAST:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case FUTURE:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case WAITING:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
                case REJECTED:
                    bookingResponceDtos = bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                    break;
            }
        return bookingResponceDtos;
    }
}

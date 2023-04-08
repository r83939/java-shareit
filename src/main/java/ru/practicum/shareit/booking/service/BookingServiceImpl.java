package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

import java.awt.print.Pageable;
import java.time.LocalDateTime;
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
        throw new EntityNotFoundException("");
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
            throw new EntityNotFoundException("Пользователь не может бронировать собвственную вещь");
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
        if (booking.get().getStatus().equals(Status.APPROVED) && approved.equals("true")) {
            throw new InvalidParameterException("Статус бронирования уже : " + approved);
        }

        if (itemRepo.findUserIdById(booking.get().getItem().getId()) != userId) {
           throw new EntityNotFoundException("Вы не можете изменить статус этого бронирования");
        }
        if (approved.equals("true")) {
            booking.get().setStatus(Status.APPROVED);
            //return BookingMapper.toApproveBookingResponceDto(bookingRepo.save(booking.get()));
            return BookingMapper.toBookingResponceDto(bookingRepo.save(booking.get()));
        }
        if (approved.equals("false")) {
            booking.get().setStatus(Status.REJECTED);
            //return BookingMapper.toApproveBookingResponceDto(bookingRepo.save(booking.get()));
            return BookingMapper.toBookingResponceDto(bookingRepo.save(booking.get()));
        }
        throw new InvalidParameterException("Указан неверный параметр: approved");
    }

    public List<BookingResponceDto> getBookingsByUserIdAndState(Long userId, String state, Integer from, Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        if (userRepo.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (!Stream.of(State.values()).anyMatch(v -> v.name().equals(state))) {
            throw new InvalidStateBookingException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (from == null || size == null) {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                        .map(b -> bookingMapper.toBookingResponceDto(b))
                        .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                default: throw new InvalidParameterException("Неверный параметр state: " + state);
            }
        }
        else {
            if (from < 0 || size <= 0) {
                throw new InvalidParameterException("Не верно заданы значения пагинации");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepo.findBookingsWithPagination(userId, from, size).stream()
                        .map(b -> bookingMapper.toBookingResponceDto(b))
                        .collect(Collectors.toList());
//                    Page<Booking> page = bookingRepo.findBookingsByBookerId(userId,
//                            PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "start")));
//                    return page.stream()
//                            .map(p -> bookingMapper.toBookingResponceDto(p))
//                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepo.findBookingsByBookerIdOrderByStartDesc(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                default: throw new InvalidParameterException("Неверный параметр state: " + state);
            }
        }
    }


    public List<BookingResponceDto> getOwnBookingsByUserId(Long userId, String state, Integer from, Integer size) throws InvalidParameterException, EntityNotFoundException, InvalidStateBookingException {
        if (userRepo.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (!Stream.of(State.values()).anyMatch(v -> v.name().equals(state))) {
            throw new InvalidStateBookingException("Unknown state: UNSUPPORTED_STATUS");
        }

        if (from == null || size == null) {
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                default: throw new InvalidParameterException("Неверный параметр state: " + state);
            }
        }
        else {
            if (from < 0 || size <= 0) {
                throw new InvalidParameterException("Не верно заданы значения пагинации");
            }
            switch (State.valueOf(state)) {
                case ALL:
                    return bookingRepo.findBookingByOwnerIdWithPagination(userId,from, size).stream()
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case CURRENT:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case PAST:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case FUTURE:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case WAITING:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.WAITING))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                case REJECTED:
                    return bookingRepo.findBookingByOwnerId(userId).stream()
                            .filter(b -> b.getStatus().equals(Status.REJECTED))
                            .map(b -> bookingMapper.toBookingResponceDto(b))
                            .collect(Collectors.toList());
                default: throw new InvalidParameterException("Неверный параметр state: " + state);
            }
        }
    }


}

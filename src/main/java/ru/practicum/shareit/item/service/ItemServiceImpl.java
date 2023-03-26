package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl {

    private final BookingRepository bookingRepo;
    private final ItemRepository itemRepo;
    private final ItemMapper itemMapper;
    private final UserRepository userRepo;

    @Autowired
    public ItemServiceImpl(BookingRepository bookingRepo, ItemRepository itemRepo, ItemMapper itemMapper, UserRepository userRepo) {
        this.bookingRepo = bookingRepo;
        this.itemRepo = itemRepo;
        this.itemMapper = itemMapper;
        this.userRepo = userRepo;
    }

    public ItemWithBookingResponceDto getItemById(long userId, long itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Не найдено позиции с id: " + itemId);
        }
        SpecialBookingDto specialLastBooking = new SpecialBookingDto();
        SpecialBookingDto specialNextBooking = new SpecialBookingDto();
        var lastBooking = bookingRepo.getLastBookingByItemId(itemId);
        var nextBooking = (bookingRepo.getNextBookingByItemId(itemId));
        if ((lastBooking != null) && (nextBooking != null) && (item.get().getOwner().getId() == userId)) {
            specialLastBooking = itemMapper.toSpecialBookingDto(lastBooking);
            specialNextBooking = itemMapper.toSpecialBookingDto(nextBooking);
        }
        else {
            specialLastBooking = null;
            specialNextBooking = null;
        }
        return new ItemWithBookingResponceDto(
                item.get().getId(),
                item.get().getName(),
                item.get().getDescription(),
                item.get().getAvailable(),
                item.get().getOwner(),
                specialLastBooking,
                specialNextBooking,
                item.get().getRequest()
        );
    }

    public List<ItemWithBookingResponceDto> getAllItemsByUserId(long userId) {
        List<ItemWithBookingResponceDto> responceDto = new ArrayList<>();
        for ( Item item : itemRepo.findAllByOwner(userId)) {
            SpecialBookingDto specialLastBooking = new SpecialBookingDto();
            SpecialBookingDto specialNextBooking = new SpecialBookingDto();
            var lastBooking = bookingRepo.getLastBookingByItemId(item.getId());
            var nextBooking = (bookingRepo.getNextBookingByItemId(item.getId()));
            if (lastBooking != null && nextBooking != null) {
                specialLastBooking = itemMapper.toSpecialBookingDto(lastBooking);
                specialNextBooking = itemMapper.toSpecialBookingDto(nextBooking);
            }
            responceDto.add(new ItemWithBookingResponceDto(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getOwner(),
                    specialLastBooking,
                    specialNextBooking,
                    item.getRequest()
            ));
        }
        return  responceDto;
    }

    public ItemResponceDto addItem(long userId,  ItemRequestDto itemRequestDto ) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Item item = new Item();
        item.setOwner(user.get());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        return itemMapper.toItemResponceDto(itemRepo.save(item));
    }

    public ItemResponceDto updateItem(long userId, ItemRequestDto itemRequestDto) throws EntityNotFoundException, AccessDeniedException {
        Optional<Item> updateItem = itemRepo.findById(itemRequestDto.getId());
        if (updateItem.isEmpty()) {
            throw new EntityNotFoundException("Позиция с id: " + itemRequestDto.getId() + "не найдена.");
        }
        if (updateItem.get().getOwner().getId() != userId) {
            throw new AccessDeniedException("У пользователя с id: " + userId +
                    " нет прав на редактирование позиции с id: " + itemRequestDto.getId());
        }
        if (itemRequestDto.getName() != null) {
            updateItem.get().setName(itemRequestDto.getName());
        }
        if (itemRequestDto.getDescription() != null) {
            updateItem.get().setDescription(itemRequestDto.getDescription());
        }
        if (itemRequestDto.getAvailable() != null) {
            updateItem.get().setAvailable(itemRequestDto.getAvailable());
        }

        return itemMapper.toItemResponceDto(itemRepo.save(updateItem.get()));
    }

    public ItemRequestDto deleteItem(long itemId) {
        return null;
    }


    public List<ItemResponceDto> searchItems(Long userId, String text) throws EntityNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepo.search(text, true).stream()
                .map(i -> itemMapper.toItemResponceDto(i))
                .collect(Collectors.toList());
    }


}

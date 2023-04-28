package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import static java.util.stream.Collectors.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;



@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final BookingRepository bookingRepo;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemRequestRepository itemRequestRepo;

    @Autowired
    public ItemServiceImpl(BookingRepository bookingRepo, ItemRepository itemRepo, ItemMapper itemMapper, UserRepository userRepo, CommentRepository commentRepo, CommentMapper commentMapper, ItemRequestRepository itemRequestRepo) {
        this.bookingRepo = bookingRepo;
        this.itemRepo = itemRepo;
        this.itemMapper = itemMapper;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.commentMapper = commentMapper;
        this.itemRequestRepo = itemRequestRepo;
    }

    @Override
    public ItemWithBookingResponceDto getItemById(long userId, long itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Не найдено позиции с id: " + itemId);
        }
        SpecialBookingDto specialLastBooking;
        SpecialBookingDto specialNextBooking;
        var lastBooking = bookingRepo.getLastBookingByItemId(itemId, LocalDateTime.now());
        var nextBooking = bookingRepo.getNextBookingByItemId(itemId, LocalDateTime.now());
        if ((lastBooking != null) &&  (item.get().getOwner().getId() == userId) && !Status.REJECTED.equals(lastBooking.getStatus())) {
            specialLastBooking = itemMapper.toSpecialBookingDto(lastBooking);
        } else {
            specialLastBooking = null;
        }
        if ((nextBooking != null) && (item.get().getOwner().getId() == userId) && !Status.REJECTED.equals(nextBooking.getStatus())) {
            specialNextBooking = itemMapper.toSpecialBookingDto(nextBooking);
        } else {
            specialNextBooking = null;
        }
        List<CommentResponceDto> comments = commentRepo.findAllByItemId(itemId).stream()
                .map(c -> commentMapper.toCommentResponceDto(c))
                .collect(toList());

        return ItemWithBookingResponceDto.builder()
                .id(item.get().getId())
                .name(item.get().getName())
                .description(item.get().getDescription())
                .available(item.get().getAvailable())
                .owner(item.get().getOwner())
                .lastBooking(specialLastBooking)
                .nextBooking(specialNextBooking)
                .request(item.get().getRequest() != null ? item.get().getRequest().getId() : null)
                .comments(comments)
                .build();
    }

    @Override
    public List<ItemWithBookingResponceDto> getAllItemsByUserId(long userId, int from, int size)  {

        log.info("#1#");
        List<Item> items = getItemsList(userId, from, size);

        log.info("#2#" + items);
        List<Long> itemsIds = getItemsIdList(items);

        log.info("#3#" + itemsIds);
        Map<Item, List<Comment>> commentsMap = commentRepo.findAllByItemIds(itemsIds)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        log.info("#4#" + commentsMap);
        Map<Item, Booking> lastBookingsMap = bookingRepo.getLastBookingByItemIds(itemsIds, LocalDateTime.now())
                .stream()
                .collect(toMap(Booking::getItem, Function.identity(), (o, n) -> o));

        log.info("#5#" + lastBookingsMap);
        Map<Item, Booking> nextBookingsMap = bookingRepo.getNextBookingByItemIds(itemsIds, LocalDateTime.now())
                .stream()
                .collect(toMap(Booking::getItem, Function.identity(), (o, n) -> o));

        log.info("#6#" + nextBookingsMap);
        List<ItemWithBookingResponceDto> responceDto = new ArrayList<>();

        log.info("#7#" + responceDto);
        for (Item item : items) {
            responceDto.add(ItemWithBookingResponceDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .lastBooking(getSpecialLastBookingDto(item, lastBookingsMap))
                    .nextBooking(getSpecialNextBookingDto(item, nextBookingsMap))
                    .request(item.getRequest() != null ? item.getRequest().getId() : null)
                    .comments(toCommentResponceDtoList(commentsMap.getOrDefault(item, List.of())))
                    .build());
        }
        log.info("#7#" + responceDto);
        return responceDto;
    }

    private SpecialBookingDto getSpecialLastBookingDto(Item item, Map<Item, Booking> lastBookingsMap) {
        var t = lastBookingsMap.get(item);
        var g = lastBookingsMap.containsKey(item);
        if (lastBookingsMap.containsKey(item)) {
            return itemMapper.toSpecialBookingDto(lastBookingsMap.get(item));
        } else return null;

    }

    private SpecialBookingDto getSpecialNextBookingDto(Item item, Map<Item, Booking> nextBookingsMap) {
        if (nextBookingsMap.containsKey(item)) {
            return itemMapper.toSpecialBookingDto(nextBookingsMap.get(item));
        } else return null;

    }

    private List<Item> getItemsList(long userId, int from, int size) {
        List<Item> items =  itemRepo.findAllByOwnerId(userId, PageRequest.of(from, size));
        log.info("#1.1#" + items);
        return items;
    }

    private List<Long> getItemsIdList(List<Item> items) {
        return items.stream()
                .map(Item::getId)
                .collect(toList());
    }

    private List<CommentResponceDto> toCommentResponceDtoList(List<Comment> comments) {
        return comments
                .stream()
                .map(c -> ItemMapper.commentResponceDto(c))
                .collect(toList());
    }

    @Override
    public ItemResponceDto addItem(long userId, ItemRequestDto itemRequestDto) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Item item = new Item();
        item.setOwner(user.get());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        Optional<ItemRequest> itemRequest = itemRequestRepo.findById(itemRequestDto.getRequestId());
        if (itemRequest.isPresent()) {
            item.setRequest(itemRequest.get());
        }
        Item savedItem = itemRepo.save(item);
        return itemMapper.toItemResponceDto(savedItem, new ArrayList<>());
    }

    @Override
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

        Item apdatedItem = itemRepo.save(updateItem.get());
        List<CommentResponceDto> comments = commentRepo.findAllByItemId(itemRequestDto.getId()).stream()
                .map(c -> commentMapper.toCommentResponceDto(c))
                .collect(toList());

        return itemMapper.toItemResponceDto(apdatedItem, comments);
    }

    @Override
    public List<ItemResponceDto> searchItems(Long userId, String text, int from, int size) throws EntityNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepo.search(text, true, PageRequest.of(from, size)).stream()
                .map(i -> itemMapper.toItemResponceDto(i, getCommentResponceDtos(i.getId())))
                .collect(toList());
    }

    private List<CommentResponceDto> getCommentResponceDtos(long itemId) {
        return commentRepo.findAllByItemId(itemId).stream()
                .map(c -> commentMapper.toCommentResponceDto(c))
                .collect(toList());
    }

    @Override
    public CommentResponceDto addComment(long userId, long itemId, CommentRequestDto commentRequestDto) throws EntityNotFoundException, InvalidParameterException {

        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Позиция с id: " + itemId + "не найдена.");
        }
        if (bookingRepo.existsByBookerAndItem(userId, itemId) == 0) {
            throw new InvalidParameterException("Пользователь не может оставлять комментарии для вещи, которую не брал в аренду.");
        }
        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setUser(user.get());
        comment.setItem(item.get());
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentResponceDto(commentRepo.save(comment));
    }
}

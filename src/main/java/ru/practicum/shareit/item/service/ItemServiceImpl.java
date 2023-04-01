package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl {

    private final BookingRepository bookingRepo;
    private final ItemRepository itemRepo;
    private final UserRepository userRepo;
    private final CommentRepository commentRepo;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemServiceImpl(BookingRepository bookingRepo, ItemRepository itemRepo, ItemMapper itemMapper, UserRepository userRepo, CommentRepository commentRepo, CommentMapper commentMapper) {
        this.bookingRepo = bookingRepo;
        this.itemRepo = itemRepo;
        this.itemMapper = itemMapper;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
        this.commentMapper = commentMapper;
    }

    public ItemWithBookingResponceDto getItemById(long userId, long itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Не найдено позиции с id: " + itemId);
        }
        SpecialBookingDto specialLastBooking;
        SpecialBookingDto specialNextBooking;
        var lastBooking = bookingRepo.getLastBookingByItemId(itemId);
        var nextBooking = bookingRepo.getNextBookingByItemId(itemId);
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
                .collect(Collectors.toList());
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
//        return new ItemWithBookingResponceDto(
//                item.get().getId(),
//                item.get().getName(),
//                item.get().getDescription(),
//                item.get().getAvailable(),
//                item.get().getOwner(),
//                specialLastBooking,
//                specialNextBooking,
//                item.get().getRequest() != null ? item.get().getRequest().getId() : null,
//                comments
//        );
    }

    public List<ItemWithBookingResponceDto> getAllItemsByUserId(long userId) {
        List<ItemWithBookingResponceDto> responceDto = new ArrayList<>();
        for (Item item : itemRepo.findAllByOwner(userId)) {
            SpecialBookingDto specialLastBooking = SpecialBookingDto.builder().build();
            SpecialBookingDto specialNextBooking = SpecialBookingDto.builder().build();
//            SpecialBookingDto specialLastBooking = new SpecialBookingDto();
//            SpecialBookingDto specialNextBooking = new SpecialBookingDto();
            var lastBooking = bookingRepo.getLastBookingByItemId(item.getId());
            var nextBooking = (bookingRepo.getNextBookingByItemId(item.getId()));
            if (lastBooking != null && nextBooking != null) {
                specialLastBooking = itemMapper.toSpecialBookingDto(lastBooking);
                specialNextBooking = itemMapper.toSpecialBookingDto(nextBooking);
            } else {
                specialLastBooking = null;
                specialNextBooking = null;
            }
            List<CommentResponceDto> comments = commentRepo.findAllByItemId(item.getId()).stream()
                    .map(c -> commentMapper.toCommentResponceDto(c))
                    .collect(Collectors.toList());
            responceDto.add(ItemWithBookingResponceDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner())
                    .lastBooking(specialLastBooking)
                    .nextBooking(specialNextBooking)
                    .request(item.getRequest() != null ? item.getRequest().getId() : null)
                    .comments(comments)
                    .build());

//            responceDto.add(new ItemWithBookingResponceDto(
//                    item.getId(),
//                    item.getName(),
//                    item.getDescription(),
//                    item.getAvailable(),
//                    item.getOwner(),
//                    specialLastBooking,
//                    specialNextBooking,
//                    item.getRequest() != null ? item.getRequest().getId() : null,
//                    comments
//            ));
        }
        return  responceDto;
    }

    public ItemResponceDto addItem(long userId,  ItemRequestDto itemRequestDto) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        //List<Comment> comments = new ArrayList<>();
        Item item = new Item();
        item.setOwner(user.get());
        item.setName(itemRequestDto.getName());
        item.setDescription(itemRequestDto.getDescription());
        item.setAvailable(itemRequestDto.getAvailable());
        Item savedItem = itemRepo.save(item);
        return itemMapper.toItemResponceDto(savedItem, new ArrayList<>());
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

        Item apdatedItem = itemRepo.save(updateItem.get());
        List<CommentResponceDto> comments = commentRepo.findAllByItemId(itemRequestDto.getId()).stream()
                .map(c -> commentMapper.toCommentResponceDto(c))
                .collect(Collectors.toList());

        return itemMapper.toItemResponceDto(apdatedItem, comments);
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
                .map(i -> itemMapper.toItemResponceDto(i, getCommentResponceDtos(i.getId())))
                .collect(Collectors.toList());
    }

    List<CommentResponceDto> getCommentResponceDtos(long itemId) {
        return commentRepo.findAllByItemId(itemId).stream()
                .map(c -> commentMapper.toCommentResponceDto(c))
                .collect(Collectors.toList());
    }

    public CommentResponceDto addComment(long userId, long itemId,CommentRequestDto commentRequestDto) throws EntityNotFoundException, InvalidParameterException {

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

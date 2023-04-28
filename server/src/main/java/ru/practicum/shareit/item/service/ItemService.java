package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemWithBookingResponceDto getItemById(long userId, long itemId) throws EntityNotFoundException;

    List<ItemWithBookingResponceDto> getAllItemsByUserId(long userId, int from, int size);

    ItemResponceDto addItem(long userId, ItemRequestDto itemRequestDto) throws EntityNotFoundException;

    ItemResponceDto updateItem(long userId, ItemRequestDto itemRequestDto) throws EntityNotFoundException, AccessDeniedException;

    List<ItemResponceDto> searchItems(Long userId, String text, int from, int size) throws EntityNotFoundException;

    CommentResponceDto addComment(long userId, long itemId, CommentRequestDto commentRequestDto) throws EntityNotFoundException, InvalidParameterException;
}

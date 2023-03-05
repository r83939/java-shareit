package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(long itemId) throws EntityNotFoundException;

    List<Item> getAllItems();

    ItemDto addItem(long userId, Item item) throws EntityNotFoundException;

    ItemDto updateItem(long userId, Item item) throws EntityNotFoundException, AccessDeniedException;

    ItemDto deleteItem(long itemId);
}

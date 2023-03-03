package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item getItemById(long itemId) throws EntityNotFoundException;

    List<Item> getAllItems();

    Item addItem(long userId, Item item) throws EntityNotFoundException;

    Item updateItem(long userId, Item item) throws EntityNotFoundException, AccessDeniedException;

    Item deleteItem(long itemId);
}

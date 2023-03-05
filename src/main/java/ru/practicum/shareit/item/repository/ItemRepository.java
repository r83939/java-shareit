package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getAll();

    Item getOne(long itemId);

    Item save(Item item);

    Item delete(long itemId);

}

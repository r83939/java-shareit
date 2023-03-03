package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class ItemRepositoryImpl implements ItemRepository{

    private Map<Long, Item> items;

    private long id;

    public ItemRepositoryImpl() {
        this.items =  new HashMap<>();
        this.id = 0;
    }

    private long setId() {
        id++;
        return id;
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item getOne(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == 0) { // новая вещь
            item.setId(setId());
        }
        items.put((item.getId()), item);
        return items.get(item.getId());
    }

    @Override
    public Item delete(long itemId) {
        return null;
    }
}

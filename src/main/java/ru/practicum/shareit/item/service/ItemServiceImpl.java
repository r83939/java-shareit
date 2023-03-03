package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;
import ru.practicum.shareit.user.service.UserServiceImpl;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService{

    private final ItemRepositoryImpl itemRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    @Autowired
    public ItemServiceImpl(ItemRepositoryImpl itemRepositoryImpl, UserRepositoryImpl userRepositoryImpl) {
        this.itemRepositoryImpl = itemRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    @Override
    public Item getItemById(long itemId) throws EntityNotFoundException {
        Item item = itemRepositoryImpl.getOne(itemId);
        if (item == null) {
            throw new EntityNotFoundException("Не найдено позиции с id: " + itemId);
        }
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return null;
    }

    @Override
    public Item addItem(long userId,  Item item) throws EntityNotFoundException {
        User user = userRepositoryImpl.getOne(userId);
        if (user == null) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        item.setOwner(user);
        return itemRepositoryImpl.save(item);
    }

    @Override
    public Item updateItem(long userId, Item item) throws EntityNotFoundException, AccessDeniedException {
        Item updateItem = itemRepositoryImpl.getOne(item.getId());
        if (updateItem == null) {
            throw new EntityNotFoundException("Позиция с id: " + item.getId() + "не найдена." );
        }
        if (updateItem.getOwner().getId() != userId) {
            throw new AccessDeniedException("У пользователя с id: " + userId +
                    " нет прав на редактирование позиции с id: " + item.getId());
        }
        if (item.getName() != null ) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null ) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return itemRepositoryImpl.save(updateItem);
    }

    @Override
    public Item deleteItem(long itemId) {
        return null;
    }

    public List<Item> searchItems(Long userId, String text) throws EntityNotFoundException {
        if (!userRepositoryImpl.isUserExist(userId)) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepositoryImpl.getAll().stream()
                .filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase()) && i.getAvailable() == true)
                        || (i.getDescription().toLowerCase().contains(text.toLowerCase()) && i.getAvailable() == true))
                .collect(Collectors.toList());
    }

    public List<Item> getAllItemsByUserId(long userId) {
        return itemRepositoryImpl.getAll().stream()
                .filter(i -> i.getOwner().getId() == userId )
                .collect(Collectors.toList());
    }
}

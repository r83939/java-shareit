package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
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
    private final ItemRepository itemRepo;
    private final ItemMapper itemMapper;
    private final UserRepository userRepo;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepo, ItemMapper itemMapper, UserRepository userRepo) {
        this.itemRepo = itemRepo;
        this.itemMapper = itemMapper;
        this.userRepo = userRepo;
    }

    public ItemDto getItemById(long itemId) throws EntityNotFoundException {
        Optional<Item> item = itemRepo.findById(itemId);
        if (item.isEmpty()) {
            throw new EntityNotFoundException("Не найдено позиции с id: " + itemId);
        }
        return itemMapper.toItemDto(item.get());
    }

    public ItemDto addItem(long userId,  Item item) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        item.setOwner(userId);
        return itemMapper.toItemDto(itemRepo.save(item));
    }

    public ItemDto updateItem(long userId, Item item) throws EntityNotFoundException, AccessDeniedException {
        Optional<Item> updateItem = itemRepo.findById(item.getId());
        if (updateItem.isEmpty()) {
            throw new EntityNotFoundException("Позиция с id: " + item.getId() + "не найдена.");
        }
        if (updateItem.get().getOwner() != userId) {
            throw new AccessDeniedException("У пользователя с id: " + userId +
                    " нет прав на редактирование позиции с id: " + item.getId());
        }
        if (item.getName() != null) {
            updateItem.get().setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.get().setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.get().setAvailable(item.getAvailable());
        }

        return itemMapper.toItemDto(itemRepo.save(updateItem.get()));
    }

    public ItemDto deleteItem(long itemId) {
        return null;
    }

    /*
    public List<ItemDto> searchItems(Long userId, String text) throws EntityNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepo.findAll().stream()
                .filter(i -> (i.getName().toLowerCase().contains(text.toLowerCase()) && i.getAvailable() == true)
                        || (i.getDescription().toLowerCase().contains(text.toLowerCase()) && i.getAvailable() == true))
                .map(i -> itemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAllItemsByUserId(long userId) {
        return itemRepo.findAll().stream()
                .filter(i -> i.getOwner() == userId)
                .map(i -> itemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }
    */


    public List<ItemDto> searchItems(Long userId, String text) throws EntityNotFoundException {
        if (!userRepo.existsById(userId)) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepo.search(text, true).stream()
                .map(i -> itemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAllItemsByUserId(long userId) {
        return itemRepo.findAllByOwner(userId).stream()
                .map(i -> itemMapper.toItemDto(i))
                .collect(Collectors.toList());
    }







}

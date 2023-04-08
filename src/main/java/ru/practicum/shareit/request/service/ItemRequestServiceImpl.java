package ru.practicum.shareit.request.service;

import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemRequestServiceImpl {

    private final ItemRequestRepository itemRequestRepo;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepo;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepo, ItemRequestMapper itemRequestMapper, UserRepository userRepo) {
        this.itemRequestRepo = itemRequestRepo;
        this.itemRequestMapper = itemRequestMapper;
        this.userRepo = userRepo;
    }

    public ItemRequestResponceDto addItemRequest(Long userId, ItemRequestRequestDto itemRequestRequestDto) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user.get());
        ItemRequest addedItemRequest = itemRequestRepo.save(itemRequest);
        return itemRequestMapper.toItemRequestResponceDto(addedItemRequest);
    }

    public List<ItemRequestResponceDto> getItemRequests(Long userId, Integer from, Integer size) throws EntityNotFoundException, InvalidParameterException {
        Optional<User> user = userRepo.findById(userId);
        if (!user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        if (from == null && size == null) {
            List<ItemRequestResponceDto> itemRequestResponceDtos =  itemRequestRepo.getAllNotOwnRequests(userId).stream()
                    .map(i -> itemRequestMapper.toItemRequestResponceDto(i))
                    .collect(Collectors.toList());
            return itemRequestResponceDtos;
        }
        if ( from >= 0 && size > 0) {
            List<ItemRequestResponceDto> itemRequestResponceDtos =  itemRequestRepo.getAllNotOwnRequestsWithPagination(userId, from, size).stream()
                    .map(i -> itemRequestMapper.toItemRequestResponceDto(i))
                    .collect(Collectors.toList());
            return itemRequestResponceDtos;
        }
        else {
            throw new InvalidParameterException("Не верно заданы значения пагинации");
        }
    }

    public List<ItemRequestResponceDto> getOwnItemRequests(Long userId) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        List<ItemRequestResponceDto> itemRequestResponceDtos =  itemRequestRepo.getAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(i -> itemRequestMapper.toItemRequestResponceDto(i))
                .collect(Collectors.toList());
        return itemRequestResponceDtos;
    }

    public ItemRequestResponceDto getItemRequest(Long userId, long requestId) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        return itemRequestMapper.toItemRequestResponceDto(itemRequestRepo.getById(requestId));
    }

    public ItemRequestResponceDto updateItemRequest(Long userId, ItemRequestRequestDto itemRequest) {
        return null;
    }

    public ItemRequestResponceDto deleteItemRequest(Long userId, Long id) {
        return null;
    }

    public List<ItemRequestResponceDto> getItemRequests(Long userId, Long requestId) {
        return null;
    }


}

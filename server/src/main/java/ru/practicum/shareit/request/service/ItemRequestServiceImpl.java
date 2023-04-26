package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.*;
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ItemRequestServiceImpl {

    private final ItemRequestRepository itemRequestRepo;
    private final ItemRequestMapper itemRequestMapper;
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepo, ItemRequestMapper itemRequestMapper, UserRepository userRepo, ItemRepository itemRepo) {
        this.itemRequestRepo = itemRequestRepo;
        this.itemRequestMapper = itemRequestMapper;
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
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

    public List<OwnItemRequestResponceDto> getItemRequests(Long userId, Integer from, Integer size) throws EntityNotFoundException, InvalidParameterException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        List<ItemRequest> requests = itemRequestRepo.getAllNotOwnRequestsWithPagination(userId, from, size);
        List<Long> requestsIds = getItemRequestsIdsList(requests);
        Map<ItemRequest, List<Item>> itemsMap = itemRepo.findAllByRequestIdIn(requestsIds)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));

        List<OwnItemRequestResponceDto>  responceDto = new ArrayList<>();
        for (ItemRequest request : requests) {
            responceDto.add(OwnItemRequestResponceDto.builder()
                    .id(request.getId())
                    .description(request.getDescription())
                    .requester(request.getRequester())
                    .created(request.getCreated())
                    .items(toItemDtoList(itemsMap.getOrDefault(request, List.of())))
                    .build());
        }
        return responceDto;
    }

    private List<ItemDto> toItemDtoList(List<Item> items) {
        return items.stream()
                .map(i-> ItemRequestMapper.toItemDto(i))
                .collect(toList());
    }

    private List<Long> getItemRequestsIdsList(List<ItemRequest> requests) {
        return requests
                .stream()
                .map(ItemRequest::getId)
                .collect(toList());
    }

    public List<OwnItemRequestResponceDto> getOwnItemRequests(Long userId) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        List<OwnItemRequestResponceDto> itemRequestResponceDtos =  itemRequestRepo.getAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(i -> itemRequestMapper.toOwnItemRequestResponceDto(i, getItemDtos(i.getId())))
                .collect(toList());
        return itemRequestResponceDtos;
    }

    private List<ItemDto> getItemDtos(Long requestId) {
        return itemRepo.findAllByRequestId(requestId).stream()
                .map(i -> itemRequestMapper.toItemDto(i))
                .collect(toList());
    }

    public OwnItemRequestResponceDto getItemRequest(Long userId, long requestId) throws EntityNotFoundException {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Нет пользователя с id: " + userId);
        }
        Optional<ItemRequest> itemRequest = itemRequestRepo.findById(requestId);
        if (itemRequest.isEmpty()) {
            throw new EntityNotFoundException("Нет запроса с id: " + requestId);
        }
        return itemRequestMapper.toOwnItemRequestResponceDto(itemRequest.get(), getItemDtos(requestId));
    }
}

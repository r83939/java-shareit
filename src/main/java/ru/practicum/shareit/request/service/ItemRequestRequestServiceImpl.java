package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.List;

@Service
@Slf4j
public class ItemRequestRequestServiceImpl {

    private final ItemRequestRepository ItemRequestRepo;

    @Autowired
    public ItemRequestRequestServiceImpl(ItemRequestRepository itemRequestRepo) {
        ItemRequestRepo = itemRequestRepo;
    }

    public ItemRequestResponceDto addItemRequest(Long userId, ItemRequestRequestDto itemRequest) {
        return null;
    }

    public List<ItemRequestResponceDto> getItemRequests(Long userId) {
        return null;
    }

    public ItemRequestResponceDto updateItemRequest(Long userId, ItemRequestRequestDto itemRequest) {
        return null;
    }

    public ItemRequestResponceDto deleteItemRequest(Long userId, Long id) {
        return null;
    }
}

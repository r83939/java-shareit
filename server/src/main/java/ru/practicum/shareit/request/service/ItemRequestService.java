package ru.practicum.shareit.request.service;

import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.dto.OwnItemRequestResponceDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponceDto addItemRequest(Long userId, ItemRequestRequestDto itemRequestRequestDto) throws EntityNotFoundException;

    List<OwnItemRequestResponceDto> getItemRequests(Long userId, Integer from, Integer size) throws EntityNotFoundException, InvalidParameterException;

    List<OwnItemRequestResponceDto> getOwnItemRequests(Long userId) throws EntityNotFoundException;

    OwnItemRequestResponceDto getItemRequest(Long userId, long requestId) throws EntityNotFoundException;
}

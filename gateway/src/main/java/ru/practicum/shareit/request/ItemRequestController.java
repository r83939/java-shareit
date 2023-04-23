package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.domain.validator.Create;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@RequestMapping("/requests")
@Slf4j
@Validated
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader(USER_ID) Long userId,
            @Validated(Create.class)
            @RequestBody ItemRequestRequestDto requestDto) {
        log.info("ItemRequestGatewayController: createItemRequest implementation. User ID {}.", userId);
        return itemRequestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequest(
            @RequestHeader(USER_ID) Long userId,
            @PathVariable Long requestId) {
        log.info("ItemRequestGatewayController: getItemRequest implementation. User ID {}, request ID {}.",
                userId, requestId);
        return itemRequestClient.getItemRequest(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestByIdr(@RequestHeader(USER_ID) Long userId) {
        log.info("ItemRequestGatewayController: getItemRequestsByUser implementation. User ID {}.", userId);
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Object> getRequests(
            @RequestHeader(USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0")
            @PositiveOrZero Integer from,
            @RequestParam(value = "size", defaultValue = "10")
            @Positive Integer size) {
        log.info("ItemRequestGatewayController: getAllItemRequests implementation. User ID {}.", userId);
        return itemRequestClient.getAll(userId, from, size);
    }







}


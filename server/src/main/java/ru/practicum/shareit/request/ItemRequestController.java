package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.dto.OwnItemRequestResponceDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    public ItemRequestResponceDto addItemRequest(@RequestHeader(USER_ID) Long userId,
                                                           @Valid @RequestBody ItemRequestRequestDto itemRequest) throws EntityNotFoundException {

        return itemRequestService.addItemRequest(userId, itemRequest);
    }

    @GetMapping("/{requestId}")
    public OwnItemRequestResponceDto getItemRequest(@RequestHeader(USER_ID) Long userId,
                                                       @PathVariable long requestId) throws EntityNotFoundException {

        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping()
    public List<OwnItemRequestResponceDto> getOwnRequests(@RequestHeader(USER_ID) Long userId) throws EntityNotFoundException {

        return itemRequestService.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<OwnItemRequestResponceDto> getRequests(@RequestHeader(USER_ID) Long userId,
                                                    @RequestParam(value = "from", required = false) Integer from,
                                                    @RequestParam(value = "size", required = false) Integer size) throws EntityNotFoundException, InvalidParameterException {

        return itemRequestService.getItemRequests(userId, from, size);
    }
}

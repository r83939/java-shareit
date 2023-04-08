package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.dto.OwnItemRequestResponceDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    static final String USERID = "X-Sharer-User-Id";
    private final ItemRequestServiceImpl itemRequestServiceImpl;

    @Autowired
    public ItemRequestController(ItemRequestServiceImpl itemRequestServiceImpl) {
        this.itemRequestServiceImpl = itemRequestServiceImpl;
    }

    @PostMapping()
    public ItemRequestResponceDto createItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                           @Valid @RequestBody ItemRequestRequestDto itemRequest) throws EntityNotFoundException {

        return itemRequestServiceImpl.addItemRequest(userId, itemRequest);
    }

    @GetMapping("/{requestId}")
    public OwnItemRequestResponceDto getRequestById(@RequestHeader(USERID) Long userId,
                                                       @PathVariable long requestId) throws EntityNotFoundException {

        return itemRequestServiceImpl.getItemRequest(userId, requestId);
    }


    @GetMapping()
    public List<OwnItemRequestResponceDto> getOwnRequests(@RequestHeader(USERID) Long userId) throws EntityNotFoundException {

        return itemRequestServiceImpl.getOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<OwnItemRequestResponceDto> getRequests(@RequestHeader(USERID) Long userId,
                                                    @RequestParam(value = "from", required = false) Integer from,
                                                    @RequestParam(value = "size", required = false) Integer size) throws EntityNotFoundException, InvalidParameterException {

        return itemRequestServiceImpl.getItemRequests(userId, from, size);
    }

    @PatchMapping("/{id}")
    public ItemRequestResponceDto updateItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                           @PathVariable Long id,
                                                           @RequestBody @Valid ItemRequestRequestDto itemRequest) {

        return itemRequestServiceImpl.updateItemRequest(userId, itemRequest);
    }

    @DeleteMapping("/{id}")
    public ItemRequestResponceDto deleteItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                          @PathVariable Long id) {

        return itemRequestServiceImpl.deleteItemRequest(userId, id);
    }
}

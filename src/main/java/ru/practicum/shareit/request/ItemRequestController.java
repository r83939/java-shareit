package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponceDto;
import ru.practicum.shareit.request.service.ItemRequestRequestServiceImpl;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    static final String USERID = "X-Sharer-User-Id";
    private final ItemRequestRequestServiceImpl itemRequestRequestServiceImpl;

    @Autowired
    public ItemRequestController(ItemRequestRequestServiceImpl itemRequestRequestServiceImpl) {
        this.itemRequestRequestServiceImpl = itemRequestRequestServiceImpl;
    }

    @PostMapping()
    public ItemRequestResponceDto createItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                           @RequestBody @Valid ItemRequestRequestDto itemRequest) {

        return itemRequestRequestServiceImpl.addItemRequest(userId, itemRequest);
    }

    @GetMapping("/{id}")
    public List<ItemRequestResponceDto> getOwnRequests(@RequestHeader(USERID) Long userId,
                                                       @PathVariable long requestId) {

        return itemRequestRequestServiceImpl.getItemRequest(userId, requestId);
    }

    @GetMapping()
    public List<ItemRequestResponceDto> getOwnRequests(@RequestHeader(USERID) Long userId) {

        return itemRequestRequestServiceImpl.getItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponceDto> getRequests(@RequestHeader(USERID) Long userId,
                                                    @RequestParam(value = "from", required = true) Integer from,
                                                    @RequestParam(value = "size", required = true) Integer size) {

        return itemRequestRequestServiceImpl.getItemRequests(userId);
    }



    @PatchMapping("/{id}")
    public ItemRequestResponceDto updateItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                          @RequestBody @Valid ItemRequestRequestDto itemRequest) {

        return itemRequestRequestServiceImpl.updateItemRequest(userId, itemRequest);
    }

    @DeleteMapping("/{id}")
    public ItemRequestResponceDto deleteItemRequestRequest(@RequestHeader(USERID) Long userId,
                                                          @PathVariable Long id) {

        return itemRequestRequestServiceImpl.deleteItemRequest(userId, id);
    }
}

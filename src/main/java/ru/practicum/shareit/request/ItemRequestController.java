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
    private final ItemRequestRequestServiceImpl itemRequestRequestServiceImpl;

    @Autowired
    public ItemRequestController(ItemRequestRequestServiceImpl itemRequestRequestServiceImpl) {
        this.itemRequestRequestServiceImpl = itemRequestRequestServiceImpl;
    }

    @PostMapping()
    public ItemRequestResponceDto createItemRequestRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestBody @Valid ItemRequestRequestDto itemRequest) {

        return itemRequestRequestServiceImpl.addItemRequest(userId, itemRequest);
    }

    @GetMapping()
    public List<ItemRequestResponceDto> createUser(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemRequestRequestServiceImpl.getItemRequests(userId);
    }

    @PatchMapping("/{id}")
    public ItemRequestResponceDto updateItemRequestRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestBody @Valid ItemRequestRequestDto itemRequest) {

        return itemRequestRequestServiceImpl.updateItemRequest(userId, itemRequest);
    }

    @DeleteMapping("/{id}")
    public ItemRequestResponceDto deleteItemRequestRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable Long id) {

        return itemRequestRequestServiceImpl.deleteItemRequest(userId, id);
    }
}

package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingResponceDto getItem(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable long itemId) throws EntityNotFoundException {

        return itemService.getItemById(userId, itemId);
    }

    @GetMapping()
    public List<ItemWithBookingResponceDto> getAllItem(@RequestHeader(USER_ID) Long userId,
                                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size)  {

        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponceDto> searchItems(@RequestHeader(USER_ID) Long userId,
                                             @RequestParam(value = "text", required = true) String text,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) throws EntityNotFoundException {

        return itemService.searchItems(userId, text, from, size);
    }

    @PostMapping()
    public ItemResponceDto createItem(@RequestHeader(value = USER_ID, required = true) Long userId,
                              @Valid @RequestBody ItemRequestDto item) throws EntityNotFoundException {

        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemResponceDto updateItem(@RequestHeader(USER_ID) Long userId,
                              @PathVariable long id,
                              @RequestBody ItemRequestDto item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(id);
        return itemService.updateItem(userId, item);
    }




    @PostMapping("/{itemId}/comment")
    public CommentResponceDto addComment(@RequestHeader(USER_ID) long userId,
                                         @PathVariable long itemId,
                                         @Valid @RequestBody CommentRequestDto comment) throws EntityNotFoundException, InvalidParameterException {
        return itemService.addComment(userId, itemId, comment);
    }
}

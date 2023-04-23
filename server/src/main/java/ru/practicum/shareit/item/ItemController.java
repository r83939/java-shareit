package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidParameterException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingResponceDto getItem(@RequestHeader(USER_ID) Long userId,
                                              @PathVariable long itemId) throws EntityNotFoundException {

        return itemServiceImpl.getItemById(userId, itemId);
    }

    @GetMapping()
    public List<ItemWithBookingResponceDto> getAllItem(@RequestHeader(USER_ID) Long userId,
                                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return itemServiceImpl.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemResponceDto> searchItems(@RequestHeader(USER_ID) Long userId,
                                             @RequestParam(value = "text", required = true) String text,
                                             @RequestParam(value = "from", defaultValue = "0") Integer from,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) throws EntityNotFoundException {

        return itemServiceImpl.searchItems(userId, text, from, size);
    }

    @PostMapping()
    public ItemResponceDto createItem(@RequestHeader(value = USER_ID, required = true) Long userId,
                              @Valid @RequestBody ItemRequestDto item) throws EntityNotFoundException {

        return itemServiceImpl.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemResponceDto updateItem(@RequestHeader(USER_ID) Long userId,
                              @PathVariable long id,
                              @RequestBody ItemRequestDto item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(id);
        return itemServiceImpl.updateItem(userId, item);
    }




    @PostMapping("/{itemId}/comment")
    public CommentResponceDto addComment(@RequestHeader(USER_ID) long userId,
                                         @PathVariable long itemId,
                                         @Valid @RequestBody CommentRequestDto comment) throws EntityNotFoundException, InvalidParameterException {
        return itemServiceImpl.addComment(userId, itemId, comment);
    }
}

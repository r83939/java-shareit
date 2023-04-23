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
    static final String USERID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("/{id}")
    public ItemWithBookingResponceDto getItem(@RequestHeader(USERID) Long userId,
                                              @PathVariable long id) throws EntityNotFoundException {

        return itemServiceImpl.getItemById(userId, id);
    }

    @GetMapping()
    public List<ItemWithBookingResponceDto> getAllItem(@RequestHeader(USERID) Long userId) {

        return itemServiceImpl.getAllItemsByUserId(userId);
    }

    @PostMapping()
    public ItemResponceDto createItem(@RequestHeader(value = USERID, required = true) Long userId,
                              @Valid @RequestBody ItemRequestDto item) throws EntityNotFoundException {

        return itemServiceImpl.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemResponceDto updateItem(@RequestHeader(USERID) Long userId,
                              @PathVariable long id,
                              @RequestBody ItemRequestDto item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(id);
        return itemServiceImpl.updateItem(userId, item);
    }


    @GetMapping("/search")
    public List<ItemResponceDto> searchItems(@RequestHeader(USERID) Long userId,
                                     @RequestParam(value = "text", required = true) String text) throws EntityNotFoundException {

        return itemServiceImpl.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponceDto addComment(@RequestHeader(USERID) long userId,
                                         @PathVariable long itemId,
                                         @Valid @RequestBody CommentRequestDto comment) throws EntityNotFoundException, InvalidParameterException {
        return itemServiceImpl.addComment(userId, itemId, comment);
    }
}

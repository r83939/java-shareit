package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.CommentResponceDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponceDto;
import ru.practicum.shareit.item.dto.ItemWithBookingResponceDto;
import ru.practicum.shareit.item.service.CommentServiceImpl;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;
    private final CommentServiceImpl commentServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl, CommentServiceImpl commentServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
        this.commentServiceImpl = commentServiceImpl;
    }

    @GetMapping("/{id}")
    public ItemWithBookingResponceDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable long id) throws EntityNotFoundException {

        return itemServiceImpl.getItemById(userId, id);
    }

    @GetMapping()
    public List<ItemWithBookingResponceDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemServiceImpl.getAllItemsByUserId(userId);
    }

    @PostMapping()
    public ItemResponceDto createItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                              @Valid @RequestBody ItemRequestDto item) throws EntityNotFoundException {

        return itemServiceImpl.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemResponceDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable long id,
                              @RequestBody ItemRequestDto item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(id);
        return itemServiceImpl.updateItem(userId, item);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @PathVariable long itemId) {
        return "Sorry, the method has not been implemented yet!";
    }

    @GetMapping("/search")
    public List<ItemResponceDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(value = "text", required = true) String text) throws EntityNotFoundException {

        return itemServiceImpl.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponceDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable long itemId) {
        return commentServiceImpl.addComment(userId, itemId);
    }



}

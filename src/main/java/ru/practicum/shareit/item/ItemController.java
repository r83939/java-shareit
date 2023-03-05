package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id) throws EntityNotFoundException {

        return itemServiceImpl.getItemById(id);
    }

    @GetMapping()
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemServiceImpl.getAllItemsByUserId(userId);
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                              @Valid @RequestBody Item item) throws EntityNotFoundException {

        return itemServiceImpl.addItem(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable long id,
                              @RequestBody Item item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(id);
        return itemServiceImpl.updateItem(userId, item);
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @PathVariable long itemId) {
        return "Sorry, the method has not been implemented yet!";
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(value = "text", required = true) String text) throws EntityNotFoundException {

        return itemServiceImpl.searchItems(userId, text);
    }
}

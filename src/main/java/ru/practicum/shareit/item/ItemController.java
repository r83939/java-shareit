package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemServiceImpl itemServiceImpl;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl, ItemMapper itemMapper) {
        this.itemServiceImpl = itemServiceImpl;
        this.itemMapper = itemMapper;
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id) {

        return itemMapper.toItemDto(itemServiceImpl.getItemById(id));
    }

    @GetMapping()
    public List<ItemDto> getAllItem(@RequestHeader("X-Sharer-User-Id") Long userId) {

        return itemServiceImpl.getAllItemsByUserId(userId).stream()
                .map(u -> itemMapper.toItemDto(u))
                .collect(Collectors.toList());
    }

    @PostMapping()
    public ItemDto createItem(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
                              @Valid @RequestBody Item item) throws EntityNotFoundException {

        return itemMapper.toItemDto(itemServiceImpl.addItem(userId, item));
    }

    @PutMapping()
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable long itemId,
                              @RequestBody Item item) throws EntityNotFoundException, AccessDeniedException {

        item.setId(itemId);
        return itemMapper.toItemDto(itemServiceImpl.updateItem(userId, item));
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @PathVariable long itemId) {
        return "Sorry, the method has not been implemented yet!";
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam(value = "text", required = true) String text) throws EntityNotFoundException {
        return itemServiceImpl.searchItems(userId, text).stream()
                .map(u -> itemMapper.toItemDto(u))
                .collect(Collectors.toList());

    }

}

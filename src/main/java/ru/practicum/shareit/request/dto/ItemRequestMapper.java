package ru.practicum.shareit.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Component
public class ItemRequestMapper {
    public static ItemRequestResponceDto toItemRequestResponceDto(ItemRequest itemRequest) {
        return new ItemRequestResponceDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester(),
                itemRequest.getCreated()
        );
    }

    public static OwnItemRequestResponceDto toOwnItemRequestResponceDto(ItemRequest itemRequest, List<ItemDto> items) {
        return new OwnItemRequestResponceDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequester(),
                itemRequest.getCreated(),
                items
        );
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest().getId()
        );
    }
}

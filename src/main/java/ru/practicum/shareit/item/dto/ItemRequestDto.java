package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull(message = "поле available не должно быть пустым.")
    private Boolean available;
    private User owner;
    private long requestId;
}

package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BookingResponceDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;

//    public BookingResponceDto(long id, LocalDateTime start, LocalDateTime end, Item item, User booker, Status status) {
//        this.id = id;
//        this.start = start;
//        this.end = end;
//        this.item = item;
//        this.booker = booker;
//        this.status = status;
//    }
}

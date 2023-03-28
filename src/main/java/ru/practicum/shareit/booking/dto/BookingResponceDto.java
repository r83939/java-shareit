package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class BookingResponceDto {
    private long id;              // уникальный идентификатор бронирования
    private LocalDateTime start;           // дата и время начала бронирования
    private LocalDateTime end;             // дата и время конца бронирования
    private Item item;            // вещь, которую пользователь бронирует
    private User booker;          // пользователь, который осуществляет бронирование
    private Status status;


    public BookingResponceDto(long id, LocalDateTime start, LocalDateTime end, Item item, User booker, Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}

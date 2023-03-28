package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import java.util.Date;

@Getter
@Setter
public class BookingResponceDto {
    private long id;              // уникальный идентификатор бронирования

    private Date start;           // дата и время начала бронирования

    private Date end;             // дата и время конца бронирования

    private long item;            // вещь, которую пользователь бронирует

    private long booker;          // пользователь, который осуществляет бронирование

    private String status;

    public BookingResponceDto(long id, Date start, Date end, long item, long booker, String status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }

}

package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BookingRequestDto {

    private Date start;           // дата и время начала бронирования

    private Date end;             // дата и время конца бронирования

    private long itemId;            // вещь, которую пользователь бронирует

    private long booker;          // пользователь, который осуществляет бронирование


    public BookingRequestDto(Date start, Date end, long itemId, long booker) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
        this.booker = booker;
    }
}

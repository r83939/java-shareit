package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.domain.validator.BookingDateEmpty;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Getter
@Setter
public class BookingRequestDto {


    @Future
    @BookingDateEmpty(message = "Не указана дата начала бронирования")
    private Date start;           // дата и время начала бронирования

    @Future
    @BookingDateEmpty(message = "Не указана дата окончания бронирования")
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

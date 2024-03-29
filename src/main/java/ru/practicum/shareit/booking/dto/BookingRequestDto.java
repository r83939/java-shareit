package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.domain.validator.BookingDateEmpty;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {
    @Future
    @BookingDateEmpty(message = "Не указана дата начала бронирования")
    private LocalDateTime start;

    @Future
    @BookingDateEmpty(message = "Не указана дата окончания бронирования")
    private LocalDateTime end;

    private long itemId;

    private long booker;
}

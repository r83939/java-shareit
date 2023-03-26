package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialBookingDto {
    private long id;
    private long bookerId;

    public SpecialBookingDto(long id, long bookerId) {
        this.id = id;
        this.bookerId = bookerId;
    }

    public SpecialBookingDto() {

    }
}

package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    private long id;                      // уникальный идентификатор бронирования
    private Date start;                   // дата и время начала бронирования
    private Date end;                     // дата и время конца бронирования
    private Item item;                    // вещь, которую пользователь бронирует
    private User booker;                  // пользователь, который осуществляет бронирование
    private BookingStatus bookingStatus;  // статус бронирования
}

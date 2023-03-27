package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import ru.practicum.shareit.domain.validator.BookingDateEmpty;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "bookings", schema = "public")
@Getter @Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;              // уникальный идентификатор бронирования


    @Column(name="start_date")
    private LocalDateTime start;           // дата и время начала бронирования


    @Column(name="end_date")
    private LocalDateTime end;             // дата и время конца бронирования

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private Item item;            // вещь, которую пользователь бронирует

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id")
    private User booker;          // пользователь, который осуществляет бронирование

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;        // статус бронирования
}

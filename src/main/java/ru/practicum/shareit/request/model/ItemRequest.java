package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
public class ItemRequest {
    @Id
    @GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_REQUESTS",allocationSize=1)
    private long id;            // уникальный идентификатор запроса

    @Column(name = "description")
    private String description; // текст запроса, содержащий описание требуемой вещи

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "requestor_id")
    private User requester;

    @Column(name = "created")
    private Date created;       // дата и время создания запроса
}

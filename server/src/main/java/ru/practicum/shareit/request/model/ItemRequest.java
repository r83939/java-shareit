package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(generator = "ID_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "ID_SEQ", sequenceName = "SEQ_REQUESTS",allocationSize = 1)
    private long id;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "created")
    private LocalDateTime created;
}

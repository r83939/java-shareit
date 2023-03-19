package ru.practicum.shareit.item.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "comments", schema = "public")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Column(name = "message")
    private String message;

    @NotEmpty
    @Column(name = "user_id")
    private Long userId;

    @NotEmpty
    @Column(name = "item_id")
    private Long itemId;





}

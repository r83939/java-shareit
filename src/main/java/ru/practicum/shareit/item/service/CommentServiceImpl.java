package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentResponceDto;
import ru.practicum.shareit.item.repository.CommentRepository;

@Service
@Slf4j
public class CommentServiceImpl {
    private final CommentRepository commentRepo;

    public CommentServiceImpl(CommentRepository commentRepo) {
        this.commentRepo = commentRepo;
    }

    public CommentResponceDto addComment(Long userId, long itemId) {
        return null;

    }
}

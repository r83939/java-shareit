package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comments c WHERE c.item_id = :item_id",
            nativeQuery = true)
    List<Comment> findAllByItemId(@Param("item_id") Long id);
}

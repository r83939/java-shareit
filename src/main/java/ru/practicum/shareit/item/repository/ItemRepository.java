package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

   @Query(value = "SELECT i.user_id FROM items i where i.id = :id",
           nativeQuery = true)
   long findUserIdById(@Param("id") Long id);

   List<Item> findAllByUserIdAndNameContaining(@Param("id") Long id, String text);

   List<Item> findAllByUserId(@Param("id") Long id);



}
